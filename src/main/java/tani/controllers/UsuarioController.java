package tani.controllers;

import tani.dto.usuario.InformacionUsuarioDTO;
import tani.dto.usuario.RegistroUsuarioDTO;
import tani.services.interfaces.UsuarioServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioServicio usuarioService;

    @GetMapping
    public ResponseEntity<List<InformacionUsuarioDTO>> obtenerTodosLosUsuarios() {
        List<InformacionUsuarioDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/editar-usuario")
    public ResponseEntity<InformacionUsuarioDTO> editarUsuario(@RequestBody RegistroUsuarioDTO registroUsuarioDTO) throws Exception {
        InformacionUsuarioDTO usuarioActualizado = usuarioService.editarUsuario(registroUsuarioDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable int id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}