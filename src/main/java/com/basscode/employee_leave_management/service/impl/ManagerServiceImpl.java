package com.basscode.employee_leave_management.service.impl;

import com.basscode.employee_leave_management.entity.Manager;
import com.basscode.employee_leave_management.exception.GlobalException;
import com.basscode.employee_leave_management.logging.AppLogger;
import com.basscode.employee_leave_management.repository.ManagerRepository;
import com.basscode.employee_leave_management.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository repository;

    @Override
    public Manager getManagerById(String id) {
        return repository.findById(id).orElseThrow(() -> GlobalException.notFoundException("manager not found"));
    }
}
