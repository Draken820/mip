package com.mycompany.ringcard.dao.impl;

import com.mycompany.ringcard.dao.INotificacionDAO;
import com.mycompany.ringcard.utils.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NotificacionDAOImpl implements INotificacionDAO {

    @Override
    public boolean registrarNotificacion(int idUsuario, String mensaje, String tipoAlerta) {
        String sql = "INSERT INTO notificacion (id_usuario, mensaje, tipo_alerta, fecha_envio) VALUES (?, ?, ?, CURRENT_DATE)";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setString(2, mensaje);
            ps.setString(3, tipoAlerta);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean yaSeNotificoEsteMes(int idTarjeta, String tipoTarjeta, int mes, int anio) {
        String sql = "SELECT id_estado_cuenta FROM estado_cuenta WHERE id_tarjeta = ? AND tipo_tarjeta = ? AND mes = ? AND anio = ?";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, idTarjeta);
            ps.setString(2, tipoTarjeta);
            ps.setInt(3, mes);
            ps.setInt(4, anio);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Retorna true si ya existe un registro este mes
            }
        } catch (Exception e) {
            return false; // Si hay error, asumimos que no para intentar de nuevo
        }
    }

    @Override
    public boolean registrarEstadoCuentaEnviado(int idTarjeta, String tipoTarjeta, String rutaArchivo, int mes, int anio) {
        String sql = "INSERT INTO estado_cuenta (id_tarjeta, tipo_tarjeta, ruta_archivo, mes, anio, fecha_generacion) VALUES (?, ?, ?, ?, ?, CURRENT_DATE)";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, idTarjeta);
            ps.setString(2, tipoTarjeta);
            ps.setString(3, rutaArchivo);
            ps.setInt(4, mes);
            ps.setInt(5, anio);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}