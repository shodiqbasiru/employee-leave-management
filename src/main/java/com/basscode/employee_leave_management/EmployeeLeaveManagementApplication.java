package com.basscode.employee_leave_management;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
@SecurityScheme(
		name        = "bearerAuth",
		type        = SecuritySchemeType.HTTP,
		scheme      = "bearer",
		bearerFormat = "JWT",
		in          = SecuritySchemeIn.HEADER,
		description = "Masukkan JWT token hasil login"
)
public class EmployeeLeaveManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeLeaveManagementApplication.class, args);
	}

}
