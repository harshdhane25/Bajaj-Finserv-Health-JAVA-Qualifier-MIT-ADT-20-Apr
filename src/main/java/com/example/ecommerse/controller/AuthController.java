package com.example.ecommerse.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @GetMapping("/auth/me")
    public Map<String, Object> currentUser(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "authorities", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                "authenticated", authentication.isAuthenticated()
        );
    }

    @GetMapping("/debug/headers")
    public Map<String, Object> inspectHeaders(
            Authentication authentication,
            @RequestHeader(value = "X-Trace-Id", required = false) String traceId,
            @RequestHeader(value = "X-Test-Mode", required = false) String testMode,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @RequestHeader(value = "User-Agent", required = false) String userAgent
    ) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("username", authentication.getName());
        response.put("traceId", traceId);
        response.put("testMode", testMode);
        response.put("correlationId", correlationId);
        response.put("userAgent", userAgent);
        return response;
    }
}
