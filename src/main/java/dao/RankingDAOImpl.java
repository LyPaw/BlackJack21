package dao;

import database.ConexionDB;
import model.JugadorRanking;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion concreta de RankingDAO sobre SQLite. Esta clase pertenece
 * a la capa DAO porque contiene todo el codigo SQL y JDBC ejecutable,
 * aislado completamente del controlador y de la capa de negocio.
 */
public class RankingDAOImpl implements RankingDAO {

    /**
     * Inserta un JugadorRanking. Convierte LocalDate a cadena TEXT ISO (yyyy-MM-dd)
     * al enviarlo a SQLite, cumpliendo el requisito del profesor de que la fecha
     * se persista como texto y no como tipo DATE nativo.
     */
    @Override
    public void insertar(JugadorRanking jugador) {
        String sentenciaSQL = "INSERT INTO ranking (nombre, monedas_finales, fecha) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionDB.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sentenciaSQL)) {
            statement.setString(1, jugador.getNombre());
            statement.setInt(2, jugador.getMonedasFinales());
            statement.setString(3, jugador.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE));
            statement.executeUpdate();
        } catch (SQLException excepcion) {
            System.err.println("Error insertando record: " + excepcion.getMessage());
        }
    }

    /**
     * Consulta las 5 puntuaciones mas altas. Al leer, convierte la fecha TEXT
     * almacenada de vuelta a LocalDate para mantener el tipo seguro en Java.
     */
    @Override
    public List<JugadorRanking> obtenerTop5() {
        List<JugadorRanking> ranking = new ArrayList<>();
        String sentenciaSQL = "SELECT nombre, monedas_finales, fecha " +
                "FROM ranking ORDER BY monedas_finales DESC LIMIT 5";
        try (Connection conexion = ConexionDB.obtenerConexion();
             Statement statement = conexion.createStatement();
             ResultSet resultado = statement.executeQuery(sentenciaSQL)) {
            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                int monedas = resultado.getInt("monedas_finales");
                LocalDate fecha = LocalDate.parse(resultado.getString("fecha"),
                        DateTimeFormatter.ISO_LOCAL_DATE);
                ranking.add(new JugadorRanking(nombre, monedas, fecha));
            }
        } catch (SQLException excepcion) {
            System.err.println("Error listando ranking: " + excepcion.getMessage());
        }
        return ranking;
    }
}
