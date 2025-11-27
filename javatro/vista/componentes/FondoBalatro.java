package javatro.vista.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.stream.IntStream;

// fondo animado menu principal con shader estilo balatro
public class FondoBalatro extends JPanel {

    private static final int TAMANO_PIXEL = 6; // pixelado estilo retro
    private BufferedImage bufferImagen;
    private int[] pixelesBuffer;
    private int anchoRender, altoRender;

    private float time = 0f;
    private boolean mostrarScanlines = true;

    // paleta de colores rojo azul negro
    private float[] colRojo = { 0.85f, 0.1f, 0.2f };
    private float[] colAzul = { 0.2f, 0.5f, 1.0f };
    private float[] colNegro = { 0.05f, 0.05f, 0.1f };

    public FondoBalatro() {
        // timer para animar el shader 60fps aprox
        new Timer(16, e -> {
            time += 0.015f;
            repaint();
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0)
            return;

        // calcular resolucion reducida para efecto pixelado
        int nw = w / TAMANO_PIXEL;
        int nh = h / TAMANO_PIXEL;

        // crear buffer si no existe o cambio de tamaño
        if (bufferImagen == null || anchoRender != nw || altoRender != nh) {
            anchoRender = nw;
            altoRender = nh;
            bufferImagen = new BufferedImage(anchoRender, altoRender, BufferedImage.TYPE_INT_RGB);
            pixelesBuffer = ((DataBufferInt) bufferImagen.getRaster().getDataBuffer()).getData();
        }

        // ejecutar shader
        renderizarEfecto();

        // escalar imagen pixelada a pantalla completa
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(bufferImagen, 0, 0, w, h, null);

        // aplicar scanlines y vineta crt
        if (mostrarScanlines) {
            g2.setColor(new Color(0, 0, 0, 50));
            for (int i = 0; i < h; i += 4) {
                g2.fillRect(0, i, w, 2);
            }

            RadialGradientPaint p = new RadialGradientPaint(
                    new Point(w / 2, h / 2), w,
                    new float[] { 0.4f, 1.0f },
                    new Color[] { new Color(0, 0, 0, 0), new Color(0, 0, 0, 200) });
            g2.setPaint(p);
            g2.fillRect(0, 0, w, h);
        }
    }

    public void setMostrarScanlines(boolean activar) {
        this.mostrarScanlines = activar;
        repaint();
    }

    // shader principal efecto remolino animado
    private void renderizarEfecto() {
        float centroX = anchoRender / 2f;
        float centroY = altoRender / 2f;
        float timeSlow = time * 0.6f;

        // procesar cada pixel en paralelo para mejor rendimiento
        IntStream.range(0, altoRender).parallel().forEach(py -> {
            for (int px = 0; px < anchoRender; px++) {

                // coordenadas normalizadas desde centro
                float x = (px - centroX) / (float) altoRender;
                float y = (py - centroY) / (float) altoRender;

                // convertir a coordenadas polares
                float dist = (float) Math.sqrt(x * x + y * y);
                float angle = (float) Math.atan2(y, x);

                // crear efecto remolino mas fuerte cerca del centro
                float angleOffset = (1.0f / (dist + 0.1f)) * 0.8f;
                angle += angleOffset + timeSlow;

                // proyectar coordenadas con zoom
                float zoom = 10f;
                float ux = (float) Math.cos(angle) * dist * zoom;
                float uy = (float) Math.sin(angle) * dist * zoom;

                // aplicar distorsion iterativa para patron complejo
                for (int i = 0; i < 3; i++) {
                    float t = (float) Math.sin(uy * 0.5f + timeSlow);
                    ux += t;
                    uy += (float) Math.cos(ux * 0.5f - timeSlow);
                }

                // calcular valor de patron
                float val = (float) Math.sin(ux + uy);

                // asignar colores segun valor positivo negativo o neutro
                float r, g, b;
                float lineThickness = 0.15f;

                if (val > lineThickness) {
                    // zona roja
                    float light = smoothstep(lineThickness, 1.0f, val);
                    r = colRojo[0] * light;
                    g = colRojo[1] * light;
                    b = colRojo[2] * light;
                } else if (val < -lineThickness) {
                    // zona azul
                    float light = smoothstep(lineThickness, 1.0f, Math.abs(val));
                    r = colAzul[0] * light;
                    g = colAzul[1] * light;
                    b = colAzul[2] * light;
                } else {
                    // zona negra lineas de separacion
                    r = colNegro[0];
                    g = colNegro[1];
                    b = colNegro[2];
                }

                // clamp valores entre 0 y 1
                r = Math.max(0f, Math.min(1f, r));
                g = Math.max(0f, Math.min(1f, g));
                b = Math.max(0f, Math.min(1f, b));

                // convertir a color rgb entero
                int c = 0xFF000000
                        | ((int) (r * 255) << 16)
                        | ((int) (g * 255) << 8)
                        | (int) (b * 255);

                pixelesBuffer[py * anchoRender + px] = c;
            }
        });
    }

    // interpolacion suave entre dos valores
    private float smoothstep(float edge0, float edge1, float x) {
        x = Math.max(0f, Math.min(1f, (x - edge0) / (edge1 - edge0)));
        return x * x * (3 - 2 * x);
    }
}