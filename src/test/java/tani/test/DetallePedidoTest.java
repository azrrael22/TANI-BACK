package tani.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tani.model.entities.*;
import tani.model.enums.ESTADO;
import tani.model.enums.TIPO_CALZADO;
import tani.model.enums.TIPO_USUARIO;
import tani.repositories.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DetallePedidoTest {

    @Autowired
    private DetallePedidoRepo detallePedidoRepo;

    @Autowired
    private PedidoRepo pedidoRepo;

    @Autowired
    private ProductoTallaRepo productoTallaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Test
    public void registrarTest(){
        Producto producto = productoRepo.save(
                Producto.builder()
                        .nombre("Sandalias playeras")
                        .descripcion("Cómodas ligeras, ideales para días de verano.")
                        .tipoCalzado(TIPO_CALZADO.SANDALIA)
                        .imagen("sandalias_playa.png")
                        .precio(45.99f)
                        //.tallas(new ArrayList<>())
                        .build()
        );

        ProductoTalla productoTalla = productoTallaRepo.save(
                ProductoTalla.builder()
                        .talla("37")
                        .cantidad(32)
                        .producto(producto)
                        .build()
        );

        Usuario usuario = usuarioRepo.save(
                Usuario.builder()
                        .nombre("Ana López")
                        .correo("ana@gmail.com")
                        .telefono("425786")
                        .contrasenia("analopez2025")
                        .tipoUsuario(TIPO_USUARIO.USUARIO)
                        .fechaNacimiento(LocalDate.of(2001, 3, 21))
                        .build()
        );

        Pedido pedido = pedidoRepo.save(
                Pedido.builder()
                        .estado(ESTADO.PENDIENTE)
                        .usuario(usuario)
                        .fecha_pedido(LocalDateTime.now())
                        .build()
        );

        DetallePedido detallePedido = detallePedidoRepo.save(
                DetallePedido.builder()
                        .pedido(pedido)
                        .productoTalla(productoTalla)
                        .cantidad(3)
                        .precio_unitario(producto.getPrecio())
                        .subtotal(240.0f) //Calcular bien el subtotal
                        .build()
        );

        // Verificaciones
        assertNotNull(detallePedido.getId_detalle());
        assertEquals(240.0f, detallePedido.getSubtotal(), 0.01);
        assertEquals("37", detallePedido.getProductoTalla().getTalla());
    }

    @Test
    public void actualizarTest(){
        // Asumiendo que existe un detalle con ID = 1 en tu DB de prueba
        int idFijo = 3;

        DetallePedido detallePedido = detallePedidoRepo.findById(idFijo). orElseThrow();
        detallePedido.setCantidad(4);
        detallePedido.setSubtotal(183.96f);
        detallePedidoRepo.save(detallePedido);

        DetallePedido actualizado = detallePedidoRepo.findById(idFijo).orElseThrow();
        assertEquals(4, actualizado.getCantidad());
    }

    @Test
    public void listarTest(){
        List<DetallePedido> detallePedidoList = detallePedidoRepo.findAll();

        // Verificaciones básicas
        assertNotNull(detallePedidoList, "La lista de detalles no debe ser nula");
        assertNotNull(detallePedidoList.isEmpty(), "Debe existir al menos un detalle registrado");

        // Validar cada detalle existente
        detallePedidoList.forEach(detallePedido -> {
            System.out.println("\n=== Detalle de Pedido Existente ===");
            System.out.println("ID: " + detallePedido.getId_detalle());
            System.out.println("Cantidad: " + detallePedido.getCantidad());
            System.out.println("Subtotal: $" + detallePedido.getSubtotal());

            //
            System.out.println("Talla asociada: " + detallePedido.getProductoTalla().getTalla());
            System.out.println("ID Pedido: " + detallePedido.getPedido().getId_pedido());
            System.out.println("ID Producto: " + detallePedido.getProductoTalla().getProducto().getId_producto());
            System.out.println("Usuario: " + detallePedido.getPedido().getUsuario().getNombre());

            // Validaciones de integridad
            assertNotNull(detallePedido.getPedido(), "El pedido no debe ser nulo");
            assertTrue(detallePedido.getPedido().getId_pedido() > 0, "ID de pedido inválido");
            assertNotNull(detallePedido.getProductoTalla(), "La talla asociada no debe ser nula");
            assertTrue(detallePedido.getCantidad() > 0, "Cantidad debe ser positiva");
            assertEquals(detallePedido.getPrecio_unitario() * detallePedido.getCantidad(), detallePedido.getSubtotal(),
                    0.01, "Subtotal mal calculado");
        });
    }

    @Test
    public void eliminarTest(){
        int idDetalleExistente = 3;

        if(detallePedidoRepo.existsById(idDetalleExistente)){
            detallePedidoRepo.deleteById(idDetalleExistente);
            assertFalse(detallePedidoRepo.existsById(idDetalleExistente));
        }   else {
            fail("El detalle con ID " + idDetalleExistente + " no existe en la BD");
        }
    }

}
