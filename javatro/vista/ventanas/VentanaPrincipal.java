package javatro.vista.ventanas;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;

import javatro.vista.paneles.PanelEstadisticas;
import javatro.vista.paneles.PanelMano;
import javatro.vista.paneles.PanelMesa;
import javatro.vista.paneles.PanelTienda;
import javatro.controlador.ControladorJuego;

public class VentanaPrincipal extends JFrame {

    private PanelEstadisticas panelStats;
    private PanelMano panelMano;

    private JPanel panelCentro;
    private CardLayout layoutCentro;

    private PanelMesa panelMesa;
    private PanelTienda panelTienda; 

    public VentanaPrincipal() {
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
        if(panelTienda != null) {
            layoutCentro.show(panelCentro, "TIENDA");
            panelMano.setVisible(false); 
        }
    }

    // Getters
    public PanelEstadisticas getPanelEstadisticas() { return panelStats; }
    public PanelMano getPanelMano() { return panelMano; }
    public PanelMesa getPanelMesa() { return panelMesa; }
}