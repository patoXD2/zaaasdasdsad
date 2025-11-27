package javatro.modelo.core;

public class Carta {
    private final Palo palo;
    private final Valor valor;
    private boolean seleccionada;

    public Carta(Palo palo, Valor valor) {
        this.palo = palo;
        this.valor = valor;
        this.seleccionada = false;
    }

    public Palo getPalo() { return palo; }
    public Valor getValor() { return valor; }

    public boolean isSeleccionada() { return seleccionada; }
    public void toggleSeleccion() { this.seleccionada = !this.seleccionada; }
    public void setSeleccionada(boolean s) { this.seleccionada = s; }

    public String getRutaImagen() {
        return palo.toString().toLowerCase() + "_" + valor.name().toLowerCase() + ".png";
    }

    @Override
    public String toString() {
        return valor + " de " + palo;
    }
}