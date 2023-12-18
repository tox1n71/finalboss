package ru.itmo.finalboss.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.itmo.finalboss.models.Role;

import java.util.Collection;
import java.util.List;
@Builder
@AllArgsConstructor
@Entity
public class UserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<PointEntity> points;

    @Enumerated(EnumType.STRING)
    private Role role;
    public UserEntity() {
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<PointEntity> getPoints() {
        return points;
    }

    public void setPoints(List<PointEntity> points) {
        this.points = points;
    }

    public Long getId() {
        return id;
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

}
