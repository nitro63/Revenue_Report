/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class home_sideController implements Initializable {

    @FXML
    private AnchorPane sideHome;

    @FXML
    private JFXButton btnAddUser;

    @FXML
    private JFXButton btnUserProfile;

    @FXML
    private JFXButton btnDashboard;

    private appController app;

    public void setappController(appController app){
        this.app = app;
    }
    public appController getappController(){
        return app;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showDashboard(null);
    }

    @FXML
    void showAddUser(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loadAddUser = new FXMLLoader();
        loadAddUser.setLocation(getClass().getResource("/Views/fxml/addUser.fxml"));
        loadAddUser.setController(new AddUserController());
        AnchorPane root = loadAddUser.load();
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(root);
    }

    @FXML
    void showDashboard(ActionEvent event) {

    }

    @FXML
    void showUserProfile(ActionEvent event) {

    }
    
}
