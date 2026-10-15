package com.example.proyectocrudg3.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductoApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProductoApplication.class.getResource("/com/example/proyectocrudg3/productos-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Ventana registro Producto");
        stage.setScene(scene);
        stage.show();
    }
}
