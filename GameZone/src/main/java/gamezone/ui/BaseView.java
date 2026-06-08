package gamezone.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;


public abstract class BaseView {

    protected final Stage stage;
    protected final Stage owner;
    protected static final String BG = "-fx-background-color: #0d0d1a;";
    protected static final String FIELD_STYLE =
        "-fx-background-color: #1e1e3a;" +
        "-fx-text-fill: #e0e0ff;" +
        "-fx-border-color: #00e5ff55;" +
        "-fx-border-width: 1;" +
        "-fx-border-radius: 4;" +
        "-fx-background-radius: 4;" +
        "-fx-prompt-text-fill: #555577;";
    protected static final String BTN_PRIMARY =
        "-fx-background-color: #00e5ff;" +
        "-fx-text-fill: #0d0d1a;" +
        "-fx-font-weight: bold;" +
        "-fx-border-radius: 5;" +
        "-fx-background-radius: 5;" +
        "-fx-cursor: hand;";
    protected static final String BTN_DANGER =
        "-fx-background-color: #ff4466;" +
        "-fx-text-fill: #ffffff;" +
        "-fx-font-weight: bold;" +
        "-fx-border-radius: 5;" +
        "-fx-background-radius: 5;" +
        "-fx-cursor: hand;";

    public BaseView(Stage owner, String title, int w, int h) {
        this.owner = owner;
        this.stage = new Stage();
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("GameZone – " + title);
        stage.setResizable(false);

        VBox root = buildContent();
        root.setStyle(BG);
        root.setPadding(new Insets(24));
        root.setSpacing(14);

        Scene scene = new Scene(root, w, h);
        stage.setScene(scene);
    }

    protected abstract VBox buildContent();

    public void show() { stage.show(); }

    // Alertas

    protected void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.initOwner(stage);
        a.showAndWait();
    }

    protected void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Información");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.initOwner(stage);
        a.showAndWait();
    }

    protected void showWarning(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle("Advertencia");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.initOwner(stage);
        a.showAndWait();
    }


    protected javafx.scene.control.Label sectionLabel(String text) {
        var lbl = new javafx.scene.control.Label(text);
        lbl.setFont(Font.font("Arial Black", FontWeight.BOLD, 18));
        lbl.setTextFill(Color.web("#00e5ff"));
        return lbl;
    }

    protected javafx.scene.control.Label fieldLabel(String text) {
        var lbl = new javafx.scene.control.Label(text);
        lbl.setTextFill(Color.web("#aaaacc"));
        lbl.setFont(Font.font("Arial", 13));
        return lbl;
    }
}
