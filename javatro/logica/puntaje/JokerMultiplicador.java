package javatro.logica.puntaje;

import java.util.List;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;
import javatro.modelo.core.Carta;

public class JokerMultiplicador extends JokerDecorator {
    private int valorAgregado;

    public JokerMultiplicador(EstrategiaPuntaje estrategia, int valor) {
        super(estrategia);
        this.valorAgregado = valor;
    }

    @Override
    public ResultadoMano calcularPuntaje(List<Carta> cartasJugadas, List<Carta> manoRestante) {
        ResultadoMano res = super.calcularPuntaje(cartasJugadas, manoRestante);

        res.multiplicadorBase += valorAgregado;
        res.nombreMano += " + JOKER";

        return res;
    }
}