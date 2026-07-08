package com.mycompany.ringcard.models;

public class Usuario {
    private int id_usuario; // [cite: 249]
    private String nombre; // [cite: 249]
    private String ap; // [cite: 250]
    private String am; // [cite: 250]
    private String pass; // [cite: 250]
    private String email; // [cite: 250]
    private int telefono; // [cite: 250]

    public Usuario() {}

    public Usuario(int id_usuario, String nombre, String ap, String am, String pass, String email, int telefono) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.ap = ap;
        this.am = am;
        this.pass = pass;
        this.email = email;
        this.telefono = telefono;
    }

    // --- GETTERS Y SETTERS ---
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) { this.id_usuario = id_usuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getAp() { return ap; }
    public void setAp(String ap) { this.ap = ap; }
    public String getAm() { return am; }
    public void setAm(String am) { this.am = am; }
    public String getPass() { return pass; }
    public void setPass(String pass) { this.pass = pass; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getTelefono() { return telefono; }
    public void setTelefono(int telefono) { this.telefono = telefono; }
}