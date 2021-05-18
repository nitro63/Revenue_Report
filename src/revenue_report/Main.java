/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revenue_report;

import Controller.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author NiTrO
 */
public class Main extends Application {

public Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/Views/fxml/Login.fxml"));
        firstLoader.setController(new LogInController());
        Parent root = firstLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Login Prompt");
        stage.getIcons().add(new Image("/Assets/kmalogo.png"));
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
