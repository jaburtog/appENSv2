package com.ens.domain.repository;

import com.ens.domain.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Role save(Role role);
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);
    List<Role> findAll();
    void delete(Long id);
    boolean existsByName(String name);
}
