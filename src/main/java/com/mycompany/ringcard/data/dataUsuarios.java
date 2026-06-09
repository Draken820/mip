/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ringcard.data;

import com.mycompany.ringcard.clases.usuarios;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author DELL
 */
public class dataUsuarios {
    Connection cx;

    String url = "jdbc:postgresql://localhost:5432/proyecto";
    String pass = "volvo";
    String user = "postgres";

        public Connection conectar() {
            try {
                cx = DriverManager.getConnection(url, user, pass);
                System.out.println("conexion exitosa");
            } catch (SQLException e2) {
                e2.printStackTrace();
                System.out.println("conexion fallida");
            }
            return cx;
        }

    public boolean autenticarUsuario(usuarios o){
        this.cx = conectar(); 

        if (this.cx == null) {
            System.out.println("No se pudo establecer la conexión. No se puede autenticar.");
            return false; 
        }
        try{
            PreparedStatement ps=cx.prepareStatement("SELECT id_usuario FROM usuarios WHERE email = ? AND pass = ?");
            ps.setString(1, o.getEmail());
            ps.setString(2, o.getPass());
            
            try (ResultSet rs = ps.executeQuery()) {
                // MODIFICACIÓN AQUÍ:
                if (rs.next()) {
                    // Extraemos el ID de la BD y lo guardamos en el objeto 'o'
                    o.setId_usuario(rs.getInt("id_usuario")); 
                    return true; // Inicio de sesión exitoso
                } else {
                    return false; // No se encontró el usuario
                }
            }
            
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean insertarUsuario(usuarios o) {

        this.cx = conectar(); 

        if (this.cx == null) {
            return false;
        }

        try {

            PreparedStatement ps = cx.prepareStatement("INSERT INTO usuarios (nombre, ap, am, pass, email, telefono) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setString(1, o.getNombre());
            ps.setString(2, o.getAp());
            ps.setString(3, o.getAm());
            ps.setString(4, o.getPass());
            ps.setString(5, o.getEmail());
            ps.setInt(6, o.getTelefono());

            ps.executeUpdate();
            return true;

        } catch (Exception e) {

            e.printStackTrace(); 
        }

        return false;
    }

    public boolean consultarUsuario(usuarios o) {
            ResultSet rs=null;
            try {
            PreparedStatement ps=cx.prepareStatement("SELECT * FORM usuario WHERE id=?");
            ps.setString(1, o.email);
            ps.setString(2, o.pass);
            ps.execute();
            if(rs.next()) {
                    o.setId_usuario(rs.getInt("id"));
                    o.setNombre(rs.getString("nombre"));
            }
            return true;
            }catch (Exception e) {
                    // TODO: handle exception
            }
            return false;
}

}

