package br.com.municipio.vacinas.vacinas_api.model;

import br.com.municipio.vacinas.vacinas_api.model.enums.Role;

import org.springframework.data.annotation.Id;

import lombok.*;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="usuarios")
public class Usuario implements UserDetails {

    @Id
    private String id;

    private String localId;

    @Field(name="full_name")
    private String nome;

    @Indexed(unique = true)
    private String email;

    @Field(name="password")
    private String senha;

    @Field(name="birth_date")
    private LocalDate dataNscto;

    @Indexed(unique = true)
    private String cpf;

    @Field(name="role")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public String getPassword() {
        return senha;
    }

}
