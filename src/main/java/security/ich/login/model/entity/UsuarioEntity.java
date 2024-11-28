package security.ich.login.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


@Entity(name = "usuarios")
@Getter
@Setter
public class UsuarioEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String idUsuario;
    @NotEmpty
    @Column(nullable = false, length = 100)
    private String nombre;
    @NotEmpty
    @Column(nullable = false, length = 100)
    private String apellido;
    @Email(message = "El campo 'email' debe ser una dirección de correo electrónico válida")
    @Column(nullable = false, length = 255)
    private String email;
    @NotEmpty
    @Column(nullable = false)
    private String passwordEncriptada;

}
