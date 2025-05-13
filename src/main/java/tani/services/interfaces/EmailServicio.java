package tani.services.interfaces;

import tani.dto.otros.EmailDTO;

public interface EmailServicio {

    void enviarCorreo(EmailDTO emailDTO) throws Exception;

    String generarCodigoCuenta();

}
