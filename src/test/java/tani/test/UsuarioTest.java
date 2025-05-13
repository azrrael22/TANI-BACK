package tani.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tani.model.entities.Usuario;
import tani.model.enums.TIPO_USUARIO;
import tani.repositories.UsuarioRepo;

import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UsuarioTest {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Test
    public void registrarTest(){
        Usuario usuario = Usuario.builder()
                .nombre("Carlos")
                .telefono("315315")
                .correo("carlos@gmail.com")
                .contrasenia("654321")
                .tipoUsuario(TIPO_USUARIO.USUARIO)
                .fechaNacimiento(LocalDate.of(2000, 11, 11))
                .build();

        Usuario usuarioRegistrado = usuarioRepo.save(usuario);
        assertNotNull(usuarioRegistrado);
    }

    @Test
    public void actualizarTest(){
        Usuario usuario = usuarioRepo.findByCorreo("juanito@gmail.com").orElseThrow();
        usuario.setNombre("Juan");
        usuario.setTelefono("312312");
        usuario.setFechaNacimiento(LocalDate.of(1990, 1, 2));

        usuarioRepo.save(usuario);
        Usuario usuarioActualizado = usuarioRepo.findByCorreo("juanito@gmail.com").orElseThrow();
        assertEquals("312312", usuarioActualizado.getTelefono());
    }

    @Test
    public void listarTodosTest(){
        List<Usuario> lista = usuarioRepo.findAll();

        lista.forEach(System.out::println);

        assertEquals(2, lista.size());
    }

    @Test
    public void eliminarTest(){
        usuarioRepo.deleteById(3);

        Usuario usuario = usuarioRepo.findById(3).orElse(null);

        assertNull(usuario);
    }
}
