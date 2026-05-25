package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase tecnica de infraestructura responsable unicamente de gestionar
 * la conexion JDBC con SQLite. Pertenece a su propio paquete 'database'
 * porque su unica responsabilidad es la comunicacion con el motor de BD,
 * manteniendo asi el Principio de Responsabilidad Unica (SRP).
 */
public class ConexionDB {

    private static final String URL_BASE_DATOS = "jdbc:sqlite:ranking.db";

    /** Retorna una conexion al archivo ranking.db (lo crea si no existe). */
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL_BASE_DATOS);
    }

    /** Crea la tabla 'ranking' si aun no existe, usando DDL con columna fecha como TEXT. */
    public static void crearTabla() {
        String sentenciaSQL = "CREATE TABLE IF NOT EXISTS ranking (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "monedas_finales INTEGER NOT NULL, " +
                "fecha TEXT NOT NULL)";
        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement()) {
            statement.execute(sentenciaSQL);
        } catch (SQLException excepcion) {
            System.err.println("Error creando tabla ranking: " + excepcion.getMessage());
        }
    }
}
