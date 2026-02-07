package com.ens.domain.repository;

import com.ens.domain.entity.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(Long id);
    List<Notification> findAll();
    List<Notification> findByStatus(String status);
    List<Notification> findByTypeId(Long typeId);
    void delete(Long id);
}
