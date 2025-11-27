package javatro.util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class GestorAudio {

    private static GestorAudio instancia;
    private Clip clipMusica;
    private float volumenGeneral = 0.0f;

    private GestorAudio() {
    }

    public static GestorAudio getInstancia() {
        if (instancia == null) {
            instancia = new GestorAudio();
        }
        return instancia;
    }

    public void setVolumen(float valor0a1) {
        this.volumenGeneral = Math.max(0f, Math.min(1f, valor0a1));
        if (clipMusica != null && clipMusica.isOpen()) {
            actualizarGanancia(clipMusica);
        }
    }

    public float getVolumen() {
        return volumenGeneral;
    }

    private void actualizarGanancia(Clip clip) {
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (volumenGeneral < 0.01f) {
                gainControl.setValue(-80.0f);
            } else {
                float dB = (float) (Math.log10(volumenGeneral) * 20.0);
                gainControl.setValue(dB);
            }
        } catch (Exception e) {
        }
    }

    public void reproducirEfecto(String nombreArchivo) {
        try {
            Clip clip = cargarClip(nombreArchivo);
            if (clip != null) {
                actualizarGanancia(clip);
                clip.start();
            }
        } catch (Exception e) {
            System.err.println("Error efecto: " + e.getMessage());
        }
    }

    public void reproducirMusica(String nombreArchivo) {
        try {
            detenerMusica();
            clipMusica = cargarClip(nombreArchivo);
            if (clipMusica != null) {
                actualizarGanancia(clipMusica);
                clipMusica.loop(Clip.LOOP_CONTINUOUSLY);
                clipMusica.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detenerMusica() {
        if (clipMusica != null && clipMusica.isRunning()) {
            clipMusica.stop();
            clipMusica.close();
        }
    }

    private Clip cargarClip(String nombreArchivo)
            throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        String[] rutasPosibles = {
                "/javatro/recursos/audio/" + nombreArchivo,
                "javatro/recursos/audio/" + nombreArchivo,
                "/recursos/audio/" + nombreArchivo
        };

        URL url = null;
        for (String ruta : rutasPosibles) {
            url = getClass().getResource(ruta);
            if (url == null)
                url = getClass().getClassLoader().getResource(ruta);
            if (url != null) {
                System.out.println("Audio cargado: " + ruta);
                break;
            }
        }

        if (url == null) {
            System.err.println("ERROR AUDIO: No se encontró el archivo " + nombreArchivo);
            return null;
        }

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        return clip;
    }
}