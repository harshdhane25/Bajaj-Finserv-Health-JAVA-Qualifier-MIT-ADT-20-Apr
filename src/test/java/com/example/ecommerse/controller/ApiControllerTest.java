package com.example.ecommerse.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldAllowPublicHealthWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/public/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(header().exists("X-Trace-Id"));
    }

    @Test
    void shouldRejectProtectedEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Ecommerse API\""));
    }

    @Test
    void shouldReturnCurrentUserForAuthenticatedRequest() throws Exception {
        mockMvc.perform(get("/api/auth/me")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("tester", "test123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tester"))
                .andExpect(jsonPath("$.authenticated").value(true));
    }

    @Test
    void shouldExposeDebugHeaders() throws Exception {
        mockMvc.perform(get("/api/debug/headers")
                        .header("X-Trace-Id", "trace-postman-123")
                        .header("X-Test-Mode", "postman")
                        .header("X-Correlation-Id", "corr-789")
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("tester", "test123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.traceId").value("trace-postman-123"))
                .andExpect(jsonPath("$.testMode").value("postman"))
                .andExpect(jsonPath("$.correlationId").value("corr-789"))
                .andExpect(header().string("X-Trace-Id", "trace-postman-123"));
    }

    @Test
    void shouldAllowAdminToCreateProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content("""
                                {
                                  "name": "Laptop Stand",
                                  "description": "Adjustable aluminum stand",
                                  "category": "Office",
                                  "price": 39.99,
                                  "stock": 18
                                }
                                """)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "admin123")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop Stand"));
    }
}
