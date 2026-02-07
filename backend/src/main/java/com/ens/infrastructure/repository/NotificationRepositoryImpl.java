package com.ens.infrastructure.repository;

import com.ens.domain.entity.Notification;
import com.ens.domain.repository.NotificationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class NotificationRepositoryImpl implements NotificationRepository {

    @PersistenceContext(unitName = "ens-pu")
    private EntityManager entityManager;

    @Override
    public Notification save(Notification notification) {
        if (notification.getId() == null) {
            entityManager.persist(notification);
            return notification;
        } else {
            return entityManager.merge(notification);
        }
    }

    @Override
    public Optional<Notification> findById(Long id) {
        Notification notification = entityManager.find(Notification.class, id);
        return Optional.ofNullable(notification);
    }

    @Override
    public List<Notification> findAll() {
        return entityManager.createQuery("SELECT n FROM Notification n", Notification.class)
            .getResultList();
    }

    @Override
    public List<Notification> findByStatus(String status) {
        return entityManager.createQuery(
            "SELECT n FROM Notification n WHERE n.status = :status", Notification.class)
            .setParameter("status", status)
            .getResultList();
    }

    @Override
    public List<Notification> findByTypeId(Long typeId) {
        return entityManager.createQuery(
            "SELECT n FROM Notification n WHERE n.type.id = :typeId", Notification.class)
            .setParameter("typeId", typeId)
            .getResultList();
    }

    @Override
    public void delete(Long id) {
        Notification notification = entityManager.find(Notification.class, id);
        if (notification != null) {
            entityManager.remove(notification);
        }
    }
}
