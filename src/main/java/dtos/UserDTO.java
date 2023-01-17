package dtos;

import entities.User;

import java.util.List;

public class UserDTO {

    int id;
    String name;
    List<String> roles;


    public UserDTO(User user) {
        this.name = user.getName();
        this.roles = user.getRolesAsStrings();
        this.id = user.getId();
    }

    public List<String> getRoles() {
        return roles;
    }

    public int getId() {return id;}
}
