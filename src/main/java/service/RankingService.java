package service;

import dao.RankingDAO;
import dao.RankingDAOImpl;
import model.JugadorRanking;
import java.time.LocalDate;
import java.util.List;

public class RankingService {

    private final RankingDAO rankingDAO;

    public RankingService() { this.rankingDAO = new RankingDAOImpl(); }

    public void guardarRecord(String nombre, int monedasFinales) {
        rankingDAO.insertar(new JugadorRanking(nombre, monedasFinales, LocalDate.now()));
    }

    public List<JugadorRanking> obtenerRanking() { return rankingDAO.obtenerTop5(); }
}
