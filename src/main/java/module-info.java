module psgra.sgra {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.sql;

    opens psgra.sgra.ui to javafx.fxml;
    exports psgra.sgra;
}