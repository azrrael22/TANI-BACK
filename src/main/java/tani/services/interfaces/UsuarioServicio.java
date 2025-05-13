package tani.services.interfaces;

import tani.dto.usuario.InformacionUsuarioDTO;
import tani.dto.usuario.LoginDTO;
import tani.dto.usuario.RegistroUsuarioDTO;
import tani.model.entities.Usuario;
import tani.dto.otros.TokenDTO;

import java.util.List;
import java.util.Optional;

public interface UsuarioServicio {

    InformacionUsuarioDTO registrarUsuario(RegistroUsuarioDTO usuarioDTO) throws Exception;

    Optional<InformacionUsuarioDTO> buscarPorCorreo(String correo) throws Exception;

    InformacionUsuarioDTO iniciarSesion(LoginDTO usuarioDTO);

    boolean existeCorreo(String correo);

    InformacionUsuarioDTO editarUsuario(RegistroUsuarioDTO usuarioDTO) throws Exception;


    List<InformacionUsuarioDTO> listarUsuarios();

    void eliminarUsuario(Integer id);

    void cambiarContrasenia(String correo, String contrasenia);

    void recuperarPassword(String correo)throws Exception;

    void enviarCorreoRecuperacion(String correo) throws Exception;

    void restablecerContrasenia(String token, String nuevaContrasenia) throws Exception;
}