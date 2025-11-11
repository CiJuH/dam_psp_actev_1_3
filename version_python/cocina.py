from pedido import Pedido
from cocinero import Cocinero
import threading
import random
import os

class Cocina:
    """
    Clase principal que coordina los hilos (cocineros) y la lista compartida de pedidos.
    """

    @staticmethod
    def main():
        # Crear lista de pedidos compartida
         # Número aleatorio de pedidos y cocineros
        num_pedidos = random.randint(6, 10)    # al menos 6 pedidos
        num_cocineros = random.randint(3, 5)   # al menos 3 cocineros

        # Crear lista de pedidos con nombre simple Pedido-1, Pedido-2, ...
        pedidos = [Pedido(i + 1, f"Pedido-{i + 1}") for i in range(num_pedidos)]

        print(f"Iniciando cocina con {num_cocineros} cocineros y {num_pedidos} pedidos.\n")

        # Crear carpeta logs si no existe
        base_dir = os.path.dirname(os.path.abspath(__file__))
        log_dir = os.path.join(base_dir, "logs")
        os.makedirs(log_dir, exist_ok=True)
        log_file = os.path.join(log_dir, "log_pedidos.txt")

        # Limpiar el log al inicio
        with open(log_file, "w", encoding="utf-8") as f:
            f.write("")

        lock = threading.Lock()

        # Crear cocineros
        cocineros = [Cocinero(f"Cocinero-{i+1}", pedidos, lock, log_file) for i in range(num_cocineros)]

        # Iniciar hilos
        for c in cocineros:
            c.start()

        # Esperar a que terminen todos
        for c in cocineros:
            c.join()

        print("\nTodos los pedidos han sido procesados.")

if __name__ == "__main__":
    Cocina.main()
