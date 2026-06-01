package model;

import java.time.LocalDate;

public class JugadorRanking {

    private final String nombre;
    private final int monedasFinales;
    private final LocalDate fecha;

    public JugadorRanking(String nombre, int monedasFinales, LocalDate fecha) {
        this.nombre = nombre;
        this.monedasFinales = monedasFinales;
        this.fecha = fecha;
    }

    public String getNombre() { return nombre; }
    public int getMonedasFinales() { return monedasFinales; }
    public LocalDate getFecha() { return fecha; }
}
