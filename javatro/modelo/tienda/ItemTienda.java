package javatro.modelo.tienda;

public class ItemTienda {
    private String nombre;
    private String descripcion;
    private int precio;
    private String rutaImagen;
    private String idEfecto; 

    public ItemTienda(String nombre, String desc, int precio, String img, String id) {
        this.nombre = nombre;
        this.descripcion = desc;
        this.precio = precio;
        this.rutaImagen = img;
        this.idEfecto = id;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getPrecio() { return precio; }
    public String getRutaImagen() { return rutaImagen; }
    public String getIdEfecto() { return idEfecto; }
}