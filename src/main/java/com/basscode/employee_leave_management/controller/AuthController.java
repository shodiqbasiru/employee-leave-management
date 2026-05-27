package com.basscode.employee_leave_management.controller;

import com.basscode.employee_leave_management.dto.request.LoginRequest;
import com.basscode.employee_leave_management.dto.response.ApiResponse;
import com.basscode.employee_leave_management.dto.response.LoginResponse;
import com.basscode.employee_leave_management.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login dan mendapatkan JWT token")
public class AuthController {
    private final AuthService service;


    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login dengan email dan password, mendapatkan JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request){
        LoginResponse loginResponse = service.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login Success", loginResponse));
    }
}
