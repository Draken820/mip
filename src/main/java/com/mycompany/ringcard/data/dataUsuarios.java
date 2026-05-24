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
	String url="jdbc://mysql/localhost:5432/proyecto";
	String pass="volvo";
	String user="postgres";
public Connection conectar() {
	try {
	cx=DriverManager.getConnection(url,user,pass);
	System.out.println("conexion exitosa");
	}catch (SQLException e2) {
	e2.printStackTrace();
		// TODO: handle exception
	System.out.println("conexion fallida");
	}
	return cx;
}
public boolean autenticarUsuario(usuarios o){
    try{
    PreparedStatement ps=cx.prepareStatement("SELECT id FROM usuarios WHERE username = ? AND password = ?");
    ps.setString(1, o.email);
    ps.setString(2, o.pass);
    try (ResultSet rs = ps.executeQuery()) {
                // Si rs.next() es true, significa que encontró un registro coincidente
                return rs.next();
            }
    }catch(Exception e){
        e.printStackTrace();
        return false;
    }
    
}
public boolean insertarUsuario(usuarios o) {
	try {
	PreparedStatement ps=cx.prepareStatement("INSERT INTO usuario VALUES ?,?,?,?,?");
	ps.setInt(1, o.id_usuario);
	ps.setString(2, o.nombre);
        ps.setString(3, o.ap);
        ps.setString(4, o.am);
        ps.setString(5, o.pass);
        ps.setString(6, o.email);
        ps.setInt(6, o.telefono);
	ps.executeUpdate();
	return true;
	}catch (Exception e) {
		// TODO: handle exception
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

