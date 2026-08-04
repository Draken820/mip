package com.mycompany.ringcard.services;

import com.mycompany.ringcard.dao.IMovimientoDAO;

public class ValidacionFinancieraService {
    
    private IMovimientoDAO dao;

    // SOLID: Inyección de Dependencias (DIP). Dependemos de la abstracción, no de la implementación.
    public ValidacionFinancieraService(IMovimientoDAO dao) {
        this.dao = dao;
    }

    public String validarGasto(int idTarjeta, String tipoTarjeta, String tipoMovimiento, double monto) {
        // Si el usuario está metiendo dinero (ingreso o abono), siempre es válido.
        if (tipoMovimiento.equalsIgnoreCase("ingreso") || tipoMovimiento.equalsIgnoreCase("abono")) {
            return "VALIDO";
        }

        double[] saldos = dao.obtenerSaldosTarjeta(idTarjeta, tipoTarjeta);
        double saldoActual = saldos[0];
        double limiteCredito = saldos[1];

        if (tipoTarjeta.equalsIgnoreCase("debito")) {
            // Regla Débito: No puedes gastar más de lo que tienes.
            if (monto > saldoActual) {
                return "FONDOS_INSUFICIENTES";
            }
        } else if (tipoTarjeta.equalsIgnoreCase("credito")) {
            // Regla Crédito: Tu deuda actual + la nueva compra no puede superar tu límite.
            if ((saldoActual + monto) > limiteCredito) {
                return "LIMITE_EXCEDIDO";
            }
        }

        return "VALIDO";
    }
}