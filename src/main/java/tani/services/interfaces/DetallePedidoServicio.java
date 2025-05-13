package tani.services.interfaces;

import tani.model.entities.DetallePedido;
import tani.model.entities.Pedido;
import tani.dto.detallepedido.RegistroDetallePedidoDTO;

import java.util.List;

public interface DetallePedidoServicio {
    List<DetallePedido> crearDetallesPedido(Pedido pedido, List<RegistroDetallePedidoDTO> detallesDTO);

    void actualizarCantidadDetalle(int idDetalle, int nuevaCantidad);

    void eliminarDetalle(int idDetalle);

}