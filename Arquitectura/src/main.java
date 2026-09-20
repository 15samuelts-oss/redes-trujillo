package Arquitectura.src;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Entry Point del Sistema - Redes Trujillo.
 * Inicializa los datos en memoria y arranca el Servidor HTTP.
 */
public class main {
    
    private static final Logger LOGGER = Logger.getLogger(main.class.getName());
    private static final int PUERTO_SERVIDOR = 8080;

    public static void main(String[] args) {
        LOGGER.info("Inicializando infraestructuras de Redes Trujillo...");

        try {
            List<Estacion> redElectrica = inicializarRedSimulada();
            LOGGER.info("Red electrica inicializada con " + redElectrica.size() + " estaciones.");

            ServidorApi servidor = new ServidorApi(PUERTO_SERVIDOR, redElectrica);
            servidor.iniciar();
            
            LOGGER.info("Servidor operativo. API en http://localhost:" + PUERTO_SERVIDOR + "/api/estaciones");
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Fallo crítico al iniciar el servidor HTTP.", e);
            System.exit(1);
        }
    }

    private static List<Estacion> inicializarRedSimulada() {
        List<Estacion> estaciones = new ArrayList<>();

        Estacion estacionAlfa = new Estacion("RT-001", "Sede Central - Gran Canaria");
        estacionAlfa.agregarPuntoCarga(new PuntoCarga("PC-A1", 50.0));
        estacionAlfa.agregarPuntoCarga(new PuntoCarga("PC-A2", 22.0));
        
        Estacion estacionBeta = new Estacion("RT-002", "Nodo Norte - Tenerife");
        estacionBeta.agregarPuntoCarga(new PuntoCarga("PC-B1", 150.0));
        
        estaciones.add(estacionAlfa);
        estaciones.add(estacionBeta);

        return estaciones;
    }
}