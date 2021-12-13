module com.example.lokaverkefni_21 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.lokaverkefni_21 to javafx.fxml;
    exports com.example.lokaverkefni_21;
}