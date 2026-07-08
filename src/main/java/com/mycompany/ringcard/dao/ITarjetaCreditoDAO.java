package com.mycompany.ringcard.dao;

import com.mycompany.ringcard.models.TarjetasCred;

public interface ITarjetaCreditoDAO {
    boolean insertarTarjeta(TarjetasCred t);
    boolean actualizarTarjeta(TarjetasCred t);
    boolean eliminarTarjeta(int idCardCredito);
    TarjetasCred consultarTarjeta(int idCardCredito);
}