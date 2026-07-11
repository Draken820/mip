package com.mycompany.ringcard;

import com.mycompany.ringcard.controllers.PanelEstadosCuentaController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanelEstadosCuenta extends JPanel {

    private JTable jTable1;
    private PanelEstadosCuentaController controlador;

    public PanelEstadosCuenta() {
        initComponents();
        this.controlador = new PanelEstadosCuentaController(this);
        configurarTabla();
        controlador.cargarArchivosEnTabla((DefaultTableModel) jTable1.getModel());
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Banco", "Tipo", "Periodo", "Ruta Oculta", "Acción"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 4; }
        };

        jTable1.setModel(modelo);
        jTable1.setRowHeight(40);
        jTable1.getTableHeader().setBackground(new Color(50, 50, 50));
        jTable1.getTableHeader().setForeground(Color.WHITE);
        jTable1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        jTable1.setBackground(new Color(64, 64, 64));
        jTable1.setForeground(Color.WHITE);
        jTable1.setShowGrid(false);

        // Ocultar la ruta absoluta
        jTable1.getColumnModel().getColumn(3).setMinWidth(0);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(0);
        jTable1.getColumnModel().getColumn(3).setWidth(0);

        // Renderizador del botón "Abrir"
        jTable1.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), controlador));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(45, 45, 45));

        JLabel titulo = new JLabel("Mis Estados de Cuenta (DOCX)", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        jTable1 = new JTable();
        JScrollPane scrollPane = new JScrollPane(jTable1);
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));
        add(scrollPane, BorderLayout.CENTER);
    }

    // --- CLASES PARA EL BOTÓN EN LA TABLA ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Abrir DOCX");
            setBackground(new Color(67, 160, 71)); // Verde Material
            setForeground(Color.WHITE);
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String rutaSeleccionada;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox, PanelEstadosCuentaController ctrl) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> {
                if (isPushed && rutaSeleccionada != null) {
                    ctrl.abrirDocumento(rutaSeleccionada);
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            rutaSeleccionada = table.getModel().getValueAt(row, 3).toString();
            button.setText("Abrir DOCX");
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return "Abrir DOCX";
        }
    }
}