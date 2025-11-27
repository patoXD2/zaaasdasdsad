package javatro.modelo.tienda;

public class InfoJoker {
    private String nombre;
    private String descripcion;
    private String rutaImagen;
    private boolean descubierto;

    public InfoJoker(String nombre, String descripcion, String rutaImagen, boolean descubierto) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.descubierto = descubierto;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getRutaImagen() { return rutaImagen; }
    public boolean isDescubierto() { return descubierto; }

    public void descubrir() { this.descubierto = true; }
}