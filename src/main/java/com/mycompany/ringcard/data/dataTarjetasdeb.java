package com.mycompany.ringcard.data;

import com.mycompany.ringcard.clases.TarjetasDeb;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dataTarjetasdeb {
    Connection cx;
    String url = "jdbc:postgresql://localhost:5432/RINGCARD";
    String pass = "pokoyo5505";
    String user = "postgres";

    public Connection conectar() {
        try {
            cx = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión exitosa a débito");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Conexión fallida para débito");
        }
        return cx;
    }

    // Método para eliminar una tarjeta de débito
    public boolean eliminarTarjetad(TarjetasDeb o) {
        this.cx = conectar();
        if (this.cx == null) {
            return false;
        }

        String sql = "DELETE FROM cardsdebito WHERE id_carddebito = ?";
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_carddebito());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para insertar una tarjeta de débito (Sin no_tarjeta)
    public boolean insertarTarjetad(TarjetasDeb o) {
        this.cx = conectar();
        if (this.cx == null) {
            return false;
        }

        // Se eliminó no_tarjeta de la consulta SQL
        String sql = "INSERT INTO cardsdebito (id_usuario, banco, fecha_vencimiento, saldo_actual) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_usuario());
            ps.setString(2, o.getBanco());
            ps.setDate(3, (Date) o.getFecha_vencimiento());
            ps.setInt(4, o.getSaldo_actual()); // Ajustado a setInt si tu BD maneja el saldo como entero
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- NUEVO MÉTODO: Actualizar tarjeta de débito (Sin no_tarjeta) ---
    public boolean actualizarTarjetad(TarjetasDeb o) {
        this.cx = conectar(); 
        
        if (this.cx == null) {
            return false;
        }

        // UPDATE para los campos de débito, omitiendo no_tarjeta
        String sql = "UPDATE cardsdebito SET id_usuario = ?, banco = ?, fecha_vencimiento = ?, saldo_actual = ? WHERE id_carddebito = ?";
        
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_usuario());
            ps.setString(2, o.getBanco());
            ps.setDate(3, (Date) o.getFecha_vencimiento());
            ps.setInt(4, o.getSaldo_actual());
            ps.setInt(5, o.getId_carddebito()); // El ID va al final para el WHERE
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0; 
            
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }

    // Método para consultar una tarjeta de débito por su ID (Sin no_tarjeta)
    public boolean consultarTarjetad(TarjetasDeb o) {
        this.cx = conectar();
        if (this.cx == null) {
            return false;
        }

        String sql = "SELECT * FROM cardsdebito WHERE id_carddebito = ?";
        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setInt(1, o.getId_carddebito()); 
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    o.setId_usuario(rs.getInt("id_usuario"));
                    // Se omitió la asignación de o.setNo_tarjeta(...)
                    o.setBanco(rs.getString("banco"));
                    o.setFecha_vencimiento(rs.getDate("fecha_vencimiento"));
                    o.setSaldo_actual(rs.getInt("saldo_actual"));
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}