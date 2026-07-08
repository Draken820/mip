package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.home;
import com.mycompany.ringcard.dao.IMovimientoDAO;
import java.sql.ResultSet;

public class DashboardController {
    private home vista;
    private IMovimientoDAO movimientoDAO;
    private int idUsuario;

    public DashboardController(home vista, IMovimientoDAO movimientoDAO, int idUsuario) {
        this.vista = vista;
        this.movimientoDAO = movimientoDAO;
        this.idUsuario = idUsuario;
    }

    public void calcularEstadisticasPrincipales() {
        double totalIngresos = 0;
        double totalEgresos = 0;

        // 1. Calcular Ingresos
        try (ResultSet rsIngresos = movimientoDAO.obtenerIngresos(idUsuario)) {
            if (rsIngresos != null) {
                while (rsIngresos.next()) {
                    totalIngresos += rsIngresos.getDouble("monto");
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        // 2. Calcular Egresos
        try (ResultSet rsEgresos = movimientoDAO.obtenerEgresos(idUsuario)) {
            if (rsEgresos != null) {
                while (rsEgresos.next()) {
                    totalEgresos += rsEgresos.getDouble("monto");
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        double saldoActual = totalIngresos - totalEgresos;

        // 3. Mandar la información calculada a la Vista
        vista.getLblIngresos().setText("$" + String.format("%.2f", totalIngresos));
        vista.getLblEgresos().setText("$" + String.format("%.2f", totalEgresos));
        vista.getLblSaldo().setText("$" + String.format("%.2f", saldoActual));
    }
    
    // Aquí puedes agregar un método público public ResultSet obtenerDatosGrafica() 
    // si usas JFreeChart para delegarle los datos a la vista sin pasar conexiones.
}