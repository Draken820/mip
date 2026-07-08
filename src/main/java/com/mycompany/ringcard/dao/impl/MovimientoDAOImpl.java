package com.mycompany.ringcard.dao.impl;

import com.mycompany.ringcard.dao.IMovimientoDAO;
import com.mycompany.ringcard.models.Movimiento;
import com.mycompany.ringcard.utils.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MovimientoDAOImpl implements IMovimientoDAO {

    @Override
    public boolean insertarMovimientoDebito(Movimiento mov) { return false; /* Movido al controlador en módulos anteriores */ }

    @Override
    public ArrayList<Movimiento> listarMovimientosDebito(int idCardDebito) {
        ArrayList<Movimiento> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM movimientos_debito WHERE id_carddebito = ? ORDER BY fecha_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idCardDebito);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movimiento mov = new Movimiento();
                mov.setIdMovimiento(rs.getInt("id_movimiento"));
                mov.setIdCardDebito(rs.getInt("id_carddebito"));
                mov.setFechaMovimiento(rs.getDate("fecha_movimiento"));
                mov.setConcepto(rs.getString("concepto"));
                mov.setMonto(rs.getDouble("monto"));
                mov.setTipoMovimiento(rs.getString("tipo_movimiento"));
                lista.add(mov);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean eliminarMovimientoDebito(int idMovimiento) { return false; }

    @Override
    public boolean insertarMovimientoCredito(Movimiento mov) { return false; }

    @Override
    public ArrayList<Movimiento> listarMovimientosCredito(int idCardCredito) {
        ArrayList<Movimiento> lista = new ArrayList<>();
        try {
            String sql = "SELECT * FROM movimientos_credito WHERE id_cardcredito = ? ORDER BY fecha_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idCardCredito);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movimiento mov = new Movimiento();
                mov.setIdMovimiento(rs.getInt("id_movimiento"));
                mov.setIdCardDebito(rs.getInt("id_cardcredito")); 
                mov.setFechaMovimiento(rs.getDate("fecha_movimiento"));
                mov.setConcepto(rs.getString("concepto"));
                mov.setMonto(rs.getDouble("monto"));
                mov.setTipoMovimiento(rs.getString("tipo_movimiento"));
                lista.add(mov);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public boolean eliminarMovimientoCredito(int idMovimiento) { return false; }

    // --- MÉTODOS PARA EL DASHBOARD EN HOME ---
    @Override
    public ResultSet obtenerTodosLosMovimientos(int idUsuario) {
        try {
            String sql = "SELECT mc.fecha_movimiento, mc.concepto, mc.monto, mc.tipo_movimiento, 'Credito' AS tarjeta, mc.id_movimiento FROM movimientos_credito mc INNER JOIN cardscredito cc ON mc.id_cardcredito = cc.id_cardcredito WHERE cc.id_usuario = ? UNION ALL SELECT md.fecha_movimiento, md.concepto, md.monto, md.tipo_movimiento, 'Debito' AS tarjeta, md.id_movimiento FROM movimientos_debito md INNER JOIN cardsdebito cd ON md.id_carddebito = cd.id_carddebito WHERE cd.id_usuario = ? ORDER BY fecha_movimiento DESC, id_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    @Override
    public ResultSet obtenerEgresos(int idUsuario) {
        try {
            String sql = "SELECT mc.fecha_movimiento, mc.concepto, mc.monto, mc.tipo_movimiento, 'Credito' AS tarjeta FROM movimientos_credito mc INNER JOIN cardscredito cc ON mc.id_cardcredito = cc.id_cardcredito WHERE cc.id_usuario = ? AND UPPER(mc.tipo_movimiento) = 'EGRESO' UNION ALL SELECT md.fecha_movimiento, md.concepto, md.monto, md.tipo_movimiento, 'Debito' AS tarjeta FROM movimientos_debito md INNER JOIN cardsdebito cd ON md.id_carddebito = cd.id_carddebito WHERE cd.id_usuario = ? AND UPPER(md.tipo_movimiento) = 'EGRESO' ORDER BY fecha_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    @Override
    public ResultSet obtenerIngresos(int idUsuario) {
        try {
            String sql = "SELECT mc.fecha_movimiento, mc.concepto, mc.monto, mc.tipo_movimiento, 'Credito' AS tarjeta FROM movimientos_credito mc INNER JOIN cardscredito cc ON mc.id_cardcredito = cc.id_cardcredito WHERE cc.id_usuario = ? AND UPPER(mc.tipo_movimiento) = 'INGRESO' UNION ALL SELECT md.fecha_movimiento, md.concepto, md.monto, md.tipo_movimiento, 'Debito' AS tarjeta FROM movimientos_debito md INNER JOIN cardsdebito cd ON md.id_carddebito = cd.id_carddebito WHERE cd.id_usuario = ? AND UPPER(md.tipo_movimiento) = 'INGRESO' ORDER BY fecha_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    public ResultSet obtenerSoloCredito(int idUsuario) {
        try {
            String sql = "SELECT mc.fecha_movimiento, mc.concepto, mc.monto, mc.tipo_movimiento, 'Credito' AS tarjeta FROM movimientos_credito mc INNER JOIN cardscredito cc ON mc.id_cardcredito = cc.id_cardcredito WHERE cc.id_usuario = ? ORDER BY mc.fecha_movimiento DESC, mc.id_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    public ResultSet obtenerSoloDebito(int idUsuario) {
        try {
            String sql = "SELECT md.fecha_movimiento, md.concepto, md.monto, md.tipo_movimiento, 'Debito' AS tarjeta FROM movimientos_debito md INNER JOIN cardsdebito cd ON md.id_carddebito = cd.id_carddebito WHERE cd.id_usuario = ? ORDER BY md.fecha_movimiento DESC, md.id_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    public ResultSet obtenerTarjetasDashboard(int idUsuario) {
        try {
            String sql = "SELECT id_cardcredito AS id_tarjeta, banco, 'Credito' AS tipo, saldo_actual, limite_credito, cantidadabonada, fecha_corte, fecha_vencimiento, estado FROM cardscredito WHERE id_usuario = ? UNION ALL SELECT id_carddebito AS id_tarjeta, banco, 'Debito' AS tipo, saldo_actual, 0 AS limite_credito, 0 AS cantidadabonada, 0 AS fecha_corte, NULL AS fecha_vencimiento, 'N/A' AS estado FROM cardsdebito WHERE id_usuario = ? ORDER BY tipo, banco";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) { e.printStackTrace(); return null; }
    }
}