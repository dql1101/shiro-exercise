package com.dql.shiro.domain;

import lombok.Data;

import java.util.List;

@Data
public class User {

    public User (String username, String password, boolean available, String role, List<String> permissions) {
        this.username = username;
        this.password = password;
        this.available = available;
        this.role = role;
        this.permissions = permissions;
    }

    private String username;

    private String password;

    private boolean available;

    private String role;

    private List<String> permissions;

}
