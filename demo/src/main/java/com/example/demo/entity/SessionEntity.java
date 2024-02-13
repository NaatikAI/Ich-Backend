package com.example.demo.entity;

import com.example.demo.jwt.token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sessions")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSession")
    private int id;

    @ManyToOne // Definimos una relación Many-to-One
    @JoinColumn(name = "idUsuario") // Columna de unión en la tabla de sesiones
    private UserEntity user;

    @Column(name = "start_time")
    private ZonedDateTime startTime;

    @Column(name = "end_time")
    private ZonedDateTime  endTime;

    @OneToMany(mappedBy = "session")
    private List<token> tokens;
}
