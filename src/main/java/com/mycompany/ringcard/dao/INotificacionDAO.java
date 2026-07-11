package com.mycompany.ringcard.dao;

import java.time.LocalDate;

public interface INotificacionDAO {
    boolean registrarNotificacion(int idUsuario, String mensaje, String tipoAlerta);
    boolean yaSeNotificoEsteMes(int idTarjeta, String tipoTarjeta, int mes, int anio);
    boolean registrarEstadoCuentaEnviado(int idTarjeta, String tipoTarjeta, String rutaArchivo, int mes, int anio);
}