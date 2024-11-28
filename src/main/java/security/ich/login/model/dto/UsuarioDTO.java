package security.ich.login.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UsuarioDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private long id;
    private String idUsuario;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String passwordEncriptada;
}
