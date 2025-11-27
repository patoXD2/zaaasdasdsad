package javatro.vista.paneles;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PanelEstadisticas extends JPanel {
    private JLabel lblObjetivo, lblPuntajeTotal, lblFichas, lblMult, lblRonda, lblDinero;
    private JButton btnInfoManos;
    private JLabel lblManosRestantes, lblDescartesRestantes;
    private JLabel lblNombreManoPrediccion;

    private Color colAzul = new Color(0, 170, 255);
    private Color colRojo = new Color(255, 85, 85);
    private Color colFondo = new Color(40, 40, 45);
    private Color colBoton = new Color(70, 70, 80);
    private Color colObjetivoTop = new Color(180, 130, 50); 
    private Color colObjetivoBot = new Color(25, 25, 30);   

    private boolean animando = false;

    public PanelEstadisticas() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBackground(colFondo);
        this.setPreferredSize(new Dimension(280, 0)); // Un poco más ancho el panel general
        this.setBorder(new EmptyBorder(10, 15, 10, 15));
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        crearSeccionObjetivoMejorada(); 
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        
        crearSeccionPuntaje();
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        
        crearSeccionContadores();
        this.add(Box.createRigidArea(new Dimension(0, 15)));
        
        crearSeccionEconomia();
        
        this.add(Box.createVerticalGlue());

        lblRonda = new JLabel("Ronda 1");
        lblRonda.setForeground(Color.WHITE);
        lblRonda.setFont(new Font("Arial", Font.BOLD, 22));
        lblRonda.setAlignmentX(CENTER_ALIGNMENT);
        this.add(lblRonda);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    // --- CORRECCIÓN 1: CAJAS DEL MISMO ANCHO Y MÁS LARGAS ---
    private void crearSeccionObjetivoMejorada() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(CENTER_ALIGNMENT);
        
        // Dimensión fija para ambas cajas (Ancho 240, Alto variable)
        Dimension dimTop = new Dimension(240, 90);
        Dimension dimBot = new Dimension(240, 60);

        // CAJA 1: PARTE SUPERIOR (DORADA)
        JPanel panelTop = new JPanel();
        panelTop.setBackground(colObjetivoTop);
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            new EmptyBorder(10, 0, 5, 0)
        ));
        panelTop.setPreferredSize(dimTop); // Forzamos tamaño
        panelTop.setMaximumSize(dimTop);   // Forzamos tamaño
        
        JLabel lblTitTop = new JLabel("PUNTAJE A BATIR");
        lblTitTop.setForeground(new Color(255, 255, 230));
        lblTitTop.setFont(new Font("Arial", Font.BOLD, 14)); // Letra un poco más grande
        lblTitTop.setAlignmentX(CENTER_ALIGNMENT);
        
        lblObjetivo = new JLabel("300");
        lblObjetivo.setForeground(Color.WHITE);
        lblObjetivo.setFont(new Font("Verdana", Font.BOLD, 42));
        lblObjetivo.setAlignmentX(CENTER_ALIGNMENT);
        
        panelTop.add(lblTitTop);
        panelTop.add(lblObjetivo);

        // CAJA 2: PARTE INFERIOR (OSCURA)
        JPanel panelBot = new JPanel();
        panelBot.setBackground(colObjetivoBot);
        panelBot.setLayout(new BoxLayout(panelBot, BoxLayout.Y_AXIS));
        panelBot.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            new EmptyBorder(5, 0, 8, 0)
        ));
        panelBot.setPreferredSize(dimBot); // Forzamos tamaño (mismo ancho)
        panelBot.setMaximumSize(dimBot);
        
        JLabel lblTitBot = new JLabel("Puntaje Actual");
        lblTitBot.setForeground(Color.GRAY);
        lblTitBot.setFont(new Font("Arial", Font.BOLD, 12));
        lblTitBot.setAlignmentX(CENTER_ALIGNMENT);

        lblPuntajeTotal = new JLabel("0");
        lblPuntajeTotal.setForeground(Color.WHITE);
        lblPuntajeTotal.setFont(new Font("Verdana", Font.BOLD, 32)); // Más grande
        lblPuntajeTotal.setAlignmentX(CENTER_ALIGNMENT);
        
        panelBot.add(lblTitBot);
        panelBot.add(lblPuntajeTotal);

        container.add(panelTop);
        container.add(Box.createRigidArea(new Dimension(0, 5))); 
        container.add(panelBot);

        this.add(container);
    }

    // --- CORRECCIÓN 2: LÓGICA DE SINCRONIZACIÓN MATEMÁTICA ---
    
 // Método mejorado para visualizar el impacto del puntaje
    public void finalizarAnimacion(int fichasReales, int multReal, int puntosGanadosEnEstaMano, int nuevoTotalAcumulado, Runnable callback) {
        
        // 1. Sincronización Forzada (Por si la animación visual falló por 1 milisegundo)
        lblFichas.setText(String.valueOf(fichasReales));
        lblMult.setText(String.valueOf(multReal));
        
        // Colores de "Cálculo Final"
        lblFichas.setForeground(Color.WHITE);
        lblMult.setForeground(Color.WHITE);
        
        // Usamos un Timer para hacer una secuencia de "Impacto"
        // Paso 0: Mostrar Puntos Ganados en Rojo en el panel inferior
        // Paso 1: Sumar al acumulado
        
        Timer timerFinal = new Timer(600, null); // Un poco más lento para ver el resultado
        final int[] pasos = {0};
        
        timerFinal.addActionListener(e -> {
            if (pasos[0] == 0) {
                // MOMENTO DE MULTIPLICACIÓN:
                // Mostramos cuánto ganamos en esta mano en la caja de abajo temporalmente
                // Ej: "+ 500"
                lblPuntajeTotal.setForeground(colRojo);
                lblPuntajeTotal.setFont(lblPuntajeTotal.getFont().deriveFont(36f)); // Grande
                lblPuntajeTotal.setText("+" + puntosGanadosEnEstaMano);
                
                // Efecto visual en las cajas de arriba también
                lblFichas.setForeground(colRojo);
                lblMult.setForeground(colRojo);
                
                pasos[0]++;
            } else if (pasos[0] == 1) {
                // MOMENTO DE SUMA:
                // Mostramos ya el total acumulado
                lblPuntajeTotal.setForeground(Color.WHITE); // O Verde si prefieres
                lblPuntajeTotal.setFont(lblPuntajeTotal.getFont().deriveFont(28f)); // Normal
                lblPuntajeTotal.setText(String.valueOf(nuevoTotalAcumulado));
                
                pasos[0]++;
                timerFinal.setInitialDelay(800); // Esperar un poco más viendo el resultado final
                timerFinal.restart();
            } else {
                // LIMPIEZA
                timerFinal.stop();
                
                // Restaurar colores y textos por defecto
                lblNombreManoPrediccion.setText(" ");
                lblFichas.setText("0");
                lblMult.setText("0");
                lblFichas.setForeground(Color.WHITE);
                lblMult.setForeground(Color.WHITE);
                
                if (callback != null) callback.run();
            }
        });
        
        timerFinal.setInitialDelay(200); 
        timerFinal.start();
    }

    // --- EL RESTO DE MÉTODOS VISUALES (IGUAL QUE ANTES) ---
    private void crearSeccionPuntaje() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(CENTER_ALIGNMENT);

        lblNombreManoPrediccion = new JLabel(" ");
        lblNombreManoPrediccion.setForeground(Color.WHITE);
        lblNombreManoPrediccion.setFont(new Font("Verdana", Font.BOLD, 26));
        lblNombreManoPrediccion.setAlignmentX(CENTER_ALIGNMENT);
        
        JPanel panelHorizontal = new JPanel();
        panelHorizontal.setLayout(new BoxLayout(panelHorizontal, BoxLayout.X_AXIS));
        panelHorizontal.setOpaque(false);
        
        JPanel boxFichas = crearCajaValor(colAzul, "0");
        lblFichas = (JLabel) boxFichas.getComponent(0);
        JLabel lblX = new JLabel(" X ");
        lblX.setForeground(colRojo);
        lblX.setFont(new Font("Verdana", Font.BOLD, 28));
        JPanel boxMult = crearCajaValor(colRojo, "0");
        lblMult = (JLabel) boxMult.getComponent(0);

        panelHorizontal.add(boxFichas);
        panelHorizontal.add(lblX);
        panelHorizontal.add(boxMult);

        container.add(lblNombreManoPrediccion);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(panelHorizontal);
        this.add(container);
    }

    private JPanel crearCajaValor(Color color, String inicial) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(color);
        panel.setBorder(BorderFactory.createLineBorder(color.darker(), 3));
        panel.setPreferredSize(new Dimension(90, 65));
        panel.setMaximumSize(new Dimension(90, 65));
        JLabel lbl = new JLabel(inicial);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Verdana", Font.BOLD, 30));
        panel.add(lbl);
        return panel;
    }

    private void crearSeccionContadores() {
        JPanel panelContadores = new JPanel(new GridLayout(1, 2, 15, 0));
        panelContadores.setOpaque(false);
        panelContadores.setMaximumSize(new Dimension(240, 80));
        lblManosRestantes = new JLabel("4"); 
        lblDescartesRestantes = new JLabel("3");
        JPanel pManos = crearCajaEstadistica("Manos", lblManosRestantes, new Color(60, 60, 180));
        JPanel pDescartes = crearCajaEstadistica("Descartes", lblDescartesRestantes, new Color(180, 60, 60));
        panelContadores.add(pManos);
        panelContadores.add(pDescartes);
        this.add(panelContadores);
    }
    
    private JPanel crearCajaEstadistica(String titulo, JLabel lblValorExistente, Color bg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bg);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        JLabel lblTit = new JLabel(titulo);
        lblTit.setForeground(new Color(200, 200, 200));
        lblTit.setFont(new Font("Arial", Font.BOLD, 12));
        lblTit.setAlignmentX(CENTER_ALIGNMENT);
        lblValorExistente.setForeground(Color.WHITE);
        lblValorExistente.setFont(new Font("Arial", Font.BOLD, 32));
        lblValorExistente.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(lblTit);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblValorExistente);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private void crearSeccionEconomia() {
        JPanel panelEco = new JPanel();
        panelEco.setLayout(new BoxLayout(panelEco, BoxLayout.Y_AXIS));
        panelEco.setOpaque(false);
        panelEco.setAlignmentX(CENTER_ALIGNMENT);
        lblDinero = new JLabel("$ 0");
        lblDinero.setForeground(new Color(255, 215, 0));
        lblDinero.setFont(new Font("Arial", Font.BOLD, 30));
        lblDinero.setAlignmentX(CENTER_ALIGNMENT);
        lblDinero.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0)); 
        btnInfoManos = new JButton("VER MANOS") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(colBoton.darker());
                else g2.setColor(colBoton);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnInfoManos.setContentAreaFilled(false);
        btnInfoManos.setFocusPainted(false);
        btnInfoManos.setBorderPainted(false);
        btnInfoManos.setForeground(Color.WHITE);
        btnInfoManos.setFont(new Font("Arial", Font.BOLD, 14));
        btnInfoManos.setAlignmentX(CENTER_ALIGNMENT);
        btnInfoManos.setMaximumSize(new Dimension(180, 45));
        btnInfoManos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelEco.add(lblDinero);
        panelEco.add(Box.createRigidArea(new Dimension(0, 15)));
        panelEco.add(btnInfoManos);
        this.add(panelEco);
    }

    public void actualizarPrediccion(String nombreMano, int fichas, int mult) {
        lblNombreManoPrediccion.setText(nombreMano);
        lblFichas.setText(String.valueOf(fichas));
        lblMult.setText(String.valueOf(mult));
    }
    public void prepararAnimacion(int fichasBase, int multBase) {
        lblFichas.setText(String.valueOf(fichasBase));
        lblMult.setText(String.valueOf(multBase));
        lblFichas.setForeground(Color.YELLOW);
    }
    public void sumarFichasInstantaneo(int cantidad) {
        int actual = Integer.parseInt(lblFichas.getText());
        lblFichas.setText(String.valueOf(actual + cantidad));
        lblFichas.setForeground(Color.WHITE);
        Timer t = new Timer(100, e -> lblFichas.setForeground(Color.YELLOW));
        t.setRepeats(false);
        t.start();
    }
    public void actualizarPuntajeAcumulado(int actual, int objetivo) {
        lblPuntajeTotal.setText(String.valueOf(actual));
        lblObjetivo.setText(String.valueOf(objetivo));
        if(actual >= objetivo) lblPuntajeTotal.setForeground(Color.GREEN);
        else lblPuntajeTotal.setForeground(Color.WHITE);
    }
    public void actualizarContadores(int manos, int descartes, int ronda) {
        lblManosRestantes.setText(String.valueOf(manos));
        lblDescartesRestantes.setText(String.valueOf(descartes));
        lblRonda.setText("Ronda " + ronda);
    }
    public void actualizarDinero(int dinero) { lblDinero.setText("$ " + dinero); }
    public JButton getBtnInfoManos() { return btnInfoManos; }
}