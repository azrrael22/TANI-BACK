package tani.services.interfaces;

import tani.dto.producto.RegistroProductoDTO;
import tani.dto.producto.InformacionProductoDTO;
import tani.dto.producto.EditarProductoDTO;
import tani.dto.productotalla.RegistroProductoTallaDTO;

import java.util.List;

public interface ProductoServicio {
    InformacionProductoDTO crearProducto(RegistroProductoDTO productoDTO);
    InformacionProductoDTO obtenerProductoPorId(int id);
    List<InformacionProductoDTO> listarProductos();
    InformacionProductoDTO editarProducto(RegistroProductoDTO productoDTO, String productoNombre);
    void eliminarProductoPorNombre(String nombreProducto);
}