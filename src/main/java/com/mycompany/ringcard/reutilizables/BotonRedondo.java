/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ringcard.reutilizables;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;

/**
 *
 * @author Gael
 */

public class BotonRedondo extends JButton {
    private Shape forma;

    public BotonRedondo(String etiqueta) {
        super(etiqueta);
        setOpaque(false); 
        setContentAreaFilled(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());    
        }else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        }else{
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0,0, getWidth()-1, getHeight()-1, 40 , 40);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        if (forma == null || !forma.getBounds().equals(getBounds())) {
            forma = new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 40, 40);
        }
        return forma.contains(x, y);
    }
}