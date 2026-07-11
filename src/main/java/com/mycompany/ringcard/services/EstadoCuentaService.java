package com.mycompany.ringcard.services;

import com.mycompany.ringcard.dao.IMovimientoDAO;
import com.mycompany.ringcard.models.Movimiento;
import com.mycompany.ringcard.utils.ConfigManager;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane; // Importante para las alertas

public class EstadoCuentaService {

    private IMovimientoDAO movimientoDAO;

    public EstadoCuentaService(IMovimientoDAO movimientoDAO) {
        this.movimientoDAO = movimientoDAO;
    }

    public void actualizarEstadoCuenta(int idTarjeta, String banco, String tipoTarjeta) {
        String rutaBase = ConfigManager.getRutaDocumentos();
        
        // 1. Validar si la ruta existe
        if (rutaBase == null || rutaBase.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "No se pudo crear el DOCX porque no hay una ruta configurada.\nVe al inicio para configurar la carpeta.", 
                "Ruta no encontrada", JOptionPane.WARNING_MESSAGE);
            return; 
        }

        File carpetaDestino = new File(rutaBase);
        if (!carpetaDestino.exists() || !carpetaDestino.isDirectory()) {
            JOptionPane.showMessageDialog(null, 
                "La carpeta seleccionada para los DOCX ya no existe o no es válida: \n" + rutaBase, 
                "Error de Carpeta", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate hoy = LocalDate.now();
        String mesAnio = String.format("%02d_%d", hoy.getMonthValue(), hoy.getYear());
        String nombreArchivo = "EstadoCuenta_" + banco + "_" + tipoTarjeta + "_" + mesAnio + ".docx";
        File archivoSalida = new File(rutaBase, nombreArchivo);

        ArrayList<Movimiento> movimientos = tipoTarjeta.equalsIgnoreCase("credito") ? 
            movimientoDAO.listarMovimientosCredito(idTarjeta) : 
            movimientoDAO.listarMovimientosDebito(idTarjeta);

        try (XWPFDocument documento = new XWPFDocument()) {
            XWPFParagraph titulo = documento.createParagraph();
            titulo.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun runTitulo = titulo.createRun();
            runTitulo.setText("Estado de Cuenta - " + banco.toUpperCase() + " (" + tipoTarjeta.toUpperCase() + ")");
            runTitulo.setBold(true);
            runTitulo.setFontSize(16);

            XWPFParagraph fechaDoc = documento.createParagraph();
            fechaDoc.createRun().setText("Periodo: " + hoy.getMonth().name() + " " + hoy.getYear());

            XWPFTable tabla = documento.createTable();
            XWPFTableRow filaCabecera = tabla.getRow(0);
            filaCabecera.getCell(0).setText("Fecha");
            filaCabecera.addNewTableCell().setText("Concepto");
            filaCabecera.addNewTableCell().setText("Tipo");
            filaCabecera.addNewTableCell().setText("Monto");

            double totalAbonos = 0;
            double totalCargos = 0;
            boolean hayMovimientosDelMes = false;

            for (Movimiento mov : movimientos) {
                LocalDate fechaMov = mov.getFechaMovimiento().toLocalDate();
                if (fechaMov.getMonthValue() == hoy.getMonthValue() && fechaMov.getYear() == hoy.getYear()) {
                    hayMovimientosDelMes = true;
                    XWPFTableRow fila = tabla.createRow();
                    fila.getCell(0).setText(mov.getFechaMovimiento().toString());
                    fila.getCell(1).setText(mov.getConcepto());
                    fila.getCell(2).setText(mov.getTipoMovimiento().toUpperCase());
                    fila.getCell(3).setText("$" + String.format("%.2f", mov.getMonto()));
                    
                    if(mov.getTipoMovimiento().equalsIgnoreCase("ingreso")) totalAbonos += mov.getMonto();
                    else totalCargos += mov.getMonto();
                }
            }

            XWPFParagraph resumen = documento.createParagraph();
            XWPFRun runResumen = resumen.createRun();
            runResumen.addBreak();
            runResumen.setText("Total Ingresos/Abonos: $" + String.format("%.2f", totalAbonos));
            runResumen.addBreak();
            runResumen.setText("Total Egresos/Cargos: $" + String.format("%.2f", totalCargos));

            try (FileOutputStream out = new FileOutputStream(archivoSalida)) {
                documento.write(out);
            }
            
            // ¡Aviso de éxito!
            JOptionPane.showMessageDialog(null, "Documento actualizado:\n" + archivoSalida.getAbsolutePath(), "DOCX Generado", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            // 2. Si falla la creación, mostramos exactamente por qué
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Ocurrió un error al intentar crear el archivo DOCX:\n" + e.toString(), 
                "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
}