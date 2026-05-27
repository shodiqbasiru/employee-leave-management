package com.basscode.employee_leave_management.dto.response;

import com.basscode.employee_leave_management.enums.LeaveStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class LeaveResponse {
    private String      id;
    private String      employeeName;
    private LocalDate   startDate;
    private LocalDate endDate;
    private int         durationDays;
    private String      reason;
    private LeaveStatus status;
    private String      reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
}
