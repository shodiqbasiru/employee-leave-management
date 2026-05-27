DROP TABLE IF EXISTS leave_requests;
DROP TABLE IF EXISTS users;

-- users
CREATE TABLE users
(
    id         VARCHAR(36) PRIMARY KEY,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL CHECK (role IN ('EMPLOYEE', 'MANAGER')),
    is_enable  BOOLEAN      NOT NULL DEFAULT TRUE,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP             DEFAULT NOW()
);

-- Departments
CREATE TABLE departments
(
    id              VARCHAR(36) PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL
);

-- Managers (profile data)
CREATE TABLE managers
(
    id           VARCHAR(36) PRIMARY KEY,
    user_id      VARCHAR(36)  NOT NULL UNIQUE REFERENCES users (id),
    manager_name VARCHAR(100) NOT NULL,
    created_at   TIMESTAMP DEFAULT NOW()
);

-- Employees (profile data)
CREATE TABLE employees
(
    id            VARCHAR(36) PRIMARY KEY,
    user_id       VARCHAR(36)  NOT NULL UNIQUE REFERENCES users (id),
    employee_name VARCHAR(100) NOT NULL,
    leave_quota   INT          NOT NULL DEFAULT 12,
    manager_id    VARCHAR(36) REFERENCES managers (id),
    department_id VARCHAR(36) REFERENCES departments (id),
    created_at    TIMESTAMP             DEFAULT NOW()
);

-- Leave Requests
CREATE TABLE leave_requests
(
    id          VARCHAR(36) PRIMARY KEY,
    user_id     VARCHAR(36) NOT NULL REFERENCES users (id),
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
    reason      TEXT        NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    reviewed_by VARCHAR(36) REFERENCES users (id),
    reviewed_at TIMESTAMP,
    created_at  TIMESTAMP            DEFAULT NOW()
);