package pl.kielce.metody.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UI_file.fxml"));

            Scene scena = new Scene(root);

            scena.getStylesheets().add(this.getClass().getResource("style.css").toExternalForm());
            primaryStage.getIcons().add(new Image("resources/icon.png"));
            primaryStage.setScene(scena);
            primaryStage.show();

            // fix resizable bug on ubuntu 14.04
            primaryStage.setMaxHeight(400);
            primaryStage.setMaxWidth(600);
            primaryStage.setMinHeight(400);
            primaryStage.setMinWidth(600);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
