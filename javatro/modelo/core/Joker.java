package javatro.modelo.core;

// representa un joker con propiedades y efectos
public class Joker {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final TipoJoker tipo;
    private final int valor;
    private final String rareza;
    private final String urlImagen;
    private final int probabilidadDestruccion; // 1 en X (ej: 6 = 1/6). 0 = indestructible.

    public Joker(String id, String nombre, String descripcion, TipoJoker tipo, int valor, String rareza,
            String urlImagen, int probabilidadDestruccion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.valor = valor;
        this.rareza = rareza;
        this.urlImagen = urlImagen;
        this.probabilidadDestruccion = probabilidadDestruccion;
    }

    // Constructor compatible (sin probabilidad)
    public Joker(String id, String nombre, String descripcion, TipoJoker tipo, int valor, String rareza,
            String urlImagen) {
        this(id, nombre, descripcion, tipo, valor, rareza, urlImagen, 0);
    }

    // getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public TipoJoker getTipo() {
        return tipo;
    }

    public int getValor() {
        return valor;
    }

    public String getRareza() {
        return rareza;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public int getProbabilidadDestruccion() {
        return probabilidadDestruccion;
    }

    public boolean debeDestruirse() {
        if (probabilidadDestruccion <= 0)
            return false;
        return new java.util.Random().nextInt(probabilidadDestruccion) == 0;
    }

    // color segun rareza
    public java.awt.Color getColorRareza() {
        switch (rareza) {
            case "Legendario":
                return new java.awt.Color(255, 215, 0);
            case "Raro":
                return new java.awt.Color(138, 43, 226);
            case "Poco común":
                return new java.awt.Color(30, 144, 255);
            default:
                return new java.awt.Color(192, 192, 192);
        }
    }

    @Override
    public String toString() {
        return String.format("%s (+%d %s) [%s]", nombre, valor, tipo, rareza);
    }
}
