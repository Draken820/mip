package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.TarjetasAddD;
import com.mycompany.ringcard.home;
import com.mycompany.ringcard.dao.ITarjetaDebitoDAO;
import com.mycompany.ringcard.models.TarjetasDeb;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Window;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TarjetaDebitoController {
    private TarjetasAddD vista;
    private ITarjetaDebitoDAO dao;
    private int idUsuario;

    public TarjetaDebitoController(TarjetasAddD vista, ITarjetaDebitoDAO dao, int idUsuario) {
        this.vista = vista;
        this.dao = dao;
        this.idUsuario = idUsuario;
        initController();
    }

    private void initController() {
        this.vista.getBtnGuardar().addActionListener(e -> guardarTarjeta());
    }

    private void guardarTarjeta() {
        try {
            TarjetasDeb tDeb = new TarjetasDeb();
            tDeb.setBanco(vista.getTxtBanco().getText());
            
            String textoFecha = vista.getTxtFecha().getText();
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaParseada = formatoEntrada.parse(textoFecha);
            tDeb.setFecha_vencimiento(new java.sql.Date(fechaParseada.getTime()));
            
            tDeb.setSaldo_actual(((Number) vista.getSpnSaldo().getValue()).intValue());
            tDeb.setId_usuario(idUsuario);
            
            if (dao.insertarTarjeta(tDeb)) {
                JOptionPane.showMessageDialog(vista, "Tarjeta de Débito agregada con éxito.");
                home ventanaHome = new home(idUsuario);
                ventanaHome.setVisible(true);

                Window ventanaPadre = SwingUtilities.getWindowAncestor(vista);
                if (ventanaPadre != null) {
                    ventanaPadre.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(vista, "Ingresa la fecha en formato DD/MM/YYYY.", "Error en la Fecha", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Revisa los datos ingresados.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}