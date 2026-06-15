package com.mycompany.ringcard.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class dataMovimientos {

    private dataUsuarios db;

    public dataMovimientos() {
        this.db = new dataUsuarios(); // Reutilizamos tu clase de conexión
    }

    // Retornamos la conexión por si otras clases (como MovimientoDAO) la necesitan
    public Connection getConnection() {
        return db.conectar();
    }

    /**
     * Carga las tarjetas de débito y crédito del usuario y llena las listas correspondientes.
     */
    public void cargarTarjetas(int idUsuario, ArrayList<String> nombresTarjetas, 
                               ArrayList<Integer> idsTarjetas, ArrayList<String> tiposTarjetas, 
                               ArrayList<String> bancosTargetasc) {
        
        nombresTarjetas.clear();
        idsTarjetas.clear();
        tiposTarjetas.clear();
        bancosTargetasc.clear();

        try (Connection cx = db.conectar()) {
            
            // 1. Obtenemos ID y Banco de Débito
            String sqlDebito = "SELECT id_carddebito, banco FROM cardsdebito WHERE id_usuario = ?";
            try (PreparedStatement psDeb = cx.prepareStatement(sqlDebito)) {
                psDeb.setInt(1, idUsuario);
                try (ResultSet rsDeb = psDeb.executeQuery()) {
                    while(rsDeb.next()) {
                        String banco = rsDeb.getString("banco");
                        nombresTarjetas.add(banco + " (Débito)");
                        bancosTargetasc.add(banco.toLowerCase());
                        idsTarjetas.add(rsDeb.getInt("id_carddebito"));
                        tiposTarjetas.add("debito");
                    }
                }
            }
            
            // 2. Obtenemos ID y Banco de Crédito
            String sqlCredito = "SELECT id_cardcredito, banco FROM cardscredito WHERE id_usuario = ?";
            try (PreparedStatement psCred = cx.prepareStatement(sqlCredito)) {
                psCred.setInt(1, idUsuario);
                try (ResultSet rsCred = psCred.executeQuery()) {
                    while(rsCred.next()) {
                        String banco = rsCred.getString("banco");
                        nombresTarjetas.add(banco + " (Crédito)");
                        bancosTargetasc.add(banco.toLowerCase());
                        idsTarjetas.add(rsCred.getInt("id_cardcredito"));
                        tiposTarjetas.add("credito");
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al cargar las tarjetas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Elimina una tarjeta de la base de datos.
     */
    public boolean borrarTarjeta(String tabla, String banco, int idUsuario) {
        boolean exito = false;
        String sql = "DELETE FROM " + tabla + " WHERE banco = ? AND id_usuario = ?";
        
        try (Connection cx = db.conectar();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            
            ps.setString(1, banco);
            ps.setInt(2, idUsuario);
            
            int filasAfectadas = ps.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (Exception e) {
            System.err.println("Error al borrar la tarjeta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito;
    }

    /**
     * Modifica el nombre del banco de una tarjeta.
     */
    public boolean modificarTarjeta(String tabla, String nuevoBanco, String bancoActual, int idUsuario) {
        boolean exito = false;
        String sql = "UPDATE " + tabla + " SET banco = ? WHERE banco = ? AND id_usuario = ?";
        
        try (Connection cx = db.conectar();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            
            ps.setString(1, nuevoBanco);
            ps.setString(2, bancoActual);
            ps.setInt(3, idUsuario);
            
            int filasAfectadas = ps.executeUpdate();
            exito = filasAfectadas > 0;
            
        } catch (Exception e) {
            System.err.println("Error al modificar la tarjeta: " + e.getMessage());
            e.printStackTrace();
        }
        
        return exito;
    }
}