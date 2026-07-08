package com.mycompany.ringcard;


import com.mycompany.ringcard.dao.impl.MovimientoDAOImpl;
import com.mycompany.ringcard.models.Movimiento;
import com.mycompany.ringcard.services.TarjetaService;
import com.mycompany.ringcard.utils.ConexionDB;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.URL;
import javax.swing.JLabel;

public class PanleMovimientos extends javax.swing.JPanel {

    private Movimiento movSeleccionado = null;
    private javax.swing.JPanel panelMovSeleccionado = null;
    private ArrayList<String> nombresTarjetas;
    private ArrayList<Integer> idsTarjetas;
    private ArrayList<String> tiposTarjetas;
    private ArrayList<String> bancosTargetasc;
    private int indiceActual = 0;
    private int idUsuarioLogueado;

    // Nueva clase de servicios
    private TarjetaService tarjetaService;

    public PanleMovimientos(int idUsuario) {
        initComponents(); // INIT COMPONENTS GENERADO POR NETBEANS
        this.idUsuarioLogueado = idUsuario;
        this.nombresTarjetas = new ArrayList<>();
        this.idsTarjetas = new ArrayList<>();
        this.tiposTarjetas = new ArrayList<>();
        this.bancosTargetasc = new ArrayList<>();

        this.tarjetaService = new TarjetaService(); // Servicio Limpio

        ContSCP.setLayout(new BoxLayout(ContSCP, BoxLayout.Y_AXIS));
        ContSCP.removeAll();

        actualizarImagenTarjeta();
        obtenerTarjetasDelUsuario();
        actualizarLabelTarjeta();
        cargarMovimientos();

        btnModificarCard.setVisible(false);
        btnBorrarCard.setVisible(false);

        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnModificarCard.setVisible(true);
                btnBorrarCard.setVisible(true);
            }
        });

        java.awt.event.MouseAdapter mantenerVisible = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnModificarCard.setVisible(true);
                btnBorrarCard.setVisible(true);
            }
        };
        btnModificarCard.addMouseListener(mantenerVisible);
        btnBorrarCard.addMouseListener(mantenerVisible);

        ContCards.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnModificarCard.setVisible(false);
                btnBorrarCard.setVisible(false);
            }
        });

        java.awt.Font fuenteBotones = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14);
        jButton1.setBackground(new java.awt.Color(67, 160, 71));
        jButton1.setForeground(java.awt.Color.WHITE);
        jButton1.setFont(fuenteBotones);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setFocusPainted(false);

        jButton2.setBackground(new java.awt.Color(251, 140, 0));
        jButton2.setForeground(java.awt.Color.WHITE);
        jButton2.setFont(fuenteBotones);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setFocusPainted(false);

        jButton3.setBackground(new java.awt.Color(229, 57, 53));
        jButton3.setForeground(java.awt.Color.WHITE);
        jButton3.setFont(fuenteBotones);
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.setFocusPainted(false);
    }

    private void obtenerTarjetasDelUsuario() {
        tarjetaService.cargarTarjetas(idUsuarioLogueado, nombresTarjetas, idsTarjetas, tiposTarjetas, bancosTargetasc);
    }

    public void cargarMovimientos() {
        ContSCP.removeAll();
        if (idsTarjetas == null || idsTarjetas.isEmpty()) {
            ContSCP.revalidate();
            ContSCP.repaint();
            return;
        }

        try {
            MovimientoDAOImpl dao = new MovimientoDAOImpl();
            int idTarjetaActual = idsTarjetas.get(indiceActual);
            String tipoActual = tiposTarjetas.get(indiceActual);

            java.util.ArrayList<Movimiento> lista = new java.util.ArrayList<>();

            if (tipoActual.equals("debito")) {
                lista = dao.listarMovimientosDebito(idTarjetaActual);
            } else if (tipoActual.equals("credito")) {
                lista = dao.listarMovimientosCredito(idTarjetaActual);
            }

            for (Movimiento mov : lista) {
                registroMovimientos panel = new registroMovimientos(mov);
                panel.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        if (panelMovSeleccionado != null) {
                            panelMovSeleccionado.setBackground(new java.awt.Color(45, 45, 45));
                        }
                        movSeleccionado = mov;
                        panelMovSeleccionado = panel;
                        panel.setBackground(new java.awt.Color(60, 80, 110));
                    }
                });
                ContSCP.add(panel);
            }

            ContSCP.revalidate();
            ContSCP.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarLabelTarjeta() {
        if (!nombresTarjetas.isEmpty()) {
            System.out.println((nombresTarjetas.get(indiceActual)));
        } else {
            jLabel1.setText("No hay tarjetas");
        }
    }

    private void actualizarImagenTarjeta() {
        if (bancosTargetasc.isEmpty()) {
            jLabel1.setIcon(null);
            return;
        }

        String banco = bancosTargetasc.get(indiceActual);
        String tipo = tiposTarjetas.get(indiceActual);
        String ruta = "";

        if (banco.equals("santander")) {
            ruta = tipo.equals("credito") ? "/img/Credito_Red.png" : "/img/debito_Red.png";
        } else if (banco.equals("bbva")) {
            ruta = tipo.equals("credito") ? "/img/Credito_blue.png" : "/img/debito_blue.png";
        } else if (banco.equals("banorte")) {
            ruta = tipo.equals("credito") ? "/img/Credito_black.png" : "/img/debito_black.png";
        }

        SetImageLabel(jLabel1, ruta);
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ContCards = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        btnBorrarCard = new javax.swing.JButton();
        btnModificarCard = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
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
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        ContCards.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 20, -1, -1));

        jButton4.setText(">");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        ContCards.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 300, -1, -1));

        jButton5.setText("<");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        ContCards.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        btnBorrarCard.setText("Borrar");
        btnBorrarCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBorrarCardActionPerformed(evt);
            }
        });
        ContCards.add(btnBorrarCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 410, 80, -1));

        btnModificarCard.setText("Modificar");
        btnModificarCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarCardActionPerformed(evt);
            }
        });
        ContCards.add(btnModificarCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 410, 90, -1));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ContCards.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 178, 410, 260));

        add(ContCards, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 720));

        ScrollContent.setBackground(new java.awt.Color(60, 64, 64));

        ContSCP.setBackground(new java.awt.Color(102, 102, 102));

        javax.swing.GroupLayout ContSCPLayout = new javax.swing.GroupLayout(ContSCP);
        ContSCP.setLayout(ContSCPLayout);
        ContSCPLayout.setHorizontalGroup(
            ContSCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 938, Short.MAX_VALUE)
        );
        ContSCPLayout.setVerticalGroup(
            ContSCPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 628, Short.MAX_VALUE)
        );

        ScrollContent.setViewportView(ContSCP);

        add(ScrollContent, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 0, 760, 610));

        ControlMovimientos.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Modificar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Eliminar");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ControlMovimientosLayout = new javax.swing.GroupLayout(ControlMovimientos);
        ControlMovimientos.setLayout(ControlMovimientosLayout);
        ControlMovimientosLayout.setHorizontalGroup(
            ControlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlMovimientosLayout.createSequentialGroup()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                .addContainerGap())
        );
        ControlMovimientosLayout.setVerticalGroup(
            ControlMovimientosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        add(ControlMovimientos, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 610, 760, 110));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (idsTarjetas == null || idsTarjetas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay tarjetas seleccionadas.");
            return;
        }

        int idTarjetaActual = idsTarjetas.get(indiceActual);
        String tipoActual = tiposTarjetas.get(indiceActual);

        if (tipoActual.equals("debito")) {
            String nombreBancoActual = nombresTarjetas.get(indiceActual);
            MovimientosAddD panmov = new MovimientosAddD(idUsuarioLogueado, idTarjetaActual, nombreBancoActual, this);
            panmov.setSize(ContSCP.getSize());
            panmov.setLocation(0, 0);

            ContSCP.removeAll();
            ContSCP.add(panmov, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
            ContSCP.revalidate();
            ContSCP.repaint();
        } else if (tipoActual.equals("credito")) {
            String nombreBancoActual = nombresTarjetas.get(indiceActual);
            MovimientosAddC panmov = new MovimientosAddC(idUsuarioLogueado, idTarjetaActual, nombreBancoActual, this);
            panmov.setSize(ContSCP.getSize());
            panmov.setLocation(0, 0);

            ContSCP.removeAll();
            ContSCP.add(panmov, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
            ContSCP.revalidate();
            ContSCP.repaint();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (nombresTarjetas != null && !nombresTarjetas.isEmpty()) {
            indiceActual--;
            if (indiceActual < 0) {
                indiceActual = nombresTarjetas.size() - 1;
            }
            actualizarLabelTarjeta();
            cargarMovimientos();
            actualizarImagenTarjeta();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (nombresTarjetas != null && !nombresTarjetas.isEmpty()) {
            indiceActual++;
            if (indiceActual >= nombresTarjetas.size()) {
                indiceActual = 0;
            }
            actualizarLabelTarjeta();
            cargarMovimientos();
            actualizarImagenTarjeta();
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnBorrarCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBorrarCardActionPerformed
        if (nombresTarjetas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay tarjetas para borrar.");
            return;
        }

        String tarjetaSeleccionada = nombresTarjetas.get(indiceActual);
        String tabla = tarjetaSeleccionada.contains("(Débito)") ? "cardsdebito" : "cardscredito";
        String nombreBanco = tarjetaSeleccionada.replace(" (Débito)", "").replace(" (Crédito)", "");
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar la tarjeta de " + nombreBanco + "?\nEsta acción no se puede deshacer.",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminado = tarjetaService.borrarTarjeta(tabla, nombreBanco, idUsuarioLogueado);
            if (eliminado) {
                JOptionPane.showMessageDialog(this, "Tarjeta eliminada con éxito.");
                indiceActual = 0;
                obtenerTarjetasDelUsuario();
                actualizarLabelTarjeta();
                cargarMovimientos();
                actualizarImagenTarjeta();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la tarjeta o ocurrió un error.");
            }
        }
    }//GEN-LAST:event_btnBorrarCardActionPerformed

    private void btnModificarCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarCardActionPerformed
        if (nombresTarjetas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay tarjetas para modificar.");
            return;
        }

        String tarjetaSeleccionada = nombresTarjetas.get(indiceActual);
        String tabla = tarjetaSeleccionada.contains("(Débito)") ? "cardsdebito" : "cardscredito";
        String nombreBancoActual = tarjetaSeleccionada.replace(" (Débito)", "").replace(" (Crédito)", "");
        String nuevoNombre = JOptionPane.showInputDialog(this,
                "Ingresa el nuevo nombre para la tarjeta:", nombreBancoActual);

        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty() && !nuevoNombre.equals(nombreBancoActual)) {
            boolean actualizado = tarjetaService.modificarTarjeta(tabla, nuevoNombre.trim(), nombreBancoActual, idUsuarioLogueado);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Tarjeta actualizada con éxito.");
                obtenerTarjetasDelUsuario();
                actualizarLabelTarjeta();
                actualizarImagenTarjeta();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la tarjeta o ocurrió un error.");
            }
        }
    }//GEN-LAST:event_btnModificarCardActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (movSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un movimiento de la lista dando clic sobre él.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de eliminar este movimiento?\nEl saldo de la tarjeta se reajustará.", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String tipoTarjeta = tiposTarjetas.get(indiceActual);
        int idTarjeta = idsTarjetas.get(indiceActual);
        Connection cx = null;

        try {
            cx = ConexionDB.getInstance();
            cx.setAutoCommit(false);

            String sqlDelete = "";
            String sqlUpdateSaldo = "";
            java.sql.PreparedStatement psDelete = null;
            java.sql.PreparedStatement psUpdate = null;

            if (tipoTarjeta.equals("debito")) {
                sqlDelete = "DELETE FROM movimientos_debito WHERE id_movimiento = ?";
                if (movSeleccionado.getConcepto().equalsIgnoreCase("egreso")) {
                    sqlUpdateSaldo = "UPDATE cardsdebito SET saldo_actual = saldo_actual + ? WHERE id_carddebito = ?";
                } else {
                    sqlUpdateSaldo = "UPDATE cardsdebito SET saldo_actual = saldo_actual - ? WHERE id_carddebito = ?";
                }
            } else if (tipoTarjeta.equals("credito")) {
                sqlDelete = "DELETE FROM movimientos_credito WHERE id_movimiento = ?";
                if (movSeleccionado.getConcepto().equalsIgnoreCase("egreso")) {
                    sqlUpdateSaldo = "UPDATE cardscredito SET saldo_actual = saldo_actual - ? WHERE id_cardcredito = ?";
                } else {
                    sqlUpdateSaldo = "UPDATE cardscredito SET saldo_actual = saldo_actual + ?, cantidadabonada = cantidadabonada - ? WHERE id_cardcredito = ?";
                }
            }

            psDelete = cx.prepareStatement(sqlDelete);
            psDelete.setInt(1, movSeleccionado.getIdMovimiento());
            psDelete.executeUpdate();

            psUpdate = cx.prepareStatement(sqlUpdateSaldo);
            psUpdate.setDouble(1, movSeleccionado.getMonto());
            if (tipoTarjeta.equals("credito") && movSeleccionado.getConcepto().equalsIgnoreCase("ingreso")) {
                psUpdate.setDouble(2, movSeleccionado.getMonto());
                psUpdate.setInt(3, idTarjeta);
            } else {
                psUpdate.setInt(2, idTarjeta);
            }
            psUpdate.executeUpdate();

            cx.commit();
            JOptionPane.showMessageDialog(this, "Movimiento eliminado y saldo restaurado.");
            movSeleccionado = null;
            cargarMovimientos();

        } catch (Exception ex) {
            if (cx != null) try {
                cx.rollback();
            } catch (Exception r) {
            }
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        } finally {
            try {
                cx.setAutoCommit(true);
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (movSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un movimiento para modificar.");
            return;
        }

        String nuevoMontoStr = JOptionPane.showInputDialog(this, "Monto actual: " + movSeleccionado.getMonto() + "\nIngresa el nuevo monto:", movSeleccionado.getMonto());
        if (nuevoMontoStr == null || nuevoMontoStr.trim().isEmpty()) {
            return;
        }

        double nuevoMonto;
        try {
            nuevoMonto = Double.parseDouble(nuevoMontoStr);
            if (nuevoMonto < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Monto inválido.");
            return;
        }

        double diferencia = nuevoMonto - movSeleccionado.getMonto();
        if (diferencia == 0) {
            return;
        }
        String tipoTarjeta = tiposTarjetas.get(indiceActual);
        int idTarjeta = idsTarjetas.get(indiceActual);

        Connection cx = null;
        try {
            cx = ConexionDB.getInstance();
            cx.setAutoCommit(false);

            if (tipoTarjeta.equals("credito") && movSeleccionado.getConcepto().equalsIgnoreCase("egreso")) {
                java.sql.PreparedStatement psCheck = cx.prepareStatement("SELECT saldo_actual, limite_credito FROM cardscredito WHERE id_cardcredito = ?");
                psCheck.setInt(1, idTarjeta);
                java.sql.ResultSet rs = psCheck.executeQuery();
                if (rs.next()) {
                    double saldoActual = rs.getDouble("saldo_actual");
                    double limite = rs.getDouble("limite_credito");
                    if (diferencia > 0 && (saldoActual + diferencia > limite)) {
                        JOptionPane.showMessageDialog(this, "Error: El nuevo monto excede tu límite de crédito disponible.");
                        return;
                    }
                }
            } else if (tipoTarjeta.equals("debito") && movSeleccionado.getConcepto().equalsIgnoreCase("egreso")) {
                java.sql.PreparedStatement psCheck = cx.prepareStatement("SELECT saldo_actual FROM cardsdebito WHERE id_carddebito = ?");
                psCheck.setInt(1, idTarjeta);
                java.sql.ResultSet rs = psCheck.executeQuery();
                if (rs.next()) {
                    if (diferencia > 0 && rs.getDouble("saldo_actual") < diferencia) {
                        JOptionPane.showMessageDialog(this, "Error: Saldo insuficiente en la tarjeta de débito para este aumento.");
                        return;
                    }
                }
            }

            String tablaMov = tipoTarjeta.equals("debito") ? "movimientos_debito" : "movimientos_credito";
            java.sql.PreparedStatement psUpdateMov = cx.prepareStatement("UPDATE " + tablaMov + " SET monto = ? WHERE id_movimiento = ?");
            psUpdateMov.setDouble(1, nuevoMonto);
            psUpdateMov.setInt(2, movSeleccionado.getIdMovimiento());
            psUpdateMov.executeUpdate();

            String sqlUpdateSaldo = "";
            java.sql.PreparedStatement psUpdateSaldo = null;

            if (tipoTarjeta.equals("debito")) {
                if (movSeleccionado.getConcepto().equalsIgnoreCase("egreso")) {
                    sqlUpdateSaldo = "UPDATE cardsdebito SET saldo_actual = saldo_actual - ? WHERE id_carddebito = ?";
                } else {
                    sqlUpdateSaldo = "UPDATE cardsdebito SET saldo_actual = saldo_actual + ? WHERE id_carddebito = ?";
                }
                psUpdateSaldo = cx.prepareStatement(sqlUpdateSaldo);
                psUpdateSaldo.setDouble(1, diferencia);
                psUpdateSaldo.setInt(2, idTarjeta);

            } else if (tipoTarjeta.equals("credito")) {
                if (movSeleccionado.getConcepto().equalsIgnoreCase("egreso")) {
                    sqlUpdateSaldo = "UPDATE cardscredito SET saldo_actual = saldo_actual + ? WHERE id_cardcredito = ?";
                    psUpdateSaldo = cx.prepareStatement(sqlUpdateSaldo);
                    psUpdateSaldo.setDouble(1, diferencia);
                    psUpdateSaldo.setInt(2, idTarjeta);
                } else {
                    sqlUpdateSaldo = "UPDATE cardscredito SET saldo_actual = saldo_actual - ?, cantidadabonada = cantidadabonada + ? WHERE id_cardcredito = ?";
                    psUpdateSaldo = cx.prepareStatement(sqlUpdateSaldo);
                    psUpdateSaldo.setDouble(1, diferencia);
                    psUpdateSaldo.setDouble(2, diferencia);
                    psUpdateSaldo.setInt(3, idTarjeta);
                }
            }

            psUpdateSaldo.executeUpdate();
            cx.commit();
            JOptionPane.showMessageDialog(this, "Movimiento modificado correctamente.");
            movSeleccionado = null;
            cargarMovimientos();

        } catch (Exception ex) {
            if (cx != null) try {
                cx.rollback();
            } catch (Exception r) {
            }
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        } finally {
            try {
                cx.setAutoCommit(true);
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed


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
private void SetImageLabel(JLabel label, String ruta) {
        URL url = getClass().getResource(ruta);
        if (url != null) {
            ImageIcon image = new ImageIcon(url);
            Image imgEscalada = image.getImage().getScaledInstance(
                    label.getWidth(),
                    label.getHeight(),
                    Image.SCALE_SMOOTH
            );
            label.setIcon(new ImageIcon(imgEscalada));
        } else {
            System.out.println("No se encontró la imagen: " + ruta);
        }
    }

}
