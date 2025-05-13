package tani.dto.pedido;

import jakarta.validation.constraints.NotNull;
import tani.dto.detallepedido.InformacionDetallePedidoDTO;
import tani.dto.usuario.InformacionUsuarioDTO;
import tani.model.enums.ESTADO;
import java.time.LocalDateTime;
import java.util.List;

public record InformacionPedidoDTO(
        int idPedido,
        int usuarioId,
        LocalDateTime fechaPedido,
        ESTADO estado,
        List<InformacionDetallePedidoDTO> detalles
) {
}