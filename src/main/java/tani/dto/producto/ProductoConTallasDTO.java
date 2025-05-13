package tani.dto.producto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import tani.dto.productotalla.RegistroProductoTallaDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductoConTallasDTO {
    @NotNull
    private RegistroProductoDTO registroProductoDTO;

    @NotNull
    private List<RegistroProductoTallaDTO> tallas;
}