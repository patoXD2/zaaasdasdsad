package javatro.vista.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonBalatro extends JButton {
    private Color colorBase;
    private Color colorSombra;
    private boolean presionado = false;
    private boolean mouseEncima = false;

    public BotonBalatro(String texto, Color color) {
        super(texto);
        this.colorBase = color;
        this.colorSombra = color.darker().darker();

        this.setFont(new Font("Monospaced", Font.BOLD, 24));
        this.setForeground(Color.WHITE);
        this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                presionado = true;
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                presionado = false;
                repaint();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseEncima = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                mouseEncima = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF); // Pixel art style

        int w = getWidth();
        int h = getHeight();
        int desplazamiento = presionado ? 4 : 0;


        if (!presionado) {
            g2.setColor(colorSombra);
            g2.fillRect(0, 10, w, h - 10);
        }


        if (mouseEncima && !presionado) {
            g2.setColor(colorBase.brighter());
        } else {
            g2.setColor(colorBase);
        }

        g2.fillRect(0, 0 + desplazamiento, w, h - 10);


        FontMetrics fm = g2.getFontMetrics();
        Rectangle r = new Rectangle(0, 0 + desplazamiento, w, h - 10);
        String text = getText();
        int x = (r.width - fm.stringWidth(text)) / 2;
        int y = (r.height - fm.getHeight()) / 2 + fm.getAscent();

        g2.setColor(new Color(0, 0, 0, 100));
        g2.drawString(text, x + 2, y + 2);


        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 60);
    }
}