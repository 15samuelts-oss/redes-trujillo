package Redes_Trujillo.src;

/**
 * Enumerador para controlar el estado del punto de carga.
 * Evita el uso de cadenas mágicas y centraliza el flujo de estados.
 */
public enum EstadoPuntoCarga {
    DISPONIBLE,
    OCUPADO,
    MANTENIMIENTO,
    FUERA_DE_LINEA
}