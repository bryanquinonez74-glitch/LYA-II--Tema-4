package com.braynsystem.generadorcodigoobjeto.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class SimbolosController {

    @FXML
    private TableView<?> tablaSimbolos;

    @FXML
    private void actualizarTabla() {

        System.out.println("Actualizando tabla de símbolos...");

    }
}