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
    public boolean insertarMovimientoDebito(Movimiento mov) {
        String sqlInsert = "INSERT INTO movimientos_debito (id_carddebito, tipo_movimiento, fecha_movimiento, concepto, monto) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdate = mov.getTipoMovimiento().equals("ingreso")
                ? "UPDATE cardsdebito SET saldo_actual = saldo_actual + ? WHERE id_carddebito = ?"
                : "UPDATE cardsdebito SET saldo_actual = saldo_actual - ? WHERE id_carddebito = ?";

        Connection cx = null;
        try {
            cx = ConexionDB.getInstance();
            cx.setAutoCommit(false); // Transacción segura

            try (PreparedStatement psInsert = cx.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, mov.getIdCardDebito());
                psInsert.setString(2, mov.getTipoMovimiento());
                psInsert.setDate(3, mov.getFechaMovimiento());
                psInsert.setString(4, mov.getConcepto());
                psInsert.setDouble(5, mov.getMonto());
                psInsert.executeUpdate();
            }

            try (PreparedStatement psUpdate = cx.prepareStatement(sqlUpdate)) {
                psUpdate.setDouble(1, mov.getMonto());
                psUpdate.setInt(2, mov.getIdCardDebito());
                psUpdate.executeUpdate();
            }

            cx.commit();
            return true;
        } catch (Exception e) {
            if (cx != null) try {
                cx.rollback();
            } catch (Exception ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            if (cx != null) try {
                cx.setAutoCommit(true);
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public boolean insertarMovimientoCredito(Movimiento mov) {
        String sqlInsert = "INSERT INTO movimientos_credito (id_cardcredito, tipo_movimiento, fecha_movimiento, concepto, monto) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdate = "";
        if (mov.getTipoMovimiento().equals("egreso")) {
            sqlUpdate = "UPDATE cardscredito SET saldo_actual = saldo_actual + ? WHERE id_cardcredito = ?";
        } else {
            sqlUpdate = "UPDATE cardscredito SET saldo_actual = saldo_actual - ?, cantidadabonada = cantidadabonada + ? WHERE id_cardcredito = ?";
        }

        Connection cx = null;
        try {
            cx = ConexionDB.getInstance();
            cx.setAutoCommit(false);

            try (PreparedStatement psInsert = cx.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, mov.getIdCardDebito());
                psInsert.setString(2, mov.getTipoMovimiento());
                psInsert.setDate(3, mov.getFechaMovimiento());
                psInsert.setString(4, mov.getConcepto());
                psInsert.setDouble(5, mov.getMonto());
                psInsert.executeUpdate();
            }

            try (PreparedStatement psUpdate = cx.prepareStatement(sqlUpdate)) {
                psUpdate.setDouble(1, mov.getMonto());
                if (mov.getTipoMovimiento().equals("egreso")) {
                    psUpdate.setInt(2, mov.getIdCardDebito());
                } else {
                    psUpdate.setDouble(2, mov.getMonto());
                    psUpdate.setInt(3, mov.getIdCardDebito());
                }
                psUpdate.executeUpdate();
            }

            cx.commit();
            return true;
        } catch (Exception e) {
            if (cx != null) try {
                cx.rollback();
            } catch (Exception ex) {
            }
            e.printStackTrace();
            return false;
        } finally {
            if (cx != null) try {
                cx.setAutoCommit(true);
            } catch (Exception ex) {
            }
        }
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public double[] obtenerSaldosTarjeta(int idTarjeta, String tipoTarjeta) {
        // Posición 0: saldo_actual, Posición 1: limite_credito
        double[] saldos = new double[]{0.0, 0.0};

        String sql = tipoTarjeta.equalsIgnoreCase("credito")
                ? "SELECT saldo_actual, limite_credito FROM cardscredito WHERE id_cardcredito = ?"
                : "SELECT saldo_actual, 0 AS limite_credito FROM cardsdebito WHERE id_carddebito = ?";
        try {
            java.sql.PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idTarjeta);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                saldos[0] = rs.getDouble("saldo_actual");
                saldos[1] = rs.getDouble("limite_credito");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saldos;
    }

    @Override
    public boolean eliminarMovimientoDebito(int idMovimiento) {
        return false;
    }

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean eliminarMovimientoCredito(int idMovimiento) {
        return false;
    }

    @Override
    public ResultSet obtenerTodosLosMovimientos(int idUsuario) {
        try {
            String sql = "SELECT mc.fecha_movimiento, mc.concepto, mc.monto, mc.tipo_movimiento, 'Credito' AS tarjeta, mc.id_movimiento FROM movimientos_credito mc INNER JOIN cardscredito cc ON mc.id_cardcredito = cc.id_cardcredito WHERE cc.id_usuario = ? UNION ALL SELECT md.fecha_movimiento, md.concepto, md.monto, md.tipo_movimiento, 'Debito' AS tarjeta, md.id_movimiento FROM movimientos_debito md INNER JOIN cardsdebito cd ON md.id_carddebito = cd.id_carddebito WHERE cd.id_usuario = ? ORDER BY fecha_movimiento DESC, id_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet obtenerEgresos(int idUsuario) {
        try {
            String sql = "SELECT mc.fecha_movimiento, mc.concepto, mc.monto, mc.tipo_movimiento, 'Credito' AS tarjeta FROM movimientos_credito mc INNER JOIN cardscredito cc ON mc.id_cardcredito = cc.id_cardcredito WHERE cc.id_usuario = ? AND UPPER(mc.tipo_movimiento) = 'EGRESO' UNION ALL SELECT md.fecha_movimiento, md.concepto, md.monto, md.tipo_movimiento, 'Debito' AS tarjeta FROM movimientos_debito md INNER JOIN cardsdebito cd ON md.id_carddebito = cd.id_carddebito WHERE cd.id_usuario = ? AND UPPER(md.tipo_movimiento) = 'EGRESO' ORDER BY fecha_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ResultSet obtenerIngresos(int idUsuario) {
        try {
            String sql = "SELECT mc.fecha_movimiento, mc.concepto, mc.monto, mc.tipo_movimiento, 'Credito' AS tarjeta FROM movimientos_credito mc INNER JOIN cardscredito cc ON mc.id_cardcredito = cc.id_cardcredito WHERE cc.id_usuario = ? AND UPPER(mc.tipo_movimiento) = 'INGRESO' UNION ALL SELECT md.fecha_movimiento, md.concepto, md.monto, md.tipo_movimiento, 'Debito' AS tarjeta FROM movimientos_debito md INNER JOIN cardsdebito cd ON md.id_carddebito = cd.id_carddebito WHERE cd.id_usuario = ? AND UPPER(md.tipo_movimiento) = 'INGRESO' ORDER BY fecha_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet obtenerSoloCredito(int idUsuario) {
        try {
            String sql = "SELECT mc.fecha_movimiento, mc.concepto, mc.monto, mc.tipo_movimiento, 'Credito' AS tarjeta FROM movimientos_credito mc INNER JOIN cardscredito cc ON mc.id_cardcredito = cc.id_cardcredito WHERE cc.id_usuario = ? ORDER BY mc.fecha_movimiento DESC, mc.id_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet obtenerSoloDebito(int idUsuario) {
        try {
            String sql = "SELECT md.fecha_movimiento, md.concepto, md.monto, md.tipo_movimiento, 'Debito' AS tarjeta FROM movimientos_debito md INNER JOIN cardsdebito cd ON md.id_carddebito = cd.id_carddebito WHERE cd.id_usuario = ? ORDER BY md.fecha_movimiento DESC, md.id_movimiento DESC";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet obtenerTarjetasDashboard(int idUsuario) {
        try {
            String sql = "SELECT id_cardcredito AS id_tarjeta, banco, 'Credito' AS tipo, saldo_actual, limite_credito, cantidadabonada, fecha_corte, fecha_vencimiento, estado FROM cardscredito WHERE id_usuario = ? UNION ALL SELECT id_carddebito AS id_tarjeta, banco, 'Debito' AS tipo, saldo_actual, 0 AS limite_credito, 0 AS cantidadabonada, 0 AS fecha_corte, NULL AS fecha_vencimiento, 'N/A' AS estado FROM cardsdebito WHERE id_usuario = ? ORDER BY tipo, banco";
            PreparedStatement ps = ConexionDB.getInstance().prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idUsuario);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
