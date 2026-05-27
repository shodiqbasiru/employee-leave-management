package com.basscode.employee_leave_management.dto.request;

import lombok.Data;

@Data
public class PaginationRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String startDate;
    private String endDate;
}
