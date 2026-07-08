package com.mycompany.ringcard.services;

import com.mycompany.ringcard.utils.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TarjetaService {
    
    public void cargarTarjetas(int idUsuario, ArrayList<String> nombresTarjetas, 
                               ArrayList<Integer> idsTarjetas, ArrayList<String> tiposTarjetas, 
                               ArrayList<String> bancosTargetasc) {
        nombresTarjetas.clear();
        idsTarjetas.clear();
        tiposTarjetas.clear();
        bancosTargetasc.clear();

        try (Connection cx = ConexionDB.getInstance()) {
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
            e.printStackTrace();
        }
    }

    public boolean borrarTarjeta(String tabla, String banco, int idUsuario) {
        String sql = "DELETE FROM " + tabla + " WHERE banco = ? AND id_usuario = ?";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, banco);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean modificarTarjeta(String tabla, String nuevoBanco, String bancoActual, int idUsuario) {
        String sql = "UPDATE " + tabla + " SET banco = ? WHERE banco = ? AND id_usuario = ?";
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, nuevoBanco);
            ps.setString(2, bancoActual);
            ps.setInt(3, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            return false;
        }
    }
}