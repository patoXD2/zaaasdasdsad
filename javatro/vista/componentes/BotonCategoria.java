package javatro.vista.componentes;

import javax.swing.*;
import java.awt.*;

public class BotonCategoria extends JButton {
    private Color colorFondo;
    private String titulo;
    private String contador;

    public BotonCategoria(String titulo, String contador, Color color) {
        this.titulo = titulo;
        this.contador = contador;
        this.colorFondo = color;

        Dimension dim = new Dimension(280, 70);
        this.setPreferredSize(dim);
        this.setMinimumSize(dim);
        this.setMaximumSize(dim);

        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        this.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); 

        int w = getWidth();
        int h = getHeight();

        if (w < 50 || h < 20) return;

        int offset = getModel().isPressed() ? 4 : 0;

        g2.setColor(colorFondo.darker().darker());
        g2.fillRoundRect(0, 8, w, h - 8, 12, 12);

        g2.setColor(getModel().isRollover() ? colorFondo.brighter() : colorFondo);
        g2.fillRoundRect(0, offset, w, h - 8, 12, 12);

        g2.setFont(new Font("Monospaced", Font.BOLD, 22));
        FontMetrics fm = g2.getFontMetrics();
        String texto = titulo.toUpperCase();
        int x = (w - fm.stringWidth(texto)) / 2;
        int yTitulo = 30;

        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(texto, x + 2, yTitulo + offset + 2);

        g2.setColor(Color.WHITE);
        g2.drawString(texto, x, yTitulo + offset);

        if (!contador.equals("-")) {
            g2.setFont(new Font("Monospaced", Font.BOLD, 14));
            fm = g2.getFontMetrics();
            x = (w - fm.stringWidth(contador)) / 2;
            g2.setColor(new Color(255, 255, 255, 180));
            g2.drawString(contador, x, 50 + offset);
        }
    }
}