package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.rg;
import com.mycompany.ringcard.Lg;
import com.mycompany.ringcard.dao.IUsuarioDAO;
import com.mycompany.ringcard.models.Usuario;

import javax.swing.JOptionPane;
import java.awt.Container;

public class RegistroController {
    private rg vista;
    private IUsuarioDAO dao;

    public RegistroController(rg vista, IUsuarioDAO dao) {
        this.vista = vista;
        this.dao = dao;
        initController();
    }

    private void initController() {
        // Enlazamos el botón "Registrar" con nuestra lógica
        vista.getBtnRegistrar().addActionListener(e -> registrarUsuario());
    }

    private void registrarUsuario() {
        try {
            Usuario u = new Usuario();
            u.setNombre(vista.getTxtn().getText());
            u.setAp(vista.getTxtap().getText());
            u.setAm(vista.getTxtam().getText());
            u.setEmail(vista.getTxtem().getText());
            u.setPass(vista.getTxtpas().getText());
            u.setTelefono(Integer.parseInt(vista.getTxttel().getText()));

            // Llamamos al DAO que inserta en la base de datos
            if (dao.insertarUsuario(u)) {
                JOptionPane.showMessageDialog(vista, "Registrado correctamente");
                volverAlLogin();
            } else {
                JOptionPane.showMessageDialog(vista, "Datos inválidos o el correo ya existe");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El teléfono debe ser un número válido, sin guiones ni espacios.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error al registrar: " + ex.getMessage());
        }
    }

    private void volverAlLogin() {
        // Lógica para cambiar de panel dentro de tu contenedor principal
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