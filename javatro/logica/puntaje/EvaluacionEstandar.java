package javatro.logica.puntaje;

import java.util.List;

import javatro.logica.evaluador.EvaluadorMano;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;
import javatro.modelo.core.Carta;

public class EvaluacionEstandar implements EstrategiaPuntaje {
    @Override
    public ResultadoMano calcularPuntaje(List<Carta> cartasJugadas, List<Carta> manoRestante) {
        return EvaluadorMano.evaluar(cartasJugadas);
    }
}