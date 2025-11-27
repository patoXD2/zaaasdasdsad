package javatro.logica.puntaje;

import java.util.List;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Valor;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;

public class JokerBaron extends JokerDecorator {

    public JokerBaron(EstrategiaPuntaje estrategia) {
        super(estrategia);
    }

    @Override
    public ResultadoMano calcularPuntaje(List<Carta> cartasJugadas, List<Carta> manoRestante) {
        ResultadoMano res = super.calcularPuntaje(cartasJugadas, manoRestante);

        long reyesEnMano = manoRestante.stream()
                .filter(c -> c.getValor() == Valor.REY)
                .count();

        if (reyesEnMano > 0) {
            double multiplicadorTotal = Math.pow(1.5, reyesEnMano);
            res.multiplicadorBase = (int) (res.multiplicadorBase * multiplicadorTotal);
            res.nombreMano += " + BARON";
        }

        return res;
    }
}
