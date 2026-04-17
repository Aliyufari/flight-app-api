package com.afgicafe.flight.service.impl;

import com.afgicafe.flight.domain.entity.Role;
import com.afgicafe.flight.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class RoleServiceImpl implements RoleService {
    @Override
    public Page<Role> getRoles(Pageable pageable) {
        return null;
    }
}
