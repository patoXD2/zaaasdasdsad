package javatro.modelo.core;

import java.util.Collections;
import java.util.Stack;

public class Mazo {
    private Stack<Carta> cartas;

    public Mazo() {
        cartas = new Stack<>();
        inicializar();
    }

    public void inicializar() {
        cartas.clear();
        for (Palo p : Palo.values()) {
            for (Valor v : Valor.values()) {
                cartas.push(new Carta(p, v));
            }
        }
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public Carta robar() {
        if (!cartas.isEmpty()) {
            return cartas.pop();
        }
        return null;
    }

    public int getCartasRestantes() {
        return cartas.size();
    }
}