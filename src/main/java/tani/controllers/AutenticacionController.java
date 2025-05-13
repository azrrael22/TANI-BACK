package tani.controllers;

import org.springframework.http.HttpStatus;
import tani.dto.usuario.InformacionUsuarioDTO;
import tani.dto.usuario.RegistroUsuarioDTO;
import tani.dto.usuario.LoginDTO;
import tani.dto.otros.TokenDTO;
import tani.model.entities.Usuario;
import tani.services.interfaces.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AutenticacionController {
    private final UsuarioServicio usuarioServicio;

    @PostMapping("/iniciar-sesion")
    public ResponseEntity<InformacionUsuarioDTO> iniciarSesion(@Valid @RequestBody LoginDTO loginDTO) {
        InformacionUsuarioDTO usuarioDTO = usuarioServicio.iniciarSesion(loginDTO);
        return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping("/crear-cuenta")
    public ResponseEntity<Map<String, String>> crearCuenta(@Valid @RequestBody RegistroUsuarioDTO registroUsuarioDTO) throws Exception {
        usuarioServicio.registrarUsuario(registroUsuarioDTO);

        // Crear una respuesta en formato JSON
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Cuenta creada exitosamente");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-contrasenia")
    public ResponseEntity<Map<String, String>> recuperarContrasenia(@Valid @RequestBody Map<String, String> request) throws Exception {
        String correo = request.get("correo");
        usuarioServicio.enviarCorreoRecuperacion(correo);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Se ha enviado un correo con instrucciones para restablecer tu contraseña.");
        return ResponseEntity.ok(response);
    }


    @PutMapping("/restablecer-contrasenia")
    public ResponseEntity<Map<String, String>> restablecerContrasenia(@RequestParam String token, @RequestBody Map<String, String> body) throws Exception {
        String nuevaContrasenia = body.get("contrasenia");
        usuarioServicio.restablecerContrasenia(token, nuevaContrasenia);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "La contraseña se ha restablecido exitosamente.");
        return ResponseEntity.ok(response);
    }




}