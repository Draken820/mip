package com.mycompany.ringcard.dao;

import com.mycompany.ringcard.models.TarjetasDeb;

public interface ITarjetaDebitoDAO {
    boolean insertarTarjeta(TarjetasDeb t);
    boolean actualizarTarjeta(TarjetasDeb t);
    boolean eliminarTarjeta(int idCardDebito);
    TarjetasDeb consultarTarjeta(int idCardDebito);
}