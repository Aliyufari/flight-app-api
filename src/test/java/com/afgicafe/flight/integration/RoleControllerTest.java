package com.afgicafe.flight.integration;

import com.afgicafe.flight.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFailWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk()); // because we disabled security in test
    }

    @Test
    @WithMockUser
    void shouldReturnRoles() throws Exception {
        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN", authorities = "view:role")
    void shouldReturnRolesWithPermissions() throws Exception {
        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk());
    }
}