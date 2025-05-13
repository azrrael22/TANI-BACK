package tani.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tani.dto.pedido.InformacionPedidoDTO;
import tani.dto.pedido.RegistroPedidoDTO;
import tani.services.interfaces.DetallePedidoServicio;
import tani.services.interfaces.PedidoServicio;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoServicio pedidoServicio;
    private final DetallePedidoServicio detallePedidoServicio;

    @PostMapping("/crear-pedido")
    public ResponseEntity<InformacionPedidoDTO> crearPedido(@Valid @RequestBody RegistroPedidoDTO registroPedidoDTO) {
        InformacionPedidoDTO pedidoCreado = pedidoServicio.crearPedido(registroPedidoDTO);
        return new ResponseEntity<>(pedidoCreado, HttpStatus.CREATED);
    }

    @GetMapping("/usuario/{idUsuario}/carrito")
    public ResponseEntity<InformacionPedidoDTO> obtenerCarrito(@PathVariable String idUsuario) {
        InformacionPedidoDTO pedidoPendiente = pedidoServicio.obtenerCarrito(idUsuario);
        return ResponseEntity.ok(pedidoPendiente);
    }

    @PutMapping("/detalle/{idDetalle}/cantidad")
    public ResponseEntity<Void> actualizarCantidadDetalle(
            @PathVariable int idDetalle,
            @RequestParam int cantidad) {
        detallePedidoServicio.actualizarCantidadDetalle(idDetalle, cantidad);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/detalle/{idDetalle}")
    public ResponseEntity<Void> eliminarDetallePedido(@PathVariable int idDetalle) {
        detallePedidoServicio.eliminarDetalle(idDetalle);
        return ResponseEntity.noContent().build();
    }
}