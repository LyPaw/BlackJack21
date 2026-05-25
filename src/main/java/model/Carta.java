package model;

/**
 * Modelo inmutable que representa una carta de la baraja francesa.
 * Pertenece a la capa MODELO porque encapsula los datos del dominio
 * sin ninguna dependencia de tecnologias (JavaFX, BD, etc.).
 */
public class Carta {

    private final String palo;
    private final int valor;
    private final String nombreArchivoImagen;

    public Carta(String palo, int valor, String nombreArchivoImagen) {
        this.palo = palo;
        this.valor = valor;
        this.nombreArchivoImagen = nombreArchivoImagen;
    }

    public String getPalo() {
        return palo;
    }

    /** Retorna el valor del Blackjack (2-10, J=10, Q=10, K=10, A=11). */
    public int obtenerPuntuacion() {
        return valor;
    }

    public String getNombreArchivoImagen() {
        return nombreArchivoImagen;
    }
}
