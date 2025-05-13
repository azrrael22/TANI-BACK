package tani.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tani.dto.producto.ProductoConTallasDTO;
import tani.dto.producto.RegistroProductoDTO;
import tani.dto.producto.InformacionProductoDTO;
import tani.dto.productotalla.RegistroProductoTallaDTO;
import tani.model.entities.Producto;
import tani.services.interfaces.ImagenesServicio;
import tani.services.interfaces.ProductoServicio;
import tani.services.interfaces.ProductoTallaServicio;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoServicio productoServicio;
    private final ProductoTallaServicio productoTallaServicio;

    private final ImagenesServicio servicioImagen;


    @PostMapping(value = "/crear-producto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> crearProducto(
            @RequestPart("producto") ProductoConTallasDTO productoConTallasDTO,
            @RequestPart("file") MultipartFile file) throws Exception {

        // Subir la imagen
        String nombreArchivo = servicioImagen.subirImagen(file);

        // Obtener el DTO original
        RegistroProductoDTO original = productoConTallasDTO.getRegistroProductoDTO();

        // Crear una nueva instancia con la imagen actualizada
        RegistroProductoDTO nuevoDTO = new RegistroProductoDTO(
                original.nombre(),
                original.descripcion(),
                original.tipoCalzado(),
                nombreArchivo,
                original.precio()
        );

        // Guardar el producto
        InformacionProductoDTO info = productoServicio.crearProducto(nuevoDTO);

        // Guardar las tallas asociadas
        productoTallaServicio.crearProductoTallas(productoConTallasDTO.getTallas(), info);

        // Respuesta
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Producto creado exitosamente");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductoConTallasDTO>> listarProductosConTallas() {
        // Obtener todos los productos
        List<InformacionProductoDTO> productos = productoServicio.listarProductos();

        // Mapear cada producto con sus tallas asociadas
        List<ProductoConTallasDTO> respuesta = productos.stream()
                .map(producto -> {
                    RegistroProductoDTO registroProductoDTO = new RegistroProductoDTO(
                            producto.nombre(),
                            producto.descripcion(),
                            producto.tipoCalzado(),
                            producto.imagen(),
                            producto.precio()
                    );

                    List<RegistroProductoTallaDTO> tallas = productoTallaServicio.obtenerTallasPorProductoId(producto.idProducto());

                    return new ProductoConTallasDTO(registroProductoDTO, tallas);
                })
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @PutMapping(value = "/editar-producto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> editarProducto(
            @RequestPart("productoNombre") String productoNombre,
            @RequestPart("productoConTallasDTO") ProductoConTallasDTO productoConTallasDTO) {

        // Actualizar los datos del producto
        RegistroProductoDTO original = productoConTallasDTO.getRegistroProductoDTO();
        InformacionProductoDTO nuevo = productoServicio.editarProducto(original, productoNombre);

        // Actualizar las tallas asociadas
        productoTallaServicio.editarProductoTalla(productoConTallasDTO.getTallas(), nuevo);

        // Respuesta
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Producto editado exitosamente");
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/eliminar-producto")
    public ResponseEntity<Map<String, String>> eliminarProducto(@RequestParam String nombre) {
        System.out.println("producto a eliminar:" + nombre);
        productoServicio.eliminarProductoPorNombre(nombre);

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Producto eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

}