package com.mycompany.ringcard.dao;

import com.mycompany.ringcard.models.Movimiento;
import java.util.ArrayList;

public interface IMovimientoDAO {
    // Para Débito
    boolean insertarMovimientoDebito(Movimiento mov);
    ArrayList<Movimiento> listarMovimientosDebito(int idCardDebito);
    boolean eliminarMovimientoDebito(int idMovimiento);
    // Método para validaciones financieras
    double[] obtenerSaldosTarjeta(int idTarjeta, String tipoTarjeta);
    // Para Crédito
    boolean insertarMovimientoCredito(Movimiento mov);
    ArrayList<Movimiento> listarMovimientosCredito(int idCardCredito);
    boolean eliminarMovimientoCredito(int idMovimiento);
    
    // Consultas generales para Dashboard y Reportes
    java.sql.ResultSet obtenerTodosLosMovimientos(int idUsuario);
    java.sql.ResultSet obtenerIngresos(int idUsuario);
    java.sql.ResultSet obtenerEgresos(int idUsuario);
    
    // --- ESTOS SON LOS MÉTODOS QUE FALTABAN AGREGAR AL CONTRATO ---
    java.sql.ResultSet obtenerSoloCredito(int idUsuario);
    java.sql.ResultSet obtenerSoloDebito(int idUsuario);
    java.sql.ResultSet obtenerTarjetasDashboard(int idUsuario);
}