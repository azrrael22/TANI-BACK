package tani.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tani.model.enums.TIPO_USUARIO;
import java.time.LocalDate;

public record InformacionUsuarioDTO(
        @NotNull int idUsuario,  // Llave primaria
        @NotNull  String nombre,
        @NotNull   LocalDate fechaNacimiento,
        @NotNull  String telefono,
        @NotBlank String correo, // Identificador natural
        @NotNull    TIPO_USUARIO tipoUsuario
) {
}

