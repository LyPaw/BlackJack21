package com.blackjack.app;

import controller.JuegoController;
import controller.MusicManager;
import database.ConexionDB;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Juego21App extends Application {

    private static Button btn(String text, String bg) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color:" + bg + ";-fx-text-fill:white;-fx-font-weight:bold;-fx-padding:8 18;");
        return b;
    }

    private static HBox cajaCartas(String bg) {
        HBox c = new HBox(15);
        c.setPadding(new Insets(20));
        c.setAlignment(Pos.CENTER);
        c.setMinHeight(250);
        c.setStyle("-fx-background-color:" + bg + ";-fx-border-radius:15;-fx-background-radius:15;");
        return c;
    }

    @Override
    public void start(Stage ventanaPrincipal) {
        ConexionDB.crearTabla();
        MusicManager.iniciar();

        Label tituloJuego = new Label("BLACKJACK 21");
        tituloJuego.setStyle("-fx-font-size:26;-fx-font-weight:bold;-fx-text-fill:#FDD835;-fx-padding:8;");
        Label etiquetaMonedas = new Label("1000");
        etiquetaMonedas.setStyle("-fx-font-size:18;-fx-font-weight:bold;-fx-text-fill:#2E7D32;");
        Label etiquetaPuntuacion = new Label("0");
        etiquetaPuntuacion.setStyle("-fx-font-size:18;-fx-font-weight:bold;-fx-text-fill:#1565C0;");
        Label etiquetaBanca = new Label("Banca: ?");
        etiquetaBanca.setStyle("-fx-font-size:14;-fx-font-weight:bold;-fx-text-fill:#C62828;");
        Label etiquetaEstado = new Label("Bienvenido! Pide una carta.");
        etiquetaEstado.setStyle("-fx-font-size:14;-fx-font-weight:bold;-fx-text-fill:#4A148C;");

        HBox contenedorCartas = cajaCartas("#1B5E20");
        HBox contenedorCartasBanca = cajaCartas("#4E342E");

        Button botonPedir = btn("Pedir Carta", "#1565C0");
        Button botonPlantarse = btn("Plantarse", "#E65100");
        Button botonReiniciar = btn("Reiniciar", "#2E7D32");
        botonReiniciar.setDisable(true);
        Button botonRegistrar = btn("Registrar Record", "#6A1B9A");
        Button botonVerRanking = btn("Ver Ranking", "#00838F");

        StackPane raiz = new StackPane();
        JuegoController controlador = new JuegoController(raiz, etiquetaMonedas, etiquetaPuntuacion, etiquetaEstado, etiquetaBanca,
                contenedorCartas, contenedorCartasBanca, botonPedir, botonPlantarse, botonReiniciar, botonRegistrar,
                botonVerRanking);

        botonPedir.setOnAction(e -> controlador.pedirCarta());
        botonPlantarse.setOnAction(e -> controlador.plantarse());
        botonReiniciar.setOnAction(e -> controlador.reiniciarPartida());
        botonRegistrar.setOnAction(e -> controlador.registrarPuntuacion());
        botonVerRanking.setOnAction(e -> controlador.verRanking());
        controlador.prepararNuevaMano();

        HBox panelSuperior = new HBox(tituloJuego);
        panelSuperior.setAlignment(Pos.CENTER);
        panelSuperior.setStyle("-fx-background-color:#1A237E;");

        HBox panelInformacion = new HBox(25);
        panelInformacion.setAlignment(Pos.CENTER);
        panelInformacion.setPadding(new Insets(8));
        Label lblMonedas = new Label("Monedas:");
        lblMonedas.setStyle("-fx-font-size:14;-fx-font-weight:bold;");
        Label lblPuntos = new Label("Puntos:");
        lblPuntos.setStyle("-fx-font-size:14;-fx-font-weight:bold;");
        panelInformacion.getChildren().addAll(lblMonedas, etiquetaMonedas, lblPuntos, etiquetaPuntuacion, etiquetaBanca);

        Label lblVol = new Label("\uD83D\uDD0A");
        lblVol.setStyle("-fx-font-size:16;");
        Slider sliderVol = new Slider(0, 1, 0.3);
        sliderVol.setPrefWidth(100);
        sliderVol.setShowTickLabels(false);
        sliderVol.valueProperty().addListener((obs, old, val) -> MusicManager.setVolumen(val.doubleValue()));
        panelInformacion.getChildren().addAll(lblVol, sliderVol);

        HBox panelBotones = new HBox(10);
        panelBotones.setAlignment(Pos.CENTER);
        panelBotones.setPadding(new Insets(8));
        panelBotones.getChildren().addAll(botonPedir, botonPlantarse, botonReiniciar, botonRegistrar, botonVerRanking);

        VBox panelInferior = new VBox(5);
        panelInferior.setAlignment(Pos.CENTER);
        panelInferior.setPadding(new Insets(16, 12, 30, 12));
        panelInferior.getChildren().addAll(panelInformacion, panelBotones, etiquetaEstado);

        Label etiquetaBancaCartas = new Label("Banca");
        etiquetaBancaCartas.setStyle("-fx-font-size:11;-fx-font-weight:bold;-fx-text-fill:#CCC;-fx-padding:0 0 0 5;");
        Label etiquetaTusCartas = new Label("Tus cartas");
        etiquetaTusCartas.setStyle("-fx-font-size:11;-fx-font-weight:bold;-fx-text-fill:#CCC;-fx-padding:0 0 0 5;");

        VBox zonaCentral = new VBox(4);
        zonaCentral.setPadding(new Insets(8, 20, 8, 20));
        zonaCentral.setStyle("-fx-background-color:#1A237E;-fx-background-radius:10;");
        zonaCentral.getChildren().addAll(etiquetaBancaCartas, contenedorCartasBanca, new Label(), etiquetaTusCartas, contenedorCartas);

        BorderPane juegoLayout = new BorderPane();
        juegoLayout.setTop(panelSuperior);
        BorderPane.setMargin(panelSuperior, new Insets(0, 0, 5, 0));
        juegoLayout.setCenter(zonaCentral);
        juegoLayout.setBottom(panelInferior);
        BorderPane.setMargin(panelInferior, new Insets(10));
        raiz.getChildren().add(juegoLayout);

        Scene escena = new Scene(raiz);
        ventanaPrincipal.setTitle("Blackjack 21");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.setFullScreen(true);
        ventanaPrincipal.show();
        try {
            ventanaPrincipal.getIcons().add(new Image(getClass().getResourceAsStream("/logo/logo.png")));
        } catch (Exception e) {
            System.out.println("No se pudo cargar el logo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
