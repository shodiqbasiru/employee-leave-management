package com.basscode.employee_leave_management.logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Set;

public class BodyMaskingUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Set<String> SENSITIVE_FIELDS = Set.of(
            "password",
            "token",
            "accessToken",
            "refreshToken",
            "authorization",
            "secret",
            "apiKey"
    );

    private static final String MASK = "***MASKED***";

    /**
     * Mask field sensitif dari JSON string.
     * Jika bukan JSON valid, kembalikan string asli tanpa modifikasi.
     */
    public static String mask(String body) {
        if (body == null || body.isBlank() || !body.trim().startsWith("{")) {
            return body;
        }

        try {
            JsonNode node = MAPPER.readTree(body);
            maskNode((ObjectNode) node);
            return MAPPER.writeValueAsString(node);
        } catch (Exception e) {
            return body;
        }
    }

    private static void maskNode(ObjectNode node) {
        node.fieldNames().forEachRemaining(field -> {
            boolean isSensitive = SENSITIVE_FIELDS.stream()
                    .anyMatch(s -> s.equalsIgnoreCase(field));

            if (isSensitive) {
                node.put(field, MASK);
            } else if (node.get(field).isObject()) {
                maskNode((ObjectNode) node.get(field));
            }
        });
    }
}