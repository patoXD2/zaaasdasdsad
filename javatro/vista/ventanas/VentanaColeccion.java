package javatro.vista.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import javatro.modelo.tienda.InfoJoker;
import javatro.vista.componentes.BotonBalatro;
import javatro.vista.componentes.FondoBalatro;
import javatro.vista.paneles.PanelItemColeccion;

public class VentanaColeccion extends JDialog {

    public VentanaColeccion(JFrame owner) {
        super(owner, "Colección de Jokers", true);
        setSize(900, 600);
        setLocationRelativeTo(owner);
        setUndecorated(true); // estilo del jueguito

        inicializarUI();
    }

    private void inicializarUI() {
        // y esto es para el fondo
        FondoBalatro fondo = new FondoBalatro();
        fondo.setLayout(new BorderLayout());
        // se quitan las lineas esas del fondo
        fondo.setMostrarScanlines(false);
        setContentPane(fondo);

        // la cabecera osea donde esta el titulo y ya
        JLabel lblTitulo = new JLabel("COLECCIÓN DE JOKERS");
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // 3. aca esta el panel para las coleccciones
        JPanel panelGrid = new JPanel(new GridLayout(0, 6, 15, 15));
        panelGrid.setOpaque(false);
        panelGrid.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // aca se cargarian los jokers con sus datos desde o un archivo o una DB
        List<InfoJoker> listaJokers = generarDatosPrueba();

        for (InfoJoker info : listaJokers) {
            panelGrid.add(new PanelItemColeccion(info));
        }

        // 4. para poder bajar
        JScrollPane scroll = new JScrollPane(panelGrid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        // bajar pero rapido
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        fondo.add(scroll, BorderLayout.CENTER);

        // volver
        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        BotonBalatro btnVolver = new BotonBalatro("VOLVER", new Color(220, 60, 60));
        btnVolver.addActionListener(e -> dispose());
        panelBoton.add(btnVolver);

        fondo.add(panelBoton, BorderLayout.SOUTH);
    }

    // Cargar jokers reales del catálogo
    private List<InfoJoker> generarDatosPrueba() {
        List<InfoJoker> lista = new ArrayList<>();

        for (javatro.modelo.core.Joker j : javatro.modelo.CatalogoJokers.CATALOGO) {
            // Todos desbloqueados por defecto como pidió el usuario
            lista.add(new InfoJoker(j.getNombre(), j.getDescripcion(), "jokers/" + j.getUrlImagen(), true));
        }

        return lista;
    }
}