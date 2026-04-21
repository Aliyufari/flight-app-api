package com.afgicafe.flight.unit;

import com.afgicafe.flight.domain.entity.Role;
import com.afgicafe.flight.domain.enums.RoleEnum;
import com.afgicafe.flight.dto.response.RoleResponse;
import com.afgicafe.flight.mapper.RoleMapper;
import com.afgicafe.flight.repository.RoleRepository;
import com.afgicafe.flight.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository repository;

    @Mock
    private RoleMapper mapper;

    @InjectMocks
    private RoleServiceImpl service;

    @Test
    void shouldReturnAllRoles() {

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        Role role1 = new Role();
        role1.setId(id1);
        role1.setName(RoleEnum.ADMIN);

        Role role2 = new Role();
        role2.setId(id2);
        role2.setName(RoleEnum.CUSTOMER);

        RoleResponse response1 = RoleResponse.builder()
                .id(id1)
                .name(RoleEnum.ADMIN)
                .permissions(Set.of())
                .build();

        RoleResponse response2 = RoleResponse.builder()
                .id(id2)
                .name(RoleEnum.CUSTOMER)
                .permissions(Set.of())
                .build();

        when(repository.findAll()).thenReturn(List.of(role1, role2));
        when(mapper.toResponse(role1)).thenReturn(response1);
        when(mapper.toResponse(role2)).thenReturn(response2);

        List<RoleResponse> result = service.getRoles();

        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(repository).findAll();
        verify(mapper).toResponse(role1);
        verify(mapper).toResponse(role2);
    }
}