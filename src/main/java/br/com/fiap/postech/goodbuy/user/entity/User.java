package br.com.fiap.postech.goodbuy.user.entity;

import br.com.fiap.postech.goodbuy.security.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "login", nullable = false)
    private String login;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "cpf", nullable = false)
    private String cpf;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    private UserRole role;

    public User() {
    }

    public User(String login, String name, String cpf, String password, UserRole role) {
        this();
        this.login = login;
        this.name = name;
        this.cpf = cpf;
        this.password = password;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
