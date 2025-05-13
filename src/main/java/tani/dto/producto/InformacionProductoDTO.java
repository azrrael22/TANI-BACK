package tani.dto.producto;

import jakarta.validation.constraints.NotNull;
import tani.model.enums.TIPO_CALZADO;

public record InformacionProductoDTO(
        @NotNull int idProducto,
        @NotNull String nombre,
        @NotNull String descripcion,
        @NotNull TIPO_CALZADO tipoCalzado,
        @NotNull String imagen,
        @NotNull float precio
) {
}
