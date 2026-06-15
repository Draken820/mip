package com.mycompany.ringcard.data;

import com.mycompany.ringcard.clases.TarjetasCred;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dataTarjetascred {
    Connection cx;

    String url = "jdbc:postgresql://localhost:5432/proyecto1";
    String pass = "pokoyo5505";
    String user = "postgres";

    public Connection conectar() {
        try {
            cx = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión exitosa a crédito");
        } catch (SQLException e2) {
            e2.printStackTrace();
            System.out.println("Conexión fallida");
        }
        return cx;
    }

    public boolean eliminarTarjetac(TarjetasCred o) {
        this.cx = conectar();
        if (this.cx == null) return false; 
        
        String sql = "DELETE FROM cardscredito WHERE id_cardcredito = ?";
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_cardcredito());
            return ps.executeUpdate() > 0; 
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //cantidadabonada

    public boolean insertarTarjetac(TarjetasCred o) {
        this.cx = conectar(); 
        if (this.cx == null) return false;

        String sql = "INSERT INTO cardscredito (id_usuario, banco, cantidadabonada, pctinteres, fecha_vencimiento, estado, saldo_actual, limite_credito, fecha_corte) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_usuario());
            ps.setString(2, o.getBanco());
            ps.setDouble(3, o.getCantidadab());
            ps.setInt(4, o.getPctinteres());
            ps.setDate(5, new java.sql.Date(o.getFecha_vencimiento().getTime()));
            ps.setString(6, o.getEstado());
            ps.setDouble(7, o.getSaldo_actual());
            ps.setDouble(8, o.getLimite_credito());
            ps.setInt(9, o.getFecha_corte());
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }

    public boolean actualizarTarjetac(TarjetasCred o) {
        this.cx = conectar(); 
        if (this.cx == null) return false;

        String sql = "UPDATE cardscredito SET id_usuario = ?, banco = ?, cantidadabonada = ?, pctinteres = ?, fecha_vencimiento = ?, estado = ?, saldo_actual = ?, limite_credito = ?, fecha_corte = ? WHERE id_cardcredito = ?";
        
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_usuario());
            ps.setString(2, o.getBanco());
            ps.setDouble(3, o.getCantidadab());
            ps.setInt(4, o.getPctinteres());
            ps.setDate(5, new java.sql.Date(o.getFecha_vencimiento().getTime()));
            ps.setString(6, o.getEstado());
            ps.setDouble(7, o.getSaldo_actual());
            ps.setDouble(8, o.getLimite_credito());
            ps.setInt(9, o.getFecha_corte());
            ps.setInt(10, o.getId_cardcredito()); 
            
            return ps.executeUpdate() > 0; 
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }

    public boolean consultarTarjetac(TarjetasCred o) {
        this.cx = conectar();
        if (this.cx == null) return false;

        String sql = "SELECT * FROM cardscredito WHERE id_cardcredito = ?";
        
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_cardcredito()); 
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    o.setId_usuario(rs.getInt("id_usuario"));
                    o.setBanco(rs.getString("banco"));
                    o.setCantidadab(rs.getDouble("cantidadabonada"));
                    o.setPctinteres(rs.getInt("pctinteres"));
                    o.setFecha_vencimiento(rs.getDate("fecha_vencimiento"));
                    o.setEstado(rs.getString("estado"));
                    o.setSaldo_actual(rs.getDouble("saldo_actual"));
                    o.setLimite_credito(rs.getDouble("limite_credito"));
                    o.setFecha_corte(rs.getInt("fecha_corte"));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}