package javatro.vista.paneles;

import javatro.modelo.core.Joker;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// panel de slots de jokers del jugador maximo 5
public class PanelJokers extends JPanel {
    private static final int MAX_SLOTS = 5;
    private static final int SLOT_WIDTH = 150;
    private static final int SLOT_HEIGHT = 200;
    private static final int SPACING = 15;

    private List<JokerSlot> slots;

    public PanelJokers() {
        setLayout(new FlowLayout(FlowLayout.CENTER, SPACING, 8));
        setOpaque(false);
        setPreferredSize(new Dimension(0, SLOT_HEIGHT + 20));

        inicializarSlots();
    }

    private void inicializarSlots() {
        slots = new ArrayList<>();
        for (int i = 0; i < MAX_SLOTS; i++) {
            JokerSlot slot = new JokerSlot(i);
            slots.add(slot);
            add(slot);
        }
    }

    public void actualizarJokers(List<Joker> jokers) {
        for (JokerSlot slot : slots) {
            slot.setJoker(null);
        }

        for (int i = 0; i < jokers.size() && i < MAX_SLOTS; i++) {
            slots.get(i).setJoker(jokers.get(i));
        }

        repaint();
    }

    private static class JokerSlot extends JPanel {
        private Joker joker;
        private final int indice;
        private static final Color COLOR_VACIO = new Color(25, 35, 30, 200);
        private static final Color COLOR_BORDE = new Color(60, 100, 80);

        public JokerSlot(int indice) {
            this.indice = indice;
            setPreferredSize(new Dimension(SLOT_WIDTH, SLOT_HEIGHT));
            setOpaque(false);
            setToolTipText("Slot vacío");
        }

        public void setJoker(Joker joker) {
            this.joker = joker;
            if (joker != null) {
                setToolTipText(String.format(
                        "<html><b>%s</b><br>%s<br><i>%s</i></html>",
                        joker.getNombre(),
                        joker.getDescripcion(),
                        joker.getRareza()));
            } else {
                setToolTipText("Slot vacío");
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (joker == null) {
                g2.setColor(COLOR_VACIO);
                g2.fillRoundRect(5, 5, w - 10, h - 10, 15, 15);

                g2.setColor(COLOR_BORDE);
                g2.setStroke(
                        new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 5, 5 }, 0));
                g2.drawRoundRect(5, 5, w - 10, h - 10, 15, 15);

                g2.setColor(new Color(120, 160, 140));
                g2.setFont(new Font("Monospaced", Font.BOLD, 14));
                String texto = String.format("Slot %d", indice + 1);
                FontMetrics fm = g2.getFontMetrics();
                int textoW = fm.stringWidth(texto);
                g2.drawString(texto, (w - textoW) / 2, h / 2);

            } else {
                Color colorRareza = joker.getColorRareza();
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(colorRareza.getRed(), colorRareza.getGreen(), colorRareza.getBlue(), 80),
                        0, h, new Color(colorRareza.getRed(), colorRareza.getGreen(), colorRareza.getBlue(), 40));
                g2.setPaint(gradient);
                g2.fillRoundRect(5, 5, w - 10, h - 10, 15, 15);

                // Dibujar imagen
                try {
                    String ruta = "/javatro/recursos/imagenes/jokers/" + joker.getUrlImagen();
                    java.net.URL imgUrl = getClass().getResource(ruta);
                    if (imgUrl != null) {
                        Image img = javax.imageio.ImageIO.read(imgUrl);
                        // Escalar manteniendo aspecto
                        int imgW = w - 20;
                        int imgH = h - 80; // Espacio para texto
                        g2.drawImage(img, 10, 40, imgW, imgH, null);
                    }
                } catch (Exception e) {
                    // Fallback si falla imagen
                }

                g2.setColor(colorRareza);

                g2.setColor(colorRareza);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(5, 5, w - 10, h - 10, 15, 15);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Monospaced", Font.BOLD, 14));
                String nombre = joker.getNombre();
                if (nombre.length() > 10) {
                    nombre = nombre.substring(0, 9) + "...";
                }
                FontMetrics fm = g2.getFontMetrics();
                int nombreW = fm.stringWidth(nombre);
                g2.drawString(nombre, (w - nombreW) / 2, 25);

                g2.setFont(new Font("Monospaced", Font.BOLD, 24));
                String valorTexto = "+" + joker.getValor();
                fm = g2.getFontMetrics();
                int valorW = fm.stringWidth(valorTexto);
                g2.drawString(valorTexto, (w - valorW) / 2, h / 2 + 5);

                g2.setFont(new Font("Monospaced", Font.PLAIN, 12));
                String tipo = joker.getTipo().toString();
                int tipoW = g2.getFontMetrics().stringWidth(tipo);
                g2.drawString(tipo, (w - tipoW) / 2, h / 2 + 25);

                g2.setFont(new Font("Monospaced", Font.ITALIC, 10));
                g2.setColor(colorRareza);
                String rareza = joker.getRareza();
                int rarezaW = g2.getFontMetrics().stringWidth(rareza);
                g2.drawString(rareza, (w - rarezaW) / 2, h - 15);
            }
        }
    }
}
