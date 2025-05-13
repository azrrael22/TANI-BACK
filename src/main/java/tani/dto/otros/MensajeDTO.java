package tani.dto.otros;

public record MensajeDTO <T> (
    boolean error,
    T respuesta
) {
}
