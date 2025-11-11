import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cocina {
    public static void main(String[] args) {
        Random rand = new Random();

        // Número aleatorio de cocineros (3-5) y pedidos (6-10)
        int numCocineros = 3 + rand.nextInt(3); // 3,4,5
        int numPedidos = 6 + rand.nextInt(5);   // 6-10

        System.out.printf("Iniciando cocina con %d cocineros y %d pedidos.%n%n", numCocineros, numPedidos);

        // Crear lista de pedidos
        List<Pedido> pedidos = new ArrayList<>();
        for (int i = 1; i <= numPedidos; i++) {
            pedidos.add(new Pedido(i));
        }

        // Crear carpeta logs si no existe
        File logDir = new File("VersionJava/logs");
        if (!logDir.exists()) logDir.mkdirs();
        File logFile = new File(logDir, "log_pedidos.txt");

        // Limpiar log al inicio
        try {
            if (logFile.exists()) logFile.delete();
            logFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Object lock = new Object(); // Lock para sincronización

        // Crear cocineros
        List<Cocinero> cocineros = new ArrayList<>();
        for (int i = 1; i <= numCocineros; i++) {
            cocineros.add(new Cocinero("Cocinero-" + i, pedidos, lock, logFile));
        }

        // Iniciar hilos
        for (Cocinero c : cocineros) {
            c.start();
        }

        // Esperar a que terminen
        for (Cocinero c : cocineros) {
            try {
                c.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nTodos los pedidos han sido procesados.");
    }
}
