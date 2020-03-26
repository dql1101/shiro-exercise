package com.dql.shiro.domain;

import lombok.Data;

@Data
public class User {

    public User (String username, String password, boolean available) {
        this.username = username;
        this.password = password;
        this.available = available;
    }

    private String username;

    private String password;

    private boolean available;

}
