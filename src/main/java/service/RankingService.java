package service;

import dao.RankingDAO;
import dao.RankingDAOImpl;
import model.JugadorRanking;
import java.time.LocalDate;
import java.util.List;

/**
 * Capa intermedia de negocio que aisla al controlador de la implementacion
 * concreta del DAO. Pertenece al paquete 'service' porque orquesta reglas
 * de negocio (como asignar la fecha automaticamente) sin conocer los
 * detalles de JavaFX ni de la base de datos.
 */
public class RankingService {

    private final RankingDAO rankingDAO;

    /** El servicio crea su propio DAO concreto; en un entorno real se usaria inyeccion de dependencias. */
    public RankingService() {
        this.rankingDAO = new RankingDAOImpl();
    }

    /**
     * Guarda un nuevo record. Crea el objeto JugadorRanking con la fecha actual
     * (LocalDate.now()) y delega la persistencia al DAO.
     */
    public void guardarRecord(String nombre, int monedasFinales) {
        JugadorRanking jugador = new JugadorRanking(nombre, monedasFinales, LocalDate.now());
        rankingDAO.insertar(jugador);
    }

    /** Retorna el Top 5 de jugadores delegando la consulta al DAO. */
    public List<JugadorRanking> obtenerRanking() {
        return rankingDAO.obtenerTop5();
    }
}
