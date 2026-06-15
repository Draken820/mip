/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ringcard.clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Gael
 */
public class MovimientoDAO {
    private Connection cx;
    
    public MovimientoDAO (Connection cx) {
        this.cx = cx;
    }
    
    // CRUD
    // re insertan datos por aca -------recuerdo cuando tu eres miaaaaa dandonos calor en la noche friaaaa-----------
    
    public boolean insertarMov(Movimiento mov) {
        try {
            String sql = 
                "INSERT INTO movimientos_debito " +
                "(id_carddebito,  fecha_movimiento, concepto, monto, tipo_movimiento) " +
                "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = cx.prepareStatement(sql);

            ps.setInt(1, mov.getIdCardDebito());
        
            ps.setDate(2, mov.getFechaMovimiento());
            ps.setString(3, mov.getConcepto());
            ps.setDouble(4, mov.getMonto());
            ps.setString(5, mov.getTipoMovimiento());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Movimiento> listarMovimientosDebito(int idCardDebito) {
        ArrayList<Movimiento> lista = new ArrayList<>();

        try {
            String sql = 
                "SELECT * FROM movimientos_debito " +
                "WHERE id_carddebito = ? " +
                "ORDER BY fecha_movimiento DESC";

            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, idCardDebito);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Movimiento mov = new Movimiento();

                mov.setIdMovimiento(rs.getInt("id_movimiento"));
                mov.setIdCardDebito(rs.getInt("id_carddebito"));
          
                mov.setFechaMovimiento(rs.getDate("fecha_movimiento"));
                mov.setConcepto(rs.getString("concepto"));
                mov.setMonto(rs.getDouble("monto"));
                mov.setTipoMovimiento(rs.getString("tipo_movimiento"));

                lista.add(mov);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ELIMINAR MOVIMIENTO
    public boolean eliminarMovimiento(int idMovimiento) {
        try {
            String sql = 
                "DELETE FROM movimientos_debito " +
                "WHERE id_movimiento = ?";

            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, idMovimiento);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // BUSCAR MOVIMIENTO POR ID
    public Movimiento buscarMovimiento(int idMovimiento) {
        try {
            String sql = 
                "SELECT * FROM movimientos_debito " +
                "WHERE id_movimiento = ?";

            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, idMovimiento);
            ResultSet rs = ps.executeQuery();
            

            if (rs.next()) {
                Movimiento mov = new Movimiento();

                mov.setIdMovimiento(rs.getInt("id_movimiento"));
                
               
                mov.setFechaMovimiento(rs.getDate("fecha_movimiento"));
                mov.setConcepto(rs.getString("concepto"));
                mov.setMonto(rs.getDouble("monto"));
                mov.setTipoMovimiento(rs.getString("tipo_movimiento"));

                return mov;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    //TODOS LOS MOVIENTOS
    public ResultSet obtenerTodosLosMovimientos(int idUsuario) {

    try {

        String sql = """
            SELECT mc.fecha_movimiento,
                   mc.concepto,
                   mc.monto,
                   mc.tipo_movimiento,
                   'Credito' AS tarjeta,
                   mc.id_movimiento
            FROM movimientos_credito mc
            INNER JOIN cardscredito cc
                ON mc.id_cardcredito = cc.id_cardcredito
            WHERE cc.id_usuario = ?

            UNION ALL

            SELECT md.fecha_movimiento,
                   md.concepto,
                   md.monto,
                   md.tipo_movimiento,
                   'Debito' AS tarjeta,
                   md.id_movimiento
            FROM movimientos_debito md
            INNER JOIN cardsdebito cd
                ON md.id_carddebito = cd.id_carddebito
            WHERE cd.id_usuario = ?

            ORDER BY fecha_movimiento DESC,
                     id_movimiento DESC
            """;

        PreparedStatement ps = cx.prepareStatement(sql);

        ps.setInt(1, idUsuario);
        ps.setInt(2, idUsuario);
        

        return ps.executeQuery();

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}

    public ResultSet obtenerEgresos(int idUsuario) {

    try {

        String sql = """
            SELECT mc.fecha_movimiento,
                   mc.concepto,
                   mc.monto,
                   mc.tipo_movimiento,
                   'Credito' AS tarjeta
            FROM movimientos_credito mc
            INNER JOIN cardscredito cc
                ON mc.id_cardcredito = cc.id_cardcredito
            WHERE cc.id_usuario = ?
              AND UPPER(mc.tipo_movimiento) = 'EGRESO'

            UNION ALL

            SELECT md.fecha_movimiento,
                   md.concepto,
                   md.monto,
                   md.tipo_movimiento,
                   'Debito' AS tarjeta
            FROM movimientos_debito md
            INNER JOIN cardsdebito cd
                ON md.id_carddebito = cd.id_carddebito
            WHERE cd.id_usuario = ?
              AND UPPER(md.tipo_movimiento) = 'EGRESO'

            ORDER BY fecha_movimiento DESC
            """;

        PreparedStatement ps = cx.prepareStatement(sql);

        ps.setInt(1, idUsuario);
        ps.setInt(2, idUsuario);

        return ps.executeQuery();

    } catch(Exception e) {
        e.printStackTrace();
    }

    return null;
}

public ResultSet obtenerIngresos(int idUsuario) {

    try {

        String sql = """
            SELECT mc.fecha_movimiento,
                   mc.concepto,
                   mc.monto,
                   mc.tipo_movimiento,
                   'Credito' AS tarjeta
            FROM movimientos_credito mc
            INNER JOIN cardscredito cc
                ON mc.id_cardcredito = cc.id_cardcredito
            WHERE cc.id_usuario = ?
              AND UPPER(mc.tipo_movimiento) = 'INGRESO'

            UNION ALL

            SELECT md.fecha_movimiento,
                   md.concepto,
                   md.monto,
                   md.tipo_movimiento,
                   'Debito' AS tarjeta
            FROM movimientos_debito md
            INNER JOIN cardsdebito cd
                ON md.id_carddebito = cd.id_carddebito
            WHERE cd.id_usuario = ?
              AND UPPER(md.tipo_movimiento) = 'INGRESO'

            ORDER BY fecha_movimiento DESC
            """;

        PreparedStatement ps = cx.prepareStatement(sql);

        ps.setInt(1, idUsuario);
        ps.setInt(2, idUsuario);

        return ps.executeQuery();

    } catch(Exception e) {
        e.printStackTrace();
    }

    return null;
}

public ResultSet obtenerSoloCredito(int idUsuario) {

    try {

        String sql = """
            SELECT mc.fecha_movimiento,
                   mc.concepto,
                   mc.monto,
                   mc.tipo_movimiento,
                   'Credito' AS tarjeta
            FROM movimientos_credito mc
            INNER JOIN cardscredito cc
                ON mc.id_cardcredito = cc.id_cardcredito
            WHERE cc.id_usuario = ?
            ORDER BY mc.fecha_movimiento DESC,
                     mc.id_movimiento DESC
            """;

        PreparedStatement ps = cx.prepareStatement(sql);

        ps.setInt(1, idUsuario);

        return ps.executeQuery();

    } catch(Exception e) {
        e.printStackTrace();
    }

    return null;
}

public ResultSet obtenerSoloDebito(int idUsuario) {

    try {

        String sql = """
            SELECT md.fecha_movimiento,
                   md.concepto,
                   md.monto,
                   md.tipo_movimiento,
                   'Debito' AS tarjeta
            FROM movimientos_debito md
            INNER JOIN cardsdebito cd
                ON md.id_carddebito = cd.id_carddebito
            WHERE cd.id_usuario = ?
            ORDER BY md.fecha_movimiento DESC,
                     md.id_movimiento DESC
            """;

        PreparedStatement ps = cx.prepareStatement(sql);

        ps.setInt(1, idUsuario);

        return ps.executeQuery();

    } catch(Exception e) {
        e.printStackTrace();
    }

    return null;
}


}