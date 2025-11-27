package javatro.vista.paneles;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import javatro.modelo.core.Carta;
import javatro.vista.componentes.PanelCarta;
import javatro.vista.componentes.FondoJuego;

public class PanelMesa extends JPanel {

    private JPanel areaCartasJugadas;
    private Timer timerAnimacionActual;

    // menu opciones
    private JButton btnOpciones;
    private JPopupMenu popupOpciones;
    private JMenuItem itemContinuar;
    private JMenuItem itemSalir;
    private FondoJuego fondoJuego;
    private PanelJokers panelJokers;

    public PanelMesa() {
        this.setLayout(new BorderLayout());

        // fondo verde animado
        fondoJuego = new FondoJuego();
        this.add(fondoJuego, BorderLayout.CENTER);
        fondoJuego.setLayout(new BorderLayout());

        // panel superior jokers y header
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setOpaque(false);

        // jokers al centro
        panelJokers = new PanelJokers();
        panelSuperior.add(panelJokers, BorderLayout.CENTER);

        // header con boton opciones
        JPanel panelHeader = crearPanelHeader();
        panelSuperior.add(panelHeader, BorderLayout.NORTH);

        // agregar panel superior
        fondoJuego.add(panelSuperior, BorderLayout.NORTH);

        areaCartasJugadas = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 40));
        areaCartasJugadas.setOpaque(false);
        fondoJuego.add(areaCartasJugadas, BorderLayout.CENTER);
    }

    private JPanel crearPanelHeader() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 0, 20));

        // boton opciones
        btnOpciones = new JButton("Opciones") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed())
                    g2.setColor(new Color(20, 20, 20));
                else
                    g2.setColor(new Color(40, 40, 45));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnOpciones.setContentAreaFilled(false);
        btnOpciones.setFocusPainted(false);
        btnOpciones.setBorderPainted(false);
        btnOpciones.setForeground(Color.WHITE);
        btnOpciones.setFont(new Font("Arial", Font.BOLD, 12));
        btnOpciones.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnOpciones.setPreferredSize(new Dimension(100, 35));

        popupOpciones = new JPopupMenu();
        popupOpciones.setBackground(new Color(40, 40, 45));
        popupOpciones.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        itemContinuar = new JMenuItem("Continuar Juego");
        estilizarMenuItem(itemContinuar);
        itemContinuar.addActionListener(e -> {
        });

        itemSalir = new JMenuItem("Volver al Menú");
        estilizarMenuItem(itemSalir);

        popupOpciones.add(itemContinuar);
        JSeparator sep = new JSeparator();
        sep.setForeground(Color.GRAY);
        sep.setBackground(Color.GRAY);
        popupOpciones.add(sep);
        popupOpciones.add(itemSalir);

        btnOpciones.addActionListener(e -> {
            popupOpciones.show(btnOpciones, btnOpciones.getWidth() - popupOpciones.getPreferredSize().width,
                    btnOpciones.getHeight());
        });

        JPanel panelBtnContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnContainer.setOpaque(false);
        panelBtnContainer.add(btnOpciones);

        panelHeader.add(panelBtnContainer, BorderLayout.EAST);
        return panelHeader;
    }

    private void estilizarMenuItem(JMenuItem item) {
        item.setBackground(new Color(40, 40, 45));
        item.setForeground(Color.WHITE);
        item.setFont(new Font("Arial", Font.BOLD, 14));
        item.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setOpaque(true);
    }

    public void setAccionVolverAlMenu(ActionListener l) {
        for (ActionListener al : itemSalir.getActionListeners())
            itemSalir.removeActionListener(al);
        itemSalir.addActionListener(l);
    }

    public void mostrarCartasJugadas(List<Carta> cartas) {
        areaCartasJugadas.removeAll();
        for (Carta c : cartas) {
            PanelCarta pc = new PanelCarta(c, null);
            c.setSeleccionada(false);
            areaCartasJugadas.add(pc);
        }
        areaCartasJugadas.revalidate();
        areaCartasJugadas.repaint();
    }

    // metodo vacio para compatibilidad
    public void mostrarNombreMano(String nombre) {
        // vacio
    }

    public void reproducirSecuenciaPuntaje(List<Carta> cartasPuntuan, Consumer<Integer> onCartaPuntua,
            Runnable alFinalizar) {
        if (timerAnimacionActual != null && timerAnimacionActual.isRunning()) {
            timerAnimacionActual.stop();
        }

        int totalComponentes = areaCartasJugadas.getComponentCount();
        if (totalComponentes == 0) {
            alFinalizar.run();
            return;
        }

        timerAnimacionActual = new Timer(800, null);
        final int[] indice = { 0 };

        timerAnimacionActual.addActionListener(e -> {
            if (indice[0] >= totalComponentes) {
                timerAnimacionActual.stop();
                Timer timerFinal = new Timer(1000, evt -> alFinalizar.run());
                timerFinal.setRepeats(false);
                timerFinal.start();
                return;
            }
            try {
                if (indice[0] > 0 && indice[0] - 1 < areaCartasJugadas.getComponentCount()) {
                    Component prev = areaCartasJugadas.getComponent(indice[0] - 1);
                    if (prev instanceof PanelCarta)
                        ((PanelCarta) prev).limpiarEfecto();
                }
                if (indice[0] < areaCartasJugadas.getComponentCount()) {
                    Component comp = areaCartasJugadas.getComponent(indice[0]);
                    if (comp instanceof PanelCarta) {
                        PanelCarta pc = (PanelCarta) comp;
                        Carta cActual = pc.getCarta();
                        boolean esGanadora = false;
                        for (Carta ganadora : cartasPuntuan) {
                            if (ganadora.getPalo() == cActual.getPalo() &&
                                    ganadora.getValor() == cActual.getValor()) {
                                esGanadora = true;
                                break;
                            }
                        }
                        if (esGanadora) {
                            int valor = cActual.getValor().getValorNumerico();
                            pc.mostrarEfectoPuntaje("+" + valor, new Color(255, 80, 80));
                            javatro.util.GestorAudio.getInstancia().reproducirEfecto("click.wav");
                            if (onCartaPuntua != null)
                                onCartaPuntua.accept(valor);
                        } else {
                            pc.mostrarEfectoPuntaje("", new Color(0, 0, 0, 0));
                        }
                    }
                }
            } catch (Exception ex) {
            }
            indice[0]++;
        });
        timerAnimacionActual.setInitialDelay(300);
        timerAnimacionActual.start();
    }

    public void limpiarMesa() {
        if (timerAnimacionActual != null)
            timerAnimacionActual.stop();
        areaCartasJugadas.removeAll();
        areaCartasJugadas.revalidate();
        areaCartasJugadas.repaint();
    }

    public JButton getBtnOpciones() {
        return btnOpciones;
    }

    public PanelJokers getPanelJokers() {
        return panelJokers;
    }
}