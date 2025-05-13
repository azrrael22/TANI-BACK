package tani.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tani.dto.detallepedido.RegistroDetallePedidoDTO;
import tani.model.entities.DetallePedido;
import tani.model.entities.Pedido;
import tani.model.entities.ProductoTalla;
import tani.repositories.DetallePedidoRepo;
import tani.repositories.ProductoTallaRepo;
import tani.services.interfaces.DetallePedidoServicio;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetallePedidoServicioImpl implements DetallePedidoServicio {

    private final DetallePedidoRepo detallePedidoRepo;
    private final ProductoTallaRepo productoTallaRepo;

    @Override
    public List<DetallePedido> crearDetallesPedido(Pedido pedido, List<RegistroDetallePedidoDTO> detallesDTO) {
        // Crear y guardar los detalles del pedido
        List<DetallePedido> detalles = detallesDTO.stream().map(detalleDTO -> {
            ProductoTalla productoTalla = productoTallaRepo.findById(detalleDTO.idProductoTalla())
                    .orElseThrow(() -> new RuntimeException("ProductoTalla no encontrado"));

            // Verificar que haya al menos 1 unidad disponible
            if (productoTalla.getCantidad() <= 0) {
                throw new RuntimeException("No se puede añadir al carrito: No se cuenta con unidades disponibles de esta talla");
            }

            return DetallePedido.builder()
                    .pedido(pedido)
                    .productoTalla(productoTalla)
                    .cantidad(detalleDTO.cantidad())
                    .precio_unitario(detalleDTO.precioUnitario())
                    .subtotal(detalleDTO.subtotal())
                    .build();
        }).collect(Collectors.toList());

        return detallePedidoRepo.saveAll(detalles);
    }

    @Override
    public void actualizarCantidadDetalle(int idDetalle, int nuevaCantidad) {
        // Buscar el detalle del pedido por su ID
        DetallePedido detalle = detallePedidoRepo.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("DetallePedido no encontrado"));

        //verificar unidades disponibles
        ProductoTalla productoTalla = detalle.getProductoTalla();
        if (productoTalla.getCantidad() < nuevaCantidad) {
            throw new RuntimeException("No se puede actualizar la cantidad: No hay suficientes unidades disponibles");
        }

        // Actualizar la cantidad
        detalle.setCantidad(nuevaCantidad);

        // Recalcular el subtotal
        detalle.setSubtotal(detalle.getPrecio_unitario() * nuevaCantidad);

        // Guardar los cambios
        detallePedidoRepo.save(detalle);
    }

    @Override
    public void eliminarDetalle(int idDetalle) {
        // Buscar el detalle del pedido por su ID
        DetallePedido detalle = detallePedidoRepo.findById(idDetalle)
                .orElseThrow(() -> new RuntimeException("DetallePedido no encontrado"));

        // Eliminar el registro de la tabla detalle_pedido
        detallePedidoRepo.delete(detalle);
    }

}