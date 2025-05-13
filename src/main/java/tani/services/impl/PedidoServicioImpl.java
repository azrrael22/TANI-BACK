package tani.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tani.dto.pedido.RegistroPedidoDTO;
import tani.dto.pedido.InformacionPedidoDTO;
import tani.dto.detallepedido.InformacionDetallePedidoDTO;
import tani.dto.producto.InformacionProductoDTO;
import tani.dto.productotalla.InformacionProductoTallaDTO;
import tani.model.entities.Pedido;
import tani.model.entities.Producto;
import tani.model.entities.ProductoTalla;
import tani.model.entities.Usuario;
import tani.model.enums.ESTADO;
import tani.repositories.PedidoRepo;
import tani.repositories.UsuarioRepo;
import tani.services.interfaces.DetallePedidoServicio;
import tani.services.interfaces.PedidoServicio;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PedidoServicioImpl implements PedidoServicio {

    private final PedidoRepo pedidoRepo;
    private final UsuarioRepo usuarioRepo;
    private final DetallePedidoServicio detallePedidoServicio;

    @Override
    public InformacionPedidoDTO crearPedido(RegistroPedidoDTO pedidoDTO) {
        // Buscar el usuario asociado al pedido
        System.out.println("idUsuarioBuscar:" + pedidoDTO.idUsuario());
        Usuario usuario = usuarioRepo.findById(pedidoDTO.idUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar si ya existe un pedido con estado "PENDIENTE" para este usuario
        Pedido pedidoPendiente = pedidoRepo.findByUsuarioAndEstado(usuario, ESTADO.PENDIENTE);

        Pedido pedido;
        if (pedidoPendiente != null) {
            // Si existe un pedido pendiente, usarlo
            pedido = pedidoPendiente;
        } else {
            // Si no existe, crear un nuevo pedido
            pedido = new Pedido();
            pedido.setUsuario(usuario);
            pedido.setFecha_pedido(LocalDateTime.now());
            pedido.setEstado(pedidoDTO.estado());
            pedido = pedidoRepo.save(pedido);
        }

        // Crear los detalles del pedido
        var detallesGuardados = detallePedidoServicio.crearDetallesPedido(pedido, pedidoDTO.detalles());

        // Convertir los DetallePedido a InformacionDetallePedidoDTO
        List<InformacionDetallePedidoDTO> detallesDTO = detallesGuardados.stream()
                .map(detalle -> new InformacionDetallePedidoDTO(
                        null,
                        null,
                        detalle.getId_detalle(),
                        detalle.getCantidad(),
                        detalle.getPrecio_unitario(),
                        detalle.getSubtotal(),
                        detalle.getProductoTalla().getId()
                ))
                .toList();

        // Retornar el DTO con la información del pedido
        return new InformacionPedidoDTO(
                pedido.getId_pedido(),
                usuario.getId_usuario(),
                pedido.getFecha_pedido(),
                pedido.getEstado(),
                detallesDTO
        );
    }

    @Override
    public InformacionPedidoDTO obtenerCarrito(String idUsuario) {
        // Buscar el usuario asociado al ID
        Usuario usuario = usuarioRepo.findById(Integer.parseInt(idUsuario))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar el pedido con estado "PENDIENTE"
        Pedido pedidoPendiente = pedidoRepo.findByUsuarioAndEstado(usuario, ESTADO.PENDIENTE);

        if (pedidoPendiente == null) {
            throw new RuntimeException("No se encontró un pedido pendiente para este usuario");
        }

        // Convertir los detalles del pedido a DTO
        List<InformacionDetallePedidoDTO> detallesDTO = pedidoPendiente.getDetalles().stream()
                .map(detalle -> {
                    // Buscar ProductoTalla
                    ProductoTalla productoTalla = detalle.getProductoTalla();
                    if (productoTalla == null) {
                        throw new RuntimeException("ProductoTalla no encontrado para el detalle del pedido");
                    }

                    // Buscar Producto
                    Producto producto = productoTalla.getProducto();
                    if (producto == null) {
                        throw new RuntimeException("Producto no encontrado para ProductoTalla");
                    }

                    // Crear DTOs de Producto y ProductoTalla
                    InformacionProductoDTO productoDTO = new InformacionProductoDTO(
                            producto.getId_producto(),
                            producto.getNombre(),
                            producto.getDescripcion(),
                            producto.getTipoCalzado(),
                            producto.getImagen(),
                            producto.getPrecio()
                    );

                    InformacionProductoTallaDTO tallaDTO = new InformacionProductoTallaDTO(
                            productoTalla.getId(),
                            producto.getId_producto(),
                            productoTalla.getTalla(),
                            productoTalla.getCantidad()
                    );

                    // Retornar el DTO del detalle del pedido
                    return new InformacionDetallePedidoDTO(
                            productoDTO,
                            tallaDTO,
                            detalle.getId_detalle(),
                            detalle.getCantidad(),
                            detalle.getPrecio_unitario(),
                            detalle.getSubtotal(),
                            productoTalla.getId()
                    );
                })
                .toList();

        // Retornar el DTO con la información del pedido
        return new InformacionPedidoDTO(
                pedidoPendiente.getId_pedido(),
                usuario.getId_usuario(),
                pedidoPendiente.getFecha_pedido(),
                pedidoPendiente.getEstado(),
                detallesDTO
        );
    }
}
