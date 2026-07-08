package com.mycompany.ringcard.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static Connection cx = null;
    private static final String URL = "jdbc:postgresql://localhost:5432/proyecto1"; 
    private static final String USER = "postgres"; 
    private static final String PASS = "volvo"; 

    // Constructor privado para evitar instancias con 'new'
    private ConexionDB() {}

    public static Connection getInstance() {
        try {
            if (cx == null || cx.isClosed()) {
                cx = DriverManager.getConnection(URL, USER, PASS);
                System.out.println("Conexión exitosa a la base de datos"); 
            }
        } catch (SQLException e) {
            System.err.println("Conexión fallida: " + e.getMessage()); 
        }
        return cx;
    }
}