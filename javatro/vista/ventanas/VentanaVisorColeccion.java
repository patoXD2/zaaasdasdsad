package javatro.vista.ventanas;

import javatro.modelo.tienda.InfoJoker;
import javatro.vista.componentes.BotonBalatro;
import javatro.vista.componentes.FondoColeccion;
import javatro.vista.paneles.PanelItemColeccion;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaVisorColeccion extends JDialog {

    private JPanel panelDetalle;
    private JLabel lblDetalleTitulo;
    private JTextArea txtDetalleDesc;
    private JLabel lblDetalleImagen;

    public VentanaVisorColeccion(JDialog owner, String categoria) {
        super(owner, "Colección: " + categoria, true);
        setSize(1100, 650);
        setLocationRelativeTo(owner);
        setUndecorated(true);

        initUI(categoria);
    }

    private void initUI(String categoria) {
        FondoColeccion fondo = new FondoColeccion();
        fondo.setLayout(new BorderLayout(0, 0));
        setContentPane(fondo);

        JPanel panelGrid = new JPanel(new GridLayout(0, 5, 15, 15));
        panelGrid.setOpaque(false);

        panelGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        List<InfoJoker> jokers = generarDatos();
        for (InfoJoker info : jokers) {
            PanelItemColeccion item = new PanelItemColeccion(info);

            item.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    actualizarDetalle(info);
                }
            });
            panelGrid.add(item);
        }

        JScrollPane scroll = new JScrollPane(panelGrid);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);


        panelDetalle = new JPanel();

        panelDetalle.setPreferredSize(new Dimension(320, 0));
        panelDetalle.setMinimumSize(new Dimension(320, 0));

        panelDetalle.setBackground(new Color(25, 25, 30)); 
        panelDetalle.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, new Color(80, 80, 80)));
        panelDetalle.setLayout(new BoxLayout(panelDetalle, BoxLayout.Y_AXIS));

        lblDetalleTitulo = new JLabel("Selecciona una carta");
        lblDetalleTitulo.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblDetalleTitulo.setForeground(Color.WHITE);
        lblDetalleTitulo.setAlignmentX(CENTER_ALIGNMENT);
        lblDetalleTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblDetalleTitulo.setMaximumSize(new Dimension(300, 60));

        lblDetalleImagen = new JLabel();
        lblDetalleImagen.setAlignmentX(CENTER_ALIGNMENT);
        lblDetalleImagen.setPreferredSize(new Dimension(180, 260));
        lblDetalleImagen.setMaximumSize(new Dimension(180, 260));
        lblDetalleImagen.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        lblDetalleImagen.setHorizontalAlignment(SwingConstants.CENTER);

        txtDetalleDesc = new JTextArea("Pasa el mouse sobre un Joker para ver sus detalles.");
        txtDetalleDesc.setFont(new Font("Monospaced", Font.PLAIN, 16));
        txtDetalleDesc.setForeground(Color.LIGHT_GRAY);
        txtDetalleDesc.setOpaque(false);
        txtDetalleDesc.setWrapStyleWord(true);
        txtDetalleDesc.setLineWrap(true);
        txtDetalleDesc.setEditable(false);
        txtDetalleDesc.setMaximumSize(new Dimension(280, 300));
        txtDetalleDesc.setMargin(new Insets(10, 10, 10, 10));

        panelDetalle.add(Box.createRigidArea(new Dimension(0, 40)));
        panelDetalle.add(lblDetalleTitulo);
        panelDetalle.add(Box.createRigidArea(new Dimension(0, 20)));
        panelDetalle.add(lblDetalleImagen);
        panelDetalle.add(Box.createRigidArea(new Dimension(0, 30)));
        panelDetalle.add(txtDetalleDesc);

        JPanel panelSur = new JPanel();
        panelSur.setOpaque(false);
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        BotonBalatro btnVolver = new BotonBalatro("VOLVER", new Color(220, 60, 60));
        btnVolver.addActionListener(e -> dispose());
        panelSur.add(btnVolver);

        fondo.add(scroll, BorderLayout.CENTER);
        fondo.add(panelDetalle, BorderLayout.EAST);
        fondo.add(panelSur, BorderLayout.SOUTH);
    }

    private void actualizarDetalle(InfoJoker info) {
        if (info.isDescubierto()) {
            lblDetalleTitulo.setText(info.getNombre());
            txtDetalleDesc.setText(info.getDescripcion());
            lblDetalleImagen.setText("");
            lblDetalleImagen.setIcon(null);

            try {
                java.net.URL url = getClass().getResource("/javatro/recursos/" + info.getRutaImagen());
                if (url != null) {
                    ImageIcon icon = new ImageIcon(javax.imageio.ImageIO.read(url));
                    Image img = icon.getImage().getScaledInstance(180, 260, Image.SCALE_SMOOTH);
                    lblDetalleImagen.setIcon(new ImageIcon(img));
                } else {
                    lblDetalleImagen.setText("NO IMG");
                    lblDetalleImagen.setForeground(Color.WHITE);
                }
            } catch(Exception e) {}

        } else {
            lblDetalleTitulo.setText("???");
            txtDetalleDesc.setText("Desbloquea este Joker jugando partidas.");
            lblDetalleImagen.setIcon(null);
            lblDetalleImagen.setText("?");
            lblDetalleImagen.setForeground(Color.GRAY);
            lblDetalleImagen.setFont(new Font("Monospaced", Font.BOLD, 60));
        }
    }

    private List<InfoJoker> generarDatos() {
        List<InfoJoker> lista = new ArrayList<>();
        // Datos de prueba
        lista.add(new InfoJoker("Bufón", "+4 Multiplicador", "joker_bufon.png", true));
        lista.add(new InfoJoker("Loco", "x4 Mult si sale trébol", "joker_loco.png", true));

        for (int i = 0; i < 30; i++) {
            lista.add(new InfoJoker("Bloqueado", "---", "", false));
        }
        return lista;
    }
}