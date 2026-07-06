/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ringcard.reutilizables;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Gael
 */
public class PanelRedondeado extends JPanel{
    private int radio = 60; 
    
    public PanelRedondeado() {
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
        g2.dispose();
        super.paintComponent(g);
    }
}
