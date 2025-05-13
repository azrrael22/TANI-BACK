package tani.test;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tani.model.entities.Producto;
import tani.model.entities.ProductoTalla;
import tani.model.enums.TIPO_CALZADO;
import tani.repositories.ProductoRepo;
import tani.repositories.ProductoTallaRepo;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductoTallaTest {

    @Autowired
    private ProductoTallaRepo productoTallaRepo;

    @Autowired
    private ProductoRepo productoRepo;

    @Autowired
    private EntityManager entityManager;


    @Test
    public void registrarTest(){
        Producto producto = productoRepo.save(Producto.builder()
                .nombre("Tenis Lifestyle")
                .descripcion("Diseño Clásico Modelo informal")
                .tipoCalzado(TIPO_CALZADO.TENIS)
                .imagen("lifestyle.png")
                .precio(100.0f)
                .build()
        );

        ProductoTalla nuevaTalla = ProductoTalla.builder()
                .talla("32")
                .cantidad(25)
                .producto(producto)
                .build();

        ProductoTalla guardada = productoTallaRepo.save(nuevaTalla);
        //Verificar persistencia
        assertNotNull(guardada.getId(), "ID no generado");
        //Verificar relación
        ProductoTalla desdeBD = productoTallaRepo.findById(guardada.getId()).orElseThrow();
        assertEquals(producto.getId_producto(), desdeBD.getProducto().getId_producto(), "Relación Incorrecta");

    }

    @Test
    public void actualizarTest(){
        ProductoTalla actualizarTalla = productoTallaRepo.findById(16).orElseThrow();
        actualizarTalla.setTalla("40");
        actualizarTalla.setCantidad(7);

        productoTallaRepo.save(actualizarTalla);
        ProductoTalla tallaActualizada = productoTallaRepo.findById(16).orElseThrow();
        assertEquals("40", tallaActualizada.getTalla());
    }

    @Test
    public void listarTest(){
        List<ProductoTalla> listaTallas = productoTallaRepo.findAll();

        //Verificaciones básicas
        assertNotNull(listaTallas, "La lista de tallas no debe ser nula");
        assertFalse(listaTallas.isEmpty(), "La lista de tallas no debe estar vacia");

        //Iterar y validar cada registro
        listaTallas.forEach(productoTalla -> {
            System.out.println("\n=== Registro Productotalla ===");
            System.out.println("ID Producto: " + productoTalla.getProducto().getId_producto());
            System.out.println("Talla: " + productoTalla.getTalla());
            System.out.println("Cantidad: " + productoTalla.getCantidad());

            assertNotNull(productoTalla.getProducto(), "El producto asociado no debe ser nulo");
            assertTrue(productoTalla.getProducto().getId_producto() > 0, "ID de producto invalido");
            assertNotNull(productoTalla.getTalla(), "La talla no debe ser nula");
            assertTrue(productoTalla.getCantidad() >= 0, "La cantidad no puede ser negativa");
        });
    }

    @Test
    public void eliminarTest(){
        productoTallaRepo.deleteById(5);

        ProductoTalla productoTalla = productoTallaRepo.findById(5).orElse(null);

        assertNull(productoTalla);
    }

}
