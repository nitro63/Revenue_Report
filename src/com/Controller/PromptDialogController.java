package com.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.revenue_report.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import com.revenue_report.DBConnection;

import java.io.IOException;
import java.util.Objects;

public class PromptDialogController {
    /**
     * Constructor will pop up a new stage which will contain
     * type of error/notification(header) and its details.
     * @param header : Prompt headline
     * @param error : Description message of prompt
     */

    protected static Stage stage = new Stage();
    public PromptDialogController(String header, String error) {

        Stage stg = new Stage();
        stg.setAlwaysOnTop(true);

        //Modality is so that this window must be interacted before others
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setResizable(false);

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/Views/fxml/dialog.fxml")));
            Scene s = new Scene(root);

            //Getting useful nodes from FXML to set error report
            Label lblHeader = (Label) root.lookup("#lblHeader");
            JFXTextArea txtError = (JFXTextArea) root.lookup("#txtError");
            JFXButton btnClose = (JFXButton) root.lookup("#btnClose");

            lblHeader.setText(header);
            txtError.setText(error);

            //Setting close button event
            btnClose.setOnAction(event -> {
                stg.hide();
            });

            stg.setScene(s);
            stg.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public PromptDialogController(String header, String error, Boolean DB) {

        Stage stg = new Stage();
        stg.setAlwaysOnTop(true);

        //Modality is so that this window must be interacted before others
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.setResizable(false);

        try {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/Views/fxml/dialog.fxml")));
            Scene s = new Scene(root);

            //Getting useful nodes from FXML to set error report
            Label lblHeader = (Label) root.lookup("#lblHeader");
            JFXTextArea txtError = (JFXTextArea) root.lookup("#txtError");
            JFXButton btnClose = (JFXButton) root.lookup("#btnClose");

            lblHeader.setText(header);
            txtError.setText(error);

            //Setting close button event
            btnClose.setOnAction(event -> {
                stg.hide();
                if (DB){
                    if (Main.stage.isShowing()){
                        FXMLLoader connectConfig = new FXMLLoader();
                        connectConfig.setLocation(DBConnection.class.getResource("/com/Views/fxml/ConnectionConfiguration.fxml"));
                        try {
                            Parent rot = connectConfig.load();
                            Scene sw = new Scene(rot);
                            Stage stag = new Stage();
                            stag.setTitle("Connection Configuration");
                            stag.initModality(Modality.APPLICATION_MODAL);
                            stag.initOwner(Main.stage);
                            stag.initStyle(StageStyle.UTILITY);
                            stag.getIcons().add(new Image("/com/Assets/kmalogo.png"));
                            stag.setScene(sw);
                            stag.setResizable(false);
                            stag.show();
                            stag.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                public void handle(WindowEvent we) {
                                    Main.stage.show();
                                    /*Scene scene = null;
                                    Stage primaryStage = new Stage();
                                    Parent rut = null;
                                    try {
                                        FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/com/Views/fxml/Login.fxml"));
                                        firstLoader.setController(new LogInController());
                                        rut = firstLoader.load();
                                        scene = new Scene(rut);
                                        primaryStage.setTitle("Login Prompt");
                                        primaryStage.getIcons().add(new Image("/com/Assets/kmalogo.png"));
                                        primaryStage.setScene(scene);
                                        primaryStage.setResizable(false);
                                        primaryStage.show();
                                        stage = primaryStage;
                                    scene = new Scene(FXMLLoader.load(getClass().getResource("/Views/fxml/login.fxml")));
                                    String css = this.getClass().getResource("/main/resources/css/login.css").toExternalForm(); // Getting stylesheet
                                    scene.getStylesheets().add(css); // Adding stylesheet
                                    primaryStage.setTitle("Log In Prompt");
                                    primaryStage.setScene(scene);
                                    primaryStage.getIcons().add(new Image("/main/resources/icons/Accounts_main.png"));
                                    primaryStage.setResizable(false);
                                    primaryStage.show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }*/
                                }});
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                    Stage current = new Stage();
                    FXMLLoader connectConfig = new FXMLLoader();
                    connectConfig.setLocation(DBConnection.class.getResource("/com/Views/fxml/ConnectionConfiguration.fxml"));
                    try {
                        Parent rot = connectConfig.load();
                        Scene sw = new Scene(rot);
                        Stage stag = new Stage();
                        stag.setTitle("Connection Configuration");
                        stag.initModality(Modality.APPLICATION_MODAL);
                        stag.initOwner(current);
                        stag.initStyle(StageStyle.UTILITY);
                        stag.getIcons().add(new Image("/com/Assets/kmalogo.png"));
                        stag.setScene(sw);
                        stag.setResizable(false);
                        stag.show();
                        stag.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            public void handle(WindowEvent we) {
                                stage.hide();
                                Scene scene = null;
                                Stage primaryStage = new Stage();
                                Parent rut = null;
                                try {
                                    FXMLLoader firstLoader = new FXMLLoader(getClass().getResource("/com/Views/fxml/Login.fxml"));
                                    firstLoader.setController(new LogInController());
                                    rut = firstLoader.load();
                                    scene = new Scene(rut);
                                    primaryStage.setTitle("Login Prompt");
                                    primaryStage.getIcons().add(new Image("/com/Assets/kmalogo.png"));
                                    primaryStage.setScene(scene);
                                    primaryStage.setResizable(false);
                                    primaryStage.show();
                                    stage = primaryStage;/*
                                    scene = new Scene(FXMLLoader.load(getClass().getResource("/Views/fxml/login.fxml")));
                                    String css = this.getClass().getResource("/main/resources/css/login.css").toExternalForm(); // Getting stylesheet
                                    scene.getStylesheets().add(css); // Adding stylesheet
                                    primaryStage.setTitle("Log In Prompt");
                                    primaryStage.setScene(scene);
                                    primaryStage.getIcons().add(new Image("/main/resources/icons/Accounts_main.png"));
                                    primaryStage.setResizable(false);
                                    primaryStage.show();*/
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }});
                    } catch (IOException e) {
                        e.printStackTrace();
                    }}
                }


            });

            stg.setScene(s);
            stg.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
