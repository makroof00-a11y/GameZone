package gamezone.ui;

import gamezone.model.Sale;
import gamezone.service.VideoGameService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListarVentasView extends BaseView {

    private final VideoGameService service;

    public ListarVentasView(VideoGameService service, Stage owner) {
        super(owner, "Historial de Ventas", 820, 440);
        this.service = service;
    }

    @Override
    protected VBox buildContent() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(20));
        root.setStyle(BG);
        root.getChildren().add(sectionLabel("📊  Historial de Ventas"));

        List<Sale> ventas = service.listarVentas();

        if (ventas.isEmpty()) {
            root.getChildren().add(fieldLabel("No hay ventas registradas aún."));
            return root;
        }

        TableView<Sale> table = new TableView<>();
        table.setStyle("-fx-background-color:#1e1e3a;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        TableColumn<Sale,String> cId     = col("ID",        s -> s.getId());
        TableColumn<Sale,String> cJuego  = col("Juego",     s -> s.getVideoGame().getTitle());
        TableColumn<Sale,String> cQty    = col("Cantidad",  s -> String.valueOf(s.getQuantity()));
        TableColumn<Sale,String> cUnit   = col("Precio Unit", s -> String.format("$%.2f", s.getUnitPrice()));
        TableColumn<Sale,String> cTotal  = col("Total",     s -> String.format("$%.2f", s.getTotal()));
        TableColumn<Sale,String> cFecha  = col("Fecha",     s -> s.getSaleDate().format(fmt));

        table.getColumns().addAll(cId, cJuego, cQty, cUnit, cTotal, cFecha);
        table.setItems(FXCollections.observableArrayList(ventas));


        double totalGeneral = ventas.stream().mapToDouble(Sale::getTotal).sum();
        Label lblTotal = fieldLabel(String.format("Total recaudado: $%.2f  |  Ventas: %d",
            totalGeneral, ventas.size()));

        root.getChildren().addAll(table, lblTotal);
        return root;
    }

    private TableColumn<Sale,String> col(String h, java.util.function.Function<Sale,String> ex) {
        TableColumn<Sale,String> c = new TableColumn<>(h);
        c.setCellValueFactory(d -> new SimpleStringProperty(ex.apply(d.getValue())));
        return c;
    }
}
