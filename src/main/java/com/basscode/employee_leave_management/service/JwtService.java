package com.basscode.employee_leave_management.service;

import com.basscode.employee_leave_management.entity.User;

public interface JwtService {
    String generateToken(User user);
    boolean verifyJwtToken(String token);
    void getClaimsByToken(String token);
}
