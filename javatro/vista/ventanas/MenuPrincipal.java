package javatro.vista.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;

import javatro.controlador.ControladorJuego;
import javatro.vista.componentes.BotonBalatro;
import javatro.vista.componentes.FondoBalatro;

public class MenuPrincipal extends JFrame {

    private Image imagenLogo;

    public MenuPrincipal() {
        setTitle("Javatro - Menú");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FondoBalatro panelFondo = new FondoBalatro();
        panelFondo.setLayout(new BorderLayout());
        this.setContentPane(panelFondo);

        // logo
        JPanel panelCentro = new JPanel(new GridBagLayout());
        panelCentro.setOpaque(false);

        JLabel lblLogo = new JLabel();
        if (imagenLogo != null) {
            Image logoEscalado = imagenLogo.getScaledInstance(850, 380, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(logoEscalado));
        } else {
            lblLogo.setText("JAVATRO");
            lblLogo.setFont(new Font("Monospaced", Font.BOLD, 100));
            lblLogo.setForeground(Color.WHITE);
            lblLogo.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 0));
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 0, 0);
        panelCentro.add(lblLogo, gbc);

        panelFondo.add(panelCentro, BorderLayout.CENTER);

        // botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 60));
        panelBotones.setOpaque(false);
        panelBotones.setPreferredSize(new Dimension(0, 180));

        // jugar
        BotonBalatro btnPlay = new BotonBalatro("JUGAR", new Color(0, 120, 255));
        btnPlay.addActionListener(e -> iniciarJuego());

        // opciones
        BotonBalatro btnOptions = new BotonBalatro("OPCIONES", new Color(255, 165, 0));
        btnOptions.addActionListener(e -> mostrarOpciones());

        // coleccion
        BotonBalatro btnCollection = new BotonBalatro("COLECCION", new Color(60, 180, 100));
        btnCollection.addActionListener(e -> {
            VentanaMenuColeccion menuCol = new VentanaMenuColeccion(this);
            menuCol.setVisible(true);
        });

        // salir
        BotonBalatro btnQuit = new BotonBalatro("SALIR", new Color(220, 60, 60));
        btnQuit.addActionListener(e -> System.exit(0));

        panelBotones.add(btnPlay);
        panelBotones.add(btnOptions);
        panelBotones.add(btnCollection);
        panelBotones.add(btnQuit);

        panelFondo.add(panelBotones, BorderLayout.SOUTH);
    }

    private void iniciarJuego() {
        this.dispose();

        SwingUtilities.invokeLater(() -> {
            javatro.vista.VistaJuego ventanaJuego = new javatro.vista.VistaJuego();

            Runnable alPerderOSalir = () -> {
                ventanaJuego.dispose();
                new MenuPrincipal().setVisible(true);
            };

            new javatro.controlador.ControladorJuego(ventanaJuego, alPerderOSalir);

            ventanaJuego.setVisible(true);
        });
    }

    private void mostrarOpciones() {
        JDialog dialogo = new JDialog(this, "OPCIONES", true);
        dialogo.setSize(400, 450);
        dialogo.setLocationRelativeTo(this);
        dialogo.setUndecorated(true);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 35));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));

        // audio
        JLabel lblTitulo = new JLabel("VOLUMEN MAESTRO");
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // la barrita del volumen : v
        int volInicial = (int) (javatro.util.GestorAudio.getInstancia().getVolumen() * 100);
        JSlider sliderVolumen = new JSlider(0, 100, volInicial);
        sliderVolumen.setBackground(new Color(30, 30, 35));
        sliderVolumen.setForeground(Color.ORANGE);
        sliderVolumen.setMaximumSize(new Dimension(300, 40));

        // subir volumen o bajar cuando cambiar el cosito del volumen
        sliderVolumen.addChangeListener(e -> {
            float valorNormalizado = sliderVolumen.getValue() / 100f;
            javatro.util.GestorAudio.getInstancia().setVolumen(valorNormalizado);
        });

        // video
        JLabel lblVideo = new JLabel("GRÁFICOS");
        lblVideo.setFont(new Font("Monospaced", Font.BOLD, 20));
        lblVideo.setForeground(Color.WHITE);
        lblVideo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // casilla pantalla completa
        JCheckBox chkFullscreen = new JCheckBox("PANTALLA COMPLETA");
        chkFullscreen.setBackground(new Color(30, 30, 35));
        chkFullscreen.setForeground(Color.WHITE);
        chkFullscreen.setFont(new Font("Monospaced", Font.BOLD, 16));
        chkFullscreen.setFocusPainted(false);
        chkFullscreen.setAlignmentX(Component.CENTER_ALIGNMENT);
        chkFullscreen.setSelected(getExtendedState() == JFrame.MAXIMIZED_BOTH);

        chkFullscreen.addActionListener(e -> {
            if (chkFullscreen.isSelected()) {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                setExtendedState(JFrame.NORMAL);
                setLocationRelativeTo(null);
            }
        });

        // casilla efecto crt(crt es tele vieja por asi decirlo)
        JCheckBox chkCRT = new JCheckBox("EFECTO CRT");
        chkCRT.setBackground(new Color(30, 30, 35));
        chkCRT.setForeground(Color.WHITE);
        chkCRT.setFont(new Font("Monospaced", Font.BOLD, 16));
        chkCRT.setFocusPainted(false);
        chkCRT.setAlignmentX(Component.CENTER_ALIGNMENT);
        chkCRT.setSelected(true);

        chkCRT.addActionListener(e -> {
            // se busca el fondo y se cambia el estado de este
            if (getContentPane() instanceof FondoBalatro) {
                ((FondoBalatro) getContentPane()).setMostrarScanlines(chkCRT.isSelected());
            }
        });

        // boton cerrar
        BotonBalatro btnCerrar = new BotonBalatro("CERRAR", new Color(220, 60, 60));
        btnCerrar.addActionListener(e -> dialogo.dispose());
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // añadir todo
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(lblTitulo);
        panel.add(sliderVolumen);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(lblVideo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(chkFullscreen);
        panel.add(chkCRT);

        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(btnCerrar);

        dialogo.add(panel);
        dialogo.setVisible(true);
    }
}