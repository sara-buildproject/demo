package com.buildsessions.stockmarketanalyzer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class CustomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String roles; // "USER,ADMIN"
    private String monitoredStockIds;

    public CustomUser(Long id, String username, String password, String roles, String monitoredStockIds) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.monitoredStockIds = monitoredStockIds;
    }

    public CustomUser() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Set<Long> getMonitoredStockIds() {
        if(monitoredStockIds == null || monitoredStockIds.isEmpty()) {
            return new HashSet<>();
        }
        return Stream.of(monitoredStockIds.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    public void setMonitoredStockIds(Set<Long> monitoredStockIds) {
        if(monitoredStockIds == null || monitoredStockIds.isEmpty()) {
            this.monitoredStockIds = "";
        } else {
            this.monitoredStockIds = monitoredStockIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CustomUser that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(roles, that.roles) && Objects.equals(monitoredStockIds, that.monitoredStockIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, roles, monitoredStockIds);
    }
}
