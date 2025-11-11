import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Cocinero extends Thread {
    private final List<Pedido> pedidos;
    private final Object lock;
    private final File logFile;

    public Cocinero(String nombre, List<Pedido> pedidos, Object lock, File logFile) {
        super(nombre);
        this.pedidos = pedidos;
        this.lock = lock;
        this.logFile = logFile;
    }

    @Override
    public void run() {
        while (true) {
            Pedido pedido = null;

            synchronized (lock) {
                if (pedidos.isEmpty()) {
                    return; // No hay más pedidos
                }
                pedido = pedidos.remove(0);
            }

            // Simula preparación
            double tiempoPreparacion = 1 + Math.random() * 2; // 1 a 3 segundos
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = LocalDateTime.now().format(formatter);
            System.out.printf("[%s] %s está preparando %s... (%.1fs)%n", timestamp, getName(), pedido.getNombre(), tiempoPreparacion);

            try {
                Thread.sleep((long)(tiempoPreparacion * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Registrar en log
            synchronized (lock) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true))) {
                    String linea = String.format("[%s] %s ha preparado %s", 
                            timestamp, getName(), pedido.getNombre());
                    bw.write(linea);
                    bw.newLine();
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
