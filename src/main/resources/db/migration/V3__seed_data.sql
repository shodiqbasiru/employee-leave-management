-- ─── DEPARTMENTS ─────────────────────────────────────────────────────────────
INSERT INTO departments (id, department_name) VALUES
                                                  ('dept-001', 'Engineering'),
                                                  ('dept-002', 'Human Resource'),
                                                  ('dept-003', 'Finance');

-- ─── USERS ───────────────────────────────────────────────────────────────────
-- Password semua: password123
-- BCrypt hash dari 'password123'
INSERT INTO users (id, email, password, role, is_enable, created_at) VALUES
                                                                         -- Managers
                                                                         ('user-mgr-001', 'budi.santoso@example.com',   '$2a$12$vLa4oUe48DGttgM3.0bF1OGGZ/lLgZTBmsB5PGoPufcaIj8OfP546', 'MANAGER',  true, NOW()),
                                                                         ('user-mgr-002', 'siti.rahayu@example.com',    '$2a$12$vLa4oUe48DGttgM3.0bF1OGGZ/lLgZTBmsB5PGoPufcaIj8OfP546', 'MANAGER',  true, NOW()),
                                                                         -- Employees
                                                                         ('user-emp-001', 'andi.wijaya@example.com',    '$2a$12$vLa4oUe48DGttgM3.0bF1OGGZ/lLgZTBmsB5PGoPufcaIj8OfP546', 'EMPLOYEE', true, NOW()),
                                                                         ('user-emp-002', 'dewi.kusuma@example.com',    '$2a$12$vLa4oUe48DGttgM3.0bF1OGGZ/lLgZTBmsB5PGoPufcaIj8OfP546', 'EMPLOYEE', true, NOW()),
                                                                         ('user-emp-003', 'riko.pratama@example.com',   '$2a$12$vLa4oUe48DGttgM3.0bF1OGGZ/lLgZTBmsB5PGoPufcaIj8OfP546', 'EMPLOYEE', true, NOW()),
                                                                         -- Disabled user (untuk test is_enable = false)
                                                                         ('user-emp-004', 'inactive.user@example.com',  '$2a$12$vLa4oUe48DGttgM3.0bF1OGGZ/lLgZTBmsB5PGoPufcaIj8OfP546', 'EMPLOYEE', false, NOW());

-- ─── MANAGERS ────────────────────────────────────────────────────────────────
INSERT INTO managers (id, user_id, manager_name, created_at) VALUES
                                                                 ('mgr-001', 'user-mgr-001', 'Budi Santoso', NOW()),
                                                                 ('mgr-002', 'user-mgr-002', 'Siti Rahayu',  NOW());

-- ─── EMPLOYEES ───────────────────────────────────────────────────────────────
INSERT INTO employees (id, user_id, employee_name, leave_quota, manager_id, department_id, created_at) VALUES
                                                                                                           ('emp-001', 'user-emp-001', 'Andi Wijaya',   12, 'mgr-001', 'dept-001', NOW()),
                                                                                                           ('emp-002', 'user-emp-002', 'Dewi Kusuma',   12, 'mgr-001', 'dept-002', NOW()),
                                                                                                           ('emp-003', 'user-emp-003', 'Riko Pratama',  12, 'mgr-002', 'dept-003', NOW()),
                                                                                                           ('emp-004', 'user-emp-004', 'Inactive User', 12, 'mgr-002', 'dept-001', NOW());

-- ─── LEAVE REQUESTS ──────────────────────────────────────────────────────────
-- Berbagai skenario untuk pengetesan semua validasi

-- APPROVED: Andi sudah pakai 5 hari (untuk test sisa quota)
INSERT INTO leave_requests (id, user_id, start_date, end_date, reason, status, reviewed_by, reviewed_at, created_at) VALUES
    ('lr-001', 'user-emp-001', '2025-01-06', '2025-01-10', 'Liburan keluarga',    'APPROVED', 'user-mgr-001', NOW(), NOW());

-- APPROVED: Dewi sudah pakai 3 hari
INSERT INTO leave_requests (id, user_id, start_date, end_date, reason, status, reviewed_by, reviewed_at, created_at) VALUES
    ('lr-002', 'user-emp-002', '2025-02-03', '2025-02-05', 'Urusan keluarga',     'APPROVED', 'user-mgr-001', NOW(), NOW());

-- PENDING: Andi punya 1 request pending (untuk test approve/reject)
INSERT INTO leave_requests (id, user_id, start_date, end_date, reason, status, reviewed_by, reviewed_at, created_at) VALUES
    ('lr-003', 'user-emp-001', '2025-08-04', '2025-08-06', 'Pernikahan saudara',  'PENDING',  NULL, NULL, NOW());

-- PENDING: Riko punya request pending
INSERT INTO leave_requests (id, user_id, start_date, end_date, reason, status, reviewed_by, reviewed_at, created_at) VALUES
    ('lr-004', 'user-emp-003', '2025-09-01', '2025-09-03', 'Istirahat sakit',     'PENDING',  NULL, NULL, NOW());

-- REJECTED: Dewi pernah direject
INSERT INTO leave_requests (id, user_id, start_date, end_date, reason, status, reviewed_by, reviewed_at, created_at) VALUES
    ('lr-005', 'user-emp-002', '2025-03-10', '2025-03-12', 'Keperluan pribadi',   'REJECTED', 'user-mgr-001', NOW(), NOW());