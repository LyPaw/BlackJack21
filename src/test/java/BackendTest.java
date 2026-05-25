import dao.RankingDAOImpl;
import database.ConexionDB;
import model.Carta;
import model.JugadorRanking;
import org.junit.jupiter.api.*;
import service.RankingService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del backend (modelo, DAO, servicio).
 * Verifica que cada capa funcione correctamente de forma aislada.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BackendTest {

    // ========================
    // TESTS: model.Carta
    // ========================

    @Test
    @Order(1)
    void testCartaObtenerPuntuacion() {
        Carta c = new Carta("C", 7, "7C.png");
        assertEquals(7, c.obtenerPuntuacion());
        assertEquals("C", c.getPalo());
        assertEquals("7C.png", c.getNombreArchivoImagen());
    }

    @Test
    @Order(2)
    void testCartaFiguraDaDiez() {
        assertEquals(10, new Carta("H", 10, "JH.png").obtenerPuntuacion());
        assertEquals(10, new Carta("D", 10, "QD.png").obtenerPuntuacion());
        assertEquals(10, new Carta("S", 10, "KS.png").obtenerPuntuacion());
    }

    @Test
    @Order(3)
    void testCartaAsDaOnce() {
        assertEquals(11, new Carta("C", 11, "AC.png").obtenerPuntuacion());
    }

    // ========================
    // TESTS: model.JugadorRanking
    // ========================

    @Test
    @Order(4)
    void testJugadorRankingConFechaLocalDate() {
        LocalDate hoy = LocalDate.of(2026, 5, 23);
        JugadorRanking j = new JugadorRanking("Ana", 1300, hoy);
        assertEquals("Ana", j.getNombre());
        assertEquals(1300, j.getMonedasFinales());
        assertEquals(hoy, j.getFecha());
    }

    @Test
    @Order(5)
    void testJugadorRankingFechaFormatoISO() {
        LocalDate hoy = LocalDate.now();
        JugadorRanking j = new JugadorRanking("Test", 500, hoy);
        assertEquals(hoy.format(DateTimeFormatter.ISO_LOCAL_DATE), j.getFecha().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    // ========================
    // TESTS: database.ConexionDB + dao.RankingDAOImpl
    // ========================

    private static RankingDAOImpl dao;
    private static final String NOMBRE_TEST = "TEST_Jugador_" + System.currentTimeMillis();

    @BeforeAll
    static void setupBaseDeDatos() {
        ConexionDB.crearTabla();
        dao = new RankingDAOImpl();
    }

    @BeforeEach
    void limpiarRegistrosTestAntesDeCadaTest() {
        try (var conn = ConexionDB.obtenerConexion();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM ranking WHERE nombre LIKE 'TEST_%'");
        } catch (Exception e) {
            System.err.println("Error limpiando registros test: " + e.getMessage());
        }
    }

    @AfterAll
    static void limpiarRegistrosTest() {
        // Limpia los registros de prueba de la BD usando JDBC directo
        try (var conn = database.ConexionDB.obtenerConexion();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM ranking WHERE nombre LIKE 'TEST_%'");
        } catch (Exception e) {
            System.err.println("Error limpiando registros test: " + e.getMessage());
        }
    }

    @Test
    @Order(6)
    void testInsertarYRecuperarRegistro() {
        RankingService servicio = new RankingService();
        servicio.guardarRecord(NOMBRE_TEST, 999);

        List<JugadorRanking> ranking = servicio.obtenerRanking();
        boolean encontrado = ranking.stream()
                .anyMatch(j -> j.getNombre().equals(NOMBRE_TEST) && j.getMonedasFinales() == 999);
        assertTrue(encontrado, "El registro insertado debe aparecer en el ranking");
    }

    @Test
    @Order(7)
    void testTop5OrdenDescendente() {
        // Inserta registros con puntuaciones variadas
        for (int i = 0; i < 6; i++) {
            dao.insertar(new JugadorRanking("TEST_P_" + i, 500 - i * 50, LocalDate.now()));
        }

        List<JugadorRanking> top5 = dao.obtenerTop5();
        assertEquals(5, top5.size(), "El Top 5 debe contener exactamente 5 registros");

        // Verifica orden descendente
        for (int i = 1; i < top5.size(); i++) {
            assertTrue(top5.get(i - 1).getMonedasFinales() >= top5.get(i).getMonedasFinales(),
                    "El ranking debe estar ordenado de mayor a menor");
        }
    }

    @Test
    @Order(8)
    void testFechaPersistidaComoText() {
        LocalDate fechaEsperada = LocalDate.of(2026, 12, 25);
        JugadorRanking j = new JugadorRanking("TEST_Navidad", 777, fechaEsperada);
        dao.insertar(j);

        // Verifica que la fecha se recupere correctamente como LocalDate
        List<JugadorRanking> ranking = dao.obtenerTop5();
        boolean ok = ranking.stream()
                .anyMatch(r -> r.getNombre().equals("TEST_Navidad")
                        && r.getFecha().equals(fechaEsperada));
        assertTrue(ok, "La fecha LocalDate debe persistirse y recuperarse sin perdida");
    }

    @Test
    @Order(9)
    void testCampoFechaNoEsNulo() {
        List<JugadorRanking> ranking = dao.obtenerTop5();
        for (JugadorRanking j : ranking) {
            assertNotNull(j.getFecha(), "La fecha nunca debe ser null en los registros");
        }
    }

    // ========================
    // TESTS: service.RankingService
    // ========================

    @Test
    @Order(10)
    void testRankingServiceGuardaYRecupera() {
        RankingService servicio = new RankingService();
        String nombreUnico = "TEST_SERVICE_" + System.nanoTime();
        servicio.guardarRecord(nombreUnico, 1234);

        List<JugadorRanking> resultados = servicio.obtenerRanking();
        assertTrue(resultados.stream().anyMatch(j -> j.getNombre().equals(nombreUnico)));
    }
}
