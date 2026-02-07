package com.ens.presentation.rest;

import com.ens.application.dto.RoleDTO;
import com.ens.domain.entity.Role;
import com.ens.domain.repository.RoleRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleResource {

    @Inject
    private RoleRepository roleRepository;

    @GET
    public Response getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> roleDTOs = roles.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(roleDTOs).build();
    }

    @GET
    @Path("/{id}")
    public Response getRoleById(@PathParam("id") Long id) {
        return roleRepository.findById(id)
            .map(role -> Response.ok(convertToDTO(role)).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createRole(@Valid RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.getName())) {
            return Response.status(Response.Status.CONFLICT)
                .entity("{\"error\":\"Role name already exists\"}")
                .build();
        }

        Role role = convertToEntity(roleDTO);
        Role savedRole = roleRepository.save(role);
        return Response.status(Response.Status.CREATED)
            .entity(convertToDTO(savedRole))
            .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateRole(@PathParam("id") Long id, @Valid RoleDTO roleDTO) {
        return roleRepository.findById(id)
            .map(existingRole -> {
                if (!existingRole.getName().equals(roleDTO.getName()) &&
                    roleRepository.existsByName(roleDTO.getName())) {
                    return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\":\"Role name already exists\"}")
                        .build();
                }

                updateEntityFromDTO(existingRole, roleDTO);
                Role updatedRole = roleRepository.save(existingRole);
                return Response.ok(convertToDTO(updatedRole)).build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteRole(@PathParam("id") Long id) {
        return roleRepository.findById(id)
            .map(role -> {
                roleRepository.delete(id);
                return Response.noContent().build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private RoleDTO convertToDTO(Role role) {
        RoleDTO dto = new RoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());
        return dto;
    }

    private Role convertToEntity(RoleDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        return role;
    }

    private void updateEntityFromDTO(Role role, RoleDTO dto) {
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
    }
}
