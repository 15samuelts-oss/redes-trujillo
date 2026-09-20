package Arquitectura.src;

import java.util.List;

/**
 * Utilidad artesanal para transformar objetos POJO a JSON.
 * Demuestra algoritmia pura sin dependencias de frameworks externos.
 */
public class SerializadorJson {

    public static String estacionesAJson(List<Estacion> estaciones) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        
        for (int i = 0; i < estaciones.size(); i++) {
            Estacion est = estaciones.get(i);
            json.append("  {\n");
            json.append("    \"id\": \"").append(est.getIdEstacion()).append("\",\n");
            json.append("    \"ubicacion\": \"").append(est.getNombreUbicacion()).append("\",\n");
            json.append("    \"puntosDeCarga\": [\n");
            
            List<PuntoCarga> puntos = est.getPuntosDeCarga();
            for (int j = 0; j < puntos.size(); j++) {
                PuntoCarga pc = puntos.get(j);
                json.append("      {\n");
                json.append("        \"id\": \"").append(pc.getId()).append("\",\n");
                json.append("        \"potenciaKw\": ").append(pc.getPotenciaMaximaKw()).append(",\n");
                json.append("        \"estado\": \"").append(pc.getEstado().name()).append("\"\n");
                json.append("      }");
                if (j < puntos.size() - 1) json.append(",");
                json.append("\n");
            }
            json.append("    ]\n");
            json.append("  }");
            if (i < estaciones.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]");
        return json.toString();
    }
}