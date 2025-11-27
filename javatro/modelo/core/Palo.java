package javatro.modelo.core;

public enum Palo {
    CORAZON,
    DIAMANTE,
    TREBOL,
    PICA;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}