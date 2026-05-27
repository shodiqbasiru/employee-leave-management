package com.basscode.employee_leave_management.controller;

import com.basscode.employee_leave_management.dto.request.LeaveRequestDto;
import com.basscode.employee_leave_management.dto.response.ApiResponse;
import com.basscode.employee_leave_management.dto.response.LeaveResponse;
import com.basscode.employee_leave_management.entity.User;
import com.basscode.employee_leave_management.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
@Tag(name = "Leave Management", description = "Pengajuan dan manajemen cuti karyawan")
@SecurityRequirement(name = "bearerAuth")
public class LeaveController {
    private final LeaveRequestService service;

    @PostMapping
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @Operation(summary = "Ajukan cuti", description = "Employee mengajukan cuti baru")
    public ResponseEntity<ApiResponse<LeaveResponse>> createLeave(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody LeaveRequestDto request
    ) {
        LeaveResponse response = service.createLeave(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Leave request successfully created",response));
    }

    @GetMapping("/my-leaves")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @Operation(summary = "Histori cuti saya", description = "Employee melihat histori cutinya sendiri")
    public ResponseEntity<ApiResponse<Page<LeaveResponse>>> getMyLeaves(
            @AuthenticationPrincipal User currentUser,
            @PageableDefault(sort = "createdAt") Pageable pageable) {

        Page<LeaveResponse> response = service.getLeaveHistory(currentUser, pageable);
        return ResponseEntity.ok(ApiResponse.success("Leave history successfully retrieved", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Semua data cuti", description = "Manager melihat seluruh data cuti semua employee")
    public ResponseEntity<ApiResponse<Page<LeaveResponse>>> getAllLeaves(
            @PageableDefault(sort = "createdAt") Pageable pageable) {

        Page<LeaveResponse> response = service.getAllLeaves(pageable);
        return ResponseEntity.ok(ApiResponse.success("Data cuti berhasil diambil", response));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Approve cuti", description = "Manager menyetujui pengajuan cuti")
    public ResponseEntity<ApiResponse<LeaveResponse>> approveLeave(
            @AuthenticationPrincipal User currentUser,
            @PathVariable String id) {

        LeaveResponse response = service.approveLeave(currentUser, id);
        return ResponseEntity.ok(ApiResponse.success("Cuti berhasil disetujui", response));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('MANAGER')")
    @Operation(summary = "Reject cuti", description = "Manager menolak pengajuan cuti")
    public ResponseEntity<ApiResponse<LeaveResponse>> rejectLeave(
            @AuthenticationPrincipal User currentUser,
            @PathVariable String id) {

        LeaveResponse response = service.rejectLeave(currentUser, id);
        return ResponseEntity.ok(ApiResponse.success("Cuti berhasil ditolak", response));
    }
}
