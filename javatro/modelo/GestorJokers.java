package javatro.modelo;

import javatro.modelo.core.Joker;
import javatro.modelo.core.TipoJoker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// gestor de jokers del jugador maximo 5 slots
public class GestorJokers {
    private static final int MAX_JOKERS = 5;
    private final List<Joker> jokersActivos;

    public GestorJokers() {
        this.jokersActivos = new ArrayList<>();
    }

    // agregar joker a slots activos
    public boolean agregarJoker(Joker joker) {
        if (jokersActivos.size() < MAX_JOKERS) {
            jokersActivos.add(joker);
            return true;
        }
        return false;
    }

    // remover joker por id
    public boolean removerJoker(String jokerId) {
        return jokersActivos.removeIf(j -> j.getId().equals(jokerId));
    }

    // remover joker por indice
    public void removerJokerEnPosicion(int index) {
        if (index >= 0 && index < jokersActivos.size()) {
            jokersActivos.remove(index);
        }
    }

    // lista de jokers activos solo lectura
    public List<Joker> getJokersActivos() {
        return Collections.unmodifiableList(jokersActivos);
    }

    // obtener joker por indice
    public Joker getJoker(int index) {
        if (index >= 0 && index < jokersActivos.size()) {
            return jokersActivos.get(index);
        }
        return null;
    }

    // numero de jokers activos
    public int getCantidadJokers() {
        return jokersActivos.size();
    }

    // verificar si hay espacio
    public boolean hayEspacio() {
        return jokersActivos.size() < MAX_JOKERS;
    }

    // slots disponibles
    public int getSlotsDisponibles() {
        return MAX_JOKERS - jokersActivos.size();
    }

    // chips adicionales de jokers
    public int calcularChipsAdicionales() {
        return jokersActivos.stream()
                .filter(j -> j.getTipo() == TipoJoker.CHIPS)
                .mapToInt(Joker::getValor)
                .sum();
    }

    // multiplicador adicional de jokers
    public int calcularMultAdicional() {
        return jokersActivos.stream()
                .filter(j -> j.getTipo() == TipoJoker.MULT)
                .mapToInt(Joker::getValor)
                .sum();
    }

    // multiplicador total de jokers multiplicadores
    public double calcularMultiplicadorTotal() {
        double multiplicador = 1.0;
        for (Joker j : jokersActivos) {
            if (j.getTipo() == TipoJoker.MULTIPLICADOR) {
                multiplicador *= j.getValor();
            }
        }
        return multiplicador;
    }

    // verificar destrucción de jokers (retorna lista de nombres destruidos)
    public List<String> verificarDestruccion() {
        List<String> destruidos = new ArrayList<>();
        List<Joker> aEliminar = new ArrayList<>();

        for (Joker j : jokersActivos) {
            if (j.debeDestruirse()) {
                destruidos.add(j.getNombre());
                aEliminar.add(j);
            }
        }

        jokersActivos.removeAll(aEliminar);
        return destruidos;
    }

    // limpiar todos los jokers
    public void limpiar() {
        jokersActivos.clear();
    }
}
