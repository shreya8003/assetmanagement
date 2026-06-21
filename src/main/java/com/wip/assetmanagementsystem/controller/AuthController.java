package com.wip.assetmanagementsystem.controller;

import com.wip.assetmanagementsystem.dto.RegisterRequest;
import com.wip.assetmanagementsystem.entity.Employee;
import com.wip.assetmanagementsystem.repository.EmployeeRepository;
import com.wip.assetmanagementsystem.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

            String role;
            if ("admin".equals(username)) {
                role = "ADMIN";
            } else {
                Employee emp = employeeRepository.findByUsername(username);
                role = (emp != null && emp.getRole() != null) ? emp.getRole() : "USER";
            }

            String token = jwtUtil.generateToken(username);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", role);
            response.put("username", username);
            return response;
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        if ("admin".equals(request.getUsername())) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Username not available"));
        }
        if (employeeRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "Username already taken"));
        }
        Employee employee = employeeRepository.findByEmail(request.getEmail());
        if (employee == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "No employee found with this email. Please contact your admin."));
        }
        if (employee.getUsername() != null) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "An account already exists for this email."));
        }
        employee.setUsername(request.getUsername());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole("USER");
        employeeRepository.save(employee);
        return ResponseEntity.ok(Map.of("message", "Account created successfully"));
    }
}
