package com.afgicafe.flight.service;

import com.afgicafe.flight.dto.response.RoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    Page<RoleResponse> getRoles (Pageable pageable);
}
