package com.ens.presentation.rest;

import com.ens.application.dto.UserDTO;
import com.ens.domain.entity.User;
import com.ens.domain.entity.Role;
import com.ens.domain.repository.UserRepository;
import com.ens.domain.repository.RoleRepository;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private RoleRepository roleRepository;

    @GET
    public Response getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOs = users.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        return Response.ok(userDTOs).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        return userRepository.findById(id)
            .map(user -> Response.ok(convertToDTO(user)).build())
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createUser(@Valid UserDTO userDTO) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return Response.status(Response.Status.CONFLICT)
                .entity("{\"error\":\"Username already exists\"}")
                .build();
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return Response.status(Response.Status.CONFLICT)
                .entity("{\"error\":\"Email already exists\"}")
                .build();
        }

        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return Response.status(Response.Status.CREATED)
            .entity(convertToDTO(savedUser))
            .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, @Valid UserDTO userDTO) {
        return userRepository.findById(id)
            .map(existingUser -> {
                // Check if username is being changed and if it already exists
                if (!existingUser.getUsername().equals(userDTO.getUsername()) &&
                    userRepository.existsByUsername(userDTO.getUsername())) {
                    return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\":\"Username already exists\"}")
                        .build();
                }
                // Check if email is being changed and if it already exists
                if (!existingUser.getEmail().equals(userDTO.getEmail()) &&
                    userRepository.existsByEmail(userDTO.getEmail())) {
                    return Response.status(Response.Status.CONFLICT)
                        .entity("{\"error\":\"Email already exists\"}")
                        .build();
                }

                updateEntityFromDTO(existingUser, userDTO);
                User updatedUser = userRepository.save(existingUser);
                return Response.ok(convertToDTO(updatedUser)).build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        return userRepository.findById(id)
            .map(user -> {
                userRepository.delete(id);
                return Response.noContent().build();
            })
            .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setActive(user.getActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        if (user.getRoles() != null) {
            dto.setRoleIds(user.getRoles().stream()
                .map(Role::getId)
                .collect(Collectors.toSet()));
        }
        return dto;
    }

    private User convertToEntity(UserDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword("defaultPassword"); // In real app, this should be hashed
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }
        return user;
    }

    private void updateEntityFromDTO(User user, UserDTO dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }
    }
}
