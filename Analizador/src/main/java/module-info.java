module com.braynsystem.analizador {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.braynsystem.analizador to javafx.fxml;
    exports com.braynsystem.analizador;
}