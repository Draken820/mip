package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.MovimientosAddC;
import com.mycompany.ringcard.dao.IMovimientoDAO;
import com.mycompany.ringcard.models.Movimiento;
import com.mycompany.ringcard.services.EstadoCuentaService;

import javax.swing.JOptionPane;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MovimientoCreditoController {

    private MovimientosAddC vista;
    private IMovimientoDAO dao;
    private int idTarjeta;

    public MovimientoCreditoController(MovimientosAddC vista, IMovimientoDAO dao, int idTarjeta) {
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
// --- NUEVA VALIDACIÓN FINANCIERA ---
            com.mycompany.ringcard.services.ValidacionFinancieraService validador = new com.mycompany.ringcard.services.ValidacionFinancieraService(dao);
            String estadoValidacion = validador.validarGasto(idTarjeta, "credito", mov.getTipoMovimiento(), monto);

            if (estadoValidacion.equals("LIMITE_EXCEDIDO")) {
                JOptionPane.showMessageDialog(vista, "Transacción rechazada: Límite excedido.\nEsta compra supera el límite de crédito de tu tarjeta.", "Operación Denegada", JOptionPane.WARNING_MESSAGE);
                return; // Corta la ejecución
            }
            // -----------------------------------

            if (dao.insertarMovimientoCredito(mov)) {
                //... (resto de tu código que ya tienes)
                JOptionPane.showMessageDialog(vista, "Movimiento de crédito registrado en la base de datos.");

                // === GENERACIÓN DEL ESTADO DE CUENTA ===
                try {
                    EstadoCuentaService docService = new EstadoCuentaService(dao);
                    docService.actualizarEstadoCuenta(idTarjeta, vista.getNombreBanco(), "credito");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, "No se pudo generar el DOCX: " + ex.getMessage(), "Error DOCX", JOptionPane.ERROR_MESSAGE);
                }
                // =======================================

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
