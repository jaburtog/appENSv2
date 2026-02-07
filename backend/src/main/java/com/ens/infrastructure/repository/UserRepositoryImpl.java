package com.ens.infrastructure.repository;

import com.ens.domain.entity.User;
import com.ens.domain.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext(unitName = "ens-pu")
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            entityManager.persist(user);
            return user;
        } else {
            return entityManager.merge(user);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            User user = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try {
            User user = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return entityManager.createQuery("SELECT u FROM User u", User.class)
            .getResultList();
    }

    @Override
    public void delete(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        Long count = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
            .setParameter("username", username)
            .getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        Long count = entityManager.createQuery(
            "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
            .setParameter("email", email)
            .getSingleResult();
        return count > 0;
    }
}
