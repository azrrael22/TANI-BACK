package tani.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import tani.config.JWTUtils;
import tani.dto.otros.EmailDTO;
import tani.dto.otros.TokenDTO;
import tani.dto.usuario.InformacionUsuarioDTO;
import tani.dto.usuario.LoginDTO;
import tani.dto.usuario.RegistroUsuarioDTO;
import tani.model.entities.Usuario;
import tani.repositories.UsuarioRepo;
import tani.services.interfaces.UsuarioServicio;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tani.services.interfaces.EmailServicio;

import java.util.Map;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServicioImpl implements UsuarioServicio {

    private final JWTUtils jwtUtils;
    private final UsuarioRepo usuarioRepo;
    private final EmailServicio emailServicio;


    @Override
    public InformacionUsuarioDTO registrarUsuario(RegistroUsuarioDTO usuarioDTO) throws Exception {
        // Verificar si el correo ya está registrado
        if (usuarioRepo.findByCorreo(usuarioDTO.correo()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        // Convertir DTO a entidad Usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.nombre());
        usuario.setFechaNacimiento(usuarioDTO.fechaNacimiento());
        usuario.setTelefono(usuarioDTO.telefono());
        usuario.setCorreo(usuarioDTO.correo());
        usuario.setContrasenia(encriptarPassword(usuarioDTO.contrasenia()));
        usuario.setTipoUsuario(usuarioDTO.tipoUsuario());

        // Guardar el usuario en la base de datos
        Usuario usuarioCreado = usuarioRepo.save(usuario);

        // Enviar correo de bienvenida
        EmailDTO email = new EmailDTO(
                "¡Bienvenido a la plataforma de TANI, " + usuarioDTO.nombre() + "!",
                "Hola " + usuarioDTO.nombre() + ", ¡Tu información personal fue guardada en nuestra base de datos! " +
                        "Te damos una cálida bienvenida a TANI.",
                usuarioDTO.correo()
        );
        emailServicio.enviarCorreo(email);

        // Convertir entidad a DTO y devolver
        return new InformacionUsuarioDTO(
                usuarioCreado.getId_usuario(),
                usuarioCreado.getNombre(),
                usuarioCreado.getFechaNacimiento(),
                usuarioCreado.getTelefono(),
                usuarioCreado.getCorreo(),
                usuarioCreado.getTipoUsuario()
        );
    }



    private String encriptarPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode( password );

    }

    @Override
    public InformacionUsuarioDTO iniciarSesion(LoginDTO usuarioDTO) {
        // Buscar el usuario por correo
        Usuario usuario = usuarioRepo.findByCorreo(usuarioDTO.email())
                .orElseThrow(() -> new RuntimeException("El correo no está registrado"));

        // Validar la contraseña con BCrypt
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(usuarioDTO.password(), usuario.getContrasenia())) {
            throw new RuntimeException("La contraseña es incorrecta");
        }

        // Crear y retornar el DTO de información del usuario
        return new InformacionUsuarioDTO(
                usuario.getId_usuario(),
                usuario.getNombre(),
                usuario.getFechaNacimiento(),
                usuario.getTelefono(),
                usuario.getCorreo(),
                usuario.getTipoUsuario()
        );
    }



    @Override
    public Optional<InformacionUsuarioDTO> buscarPorCorreo(String correo) {
        return usuarioRepo.findByCorreo(correo)
                .map(usuario -> new InformacionUsuarioDTO(
                        usuario.getId_usuario(),
                        usuario.getNombre(),
                        usuario.getFechaNacimiento(),
                        usuario.getTelefono(),
                        usuario.getCorreo(),
                        usuario.getTipoUsuario()
                ));
    }

    @Override
    public boolean existeCorreo(String correo) {
        return usuarioRepo.findByCorreo(correo).isPresent();
    }


    @Override
    public List<InformacionUsuarioDTO> listarUsuarios() {
        return usuarioRepo.findAll().stream()
                .map(usuario -> new InformacionUsuarioDTO(
                        usuario.getId_usuario(),
                        usuario.getNombre(),
                        usuario.getFechaNacimiento(),
                        usuario.getTelefono(),
                        usuario.getCorreo(),
                        usuario.getTipoUsuario()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarUsuario(Integer id) {
        usuarioRepo.deleteById(id);
    }

    @Override
    public void recuperarPassword(String correo) throws Exception {
        Usuario usuario = usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));

        // Convertir entidad a DTO
        InformacionUsuarioDTO usuarioDTO = new InformacionUsuarioDTO(
                usuario.getId_usuario(),
                usuario.getNombre(),
                usuario.getFechaNacimiento(),
                usuario.getTelefono(),
                usuario.getCorreo(),
                usuario.getTipoUsuario()
        );

        // Enviar correo de recuperación de contraseña
        String link = "http://localhost:8080/api/auth/cambiar-contrasenia/" + usuarioDTO.correo();
        EmailDTO email = new EmailDTO(
                "¡Recuperacion de contraseña TANI calzados!",
                "Hola " + usuarioDTO.nombre() + ", ¡Se genero el siguiente link para la recuperacion de la contraseña! " + link,
                usuarioDTO.correo()
        );
        emailServicio.enviarCorreo(email);
    }

    @Override
    public void enviarCorreoRecuperacion(String correo) throws Exception {
        Usuario usuario = usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Correo no encontrado"));

        // Generar un token JWT temporal (ej: válido por 15 minutos)
        Map<String, Object> claims = Map.of(
                "tipo", "RECUPERACION_CONTRASENIA"
        );
        String token = jwtUtils.generarTokenConExpiracion(correo, claims, 15); // 15 minutos

        // Construir el link que irá en el correo (a la vista del front Angular)
        String linkRecuperacion = "http://localhost:4200/account/reset-password?token=" + token;

        // Enviar el correo con el link
        EmailDTO email = new EmailDTO(
                "Recuperación de contraseña - TANI Calzados",
                "Hola " + usuario.getNombre() + ",\n\n" +
                        "Has solicitado recuperar tu contraseña. Por favor haz clic en el siguiente enlace para restablecerla:\n\n" +
                        linkRecuperacion + "\n\n" +
                        "Este enlace expirará en 15 minutos.\n\n" +
                        "Si no solicitaste este cambio, puedes ignorar este mensaje.",
                correo
        );

        emailServicio.enviarCorreo(email);
    }

    @Override
    public void restablecerContrasenia(String token, String nuevaContrasenia) throws Exception {
        // Validar y obtener el correo desde el token
        String correo = jwtUtils.obtenerCorreoDesdeToken(token);

        Usuario usuario = usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Cambiar contraseña
        usuario.setContrasenia(encriptarPassword(nuevaContrasenia));
        usuarioRepo.save(usuario);

        // (Opcional) Enviar correo de confirmación
        EmailDTO email = new EmailDTO(
                "Confirmación de cambio de contraseña",
                "Hola " + usuario.getNombre() + ",\n\nTu contraseña fue restablecida correctamente. Si no realizaste esta acción, por favor contacta al soporte de TANI.",
                correo
        );
        emailServicio.enviarCorreo(email);
    }



    @Override
    public void cambiarContrasenia(String correo, String contrasenia) {
        // Buscar el usuario por correo
        Usuario usuario = usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar la contraseña del usuario
        usuario.setContrasenia(encriptarPassword(contrasenia));

        // Guardar los cambios en la base de datos
        usuarioRepo.save(usuario);
    }

    @Override
    public InformacionUsuarioDTO editarUsuario(RegistroUsuarioDTO usuarioDTO) throws Exception {
        // Buscar el usuario por correo
        Usuario usuario = usuarioRepo.findByCorreo(usuarioDTO.correo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar los campos del usuario
        usuario.setNombre(usuarioDTO.nombre());
        usuario.setFechaNacimiento(usuarioDTO.fechaNacimiento());
        usuario.setTelefono(usuarioDTO.telefono());
        usuario.setContrasenia(encriptarPassword(usuarioDTO.contrasenia()));
        usuario.setTipoUsuario(usuarioDTO.tipoUsuario());

        // Guardar los cambios en la base de datos
        Usuario usuarioActualizado = usuarioRepo.save(usuario);

        // Convertir entidad a DTO y devolver
        return new InformacionUsuarioDTO(
                usuarioActualizado.getId_usuario(),
                usuarioActualizado.getNombre(),
                usuarioActualizado.getFechaNacimiento(),
                usuarioActualizado.getTelefono(),
                usuarioActualizado.getCorreo(),
                usuarioActualizado.getTipoUsuario()
        );
    }


}