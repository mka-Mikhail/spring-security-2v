package com.mka.springsecurity2v.model;

public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private Role role;
    private Status status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserDto() {
    }

    public UserDto(Long idl, String email, String password, String first_name, String last_name, Role role, Status status) {
        this.id = idl;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.role = role;
        this.status = status;
    }

    public UserDto(Long idl, String email, String password, String first_name, String last_name) {
        this.id = idl;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
