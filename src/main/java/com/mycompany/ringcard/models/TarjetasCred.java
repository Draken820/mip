package com.mycompany.ringcard.models;

import java.util.Date;

public class TarjetasCred {
    private int id_cardcredito;
    private int id_usuario;
    private String banco;
    private double cantidadab;
    private int pctinteres;
    private Date fecha_vencimiento;
    private String estado;
    private double saldo_actual;
    private double limite_credito;
    private int fecha_corte;

    public TarjetasCred() {}

    // --- GETTERS Y SETTERS ---
    public int getId_cardcredito() { return id_cardcredito; }
    public void setId_cardcredito(int id_cardcredito) { this.id_cardcredito = id_cardcredito; }
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }
    public double getCantidadab() { return cantidadab; }
    public void setCantidadab(double cantidadab) { this.cantidadab = cantidadab; }
    public int getPctinteres() { return pctinteres; }
    public void setPctinteres(int pctinteres) { this.pctinteres = pctinteres; }
    public Date getFecha_vencimiento() { return fecha_vencimiento; }
    public void setFecha_vencimiento(Date fecha_vencimiento) { this.fecha_vencimiento = fecha_vencimiento; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public double getSaldo_actual() { return saldo_actual; }
    public void setSaldo_actual(double saldo_actual) { this.saldo_actual = saldo_actual; }
    public double getLimite_credito() { return limite_credito; }
    public void setLimite_credito(double limite_credito) { this.limite_credito = limite_credito; }
    public int getFecha_corte() { return fecha_corte; }
    public void setFecha_corte(int fecha_corte) { this.fecha_corte = fecha_corte; }
}