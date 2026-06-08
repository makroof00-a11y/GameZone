package gamezone.ui;

import gamezone.model.VideoGame;
import gamezone.service.VideoGameService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BuscarTituloView extends BaseView {

    private final VideoGameService service;

    public BuscarTituloView(VideoGameService service, Stage owner) {
        super(owner, "Buscar por Título", 520, 360);
        this.service = service;
    }

    @Override
    protected VBox buildContent() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(20));
        root.setStyle(BG);
        root.getChildren().add(sectionLabel("🔍  Buscar por Título"));

        TextField tfBuscar = new TextField();
        tfBuscar.setPromptText("Ingresa el título del juego...");
        tfBuscar.setStyle(FIELD_STYLE);
        tfBuscar.setPrefWidth(340);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(BTN_PRIMARY);
        btnBuscar.setPrefHeight(36);

        HBox row = new HBox(10, fieldLabel("Título:"), tfBuscar, btnBuscar);
        row.setPadding(new Insets(0));

        TextArea resultado = new TextArea();
        resultado.setEditable(false);
        resultado.setStyle("-fx-control-inner-background:#1e1e3a;-fx-text-fill:#e0e0ff;");
        resultado.setFont(Font.font("Courier New", 13));
        resultado.setPrefRowCount(10);

        btnBuscar.setOnAction(e -> {
            String titulo = tfBuscar.getText().trim();
            if (titulo.isEmpty()) { showError("Ingresa un título para buscar."); return; }
            VideoGame vg = service.buscarPorTitulo(titulo);
            if (vg != null) resultado.setText(vg.getDisplayInfo());
            else            resultado.setText("❌ No se encontró ningún juego con ese título.");
        });

        root.getChildren().addAll(row, resultado);
        return root;
    }
}
