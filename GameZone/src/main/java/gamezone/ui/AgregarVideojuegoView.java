package gamezone.ui;

import gamezone.model.DigitalVideoGame;
import gamezone.model.PhysicalVideoGame;
import gamezone.model.VideoGame;
import gamezone.service.VideoGameService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

/**
 * Vista de CRUD: Agregar, Listar, Actualizar, Eliminar videojuegos.
 */
public class AgregarVideojuegoView extends BaseView {

    private final VideoGameService service;
    private TableView<VideoGame> table;
    private TextField tfTitulo, tfPrecio, tfPlatforma, tfStock, tfGenero;
    private TextField tfExtra1, tfExtra2;          // campos dinámicos
    private ComboBox<String> cbTipo;
    private Label lblExtra1, lblExtra2;

    public AgregarVideojuegoView(VideoGameService service, Stage owner) {
        super(owner, "Gestión de Videojuegos", 900, 650);
        this.service = service;
    }

    @Override
    protected VBox buildContent() {
        VBox root = new VBox(14);
        root.setPadding(new Insets(20));
        root.setStyle(BG);

        // ── Título ──
        root.getChildren().add(sectionLabel("🎮  Gestión de Videojuegos"));

        // ── Formulario ──
        GridPane form = buildForm();

        // ── Botones CRUD ──
        HBox btnRow = new HBox(10);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        Button btnAgregar    = styledBtn("➕ Agregar",    BTN_PRIMARY);
        Button btnActualizar = styledBtn("✏️ Actualizar", BTN_PRIMARY);
        Button btnEliminar   = styledBtn("🗑️ Eliminar",  BTN_DANGER);
        Button btnLimpiar    = styledBtn("🔄 Limpiar",   "-fx-background-color:#2a2a4a;-fx-text-fill:#aaaacc;-fx-border-radius:5;-fx-background-radius:5;-fx-cursor:hand;");

        btnAgregar.setOnAction(e -> agregarJuego());
        btnActualizar.setOnAction(e -> actualizarJuego());
        btnEliminar.setOnAction(e -> eliminarJuego());
        btnLimpiar.setOnAction(e -> limpiarForm());

        btnRow.getChildren().addAll(btnAgregar, btnActualizar, btnEliminar, btnLimpiar);

        // ── Tabla ──
        table = buildTable();
        refreshTable();

        root.getChildren().addAll(form, btnRow, new Separator(), table);
        return root;
    }

    // ─────────────────────────────────────────────
    //  Formulario
    // ─────────────────────────────────────────────
    private GridPane buildForm() {
        GridPane g = new GridPane();
        g.setHgap(12); g.setVgap(8);

        tfTitulo   = styledField("Ej: The Last of Us");
        tfPrecio   = styledField("Ej: 150000");
        tfPlatforma = styledField("Ej: PC");
        tfStock    = styledField("Ej: 10");
        tfGenero   = styledField("Ej: Acción");
        tfExtra1   = styledField("");
        tfExtra2   = styledField("");
        lblExtra1  = fieldLabel("Tamaño GB");
        lblExtra2  = fieldLabel("Plataforma Descarga");

        cbTipo = new ComboBox<>(FXCollections.observableArrayList("Digital", "Físico"));
        cbTipo.setValue("Digital");
        cbTipo.setStyle(FIELD_STYLE);
        cbTipo.setPrefWidth(200);
        cbTipo.setOnAction(e -> updateExtraLabels());

        // Fila 0: Tipo
        g.add(fieldLabel("Tipo:"),    0, 0); g.add(cbTipo,      1, 0);
        g.add(fieldLabel("Título:"),  2, 0); g.add(tfTitulo,    3, 0);
        // Fila 1
        g.add(fieldLabel("Precio:"),   0, 1); g.add(tfPrecio,   1, 1);
        g.add(fieldLabel("Plataforma:"),2,1); g.add(tfPlatforma,3, 1);
        // Fila 2
        g.add(fieldLabel("Stock:"),   0, 2); g.add(tfStock,     1, 2);
        g.add(fieldLabel("Género:"),  2, 2); g.add(tfGenero,    3, 2);
        // Fila 3: campos extra dinámicos
        g.add(lblExtra1, 0, 3); g.add(tfExtra1, 1, 3);
        g.add(lblExtra2, 2, 3); g.add(tfExtra2, 3, 3);

        return g;
    }

    private void updateExtraLabels() {
        if ("Digital".equals(cbTipo.getValue())) {
            lblExtra1.setText("Tamaño (GB):");
            lblExtra2.setText("Plataforma descarga:");
            tfExtra1.setPromptText("Ej: 45");
            tfExtra2.setPromptText("Ej: Steam");
        } else {
            lblExtra1.setText("Condición:");
            lblExtra2.setText("Distribuidor:");
            tfExtra1.setPromptText("nuevo / usado");
            tfExtra2.setPromptText("Ej: Sony");
        }
    }

    // ─────────────────────────────────────────────
    //  Tabla
    // ─────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private TableView<VideoGame> buildTable() {
        TableView<VideoGame> tv = new TableView<>();
        tv.setStyle("-fx-background-color:#1e1e3a;-fx-text-fill:#e0e0ff;");
        tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<VideoGame,String> colTitulo   = col("Título",     "title");
        TableColumn<VideoGame,Double> colPrecio   = col2("Precio",    "price");
        TableColumn<VideoGame,String> colPlat     = col("Plataforma", "platform");
        TableColumn<VideoGame,Integer>colStock    = col3("Stock",     "stock");
        TableColumn<VideoGame,String> colGenero   = col("Género",     "genre");

        // Columna tipo
        TableColumn<VideoGame,String> colTipo = new TableColumn<>("Tipo");
        colTipo.setCellValueFactory(d ->
            new javafx.beans.property.SimpleStringProperty(
                d.getValue() instanceof DigitalVideoGame ? "Digital" : "Físico"));

        tv.getColumns().addAll(colTitulo, colPrecio, colPlat, colStock, colGenero, colTipo);

        // Al hacer clic carga el juego en el formulario
        tv.setOnMouseClicked(e -> {
            VideoGame sel = tv.getSelectionModel().getSelectedItem();
            if (sel != null) cargarEnForm(sel);
        });

        return tv;
    }

    private void refreshTable() {
        List<VideoGame> list = service.listarTodos();
        table.setItems(FXCollections.observableArrayList(list));
    }

    // ─────────────────────────────────────────────
    //  Acciones
    // ─────────────────────────────────────────────
    private void agregarJuego() {
        try {
            VideoGame vg = buildFromForm();
            service.agregarVideojuego(vg);
            showInfo("✅ Videojuego agregado correctamente.");
            limpiarForm();
            refreshTable();
        } catch (IllegalArgumentException ex) {
            // Si el título duplicado → ventana emergente específica
            if (ex.getMessage().contains("ya existe")) {
                showWarning(ex.getMessage());
            } else {
                showError(ex.getMessage());
            }
        }
    }

    private void actualizarJuego() {
        String titulo = tfTitulo.getText().trim();
        if (titulo.isEmpty()) { showError("Ingresa el título del juego a actualizar."); return; }
        try {
            VideoGame updated = buildFromForm();
            boolean ok = service.actualizarVideojuego(titulo, updated);
            if (ok) { showInfo("✅ Juego actualizado."); refreshTable(); limpiarForm(); }
            else    { showWarning("No se encontró el juego '" + titulo + "'."); }
        } catch (IllegalArgumentException ex) { showError(ex.getMessage()); }
    }

    private void eliminarJuego() {
        String titulo = tfTitulo.getText().trim();
        if (titulo.isEmpty()) { showError("Ingresa el título del juego a eliminar."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "¿Eliminar '" + titulo + "'?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.initOwner(stage);
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.YES) {
                boolean ok = service.eliminarVideojuego(titulo);
                if (ok) { showInfo("🗑️ Juego eliminado."); refreshTable(); limpiarForm(); }
                else    { showWarning("No se encontró el juego."); }
            }
        });
    }

    private void limpiarForm() {
        tfTitulo.clear(); tfPrecio.clear(); tfPlatforma.clear();
        tfStock.clear(); tfGenero.clear(); tfExtra1.clear(); tfExtra2.clear();
        cbTipo.setValue("Digital");
        updateExtraLabels();
    }

    private VideoGame buildFromForm() {
        String titulo    = tfTitulo.getText().trim();
        double precio    = Double.parseDouble(tfPrecio.getText().trim());
        String plat      = tfPlatforma.getText().trim();
        int    stock     = Integer.parseInt(tfStock.getText().trim());
        String genero    = tfGenero.getText().trim();

        if ("Digital".equals(cbTipo.getValue())) {
            double sizeGB = Double.parseDouble(tfExtra1.getText().trim());
            String dlPlat  = tfExtra2.getText().trim();
            return new DigitalVideoGame(titulo, precio, plat, stock, genero, sizeGB, dlPlat);
        } else {
            String cond  = tfExtra1.getText().trim();
            String dist  = tfExtra2.getText().trim();
            return new PhysicalVideoGame(titulo, precio, plat, stock, genero, cond, dist);
        }
    }

    private void cargarEnForm(VideoGame vg) {
        tfTitulo.setText(vg.getTitle());
        tfPrecio.setText(String.valueOf(vg.getPrice()));
        tfPlatforma.setText(vg.getPlatform());
        tfStock.setText(String.valueOf(vg.getStock()));
        tfGenero.setText(vg.getGenre());
        if (vg instanceof DigitalVideoGame d) {
            cbTipo.setValue("Digital");
            tfExtra1.setText(String.valueOf(d.getSizeGB()));
            tfExtra2.setText(d.getDownloadPlatform());
        } else if (vg instanceof PhysicalVideoGame p) {
            cbTipo.setValue("Físico");
            tfExtra1.setText(p.getCondition());
            tfExtra2.setText(p.getDistributor());
        }
        updateExtraLabels();
    }

    // ─────────────────────────────────────────────
    //  Helpers UI
    // ─────────────────────────────────────────────
    private TextField styledField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.setStyle(FIELD_STYLE);
        tf.setPrefWidth(190);
        return tf;
    }

    private Button styledBtn(String text, String style) {
        Button btn = new Button(text);
        btn.setPrefHeight(36);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        btn.setStyle(style);
        return btn;
    }

    private <T> TableColumn<VideoGame,T> col(String header, String prop) {
        TableColumn<VideoGame,T> c = new TableColumn<>(header);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        return c;
    }
    private TableColumn<VideoGame,Double> col2(String h, String p) { return col(h,p); }
    private TableColumn<VideoGame,Integer> col3(String h, String p) { return col(h,p); }
}
