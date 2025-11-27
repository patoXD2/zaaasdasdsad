package javatro.app;

import java.net.URL;

public class TestResources {
    public static void main(String[] args) {
        System.out.println("Testing resource loading...");

        String[] paths = {
                "/javatro/recursos/imagenes/titulo_balatro.png",
                "/javatro/recursos/audio/musica1.wav",
                "javatro/recursos/imagenes/titulo_balatro.png" // Relative check
        };

        for (String path : paths) {
            URL url = TestResources.class.getResource(path);
            System.out.println("Path: " + path + " -> " + (url != null ? "FOUND: " + url : "NOT FOUND"));
        }

        System.out.println("Classpath: " + System.getProperty("java.class.path"));
    }
}
