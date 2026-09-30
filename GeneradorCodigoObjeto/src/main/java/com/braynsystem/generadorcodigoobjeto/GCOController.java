package com.braynsystem.generadorcodigoobjeto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class GCOController {

    public StackPane contenedor;

    public void cargarVista(String archivo) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/braynsystem/generadorcodigoobjeto/" + archivo
                    )
            );

            Parent vista = loader.load();

            contenedor.getChildren().clear();
            contenedor.getChildren().add(vista);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrarAnalisis() {
        cargarVista("analisis.fxml");
    }

    public void mostrarIntermedio() {
        cargarVista("intermedio.fxml");
    }

    public void mostrarRegistros() {
        cargarVista("registros.fxml");
    }

    public void mostrarEnsamblador() {
        cargarVista("ensamblador.fxml");
    }

    public void mostrarMaquina() {
        cargarVista("maquina.fxml");
    }

    public void mostrarMemoria() {
        cargarVista("memoria.fxml");
    }

    public void mostrarSimbolos() {
        cargarVista("simbolos.fxml");
    }
}