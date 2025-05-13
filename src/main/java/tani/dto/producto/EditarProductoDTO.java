package tani.dto.producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tani.model.enums.TIPO_CALZADO;

public record EditarProductoDTO(
        @NotNull int idProducto,
        @NotBlank String nombre,
        @NotBlank String descripcion,
        @NotNull TIPO_CALZADO tipoCalzado,
        @NotBlank String imagen,
        @NotNull float precio
) {
}