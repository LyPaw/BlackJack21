package database;

import java.sql.*;

public class ConexionDB {

    private static String urlBaseDatos = "jdbc:sqlite:ranking.db";

    public static void setUrlBaseDatos(String url) { urlBaseDatos = url; }
    public static Connection obtenerConexion() throws SQLException { return DriverManager.getConnection(urlBaseDatos); }

    public static void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS ranking (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "monedas_finales INTEGER NOT NULL, " +
                "fecha TEXT NOT NULL)";
        try (Connection conexion = obtenerConexion(); Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) { System.err.println("Error creando tabla ranking: " + e.getMessage()); }
    }
}
