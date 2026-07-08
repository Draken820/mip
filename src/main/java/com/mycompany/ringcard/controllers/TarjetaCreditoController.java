package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.TarjetasAddC;
import com.mycompany.ringcard.home;
import com.mycompany.ringcard.dao.ITarjetaCreditoDAO;
import com.mycompany.ringcard.models.TarjetasCred;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Window;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TarjetaCreditoController {
    private TarjetasAddC vista;
    private ITarjetaCreditoDAO dao;
    private int idUsuario;

    public TarjetaCreditoController(TarjetasAddC vista, ITarjetaCreditoDAO dao, int idUsuario) {
        this.vista = vista;
        this.dao = dao;
        this.idUsuario = idUsuario;
        initController();
    }

    private void initController() {
        // Escuchamos el clic del botón "Agregar" de la vista
        this.vista.getBtnGuardar().addActionListener(e -> guardarTarjeta());
    }

    private void guardarTarjeta() {
        try {
            TarjetasCred tCred = new TarjetasCred();
            tCred.setBanco(vista.getTxtBanco().getText());
            tCred.setCantidadab(((Number) vista.getSpnAbonado().getValue()).doubleValue());
            tCred.setPctinteres(((Number) vista.getSpnInteres().getValue()).intValue());

            String textoFecha = vista.getTxtFecha().getText();
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaParseada = formatoEntrada.parse(textoFecha);
            tCred.setFecha_vencimiento(new java.sql.Date(fechaParseada.getTime()));

            tCred.setEstado(vista.getCmbEstado().getSelectedItem().toString());
            tCred.setSaldo_actual(((Number) vista.getSpnSaldo().getValue()).doubleValue());
            tCred.setLimite_credito(((Number) vista.getSpnLimite().getValue()).doubleValue());
            tCred.setFecha_corte(((Number) vista.getSpnCorte().getValue()).intValue());
            tCred.setId_usuario(idUsuario);

            // Llamamos al DAO
            if (dao.insertarTarjeta(tCred)) {
                JOptionPane.showMessageDialog(vista, "Tarjeta de crédito agregada con éxito.");
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
            JOptionPane.showMessageDialog(vista, "Ingresa la fecha en formato DD/MM/YYYY (ej. 06/06/2026).", "Error en la Fecha", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Revisa los datos numéricos ingresados.", "Error de formato", JOptionPane.WARNING_MESSAGE);
        }
    }
}