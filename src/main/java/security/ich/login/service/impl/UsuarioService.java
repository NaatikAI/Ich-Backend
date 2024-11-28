package security.ich.login.service.impl;

import security.ich.login.exceptions.AppException;
import security.ich.login.model.dto.UsuarioDTO;
import security.ich.login.model.entity.UsuarioEntity;
import security.ich.login.repository.UsuarioRepository;
import security.ich.login.service.interfaces.UsuarioServiceInterface;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UsuarioService implements UsuarioServiceInterface {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Crea un usuario en la base de datos
     * @param usuarioDTO UsuarioDTO
     * @return UsuarioDTO
     */
    @Override
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.findByEmail(usuarioDTO.getEmail()) != null){
            throw new AppException(HttpStatus.BAD_REQUEST, "Usuario ya registrado");
        }
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        BeanUtils.copyProperties(usuarioDTO, usuarioEntity);

        usuarioEntity.setPasswordEncriptada(bCryptPasswordEncoder.encode(usuarioDTO.getPassword()));//encriptar contraseña
        UUID uuid = UUID.randomUUID();//genera un ID público
        usuarioEntity.setIdUsuario(uuid.toString());
        UsuarioEntity usuarioCreado = usuarioRepository.save(usuarioEntity);
        UsuarioDTO usuarioReturn = new UsuarioDTO();
        BeanUtils.copyProperties(usuarioCreado, usuarioReturn);
        return usuarioReturn;
    }

    /**
     * Carga los detalles del usuario para la autenticación basándose en el correo electrónico proporcionado.
     * @param email el email utilizado para el login del usuario
     * @return Objecto UserDetails que representa los detalles del usuario
     * @throws UsernameNotFoundException si el usuario no es encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioEntity usuarioEntity =  usuarioRepository.findByEmail(email);
        if (usuarioEntity == null) throw new UsernameNotFoundException("Usuario no encontrado: "+email);
        return new User(usuarioEntity.getEmail(), usuarioEntity.getPasswordEncriptada(), new ArrayList<>());
    }
}
