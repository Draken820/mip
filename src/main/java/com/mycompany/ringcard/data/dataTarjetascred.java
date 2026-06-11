package com.mycompany.ringcard.data;

import com.mycompany.ringcard.clases.TarjetasCred;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author drako
 */
public class dataTarjetascred {
    Connection cx;

    String url = "jdbc:postgresql://localhost:5432/RINGCARD";
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

    // Método para eliminar una tarjeta
    public boolean eliminarTarjetac(TarjetasCred o) {
        this.cx = conectar();
        
        if (this.cx == null) {
            System.out.println("No se pudo establecer la conexión.");
            return false; 
        }
        
        String sql = "DELETE FROM cardscredito WHERE id_cardcredito = ?";
        
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_cardcredito());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; 
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para insertar una tarjeta (CONTEMPLANDO no_tarjeta)
    public boolean insertarTarjetac(TarjetasCred o) {
        this.cx = conectar(); 
        
        if (this.cx == null) {
            return false;
        }

        // Se agregó no_tarjeta a la consulta
        String sql = "INSERT INTO cardscredito (id_usuario, banco, fecha_vencimiento, estado, saldo_actual, limite_credito, fecha_corte) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_usuario());
             // Agregado
            ps.setString(2, o.getBanco());
            ps.setDate(3, (Date) o.getFecha_vencimiento());
            ps.setString(4, o.getEstado());
            ps.setInt(5, o.getSaldo_actual());
            ps.setInt(6, o.getLimite_credito());
            ps.setInt(7, o.getFecha_corte());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }

    // --- MÉTODO DE ACTUALIZAR (CONTEMPLANDO no_tarjeta) ---
    public boolean actualizarTarjetac(TarjetasCred o) {
        this.cx = conectar(); 
        
        if (this.cx == null) {
            return false;
        }

        // Se agregó no_tarjeta al UPDATE
        String sql = "UPDATE cardscredito SET id_usuario = ?,  banco = ?, fecha_vencimiento = ?, estado = ?, saldo_actual = ?, limite_credito = ?, fecha_corte = ? WHERE id_cardcredito = ?";
        
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_usuario());
           // Agregado
            ps.setString(2, o.getBanco());
            ps.setDate(3, (Date) o.getFecha_vencimiento());
            ps.setString(4, o.getEstado());
            ps.setInt(5, o.getSaldo_actual());
            ps.setInt(6, o.getLimite_credito());
            ps.setInt(7, o.getFecha_corte());
            ps.setInt(8, o.getId_cardcredito()); // El ID va al final para el WHERE
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; 
            
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }

    // Método para consultar una tarjeta por su ID (CONTEMPLANDO no_tarjeta)
    public boolean consultarTarjetac(TarjetasCred o) {
        this.cx = conectar();
        
        if (this.cx == null) {
            return false;
        }

        String sql = "SELECT * FROM cardscredito WHERE id_cardcredito = ?";
        
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_cardcredito()); 
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    o.setId_usuario(rs.getInt("id_usuario"));
                    // Agregado el mapeo del ResultSet
                    o.setBanco(rs.getString("banco"));
                    o.setFecha_vencimiento(rs.getDate("fecha_vencimiento"));
                    o.setEstado(rs.getString("estado"));
                    o.setSaldo_actual(rs.getInt("saldo_actual"));
                    o.setLimite_credito(rs.getInt("limite_credito"));
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