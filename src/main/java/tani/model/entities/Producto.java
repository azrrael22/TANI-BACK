package tani.model.entities;
import jakarta.persistence.*;
import lombok.*;
import tani.model.enums.TIPO_CALZADO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id_producto;

    private String nombre;

    private String descripcion;


    @Enumerated(EnumType.STRING)
    private TIPO_CALZADO tipoCalzado;

    private String imagen;
    private float precio;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoTalla> tallas = new ArrayList<>();


    @Builder
    public Producto(String nombre, String descripcion, TIPO_CALZADO tipoCalzado, String imagen, float precio, List<ProductoTalla> tallas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoCalzado = tipoCalzado;
        this.imagen = imagen;
        this.precio = precio;
        this.tallas = tallas;
    }
}
