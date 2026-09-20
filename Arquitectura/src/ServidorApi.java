package Arquitectura.src;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Servidor HTTP de Redes Trujillo. 
 * Soporta GET (Telemetría) y POST (Mutación de estado).
 * Compatible con Java 8+.
 */
public class ServidorApi {

    private final HttpServer servidor;
    private final List<Estacion> redEstaciones;

    public ServidorApi(int puerto, List<Estacion> redEstaciones) throws IOException {
        this.redEstaciones = redEstaciones;
        this.servidor = HttpServer.create(new InetSocketAddress(puerto), 0);
        this.servidor.createContext("/api/estaciones", new ManejadorEstaciones());
        this.servidor.setExecutor(null); 
    }

    public void iniciar() {
        this.servidor.start();
    }

    private class ManejadorEstaciones implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Configuración CORS
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            // Preflight de CORS
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // Procesamiento de mutación de estado vía POST
            if ("POST".equals(exchange.getRequestMethod())) {
                InputStream is = exchange.getRequestBody();
                
                // [SOLUCIÓN - COMPATIBILIDAD JAVA 8]
                // Uso de BufferedReader para leer el InputStream línea por línea
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        sb.append(linea);
                    }
                }
                String body = sb.toString();
                
                // Parseo JSON manual
                String idPunto = extraerValorJson(body, "id");
                String nuevoEstado = extraerValorJson(body, "estado");

                boolean actualizado = actualizarEstadoPunto(idPunto, nuevoEstado);

                if (actualizado) {
                    enviarRespuestaTexto(exchange, 200, "{\"status\":\"success\"}");
                } else {
                    enviarRespuestaTexto(exchange, 404, "{\"error\":\"Punto de carga no encontrado\"}");
                }
                return;
            }

            // Procesamiento de lectura GET
            if ("GET".equals(exchange.getRequestMethod())) {
                exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
                String respuestaJson = SerializadorJson.estacionesAJson(redEstaciones);
                enviarRespuestaTexto(exchange, 200, respuestaJson);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
        }

        private boolean actualizarEstadoPunto(String id, String estadoStr) {
            try {
                EstadoPuntoCarga nuevoEstado = EstadoPuntoCarga.valueOf(estadoStr);
                for (Estacion estacion : redEstaciones) {
                    for (PuntoCarga punto : estacion.getPuntosDeCarga()) {
                        if (punto.getId().equals(id)) {
                            punto.setEstado(nuevoEstado);
                            return true;
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Estado inválido recibido: " + estadoStr);
            }
            return false;
        }

        private String extraerValorJson(String json, String clave) {
            String patronBuscado = "\"" + clave + "\":\"";
            int inicio = json.indexOf(patronBuscado);
            if (inicio == -1) return "";
            inicio += patronBuscado.length();
            int fin = json.indexOf("\"", inicio);
            return json.substring(inicio, fin);
        }

        private void enviarRespuestaTexto(HttpExchange exchange, int codigo, String texto) throws IOException {
            byte[] bytes = texto.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(codigo, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}