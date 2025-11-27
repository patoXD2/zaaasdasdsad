package javatro.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.function.Consumer;

import javatro.modelo.tienda.ItemTienda;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Joker;
import javatro.vista.paneles.*;

public class VistaJuego extends JFrame {

    private PanelEstadisticas panelStats;
    private PanelMano panelMano;
    private PanelMesa panelMesa;
    private PanelTienda panelTienda;

    // layout central
    private JPanel panelCentro;
    private CardLayout layoutCentro;

    public VistaJuego() {
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        this.setTitle("Javatro - Poker Roguelike");
        this.setSize(1280, 720);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {

        panelStats = new PanelEstadisticas();
        this.add(panelStats, BorderLayout.WEST);

        panelMano = new PanelMano();
        this.add(panelMano, BorderLayout.SOUTH);

        layoutCentro = new CardLayout();
        panelCentro = new JPanel(layoutCentro);

        panelMesa = new PanelMesa();
        panelCentro.add(panelMesa, "MESA");

        this.add(panelCentro, BorderLayout.CENTER);
    }

    public void registrarTienda(PanelTienda tienda) {
        this.panelTienda = tienda;
        panelCentro.add(tienda, "TIENDA");
    }

    public void mostrarMesa() {
        layoutCentro.show(panelCentro, "MESA");
        panelMano.setVisible(true);
    }

    public void mostrarTienda() {
        if (panelTienda != null) {
            layoutCentro.show(panelCentro, "TIENDA");
            panelMano.setVisible(false);
        }
    }

    public void actualizarManoJugador(List<Carta> cartas, Consumer<Carta> onCartaClick) {
        panelMano.mostrarCartas(cartas, onCartaClick);
    }

    public void actualizarMesa(List<Carta> cartas) {
        panelMesa.mostrarCartasJugadas(cartas);
    }

    public void actualizarEstadisticas(int puntaje, int objetivo, int manos, int descartes, int dinero, int ronda) {
        panelStats.actualizarPuntajeAcumulado(puntaje, objetivo);
        panelStats.actualizarContadores(manos, descartes, ronda);
        panelStats.actualizarDinero(dinero);

        if (panelTienda != null && panelTienda.isVisible()) {
            panelTienda.actualizarDinero();
        }
    }

    // prediccion
    public void actualizarPrediccion(String nombreMano, int fichas, int mult) {
        panelStats.actualizarPrediccion(nombreMano, fichas, mult);
    }

    public void setAccionJugar(ActionListener l) {
        panelMano.getBtnJugar().addActionListener(l);
    }

    public void setAccionDescartar(ActionListener l) {
        panelMano.getBtnDescartar().addActionListener(l);
    }

    public void setAccionVolverAlMenu(ActionListener l) {
        panelMesa.setAccionVolverAlMenu(l);
    }

    public void setAccionOrdenarValor(ActionListener l) {
        panelMano.getBtnOrdenarValor().addActionListener(l);
    }

    public void setAccionOrdenarPalo(ActionListener l) {

        panelMano.getBtnOrdenarPalo().addActionListener(l);
    }

    public JPanel getPanelCartasContainer() {
        return panelMano.getPanelCartas();
    }

    public void actualizarTienda(List<ItemTienda> items) {
        if (panelTienda != null) {
            panelTienda.mostrarItems(items);
        }
    }

    /**
     * Actualiza la visualización de jokers.
     * Ahora delega a PanelMesa que contiene el PanelJokers.
     */
    public void actualizarJokers(List<Joker> jokers) {
        if (panelMesa != null && panelMesa.getPanelJokers() != null) {
            panelMesa.getPanelJokers().actualizarJokers(jokers);
        }
    }

    public PanelEstadisticas getPanelEstadisticas() {
        return panelStats;
    }

    public PanelMano getPanelMano() {
        return panelMano;
    }

    public PanelMesa getPanelMesa() {
        return panelMesa;
    }
}