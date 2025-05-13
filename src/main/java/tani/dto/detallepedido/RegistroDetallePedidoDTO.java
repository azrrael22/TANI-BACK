package tani.dto.detallepedido;

import jakarta.validation.constraints.NotNull;

public record RegistroDetallePedidoDTO(
        @NotNull int cantidad,
        @NotNull float precioUnitario,
        @NotNull float subtotal,
        @NotNull int idProductoTalla,
        int idPedido
) {
}