package dao;

import model.JugadorRanking;
import java.util.List;

public interface RankingDAO {
    void insertar(JugadorRanking jugador);
    List<JugadorRanking> obtenerTop5();
}
