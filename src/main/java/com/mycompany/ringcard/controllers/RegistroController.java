package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.rg;
import com.mycompany.ringcard.Lg;
import com.mycompany.ringcard.dao.IUsuarioDAO;
import com.mycompany.ringcard.models.Usuario;
import com.mycompany.ringcard.services.EmailService;

import javax.swing.JOptionPane;
import java.awt.Container;
import java.util.Random;

public class RegistroController {
    private rg vista;
    private IUsuarioDAO dao;
    private EmailService emailService;

    public RegistroController(rg vista, IUsuarioDAO dao) {
        this.vista = vista;
        this.dao = dao;
        this.emailService = new EmailService();
        initController();
    }

    private void initController() {
        // Enlazamos el botón "Registrar" con nuestra lógica
        vista.getBtnRegistrar().addActionListener(e -> registrarUsuario());
    }

    private void registrarUsuario() {
        try {
            // 1. Extraemos los datos y validamos que el correo no esté vacío
            String email = vista.getTxtem().getText().trim();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "El campo de correo electrónico es obligatorio.");
                return;
            }

            Usuario u = new Usuario();
            u.setNombre(vista.getTxtn().getText());
            u.setAp(vista.getTxtap().getText());
            u.setAm(vista.getTxtam().getText());
            u.setEmail(email);
            u.setPass(vista.getTxtpas().getText());
            u.setTelefono(Integer.parseInt(vista.getTxttel().getText()));

            // 2. Generamos un código aleatorio de 6 dígitos
            String codigoGenerado = String.format("%06d", new Random().nextInt(999999));

            // 3. Preparamos y enviamos el correo
            String asunto = "Código de Verificación";
            String cuerpo = "Hola " + u.getNombre() + ",\n\n"
                          + "Tu código de verificación para completar el registro es: " + codigoGenerado + "\n\n"
                          + "Si no solicitaste este registro, ignora este mensaje.";

            // Mostramos un mensaje de espera opcional, ya que enviar el correo toma unos 2-3 segundos
            System.out.println("Enviando correo de verificación a: " + email); 
            boolean correoEnviado = emailService.enviarCorreoSimple(email, asunto, cuerpo);

            if (!correoEnviado) {
                JOptionPane.showMessageDialog(vista, "Error al enviar el código de verificación. Revisa tu conexión o asegúrate de que el correo sea válido.", "Error de Envío", JOptionPane.ERROR_MESSAGE);
                return; // Cortamos el flujo, no se registra
            }

            // 4. Le pedimos el código al usuario
            String codigoIngresado = JOptionPane.showInputDialog(vista, 
                "Se ha enviado un código de 6 dígitos a:\n" + email + "\n\nPor favor, ingrésalo a continuación:", 
                "Verificación de Correo", 
                JOptionPane.QUESTION_MESSAGE);

            // 5. Validaciones de la respuesta del usuario
            if (codigoIngresado == null || codigoIngresado.trim().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Registro cancelado por el usuario.");
                return; 
            }

            if (!codigoIngresado.trim().equals(codigoGenerado)) {
                JOptionPane.showMessageDialog(vista, "El código ingresado es incorrecto. Registro cancelado.", "Error de Verificación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 6. Si el código coincide, ahora sí procedemos a guardar en la base de datos
            if (dao.insertarUsuario(u)) {
                JOptionPane.showMessageDialog(vista, "¡Registrado y verificado correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                volverAlLogin();
            } else {
                JOptionPane.showMessageDialog(vista, "Datos inválidos o el correo ya existe en la base de datos.", "Error de Registro", JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El teléfono debe ser un número válido, sin guiones ni espacios.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al registrar: " + ex.getMessage());
        }
    }

    private void volverAlLogin() {
        Container contenedorPadre = vista.getParent();
        if (contenedorPadre != null) {
            contenedorPadre.removeAll();
            Lg panelLogin = new Lg();
            panelLogin.setSize(contenedorPadre.getSize());
            panelLogin.setLocation(0, 0);
            contenedorPadre.add(panelLogin);
            contenedorPadre.revalidate();
            contenedorPadre.repaint();
        }
    }
}