package com.inventory.app.models.dto.mapper;

import com.inventory.app.models.dto.UserDto;
import com.inventory.app.models.entities.User;

public class DtoMapperUser {
    private User user;

    private DtoMapperUser() {

    }

    public static DtoMapperUser builder() {
        return new DtoMapperUser();
    }

    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDto build() {

        if (user == null) {
            throw new RuntimeException("Debe pasar la entidad User");
        }

        // Verifica si el usuario tiene los roles de MANAGER o ADMIN
        boolean isManager = user.getRoles().stream().anyMatch(r -> "ROLE_MANAGER".equals(r.getName()));
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));

        // Devuelve una nueva instancia de UserDto con los datos mapeados
        return new UserDto(this.user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                isManager,
                isAdmin);

    }

}
