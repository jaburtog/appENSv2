package com.ens.infrastructure.repository;

import com.ens.domain.entity.NotificationType;
import com.ens.domain.repository.NotificationTypeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class NotificationTypeRepositoryImpl implements NotificationTypeRepository {

    @PersistenceContext(unitName = "ens-pu")
    private EntityManager entityManager;

    @Override
    public NotificationType save(NotificationType notificationType) {
        if (notificationType.getId() == null) {
            entityManager.persist(notificationType);
            return notificationType;
        } else {
            return entityManager.merge(notificationType);
        }
    }

    @Override
    public Optional<NotificationType> findById(Long id) {
        NotificationType notificationType = entityManager.find(NotificationType.class, id);
        return Optional.ofNullable(notificationType);
    }

    @Override
    public Optional<NotificationType> findByName(String name) {
        try {
            NotificationType notificationType = entityManager.createQuery(
                "SELECT nt FROM NotificationType nt WHERE nt.name = :name", NotificationType.class)
                .setParameter("name", name)
                .getSingleResult();
            return Optional.of(notificationType);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<NotificationType> findAll() {
        return entityManager.createQuery("SELECT nt FROM NotificationType nt", NotificationType.class)
            .getResultList();
    }

    @Override
    public List<NotificationType> findAllActive() {
        return entityManager.createQuery(
            "SELECT nt FROM NotificationType nt WHERE nt.active = true", NotificationType.class)
            .getResultList();
    }

    @Override
    public void delete(Long id) {
        NotificationType notificationType = entityManager.find(NotificationType.class, id);
        if (notificationType != null) {
            entityManager.remove(notificationType);
        }
    }
}
