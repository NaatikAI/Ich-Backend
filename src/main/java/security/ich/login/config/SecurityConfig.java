package security.ich.login.config;

import security.ich.login.config.filters.AuthenticationFilter;
import security.ich.login.service.interfaces.UsuarioServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UsuarioServiceInterface usuarioService;
    private final BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(UsuarioServiceInterface usuarioService, BCryptPasswordEncoder passwordEncoder){
        this.usuarioService=usuarioService;
        this.passwordEncoder=passwordEncoder;
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/**").permitAll() // Permitir acceso a todas las rutas GET sin autenticación
                .antMatchers(HttpMethod.POST, "/usuario").permitAll() // Permitir acceso sin autenticación para el método POST en la ruta /usuario
                .anyRequest().authenticated() // Requiere autenticación para todas las demás solicitudes
                .and().addFilter(new AuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//hace que la aplicación no guarde datos de la sesión de los usuarios Cada solicitud que realiza un cliente se trata de manera independiente y no se almacena información relacionada con esa solicitud para su uso en solicitudes futuras.

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usuarioService).passwordEncoder(passwordEncoder);
    }
}
