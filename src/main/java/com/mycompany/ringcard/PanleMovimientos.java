/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.ringcard;


import com.mycompany.ringcard.clases.Movimiento;
import com.mycompany.ringcard.clases.MovimientoDAO;
import com.mycompany.ringcard.data.dataUsuarios;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Gael
 */
public class PanleMovimientos extends javax.swing.JPanel {
private ArrayList<String> nombresTarjetas;
    private int indiceActual = 0;
    
    // Es vital recibir el ID del usuario para saber de quién son las tarjetas
    private int idUsuarioLogueado;
    /**
     * Creates new form PanleMovimientos
     */
    public PanleMovimientos(int idUsuario) {
        initComponents();
        this.idUsuarioLogueado = idUsuario;
        this.nombresTarjetas = new ArrayList<>();
        ContCards.setComponentZOrder(btnBorrarCard, 0);
        ContCards.setComponentZOrder(btnModificarCard, 1);
        
        this.idUsuarioLogueado = idUsuario;
        this.nombresTarjetas = new ArrayList<>();
        
        ContSCP.setLayout(new BoxLayout(ContSCP, BoxLayout.Y_AXIS));
        ContSCP.removeAll();
        
        // 1. Cargamos la lista de tarjetas desde la BD
        obtenerTarjetasDelUsuario();
        
        // 2. Mostramos la primera tarjeta en el JLabel
        actualizarLabelTarjeta();
        
        // 3. Cargamos los movimientos (puedes modificar este método para que dependa de la tarjeta seleccionada)
// ... tu código anterior del constructor ...
        cargarMovimientos(); 

        // 1. Ocultar los botones apenas se abre el panel
        btnModificarCard.setVisible(false);
        btnBorrarCard.setVisible(false);

        // 2. Evento para MOSTRAR los botones al pasar el mouse sobre el Label
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnModificarCard.setVisible(true);
                btnBorrarCard.setVisible(true);
            }
        });

        // 3. (Opcional pero recomendado) Mantenerlos visibles si el mouse pasa sobre los botones mismos
        java.awt.event.MouseAdapter mantenerVisible = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnModificarCard.setVisible(true);
                btnBorrarCard.setVisible(true);
            }
        };
        btnModificarCard.addMouseListener(mantenerVisible);
        btnBorrarCard.addMouseListener(mantenerVisible);

        // 4. Evento para OCULTAR los botones cuando el mouse toca el panel de fondo oscuro
        ContCards.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnModificarCard.setVisible(false);
                btnBorrarCard.setVisible(false);
            }
        });
    } // <-- Cierre del constructor
    private void obtenerTarjetasDelUsuario() {
        nombresTarjetas.clear();
        try {
            dataUsuarios db = new dataUsuarios();
            Connection cx = db.conectar();
            
            // Consulta para obtener tarjetas de débito
            String sqlDebito = "SELECT banco FROM cardsdebito WHERE id_usuario = ?";
            java.sql.PreparedStatement psDeb = cx.prepareStatement(sqlDebito);
            psDeb.setInt(1, this.idUsuarioLogueado);
            java.sql.ResultSet rsDeb = psDeb.executeQuery();
            while(rsDeb.next()) {
                nombresTarjetas.add(rsDeb.getString("banco") + " (Débito)");
            }
            
            // Consulta para obtener tarjetas de crédito
            String sqlCredito = "SELECT banco FROM cardscredito WHERE id_usuario = ?";
            java.sql.PreparedStatement psCred = cx.prepareStatement(sqlCredito);
            psCred.setInt(1, this.idUsuarioLogueado);
            java.sql.ResultSet rsCred = psCred.executeQuery();
            while(rsCred.next()) {
                nombresTarjetas.add(rsCred.getString("banco") + " (Crédito)");
            }
            
            cx.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarLabelTarjeta() {
        if (!nombresTarjetas.isEmpty()) {
            jLabel1.setText(nombresTarjetas.get(indiceActual));
        } else {
            jLabel1.setText("No hay tarjetas");
        }
    }
public void cargarMovimientos() {

    ContSCP.removeAll();

    try {

        dataUsuarios db = new dataUsuarios();
        Connection cx = db.conectar();

        MovimientoDAO dao =
                new MovimientoDAO(cx);

        ArrayList<Movimiento> lista =
                dao.listarMovimientosDebito(1);

        System.out.println("Registros encontrados: "
                + lista.size());

        for (Movimiento mov : lista) {

            registroMovimientos panel =
                    new registroMovimientos(mov);

            ContSCP.add(panel);
        }

        ContSCP.revalidate();
        ContSCP.repaint();

    } catch (Exception e) {

        e.printStackTrace();
    }
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ContCards = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        btnBorrarCard = new javax.swing.JButton();
        btnModificarCard = new javax.swing.JButton();
        ScrollContent = new javax.swing.JScrollPane();
        ContSCP = new javax.swing.JPanel();
        ControlMovimientos = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setBackground(new java.awt.Color(200, 200, 200));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ContCards.setBackground(new java.awt.Color(64, 64, 64));
        ContCards.setPreferredSize(new java.awt.Dimension(475, 475));
        ContCards.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mostrar todo", "Por Mes", "Por Semana" }));
        ContCards.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(207, 6, -1, -1));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Cards");
        jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        ContCards.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(77, 198, 199, 120));

        jButton4.setText(">");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        ContCards.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(299, 246, -1, -1));

        jButton5.setText("<");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        ContCards.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 246, -1, -1));

        btnBorrarCard.setText("Borrar");
        btnBorrarCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarCardActionPerformed(evt);
            }
        });
        ContCards.add(btnBorrarCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 100, -1));

        btnModificarCard.setText("Modificar");
        btnModificarCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarCardActionPerformed(evt);
            }
        });
        ContCards.add(btnModificarCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 290, 100, -1));

        add(ContCards, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 550));

        ScrollContent.setBackground(new java.awt.Color(60, 64, 64));

        ContSCP.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout ContSCPLayout = new javax.swing.GroupLayout(ContSCP);
        ContSCP.setLayout(ContSCPLayout);
        ContSCPLayout.setHorizontalGroup(
            ContSCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );
        ContSCPLayout.setVerticalGroup(
            ContSCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 462, Short.MAX_VALUE)
        );

        ScrollContent.setViewportView(ContSCP);

        add(ScrollContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 610, 460));

        ControlMovimientos.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Modificar");

        jButton3.setText("Eliminar");

        javax.swing.GroupLayout ControlMovimientosLayout = new javax.swing.GroupLayout(ControlMovimientos);
        ControlMovimientos.setLayout(ControlMovimientosLayout);
        ControlMovimientosLayout.setHorizontalGroup(
            ControlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlMovimientosLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 113, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(91, 91, 91)
                .addComponent(jButton3)
                .addGap(84, 84, 84))
        );
        ControlMovimientosLayout.setVerticalGroup(
            ControlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlMovimientosLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(ControlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        add(ControlMovimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 460, 610, 90));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        if (nombresTarjetas != null && nombresTarjetas.size() > 0) {
            indiceActual--;
            // Si retrocedemos más allá de la posición 0, vamos a la última tarjeta
            if (indiceActual < 0) {
                indiceActual = nombresTarjetas.size() - 1; 
            }
            actualizarLabelTarjeta();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if (nombresTarjetas != null && nombresTarjetas.size() > 0) {
            indiceActual++;
            // Si pasamos el límite de la lista, volvemos a la posición 0
            if (indiceActual >= nombresTarjetas.size()) {
                indiceActual = 0; 
            }
            actualizarLabelTarjeta();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnBorrarCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarCardActionPerformed
        if (nombresTarjetas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay tarjetas para borrar.");
            return;
        }

        // 1. Extraer datos de la tarjeta seleccionada
        String tarjetaSeleccionada = nombresTarjetas.get(indiceActual);
        String tabla = tarjetaSeleccionada.contains("(Débito)") ? "cardsdebito" : "cardscredito";
        String nombreBanco = tarjetaSeleccionada.replace(" (Débito)", "").replace(" (Crédito)", "");

        // 2. Pedir confirmación al usuario
        int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que deseas eliminar la tarjeta de " + nombreBanco + "?\nEsta acción no se puede deshacer.", 
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                dataUsuarios db = new dataUsuarios();
                Connection cx = db.conectar();

                // 3. Consulta SQL dinámica (se adapta a crédito o débito)
                String sql = "DELETE FROM " + tabla + " WHERE banco = ? AND id_usuario = ?";
                java.sql.PreparedStatement ps = cx.prepareStatement(sql);
                ps.setString(1, nombreBanco);
                ps.setInt(2, this.idUsuarioLogueado);

                int filasAfectadas = ps.executeUpdate();

                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Tarjeta eliminada con éxito.");
                    // 4. Refrescar la interfaz para que desaparezca la tarjeta borrada
                    indiceActual = 0; 
                    obtenerTarjetasDelUsuario();
                    actualizarLabelTarjeta();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la tarjeta.");
                }

                cx.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnBorrarCardActionPerformed

    private void btnModificarCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarCardActionPerformed
       if (nombresTarjetas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay tarjetas para modificar.");
            return;
        }

        // 1. Extraer datos actuales
        String tarjetaSeleccionada = nombresTarjetas.get(indiceActual);
        String tabla = tarjetaSeleccionada.contains("(Débito)") ? "cardsdebito" : "cardscredito";
        String nombreBancoActual = tarjetaSeleccionada.replace(" (Débito)", "").replace(" (Crédito)", "");

        // 2. Pedir el nuevo nombre
        String nuevoNombre = JOptionPane.showInputDialog(this, 
                "Ingresa el nuevo nombre para la tarjeta:", nombreBancoActual);

        // Si el usuario presiona cancelar o lo deja en blanco, no hacemos nada
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty() && !nuevoNombre.equals(nombreBancoActual)) {
            try {
                dataUsuarios db = new dataUsuarios();
                Connection cx = db.conectar();

                // 3. Consulta UPDATE dinámica
                String sql = "UPDATE " + tabla + " SET banco = ? WHERE banco = ? AND id_usuario = ?";
                java.sql.PreparedStatement ps = cx.prepareStatement(sql);
                ps.setString(1, nuevoNombre.trim());
                ps.setString(2, nombreBancoActual);
                ps.setInt(3, this.idUsuarioLogueado);

                int filasAfectadas = ps.executeUpdate();

                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Tarjeta actualizada con éxito.");
                    
                    obtenerTarjetasDelUsuario();
                    actualizarLabelTarjeta();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar la tarjeta.");
                }

                cx.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error de base de datos: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnModificarCardActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ContCards;
    private javax.swing.JPanel ContSCP;
    private javax.swing.JPanel ControlMovimientos;
    private javax.swing.JScrollPane ScrollContent;
    private javax.swing.JButton btnBorrarCard;
    private javax.swing.JButton btnModificarCard;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
