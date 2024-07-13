module berliano.uas {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens berliano.uas to javafx.fxml;
    opens berliano.uas.model to javafx.base;
    opens berliano.uas.controller to javafx.fxml;

    exports berliano.uas;
    exports berliano.uas.controller;
}
