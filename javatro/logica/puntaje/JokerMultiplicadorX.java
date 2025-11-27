package javatro.logica.puntaje;

import java.util.List;
import javatro.modelo.core.Carta;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;

public class JokerMultiplicadorX extends JokerDecorator {
    private double multiplicador;

    public JokerMultiplicadorX(EstrategiaPuntaje estrategia, double multiplicador) {
        super(estrategia);
        this.multiplicador = multiplicador;
    }

    @Override
    public ResultadoMano calcularPuntaje(List<Carta> cartasJugadas, List<Carta> manoRestante) {
        ResultadoMano res = super.calcularPuntaje(cartasJugadas, manoRestante);

        res.multiplicadorBase = (int) (res.multiplicadorBase * multiplicador);
        res.nombreMano += " x" + multiplicador;

        return res;
    }
}
