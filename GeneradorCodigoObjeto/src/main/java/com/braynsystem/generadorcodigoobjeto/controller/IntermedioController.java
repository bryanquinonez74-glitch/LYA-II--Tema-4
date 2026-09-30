package com.braynsystem.generadorcodigoobjeto.controller;

import com.braynsystem.generadorcodigoobjeto.compiler.GeneradorIntermedio;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class IntermedioController {

    @FXML
    private TextArea txtCodigoFuente;

    @FXML
    private TextArea txtCodigoIntermedio;

    private GeneradorIntermedio generador;

    @FXML
    public void initialize() {
        generador = new GeneradorIntermedio();
    }

    @FXML
    private void generarIntermedio() {

        String codigo = txtCodigoFuente.getText();

        if (codigo == null || codigo.isBlank()) {

            txtCodigoIntermedio.setText(
                    "⚠ No se ha introducido código fuente."
            );

            return;
        }

        generador.generar(codigo);

        String resultado = generador.obtenerCodigo();

        if (resultado.isBlank()) {

            txtCodigoIntermedio.setText(
                    "⚠ No se pudieron generar instrucciones."
            );

            return;
        }

        txtCodigoIntermedio.setText(resultado);
    }

    @FXML
    private void limpiar() {

        txtCodigoFuente.clear();
        txtCodigoIntermedio.clear();
    }
}