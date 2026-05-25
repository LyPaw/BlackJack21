package dao;

import model.JugadorRanking;
import java.util.List;

/**
 * Interfaz DAO que abstrae las operaciones de persistencia del ranking.
 * Definir una interfaz permite desacoplar la capa de negocio de la
 * implementacion concreta de BD, facilitando pruebas unitarias con mocks
 * y futuros cambios de motor de base de datos.
 */
public interface RankingDAO {

    /** Persiste un nuevo registro del jugador en la tabla ranking. */
    void insertar(JugadorRanking jugador);

    /** Retorna la lista de los 5 mejores jugadores (Top 5) ordenados por monedas finales descendente. */
    List<JugadorRanking> obtenerTop5();
}
