/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ringcard.clases;

import com.mycompany.ringcard.data.dataTarjetascred;
import java.util.Date;

/**
 *
 * @author drako
 */
public class TarjetasCred {
    dataTarjetascred x=new dataTarjetascred();
    public int id_cardcredito;
    public int id_usuario;
    public String banco;
    public Date fecha_vencimiento;
    public String estado;
    public int saldo_actual;
    public int limite_credito;
    public int fecha_corte;

    /**
     * @return the id_cardcredito
     */
    public int getId_cardcredito() {
        return id_cardcredito;
    }

    /**
     * @param id_cardcredito the id_cardcredito to set
     */
    public void setId_cardcredito(int id_cardcredito) {
        this.id_cardcredito = id_cardcredito;
    }

    /**
     * @return the id_usuario
     */
    public int getId_usuario() {
        return id_usuario;
    }

    /**
     * @param id_usuario the id_usuario to set
     */
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    /**
     * @return the banco
     */
    public String getBanco() {
        return banco;
    }

    /**
     * @param banco the banco to set
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     * @return the fecha_vencimiento
     */
    public Date getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    /**
     * @param fecha_vencimiento the fecha_vencimiento to set
     */
    public void setFecha_vencimiento(Date fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the saldo_actual
     */
    public int getSaldo_actual() {
        return saldo_actual;
    }

    /**
     * @param saldo_actual the saldo_actual to set
     */
    public void setSaldo_actual(int saldo_actual) {
        this.saldo_actual = saldo_actual;
    }

    /**
     * @return the limite_credito
     */
    public int getLimite_credito() {
        return limite_credito;
    }

    /**
     * @param limite_credito the limite_credito to set
     */
    public void setLimite_credito(int limite_credito) {
        this.limite_credito = limite_credito;
    }

    /**
     * @return the fecha_corte
     */
    public int getFecha_corte() {
        return fecha_corte;
    }

    /**
     * @param fecha_corte the fecha_corte to set
     */
    public void setFecha_corte(int fecha_corte) {
        this.fecha_corte = fecha_corte;
    }
    public TarjetasCred(){
        
    }
    public boolean insertarTarjetac() {
	if(x.insertarTarjetac(this)) {
		return true;
	}else {
		return false;
	}
}
     public boolean eliminarTarjetac() {
	if(x.eliminarTarjetac(this)) {
		return true;
	}else {
		return false;
	}
}
public boolean consultarTarjetac() {
	if(x.consultarTarjetac(this)) {
		return true;
	}else {
		return false;
	}
}
public boolean actualizarTarjetac() {
	if(x.actualizarTarjetac(this)) {
		return true;
	}else {
		return false;
	}
}
            
         
}
