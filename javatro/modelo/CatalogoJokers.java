package javatro.modelo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javatro.modelo.core.Joker;
import javatro.modelo.core.TipoJoker;

public class CatalogoJokers {

    public static final List<Joker> CATALOGO = Arrays.asList(
            // 1. Jimbo (Básico)
            new Joker("j_basico", "Joker", "+4 Mult", TipoJoker.MULT, 4, "Común", "jimbo.png"),

            // 2. Fibonacci
            new Joker("j_fibonacci", "Fibonacci", "+8 Mult por A,2,3,5,8", TipoJoker.MULT_CONDICIONAL_CARTAS, 8,
                    "Poco común", "fibonacci.png"),

            // 3. Barón
            new Joker("j_baron", "Barón", "X1.5 por Rey en mano", TipoJoker.MULTIPLICADOR_CONDICIONAL, 0, "Raro",
                    "rey.png"), // Valor 0 porque es dinámico

            // 4. Monologuista
            new Joker("j_monologuista", "Monologuista", "+15 si Escalera", TipoJoker.MULT_CONDICIONAL_MANO, 15,
                    "Poco común", "comediante.png"),

            // 5. Gros Michel
            new Joker("j_gros_michel", "Gros Michel", "+15 Mult (1/6 fin ronda)", TipoJoker.DESTRUCTIBLE_MULT, 15,
                    "Raro", "grosmitchel.png", 6),

            // 6. Cavendish
            new Joker("j_cavendish", "Cavendish", "X3 Mult (1/1000 fin ronda)", TipoJoker.DESTRUCTIBLE_MULTIPLICADOR, 3,
                    "Legendario", "cavendish.png", 1000));

    public static List<Joker> obtenerAleatorios(int cantidad) {
        List<Joker> copia = new ArrayList<>(CATALOGO);
        Collections.shuffle(copia);
        return copia.subList(0, Math.min(cantidad, copia.size()));
    }

    public static Joker buscarPorId(String id) {
        return CATALOGO.stream()
                .filter(j -> j.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
