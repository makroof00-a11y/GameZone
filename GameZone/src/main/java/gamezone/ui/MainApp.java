package gamezone.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GameZone – Sistema de Gestión");

        // ── Fondo degradado oscuro ──────────────────────────────────────────
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0d0d1a, #1a1a2e);");

        // ── Título ──────────────────────────────────────────────────────────
        Label title = new Label("🎮 GAMEZONE");
        title.setFont(Font.font("Arial Black", FontWeight.BOLD, 40));
        title.setTextFill(Color.web("#00e5ff"));
        title.setStyle("-fx-effect: dropshadow(gaussian, #00e5ff, 20, 0.7, 0, 0);");

        Label subtitle = new Label("SISTEMA DE GESTIÓN DE VIDEOJUEGOS");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitle.setTextFill(Color.web("#aaaacc"));

        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: #00e5ff33;");

        // ── Botones del menú ────────────────────────────────────────────────
        String[] labels = {
            "1.  Agregar Videojuego",
            "2.  Listar todos los Videojuegos",
            "3.  Buscar por Título",
            "4.  Buscar por Plataforma",
            "5.  Realizar Venta",
            "6.  Mostrar Ventas",
            "7.  Salir"
        };

        VBox btnBox = new VBox(12);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setMaxWidth(380);

        gamezone.service.VideoGameService service = new gamezone.service.VideoGameService();

        for (int i = 0; i < labels.length; i++) {
            final int idx = i + 1;
            Button btn = buildMenuButton(labels[i]);
            btn.setOnAction(e -> handleMenu(idx, primaryStage, service));
            btnBox.getChildren().add(btn);
        }

        root.getChildren().addAll(title, subtitle, sep, btnBox);

        Scene scene = new Scene(root, 520, 660);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // ── Builder de botones ──────────────────────────────────────────────────
    private Button buildMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(360);
        btn.setPrefHeight(42);
        btn.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        btn.setStyle(
            "-fx-background-color: #1e1e3a;" +
            "-fx-text-fill: #e0e0ff;" +
            "-fx-border-color: #00e5ff55;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #00e5ff22;" +
            "-fx-text-fill: #00e5ff;" +
            "-fx-border-color: #00e5ff;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: #1e1e3a;" +
            "-fx-text-fill: #e0e0ff;" +
            "-fx-border-color: #00e5ff55;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 6;" +
            "-fx-background-radius: 6;" +
            "-fx-cursor: hand;"
        ));
        return btn;
    }

    // ── Router del menú ─────────────────────────────────────────────────────
    private void handleMenu(int option, Stage owner, gamezone.service.VideoGameService service) {
        switch (option) {
            case 1 -> new AgregarVideojuegoView(service, owner).show();
            case 2 -> new ListarVideojuegosView(service, owner).show();
            case 3 -> new BuscarTituloView(service, owner).show();
            case 4 -> new BuscarPlataformaView(service, owner).show();
            case 5 -> new VenderView(service, owner).show();
            case 6 -> new ListarVentasView(service, owner).show();
            case 7 -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "¿Desea salir del sistema?", ButtonType.YES, ButtonType.NO);
                confirm.setTitle("Salir");
                confirm.setHeaderText(null);
                confirm.showAndWait().ifPresent(r -> {
                    if (r == ButtonType.YES) owner.close();
                });
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
