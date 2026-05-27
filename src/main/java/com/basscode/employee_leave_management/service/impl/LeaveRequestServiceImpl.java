package com.basscode.employee_leave_management.service.impl;

import com.basscode.employee_leave_management.dto.request.LeaveRequestDto;
import com.basscode.employee_leave_management.dto.response.LeaveResponse;
import com.basscode.employee_leave_management.entity.Employee;
import com.basscode.employee_leave_management.entity.LeaveRequest;
import com.basscode.employee_leave_management.entity.Manager;
import com.basscode.employee_leave_management.entity.User;
import com.basscode.employee_leave_management.enums.LeaveStatus;
import com.basscode.employee_leave_management.exception.GlobalException;
import com.basscode.employee_leave_management.repository.LeaveRequestRepository;
import com.basscode.employee_leave_management.service.EmployeeService;
import com.basscode.employee_leave_management.service.LeaveRequestService;
import com.basscode.employee_leave_management.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository repository;
    private final EmployeeService employeeService;
    private final ManagerService managerService;

    @Override
    public LeaveResponse createLeave(User currentUser, LeaveRequestDto requestDto) {
        if (requestDto.getStartDate().isAfter(requestDto.getEndDate())) {
            throw GlobalException.badRequestException("Tanggal mulai tidak boleh lebih besar dari tanggal selesai");
        }

        Employee employee = employeeService.getEmployeeById(currentUser.getEmployee().getId());

        boolean hasOverlap = repository.existsOverlappingLeave(
                currentUser.getId(),
                requestDto.getStartDate(),
                requestDto.getEndDate(),
                LeaveStatus.APPROVED
        );
        if (hasOverlap) {
            throw GlobalException.badRequestException(
                    "Tanggal cuti overlap dengan cuti yang sudah disetujui"
            );
        }

        // validate quota
        int year = requestDto.getStartDate().getYear();
        int usedDays = repository.getTotalApprovedLeaveDays(currentUser.getId(), year);
        int requestedDays = (int) (requestDto.getEndDate().toEpochDay() - requestDto.getStartDate().toEpochDay() + 1);
        int quota = employee.getLeaveQuota();

        if (usedDays + requestedDays > quota) {
            throw GlobalException.badRequestException(
                    String.format("Quota cuti tidak mencukupi. Sisa: %d hari, Diajukan: %d hari",
                            quota - usedDays, requestedDays)
            );
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .user(currentUser)
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .reason(requestDto.getReason())
                .status(LeaveStatus.PENDING)
                .build();
        repository.saveAndFlush(leaveRequest);

        return toLeaveResponse(leaveRequest, employee);
    }

    @Override
    public Page<LeaveResponse> getLeaveHistory(User currentUser, Pageable pageable) {
        Employee employee = employeeService.getEmployeeById(currentUser.getEmployee().getId());

        return repository.findByUserId(currentUser.getId(), pageable)
                .map(lr -> toLeaveResponse(lr, employee));
    }

    @Override
    public Page<LeaveResponse> getAllLeaves(Pageable pageable) {

        return repository.findAll(pageable).map(lr -> {
            Employee emp = employeeService.getEmployeeById(lr.getUser().getEmployee().getId());
            return toLeaveResponse(lr, emp);
        });
    }

    @Override
    public LeaveResponse approveLeave(User currentUser, String id) {
        return processReviewLeaveRequest(currentUser,id,LeaveStatus.APPROVED);
    }

    @Override
    public LeaveResponse rejectLeave(User currentUser, String id) {
        return processReviewLeaveRequest(currentUser,id,LeaveStatus.REJECTED);
    }

    private LeaveResponse processReviewLeaveRequest(User manager, String lrId, LeaveStatus leaveStatus) {
        LeaveRequest leaveRequest = repository.findById(lrId).orElseThrow(() ->
                GlobalException.notFoundException("leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw GlobalException.badRequestException("Only PENDING status can be process");
        }

        leaveRequest.setStatus(leaveStatus);
        leaveRequest.setReviewedBy(manager);
        leaveRequest.setReviewedAt(LocalDateTime.now());
        repository.saveAndFlush(leaveRequest);

        Employee employee = employeeService.getEmployeeById(leaveRequest.getUser().getEmployee().getId());
        return toLeaveResponse(leaveRequest,employee);
    }

    private LeaveResponse toLeaveResponse(LeaveRequest lr, Employee employee) {
        String employeeName = employee != null ? employee.getEmployeeName() : "Unknown";

        String reviewerName = null;
        if (lr.getReviewedBy() != null) {
            Manager reviewer = managerService
                    .getManagerById(lr.getReviewedBy().getManager().getId());
            reviewerName = reviewer != null ? reviewer.getManagerName() : null;
        }

        return LeaveResponse.builder()
                .id(lr.getId())
                .employeeName(employeeName)
                .startDate(lr.getStartDate())
                .endDate(lr.getEndDate())
                .durationDays(lr.getDurationDays())
                .reason(lr.getReason())
                .status(lr.getStatus())
                .reviewedBy(reviewerName)
                .reviewedAt(lr.getReviewedAt())
                .createdAt(lr.getCreatedAt())
                .build();
    }

}
