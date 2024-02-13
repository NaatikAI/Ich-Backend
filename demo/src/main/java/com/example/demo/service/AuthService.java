package com.example.demo.service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.NewUser;
import com.example.demo.entity.PersonEntity;
import com.example.demo.entity.SessionEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enums.RolName;
import com.example.demo.jwt.TokenRepository;
import com.example.demo.jwt.TokenType;
import com.example.demo.jwt.token;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.SessionRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private EntityManager entityManager;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final SessionRepository sessionRepository;
    private final SessionService sessionService;


    public Optional<UserEntity> getByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public AuthResponse addnewUser(NewUser request){
        UserEntity user = UserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode( request.getPassword()))
                .phone(request.getPhone())
                .role(RolName.ROLE_USER_INTERMEDIO)
                .build();
        PersonEntity user1 = PersonEntity.builder()
                .username(request.getUsername())
                .apellidoPa(request.getApellidoPa())
                .apellidoMa(request.getApellidoMa())
                .fechaNacimiento(request.getFechaNacimiento())
                .build();
        user.setPersonEntity(user1);
        var saveUser = userRepository.save(user);
        personRepository.save(user1);
        var jwtToken = jwtService.generateToken(user);
        // Crear una nueva sesión
        SessionEntity newSession = new SessionEntity();
        newSession.setUser(user);
        ZoneId zonaHorariaSistema2 = ZoneId.systemDefault();
        ZonedDateTime horaActual = ZonedDateTime.now(zonaHorariaSistema2);

        newSession.setStartTime(horaActual);

        sessionRepository.save(newSession);
        saveUserToken(saveUser, jwtToken,newSession);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()));

            // La autenticación fue exitosa, proceder a generar el token
            UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken(user);

            // Crear una nueva sesión
            SessionEntity newSession = new SessionEntity();
            newSession.setUser(user);

            // Obtener la hora actual en la zona horaria del sistema
            ZonedDateTime horaActual = ZonedDateTime.now(ZoneId.systemDefault());
            System.out.println("Hora actual en la zona horaria del sistema: " + horaActual);

            // Convertir la hora a UTC antes de guardarla
            newSession.setStartTime(horaActual);


            sessionRepository.save(newSession);

            // Asociar el ID de sesión al token
            token token = new token();
            token.setSession(newSession); // Asociar el token con la sesión

            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken,newSession);
            return AuthResponse.builder()
                    .token(jwtToken)
                    .build();
        } catch (BadCredentialsException ex) {
            // La contraseña no coincide
            return new AuthResponse(null, "Correo o Contraseña incorrecto(s)");
        } catch (UsernameNotFoundException ex) {
            // El correo no existe
            return new AuthResponse(null, "El correo no está registrado");
        }
    }


    private void saveUserToken(UserEntity userEntity, String jwtToken, SessionEntity sessionEntity) {
        var Token = token.builder()
                .userEntity(userEntity)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .session(sessionEntity)
                .build();
        tokenRepository.save(Token);
    }


    private void revokeAllUserTokens(UserEntity userEntity) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(userEntity.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
