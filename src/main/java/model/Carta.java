package model;

public class Carta {

    private final String palo;
    private final int valor;
    private final String nombreArchivoImagen;

    public Carta(String palo, int valor, String nombreArchivoImagen) {
        this.palo = palo;
        this.valor = valor;
        this.nombreArchivoImagen = nombreArchivoImagen;
    }

    public String getPalo() { return palo; }
    public int obtenerPuntuacion() { return valor; }
    public String getNombreArchivoImagen() { return nombreArchivoImagen; }
}
