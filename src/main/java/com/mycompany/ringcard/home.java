/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.ringcard;

import com.mycompany.ringcard.data.dataUsuarios;
import com.mycompany.ringcard.data.MovimientoDAO;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class home extends javax.swing.JFrame {

    public int idUsuarioLogueado;
    private Connection con;

    public home(int id) {
        initComponents();
        this.idUsuarioLogueado = id;
        this.setLocationRelativeTo(null);

        // Ajustar diseño de la tabla
        jTable1.setRowHeight(35);
        jTable1.getTableHeader().setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jTable1.getTableHeader().setOpaque(false);
        jTable1.getTableHeader().setBackground(new Color(32, 136, 203));
        jTable1.getTableHeader().setForeground(Color.WHITE);

        dataUsuarios du = new dataUsuarios();
        this.con = du.conectar();

        cargarTarjetasEnTabla();

        // --- LÓGICA PARA INSERTAR LAS GRÁFICAS ---
        // Convertimos jLabel3 y jLabel4 (tus placeholders) a layouts que acepten componentes anidados
        jLabel3.setLayout(new BorderLayout());
        jLabel4.setLayout(new BorderLayout());

        // Vaciamos cualquier texto que tuvieran
        jLabel3.setText("");
        jLabel4.setText("");

        // Generamos los ChartPanels
        ChartPanel panelGraficaDebito = crearGraficaDebito();
        ChartPanel panelGraficaCredito = crearGraficaCredito();

        // Los añadimos al centro de los JLabels
        jLabel3.add(panelGraficaDebito, BorderLayout.CENTER);
        jLabel4.add(panelGraficaCredito, BorderLayout.CENTER);

        // Forzamos actualización visual
        jPanel3.revalidate();
        jPanel3.repaint();
    }

    public void cargarTarjetasEnTabla() {
        try {
            MovimientoDAO dao = new MovimientoDAO(con);
            ResultSet rs = dao.obtenerTarjetasDashboard(idUsuarioLogueado);

            // Ajustamos las columnas para incluir el Día de Corte y el Límite de Crédito de forma explícita
            DefaultTableModel modelo = new DefaultTableModel(
                    new Object[][]{},
                    new String[]{"ID", "Banco", "Tipo", "Límite Crédito", "Día Corte", "Deuda Actual", "Falta Pagar", "Fecha Límite Pago", "Estado", "Acción"}
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 9; // Ahora el botón de acción está en la columna 9
                }
            };

            LocalDate hoy = LocalDate.now();

            while (rs.next()) {
                int id = rs.getInt("id_tarjeta");
                String banco = rs.getString("banco");
                String tipo = rs.getString("tipo");
                double saldoActual = rs.getDouble("saldo_actual");
                double limiteCredito = rs.getDouble("limite_credito");
                double abonado = rs.getDouble("cantidadabonada");
                int diaCorte = rs.getInt("fecha_corte");
                java.sql.Date fechaVencimientoPlastico = rs.getDate("fecha_vencimiento");
                String estadoBD = rs.getString("estado");

                double faltaPagar = 0;
                String estadoReal = "N/A";
                String fechaLimitePagoStr = "N/A";

                // 1. VALIDACIÓN PRIMORDIAL: ¿El plástico de la tarjeta ya expiró?
                if (fechaVencimientoPlastico != null && hoy.isAfter(fechaVencimientoPlastico.toLocalDate())) {
                    estadoReal = "Expirada";
                    if (tipo.equals("Credito")) {
                        faltaPagar = saldoActual - abonado;
                    }
                } // 2. LÓGICA PARA TARJETAS DE CRÉDITO ACTIVAS
                else if (tipo.equals("Credito")) {
                    faltaPagar = saldoActual - abonado;
                    if (faltaPagar < 0) {
                        faltaPagar = 0;
                    }

                    // Determinar la fecha exacta del corte de este ciclo
                    LocalDate fechaCorteEvaluada;
                    if (hoy.getDayOfMonth() > diaCorte) {
                        // Si ya pasamos el día de corte de este mes, el corte ocurrió este mes
                        fechaCorteEvaluada = LocalDate.of(hoy.getYear(), hoy.getMonth(), diaCorte);
                    } else {
                        // Si no hemos llegado al día de corte, corresponde al corte del mes pasado
                        fechaCorteEvaluada = LocalDate.of(hoy.getYear(), hoy.getMonth(), diaCorte).minusMonths(1);
                    }

                    // La fecha límite de pago estándar son 20 días después de la fecha de corte
                    LocalDate fechaLimitePago = fechaCorteEvaluada.plusDays(20);
                    fechaLimitePagoStr = fechaLimitePago.toString();

                    // Evaluar el estado del pago
                    if (faltaPagar <= 0) {
                        estadoReal = "pagado";
                    } else if (hoy.isAfter(fechaLimitePago)) {
                        estadoReal = "vencido"; // Ya pasó la fecha límite y no se ha liquidado
                    } else {
                        estadoReal = "espera"; // Está dentro del plazo de pago
                    }

                    // Sincronizar el estado con la base de datos si hubo cambios automáticos
                    if (!estadoReal.equals(estadoBD)) {
                        actualizarEstadoDB(id, estadoReal);
                    }
                }

                // Agregar la fila con el nuevo orden de columnas
                modelo.addRow(new Object[]{
                    id,
                    banco,
                    tipo,
                    tipo.equals("Credito") ? "$" + limiteCredito : "N/A",
                    tipo.equals("Credito") ? diaCorte : "N/A",
                    "$" + saldoActual,
                    tipo.equals("Credito") ? "$" + faltaPagar : "N/A",
                    fechaLimitePagoStr,
                    estadoReal,
                    tipo.equals("Credito") ? "Abonar" : "-"
                });
            }

            jTable1.setModel(modelo);

            // Vincular los renderizadores visuales a las nuevas posiciones de columnas
            jTable1.setDefaultRenderer(Object.class, new CustomRowRenderer());
            jTable1.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
            jTable1.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(new javax.swing.JCheckBox(), this));

            // Mantener oculto el ID de la tarjeta en la columna 0
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(0).setWidth(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarEstadoDB(int idTarjeta, String nuevoEstado) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE cardscredito SET estado = ? WHERE id_cardcredito = ?");
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idTarjeta);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para aplicar un abono desde el botón de la tabla
    public void aplicarAbono(int idTarjeta, String banco) {
        try {
            String montoStr = JOptionPane.showInputDialog(this, "Ingresa la cantidad a abonar a la tarjeta de crédito " + banco + ":");
            if (montoStr == null || montoStr.trim().isEmpty()) {
                return;
            }

            double montoAbono = Double.parseDouble(montoStr);
            if (montoAbono <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor a 0.");
                return;
            }

            PreparedStatement ps = con.prepareStatement("UPDATE cardscredito SET cantidadabonada = cantidadabonada + ? WHERE id_cardcredito = ?");
            ps.setDouble(1, montoAbono);
            ps.setInt(2, idTarjeta);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Abono registrado correctamente.");
            cargarTarjetasEnTabla(); // Refrescar la tabla para reevaluar la lógica

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa una cantidad numérica válida.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al aplicar el abono: " + e.getMessage());
        }
    }

    // --- CLASES INTERNAS PARA EL DISEÑO DE LA TABLA ---
    // 1. Renderizador para pintar las filas según el estado
    class CustomRowRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Cambiado a la columna 8 que ahora guarda el "Estado"
            String estado = table.getValueAt(row, 8).toString();

            if (!isSelected) {
                switch (estado) {
                    case "vencido":
                        c.setBackground(new Color(255, 102, 102)); // Rojo
                        c.setForeground(Color.BLACK);
                        break;
                    case "pagado":
                        c.setBackground(new Color(144, 238, 144)); // Verde
                        c.setForeground(Color.BLACK);
                        break;
                    case "espera":
                        c.setBackground(new Color(255, 255, 153)); // Amarillo
                        c.setForeground(Color.BLACK);
                        break;
                    case "Expirada":
                        c.setBackground(new Color(180, 180, 180)); // Gris (Plástico caducado)
                        c.setForeground(Color.DARK_GRAY);
                        break;
                    default:
                        c.setBackground(new Color(64, 64, 64)); // Débito
                        c.setForeground(Color.WHITE);
                        break;
                }
            }
            return c;
        }
    }

    // 2. Renderizador visual para el botón
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null && value.toString().equals("-")) {
                return null; // No mostrar botón en Débito
            }
            setText((value == null) ? "Abonar" : value.toString());
            return this;
        }
    }

    // 3. Editor lógico para detectar el clic en el botón
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private JButton button;
        private String label;
        private boolean isPushed;
        private int idTarjetaSeleccionada;
        private String bancoSeleccionado;
        private home homeRef;

        public ButtonEditor(javax.swing.JCheckBox checkBox, home homeRef) {
            this.homeRef = homeRef;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value != null && value.toString().equals("-")) {
                return null;
            }

            label = (value == null) ? "Abonar" : value.toString();
            button.setText(label);
            isPushed = true;
            idTarjetaSeleccionada = (int) table.getModel().getValueAt(row, 0); // ID oculto
            bancoSeleccionado = table.getModel().getValueAt(row, 1).toString(); // Banco
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isPushed) {
                // Al hacer clic, llamamos al método del JFrame principal
                homeRef.aplicarAbono(idTarjetaSeleccionada, bancoSeleccionado);
            }
            isPushed = false;
            fireEditingStopped();
        }
    }

    // ... (AQUÍ DEBES DEJAR TODO TU BLOQUE initComponents() TAL COMO LO TENÍAS) ...
    // No modifico initComponents porque NetBeans lo bloquea, solo asegúrate de 
    // quitar el modelo predeterminado de jTable1 si puedes, ya que lo llenamos por código.
    private void llenarTabla(ResultSet rs) {
        try {
            // 1. Limpiamos cualquier renderizador personalizado (colores y botones) de la vista anterior
            jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer());
            
            // 2. Creamos un modelo completamente nuevo específico para Movimientos (5 columnas)
            DefaultTableModel modelo = new DefaultTableModel(
                new Object [][] {},
                new String [] {"Fecha", "Tarjeta", "Tipo Movimiento", "Concepto", "Monto"}
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Ninguna celda es editable en la vista de movimientos
                }
            };

            // 3. Llenamos el modelo con los datos del ResultSet
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getDate("fecha_movimiento"),
                    rs.getString("tarjeta"),
                    rs.getString("tipo_movimiento"),
                    rs.getString("concepto"),
                    "$" + rs.getDouble("monto") // Le agregamos el signo de pesos por estética
                });
            }

            // 4. Asignamos el nuevo modelo a la tabla
            jTable1.setModel(modelo);

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

        ContentPrincipal = new javax.swing.JPanel();
        panprincipal = new javax.swing.JPanel();
        ContenedorGeneralCyD = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        CBMostrar = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ContentPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panprincipal.setBackground(new java.awt.Color(64, 60, 60));
        panprincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ContenedorGeneralCyD.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(200, 200, 200));

        CBMostrar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CBMostrar.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mostrar Todo", "Ingresos", "Egresos", "Solo Credito", "Solo Debito" }));
        CBMostrar.addActionListener(this::CBMostrarActionPerformed);

        jTable1.setBackground(new java.awt.Color(64, 64, 64));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Banco", "CantidadGastada", "LimiteDeCredito", "A pagar", "Fecha de corte", "Estado"
            }
        ));
        jTable1.setOpaque(false);
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(CBMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 861, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(CBMostrar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        ContenedorGeneralCyD.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, 720));

        panprincipal.add(ContenedorGeneralCyD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, 720));

        jPanel3.setBackground(new java.awt.Color(64, 64, 64));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Debito");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Credito");

        jLabel3.setText("jLabel3");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jLabel4.setText("jLabel3");
        jLabel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(34, 34, 34))
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(92, 92, 92))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jLabel2)
                .addGap(29, 29, 29)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(197, Short.MAX_VALUE))
        );

        panprincipal.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 0, 330, 720));

        ContentPrincipal.add(panprincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));

        jMenu1.setText("Opciones");
        jMenu1.setPreferredSize(new java.awt.Dimension(70, 22));
        jMenu1.addActionListener(this::jMenu1ActionPerformed);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/IcardmovB.png"))); // NOI18N
        jMenuItem1.setText("Movimientos");
        jMenuItem1.addActionListener(this::jMenuItem1ActionPerformed);
        jMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icardgenB.png"))); // NOI18N
        jMenuItem2.setText("General");
        jMenuItem2.addActionListener(this::jMenuItem2ActionPerformed);
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Tarjeta");
        jMenu2.setPreferredSize(new java.awt.Dimension(60, 22));

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/IcardB.png"))); // NOI18N
        jMenuItem3.setText("TarjetasC");
        jMenuItem3.addActionListener(this::jMenuItem3ActionPerformed);
        jMenu2.add(jMenuItem3);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/IcardB.png"))); // NOI18N
        jMenuItem4.setText("TarjetasD");
        jMenuItem4.addActionListener(this::jMenuItem4ActionPerformed);
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ContentPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ContentPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CBMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBMostrarActionPerformed
        dataUsuarios du = new dataUsuarios();
        Connection con = du.conectar();

        MovimientoDAO dao = new MovimientoDAO(con);

        String opcion = CBMostrar.getSelectedItem().toString();

        System.out.println("Seleccionado: " + opcion);

        switch (opcion) {

            case "Mostrar Todo":
                llenarTabla(
                        dao.obtenerTodosLosMovimientos(idUsuarioLogueado)
                );
                break;

            case "Ingresos":
                llenarTabla(
                        dao.obtenerIngresos(idUsuarioLogueado)
                );
                break;

            case "Egresos":
                llenarTabla(
                        dao.obtenerEgresos(idUsuarioLogueado)
                );
                break;

            case "Solo Credito":
                llenarTabla(
                        dao.obtenerSoloCredito(idUsuarioLogueado)
                );
                break;

            case "Solo Debito":
                llenarTabla(
                        dao.obtenerSoloDebito(idUsuarioLogueado)
                );
                break;
        }

    }//GEN-LAST:event_CBMostrarActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Te lleva al panel de Movimientos
        PanleMovimientos panmov = new PanleMovimientos(idUsuarioLogueado);
        panmov.setSize(ContentPrincipal.getSize());
        panmov.setLocation(0, 0);

        ContentPrincipal.removeAll();
        ContentPrincipal.add(panmov, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
        ContentPrincipal.revalidate();
        ContentPrincipal.repaint();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
       panprincipal.setSize(ContentPrincipal.getSize());
        panprincipal.setLocation(0, 0);
        ContentPrincipal.removeAll();
        ContentPrincipal.add(panprincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
        
        // Volvemos a cargar la vista del dashboard con los colores y botones
        cargarTarjetasEnTabla(); 
        
        ContentPrincipal.revalidate();
        ContentPrincipal.repaint();

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        TarjetasAddC tac = new TarjetasAddC(idUsuarioLogueado);
        tac.setSize(ContentPrincipal.getSize());
        tac.setLocation(0, 0);

        ContentPrincipal.removeAll();
        ContentPrincipal.add(tac, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
        ContentPrincipal.revalidate();
        ContentPrincipal.repaint();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        // TODO add your handling code here:
        TarjetasAddD tad = new TarjetasAddD(idUsuarioLogueado);
        tad.setSize(ContentPrincipal.getSize());
        tad.setLocation(0, 0);

        ContentPrincipal.removeAll();
        ContentPrincipal.add(tad, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
        ContentPrincipal.revalidate();
        ContentPrincipal.repaint();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    /**
     * @param args the command line arguments
     */
    /*public static void main(String args[]) {
        //FlatDarkLaf.setup();
        java.awt.EventQueue.invokeLater(() -> new home(0).setVisible(true));
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CBMostrar;
    private javax.swing.JPanel ContenedorGeneralCyD;
    private javax.swing.JPanel ContentPrincipal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panprincipal;
    // End of variables declaration//GEN-END:variables
// Método para crear la gráfica de Crédito
    private ChartPanel crearGraficaCredito() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        double totalIngresos = 0;
        double totalEgresos = 0;

        try {
            // Sumar los ingresos (pagos a la tarjeta) y egresos (compras) de TODAS las tarjetas de crédito del usuario
            String sql = "SELECT tipo_movimiento, SUM(monto) as total FROM movimientos_credito mc "
                    + "JOIN cardscredito cc ON mc.id_cardcredito = cc.id_cardcredito "
                    + "WHERE cc.id_usuario = ? GROUP BY tipo_movimiento";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuarioLogueado);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tipo = rs.getString("tipo_movimiento").toLowerCase();
                if (tipo.equals("ingreso")) {
                    totalIngresos = rs.getDouble("total");
                } else if (tipo.equals("egreso")) {
                    totalEgresos = rs.getDouble("total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataset.setValue("Abonos (Ingresos)", totalIngresos);
        dataset.setValue("Compras (Egresos)", totalEgresos);

        JFreeChart chart = ChartFactory.createPieChart(
                "Movimientos Crédito", // Título
                dataset,
                false, // Leyenda
                true, // Tooltips
                false // URLs
        );

        return estilizarGraficaPie(chart, new Color(50, 150, 250), new Color(250, 80, 80));
    }

    // Método para crear la gráfica de Débito
    private ChartPanel crearGraficaDebito() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        double totalIngresos = 0;
        double totalEgresos = 0;

        try {
            // Sumar ingresos y egresos de TODAS las tarjetas de débito del usuario
            String sql = "SELECT tipo_movimiento, SUM(monto) as total FROM movimientos_debito md "
                    + "JOIN cardsdebito cd ON md.id_carddebito = cd.id_carddebito "
                    + "WHERE cd.id_usuario = ? GROUP BY tipo_movimiento";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuarioLogueado);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String tipo = rs.getString("tipo_movimiento").toLowerCase();
                if (tipo.equals("ingreso")) {
                    totalIngresos = rs.getDouble("total");
                } else if (tipo.equals("egreso")) {
                    totalEgresos = rs.getDouble("total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dataset.setValue("Ingresos", totalIngresos);
        dataset.setValue("Gastos (Egresos)", totalEgresos);

        JFreeChart chart = ChartFactory.createPieChart(
                "Movimientos Débito", // Título
                dataset,
                false, // Leyenda
                true, // Tooltips
                false // URLs
        );

        // Usamos verde para ingresos en débito y rojo/naranja para gastos
        return estilizarGraficaPie(chart, new Color(80, 220, 100), new Color(250, 80, 80));
    }

    // Método genérico para darle un estilo moderno y oscuro a las gráficas
    private ChartPanel estilizarGraficaPie(JFreeChart chart, Color colorIngreso, Color colorEgreso) {
        chart.setBackgroundPaint(new Color(64, 64, 64)); // Fondo igual a tu jPanel3
        chart.getTitle().setPaint(Color.WHITE); // Título en blanco

        // ... código anterior
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setBackgroundPaint(new Color(64, 64, 64));
        plot.setOutlinePaint(null); // <-- Cambio aquí

// Configurar colores de los gajos
        plot.setSectionPaint(0, colorIngreso);
        plot.setSectionPaint(1, colorEgreso);

// Configurar las etiquetas
        PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator(
                "{0}: {1} ({2})", new DecimalFormat("0.00"), new DecimalFormat("0%")
        );
        plot.setLabelGenerator(labelGenerator);
        plot.setLabelBackgroundPaint(new Color(255, 255, 255, 200));
        plot.setLabelShadowPaint(null);
        plot.setLabelOutlinePaint(null);
// <-- Línea de setSimpleLabels eliminada
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setOpaque(false); // Para que se integre bien en tu panel
        return chartPanel;
    }
}
