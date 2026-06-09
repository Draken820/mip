/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ringcard.clases;

import com.mycompany.ringcard.data.dataTarjetascred;
import com.mycompany.ringcard.data.dataTarjetasdeb;
import java.util.Date;

/**
 *
 * @author drako
 */
public class TarjetasDeb{
    dataTarjetasdeb x=new dataTarjetasdeb();
    private int id_carddebito;
    public int id_usuario;
    public String banco;
    public Date fecha_vencimiento;
   
    public int saldo_actual;
 

    /**
     * @return the id_cardcredito
     */


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
     * @param estado the estado to set
     */
   
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
  
    public TarjetasDeb(){
        
    }
    public boolean insertarTarjetad() {
	if(x.insertarTarjetad(this)) {
		return true;
	}else {
		return false;
	}
}
     public boolean eliminarTarjetad() {
	if(x.eliminarTarjetad(this)) {
		return true;
	}else {
		return false;
	}
}
public boolean consultarTarjetad() {
	if(x.consultarTarjetad(this)) {
		return true;
	}else {
		return false;
	}
}
public boolean actualizarTarjetad() {
	if(x.actualizarTarjetad(this)) {
		return true;
	}else {
		return false;
	}
}

    /**
     * @return the id_carddebito
     */
    public int getId_carddebito() {
        return id_carddebito;
    }

    /**
     * @param id_carddebito the id_carddebito to set
     */
    public void setId_carddebito(int id_carddebito) {
        this.id_carddebito = id_carddebito;
    }
            
         
}
