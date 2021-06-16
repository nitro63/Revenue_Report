/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revenue_report;

import Controller.LogInController;
import Controller.MonthlyReportController;
import Controller.PromptDialogController;
import Controller.appController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author NiTrO
 */
public class Main extends Application {

    public Stage stage;
    static final Logger logger = Logger.getLogger(MonthlyReportController.class.getName());
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Parent root = null;
        PropertyConfigurator.configure(getClass().getResource("/Controller/log4j.properties"));
        try {
            FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/Views/fxml/Login.fxml"));
            firstLoader.setController(new LogInController());
            root = firstLoader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Login Prompt");
            stage.getIcons().add(new Image("/Assets/kmalogo.png"));
            stage.setScene(scene);
            stage.show();
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
        stage.getIcons().add(new Image("/Assets/kmalogo.png"));
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
