package com.afgicafe.flight.controller;

import com.afgicafe.flight.dto.response.RoleResponse;
import com.afgicafe.flight.service.RoleService;
import com.afgicafe.flight.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Roles")
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService service;

    @Operation(
            summary = "Paginated role list",
            description = "Paginated role list route"
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('view:role')")
    public ResponseEntity<ApiResponse<Page<RoleResponse>>> index (Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK,
                        "Role retrieved successfully",
                        service.getRoles(pageable)
                )
        );
    }
}
