package tani.dto.productotalla;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroProductoTallaDTO(
        int productoId,
        int productoTallaId,
        @NotBlank String talla,
        @NotNull int cantidad
) {
}