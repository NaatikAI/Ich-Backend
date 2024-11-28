package security.ich.login.controller;

import security.ich.login.model.dto.UsuarioDTO;
import security.ich.login.model.request.UsuarioRequest;
import security.ich.login.model.response.UsuarioResponse;
import security.ich.login.service.interfaces.UsuarioServiceInterface;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServiceInterface usuarioService;

    @GetMapping()
    public String getUsuario(){
        return "hola";
    }

    @PostMapping
    public UsuarioResponse crearUsuario(@RequestBody @Valid UsuarioRequest usuario){
        UsuarioResponse usuarioResponse = new UsuarioResponse();
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuario, usuarioDTO);

        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioDTO);
        BeanUtils.copyProperties(usuarioCreado, usuarioResponse);
        return usuarioResponse;
    }

}
