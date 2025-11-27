package javatro.controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.function.Consumer;

import javatro.modelo.JuegoModelo;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Joker;
import javatro.modelo.interfaces.ObservadorJuego;
import javatro.modelo.tienda.ItemTienda;
import javatro.vista.VistaJuego;
import javatro.vista.ventanas.PanelAdmin;
import javatro.vista.paneles.PanelTienda;

public class ControladorJuego implements ObservadorJuego {

    private VistaJuego vista;
    private JuegoModelo modelo;
    private PanelAdmin panelAdmin;
    private PanelTienda panelTienda;
    private Runnable accionIrAlMenu;
    private Runnable accionVolverMenu;

    public ControladorJuego(VistaJuego vista, Runnable accionVolverMenu) {
        this.vista = vista;
        this.accionVolverMenu = accionVolverMenu; // Guardamos la acción aquí
        this.modelo = new JuegoModelo();

        modelo.agregarObservador(this);

        this.panelAdmin = new PanelAdmin(vista, this);
        this.panelTienda = new PanelTienda(this);

        vista.registrarTienda(panelTienda);
        vista.mostrarMesa();

        setupAdminKeys();
        asignarEventosBotones();

        modelo.reiniciarJuegoTotal();
    }

    private void setupAdminKeys() {
        java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == java.awt.event.KeyEvent.KEY_PRESSED && e.getKeyCode() == java.awt.event.KeyEvent.VK_F1) {
                panelAdmin.setVisible(!panelAdmin.isVisible());
                return true;
            }
            return false;
        });
    }

    private void asignarEventosBotones() {
        // --- BOTÓN JUGAR ---
        vista.setAccionJugar(e -> {
            List<Carta> seleccionadas = obtenerSeleccionadas();

            if (seleccionadas.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Selecciona al menos 1 carta.");
            } else {
                // 1. Bloqueamos el botón para que no le den click dos veces
                vista.getPanelMano().getBtnJugar().setEnabled(false);

                // 2. Llamamos al modelo.
                // NOTA: Ya NO ponemos el Timer aquí.
                // La limpieza se hará automáticamente en el método 'onManoJugada'
                // cuando la animación visual termine.
                modelo.jugarMano(seleccionadas);
            }
        });

        // --- BOTÓN DESCARTAR ---
        vista.setAccionDescartar(e -> {
            List<Carta> seleccionadas = obtenerSeleccionadas();
            if (!seleccionadas.isEmpty()) {
                modelo.descartar(seleccionadas);
                javatro.util.GestorAudio.getInstancia().reproducirEfecto("click.wav");
            }
        });

        // --- OTROS BOTONES ---
        vista.setAccionOrdenarValor(e -> modelo.ordenarManoPorValor());
        vista.setAccionOrdenarPalo(e -> modelo.ordenarManoPorPalo());

        vista.getPanelEstadisticas().getBtnInfoManos().addActionListener(e -> mostrarInfoManos());

        // --- CONEXIÓN MVC DEL BOTÓN OPCIONES ---
        vista.setAccionVolverAlMenu(e -> {
            // Lógica del Controlador: Preguntar y ejecutar acción
            int confirm = JOptionPane.showConfirmDialog(vista,
                    "¿Seguro que quieres salir al menú?",
                    "Opciones", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION && accionVolverMenu != null) {
                accionVolverMenu.run(); // Ejecuta el Runnable que recibimos del main
            }
        });
    }

    private void gestionarClickCarta(Carta c) {
        if (c.isSeleccionada()) {
            c.toggleSeleccion();
        } else {
            if (obtenerSeleccionadas().size() < 5) {
                c.toggleSeleccion();
                javatro.util.GestorAudio.getInstancia().reproducirEfecto("click.wav");
            } else {

                System.out.println("Límite de 5 cartas alcanzado");
            }
        }

        modelo.calcularPrediccion(obtenerSeleccionadas());
        vista.getPanelMano().repaint();
    }

    private List<Carta> obtenerSeleccionadas() {
        List<Carta> sel = new ArrayList<>();
        for (Carta c : modelo.getManoJugador()) {
            if (c.isSeleccionada())
                sel.add(c);
        }
        return sel;
    }

    private void mostrarInfoManos() {
        String info = "GUÍA DE PUNTAJES:\n\n" +
                "Escalera Real:  100 x 8\n" +
                "Poker:          60 x 7\n" +
                "Full House:     40 x 4\n" +
                "Color:          35 x 4\n" +
                "Escalera:       30 x 4\n" +
                "Trío:           30 x 3\n" +
                "Doble Par:      20 x 2\n" +
                "Par:            10 x 2\n" +
                "Carta Alta:     5 x 1";
        JOptionPane.showMessageDialog(vista, info, "Manos de Poker", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onManoJugada(String nombreMano, int fichasTotales, int mult, int totalMano, int totalAcumulado,
            List<Carta> cartasPuntuan) {

        // 1. CALCULAR LA BASE INICIAL
        // (Total Fichas 18 - Suma Cartas 8 = Base 10)
        int sumaValorCartas = 0;
        for (Carta c : cartasPuntuan) {
            sumaValorCartas += c.getValor().getValorNumerico();
        }
        int fichasBaseInicio = fichasTotales - sumaValorCartas;

        // 2. CONFIGURAR PANELES (CORRECCIÓN DE UBICACIÓN)

        // A) Mandamos el nombre y la BASE al panel IZQUIERDO (Estadísticas)
        // Esto hace que la caja azul baje visualmente a 10 inmediatamente.
        vista.getPanelEstadisticas().actualizarPrediccion(nombreMano, fichasBaseInicio, mult);
        vista.getPanelEstadisticas().prepararAnimacion(fichasBaseInicio, mult);

        // B) Borramos cualquier texto del panel CENTRAL (Mesa)
        // Para asegurarnos de que no salga "PAR" en medio de las cartas
        vista.getPanelMesa().mostrarNombreMano("");

        // 3. INICIAR SECUENCIA (SUMA EN TIEMPO REAL)
        Consumer<Integer> accionAlPuntuar = (valorCarta) -> {
            // Cada vez que una carta hace POP, sumamos su valor a la caja azul
            // 10... +4 ... = 14 ... +4 ... = 18
            vista.getPanelEstadisticas().sumarFichasInstantaneo(valorCarta);
        };

        // 4. FINALIZAR (MULTIPLICACIÓN)
        Runnable accionAlFinalizar = () -> {
            // Pasamos los valores finales reales para la multiplicación
            vista.getPanelEstadisticas().finalizarAnimacion(fichasTotales, mult, totalMano, totalAcumulado, () -> {

                modelo.finalizarTurno();
                vista.getPanelMano().getBtnJugar().setEnabled(true);

                if (modelo.getPuntajeAcumulado() >= modelo.getPuntajeObjetivo()) {
                    modelo.cobrarRecompensa();
                    JOptionPane.showMessageDialog(vista, "¡RONDA SUPERADA!");
                    abrirTienda();
                } else if (modelo.getManosRestantes() <= 0) {
                    onFinalizarJuego(false, "Te quedaste sin manos.");
                }
            });
        };

        // Ejecutar animación
        vista.getPanelMesa().reproducirSecuenciaPuntaje(cartasPuntuan, accionAlPuntuar, accionAlFinalizar);
    }

    @Override
    public void onCambioEstadisticas(int puntaje, int objetivo, int manos, int descartes, int dinero, int ronda) {
        // SOLO actualizamos la vista. NO mostramos mensajes aquí.
        vista.actualizarEstadisticas(puntaje, objetivo, manos, descartes, dinero, ronda);
    }

    @Override
    public void onCambioCartas(List<Carta> mano, List<Carta> mesa) {
        // Aquí pasamos "this::gestionarClickCarta" como la acción a ejecutar
        // Esto crea un enlace directo entre el click en la vista y el método en el
        // controlador
        vista.actualizarManoJugador(mano, this::gestionarClickCarta);

        vista.actualizarMesa(mesa);

        // ELIMINA ESTA LÍNEA: reasignarEventosCartas();
    }

    @Override
    public void onFinalizarJuego(boolean victoria, String mensaje) {
        String titulo = victoria ? "¡Victoria!" : "Fin del Juego";
        int tipo = victoria ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;

        JOptionPane.showMessageDialog(vista, mensaje, titulo, tipo);

        // Si pierde, usa la variable que guardamos en el constructor para volver al
        // menú
        if (!victoria && accionVolverMenu != null) {
            accionVolverMenu.run();
        }
    }

    @Override
    public void onPrediccionPuntaje(String nombreMano, int fichas, int mult) {
        // Llamamos al nuevo método de la vista que acepta el nombre
        vista.actualizarPrediccion(nombreMano, fichas, mult);
    }

    @Override
    public void onCambioTienda(List<ItemTienda> items) {
        vista.actualizarTienda(items);
    }

    @Override
    public void onCambioJokers(List<Joker> jokers) {
        vista.actualizarJokers(jokers);
    }

    private void verificarEstadoJuego() {
        if (modelo.getPuntajeAcumulado() >= modelo.getPuntajeObjetivo()) {
            JOptionPane.showMessageDialog(vista, "¡RONDA SUPERADA!");
            abrirTienda();
        } else if (modelo.getManosRestantes() == 0) {
            onFinalizarJuego(false, "Te quedaste sin manos.");
        }
    }

    // --- TIENDA Y ADMIN ---

    private void abrirTienda() {
        modelo.rerollTienda();
        vista.mostrarTienda();
    }

    public void cerrarTienda() {
        vista.mostrarMesa();

        // CAMBIO AQUÍ:
        // Antes: modelo.iniciarNuevaRonda();
        // Ahora: modelo.prepararSiguienteRonda(); (Sube dificultad y ronda)
        modelo.prepararSiguienteRonda();
    }

    public boolean gastarDinero(int cantidad) {
        return modelo.gastarDinero(cantidad);
    }

    public void intentarComprar(int idx) {
        modelo.comprarItem(idx);
    }

    public void solicitarReroll() {
        modelo.intentarReroll();
    }

    public int getDinero() {
        return modelo.getDinero();
    }

    public void adminGanarRonda() {
        modelo.agregarDinero(1000);
    }

    public void adminIrTienda() {
        abrirTienda();
    }

    public void adminDarDinero(int c) {
        modelo.agregarDinero(c);
    }

    public void adminResetManos() {
        modelo.iniciarNuevaRonda();
    }

    public void adminPerder() {
        /* ... */ }

    public void adminDarMult(int cantidad) {

        modelo.agregarJoker("Bufón");

        // Feedback visual
        javax.swing.JOptionPane.showMessageDialog(vista, "CHEAT: ¡Joker de Multiplicador agregado!");
    }
}