package com.ens.presentation.rest;

import com.ens.application.dto.UserNotificationDTO;
import com.ens.domain.entity.UserNotification;
import com.ens.domain.entity.User;
import com.ens.domain.entity.Notification;
import com.ens.domain.repository.UserNotificationRepository;
import com.ens.domain.repository.UserRepository;
import com.ens.domain.repository.NotificationRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/user-notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserNotificationResource {

    @Inject
    private UserNotificationRepository userNotificationRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private NotificationRepository notificationRepository;

    @GET
    public Response getAllUserNotifications() {
        List<UserNotification> userNotifications = userNotificationRepository.findAll();
        List<UserNotificationDTO> dtos = userNotifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserNotificationById(@PathParam("id") Long id) {
        return userNotificationRepository.findById(id)
            .map(un -> Response.ok(convertToDTO(un)).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserNotificationsByUserId(@PathParam("userId") Long userId) {
        List<UserNotification> userNotifications = userNotificationRepository.findByUserId(userId);
        List<UserNotificationDTO> dtos = userNotifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("/user/{userId}/unread")
    public Response getUnreadUserNotifications(@PathParam("userId") Long userId) {
        List<UserNotification> userNotifications = userNotificationRepository.findUnreadByUserId(userId);
        List<UserNotificationDTO> dtos = userNotifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @POST
    public Response createUserNotification(@Valid UserNotificationDTO dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new WebApplicationException("User not found", Response.Status.BAD_REQUEST));
        
        Notification notification = notificationRepository.findById(dto.getNotificationId())
            .orElseThrow(() -> new WebApplicationException("Notification not found", Response.Status.BAD_REQUEST));

        UserNotification userNotification = convertToEntity(dto, user, notification);
        UserNotification saved = userNotificationRepository.save(userNotification);
        return Response.status(Response.Status.CREATED)
            .entity(convertToDTO(saved))
            .build();
    }

    @PUT
    @Path("/{id}/mark-read")
    public Response markAsRead(@PathParam("id") Long id) {
        return userNotificationRepository.findById(id)
            .map(userNotification -> {
                userNotification.setRead(true);
                userNotification.setReadAt(LocalDateTime.now());
                UserNotification updated = userNotificationRepository.save(userNotification);
                return Response.ok(convertToDTO(updated)).build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUserNotification(@PathParam("id") Long id) {
        return userNotificationRepository.findById(id)
            .map(un -> {
                userNotificationRepository.delete(id);
                return Response.noContent().build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private UserNotificationDTO convertToDTO(UserNotification userNotification) {
        UserNotificationDTO dto = new UserNotificationDTO();
        dto.setId(userNotification.getId());
        dto.setUserId(userNotification.getUser().getId());
        dto.setUsername(userNotification.getUser().getUsername());
        dto.setNotificationId(userNotification.getNotification().getId());
        dto.setNotificationTitle(userNotification.getNotification().getTitle());
        dto.setRead(userNotification.getRead());
        dto.setReadAt(userNotification.getReadAt());
        dto.setDeliveredAt(userNotification.getDeliveredAt());
        dto.setCreatedAt(userNotification.getCreatedAt());
        return dto;
    }

    private UserNotification convertToEntity(UserNotificationDTO dto, User user, Notification notification) {
        UserNotification userNotification = new UserNotification();
        userNotification.setUser(user);
        userNotification.setNotification(notification);
        if (dto.getRead() != null) {
            userNotification.setRead(dto.getRead());
        }
        if (dto.getDeliveredAt() != null) {
            userNotification.setDeliveredAt(dto.getDeliveredAt());
        }
        return userNotification;
    }
}
