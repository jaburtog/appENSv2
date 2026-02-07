package com.ens.infrastructure.repository;

import com.ens.domain.entity.Role;
import com.ens.domain.repository.RoleRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class RoleRepositoryImpl implements RoleRepository {

    @PersistenceContext(unitName = "ens-pu")
    private EntityManager entityManager;

    @Override
    public Role save(Role role) {
        if (role.getId() == null) {
            entityManager.persist(role);
            return role;
        } else {
            return entityManager.merge(role);
        }
    }

    @Override
    public Optional<Role> findById(Long id) {
        Role role = entityManager.find(Role.class, id);
        return Optional.ofNullable(role);
    }

    @Override
    public Optional<Role> findByName(String name) {
        try {
            Role role = entityManager.createQuery(
                "SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name)
                .getSingleResult();
            return Optional.of(role);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class)
            .getResultList();
    }

    @Override
    public void delete(Long id) {
        Role role = entityManager.find(Role.class, id);
        if (role != null) {
            entityManager.remove(role);
        }
    }

    @Override
    public boolean existsByName(String name) {
        Long count = entityManager.createQuery(
            "SELECT COUNT(r) FROM Role r WHERE r.name = :name", Long.class)
            .setParameter("name", name)
            .getSingleResult();
        return count > 0;
    }
}
