package com.brayansystem.analizadorsemantico.controller;

import com.brayansystem.analizadorsemantico.lexer.AnalizadorLexico;
import com.brayansystem.analizadorsemantico.modelo.ErrorSemantico;
import com.brayansystem.analizadorsemantico.modelo.Simbolo;
import com.brayansystem.analizadorsemantico.modelo.Token;
import com.brayansystem.analizadorsemantico.semantico.AnalizadorSemantico;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.util.List;

public class AnalizadorSController {

    @FXML
    private TextArea txtCodigo;

    @FXML
    private TextArea txtResultado;

    @FXML
    private TableView<Token> tablaTokens;

    @FXML
    private TableColumn<Token, String> colLexema;

    @FXML
    private TableColumn<Token, String> colTipo;

    @FXML
    private TableColumn<Token, String> colLinea;

    @FXML
    private TableColumn<Token, String> colColumna;

    @FXML
    private TableView<Simbolo> tablaSimbolos;

    @FXML
    private TableColumn<Simbolo, String> colNombreSimbolo;

    @FXML
    private TableColumn<Simbolo, String> colTipoSimbolo;

    @FXML
    private TableColumn<Simbolo, String> colValorSimbolo;

    @FXML
    private TableColumn<Simbolo, String> colLineaSimbolo;

    @FXML
    private Button btnAnalizar;

    @FXML
    private Button btnLimpiar;

    private final ObservableList<Token> listaTokens =
            FXCollections.observableArrayList();

    private final ObservableList<Simbolo> listaSimbolos =
            FXCollections.observableArrayList();

    private final AnalizadorLexico analizadorLexico =
            new AnalizadorLexico();

    private final AnalizadorSemantico analizadorSemantico =
            new AnalizadorSemantico();

    @FXML
    public void initialize() {

        configurarTablaTokens();
        configurarTablaSimbolos();

        tablaTokens.setItems(listaTokens);
        tablaSimbolos.setItems(listaSimbolos);
    }

    private void configurarTablaTokens() {

        colLexema.setCellValueFactory(
                datos ->
                        new SimpleStringProperty(
                                datos.getValue().getLexema()
                        )
        );

        colTipo.setCellValueFactory(
                datos ->
                        new SimpleStringProperty(
                                datos.getValue().getTipo()
                        )
        );

        colLinea.setCellValueFactory(
                datos ->
                        new SimpleStringProperty(
                                String.valueOf(
                                        datos.getValue().getLinea()
                                )
                        )
        );

        colColumna.setCellValueFactory(
                datos ->
                        new SimpleStringProperty(
                                String.valueOf(
                                        datos.getValue().getColumna()
                                )
                        )
        );
    }

    private void configurarTablaSimbolos() {

        colNombreSimbolo.setCellValueFactory(
                datos ->
                        new SimpleStringProperty(
                                datos.getValue().getNombre()
                        )
        );

        colTipoSimbolo.setCellValueFactory(
                datos ->
                        new SimpleStringProperty(
                                String.valueOf(
                                        datos.getValue().getTipo()
                                )
                        )
        );

        colValorSimbolo.setCellValueFactory(
                datos ->
                        new SimpleStringProperty(
                                String.valueOf(
                                        datos.getValue().getValor()
                                )
                        )
        );

        colLineaSimbolo.setCellValueFactory(
                datos ->
                        new SimpleStringProperty(
                                String.valueOf(
                                        datos.getValue().getLinea()
                                )
                        )
        );
    }

    @FXML
    private void analizarCodigo() {

        String codigo = txtCodigo.getText();

        listaTokens.clear();
        listaSimbolos.clear();
        txtResultado.clear();

        if (codigo == null || codigo.isBlank()) {

            txtResultado.setText(
                    "No hay código para analizar."
            );

            return;
        }

        try {

            List<Token> tokens =
                    analizadorLexico.analizar(codigo);

            listaTokens.addAll(tokens);

            analizadorSemantico.analizar(tokens);

            listaSimbolos.addAll(
                    analizadorSemantico
                            .getTablaSimbolos()
                            .getSimbolos()
            );

            StringBuilder resultado =
                    new StringBuilder();

            resultado.append(
                    "ANÁLISIS DEL CÓDIGO\n"
            );



            resultado.append(
                    "ANÁLISIS LÉXICO\n"
            );

            resultado.append(
                    "Tokens encontrados: "
                            + tokens.size()
                            + "\n\n"
            );

            resultado.append(
                    "TABLA DE SÍMBOLOS\n"
            );

            resultado.append(
                    "Símbolos encontrados: "
                            + listaSimbolos.size()
                            + "\n\n"
            );

            List<ErrorSemantico> errores =
                    analizadorSemantico.getErrores();

            if (errores.isEmpty()) {

                resultado.append(
                        "ANÁLISIS SINTÁCTICO\n"
                );

                resultado.append(
                        "Sintaxis correcta.\n\n"
                );

                resultado.append(
                        "ANÁLISIS SEMÁNTICO\n"
                );

                resultado.append(
                        "No se encontraron errores semánticos.\n"
                );

            } else {

                resultado.append(
                        "ERRORES ENCONTRADOS\n"
                );

                for (ErrorSemantico error :
                        errores) {

                    resultado.append(
                            "✗ "
                    );

                    resultado.append(
                            error.toString()
                    );

                    resultado.append("\n");
                }
            }

            txtResultado.setText(
                    resultado.toString()
            );

        } catch (Exception e) {

            txtResultado.setText(
                    "Error durante el análisis:\n\n"
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void limpiar() {

        txtCodigo.clear();
        txtResultado.clear();

        listaTokens.clear();
        listaSimbolos.clear();

        analizadorSemantico.limpiar();
    }
}