package tani.dto.detallepedido;


import jakarta.validation.constraints.NotNull;
import tani.dto.producto.InformacionProductoDTO;
import tani.dto.productotalla.InformacionProductoTallaDTO;

public record InformacionDetallePedidoDTO(

         InformacionProductoDTO productoDTO,
         InformacionProductoTallaDTO tallaDTO,
        @NotNull int idDetalle, // ID del detalle del pedido
        @NotNull int cantidad,
        @NotNull float precioUnitario,
        @NotNull float subtotal,
        @NotNull int productoTallaId
) {
}