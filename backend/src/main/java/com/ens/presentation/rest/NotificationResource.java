package com.ens.presentation.rest;

import com.ens.application.dto.NotificationDTO;
import com.ens.domain.entity.Notification;
import com.ens.domain.entity.NotificationType;
import com.ens.domain.repository.NotificationRepository;
import com.ens.domain.repository.NotificationTypeRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/notifications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    private NotificationRepository notificationRepository;

    @Inject
    private NotificationTypeRepository notificationTypeRepository;

    @GET
    public Response getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> notificationDTOs = notifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(notificationDTOs).build();
    }

    @GET
    @Path("/{id}")
    public Response getNotificationById(@PathParam("id") Long id) {
        return notificationRepository.findById(id)
            .map(notification -> Response.ok(convertToDTO(notification)).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/status/{status}")
    public Response getNotificationsByStatus(@PathParam("status") String status) {
        List<Notification> notifications = notificationRepository.findByStatus(status);
        List<NotificationDTO> notificationDTOs = notifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(notificationDTOs).build();
    }

    @GET
    @Path("/type/{typeId}")
    public Response getNotificationsByType(@PathParam("typeId") Long typeId) {
        List<Notification> notifications = notificationRepository.findByTypeId(typeId);
        List<NotificationDTO> notificationDTOs = notifications.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(notificationDTOs).build();
    }

    @POST
    public Response createNotification(@Valid NotificationDTO notificationDTO) {
        NotificationType type = notificationTypeRepository.findById(notificationDTO.getTypeId())
            .orElseThrow(() -> new WebApplicationException("Notification type not found", Response.Status.BAD_REQUEST));

        Notification notification = convertToEntity(notificationDTO, type);
        Notification savedNotification = notificationRepository.save(notification);
        return Response.status(Response.Status.CREATED)
            .entity(convertToDTO(savedNotification))
            .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateNotification(@PathParam("id") Long id, @Valid NotificationDTO notificationDTO) {
        return notificationRepository.findById(id)
            .map(existingNotification -> {
                NotificationType type = notificationTypeRepository.findById(notificationDTO.getTypeId())
                    .orElseThrow(() -> new WebApplicationException("Notification type not found", Response.Status.BAD_REQUEST));

                updateEntityFromDTO(existingNotification, notificationDTO, type);
                Notification updatedNotification = notificationRepository.save(existingNotification);
                return Response.ok(convertToDTO(updatedNotification)).build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteNotification(@PathParam("id") Long id) {
        return notificationRepository.findById(id)
            .map(notification -> {
                notificationRepository.delete(id);
                return Response.noContent().build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setTypeId(notification.getType().getId());
        dto.setTypeName(notification.getType().getName());
        dto.setPriority(notification.getPriority());
        dto.setScheduledAt(notification.getScheduledAt());
        dto.setSentAt(notification.getSentAt());
        dto.setStatus(notification.getStatus());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setUpdatedAt(notification.getUpdatedAt());
        return dto;
    }

    private Notification convertToEntity(NotificationDTO dto, NotificationType type) {
        Notification notification = new Notification();
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setType(type);
        if (dto.getPriority() != null) {
            notification.setPriority(dto.getPriority());
        }
        if (dto.getScheduledAt() != null) {
            notification.setScheduledAt(dto.getScheduledAt());
        }
        if (dto.getStatus() != null) {
            notification.setStatus(dto.getStatus());
        }
        return notification;
    }

    private void updateEntityFromDTO(Notification notification, NotificationDTO dto, NotificationType type) {
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setType(type);
        if (dto.getPriority() != null) {
            notification.setPriority(dto.getPriority());
        }
        if (dto.getScheduledAt() != null) {
            notification.setScheduledAt(dto.getScheduledAt());
        }
        if (dto.getStatus() != null) {
            notification.setStatus(dto.getStatus());
        }
    }
}
