//package com.afgicafe.flight.security;
//
//import com.afgicafe.flight.security.JwtAuthFilter;
//import com.afgicafe.flight.utils.ApiResponse;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//class SecurityTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    /**
//     * Disable real JWT filter so we can control auth scenarios cleanly
//     */
//    @MockitoBean
//    private JwtAuthFilter jwtAuthFilter;
//
//    // ===============================
//    // 1. PUBLIC ENDPOINT ACCESS
//    // ===============================
//
//    @Test
//    void shouldAllowAccessToRegisterEndpoint() throws Exception {
//
//        String request = """
//        {
//            "first_name": "John",
//            "last_name": "Doe",
//            "email": "john@email.com",
//            "phone_number": "08012345678",
//            "password": "Password@123"
//        }
//        """;
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void shouldAllowAccessToLoginEndpoint() throws Exception {
//
//        String request = """
//        {
//            "email": "john@email.com",
//            "password": "Password@123"
//        }
//        """;
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isOk());
//    }
//
//    // ===============================
//    // 2. PROTECTED ENDPOINTS (NO AUTH)
//    // ===============================
//
//    @Test
//    void shouldRejectAccessToProtectedEndpointWithoutAuth() throws Exception {
//
//        mockMvc.perform(get("/api/v1/users")) // assume protected endpoint
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void shouldRejectRefreshWithoutToken() throws Exception {
//
//        String request = """
//        {
//            "refresh_token": ""
//        }
//        """;
//
//        mockMvc.perform(post("/api/v1/auth/refresh")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isBadRequest());
//    }
//
//    // ===============================
//    // 3. INVALID ROUTES SECURITY
//    // ===============================
//
//    @Test
//    void shouldReturnNotFoundForUnknownRoute() throws Exception {
//
//        mockMvc.perform(get("/api/v1/admin/unknown"))
//                .andExpect(status().isNotFound());
//    }
//
//    // ===============================
//    // 4. METHOD SECURITY (if enabled later)
//    // ===============================
//
//    @Test
//    void shouldBlockDeleteUserEndpointWithoutAuth() throws Exception {
//
//        mockMvc.perform(delete("/api/v1/users/1"))
//                .andExpect(status().isUnauthorized());
//    }
//}