package tani.dto.productotalla;

public record InformacionProductoTallaDTO(
        int id,
        int productoId,
        String talla,
        int cantidad
) {
}