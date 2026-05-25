import controller.JuegoController;
import database.ConexionDB;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Clase de entrada de la aplicacion. Extiende Application y es la unica
 * responsable del ciclo de vida JavaFX y de construir la interfaz grafica
 * siguiendo el patron MVC. La creacion del layout, los estilos CSS y la
 * conexion de eventos se realiza aqui, mientras que toda la logica del
 * juego se delega en JuegoController.
 */
public class Juego21App extends Application {

    @Override
    public void start(Stage ventanaPrincipal) {
        // Inicializa la base de datos (crea ranking.db y la tabla si no existen)
        ConexionDB.crearTabla();

        // ========== CREACION DE COMPONENTES DE INTERFAZ ==========

        Label tituloJuego = new Label("BLACKJACK 21");
        tituloJuego.setStyle("-fx-font-size: 26; -fx-font-weight: bold; " +
                "-fx-text-fill: #FDD835; -fx-padding: 8;");

        Label etiquetaMonedas = new Label("1000");
        etiquetaMonedas.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2E7D32;");

        Label etiquetaPuntuacion = new Label("0");
        etiquetaPuntuacion.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #1565C0;");

        Label etiquetaBanca = new Label("Banca: ?");
        etiquetaBanca.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #C62828;");

        Label etiquetaEstado = new Label("Bienvenido! Pide una carta.");
        etiquetaEstado.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #4A148C;");

        HBox contenedorCartas = new HBox(15);
        contenedorCartas.setPadding(new Insets(20));
        contenedorCartas.setAlignment(Pos.CENTER);
        contenedorCartas.setMinHeight(250);
        contenedorCartas.setStyle("-fx-background-color: #1B5E20; " +
                "-fx-border-radius: 15; -fx-background-radius: 15;");

        HBox contenedorCartasBanca = new HBox(15);
        contenedorCartasBanca.setPadding(new Insets(20));
        contenedorCartasBanca.setAlignment(Pos.CENTER);
        contenedorCartasBanca.setMinHeight(250);
        contenedorCartasBanca.setStyle("-fx-background-color: #4E342E; " +
                "-fx-border-radius: 15; -fx-background-radius: 15;");

        // Botones con estilo CSS y eventos lambda conectados al controlador
        Button botonPedir = new Button("Pedir Carta");
        botonPedir.setStyle("-fx-background-color: #1565C0; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 18;");

        Button botonPlantarse = new Button("Plantarse");
        botonPlantarse.setStyle("-fx-background-color: #E65100; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 18;");

        Button botonReiniciar = new Button("Reiniciar");
        botonReiniciar.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 18;");
        botonReiniciar.setDisable(true);

        Button botonRegistrar = new Button("Registrar Record");
        botonRegistrar.setStyle("-fx-background-color: #6A1B9A; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 18;");

        Button botonVerRanking = new Button("Ver Ranking");
        botonVerRanking.setStyle("-fx-background-color: #00838F; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 8 18;");

        StackPane raiz = new StackPane();

        // ========== CONEXION VISTA-CONTROLADOR ==========

        JuegoController controlador = new JuegoController(
                raiz,
                etiquetaMonedas, etiquetaPuntuacion, etiquetaEstado, etiquetaBanca,
                contenedorCartas, contenedorCartasBanca,
                botonPedir, botonPlantarse, botonReiniciar, botonRegistrar,
                botonVerRanking);

        botonPedir.setOnAction(e -> controlador.pedirCarta());
        botonPlantarse.setOnAction(e -> controlador.plantarse());
        botonReiniciar.setOnAction(e -> controlador.reiniciarPartida());
        botonRegistrar.setOnAction(e -> controlador.registrarPuntuacion());
        botonVerRanking.setOnAction(e -> controlador.verRanking());

        // Inicia la primera mano (establece manoActiva = true, habilita botones, baraja)
        controlador.prepararNuevaMano();

        // ========== CONSTRUCCION DEL LAYOUT CON BORDERPANE ==========

        HBox panelSuperior = new HBox(tituloJuego);
        panelSuperior.setAlignment(Pos.CENTER);
        panelSuperior.setStyle("-fx-background-color: #1A237E;");

        HBox panelInformacion = new HBox(25);
        panelInformacion.setAlignment(Pos.CENTER);
        panelInformacion.setPadding(new Insets(8));
        Label lblMonedas = new Label("Monedas:");
        lblMonedas.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        Label lblPuntos = new Label("Puntos:");
        lblPuntos.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");
        panelInformacion.getChildren().addAll(
                lblMonedas, etiquetaMonedas, lblPuntos, etiquetaPuntuacion, etiquetaBanca);

        HBox panelBotones = new HBox(10);
        panelBotones.setAlignment(Pos.CENTER);
        panelBotones.setPadding(new Insets(8));
        panelBotones.getChildren().addAll(
                botonPedir, botonPlantarse, botonReiniciar, botonRegistrar, botonVerRanking);

        VBox panelInferior = new VBox(5);
        panelInferior.setAlignment(Pos.CENTER);
        panelInferior.setPadding(new Insets(16, 12, 30, 12));
        panelInferior.getChildren().addAll(panelInformacion, panelBotones, etiquetaEstado);

        Label etiquetaBancaCartas = new Label("Banca");
        etiquetaBancaCartas.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: #CCC; -fx-padding: 0 0 0 5;");

        Label etiquetaTusCartas = new Label("Tus cartas");
        etiquetaTusCartas.setStyle("-fx-font-size: 11; -fx-font-weight: bold; -fx-text-fill: #CCC; -fx-padding: 0 0 0 5;");

        VBox zonaCentral = new VBox(4);
        zonaCentral.setPadding(new Insets(8, 20, 8, 20));
        zonaCentral.setStyle("-fx-background-color: #1A237E; -fx-background-radius: 10;");
        zonaCentral.getChildren().addAll(
                etiquetaBancaCartas, contenedorCartasBanca,
                new Label(), etiquetaTusCartas, contenedorCartas);

        BorderPane juegoLayout = new BorderPane();
        juegoLayout.setTop(panelSuperior);
        BorderPane.setMargin(panelSuperior, new Insets(0, 0, 5, 0));
        juegoLayout.setCenter(zonaCentral);
        juegoLayout.setBottom(panelInferior);
        BorderPane.setMargin(panelInferior, new Insets(10));

        raiz.getChildren().add(juegoLayout);

        // ========== CONFIGURACION DEL STAGE ==========

        Scene escena = new Scene(raiz);
        ventanaPrincipal.setTitle("Blackjack 21");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.setFullScreen(true);
        ventanaPrincipal.show();
    }

    /** Punto de entrada: delega en Application.launch() el ciclo de vida JavaFX. */
    public static void main(String[] args) {
        launch(args);
    }
}
