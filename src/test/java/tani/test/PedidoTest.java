package tani.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
import tani.model.entities.*;
import tani.model.enums.ESTADO;
import tani.model.enums.TIPO_CALZADO;
import tani.model.enums.TIPO_USUARIO;
import tani.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class PedidoTest {

    @Autowired
    private PedidoRepo pedidoRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private ProductoTallaRepo productoTallaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private DetallePedidoRepo detallePedidoRepo;

    @Test
    public void registrarTest(){
        Producto producto = productoRepo.save(
                Producto.builder()
                        .nombre("Tenis Training")
                        .descripcion("Diseñados para tus entrenamientos")
                        .tipoCalzado(TIPO_CALZADO.TENIS)
                        .imagen("training.png")
                        .precio(201.9f)
                        .build()
        );

        ProductoTalla productoTalla = productoTallaRepo.save(
                ProductoTalla.builder()
                        .talla("36")
                        .cantidad(8)
                        .producto(producto)
                        .build()
        );

        Usuario usuario = usuarioRepo.save(Usuario.builder()
                .nombre("Jose")
                .fechaNacimiento(LocalDate.of(1999, 1, 20))
                .telefono("254896")
                .correo("jose@gmail.com")
                .contrasenia("jose321")
                .tipoUsuario(TIPO_USUARIO.USUARIO)
                .build()
        );

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setUsuario(usuario);
        nuevoPedido.setEstado(ESTADO.PENDIENTE);
        nuevoPedido.setFecha_pedido(LocalDateTime.now());

        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setProductoTalla(productoTalla);
        detallePedido.setCantidad(3);
        detallePedido.setPrecio_unitario(producto.getPrecio());
        detallePedido.setSubtotal(detallePedido.getCantidad() * detallePedido.getPrecio_unitario());
        detallePedido.setPedido(nuevoPedido);

        nuevoPedido.setDetalles(List.of(detallePedido));

        Pedido guardado = pedidoRepo.save(nuevoPedido);

        assertNotNull(guardado.getId_pedido());
        assertEquals(ESTADO.PENDIENTE, guardado.getEstado()); //CAMBIAR EL ESTADO AQUI ANTES DE EJECUTAR
        assertFalse(guardado.getDetalles().isEmpty());

        ProductoTalla ptDesdeBD = productoTallaRepo.findById(productoTalla.getId()).orElseThrow();
        assertEquals(producto.getId_producto(), ptDesdeBD.getProducto().getId_producto());

    }

    @Test
    public void actualizarTest(){
        Pedido actualizarPedido = pedidoRepo.findById(1).orElseThrow();
        actualizarPedido.setEstado(ESTADO.ENVIADO);
        actualizarPedido.setFecha_pedido(LocalDateTime.now().plusDays(2));

        pedidoRepo.save(actualizarPedido);
        Pedido actualizado = pedidoRepo.findById(1).orElseThrow();
        assertEquals("Oscar", actualizado.getUsuario().getNombre());

    }

    @Test
    public void listarTest(){

        List<Pedido> listaPedidos = pedidoRepo.findAllWithDetalles();

        //Verificaciones básicas
        assertNotNull(listaPedidos, "La lista de pedidos no debe ser nula");
        assertFalse(listaPedidos.isEmpty(), "La lista de pedidos no debe estar vacia");

        //Iterar y validar cada registro
        listaPedidos.forEach(pedidos -> {
            System.out.println("\n=== Registro Pedidos ===");
            System.out.println("Usuario: " + pedidos.getUsuario().getNombre());
            System.out.println("Estado: " + pedidos.getEstado());
            System.out.println("Fecha: " + pedidos.getFecha_pedido());

            // Validaciones básicas
            assertNotNull(pedidos.getId_pedido(), "ID de pedido no generado");
            assertNotNull(pedidos.getFecha_pedido(), "Fecha de pedido obligatoria");
            assertNotNull(pedidos.getUsuario(), "Pedido sin usuario asociado");

            // Validar detalles del pedido
            assertFalse(pedidos.getDetalles().isEmpty(), "El pedido debe tener detalles");

            pedidos.getDetalles().forEach(detallePedido -> {
                System.out.println(" - Talla: " + detallePedido.getProductoTalla().getTalla());
                System.out.println(" - Cantidad: " + detallePedido.getCantidad());
                System.out.println(" - Subtotal: " + detallePedido.getSubtotal());
                System.out.println(" - Producto: " + detallePedido.getProductoTalla().getProducto().getId_producto());

                // Validaciones de detalle
                assertNotNull(detallePedido.getProductoTalla(), "Detalle sin talla asociada");
                assertTrue(detallePedido.getCantidad() > 0, "Cantidad debe ser positiva");
                assertEquals(
                        detallePedido.getPrecio_unitario() * detallePedido.getCantidad(),
                        detallePedido.getSubtotal(),
                        0.01,
                        "Subtotal calculado incorrectamente");

            });

        });
    }

    @Test
    public void eliminarTest(){
        int pedidoExistente = 2;
        assertTrue(pedidoRepo.existsById(pedidoExistente), "El pedido no existe");

        //Obtener detalles asociados
        List<DetallePedido> detallePedidos = detallePedidoRepo.buscarPorIdPedido(pedidoExistente);
        assertFalse(detallePedidos.isEmpty(), "El pedido no tiene detalles");

        //Eliminar
        pedidoRepo.deleteById(pedidoExistente);

        //Verificar
        assertFalse(pedidoRepo.existsById(pedidoExistente), "Eliminación Fallida");
        detallePedidos.forEach(detallePedido ->
                assertFalse(detallePedidoRepo.existsById(detallePedido.getId_detalle()), "Detalle no Eliminado")
        );

    }


}
