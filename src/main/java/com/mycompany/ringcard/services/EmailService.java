package com.mycompany.ringcard.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.Properties;

public class EmailService {
    
    // Configura aquí tu correo desde el cual saldrán los avisos
    private final String remitente = "tu_correo@gmail.com"; 
    private final String passwordApp = "TU_CONTRASEÑA_DE_APLICACION_DE_16_LETRAS"; 

    public boolean enviarEstadoDeCuenta(String destinatario, String asunto, String cuerpo, String rutaArchivoAdjunto) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, passwordApp);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // Parte 1: El texto del correo
            MimeBodyPart textoBodyPart = new MimeBodyPart();
            textoBodyPart.setText(cuerpo);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textoBodyPart);

            // Parte 2: El archivo adjunto
            if (rutaArchivoAdjunto != null && !rutaArchivoAdjunto.isEmpty()) {
                File adjunto = new File(rutaArchivoAdjunto);
                if (adjunto.exists()) {
                    MimeBodyPart archivoBodyPart = new MimeBodyPart();
                    archivoBodyPart.attachFile(adjunto);
                    multipart.addBodyPart(archivoBodyPart);
                }
            }

            message.setContent(multipart);
            Transport.send(message);
            return true;

        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            return false;
        }
    }
}