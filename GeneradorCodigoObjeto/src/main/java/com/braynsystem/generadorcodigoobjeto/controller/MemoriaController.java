package com.braynsystem.generadorcodigoobjeto.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class MemoriaController {

    @FXML
    private TableView<?> tablaMemoria;

    @FXML
    private void reservarMemoria() {

        System.out.println("Reservando memoria...");

    }
}