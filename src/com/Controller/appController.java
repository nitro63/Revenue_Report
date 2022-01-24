/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import com.Controller.Gets.GetRevCenter;
import javafx.stage.Stage;
import com.revenue_report.Main;

/**
 *
 * @author NiTrO
 */
//public class appController  {
public class appController  implements Initializable{
    
    
    @FXML
    public BorderPane border_pane;
    @FXML
    private VBox centerPane;
    @FXML
    private VBox leftPane;
    @FXML
    private MenuButton btnUserMenu;
    @FXML
    private Label lblDashboard;
    @FXML
    private Label lblAdministrative;
    @FXML
    private Label lblReports;
    @FXML
    private Label lblEntries;
    @FXML
    private ImageView imgUser;

    @FXML
    private MenuItem menuItemViewProfile;

    @FXML
    private MenuItem menuItemLogout;

    @FXML
    private JFXHamburger btnBurger;

    @FXML
    private Button btnSDHME;
    @FXML
    private Button btnSGRE;
    @FXML
    private Button btnSDRR;
    @FXML
    private Button btnSDRI;
    @FXML
    private Button btnSDHelp;
    @FXML
    private ImageView imgAvatar;
    @FXML
    private Label txtUser;
    @FXML
    private Button btnLogout;
    GetRevCenter GetCenter = new GetRevCenter();
    HamburgerBasicCloseTransition transition;
    ObservableList<Button> menuButtons = FXCollections.observableArrayList();
    LogInController getLogin ;/*
     InitializerController app;
     public void setappController(InitializerController app){
         this.app = app;
     }*/

public  appController() {
         }
     public VBox getCenterPane(){
         return centerPane;
     }
    
      

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    menuButtons.addAll(Arrays.asList(btnSDHME,btnSDRI,btnSDRR,btnSGRE));
    menuItemViewProfile.setDisable(true);
    transition = new HamburgerBasicCloseTransition(btnBurger);
        border_pane.widthProperty().addListener(((observable, oldValue, newValue) -> {
            sideBarResize();
        }));
        transition.setRate(-1);
    btnBurger.setOnMousePressed(e ->{
        transition.setRate(transition.getRate()*-1);
        transition.play();
        if (transition.getRate() > 0){
            leftPane.setMinWidth(168);
            leftPane.setMaxWidth(168);
            leftPane.setPrefWidth(168);
            btnSDHME.setPrefWidth(168);
            btnSDRI.setPrefWidth(168);
            btnSDRR.setPrefWidth(168);
            btnSGRE.setPrefWidth(168);
            lblDashboard.setVisible(true);
            lblAdministrative.setVisible(LogInController.OverAllAdmin);
            lblEntries.setVisible(true);
            lblReports.setVisible(true);
        }else {
            leftPane.setMinWidth(51);
            leftPane.setMaxWidth(51);
            leftPane.setPrefWidth(51);
            btnSDHME.setPrefWidth(51);
            btnSDRI.setPrefWidth(51);
            btnSDRR.setPrefWidth(51);
            btnSGRE.setPrefWidth(51);
            lblDashboard.setVisible(false);
            lblAdministrative.setVisible(false);
            lblEntries.setVisible(false);
            lblReports.setVisible(false);
        }
    });
        txtUser.setText(LogInController.loggerUsername);
        if (!LogInController.OverAllAdmin){
            btnSDRI.setVisible(false);
            lblAdministrative.setVisible(false);
        }
        btnSDHME.setDisable(true);
        try {
            showSGRE(null);
        } catch (IOException ex) {
            Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    

    @FXML
    private void showSDHME(ActionEvent event) throws IOException {
        FXMLLoader loadsdhme = new FXMLLoader();
        loadsdhme.setLocation(getClass().getResource( "/com/Views/fxml/home_side.fxml"));
        loadsdhme.setController(new home_sideController());
        home_sideController home = loadsdhme.getController();
        home.setappController(this);
        for (Button button: menuButtons) {
            if (button.equals(btnSDHME)){
                btnSDHME.getStyleClass().add("bigs");
            }else button.getStyleClass().remove("bigs");
        }
        /*
        leftPane.getChildren().clear();
        centerPane.getChildren().clear();
        leftPane.getChildren().add(loadsdhme.load());*/
    }

    @FXML
    private void showSGRE(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loadsgre = new FXMLLoader();
        loadsgre.setLocation(getClass().getResource( "/com/Views/fxml/entries_side.fxml"));
        loadsgre.setController(new entries_sideController(/*GetCenter*/));
        entries_sideController cont = (entries_sideController)loadsgre.getController();
        cont.setappController(this);
        centerPane.getChildren().clear();
        centerPane.getChildren().add(loadsgre.load());
        VBox.setVgrow(loadsgre.getRoot(), Priority.ALWAYS);
        for (Button button: menuButtons) {
            if (button.equals(btnSGRE)){
                btnSGRE.getStyleClass().add("bigs");
            }else button.getStyleClass().remove("bigs");
        }
//        border_pane.setCenter(loadsgre.load());
    }

    @FXML
    private void showSDRR(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("/com/Views/fxml/report_side.fxml"));
        load.setController(new report_sideController());
        report_sideController rep = (report_sideController)load.getController();
        rep.setappController(this);
        centerPane.getChildren().clear();
        centerPane.getChildren().add(load.load());
        for (Button button: menuButtons) {
            if (button.equals(btnSDRR)){
                btnSDRR.getStyleClass().add("bigs");
            }else button.getStyleClass().remove("bigs");
        }
    }

    @FXML
    private void showSDRI(ActionEvent event) throws IOException {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("/com/Views/fxml/add_side.fxml"));
        load.setController(new add_sideController());
        add_sideController rep = (add_sideController)load.getController();
        rep.setappController(this);
        centerPane.getChildren().clear();
        centerPane.getChildren().add(load.load());
        for (Button button: menuButtons) {
            if (button.equals(btnSDRI)){
                btnSDRI.getStyleClass().add("bigs");
            }else button.getStyleClass().remove("bigs");
        }
    }

    @FXML
    private void showSDHELP(ActionEvent event) {
        centerPane.getChildren().clear();
    }


    @FXML
    private void Logout(ActionEvent event) {
    Stage current = (Stage)txtUser.getScene().getWindow();
    current.close();
    Main.stage.show();/*
        try {
            // Setting login window
            Main st = new Main();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/Views/fxml/Login.fxml"));
            loader.setController(new LogInController());
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Login Prompt");
            stage.getIcons().add(new Image("/com/Assets/kmalogo.png"));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            new PromptDialogController("Error!", "Error Occured. Failed to initialize system.");
        }*/
    }

    void sideBarResize(){
        if (border_pane.getWidth() > 1038.0){
            if (transition.getRate() < 0){
            transition.setRate(transition.getRate()*-1);
            transition.play();}
            leftPane.setMinWidth(168);
            leftPane.setMaxWidth(168);
            leftPane.setPrefWidth(168);
            btnSDHME.setPrefWidth(168);
            btnSDRI.setPrefWidth(168);
            btnSDRR.setPrefWidth(168);
            btnSGRE.setPrefWidth(168);
            lblDashboard.setVisible(true);
            if (!LogInController.OverAllAdmin){
                lblAdministrative.setVisible(false);
            }else {
                lblAdministrative.setVisible(true);}
            lblEntries.setVisible(true);
            lblReports.setVisible(true);
        }else if (border_pane.getWidth() <= 1038.0){
            if (transition.getRate() > 0){
            transition.setRate(-1);
            transition.play();}
            leftPane.setMinWidth(51);
            leftPane.setMaxWidth(51);
            leftPane.setPrefWidth(51);
            btnSDHME.setPrefWidth(51);
            btnSDRI.setPrefWidth(51);
            btnSDRR.setPrefWidth(51);
            btnSGRE.setPrefWidth(51);
            lblDashboard.setVisible(false);
            lblAdministrative.setVisible(false);
            lblEntries.setVisible(false);
            lblReports.setVisible(false);
        }}
    @FXML
    void viewProfile(ActionEvent event) {

    }
}
