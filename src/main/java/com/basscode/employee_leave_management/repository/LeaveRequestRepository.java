package com.basscode.employee_leave_management.repository;

import com.basscode.employee_leave_management.entity.LeaveRequest;
import com.basscode.employee_leave_management.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, String> {
    @Query("""
            SELECT COUNT(lr) > 0
            FROM LeaveRequest lr
              WHERE lr.user.id = :userId
                     AND lr.status = :status
                     AND lr.startDate <= :endDate
                     AND lr.endDate   >= :startDate
            
            """)
    boolean existsOverlappingLeave(
            @Param("userId") String userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status") LeaveStatus status
    );

    @Query(value = """
            SELECT COALESCE(SUM(
                (lr.end_date - lr.start_date) + 1
            ), 0)
            FROM leave_requests lr
            WHERE lr.user_id = :userId
              AND lr.status = 'APPROVED'
              AND EXTRACT(YEAR FROM lr.start_date) = :year
            """, nativeQuery = true)
    int getTotalApprovedLeaveDays(
            @Param("userId") String userId,
            @Param("year") int year
    );

    Page<LeaveRequest> findByUserId(String userId, Pageable pageable);

    List<LeaveRequest> findByUserId(String userId);

    Page<LeaveRequest> findAll(Pageable pageable);

    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);

}
