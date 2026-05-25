package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Carta;
import model.JugadorRanking;
import service.RankingService;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.time.format.DateTimeFormatter;
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

    private final StackPane raiz;
    private final RankingService rankingService;

    private VBox overlayActual;
    private Region fondoOverlay;

    public JuegoController(StackPane raiz,
                           Label etiquetaMonedas, Label etiquetaPuntuacion,
                           Label etiquetaEstado, Label etiquetaBanca,
                           HBox contenedorCartas, HBox contenedorCartasBanca,
                           Button botonPedir, Button botonPlantarse,
                           Button botonReiniciar, Button botonRegistrar,
                           Button botonVerRanking) {
        this.raiz = raiz;
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
        this.rankingService = new RankingService();
        this.cartasJugador = new ArrayList<>();
        this.cartasBanca = new ArrayList<>();
        this.baraja = new ArrayList<>();
    }

    private void reproducirSonido() {
        try {
            java.net.URL url = getClass().getResource("/sound/barajar.wav");
            if (url == null) return;
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error de audio: " + e.getMessage());
        }
    }

    private void inicializarBaraja() {
        baraja = new ArrayList<>();
        String[] palos = {"C", "D", "H", "S"};
        String[] caras = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] valores = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};
        for (String palo : palos) {
            for (int i = 0; i < caras.length; i++) {
                baraja.add(new Carta(palo, valores[i], caras[i] + palo + ".png"));
            }
        }
        Collections.shuffle(baraja);
        reproducirSonido();
    }

    private void mostrarCartaEn(Carta carta, HBox contenedor) {
        try {
            java.net.URL url = getClass().getResource("/image/" + carta.getNombreArchivoImagen());
            if (url != null) {
                ImageView vista = new ImageView(new Image(url.toString()));
                vista.setFitHeight(200);
                vista.setPreserveRatio(true);
                contenedor.getChildren().add(vista);
            }
        } catch (Exception ex) {
            Label fallback = new Label(carta.getNombreArchivoImagen().replace(".png", ""));
            fallback.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-padding: 15; " +
                    "-fx-border-color: #333; -fx-border-radius: 5; -fx-background-color: white;");
            contenedor.getChildren().add(fallback);
        }
    }

    private VBox crearBaseOverlay(String titulo) {
        VBox overlay = new VBox(15);
        overlay.setAlignment(Pos.TOP_CENTER);
        overlay.setMaxWidth(450);
        overlay.setMaxHeight(Region.USE_PREF_SIZE);
        overlay.setPadding(new Insets(25));
        overlay.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 20, 0, 0, 4);");

        Label encabezado = new Label(titulo);
        encabezado.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        Region separador = new Region();
        separador.setStyle("-fx-border-color: #CCC; -fx-border-width: 0 0 1 0;");
        separador.setMinHeight(1);
        separador.setMaxHeight(1);

        overlay.getChildren().addAll(encabezado, separador);
        return overlay;
    }

    private void mostrarOverlay(VBox overlay) {
        fondoOverlay = new Region();
        fondoOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.55);");
        overlayActual = overlay;
        raiz.getChildren().addAll(fondoOverlay, overlay);
        StackPane.setAlignment(overlay, Pos.CENTER);
    }

    private void ocultarOverlay() {
        raiz.getChildren().removeAll(fondoOverlay, overlayActual);
        fondoOverlay = null;
        overlayActual = null;
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
        puntuacionActual += carta.obtenerPuntuacion();
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
        List<Carta> copiaBaraja = new ArrayList<>(baraja);
        Collections.shuffle(copiaBaraja);
        while (puntuacionBanca < BANCA_SE_PLANTA && !copiaBaraja.isEmpty()) {
            Carta carta = copiaBaraja.remove(0);
            cartasBanca.add(carta);
            mostrarCartaEn(carta, contenedorCartasBanca);
            puntuacionBanca += carta.obtenerPuntuacion();
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

    public void registrarPuntuacion() {
        VBox overlay = crearBaseOverlay("Registrar Record");

        Label mensaje = new Label("Alcanzaste " + monedasActuales + " monedas");
        mensaje.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #6A1B9A;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12;");

        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Introduce tu nombre");
        campoNombre.setStyle("-fx-font-size: 14; -fx-padding: 8;");

        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER);

        Button guardar = new Button("Guardar");
        guardar.setStyle("-fx-background-color: #6A1B9A; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");
        guardar.setOnAction(e -> {
            String nombre = campoNombre.getText().trim();
            if (nombre.isEmpty()) {
                errorLabel.setText("El nombre no puede estar vacio");
                return;
            }
            rankingService.guardarRecord(nombre, monedasActuales);
            ocultarOverlay();
            mostrarRanking();
        });

        Button cancelar = new Button("Cancelar");
        cancelar.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20;");
        cancelar.setOnAction(e -> ocultarOverlay());

        botones.getChildren().addAll(guardar, cancelar);

        overlay.getChildren().addAll(mensaje, errorLabel, new Label("Nombre para el ranking:"), campoNombre, botones);
        mostrarOverlay(overlay);
    }

    private void mostrarRanking() {
        List<JugadorRanking> ranking = rankingService.obtenerRanking();

        VBox overlay = crearBaseOverlay("TOP 5 RANKING");

        Label subtitulo = new Label("Los mejores jugadores de Blackjack 21");
        subtitulo.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        StringBuilder texto = new StringBuilder();
        if (ranking.isEmpty()) {
            texto.append("\n\n  Aun no hay registros. Se el primero!\n");
        } else {
            texto.append(String.format("  %-3s %-20s %10s   %s\n", "N\u00ba", "Nombre", "Monedas", "Fecha"));
            texto.append("  ").append("\u2500".repeat(52)).append("\n");
            for (int i = 0; i < ranking.size(); i++) {
                JugadorRanking j = ranking.get(i);
                texto.append(String.format("  %-3d %-20s %8d   %s\n",
                        i + 1, j.getNombre(), j.getMonedasFinales(),
                        j.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            }
        }

        Label cuerpo = new Label(texto.toString());
        cuerpo.setFont(Font.font("Consolas", FontWeight.NORMAL, 14));
        cuerpo.setStyle("-fx-padding: 8; -fx-background-color: #F5F5F5; -fx-border-color: #CCC; -fx-border-radius: 5;");

        Button cerrar = new Button("Cerrar");
        cerrar.setStyle("-fx-background-color: #00838F; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 24;");
        cerrar.setOnAction(e -> ocultarOverlay());

        overlay.getChildren().addAll(subtitulo, cuerpo, cerrar);
        mostrarOverlay(overlay);
    }

    public void verRanking() {
        mostrarRanking();
    }
}
