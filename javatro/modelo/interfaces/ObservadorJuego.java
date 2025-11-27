package javatro.modelo.interfaces;

import java.util.List;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Joker;
import javatro.modelo.tienda.ItemTienda;

public interface ObservadorJuego {
    void onCambioEstadisticas(int puntaje, int objetivo, int manos, int descartes, int dinero, int ronda);

    void onCambioCartas(List<Carta> mano, List<Carta> mesa);

    void onFinalizarJuego(boolean victoria, String mensaje);

    void onPrediccionPuntaje(String nombreMano, int fichas, int mult);

    void onCambioTienda(List<ItemTienda> items);

    void onManoJugada(String nombreMano, int fichas, int mult, int totalMano, int totalAcumulado,
            List<Carta> cartasPuntuan);

    void onCambioJokers(List<Joker> jokers);
}