package javatro.logica.puntaje;

import java.util.List;
import javatro.modelo.core.Carta;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;

public abstract class JokerDecorator implements EstrategiaPuntaje {

    protected EstrategiaPuntaje estrategiaEnvuelt;

    public JokerDecorator(EstrategiaPuntaje estrategia) {
        this.estrategiaEnvuelt = estrategia;
    }

    @Override
    public ResultadoMano calcularPuntaje(List<Carta> cartasJugadas, List<Carta> manoRestante) {
        return estrategiaEnvuelt.calcularPuntaje(cartasJugadas, manoRestante);
    }
}