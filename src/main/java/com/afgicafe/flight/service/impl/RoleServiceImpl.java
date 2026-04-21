package com.afgicafe.flight.service.impl;

import com.afgicafe.flight.dto.response.RoleResponse;
import com.afgicafe.flight.mapper.RoleMapper;
import com.afgicafe.flight.repository.RoleRepository;
import com.afgicafe.flight.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;
    private final RoleMapper mapper;

    @Override
    public List<RoleResponse> getRoles() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
