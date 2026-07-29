package com.mycompany.ringcard;

import com.mycompany.ringcard.reutilizables.BotonRedondo;
import java.text.ParseException;
import javax.swing.JPanel;
import javax.swing.text.MaskFormatter;

public class MovimientosAddC extends javax.swing.JPanel {

    private int idUsuarioLog;
    private int idTarjeta;
    private JPanel panelPadre;
    private String nombreBanco;

    public MovimientosAddC(int idUsuarioLogueado, int idTarjetaSeleccionada, String nombreBanco, JPanel padre) {
        initComponents(); // ESTE ES EL QUE GENERA NETBEANS, DÉJALO INTACTO.

        this.idUsuarioLog = idUsuarioLogueado;
        this.idTarjeta = idTarjetaSeleccionada;
        this.nombreBanco = nombreBanco;
        this.panelPadre = padre;

        jLabel1.setText("Tarjeta a hacer el cambio: " + this.nombreBanco);
        //aplicarEstilosModernos();

        try {
            MaskFormatter mascaraFecha = new MaskFormatter("##/##/####");
            mascaraFecha.setPlaceholderCharacter('_');
            mascaraFecha.install(jFormattedTextField1);
        } catch (ParseException ex) {
            System.err.println("Error en el formato de la fecha: " + ex.getMessage());
        }

        // Conectamos la vista con su controlador
        new com.mycompany.ringcard.controllers.MovimientoCreditoController(
                this,
                new com.mycompany.ringcard.dao.impl.MovimientoDAOImpl(),
                this.idTarjeta
        );
    }

// --- GETTERS PARA EL CONTROLADOR ---
    public javax.swing.JComboBox<String> getCmbTipo() {
        return jComboBox1;
    }

    public javax.swing.JFormattedTextField getTxtFecha() {
        return jFormattedTextField1;
    }

    public javax.swing.JTextField getTxtConcepto() {
        return jTextField1;
    }

    public javax.swing.JSpinner getSpnMonto() {
        return jSpinner1;
    }

    public javax.swing.JButton getBtnGuardar() {
        return jButton2;
    }

    public javax.swing.JButton getBtnVolver() {
        return jButton1;
    }

    // AGREGA ESTE NUEVO GETTER:
    public String getNombreBanco() {
        return this.nombreBanco;
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new BotonRedondo("Ingresar");
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jButton2 = new BotonRedondo("Ingresar");
        jFormattedTextField1 = new javax.swing.JFormattedTextField();

        setBackground(new java.awt.Color(20, 20, 0));
        setPreferredSize(new java.awt.Dimension(760, 610));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Tarjeta a hacer el cambio:");

        jButton1.setBackground(new java.awt.Color(59, 56, 62));
        jButton1.setText("volver");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ingreso", "Egreso" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Tipo Movimeinto");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Fecha Movimeinto");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Concepto");

        jTextField1.setText("Ingresa el Consepto");
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
        });
        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Monto");

        jSpinner1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jSpinner1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jButton2.setBackground(new java.awt.Color(59, 56, 62));
        jButton2.setText("Agregar");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jFormattedTextField1.setText("jFormattedTextField1");
        jFormattedTextField1.addActionListener(this::jFormattedTextField1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(240, 240, 240)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                    .addComponent(jComboBox1, 0, 140, Short.MAX_VALUE)
                                    .addComponent(jSpinner1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                    .addComponent(jFormattedTextField1)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52)))
                        .addGap(0, 218, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(173, 173, 173)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(68, 68, 68)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        volverAtras();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jFormattedTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        // TODO add your handling code here:
        if (jTextField1.getText().equals("Ingresa el Consepto")) {
            jTextField1.setText("");
        }
    }//GEN-LAST:event_jTextField1FocusGained

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
    /*private void aplicarEstilosModernos() {
        this.setBackground(new java.awt.Color(20, 20, 0));
        java.awt.Font fuenteEtiquetas = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);
        java.awt.Color colorTexto = java.awt.Color.WHITE;

        javax.swing.JLabel[] etiquetas = {jLabel1, jLabel2, jLabel3, jLabel4, jLabel5};
        for (javax.swing.JLabel label : etiquetas) {
            label.setFont(fuenteEtiquetas);
            label.setForeground(colorTexto);
        }

        jComboBox1.setBackground(new java.awt.Color(64, 64, 64));
        jComboBox1.setForeground(colorTexto);
        jComboBox1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        jComboBox1.setFocusable(false);

        java.awt.Color fondoInputs = new java.awt.Color(64, 64, 64);
        jFormattedTextField1.setBackground(fondoInputs);
        jFormattedTextField1.setForeground(colorTexto);
        jFormattedTextField1.setCaretColor(colorTexto);
        jFormattedTextField1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(100, 100, 100)),
                javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        jTextField1.setBackground(fondoInputs);
        jTextField1.setForeground(colorTexto);
        jTextField1.setCaretColor(colorTexto);
        jTextField1.setBorder(jFormattedTextField1.getBorder());
        jTextField1.setText("");

        jSpinner1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        javax.swing.JComponent editor = jSpinner1.getEditor();
        if (editor instanceof javax.swing.JSpinner.DefaultEditor) {
            javax.swing.JSpinner.DefaultEditor spinnerEditor = (javax.swing.JSpinner.DefaultEditor) editor;
            spinnerEditor.getTextField().setBackground(fondoInputs);
            spinnerEditor.getTextField().setForeground(colorTexto);
            spinnerEditor.getTextField().setCaretColor(colorTexto);
        }

        java.awt.Font fuenteBotones = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);
        jButton1.setBackground(new java.awt.Color(100, 100, 100));
        jButton1.setForeground(java.awt.Color.WHITE);
        jButton1.setFont(fuenteBotones);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusPainted(false);

        jButton2.setBackground(new java.awt.Color(67, 160, 71));
        jButton2.setForeground(java.awt.Color.WHITE);
        jButton2.setFont(fuenteBotones);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusPainted(false);
    }*/

    public void volverAtras() {
        java.awt.Container contenedor = this.getParent();
        if (contenedor != null) {
            contenedor.remove(this);
            if (panelPadre instanceof PanleMovimientos) {
                ((PanleMovimientos) panelPadre).cargarMovimientos();
            }
            contenedor.revalidate();
            contenedor.repaint();
        }
    }
}
