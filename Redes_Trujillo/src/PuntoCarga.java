package Redes_Trujillo.src;

public class PuntoCarga {
    private final String id;
    private final double potenciaMaximaKw;
    private EstadoPuntoCarga estado;

    public PuntoCarga(String id, double potenciaMaximaKw) {
        this.id = id;
        this.potenciaMaximaKw = potenciaMaximaKw;
        this.estado = EstadoPuntoCarga.DISPONIBLE; // Estado por defecto
    }

    public String getId() { return id; }
    public double getPotenciaMaximaKw() { return potenciaMaximaKw; }
    public EstadoPuntoCarga getEstado() { return estado; }
    
    public void setEstado(EstadoPuntoCarga estado) {
        if (estado == null) throw new IllegalArgumentException("El estado no puede ser nulo.");
        this.estado = estado;
    }

    public boolean estaDisponible() {
        return this.estado == EstadoPuntoCarga.DISPONIBLE;
    }
}
