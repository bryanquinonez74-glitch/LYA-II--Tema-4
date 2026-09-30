module com.brayansystem.analizadorsemantico {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    exports com.brayansystem.analizadorsemantico;

    exports com.brayansystem.analizadorsemantico.controller;

    opens com.brayansystem.analizadorsemantico.controller
            to javafx.fxml;
}