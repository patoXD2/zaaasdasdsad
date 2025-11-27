package javatro.vista.componentes;

import javax.swing.*;
import java.awt.*;

public class FondoColeccion extends JPanel {

    public FondoColeccion() {
        this.setBackground(new Color(35, 39, 45));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // colores
        g2.setColor(new Color(255, 255, 255, 10)); 
        int tamano = 20;
        for (int i = 0; i < getWidth(); i+=tamano) {
            for (int j = 0; j < getHeight(); j+=tamano) {
                if ((i/tamano + j/tamano) % 2 == 0) {
                    g2.fillRect(i, j, tamano, tamano);
                }
            }
        }
    }
}