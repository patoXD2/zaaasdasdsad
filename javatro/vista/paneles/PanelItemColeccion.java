package javatro.vista.paneles;

import javax.swing.*;

import javatro.modelo.tienda.InfoJoker;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class PanelItemColeccion extends JPanel {

    private InfoJoker info;
    private Image imagen;
    private boolean mouseEncima = false;

    public PanelItemColeccion(InfoJoker info) {
        this.info = info;
        this.setPreferredSize(new Dimension(100, 140));
        this.setBackground(new Color(0,0,0,0));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cargarImagen();


        if (info.isDescubierto()) {
            this.setToolTipText("<html><b>" + info.getNombre() + "</b><br>" + info.getDescripcion() + "</html>");
        } else {
            this.setToolTipText("??? (Juega más para descubrir)");
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { mouseEncima = true; repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { mouseEncima = false; repaint(); }
        });
    }

    private void cargarImagen() {
        if (!info.isDescubierto()) return; 

        try {

            URL url = getClass().getResource("/javatro/recursos/" + info.getRutaImagen());
            if (url != null) {
                imagen = javax.imageio.ImageIO.read(url);
            }
        } catch (Exception e) { }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        int yOffset = mouseEncima ? -5 : 0;

        if (info.isDescubierto()) {

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(5, 5 + yOffset, w - 10, h - 10, 10, 10);

            if (imagen != null) {
                g2.drawImage(imagen, 10, 10 + yOffset, w - 20, h - 20, this);
            } else {

                g2.setColor(Color.BLACK);
                g2.drawString(info.getNombre(), 15, h/2 + yOffset);
            }


            g2.setColor(new Color(0, 120, 200));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(5, 5 + yOffset, w - 10, h - 10, 10, 10);

        } else {

            g2.setColor(new Color(40, 40, 45)); 
            g2.fillRoundRect(5, 5 + yOffset, w - 10, h - 10, 10, 10);

            g2.setColor(new Color(80, 80, 90)); 
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(5, 5 + yOffset, w - 10, h - 10, 10, 10);

            g2.setColor(new Color(100, 100, 110));
            g2.setFont(new Font("Monospaced", Font.BOLD, 40));

            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth("?")) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString("?", tx, ty + yOffset);
        }
    }
}