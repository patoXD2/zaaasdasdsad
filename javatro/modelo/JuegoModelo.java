package javatro.modelo;

import java.util.ArrayList;
import java.util.List;

import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;
import javatro.logica.puntaje.EstrategiaPuntaje;
import javatro.logica.puntaje.EvaluacionEstandar;
import javatro.logica.puntaje.JokerBaron;
import javatro.logica.puntaje.JokerFibonacci;
import javatro.logica.puntaje.JokerMonologuista;
import javatro.logica.puntaje.JokerMultiplicador;
import javatro.logica.puntaje.JokerMultiplicadorX;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Joker;
import javatro.modelo.core.Mazo;
import javatro.modelo.interfaces.ObservadorJuego;
import javatro.modelo.tienda.ItemTienda;

public class JuegoModelo {

    private Mazo mazo;
    private List<Carta> manoJugador;
    private List<Carta> cartasEnMesa;

    private int rondaActual;
    private int puntajeAcumulado;
    private int puntajeObjetivo;
    private int manosRestantes;
    private int descartesRestantes;
    private int dinero;

    private EstrategiaPuntaje estrategiaPuntaje;
    private GestorJokers gestorJokers;

    private List<ObservadorJuego> observadores;
    private List<ItemTienda> itemsTiendaDisponibles;

    public JuegoModelo() {
        this.mazo = new Mazo();
        this.manoJugador = new ArrayList<>();
        this.cartasEnMesa = new ArrayList<>();
        this.observadores = new ArrayList<>();
        this.itemsTiendaDisponibles = new ArrayList<>();
        this.gestorJokers = new GestorJokers();
        this.estrategiaPuntaje = new EvaluacionEstandar();

        reiniciarJuegoTotal();
    }

    public void agregarObservador(ObservadorJuego obs) {
        observadores.add(obs);
        notificarTodo();
    }

    public void reiniciarJuegoTotal() {
        rondaActual = 1;
        dinero = 0;
        puntajeObjetivo = 300;
        gestorJokers.limpiar();
        reconstruirEstrategia();
        iniciarNuevaRonda();
    }

    public void iniciarNuevaRonda() {
        puntajeAcumulado = 0;
        manosRestantes = 4;
        descartesRestantes = 3;
        cartasEnMesa.clear();

        mazo.inicializar();
        mazo.barajar();
        manoJugador.clear();
        robarHastaLlenar();

        notificarTodo();
    }

    public void robarHastaLlenar() {
        while (manoJugador.size() < 8 && mazo.getCartasRestantes() > 0) {
            Carta c = mazo.robar();
            if (c != null)
                manoJugador.add(c);
        }
        notificarCartas();
    }

    public void jugarMano(List<Carta> seleccionadas) {
        if (manosRestantes <= 0)
            return;

        manoJugador.removeAll(seleccionadas);
        cartasEnMesa = new ArrayList<>(seleccionadas);

        // 1. Evaluamos pasando la mano restante (para Baron, etc)
        ResultadoMano res = estrategiaPuntaje.calcularPuntaje(seleccionadas, new ArrayList<>(manoJugador));

        // 2. Sumamos SOLO las cartas que puntúan
        int sumaFichasCartas = 0;
        List<Carta> cartasActivas = (res.cartasQuePuntuan != null) ? res.cartasQuePuntuan : seleccionadas;

        for (Carta c : cartasActivas) {
            sumaFichasCartas += c.getValor().getValorNumerico();
        }

        int fichasTotalesMano = res.fichasBase + sumaFichasCartas;
        int puntosMano = fichasTotalesMano * res.multiplicadorBase;

        puntajeAcumulado += puntosMano;
        manosRestantes--;

        notificarCartas();
        notificarJugadaAnimada(res.nombreMano, fichasTotalesMano, res.multiplicadorBase, puntosMano, puntajeAcumulado,
                cartasActivas);
    }

    private void notificarJugadaAnimada(String nombre, int fichas, int mult, int puntos, int total,
            List<Carta> activas) {
        for (ObservadorJuego obs : observadores) {
            obs.onManoJugada(nombre, fichas, mult, puntos, total, activas);
        }
    }

    public void descartar(List<Carta> seleccionadas) {
        if (descartesRestantes <= 0)
            return;

        manoJugador.removeAll(seleccionadas);
        descartesRestantes--;
        robarHastaLlenar();
        notificarEstadisticas();
    }

    public void calcularPrediccion(List<Carta> seleccionadas) {
        if (seleccionadas.isEmpty()) {
            notificarPrediccion(" ", 0, 0);
            return;
        }

        // Evaluamos con mano restante simulada
        List<Carta> manoSimulada = new ArrayList<>(manoJugador);
        manoSimulada.removeAll(seleccionadas);
        ResultadoMano res = estrategiaPuntaje.calcularPuntaje(seleccionadas, manoSimulada);

        notificarPrediccion(res.nombreMano, res.fichasBase, res.multiplicadorBase);
    }

    public void finalizarTurno() {
        cartasEnMesa.clear();
        robarHastaLlenar();
        notificarEstadisticas();
    }

    private void notificarPrediccion(String nombre, int fichas, int mult) {
        for (ObservadorJuego obs : observadores) {
            obs.onPrediccionPuntaje(nombre, fichas, mult);
        }
    }

    private void notificarTodo() {
        notificarEstadisticas();
        notificarCartas();
        notificarJokers();
        notificarTienda();
    }

    private void notificarEstadisticas() {
        for (ObservadorJuego obs : observadores) {
            obs.onCambioEstadisticas(puntajeAcumulado, puntajeObjetivo, manosRestantes, descartesRestantes, dinero,
                    rondaActual);
        }
    }

    private void notificarCartas() {
        List<Carta> manoCopia = new ArrayList<>(manoJugador);
        List<Carta> mesaCopia = new ArrayList<>(cartasEnMesa);
        for (ObservadorJuego obs : observadores) {
            obs.onCambioCartas(manoCopia, mesaCopia);
        }
    }

    private void notificarJokers() {
        List<Joker> jokersCopia = new ArrayList<>(gestorJokers.getJokersActivos());
        for (ObservadorJuego obs : observadores) {
            obs.onCambioJokers(jokersCopia);
        }
    }

    private void notificarTienda() {
        List<ItemTienda> copia = new ArrayList<>(itemsTiendaDisponibles);
        for (ObservadorJuego obs : observadores) {
            obs.onCambioTienda(copia);
        }
    }

    public void cobrarRecompensa() {
        int recompensaBase = 5;
        int bonoManos = manosRestantes;
        int totalGanado = recompensaBase + bonoManos;

        this.dinero += totalGanado;
        notificarEstadisticas();
    }

    public void prepararSiguienteRonda() {
        rondaActual++;
        puntajeObjetivo = (int) (puntajeObjetivo * 1.5);

        // Verificar destrucción de jokers
        List<String> destruidos = gestorJokers.verificarDestruccion();
        if (!destruidos.isEmpty()) {
            reconstruirEstrategia();
            // Podríamos notificar qué se rompió
            System.out.println("Jokers destruidos: " + destruidos);
        }

        iniciarNuevaRonda();
    }

    // Getters
    public int getManosRestantes() {
        return manosRestantes;
    }

    public int getDescartesRestantes() {
        return descartesRestantes;
    }

    public List<Carta> getManoJugador() {
        return manoJugador;
    }

    public int getPuntajeAcumulado() {
        return puntajeAcumulado;
    }

    public int getPuntajeObjetivo() {
        return puntajeObjetivo;
    }

    public int getDinero() {
        return dinero;
    }

    public GestorJokers getGestorJokers() {
        return gestorJokers;
    }

    public void agregarDinero(int cantidad) {
        this.dinero += cantidad;
        notificarEstadisticas();
    }

    public boolean gastarDinero(int cantidad) {
        if (dinero >= cantidad) {
            dinero -= cantidad;
            notificarEstadisticas();
            return true;
        }
        return false;
    }

    // Gestión de Jokers
    public void agregarJoker(String nombreItem) {
        // Buscar en el catálogo por nombre (esto es un poco frágil, mejor usar ID)
        // Pero la tienda usa ItemTienda que tiene nombre.
        // Mapeamos nombre a ID o buscamos en el catálogo.
        Joker joker = CatalogoJokers.CATALOGO.stream()
                .filter(j -> j.getNombre().equals(nombreItem))
                .findFirst()
                .orElse(null);

        if (joker != null) {
            if (gestorJokers.agregarJoker(joker)) {
                reconstruirEstrategia();
                notificarEstadisticas(); // Forzar update UI
            }
        }
    }

    private void reconstruirEstrategia() {
        this.estrategiaPuntaje = new EvaluacionEstandar();
        for (Joker j : gestorJokers.getJokersActivos()) {
            switch (j.getTipo()) {
                case MULT:
                case DESTRUCTIBLE_MULT:
                    this.estrategiaPuntaje = new JokerMultiplicador(this.estrategiaPuntaje, j.getValor());
                    break;
                case MULTIPLICADOR_CONDICIONAL:
                    if (j.getId().equals("j_baron")) {
                        this.estrategiaPuntaje = new JokerBaron(this.estrategiaPuntaje);
                    }
                    break;
                case MULT_CONDICIONAL_CARTAS:
                    if (j.getId().equals("j_fibonacci")) {
                        this.estrategiaPuntaje = new JokerFibonacci(this.estrategiaPuntaje);
                    }
                    break;
                case MULT_CONDICIONAL_MANO:
                    if (j.getId().equals("j_monologuista")) {
                        this.estrategiaPuntaje = new JokerMonologuista(this.estrategiaPuntaje);
                    }
                    break;
                case DESTRUCTIBLE_MULTIPLICADOR:
                    this.estrategiaPuntaje = new JokerMultiplicadorX(this.estrategiaPuntaje, j.getValor());
                    break;
                default:
                    break;
            }
        }
    }

    public void rerollTienda() {
        itemsTiendaDisponibles.clear();
        List<Joker> nuevos = CatalogoJokers.obtenerAleatorios(3);
        for (Joker j : nuevos) {
            itemsTiendaDisponibles
                    .add(new ItemTienda(j.getNombre(), j.getDescripcion(), 5, "jokers/" + j.getUrlImagen(), j.getId()));
        }
        // Siempre añadimos un tarot o carta extra si queremos, o solo jokers
        // itemsTiendaDisponibles.add(new ItemTienda("La Torre", "Mejorar carta", 4,
        // "tarot_torre.png", "c_tarot"));

        notificarTienda();
    }

    public boolean intentarReroll() {
        int costoReroll = 5;
        if (gastarDinero(costoReroll)) {
            rerollTienda();
            return true;
        }
        return false;
    }

    public boolean comprarItem(int indice) {
        if (indice < 0 || indice >= itemsTiendaDisponibles.size())
            return false;
        ItemTienda item = itemsTiendaDisponibles.get(indice);

        if (gastarDinero(item.getPrecio())) {
            agregarJoker(item.getNombre());
            itemsTiendaDisponibles.remove(indice);
            notificarTienda();
            return true;
        }
        return false;
    }

    public void ordenarManoPorValor() {
        manoJugador.sort((c1, c2) -> Integer.compare(c2.getValor().ordinal(), c1.getValor().ordinal()));
        notificarCartas();
    }

    public void ordenarManoPorPalo() {
        manoJugador.sort((c1, c2) -> {
            int cmp = c1.getPalo().compareTo(c2.getPalo());
            if (cmp == 0)
                return Integer.compare(c2.getValor().ordinal(), c1.getValor().ordinal());
            return cmp;
        });
        notificarCartas();
    }
}