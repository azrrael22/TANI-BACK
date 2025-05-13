package tani.dto.productotalla;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditarProductoTallaDTO(
        @NotNull int id,
        @NotNull int productoId,
        @NotBlank String talla,
        @NotNull int cantidad
) {
}