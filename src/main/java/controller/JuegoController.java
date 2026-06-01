package controller;

import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.Carta;
import service.RankingService;
import view.RankingView;

import java.util.*;

public class JuegoController {

    private static final int APUESTA = 100;
    private static final int MONEDAS_INICIALES = 1000;
    private static final int BANCA_SE_PLANTA = 17;

    private int monedasActuales = MONEDAS_INICIALES;
    private int puntuacionActual = 0;
    private int puntuacionBanca = 0;
    private List<Carta> baraja;
    private List<Carta> cartasJugador;
    private List<Carta> cartasBanca;
    private boolean manoActiva = false;

    private final Label etiquetaMonedas;
    private final Label etiquetaPuntuacion;
    private final Label etiquetaEstado;
    private final Label etiquetaBanca;
    private final HBox contenedorCartas;
    private final HBox contenedorCartasBanca;
    private final Button botonPedir;
    private final Button botonPlantarse;
    private final Button botonReiniciar;
    private final Button botonRegistrar;
    private final Button botonVerRanking;

    private final RankingView rankingView;

    public JuegoController(StackPane raiz,
                           Label etiquetaMonedas, Label etiquetaPuntuacion,
                           Label etiquetaEstado, Label etiquetaBanca,
                           HBox contenedorCartas, HBox contenedorCartasBanca,
                           Button botonPedir, Button botonPlantarse,
                           Button botonReiniciar, Button botonRegistrar,
                           Button botonVerRanking) {
        this.etiquetaMonedas = etiquetaMonedas;
        this.etiquetaPuntuacion = etiquetaPuntuacion;
        this.etiquetaEstado = etiquetaEstado;
        this.etiquetaBanca = etiquetaBanca;
        this.contenedorCartas = contenedorCartas;
        this.contenedorCartasBanca = contenedorCartasBanca;
        this.botonPedir = botonPedir;
        this.botonPlantarse = botonPlantarse;
        this.botonReiniciar = botonReiniciar;
        this.botonRegistrar = botonRegistrar;
        this.botonVerRanking = botonVerRanking;
        this.rankingView = new RankingView(raiz, new RankingService());
        this.cartasJugador = new ArrayList<>();
        this.cartasBanca = new ArrayList<>();
        this.baraja = new ArrayList<>();
    }

    private void inicializarBaraja() {
        baraja = new ArrayList<>();
        String[] palos = {"C", "D", "H", "S"};
        String[] caras = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] valores = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};
        for (String palo : palos)
            for (int i = 0; i < caras.length; i++)
                baraja.add(new Carta(palo, valores[i], caras[i] + palo + ".png"));
        Collections.shuffle(baraja);
        MusicManager.reproducirSonidoBarajar();
    }

    private int calcularPuntuacion(List<Carta> cartas) {
        int total = 0;
        int aces = 0;
        for (Carta c : cartas) {
            int valor = c.obtenerPuntuacion();
            total += valor;
            if (valor == 11) aces++;
        }
        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }
        return total;
    }

    private void mostrarCartaEn(Carta carta, HBox contenedor) {
        java.net.URL url = getClass().getResource("/image/" + carta.getNombreArchivoImagen());
        if (url == null) return;
        ImageView vista = new ImageView(new Image(url.toString()));
        vista.setFitHeight(200);
        vista.setPreserveRatio(true);
        contenedor.getChildren().add(vista);
    }

    public void prepararNuevaMano() {
        puntuacionActual = 0;
        puntuacionBanca = 0;
        cartasJugador = new ArrayList<>();
        cartasBanca = new ArrayList<>();
        contenedorCartas.getChildren().clear();
        contenedorCartasBanca.getChildren().clear();
        etiquetaPuntuacion.setText("0");
        etiquetaBanca.setText("Banca: ?");
        etiquetaEstado.setText("Nueva mano - pide una carta");
        inicializarBaraja();
        botonPedir.setDisable(false);
        botonPlantarse.setDisable(false);
        botonReiniciar.setDisable(true);
        manoActiva = true;
    }

    public void reiniciarPartida() {
        monedasActuales = MONEDAS_INICIALES;
        etiquetaMonedas.setText(String.valueOf(monedasActuales));
        prepararNuevaMano();
        botonRegistrar.setDisable(false);
    }

    public void pedirCarta() {
        if (!manoActiva) {
            if (monedasActuales <= 0) return;
            prepararNuevaMano();
        }
        if (baraja.isEmpty()) return;
        Carta carta = baraja.remove(0);
        cartasJugador.add(carta);
        mostrarCartaEn(carta, contenedorCartas);
        puntuacionActual = calcularPuntuacion(cartasJugador);
        etiquetaPuntuacion.setText(String.valueOf(puntuacionActual));
        if (puntuacionActual > 21) {
            etiquetaEstado.setText("Te pasaste de 21! Perdiste.");
            monedasActuales -= APUESTA;
            etiquetaMonedas.setText(String.valueOf(monedasActuales));
            finalizarMano();
        }
    }

    public void plantarse() {
        if (!manoActiva) return;
        botonPedir.setDisable(true);
        botonPlantarse.setDisable(true);
        while (puntuacionBanca < BANCA_SE_PLANTA && !baraja.isEmpty()) {
            Carta carta = baraja.remove(0);
            cartasBanca.add(carta);
            mostrarCartaEn(carta, contenedorCartasBanca);
            puntuacionBanca = calcularPuntuacion(cartasBanca);
        }
        etiquetaBanca.setText("Banca: " + puntuacionBanca + " pts");
        if (puntuacionBanca > 21 || puntuacionActual > puntuacionBanca) {
            etiquetaEstado.setText("Ganaste! (+" + APUESTA + ")");
            monedasActuales += APUESTA;
        } else if (puntuacionActual == puntuacionBanca) {
            etiquetaEstado.setText("Empate. Recuperas tu apuesta.");
        } else {
            etiquetaEstado.setText("Banca gana. (-" + APUESTA + ")");
            monedasActuales -= APUESTA;
        }
        etiquetaMonedas.setText(String.valueOf(monedasActuales));
        finalizarMano();
    }

    private void finalizarMano() {
        manoActiva = false;
        botonPlantarse.setDisable(true);
        botonReiniciar.setDisable(false);
        if (monedasActuales <= 0) {
            botonPedir.setDisable(true);
            botonRegistrar.setDisable(false);
            etiquetaEstado.setText("Sin monedas! Registra tu record o reinicia.");
        } else {
            botonPedir.setDisable(false);
            etiquetaEstado.setText(etiquetaEstado.getText() + " Pulsa 'Pedir Carta' para seguir.");
        }
    }

    public void registrarPuntuacion() { rankingView.registrarPuntuacion(monedasActuales); }
    public void verRanking() { rankingView.mostrarRanking(); }
}
