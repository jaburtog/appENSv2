package com.ens.domain.repository;

import com.ens.domain.entity.NotificationType;
import java.util.List;
import java.util.Optional;

public interface NotificationTypeRepository {
    NotificationType save(NotificationType notificationType);
    Optional<NotificationType> findById(Long id);
    Optional<NotificationType> findByName(String name);
    List<NotificationType> findAll();
    List<NotificationType> findAllActive();
    void delete(Long id);
}
