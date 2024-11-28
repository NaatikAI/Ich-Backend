package security.ich.login.service.interfaces;

import security.ich.login.model.dto.UsuarioDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioServiceInterface extends UserDetailsService {
    public UsuarioDTO crearUsuario(UsuarioDTO usuario);
}
