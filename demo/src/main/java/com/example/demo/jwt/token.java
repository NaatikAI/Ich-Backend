package com.example.demo.jwt;

import com.example.demo.entity.SessionEntity;
import com.example.demo.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "token")
public class token {
    @Id
    @GeneratedValue
    @Column(name = "idToken")
    public Integer id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    public UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "idSession") // Nombre de la columna en la tabla de tokens que enlaza con la sesión
    private SessionEntity session;

}
