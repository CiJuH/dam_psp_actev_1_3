import os
import threading
import time
import random
from datetime import datetime

class Cocinero(threading.Thread):
    """
    Cada cocinero es un hilo que procesa pedidos de una lista compartida.
    Usa un Lock para acceder de forma segura a la lista y al archivo de log.
    """

    def __init__(self, nombre: str, pedidos: list, lock: threading.Lock, log_file: str):
        super().__init__(name=nombre)
        self.pedidos = pedidos
        self.lock = lock
        self.log_file = log_file

    def run(self):
        while True:
            self.lock.acquire()
            try:
                if not self.pedidos:
                    # No hay más pedidos que procesar
                    return
                pedido = self.pedidos.pop(0)
            finally:
                self.lock.release()

            # Simula el tiempo de preparación del pedido
            tiempo_preparacion = random.uniform(1, 3)
            
            timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S.%f")[:-3]
            print(f"[{timestamp}] {self.name} está preparando {pedido.plato}... ({tiempo_preparacion:.1f}s)")
            time.sleep(tiempo_preparacion)

            # Registrar en el log (zona crítica protegida)
            with self.lock:
                timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S.%f")[:-3]
                linea = f"[{timestamp}] {self.name} ha preparado {pedido.plato} (Pedido {pedido.id})"
                with open(self.log_file, "a", encoding="utf-8") as f:
                    f.write(linea + "\n")
