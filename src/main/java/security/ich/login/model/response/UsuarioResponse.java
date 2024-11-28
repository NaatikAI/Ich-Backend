package security.ich.login.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {
    private String idUsuario;
    private String nombre;
    private String apellido;
    private String email;

}
