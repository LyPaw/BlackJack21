package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.JugadorRanking;
import service.RankingService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RankingView {

    private final StackPane raiz;
    private final RankingService rankingService;
    private VBox overlayActual;
    private Region fondoOverlay;

    public RankingView(StackPane raiz, RankingService rankingService) {
        this.raiz = raiz;
        this.rankingService = rankingService;
    }

    public void registrarPuntuacion(int monedasActuales) {
        VBox overlay = crearBaseOverlay("Registrar Record");
        Label mensaje = new Label("Alcanzaste " + monedasActuales + " monedas");
        mensaje.setStyle("-fx-font-size:16;-fx-font-weight:bold;-fx-text-fill:#6A1B9A;");
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill:red;-fx-font-size:12;");
        TextField campoNombre = new TextField();
        campoNombre.setPromptText("Introduce tu nombre");
        campoNombre.setStyle("-fx-font-size:14;-fx-padding:8;");
        HBox botones = new HBox(10);
        botones.setAlignment(Pos.CENTER);
        Button guardar = new Button("Guardar");
        guardar.setStyle("-fx-background-color:#6A1B9A;-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:8 20;");
        guardar.setOnAction(e -> {
            String nombre = campoNombre.getText().trim();
            if (nombre.isEmpty()) { errorLabel.setText("El nombre no puede estar vacio"); return; }
            rankingService.guardarRecord(nombre, monedasActuales);
            ocultarOverlay();
            mostrarRanking();
        });
        Button cancelar = new Button("Cancelar");
        cancelar.setStyle("-fx-background-color:#757575;-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:8 20;");
        cancelar.setOnAction(e -> ocultarOverlay());
        botones.getChildren().addAll(guardar, cancelar);
        overlay.getChildren().addAll(mensaje, errorLabel, new Label("Nombre para el ranking:"), campoNombre, botones);
        mostrarOverlay(overlay);
    }

    public void mostrarRanking() {
        List<JugadorRanking> ranking = rankingService.obtenerRanking();
        VBox overlay = crearBaseOverlay("TOP 5 RANKING");
        Label subtitulo = new Label("Los mejores jugadores de Blackjack 21");
        subtitulo.setStyle("-fx-font-size:12;-fx-text-fill:#666;");
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
        cuerpo.setStyle("-fx-padding:8;-fx-background-color:#F5F5F5;-fx-border-color:#CCC;-fx-border-radius:5;");
        Button cerrar = new Button("Cerrar");
        cerrar.setStyle("-fx-background-color:#00838F;-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:8 24;");
        cerrar.setOnAction(e -> ocultarOverlay());
        overlay.getChildren().addAll(subtitulo, cuerpo, cerrar);
        mostrarOverlay(overlay);
    }

    private VBox crearBaseOverlay(String titulo) {
        VBox overlay = new VBox(15);
        overlay.setAlignment(Pos.TOP_CENTER);
        overlay.setMaxWidth(450);
        overlay.setMaxHeight(Region.USE_PREF_SIZE);
        overlay.setPadding(new Insets(25));
        overlay.setStyle("-fx-background-color:white;-fx-background-radius:12;" +
                "-fx-border-radius:12;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.5),20,0,0,4);");
        Label encabezado = new Label(titulo);
        encabezado.setStyle("-fx-font-size:20;-fx-font-weight:bold;-fx-text-fill:#1A237E;");
        Region separador = new Region();
        separador.setStyle("-fx-border-color:#CCC;-fx-border-width:0 0 1 0;");
        separador.setMinHeight(1);
        separador.setMaxHeight(1);
        overlay.getChildren().addAll(encabezado, separador);
        return overlay;
    }

    private void mostrarOverlay(VBox overlay) {
        fondoOverlay = new Region();
        fondoOverlay.setStyle("-fx-background-color:rgba(0,0,0,0.55);");
        overlayActual = overlay;
        raiz.getChildren().addAll(fondoOverlay, overlay);
        StackPane.setAlignment(overlay, Pos.CENTER);
    }

    private void ocultarOverlay() {
        raiz.getChildren().removeAll(fondoOverlay, overlayActual);
        fondoOverlay = null;
        overlayActual = null;
    }
}
