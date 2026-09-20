package Redes_Trujillo.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estacion {
    private final String idEstacion;
    private String nombreUbicacion;
    private final List<PuntoCarga> puntosDeCarga;

    public Estacion(String idEstacion, String nombreUbicacion) {
        this.idEstacion = idEstacion;
        this.nombreUbicacion = nombreUbicacion;
        this.puntosDeCarga = new ArrayList<>();
    }

    public void agregarPuntoCarga(PuntoCarga punto) {
        if (punto == null) throw new IllegalArgumentException("El punto de carga no puede ser nulo.");
        this.puntosDeCarga.add(punto);
    }

    // Retorna una lista inmutable para proteger el encapsulamiento
    public List<PuntoCarga> getPuntosDeCarga() {
        return Collections.unmodifiableList(puntosDeCarga);
    }

    public String getIdEstacion() { return idEstacion; }
    public String getNombreUbicacion() { return nombreUbicacion; }
}