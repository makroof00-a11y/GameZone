package gamezone.ui;

import gamezone.model.DigitalVideoGame;
import gamezone.model.VideoGame;
import gamezone.service.VideoGameService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ListarVideojuegosView extends BaseView {

    private final VideoGameService service;

    public ListarVideojuegosView(VideoGameService service, Stage owner) {
        super(owner, "Todos los Videojuegos", 820, 480);
        this.service = service;
    }

    @Override
    protected VBox buildContent() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(20));
        root.setStyle(BG);
        root.getChildren().add(sectionLabel("📋  Lista de Videojuegos"));

        List<VideoGame> juegos = service.listarTodos();

        if (juegos.isEmpty()) {
            Label empty = fieldLabel("No hay videojuegos en el catálogo.");
            root.getChildren().add(empty);
            return root;
        }

        TableView<VideoGame> table = new TableView<>();
        table.setStyle("-fx-background-color:#1e1e3a;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<VideoGame,String>  colTitulo = col("Título",      vg -> vg.getTitle());
        TableColumn<VideoGame,String>  colPrecio = col("Precio Final",vg -> String.format("$%.2f", vg.calculateFinalPrice()));
        TableColumn<VideoGame,String>  colPlat   = col("Plataforma",  vg -> vg.getPlatform());
        TableColumn<VideoGame,String>  colGenero = col("Género",      vg -> vg.getGenre());
        TableColumn<VideoGame,String>  colStock  = col("Stock",       vg -> String.valueOf(vg.getStock()));
        TableColumn<VideoGame,String>  colTipo   = col("Tipo",        vg -> vg instanceof DigitalVideoGame ? "Digital" : "Físico");

        table.getColumns().addAll(colTitulo, colPrecio, colPlat, colGenero, colStock, colTipo);
        table.setItems(FXCollections.observableArrayList(juegos));

        root.getChildren().add(table);
        return root;
    }

    @SuppressWarnings("unchecked")
    private TableColumn<VideoGame,String> col(String header,
            java.util.function.Function<VideoGame,String> extractor) {
        TableColumn<VideoGame,String> c = new TableColumn<>(header);
        c.setCellValueFactory(d ->
            new javafx.beans.property.SimpleStringProperty(extractor.apply(d.getValue())));
        return c;
    }
}
