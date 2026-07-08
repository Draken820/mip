package com.mycompany.ringcard.dao.impl;

import com.mycompany.ringcard.dao.ITarjetaDebitoDAO;
import com.mycompany.ringcard.models.TarjetasDeb;
import com.mycompany.ringcard.utils.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TarjetaDebitoDAOImpl implements ITarjetaDebitoDAO {

    @Override
    public boolean insertarTarjeta(TarjetasDeb t) {
        String sql = "INSERT INTO cardsdebito (id_usuario, banco, fecha_vencimiento, saldo_actual) VALUES (?, ?, ?, ?)";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
             
            ps.setInt(1, t.getId_usuario());
            ps.setString(2, t.getBanco());
            ps.setDate(3, new java.sql.Date(t.getFecha_vencimiento().getTime()));
            ps.setInt(4, t.getSaldo_actual());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizarTarjeta(TarjetasDeb t) {
        String sql = "UPDATE cardsdebito SET id_usuario = ?, banco = ?, fecha_vencimiento = ?, saldo_actual = ? WHERE id_carddebito = ?";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
             
            ps.setInt(1, t.getId_usuario());
            ps.setString(2, t.getBanco());
            ps.setDate(3, new java.sql.Date(t.getFecha_vencimiento().getTime()));
            ps.setInt(4, t.getSaldo_actual());
            ps.setInt(5, t.getId_carddebito());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarTarjeta(int idCardDebito) {
        String sql = "DELETE FROM cardsdebito WHERE id_carddebito = ?";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
             
            ps.setInt(1, idCardDebito);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public TarjetasDeb consultarTarjeta(int idCardDebito) {
        String sql = "SELECT * FROM cardsdebito WHERE id_carddebito = ?";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
             
            ps.setInt(1, idCardDebito);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TarjetasDeb t = new TarjetasDeb();
                    t.setId_carddebito(rs.getInt("id_carddebito"));
                    t.setId_usuario(rs.getInt("id_usuario"));
                    t.setBanco(rs.getString("banco"));
                    t.setFecha_vencimiento(rs.getDate("fecha_vencimiento"));
                    t.setSaldo_actual(rs.getInt("saldo_actual"));
                    return t;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}