module com.brayansystem.analizadorsemantico {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    exports com.brayansystem.analizadorsemantico;
    exports com.brayansystem.analizadorsemantico.controller;
    exports com.brayansystem.analizadorsemantico.modelo;
    exports com.brayansystem.analizadorsemantico.lexer;
    exports com.brayansystem.analizadorsemantico.parser;
    exports com.brayansystem.analizadorsemantico.ast;
    exports com.brayansystem.analizadorsemantico.semantico;

    opens com.brayansystem.analizadorsemantico.controller
            to javafx.fxml;
}