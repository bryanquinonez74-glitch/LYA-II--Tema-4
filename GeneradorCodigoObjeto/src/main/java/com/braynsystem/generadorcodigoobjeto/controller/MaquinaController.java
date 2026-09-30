package com.braynsystem.generadorcodigoobjeto.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class MaquinaController {

    @FXML
    private TextArea txtEnsamblador;

    @FXML
    private TextArea txtMaquina;

    @FXML
    private void generarMaquina() {

        txtMaquina.setText(
                "0001 0001 1010\n" +
                        "0001 0010 1010\n" +
                        "0010 0011 0001 0010"
        );
    }
}