module com.braynsystem.generadorcodigoobjeto {

    requires javafx.controls;
    requires javafx.fxml;

    opens com.braynsystem.generadorcodigoobjeto
            to javafx.fxml;

    opens com.braynsystem.generadorcodigoobjeto.controller
            to javafx.fxml;

    opens com.braynsystem.generadorcodigoobjeto.model
            to javafx.base;

    exports com.braynsystem.generadorcodigoobjeto;

    exports com.braynsystem.generadorcodigoobjeto.controller;

    exports com.braynsystem.generadorcodigoobjeto.model;
}