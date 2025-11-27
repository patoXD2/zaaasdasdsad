package javatro.vista.ventanas;

import javatro.controlador.ControladorJuego;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PanelAdmin extends JDialog {

    private ControladorJuego controlador;

    public PanelAdmin(JFrame owner, ControladorJuego controlador) {
        super(owner, "ADMIN CONSOLE - JAVATRO", false); // false = no modal (puedes jugar con esto abierto)
        this.controlador = controlador;

        setSize(300, 400);
        setLocation(10, 10); // Esquina superior izquierda
        setLayout(new GridLayout(0, 1, 5, 5));

        // Cerrar con F1
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    setVisible(false);
                }
            }
        });

        inicializarBotones();
    }

    private void inicializarBotones() {
        add(crearBoton("GANAR RONDA (Instant)", e -> controlador.adminGanarRonda()));
        add(crearBoton("SALTAR A TIENDA", e -> controlador.adminIrTienda()));
        add(crearBoton("+$100 DINERO", e -> controlador.adminDarDinero(100)));
        add(crearBoton("+10 MULTIPLICADOR", e -> controlador.adminDarMult(10)));
        add(crearBoton("REINICIAR MANOS/DESCARTES", e -> controlador.adminResetManos()));
        add(crearBoton("PERDER (Game Over)", e -> controlador.adminPerder()));
        add(crearBoton("CERRAR CONSOLA (F1)", e -> setVisible(false)));
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setBackground(Color.BLACK);
        btn.setForeground(Color.GREEN); // Estilo "Hacker"
        btn.setFont(new Font("Consolas", Font.BOLD, 12));
        btn.setFocusable(false); // Importante para no robar el foco del teclado
        btn.addActionListener(accion);
        return btn;
    }
}