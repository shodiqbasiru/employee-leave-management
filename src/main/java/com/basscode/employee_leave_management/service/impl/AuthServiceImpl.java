package com.basscode.employee_leave_management.service.impl;

import com.basscode.employee_leave_management.dto.request.LoginRequest;
import com.basscode.employee_leave_management.dto.response.LoginResponse;
import com.basscode.employee_leave_management.entity.User;
import com.basscode.employee_leave_management.exception.GlobalException;
import com.basscode.employee_leave_management.repository.UserRepository;
import com.basscode.employee_leave_management.security.JwtUtil;
import com.basscode.employee_leave_management.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil               jwtUtil;


    @Override
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        User user = (User) authenticate.getPrincipal();
        if (!user.getIsEnable()) {
            throw GlobalException.forbiddenException("your account is not active");
        }

        String email = user.getUsername();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String jwtToken = jwtUtil.generateToken(user);

        return LoginResponse.builder()
                .email(email)
                .roles(roles)
                .token(jwtToken)
                .build();
    }
}
