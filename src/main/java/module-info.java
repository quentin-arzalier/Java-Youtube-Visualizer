open module ytVisualizer {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.media;
    requires org.jsoup;
    requires org.json;

    exports ytVisualizer;
    exports ytVisualizer.GUI;
}