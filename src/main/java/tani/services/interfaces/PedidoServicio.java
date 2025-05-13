package tani.services.interfaces;

import tani.dto.pedido.RegistroPedidoDTO;
import tani.dto.pedido.InformacionPedidoDTO;

public interface PedidoServicio {
    InformacionPedidoDTO crearPedido(RegistroPedidoDTO registroPedidoDTO);

    InformacionPedidoDTO obtenerCarrito(String idUsuario);
}