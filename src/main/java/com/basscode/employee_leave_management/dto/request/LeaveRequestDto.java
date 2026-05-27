package com.basscode.employee_leave_management.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDto {
    @NotNull(message = "Tanggal mulai tidak boleh kosong")
    @Future(message = "Tanggal mulai harus di masa depan")
    private LocalDate startDate;

    @NotNull(message = "Tanggal selesai tidak boleh kosong")
    @Future(message = "Tanggal selesai harus di masa depan")
    private LocalDate endDate;

    @NotBlank(message = "Alasan cuti tidak boleh kosong")
    private String reason;
}
