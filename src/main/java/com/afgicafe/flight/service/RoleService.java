package com.afgicafe.flight.service;

import com.afgicafe.flight.domain.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    Page<Role> getRoles (Pageable pageable);
}
