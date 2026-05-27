package com.basscode.employee_leave_management.repository;

import com.basscode.employee_leave_management.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,String> {
}
