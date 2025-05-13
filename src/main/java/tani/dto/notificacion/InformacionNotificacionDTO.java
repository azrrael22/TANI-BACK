package tani.dto.notificacion;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record InformacionNotificacionDTO(
        @NotNull int idNotificacion,//llave primaria
        @NotNull int idInventario,//llave foranea de inventario
        @NotNull String mensaje,//mensaje de la notificacion
        @NotNull LocalDateTime fecha//fecha de la notificacion

) {
}
