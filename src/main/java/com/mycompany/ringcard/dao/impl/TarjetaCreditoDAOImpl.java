package com.mycompany.ringcard.dao.impl;

import com.mycompany.ringcard.dao.ITarjetaCreditoDAO;
import com.mycompany.ringcard.models.TarjetasCred;
import com.mycompany.ringcard.utils.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TarjetaCreditoDAOImpl implements ITarjetaCreditoDAO {

    @Override
    public boolean insertarTarjeta(TarjetasCred t) {
        String sql = "INSERT INTO cardscredito (id_usuario, banco, cantidadabonada, pctinteres, fecha_vencimiento, estado, saldo_actual, limite_credito, fecha_corte) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            
            ps.setInt(1, t.getId_usuario());
            ps.setString(2, t.getBanco());
            ps.setDouble(3, t.getCantidadab());
            ps.setInt(4, t.getPctinteres());
            ps.setDate(5, new java.sql.Date(t.getFecha_vencimiento().getTime()));
            ps.setString(6, t.getEstado());
            ps.setDouble(7, t.getSaldo_actual());
            ps.setDouble(8, t.getLimite_credito());
            ps.setInt(9, t.getFecha_corte());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarTarjeta(TarjetasCred t) {
        String sql = "UPDATE cardscredito SET id_usuario = ?, banco = ?, cantidadabonada = ?, pctinteres = ?, fecha_vencimiento = ?, estado = ?, saldo_actual = ?, limite_credito = ?, fecha_corte = ? WHERE id_cardcredito = ?";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
             
            ps.setInt(1, t.getId_usuario());
            ps.setString(2, t.getBanco());
            ps.setDouble(3, t.getCantidadab());
            ps.setInt(4, t.getPctinteres());
            ps.setDate(5, new java.sql.Date(t.getFecha_vencimiento().getTime()));
            ps.setString(6, t.getEstado());
            ps.setDouble(7, t.getSaldo_actual());
            ps.setDouble(8, t.getLimite_credito());
            ps.setInt(9, t.getFecha_corte());
            ps.setInt(10, t.getId_cardcredito());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarTarjeta(int idCardCredito) {
        String sql = "DELETE FROM cardscredito WHERE id_cardcredito = ?";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
             
            ps.setInt(1, idCardCredito);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TarjetasCred consultarTarjeta(int idCardCredito) {
        String sql = "SELECT * FROM cardscredito WHERE id_cardcredito = ?";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
             
            ps.setInt(1, idCardCredito);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TarjetasCred t = new TarjetasCred();
                    t.setId_cardcredito(rs.getInt("id_cardcredito"));
                    t.setId_usuario(rs.getInt("id_usuario"));
                    t.setBanco(rs.getString("banco"));
                    t.setCantidadab(rs.getDouble("cantidadabonada"));
                    t.setPctinteres(rs.getInt("pctinteres"));
                    t.setFecha_vencimiento(rs.getDate("fecha_vencimiento"));
                    t.setEstado(rs.getString("estado"));
                    t.setSaldo_actual(rs.getDouble("saldo_actual"));
                    t.setLimite_credito(rs.getDouble("limite_credito"));
                    t.setFecha_corte(rs.getInt("fecha_corte"));
                    return t;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}