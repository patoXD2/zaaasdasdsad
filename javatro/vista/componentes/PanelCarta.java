package javatro.vista.componentes;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javatro.modelo.core.Carta;

public class PanelCarta extends JPanel {
    private Carta carta;
    private Image imagen;
    private boolean mouseEncima = false;

    // efectos de texto
    private String textoFlotante = null;
    private Color colorTexto = Color.RED;

    // cache de imagenes
    private static Map<String, Image> cacheImagenes = new HashMap<>();

    private static final int ANCHO_CARTA = 110;
    private static final int ALTO_CARTA = 152;
    private static final int ALTO_PANEL = 190;

    public PanelCarta(Carta carta, Consumer<Carta> alHacerClick) {
        this.carta = carta;
        this.setPreferredSize(new Dimension(120, ALTO_PANEL));
        this.setOpaque(false);
        cargarImagen();

        agregarEventosMouse(alHacerClick);
    }

    private void cargarImagen() {
        String ruta = carta.getRutaImagen();

        if (cacheImagenes.containsKey(ruta)) {
            imagen = cacheImagenes.get(ruta);
        } else {
            try {
                URL url = getClass().getResource("/javatro/recursos/imagenes/" + ruta);
                if (url != null) {
                    Image imgTemp = ImageIO.read(url);
                    imagen = imgTemp.getScaledInstance(ANCHO_CARTA, ALTO_CARTA, Image.SCALE_SMOOTH);
                    cacheImagenes.put(ruta, imagen);
                } else {
                    System.err.println("Imagen no encontrada: " + ruta);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void agregarEventosMouse(Consumer<Carta> accion) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (accion != null) {
                    accion.accept(carta);
                    repaint();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (accion != null) {
                    mouseEncima = true;
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (accion != null) {
                    mouseEncima = false;
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    repaint();
                }
            }
        });
    }

    // efectos visuales
    public void mostrarEfectoPuntaje(String texto, Color color) {
        this.textoFlotante = texto;
        this.colorTexto = color;
        repaint();
    }

    public void limpiarEfecto() {
        this.textoFlotante = null;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int yBase = 25;
        if (carta.isSeleccionada() || (mouseEncima && !carta.isSeleccionada())) {
            yBase = 5;
        }

        int xCentrado = (getWidth() - ANCHO_CARTA) / 2;

        // dibujo carta
        if (imagen != null) {
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(xCentrado, yBase, ANCHO_CARTA, ALTO_CARTA, 15, 15);
            g2.drawImage(imagen, xCentrado, yBase, this);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(xCentrado, yBase, ANCHO_CARTA, ALTO_CARTA, 15, 15);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(xCentrado, yBase, ANCHO_CARTA, ALTO_CARTA, 15, 15);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(xCentrado, yBase, ANCHO_CARTA, ALTO_CARTA, 15, 15);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(carta.getValor().name(), xCentrado + 10, yBase + 60);
        }

        // seleccion dorada
        if (carta.isSeleccionada()) {
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(4));
            g2.drawRoundRect(xCentrado, yBase, ANCHO_CARTA, ALTO_CARTA, 15, 15);
        }

        // texto flotante
        if (textoFlotante != null) {
            g2.setFont(new Font("Verdana", Font.BOLD, 36));
            FontMetrics fm = g2.getFontMetrics();
            int textW = fm.stringWidth(textoFlotante);

            int x = (getWidth() - textW) / 2;
            int y = yBase + 25;

            // borde negro
            g2.setColor(Color.BLACK);
            int grosor = 2;
            g2.drawString(textoFlotante, x - grosor, y);
            g2.drawString(textoFlotante, x + grosor, y);
            g2.drawString(textoFlotante, x, y - grosor);
            g2.drawString(textoFlotante, x, y + grosor);

            // texto color
            g2.setColor(colorTexto);
            g2.drawString(textoFlotante, x, y);
        }
    }

    public Carta getCarta() {
        return carta;
    }
}