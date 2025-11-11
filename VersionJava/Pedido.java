public class Pedido {
    private final int id;
    private final String nombre;

    public Pedido(int id) {
        this.id = id;
        this.nombre = "Pedido-" + id;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
