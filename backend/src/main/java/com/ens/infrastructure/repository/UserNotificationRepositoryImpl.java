package com.ens.infrastructure.repository;

import com.ens.domain.entity.UserNotification;
import com.ens.domain.repository.UserNotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class UserNotificationRepositoryImpl implements UserNotificationRepository {

    @PersistenceContext(unitName = "ens-pu")
    private EntityManager entityManager;

    @Override
    public UserNotification save(UserNotification userNotification) {
        if (userNotification.getId() == null) {
            entityManager.persist(userNotification);
            return userNotification;
        } else {
            return entityManager.merge(userNotification);
        }
    }

    @Override
    public Optional<UserNotification> findById(Long id) {
        UserNotification userNotification = entityManager.find(UserNotification.class, id);
        return Optional.ofNullable(userNotification);
    }

    @Override
    public List<UserNotification> findAll() {
        return entityManager.createQuery("SELECT un FROM UserNotification un", UserNotification.class)
            .getResultList();
    }

    @Override
    public List<UserNotification> findByUserId(Long userId) {
        return entityManager.createQuery(
            "SELECT un FROM UserNotification un WHERE un.user.id = :userId", UserNotification.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public List<UserNotification> findByNotificationId(Long notificationId) {
        return entityManager.createQuery(
            "SELECT un FROM UserNotification un WHERE un.notification.id = :notificationId", UserNotification.class)
            .setParameter("notificationId", notificationId)
            .getResultList();
    }

    @Override
    public List<UserNotification> findUnreadByUserId(Long userId) {
        return entityManager.createQuery(
            "SELECT un FROM UserNotification un WHERE un.user.id = :userId AND un.read = false", UserNotification.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    @Override
    public void delete(Long id) {
        UserNotification userNotification = entityManager.find(UserNotification.class, id);
        if (userNotification != null) {
            entityManager.remove(userNotification);
        }
    }
}
