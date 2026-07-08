package com.mycompany.ringcard.dao.impl;

import com.mycompany.ringcard.dao.IUsuarioDAO;
import com.mycompany.ringcard.models.Usuario;
import com.mycompany.ringcard.utils.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAOImpl implements IUsuarioDAO {

    @Override
    public boolean insertarUsuario(Usuario o) {
        String sql = "INSERT INTO usuarios (nombre, ap, am, pass, email, telefono) VALUES (?, ?, ?, ?, ?, ?)"; // [cite: 177]
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            
            ps.setString(1, o.getNombre()); // [cite: 178]
            ps.setString(2, o.getAp()); // [cite: 178]
            ps.setString(3, o.getAm()); // [cite: 178]
            ps.setString(4, o.getPass()); // [cite: 178]
            ps.setString(5, o.getEmail()); // [cite: 178]
            ps.setInt(6, o.getTelefono()); // [cite: 178]

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Usuario autenticarUsuario(String email, String pass) {
        String sql = "SELECT id_usuario FROM usuarios WHERE email = ? AND pass = ?"; // [cite: 171]
        try (Connection cx = ConexionDB.getInstance();
             PreparedStatement ps = cx.prepareStatement(sql)) {
            
            ps.setString(1, email); // [cite: 172]
            ps.setString(2, pass); // [cite: 172]
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId_usuario(rs.getInt("id_usuario")); // [cite: 172]
                    u.setEmail(email);
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}