/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revenue_report;

import com.Controller.LogInController;
import com.Controller.Reports.MonthlyReportController;
import com.Controller.PromptDialogController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

/**
 *
 * @author NiTrO
 */
public class Main extends Application {

    public static Stage stage;
    static final Logger logger = Logger.getLogger(MonthlyReportController.class.getName());
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = null;
        PropertyConfigurator.configure(getClass().getResource("log4j.properties"));
        try {
            FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/com/Views/fxml/Login.fxml"));
            firstLoader.setController(new LogInController());
            root = firstLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Login Prompt");
            stage.getIcons().add(new Image("/com/Assets/kmalogo.png"));
            stage.setScene(scene);
            stage.show();
            Main.stage = stage;
        } catch (IOException e) {
            logger.error(e);
            new PromptDialogController("Error!", "Error Occured. Failed to initialize system.");
        }
    }

   /* public void initial(Stage stage) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/Views/fxml/Login.fxml"));
        firstLoader.setController(new LogInController());
        Parent root = firstLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Login Prompt");
        stage.getIcons().add(new Image("/com.Assets/kmalogo.png"));
        stage.setScene(scene);
        stage.show();
    }*/

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
