package com.basscode.employee_leave_management.service;

import com.basscode.employee_leave_management.dto.request.LoginRequest;
import com.basscode.employee_leave_management.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
}
