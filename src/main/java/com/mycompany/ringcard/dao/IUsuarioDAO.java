package com.mycompany.ringcard.dao;
import com.mycompany.ringcard.models.Usuario;

public interface IUsuarioDAO {
    boolean insertarUsuario(Usuario o);
    Usuario autenticarUsuario(String email, String pass);
}