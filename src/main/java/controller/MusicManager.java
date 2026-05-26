package controller;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
    private static MediaPlayer mediaPlayer;

    public static void iniciar() {
        if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) return;
        try {
            java.net.URL url = MusicManager.class.getResource("/sound/musica_fondo.wav");
            if (url == null) return;
            mediaPlayer = new MediaPlayer(new Media(url.toString()));
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.15);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error al iniciar musica: " + e.getMessage());
        }
    }

    public static void setVolumen(double porcentaje) {
        if (mediaPlayer == null) return;
        mediaPlayer.setVolume(Math.max(0, Math.min(1, porcentaje)));
    }

    public static void detener() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }
}
