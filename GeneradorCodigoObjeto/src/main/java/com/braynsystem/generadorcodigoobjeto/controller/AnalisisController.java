package com.braynsystem.generadorcodigoobjeto.controller;

import com.braynsystem.generadorcodigoobjeto.compiler.AnalizadorSemantico;
import com.braynsystem.generadorcodigoobjeto.model.Variable;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class AnalisisController {

    @FXML
    private TextArea txtCodigo;

    @FXML
    private Label lblResultado;

    @FXML
    private TableView<Variable> tablaSimbolos;

    @FXML
    private TableColumn<Variable, String> colNombre;

    @FXML
    private TableColumn<Variable, String> colTipo;

    @FXML
    private TableColumn<Variable, String> colValor;

    @FXML
    private TableColumn<Variable, String> colMemoria;

    @FXML
    private TableColumn<Variable, String> colRegistro;

    private AnalizadorSemantico analizador;

    @FXML
    public void initialize() {

        analizador = new AnalizadorSemantico();

        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre")
        );

        colTipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo")
        );

        colValor.setCellValueFactory(
                new PropertyValueFactory<>("valor")
        );

        colMemoria.setCellValueFactory(
                new PropertyValueFactory<>("direccionMemoria")
        );

        colRegistro.setCellValueFactory(
                new PropertyValueFactory<>("registro")
        );
    }

    @FXML
    private void analizarCodigo() {

        String codigo = txtCodigo.getText();

        if (codigo == null || codigo.isBlank()) {

            lblResultado.setText(
                    "⚠ No se ha introducido código."
            );

            tablaSimbolos.getItems().clear();

            return;
        }

        analizador.analizar(codigo);

        tablaSimbolos.setItems(
                FXCollections.observableArrayList(
                        analizador.getVariables()
                )
        );

        if (analizador.esValido()) {

            lblResultado.setText(
                    "✓ Análisis semántico correcto. " +
                            "No se encontraron errores."
            );

        } else {

            StringBuilder mensaje =
                    new StringBuilder();

            mensaje.append(
                    "✗ Se encontraron errores:\n"
            );

            for (String error :
                    analizador.getErrores()) {

                mensaje.append("• ")
                        .append(error)
                        .append("\n");
            }

            lblResultado.setText(
                    mensaje.toString()
            );
        }
    }

    @FXML
    private void limpiar() {

        txtCodigo.clear();

        lblResultado.setText(
                "Resultado del análisis"
        );

        tablaSimbolos.getItems().clear();
    }
}