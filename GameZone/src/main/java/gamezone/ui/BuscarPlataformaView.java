package gamezone.ui;

import gamezone.model.VideoGame;
import gamezone.service.VideoGameService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class BuscarPlataformaView extends BaseView {

    private final VideoGameService service;

    public BuscarPlataformaView(VideoGameService service, Stage owner) {
        super(owner, "Buscar por Plataforma", 720, 440);
        this.service = service;
    }

    @Override
    protected VBox buildContent() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(20));
        root.setStyle(BG);
        root.getChildren().add(sectionLabel("🖥️  Buscar por Plataforma"));

        TextField tfPlat = new TextField();
        tfPlat.setPromptText("Ej: PC, PlayStation 5...");
        tfPlat.setStyle(FIELD_STYLE);
        tfPlat.setPrefWidth(280);

        Button btnBuscar = new Button("Buscar");
        btnBuscar.setStyle(BTN_PRIMARY);
        btnBuscar.setPrefHeight(36);

        HBox row = new HBox(10, fieldLabel("Plataforma:"), tfPlat, btnBuscar);

        TableView<VideoGame> table = new TableView<>();
        table.setStyle("-fx-background-color:#1e1e3a;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<VideoGame,String> cTit   = col("Título",   vg -> vg.getTitle());
        TableColumn<VideoGame,String> cPrecio= col("Precio",   vg -> String.format("$%.2f", vg.calculateFinalPrice()));
        TableColumn<VideoGame,String> cPlat  = col("Plataforma",vg -> vg.getPlatform());
        TableColumn<VideoGame,String> cStock = col("Stock",    vg -> String.valueOf(vg.getStock()));
        table.getColumns().addAll(cTit, cPrecio, cPlat, cStock);

        btnBuscar.setOnAction(e -> {
            String plat = tfPlat.getText().trim();
            if (plat.isEmpty()) { showError("Ingresa una plataforma."); return; }
            List<VideoGame> result = service.buscarPorPlataforma(plat);
            if (result == null || result.isEmpty()) {
                showWarning("No se encontraron juegos para la plataforma '" + plat + "'.");
                table.setItems(FXCollections.emptyObservableList());
            } else {
                table.setItems(FXCollections.observableArrayList(result));
            }
        });

        root.getChildren().addAll(row, table);
        return root;
    }

    private TableColumn<VideoGame,String> col(String h, java.util.function.Function<VideoGame,String> ex) {
        TableColumn<VideoGame,String> c = new TableColumn<>(h);
        c.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(ex.apply(d.getValue())));
        return c;
    }
}
