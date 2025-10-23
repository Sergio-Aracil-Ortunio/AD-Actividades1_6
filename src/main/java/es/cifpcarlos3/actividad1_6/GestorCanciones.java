package es.cifpcarlos3.actividad1_6;

import es.cifpcarlos3.actividad1_6.vo.Cancion;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;


public class GestorCanciones {
    public static void main(String[] args) throws IOException {
        Path dir_base = Path.of("src", "main", "java", "es", "cifpcarlos3", "actividad1_6");
        Path txt = dir_base.resolve("canciones.txt");
        Path json = dir_base.resolve("canciones.json");

        List<Cancion> canciones = new ArrayList<>();

        int lineasLeidas = 0;
        int lineasValidas = 0;
        int lineasIgnoradas = 0;

        try (var br = Files.newBufferedReader(txt, StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineasLeidas++;
                String[] datos = linea.split(",", -1);

                if (datos.length != 5) {
                    System.out.println("Línea " + lineasLeidas + " ignorada: número de campos incorrecto");
                    lineasIgnoradas++;
                    continue;
                }

                try {
                    int anio = Integer.parseInt(datos[0].trim());
                    String titulo = datos[1].trim();
                    String autor = datos[2].trim();
                    String duracion = datos[3].trim();
                    String esEspanolaStr = datos[4].trim().toLowerCase();

                    if (!duracion.matches("\\d{1,2}:\\d{2}")) {
                        System.out.println("Línea " + lineasLeidas + " ignorada: formato de duración inválido (" + duracion + ")");
                        lineasIgnoradas++;
                        continue;
                    }

                    if (!esEspanolaStr.equals("true") && !esEspanolaStr.equals("false")) {
                        System.out.println("Línea " + lineasLeidas + " ignorada: valor boolean inválido (" + esEspanolaStr + ")");
                        lineasIgnoradas++;
                        continue;
                    }

                    boolean esEspanola = Boolean.parseBoolean(esEspanolaStr);
                    canciones.add(new Cancion(anio, titulo, autor, duracion, esEspanola));
                    lineasValidas++;
                } catch (NumberFormatException e) {
                    System.out.println("Línea " + lineasLeidas + " ignorada: año no numérico");
                    lineasIgnoradas++;
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            return;
        }

        try (var writer = Files.newBufferedWriter(json, StandardCharsets.UTF_8)) {
            var mapper = JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).enable(SerializationFeature.WRAP_ROOT_VALUE).build();
            mapper.writeValue(writer, canciones);
        }

        System.out.println("Leídas: " + lineasLeidas + " | Válidas: " + lineasValidas + " | Ignoradas: " + lineasIgnoradas);
        System.out.println("JSON generado en: " + json.toAbsolutePath());
    }
}
