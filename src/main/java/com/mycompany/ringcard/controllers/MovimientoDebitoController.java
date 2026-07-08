package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.MovimientosAddD;
import com.mycompany.ringcard.dao.IMovimientoDAO;
import com.mycompany.ringcard.models.Movimiento;
import javax.swing.JOptionPane;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MovimientoDebitoController {
    private MovimientosAddD vista;
    private IMovimientoDAO dao;
    private int idTarjeta;

    public MovimientoDebitoController(MovimientosAddD vista, IMovimientoDAO dao, int idTarjeta) {
        this.vista = vista;
        this.dao = dao;
        this.idTarjeta = idTarjeta;
        initController();
    }

    private void initController() {
        this.vista.getBtnGuardar().addActionListener(e -> guardarMovimiento());
    }

    private void guardarMovimiento() {
        try {
            Movimiento mov = new Movimiento();
            mov.setIdCardDebito(idTarjeta);
            mov.setTipoMovimiento(vista.getCmbTipo().getSelectedItem().toString().toLowerCase());
            mov.setConcepto(vista.getTxtConcepto().getText());
            
            double monto = ((Number) vista.getSpnMonto().getValue()).doubleValue();
            if (monto <= 0) {
                JOptionPane.showMessageDialog(vista, "El monto debe ser mayor a cero.");
                return;
            }
            mov.setMonto(monto);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fechaParseada = sdf.parse(vista.getTxtFecha().getText());
            mov.setFechaMovimiento(new java.sql.Date(fechaParseada.getTime()));

            if (dao.insertarMovimientoDebito(mov)) {
                JOptionPane.showMessageDialog(vista, "Movimiento registrado y saldo actualizado con éxito.");
                vista.volverAtras();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar el movimiento en la base de datos.");
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(vista, "Formato de fecha inválido. Usa el formato DD/MM/YYYY");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Error procesando los datos: " + ex.getMessage());
        }
    }
}