package javatro.vista.paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants; // FALTABA ESTO

import javatro.controlador.ControladorJuego;
import javatro.modelo.tienda.ItemTienda;
import javatro.vista.componentes.BotonBalatro;

public class PanelTienda extends JPanel {

    private ControladorJuego controlador;
    private JLabel lblDinero;
    private JPanel panelItems;

    public PanelTienda(ControladorJuego controlador) {
        this.controlador = controlador;
        this.setLayout(new BorderLayout());

        this.setBackground(new Color(35, 39, 45)); 

        inicializarUI();
    }

    private void inicializarUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));

        JLabel lblTitulo = new JLabel("TIENDA");
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 50));
        lblTitulo.setForeground(Color.WHITE);

        lblDinero = new JLabel("$ 0");
        lblDinero.setFont(new Font("Monospaced", Font.BOLD, 40));
        lblDinero.setForeground(new Color(255, 215, 0)); 

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(lblDinero, BorderLayout.EAST);
        this.add(header, BorderLayout.NORTH);

        panelItems = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panelItems.setOpaque(false);

        panelItems.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 50, 20, 50),
                BorderFactory.createDashedBorder(Color.GRAY, 2, 5)
        ));

        this.add(panelItems, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        footer.setOpaque(false);
        footer.setPreferredSize(new Dimension(0, 150));

        BotonBalatro btnReroll = new BotonBalatro("REROLL ($5)", new Color(220, 60, 60));
        btnReroll.addActionListener(e -> {
            controlador.solicitarReroll(); 
        });

        BotonBalatro btnSiguiente = new BotonBalatro("SIGUIENTE RONDA", new Color(0, 120, 255));
        btnSiguiente.setPreferredSize(new Dimension(300, 60));
        btnSiguiente.addActionListener(e -> controlador.cerrarTienda());

        footer.add(btnReroll);
        footer.add(btnSiguiente);
        this.add(footer, BorderLayout.SOUTH);
    }

    public void abrirTienda() {
        actualizarDinero();
        this.setVisible(true);
    }

    public void actualizarDinero() {
        if(controlador != null) {
            lblDinero.setText("$ " + controlador.getDinero());
        }
    }

    public void mostrarItems(List<ItemTienda> items) {
        panelItems.removeAll();

        for (int i = 0; i < items.size(); i++) {
            ItemTienda item = items.get(i);
            final int index = i; 

            JPanel panelCarta = crearItemVisual(item, e -> {
                controlador.intentarComprar(index); 
            });

            panelItems.add(panelCarta);
        }
        panelItems.revalidate();
        panelItems.repaint();
    }

    private JPanel crearItemVisual(ItemTienda item, java.awt.event.ActionListener accionCompra) {
        JPanel panelCarta = new JPanel();
        panelCarta.setLayout(new BoxLayout(panelCarta, BoxLayout.Y_AXIS));
        panelCarta.setOpaque(false); 

        panelCarta.setPreferredSize(new Dimension(160, 260));
        panelCarta.setMaximumSize(new Dimension(160, 260));

        panelCarta.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));

        JLabel lblImg = new JLabel();
        lblImg.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);

        lblImg.setPreferredSize(new Dimension(100, 140));
        lblImg.setMaximumSize(new Dimension(100, 140));

        try {

            URL url = getClass().getResource("/javatro/recursos/imagenes/" + item.getRutaImagen());
            
            if (url != null) {
                ImageIcon icon = new ImageIcon(javax.imageio.ImageIO.read(url));
                Image img = icon.getImage().getScaledInstance(100, 140, Image.SCALE_SMOOTH);
                lblImg.setIcon(new ImageIcon(img));
            } else {
                lblImg.setText("<html><center>" + item.getNombre() + "</center></html>");
                lblImg.setForeground(Color.WHITE);
                lblImg.setFont(new Font("Arial", Font.BOLD, 14));
            }
        } catch (Exception e) {
            lblImg.setText("?");
        }

        String tooltipHTML = "<html><div style='text-align:center;'><b>" + item.getNombre() + "</b><br>" 
                           + item.getDescripcion() + "</div></html>";
        panelCarta.setToolTipText(tooltipHTML);
        lblImg.setToolTipText(tooltipHTML);

        BotonBalatro btnComprar = new BotonBalatro("$" + item.getPrecio(), new Color(0, 180, 80));
        btnComprar.setFont(new Font("Monospaced", Font.BOLD, 18));
        btnComprar.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnComprar.setPreferredSize(new Dimension(140, 45));
        btnComprar.setMaximumSize(new Dimension(140, 45));

        btnComprar.addActionListener(accionCompra);

        panelCarta.add(Box.createRigidArea(new Dimension(0, 15))); 
        panelCarta.add(lblImg);
        panelCarta.add(Box.createVerticalGlue());
        panelCarta.add(btnComprar);
        panelCarta.add(Box.createRigidArea(new Dimension(0, 15))); 

        panelCarta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panelCarta.setBackground(new Color(255, 255, 255, 20));
                panelCarta.setOpaque(true);
                panelCarta.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); 
                panelCarta.repaint();
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panelCarta.setOpaque(false);
                panelCarta.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));
                panelCarta.repaint();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return panelCarta;
    }
}