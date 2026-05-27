package com.basscode.employee_leave_management.service;

import com.basscode.employee_leave_management.dto.request.LeaveRequestDto;
import com.basscode.employee_leave_management.dto.response.LeaveResponse;
import com.basscode.employee_leave_management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LeaveRequestService {
    LeaveResponse createLeave(User currentUser, LeaveRequestDto requestDto);
    Page<LeaveResponse> getLeaveHistory(User currentUser, Pageable pageable);

    Page<LeaveResponse> getAllLeaves(Pageable pageable);

    LeaveResponse approveLeave(User currentUser, String id);

    LeaveResponse rejectLeave(User currentUser, String id);
}
