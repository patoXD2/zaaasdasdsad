package javatro.vista.paneles;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javatro.modelo.core.Carta;
import javatro.vista.componentes.PanelCarta;

public class PanelMano extends JPanel {

    private JPanel panelCartas;
    private JButton btnJugar;
    private JButton btnDescartar;
    private JButton btnOrdenarValor;
    private JButton btnOrdenarPalo;

    private JLabel lblMazoVisual;
    private JLabel lblContadorCartas;

    public PanelMano() {
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(25, 25, 25));
        this.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.ORANGE));
        this.setPreferredSize(new Dimension(0, 300));

        inicializarPanelCartas();
        inicializarPanelBotones();
    }

    private void inicializarPanelCartas() {
        panelCartas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelCartas.setOpaque(false);
        this.add(panelCartas, BorderLayout.CENTER);
    }

    private void inicializarPanelBotones() {
        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelDerecha.setOpaque(false);
        panelDerecha.setPreferredSize(new Dimension(420, 280));

        JPanel colBotones = new JPanel(new GridLayout(3, 1, 0, 10));
        colBotones.setOpaque(false);

        JPanel panelSort = new JPanel(new GridLayout(1, 2, 5, 0));
        panelSort.setOpaque(false);

        btnOrdenarValor = crearBotonEstilizado("Valor", new Color(255, 140, 0), 14);
        btnOrdenarPalo = crearBotonEstilizado("Palo", new Color(255, 140, 0), 14);

        panelSort.add(btnOrdenarValor);
        panelSort.add(btnOrdenarPalo);

        btnJugar = crearBotonEstilizado("JUGAR", new Color(0, 180, 80), 24);
        btnDescartar = crearBotonEstilizado("DESCARTAR", new Color(220, 40, 40), 24);

        colBotones.add(panelSort);
        colBotones.add(btnJugar);
        colBotones.add(btnDescartar);

        JPanel colMazo = new JPanel();
        colMazo.setLayout(new BoxLayout(colMazo, BoxLayout.Y_AXIS));
        colMazo.setOpaque(false);

        lblMazoVisual = new JLabel();
        lblMazoVisual.setPreferredSize(new Dimension(105, 145));
        lblMazoVisual.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        lblMazoVisual.setOpaque(true);
        lblMazoVisual.setBackground(new Color(150, 50, 50));
        lblMazoVisual.setAlignmentX(CENTER_ALIGNMENT);

        try {
            URL urlImagen = getClass().getResource("/javatro/recursos/imagenes/reverso.png");
            if (urlImagen != null) {
                ImageIcon icon = new ImageIcon(ImageIO.read(urlImagen));
                Image imgEscalada = icon.getImage().getScaledInstance(105, 145, Image.SCALE_SMOOTH);
                lblMazoVisual.setIcon(new ImageIcon(imgEscalada));
            }
        } catch (Exception e) {
        }

        lblContadorCartas = new JLabel("52");
        lblContadorCartas.setForeground(Color.WHITE);
        lblContadorCartas.setFont(new Font("Arial", Font.BOLD, 18));
        lblContadorCartas.setAlignmentX(CENTER_ALIGNMENT);

        colMazo.add(lblMazoVisual);
        colMazo.add(Box.createRigidArea(new Dimension(0, 5)));
        colMazo.add(lblContadorCartas);

        panelDerecha.add(colBotones);
        panelDerecha.add(colMazo);

        this.add(panelDerecha, BorderLayout.EAST);
    }

    private JButton crearBotonEstilizado(String texto, Color colorFondo, int tamanoLetra) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(colorFondo.darker());
                } else {
                    g.setColor(colorFondo);
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, tamanoLetra));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void mostrarCartas(List<Carta> cartas, Consumer<Carta> onCartaClick) {
        panelCartas.removeAll();
        for (Carta c : cartas) {
            PanelCarta pc = new PanelCarta(c, onCartaClick);
            panelCartas.add(pc);
        }
        panelCartas.revalidate();
        panelCartas.repaint();
    }

    public void actualizarContadorMazo(int restantes) {
        lblContadorCartas.setText(restantes + "/52");
    }

    public JPanel getPanelCartas() {
        return panelCartas;
    }

    public JButton getBtnJugar() {
        return btnJugar;
    }

    public JButton getBtnDescartar() {
        return btnDescartar;
    }

    public JButton getBtnOrdenarValor() {
        return btnOrdenarValor;
    }

    public JButton getBtnOrdenarPalo() {
        return btnOrdenarPalo;
    }
}