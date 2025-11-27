package javatro.logica.puntaje;

import java.util.List;
import javatro.modelo.core.Carta;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;

public class JokerMonologuista extends JokerDecorator {

    public JokerMonologuista(EstrategiaPuntaje estrategia) {
        super(estrategia);
    }

    @Override
    public ResultadoMano calcularPuntaje(List<Carta> cartasJugadas, List<Carta> manoRestante) {
        ResultadoMano res = super.calcularPuntaje(cartasJugadas, manoRestante);

        if (res.nombreMano.contains("ESCALERA")) {
            res.multiplicadorBase += 15;
            res.nombreMano += " + MONOLOGUISTA";
        }

        return res;
    }
}
