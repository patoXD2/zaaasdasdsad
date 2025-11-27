package javatro.logica.puntaje;

import java.util.List;
import java.util.Set;
import javatro.modelo.core.Carta;
import javatro.modelo.core.Valor;
import javatro.logica.evaluador.EvaluadorMano.ResultadoMano;

public class JokerFibonacci extends JokerDecorator {
    private static final Set<Valor> VALORES_FIBONACCI = Set.of(
            Valor.AS, Valor.DOS, Valor.TRES, Valor.CINCO, Valor.OCHO);

    public JokerFibonacci(EstrategiaPuntaje estrategia) {
        super(estrategia);
    }

    @Override
    public ResultadoMano calcularPuntaje(List<Carta> cartasJugadas, List<Carta> manoRestante) {
        ResultadoMano res = super.calcularPuntaje(cartasJugadas, manoRestante);

        int cantidadFib = 0;
        // Usamos cartasQuePuntuan si existe, sino cartasJugadas
        List<Carta> activas = (res.cartasQuePuntuan != null) ? res.cartasQuePuntuan : cartasJugadas;

        for (Carta c : activas) {
            if (VALORES_FIBONACCI.contains(c.getValor())) {
                cantidadFib++;
            }
        }

        if (cantidadFib > 0) {
            res.multiplicadorBase += cantidadFib * 8;
            res.nombreMano += " + FIBONACCI";
        }

        return res;
    }
}
