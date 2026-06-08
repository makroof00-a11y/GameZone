module gamezone {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens gamezone.ui    to javafx.graphics, javafx.fxml;
    opens gamezone.model to javafx.base;

    exports gamezone.ui;
    exports gamezone.model;
    exports gamezone.service;
    exports gamezone.repository;
    exports gamezone.interfaces;
}
