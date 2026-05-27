-- Table: users (employee & manager)
CREATE TABLE users
(
    id          VARCHAR PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL CHECK (role IN ('EMPLOYEE', 'MANAGER')),
    leave_quota INT          NOT NULL DEFAULT 12,
    created_at  TIMESTAMP             DEFAULT NOW()
);

-- Table: leave_requests
CREATE TABLE leave_requests
(
    id          VARCHAR PRIMARY KEY,
    user_id     VARCHAR      NOT NULL REFERENCES users (id),
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
    reason      TEXT        NOT NULL,
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED')),
    reviewed_by VARCHAR REFERENCES users (id),
    reviewed_at TIMESTAMP,
    created_at  TIMESTAMP            DEFAULT NOW()
);