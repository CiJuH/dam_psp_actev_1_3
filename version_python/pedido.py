class Pedido:
    """
    Representa un pedido en la cocina.
    Tiene un ID y el nombre del plato.
    """

    def __init__(self, id_pedido: int, nombre_plato: str):
        self.id = id_pedido
        self.plato = nombre_plato

    def __str__(self):
        return f"Pedido {self.id}: {self.plato}"
