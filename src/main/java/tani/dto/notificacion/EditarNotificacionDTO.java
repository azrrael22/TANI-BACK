package tani.dto.notificacion;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EditarNotificacionDTO(
        @NotNull int idNotificacion,//llave primaria
        @NotNull String mensaje,//mensaje de la notificacion
        @NotNull LocalDateTime fecha//fecha de la notificacion
) {
}
