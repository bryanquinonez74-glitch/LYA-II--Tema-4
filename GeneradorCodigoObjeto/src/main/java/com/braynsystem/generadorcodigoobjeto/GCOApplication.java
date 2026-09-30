package com.braynsystem.generadorcodigoobjeto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GCOApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GCOApplication.class.getResource("Dashboar-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("Generador de Código Objeto");
        stage.setScene(scene);
        stage.show();
    }
}
