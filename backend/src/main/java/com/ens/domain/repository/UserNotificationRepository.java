package com.ens.domain.repository;

import com.ens.domain.entity.UserNotification;
import java.util.List;
import java.util.Optional;

public interface UserNotificationRepository {
    UserNotification save(UserNotification userNotification);
    Optional<UserNotification> findById(Long id);
    List<UserNotification> findAll();
    List<UserNotification> findByUserId(Long userId);
    List<UserNotification> findByNotificationId(Long notificationId);
    List<UserNotification> findUnreadByUserId(Long userId);
    void delete(Long id);
}
