package dao;

import database.ConexionDB;
import model.JugadorRanking;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RankingDAOImpl implements RankingDAO {

    @Override
    public void insertar(JugadorRanking jugador) {
        String sql = "INSERT INTO ranking (nombre, monedas_finales, fecha) VALUES (?, ?, ?)";
        try (Connection conexion = ConexionDB.obtenerConexion(); PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, jugador.getNombre());
            stmt.setInt(2, jugador.getMonedasFinales());
            stmt.setString(3, jugador.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE));
            stmt.executeUpdate();
        } catch (SQLException e) { System.err.println("Error insertando record: " + e.getMessage()); }
    }

    @Override
    public List<JugadorRanking> obtenerTop5() {
        ArrayList<JugadorRanking> ranking = new ArrayList<>();
        String sql = "SELECT nombre, monedas_finales, fecha FROM ranking ORDER BY monedas_finales DESC LIMIT 5";
        try (Connection conexion = ConexionDB.obtenerConexion(); Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate fecha = LocalDate.parse(rs.getString("fecha"), DateTimeFormatter.ISO_LOCAL_DATE);
                ranking.add(new JugadorRanking(rs.getString("nombre"), rs.getInt("monedas_finales"), fecha));
            }
        } catch (SQLException e) { System.err.println("Error listando ranking: " + e.getMessage()); }
        return ranking;
    }
}
