package tani.services.impl;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tani.dto.otros.EmailDTO;
import tani.services.interfaces.EmailServicio;

@Service
public class EmailServicioImpl implements EmailServicio {

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.password}")
    private String emailPassword;

    @Override
    @Async
    public void enviarCorreo(EmailDTO emailDTO) throws Exception {


        Email email = EmailBuilder.startingBlank()
                .from(emailUsername)
                .to(emailDTO.getDestinatario())
                .withSubject(emailDTO.getAsunto())
                .withHTMLText(emailDTO.getCuerpo())
                .buildEmail();


        try (Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, emailUsername, emailPassword)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer()) {


            mailer.sendMail(email);
        }


    }

    public String generarCodigoCuenta(){
        String codigo="";
        int bajo=65;
        int alto=90;
        String valor="";
        for (int i=0;i<6;i++){
            int r= (int) (Math.random()*(1+alto-bajo)+bajo);

            char c= (char)r;

            valor=c+"";
            if(Math.random()>0.6){
                valor=valor.toLowerCase();
            }
            codigo+=valor;
        }

        return codigo;
    }
}