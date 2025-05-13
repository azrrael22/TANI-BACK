package tani.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tani.model.enums.TIPO_USUARIO;
import java.time.LocalDate;

public record RegistroUsuarioDTO(
        @NotNull String nombre,
        @NotNull  LocalDate fechaNacimiento,
        @NotNull  String telefono,
        @NotBlank String correo, // Identificador natural, en lugar del idUsuario
        @NotBlank  String contrasenia,
        @NotNull   TIPO_USUARIO tipoUsuario
) {
}
