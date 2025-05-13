package tani.dto.pedido;

import jakarta.validation.constraints.NotNull;
import tani.dto.detallepedido.RegistroDetallePedidoDTO;
import tani.model.enums.ESTADO;
import java.time.LocalDateTime;
import java.util.List;

public record RegistroPedidoDTO(
        @NotNull LocalDateTime fechaPedido,
        @NotNull ESTADO estado,
        @NotNull int idUsuario,
        @NotNull List<RegistroDetallePedidoDTO> detalles
) {
}