package com.basscode.employee_leave_management.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AppLogger {
    public static final Logger STREAM   = LoggerFactory.getLogger("STREAM_LOGGER");
    public static final Logger TRACE    = LoggerFactory.getLogger("TRACE_LOGGER");
    public static final Logger DATABASE = LoggerFactory.getLogger("DATABASE_LOGGER");

    public static final Logger ERROR   = LoggerFactory.getLogger("ERROR_LOGGER");
    public static final Logger WARNING = LoggerFactory.getLogger("WARNING_LOGGER");
}