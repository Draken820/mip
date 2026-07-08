package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.PanleMovimientos;
import com.mycompany.ringcard.dao.IMovimientoDAO;
import com.mycompany.ringcard.services.TarjetaService;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

public class PanelMovimientosController {
    private PanleMovimientos vista;
    private TarjetaService tarjetaService;
    private IMovimientoDAO movimientoDAO;
    private int idUsuario;

    public PanelMovimientosController(PanleMovimientos vista, TarjetaService tarjetaService, IMovimientoDAO movimientoDAO, int idUsuario) {
        this.vista = vista;
        this.tarjetaService = tarjetaService;
        this.movimientoDAO = movimientoDAO;
        this.idUsuario = idUsuario;
    }

    public void cargarDatosCarrusel(ArrayList<String> nombres, ArrayList<Integer> ids, ArrayList<String> tipos, ArrayList<String> bancosOriginales) {
        // Delegamos la lógica de negocio al servicio
        tarjetaService.cargarTarjetas(idUsuario, nombres, ids, tipos, bancosOriginales);
    }

    public void llenarTablaMovimientos(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0); // Limpiamos la tabla
        
        // Pedimos los datos al DAO en lugar de hacer la consulta aquí
        try (ResultSet rs = movimientoDAO.obtenerTodosLosMovimientos(idUsuario)) {
            if (rs != null) {
                while (rs.next()) {
                    Object[] fila = new Object[5];
                    fila[0] = rs.getDate("fecha_movimiento");
                    fila[1] = rs.getString("concepto");
                    fila[2] = "$" + String.format("%.2f", rs.getDouble("monto"));
                    fila[3] = rs.getString("tipo_movimiento").toUpperCase();
                    fila[4] = rs.getString("tarjeta");
                    modeloTabla.addRow(fila);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar movimientos: " + e.getMessage());
        }
    }

    public boolean eliminarTarjeta(String tabla, String banco) {
        return tarjetaService.borrarTarjeta(tabla, banco, idUsuario);
    }

    public boolean actualizarTarjeta(String tabla, String nuevoBanco, String bancoActual) {
        return tarjetaService.modificarTarjeta(tabla, nuevoBanco, bancoActual, idUsuario);
    }
}