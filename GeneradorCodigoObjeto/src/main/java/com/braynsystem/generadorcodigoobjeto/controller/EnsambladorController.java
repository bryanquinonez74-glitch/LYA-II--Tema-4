package com.braynsystem.generadorcodigoobjeto.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class EnsambladorController {

    @FXML
    private TextArea txtIntermedio;

    @FXML
    private TextArea txtEnsamblador;

    @FXML
    private void generarEnsamblador() {

        txtEnsamblador.setText(
                "MOV R1, #10\n" +
                        "MOV R2, #20\n" +
                        "ADD R3, R1, R2"
        );
    }
}