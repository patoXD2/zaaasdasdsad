package javatro.logica.puntaje;

import java.util.List;
import javatro.modelo.core.Carta;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;

public interface EstrategiaPuntaje {
    ResultadoMano calcularPuntaje(List<Carta> cartasJugadas, List<Carta> manoRestante);
}