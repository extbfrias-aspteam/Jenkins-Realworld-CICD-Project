package com.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Aplicación de prueba para demostrar el pipeline Jenkins + Maven.
 * Simula una app real con lógica de negocio básica y configuración por entorno.
 */
public class App {

    private final String environment;
    private final String buildNumber;

    public App(String environment, String buildNumber) {
        this.environment = environment;
        this.buildNumber = buildNumber;
    }

    public String getStatus() {
        return "OK";
    }

    public String getBanner() {
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return String.join("\n",
            "╔══════════════════════════════════════════╗",
            "║        Test Application v1.0             ║",
            "║   Jenkins + Maven CI/CD Demo             ║",
            "╚══════════════════════════════════════════╝",
            "",
            "  Status      : " + getStatus(),
            "  Environment : " + environment,
            "  Build       : #" + buildNumber,
            "  Timestamp   : " + timestamp,
            "  Java        : " + System.getProperty("java.version"),
            ""
        );
    }

    public static void main(String[] args) {
        String env   = System.getProperty("environment", "local");
        String build = System.getProperty("buildNumber",  "dev");

        App app = new App(env, build);
        System.out.println(app.getBanner());

        // Salida 0 = éxito (usado para verificación en el pipeline)
        System.exit(0);
    }
}
