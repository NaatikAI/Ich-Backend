package com.example.demo.service;

import com.example.demo.entity.SessionEntity;
import com.example.demo.jwt.TokenRepository;
import com.example.demo.jwt.token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final SessionService sessionService;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        ZoneId zonaHorariaSistema2 = ZoneId.systemDefault();
        ZonedDateTime horaActual = ZonedDateTime.now(zonaHorariaSistema2);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);

            // Buscar el token en la base de datos
            Optional<token> storedToken = tokenRepository.findByToken(jwt);

            if (storedToken.isPresent()) {
                token token = storedToken.get();

                // Marcar el token como revocado y caducado
                token.setRevoked(true);
                token.setExpired(true);

                // Obtener la sesión asociada al token
                SessionEntity session = token.getSession();

                if (session != null) {
                    // Establecer la fecha de cierre de sesión
                    session.setEndTime(horaActual);

                    // Actualizar la sesión en la base de datos
                    sessionService.updateSession(session);
                }

                // Actualizar el token en la base de datos
                tokenRepository.save(token);
            }
        }

        // Limpiar el contexto de seguridad
        SecurityContextHolder.clearContext();
    }

}
