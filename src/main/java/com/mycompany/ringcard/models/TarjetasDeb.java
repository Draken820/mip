package com.mycompany.ringcard.models;

import java.util.Date;

public class TarjetasDeb {
    private int id_carddebito;
    private int id_usuario;
    private String banco;
    private Date fecha_vencimiento;
    private int saldo_actual;

    public TarjetasDeb() {}

    // --- GETTERS Y SETTERS ---
    public int getId_carddebito() { return id_carddebito; }
    public void setId_carddebito(int id_carddebito) { this.id_carddebito = id_carddebito; }
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }
    public Date getFecha_vencimiento() { return fecha_vencimiento; }
    public void setFecha_vencimiento(Date fecha_vencimiento) { this.fecha_vencimiento = fecha_vencimiento; }
    public int getSaldo_actual() { return saldo_actual; }
    public void setSaldo_actual(int saldo_actual) { this.saldo_actual = saldo_actual; }
}