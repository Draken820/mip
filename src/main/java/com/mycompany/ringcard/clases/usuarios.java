/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ringcard.clases;

import com.mycompany.ringcard.data.dataUsuarios;

/**
 *
 * @author DELL
 */
public class usuarios {
    public int id_usuario;
    public String nombre;
    public String ap;
    public String am;
    public String pass;
    public String email;
    public int telefono;
    dataUsuarios x=new dataUsuarios();

    public usuarios() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    public boolean insertarUsuario() {
	if(x.insertarUsuario(this)) {
		return true;
	}else {
		return false;
	}
}
     public boolean autenticarUsuario() {
	if(x.insertarUsuario(this)) {
		return true;
	}else {
		return false;
	}
}
public boolean consultarUsuario() {
	if(x.consultarUsuario(this)) {
		return true;
	}else {
		return false;
	}
}

    public usuarios(int id_usuario, String nombre, String ap, String am, String pass, String email, int telefono) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.ap = ap;
        this.am = am;
        this.pass = pass;
        this.email = email;
        this.telefono = telefono;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

}
