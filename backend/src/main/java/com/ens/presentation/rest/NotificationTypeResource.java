package com.ens.presentation.rest;

import com.ens.application.dto.NotificationTypeDTO;
import com.ens.domain.entity.NotificationType;
import com.ens.domain.repository.NotificationTypeRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/notification-types")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificationTypeResource {

    @Inject
    private NotificationTypeRepository notificationTypeRepository;

    @GET
    public Response getAllNotificationTypes() {
        List<NotificationType> types = notificationTypeRepository.findAll();
        List<NotificationTypeDTO> typeDTOs = types.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(typeDTOs).build();
    }

    @GET
    @Path("/active")
    public Response getActiveNotificationTypes() {
        List<NotificationType> types = notificationTypeRepository.findAllActive();
        List<NotificationTypeDTO> typeDTOs = types.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(typeDTOs).build();
    }

    @GET
    @Path("/{id}")
    public Response getNotificationTypeById(@PathParam("id") Long id) {
        return notificationTypeRepository.findById(id)
            .map(type -> Response.ok(convertToDTO(type)).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createNotificationType(@Valid NotificationTypeDTO typeDTO) {
        NotificationType type = convertToEntity(typeDTO);
        NotificationType savedType = notificationTypeRepository.save(type);
        return Response.status(Response.Status.CREATED)
            .entity(convertToDTO(savedType))
            .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateNotificationType(@PathParam("id") Long id, @Valid NotificationTypeDTO typeDTO) {
        return notificationTypeRepository.findById(id)
            .map(existingType -> {
                updateEntityFromDTO(existingType, typeDTO);
                NotificationType updatedType = notificationTypeRepository.save(existingType);
                return Response.ok(convertToDTO(updatedType)).build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteNotificationType(@PathParam("id") Long id) {
        return notificationTypeRepository.findById(id)
            .map(type -> {
                notificationTypeRepository.delete(id);
                return Response.noContent().build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private NotificationTypeDTO convertToDTO(NotificationType type) {
        NotificationTypeDTO dto = new NotificationTypeDTO();
        dto.setId(type.getId());
        dto.setName(type.getName());
        dto.setDescription(type.getDescription());
        dto.setActive(type.getActive());
        dto.setCreatedAt(type.getCreatedAt());
        dto.setUpdatedAt(type.getUpdatedAt());
        return dto;
    }

    private NotificationType convertToEntity(NotificationTypeDTO dto) {
        NotificationType type = new NotificationType();
        type.setName(dto.getName());
        type.setDescription(dto.getDescription());
        if (dto.getActive() != null) {
            type.setActive(dto.getActive());
        }
        return type;
    }

    private void updateEntityFromDTO(NotificationType type, NotificationTypeDTO dto) {
        type.setName(dto.getName());
        type.setDescription(dto.getDescription());
        if (dto.getActive() != null) {
            type.setActive(dto.getActive());
        }
    }
}
