package gamezone.ui;

import gamezone.model.Sale;
import gamezone.service.VideoGameService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VenderView extends BaseView {

    private final VideoGameService service;

    public VenderView(VideoGameService service, Stage owner) {
        super(owner, "Realizar Venta", 500, 380);
        this.service = service;
    }

    @Override
    protected VBox buildContent() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));
        root.setStyle(BG);
        root.getChildren().add(sectionLabel("🛒  Realizar Venta"));

        // ── Campos ──
        TextField tfTitulo = styledTF("Título del juego...");
        TextField tfCantidad = styledTF("Cantidad...");

        GridPane form = new GridPane();
        form.setHgap(12); form.setVgap(10);
        form.add(fieldLabel("Título:"),   0, 0); form.add(tfTitulo,   1, 0);
        form.add(fieldLabel("Cantidad:"), 0, 1); form.add(tfCantidad, 1, 1);

        // ── Resultado ──
        TextArea resultado = new TextArea();
        resultado.setEditable(false);
        resultado.setStyle("-fx-control-inner-background:#1e1e3a;-fx-text-fill:#00ff99;");
        resultado.setFont(Font.font("Courier New", 13));
        resultado.setPrefRowCount(6);

        // boton
        Button btnVender = new Button("💳  Confirmar Venta");
        btnVender.setStyle(BTN_PRIMARY);
        btnVender.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btnVender.setPrefWidth(280);
        btnVender.setPrefHeight(40);

        btnVender.setOnAction(e -> {
            String titulo = tfTitulo.getText().trim();
            String cantStr = tfCantidad.getText().trim();

            if (titulo.isEmpty() || cantStr.isEmpty()) {
                showError("Completa todos los campos.");
                return;
            }

            int qty;
            try { qty = Integer.parseInt(cantStr); }
            catch (NumberFormatException ex) { showError("La cantidad debe ser un número entero."); return; }

            if (qty <= 0) { showError("La cantidad debe ser mayor a 0."); return; }

            try {
                Sale sale = service.venderVideojuego(titulo, qty);
                resultado.setText(
                    "✅ VENTA REGISTRADA\n" +
                    "─────────────────────────────\n" +
                    "ID Venta   : " + sale.getId() + "\n" +
                    "Juego      : " + sale.getVideoGame().getTitle() + "\n" +
                    "Cantidad   : " + sale.getQuantity() + "\n" +
                    "Precio unit: $" + String.format("%.2f", sale.getUnitPrice()) + "\n" +
                    "TOTAL      : $" + String.format("%.2f", sale.getTotal()) + "\n" +
                    "Fecha      : " + sale.getSaleDate().toString().replace("T", " ").substring(0, 19)
                );
                tfTitulo.clear();
                tfCantidad.clear();
            } catch (IllegalArgumentException ex) {
                // Alerta UI si no se puede realizar la venta
                showWarning("⚠️ " + ex.getMessage());
                resultado.setText("❌ Venta no realizada:\n" + ex.getMessage());
            }
        });

        root.getChildren().addAll(form, btnVender, resultado);
        return root;
    }

    private TextField styledTF(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(FIELD_STYLE);
        tf.setPrefWidth(280);
        return tf;
    }
}
