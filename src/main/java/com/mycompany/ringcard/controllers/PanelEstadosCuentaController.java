package com.mycompany.ringcard.controllers;

import com.mycompany.ringcard.PanelEstadosCuenta;
import com.mycompany.ringcard.utils.ConfigManager;

import javax.swing.table.DefaultTableModel;
import java.awt.Desktop;
import java.io.File;
import javax.swing.JOptionPane;

public class PanelEstadosCuentaController {
    private PanelEstadosCuenta vista;

    public PanelEstadosCuentaController(PanelEstadosCuenta vista) {
        this.vista = vista;
    }

    public void cargarArchivosEnTabla(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        String ruta = ConfigManager.getRutaDocumentos();
        
        if (ruta == null) return;

        File carpeta = new File(ruta);
        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".docx") && name.startsWith("EstadoCuenta"));

        if (archivos != null) {
            for (File archivo : archivos) {
                // Parseamos el nombre (Ej: EstadoCuenta_Santander_credito_07_2026.docx)
                String nombre = archivo.getName();
                String[] partes = nombre.replace(".docx", "").split("_");
                
                String banco = partes.length > 1 ? partes[1] : "Desconocido";
                String tipo = partes.length > 2 ? partes[2] : "-";
                String periodo = partes.length > 4 ? partes[3] + "/" + partes[4] : "-";

                modelo.addRow(new Object[]{ banco, tipo, periodo, archivo.getAbsolutePath(), "Abrir" });
            }
        }
    }

    public void abrirDocumento(String rutaAbsoluta) {
        try {
            File archivo = new File(rutaAbsoluta);
            if (archivo.exists()) {
                Desktop.getDesktop().open(archivo);
            } else {
                JOptionPane.showMessageDialog(vista, "El archivo ya no existe en la ruta.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "No se pudo abrir el archivo. ¿Tienes LibreOffice/Word instalado?");
        }
    }
}