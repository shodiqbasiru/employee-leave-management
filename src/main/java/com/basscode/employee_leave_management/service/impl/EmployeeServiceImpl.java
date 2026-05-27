package com.basscode.employee_leave_management.service.impl;

import com.basscode.employee_leave_management.entity.Employee;
import com.basscode.employee_leave_management.exception.GlobalException;
import com.basscode.employee_leave_management.repository.EmployeeRepository;
import com.basscode.employee_leave_management.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;
    @Override
    public Employee getEmployeeById(String id) {
        return repository.findById(id).orElseThrow(()-> GlobalException.notFoundException("Employee not found"));
    }
}
