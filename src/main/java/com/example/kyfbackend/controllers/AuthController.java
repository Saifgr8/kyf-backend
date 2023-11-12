package com.example.kyfbackend.controllers;

import com.example.kyfbackend.models.Role;
import com.example.kyfbackend.models.User;
import com.example.kyfbackend.repos.RolesRepository;
import com.example.kyfbackend.repos.UserRepository;
import com.example.kyfbackend.security.*;
import com.example.kyfbackend.security.payloads.request.LoginRequest;
import com.example.kyfbackend.security.payloads.request.ResetPasswordRequest;
import com.example.kyfbackend.security.payloads.request.SignupRequest;
import com.example.kyfbackend.security.payloads.response.MessageResponse;
import com.example.kyfbackend.security.payloads.response.UserInfoResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RolesRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JavaMailSender javaMailSender;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setId(userDetails.getId());
        userInfoResponse.setUsername(userDetails.getUsername());
        userInfoResponse.setEmail(userDetails.getEmail());
        userInfoResponse.setRoles(roles);
        userInfoResponse.setWeight(userDetails.getWeight());
        userInfoResponse.setHeight(userDetails.getHeight());
        userInfoResponse.setAge(userDetails.getAge());


        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(userInfoResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        user.setAge((int) signUpRequest.getAge());
        user.setHeight(signUpRequest.getHeight());
        user.setWeight(signUpRequest.getWeight());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;

                    default:
                        Role userRole = roleRepository.findByName(ERole.USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    @PostMapping("/forgotpassword")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User not found with email: " + email));
        } else {
            String token = UUID.randomUUID().toString();
            user.setResetPasswordToken(token);
            userRepository.save(user);
            try {
                sendEmailToUser(user, token);
            } catch (Exception e) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email not sent!"));
            }
            return ResponseEntity.ok(new MessageResponse("Email sent successfully!"));
        }
    }

    private void sendEmailToUser(User user, String token) throws MessagingException {
        String url = "http://localhost:3000/resetpassword?token=" + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        String text = "<h3>Hi, " + user.getUsername() + ".</h3>" +
                "<p>You have requested to reset your password.</p>" +
                "<p>Click the link below to reset your password</p>" +
                "<a href=\"" + url + "\">Reset Password</a>";
        helper.setTo(user.getEmail());
        helper.setFrom("kyf.services1@gmail.com");
        helper.setSubject("Reset Password");
        boolean html = true;
        helper.setText(text, html);
        javaMailSender.send(message);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        User user = userRepository.findByResetPasswordToken(resetPasswordRequest.getToken());
        if (user == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Invalid token!"));
        } else {
            user.setPassword(encoder.encode(resetPasswordRequest.getPassword()));
            user.setResetPasswordToken(null);
            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("Password reset successfully!"));
        }
    }
}
