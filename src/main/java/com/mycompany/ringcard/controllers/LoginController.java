package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.Lg;
import com.mycompany.ringcard.home;
import com.mycompany.ringcard.dao.IUsuarioDAO;
import com.mycompany.ringcard.models.Usuario;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Window;

public class LoginController {
    private Lg vista;
    private IUsuarioDAO usuarioDAO;

    public LoginController(Lg vista, IUsuarioDAO usuarioDAO) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;
        initController();
    }

    private void initController() {
        // Enlazamos el botón de la vista con nuestro método de lógica
        this.vista.getBtnIngresar().addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String email = vista.getTxtEmail().getText();
        String pass = vista.getTxtPass().getText();

        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor llena todos los campos");
            return;
        }

        Usuario user = usuarioDAO.autenticarUsuario(email, pass);

        if (user != null) {
            JOptionPane.showMessageDialog(vista, "Inicio correcto"); // [cite: 1059]
            
            home ventanaHome = new home(user.getId_usuario()); // [cite: 1059]
            ventanaHome.setVisible(true); // [cite: 1059]
            
            Window ventanaPadre = SwingUtilities.getWindowAncestor(vista); // [cite: 1060]
            if (ventanaPadre != null) {
                ventanaPadre.dispose(); // [cite: 1060]
            }
        } else {
            JOptionPane.showMessageDialog(vista, "Credenciales incorrectas"); // [cite: 1061]
        }
    }
}