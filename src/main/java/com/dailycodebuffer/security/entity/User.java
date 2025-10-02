package com.dailycodebuffer.security.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    private Integer idl;

    private String userName;

    private String password;

    public Integer getIdl() {
        return idl;
    }

    public void setIdl(Integer idl) {
        this.idl = idl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }

    public User(Integer idl, String userName, String password) {
        this.idl = idl;
        this.userName = userName;
        this.password = password;
    }
}
