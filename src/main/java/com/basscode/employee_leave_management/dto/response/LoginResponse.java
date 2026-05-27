package com.basscode.employee_leave_management.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class LoginResponse {
    private String email;
    private String token;
    private List<String> roles;
}
