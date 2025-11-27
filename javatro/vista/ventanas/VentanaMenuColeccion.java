package javatro.vista.ventanas;

import javax.swing.*;

import javatro.vista.componentes.BotonBalatro;
import javatro.vista.componentes.BotonCategoria;
import javatro.vista.componentes.FondoColeccion;

import java.awt.*;

public class VentanaMenuColeccion extends JDialog {

    public VentanaMenuColeccion(JFrame owner) {
        super(owner, "Colección", true);
        setSize(1000, 750);
        setLocationRelativeTo(owner);
        setUndecorated(true);

        initUI();
    }

    private void initUI() {
        FondoColeccion fondo = new FondoColeccion();
        fondo.setLayout(new BorderLayout());
        setContentPane(fondo);

        // Título superior
        JLabel lblTitulo = new JLabel("COLECCIÓN");
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 30));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        fondo.add(lblTitulo, BorderLayout.NORTH);

        // --- PANEL CENTRAL ---
        // Usamos GridLayout de 1 fila y 2 columnas con espacio de 40px en medio
        JPanel panelCentral = new JPanel(new GridLayout(1, 2, 40, 0));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // --- COLUMNA IZQUIERDA ---
        JPanel colIzq = new JPanel();
        colIzq.setLayout(new BoxLayout(colIzq, BoxLayout.Y_AXIS));
        colIzq.setOpaque(false);

        // 1. Botón Jokers (ESTE ES EL QUE FUNCIONA)
        BotonCategoria btnJokers = new BotonCategoria("Jokers", "150 / 150", new Color(255, 77, 77));
        btnJokers.addActionListener(e -> abrirVisor("Jokers"));
        colIzq.add(btnJokers);
        colIzq.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. Otros botones (Solo visuales por ahora)
        agregarBotonNoImplementado(colIzq, "Barajas", "15 / 15", new Color(255, 77, 77));
        agregarBotonNoImplementado(colIzq, "Vouchers", "32 / 32", new Color(255, 77, 77));

        // Sub-panel CONSUMIBLES
        JPanel panelConsumibles = new JPanel();
        panelConsumibles.setLayout(new BoxLayout(panelConsumibles, BoxLayout.Y_AXIS));
        panelConsumibles.setBackground(new Color(0, 0, 0, 80));
        panelConsumibles.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelConsumibles.setMaximumSize(new Dimension(300, 300)); // Evita que se estire demasiado

        agregarBotonNoImplementado(panelConsumibles, "Cartas del tarot", "22 / 22", new Color(136, 85, 255));
        panelConsumibles.add(Box.createRigidArea(new Dimension(0, 10)));

        agregarBotonNoImplementado(panelConsumibles, "Cartas planetarias", "12 / 12", new Color(0, 153, 204));
        panelConsumibles.add(Box.createRigidArea(new Dimension(0, 10)));

        agregarBotonNoImplementado(panelConsumibles, "Cartas espectrales", "18 / 18", new Color(68, 136, 255));

        colIzq.add(panelConsumibles);

        // --- COLUMNA DERECHA ---
        JPanel colDer = new JPanel();
        colDer.setLayout(new BoxLayout(colDer, BoxLayout.Y_AXIS));
        colDer.setOpaque(false);

        agregarBotonNoImplementado(colDer, "Mejoras de cartas", "-", new Color(255, 77, 77));
        agregarBotonNoImplementado(colDer, "Sellos", "-", new Color(255, 77, 77));
        agregarBotonNoImplementado(colDer, "Ediciones", "5 / 5", new Color(255, 77, 77));
        agregarBotonNoImplementado(colDer, "Paquetes potenciadores", "32 / 32", new Color(255, 77, 77));
        agregarBotonNoImplementado(colDer, "Modificadores", "24 / 24", new Color(255, 77, 77));
        agregarBotonNoImplementado(colDer, "Ciegas", "30 / 30", new Color(255, 77, 77));

        panelCentral.add(colIzq);
        panelCentral.add(colDer);

        fondo.add(panelCentral, BorderLayout.CENTER);

        // --- BOTÓN BACK ---
        JPanel panelAbajo = new JPanel();
        panelAbajo.setOpaque(false);
        panelAbajo.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        BotonBalatro btnBack = new BotonBalatro("BACK", new Color(255, 165, 0));
        btnBack.setPreferredSize(new Dimension(400, 60));
        btnBack.addActionListener(e -> dispose());

        panelAbajo.add(btnBack);
        fondo.add(panelAbajo, BorderLayout.SOUTH);
    }

    // Método auxiliar para crear botones que dicen "No implementado"
    private void agregarBotonNoImplementado(JPanel panel, String titulo, String contador, Color color) {
        BotonCategoria btn = new BotonCategoria(titulo, contador, color);
        btn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Función no implementada todavía."));
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
    }

    private void abrirVisor(String categoria) {
        VentanaVisorColeccion visor = new VentanaVisorColeccion(this, categoria);
        visor.setVisible(true);
    }
}