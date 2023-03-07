package com.mka.springsecurity2v.model;

//ограничения доступа к функционалу приложения, затем надо связать роль с ограничением
public enum Permission {
    DEVELOPERS_READ("developers:read"),
    DEVELOPERS_WRITE("developers:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
