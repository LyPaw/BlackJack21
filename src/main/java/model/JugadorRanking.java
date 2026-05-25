package model;

import java.time.LocalDate;

/**
 * Modelo puro de datos que almacena un registro del ranking historico.
 * La fecha se maneja como LocalDate en Java (tipo nativo sin zona horaria)
 * y se convertira a TEXT al persistir en SQLite, tal como exige la
 * especificacion del profesor para evitar problemas de serializacion.
 */
public class JugadorRanking {

    private final String nombre;
    private final int monedasFinales;
    private final LocalDate fecha;

    public JugadorRanking(String nombre, int monedasFinales, LocalDate fecha) {
        this.nombre = nombre;
        this.monedasFinales = monedasFinales;
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMonedasFinales() {
        return monedasFinales;
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
