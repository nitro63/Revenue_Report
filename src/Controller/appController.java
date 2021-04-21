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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import Controller.Gets.GetRevCenter;
/**
 *
 * @author NiTrO
 */
//public class appController  {
public class appController  implements Initializable{
    
    
    @FXML
    private BorderPane border_pane;
    @FXML
    private VBox centerPane; 
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
    private Text txtUser;
    @FXML
    private Button btnLogout;
    @FXML
    private VBox leftPane;
    GetRevCenter GetCenter = new GetRevCenter();
    

public  appController() {
         }
     public VBox getCenterPane(){
         return centerPane;
     }
    
      

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ActionEvent event = null;
        try {
            showSDHME(event);
        } catch (IOException ex) {
            Logger.getLogger(appController.class.getName()).log(Level.SEVERE, null, ex);
        }
                }    

    

    @FXML
    private void showSDHME(ActionEvent event) throws IOException {
        FXMLLoader loadsdhme = new FXMLLoader();
        loadsdhme.setLocation(getClass().getResource( "/Views/fxml/home_side.fxml"));
        loadsdhme.setController(new home_sideController());
        leftPane.getChildren().clear();
        centerPane.getChildren().clear();
        leftPane.getChildren().add(loadsdhme.load());
    }

    @FXML
    private void showSGRE(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loadsgre = new FXMLLoader();
        loadsgre.setLocation(getClass().getResource( "/Views/fxml/entries_side.fxml"));
        loadsgre.setController(new entries_sideController(GetCenter));
        entries_sideController cont = (entries_sideController)loadsgre.getController();
        cont.setappController(this);
        leftPane.getChildren().clear();
        centerPane.getChildren().clear();
        leftPane.getChildren().add(loadsgre.load());
    }

    @FXML
    private void showSDRR(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("/Views/fxml/report_side.fxml"));
        load.setController(new report_sideController());
        report_sideController rep = (report_sideController)load.getController();
        rep.setappController(this);
        leftPane.getChildren().clear();
        centerPane.getChildren().clear();
        leftPane.getChildren().add(load.load());
    }

    @FXML
    private void showSDRI(ActionEvent event) throws IOException {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("/Views/fxml/add_side.fxml"));
        load.setController(new add_sideController());
        add_sideController rep = (add_sideController)load.getController();
        rep.setappController(this);
        leftPane.getChildren().clear();
        centerPane.getChildren().clear();
        leftPane.getChildren().add(load.load());
    }

    @FXML
    private void showSDHELP(ActionEvent event) {
        centerPane.getChildren().clear();
    }

    @FXML
    private void LOGOUT(ActionEvent event) {
    }
}
