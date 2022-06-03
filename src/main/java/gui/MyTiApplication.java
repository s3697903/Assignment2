package main.java.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class MyTiApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyTiApplication.class.getClassLoader().getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 490);
        stage.setTitle("MyTi!");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest((event) -> {
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}
