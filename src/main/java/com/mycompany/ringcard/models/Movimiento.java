package com.mycompany.ringcard.models;

import java.sql.Date;

public class Movimiento {
    private int idMovimiento;
    private int idCardDebito; // En tu base de datos lo usas tanto para crédito como débito en las consultas
    private Date fechaMovimiento;
    private String concepto;
    private double monto;
    private String tipoMovimiento;

    public Movimiento() {}

    public Movimiento(int idMovimiento, int idCardDebito, Date fechaMovimiento, String concepto, double monto, String tipoMovimiento) {
        this.idMovimiento = idMovimiento;
        this.idCardDebito = idCardDebito;
        this.fechaMovimiento = fechaMovimiento;
        this.concepto = concepto;
        this.monto = monto;
        this.tipoMovimiento = tipoMovimiento;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }
    public int getIdCardDebito() { return idCardDebito; }
    public void setIdCardDebito(int idCardDebito) { this.idCardDebito = idCardDebito; }
    public Date getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(Date fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
    public String getConcepto() { return concepto; }
    public void setConcepto(String concepto) { this.concepto = concepto; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
}