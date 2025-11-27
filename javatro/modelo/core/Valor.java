package javatro.modelo.core;

public enum Valor {
    DOS(2), TRES(3), CUATRO(4), CINCO(5), SEIS(6), SIETE(7), OCHO(8),
    NUEVE(9), DIEZ(10), JOTA(10), REINA(10), REY(10), AS(11);

    private final int valorNumerico;

    Valor(int valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }
}