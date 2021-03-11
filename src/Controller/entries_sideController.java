/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import Controller.Gets.GetRevCenter;
import java.sql.SQLException;


/**
 * FXML Controller class
 *
 * @author HP
 */
public class entries_sideController  implements Initializable {

    @FXML
     ComboBox<String> cmbRevGroup;
    @FXML
    private ComboBox<String> cmbRevCent;

    @FXML
    private Button btnDailies;

    @FXML
    private Button btnVLBStcxEntries;

    @FXML
    private Button btnEntriesUpdate;
    
       
    private final GetRevCenter GetCenter;
    
    public  ObservableList<String> RevenueCenters = FXCollections.observableArrayList("KMA MAIN","OUTSOURCED","SUB-METROS","OTHER REVENUES");
      String SelItem ;
      String SelRev;
     GetRevCenter u = new GetRevCenter();
     
     appController app;
    @FXML
    private Button btnPaymentEntries;
    @FXML
    private Button btnTargEntries;
    @FXML
    private Button btnPaymentDetails;

     /***
    * @param GetCenter
      */
     public entries_sideController(GetRevCenter GetCenter){         
         this.GetCenter = GetCenter;
     }
     public void setRevCenter(String revCenter){
         GetCenter.setRevCenter(revCenter);
     }
     
     public void setappController(appController app){
         this.app = app;
     }
     public appController getappController(){
         return app;
     }
    
     public ComboBox getRevGroup(){
         return cmbRevGroup;
     }
     
     public ComboBox getRevCent(){
         return cmbRevCent;
     }


    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SetRevenueCenters();
    }  
    
    
    public void SetRevenueCenters(){
        cmbRevGroup.getItems().clear();
        cmbRevGroup.getItems().addAll(RevenueCenters);
    }
    
    private void SetCenters(){
        String Center = cmbRevGroup.getSelectionModel().getSelectedItem();
        switch(Center){
        
            case "KMA MAIN":
                cmbRevCent.getItems().clear();
                cmbRevCent.getItems().addAll("Marriage Unit", " Licence Unit", "Environmental", "Waste Management");
                break;
            case "OUTSOURCED":
                cmbRevCent.getItems().clear();
                cmbRevCent.getItems().addAll("SKYMOUNT", "GREENFIELD", "DEGEON", "GOLDSTREET", "MY COMPANY 360");
                break;
            case "SUB-METROS":
                 cmbRevCent.getItems().clear();
                cmbRevCent.getItems().addAll("BANTAMA", "MANHYIA SOUTH", "MANHYIA NORTH", "SUBIN", "NHYIAESO");
                break;
            case "OTHER REVENUES":
                cmbRevCent.getItems().clear();
                cmbRevCent.getItems().addAll("SOKOBAN WOOD VILLAGE","ABINKYI","AFIA KOBI", "ASAFO MARKET");
                break;
           
    }
    }

   
    @FXML
    private void ShowRevenue_Entries(ActionEvent event) throws IOException {
        if(cmbRevCent.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Select Revenue Center");
                alert.showAndWait();
        }else{            
        FXMLLoader loadEntries = new FXMLLoader();
        loadEntries.setLocation(getClass().getResource("/Views/fxml/Revenue_Entries.fxml"));
       loadEntries.setController(new Revenue_EntriesController(GetCenter));
       Revenue_EntriesController revEnt = (Revenue_EntriesController)loadEntries.getController();
       revEnt.setappController(this);
       AnchorPane root = loadEntries.load();
       app.getCenterPane().getChildren().clear();
       app.getCenterPane().getChildren().add(root);
        }
    }
    
    
    @FXML
    private void setCenters(ActionEvent event) {       
        SetCenters();
        }
    
    @FXML
    private void SelectedCenter(ActionEvent event) {
           if(app.getCenterPane().getChildren() != null){
               app.getCenterPane().getChildren().clear();
           }
        SelItem = cmbRevCent.getSelectionModel().getSelectedItem();
        GetCenter.setRevCenter(SelItem);
    }

    @FXML
    private void showPaymentEntries(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        if(cmbRevCent.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Select Revenue Center");
                alert.showAndWait();
        }else{            
        FXMLLoader loadEntries = new FXMLLoader();
        loadEntries.setLocation(getClass().getResource("/Views/fxml/Payment_Entries.fxml"));
       loadEntries.setController(new Payment_EntriesController(GetCenter));
       Payment_EntriesController revEnt = (Payment_EntriesController)loadEntries.getController();
       revEnt.setappController(this);
       AnchorPane root = loadEntries.load();
       app.getCenterPane().getChildren().clear();
       app.getCenterPane().getChildren().add(root);
        }            
    }
    @FXML
    void showPaymentDetails(ActionEvent event) {

    }

    @FXML
    private void showTargetEntries(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        if(cmbRevCent.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Select Revenue Center");
                alert.showAndWait();
        }else{            
        FXMLLoader loadEntries = new FXMLLoader();
        loadEntries.setLocation(getClass().getResource("/Views/fxml/Target_Entries.fxml"));
       loadEntries.setController(new Target_EntriesController(GetCenter));
       Target_EntriesController revEnt = (Target_EntriesController)loadEntries.getController();
       revEnt.setappController(this);
       AnchorPane root = loadEntries.load();
       app.getCenterPane().getChildren().clear();
       app.getCenterPane().getChildren().add(root);
        }            
    }

    @FXML
    void showEntriesUpdate(ActionEvent event) {
    }

    @FXML
    void showStocksEntries(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if(cmbRevCent.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Select Revenue Center");
            alert.showAndWait();
        }else{
            FXMLLoader loadEntries = new FXMLLoader();
            loadEntries.setLocation(getClass().getResource("/Views/fxml/valueBooks_stockEntries.fxml"));
            loadEntries.setController(new valueBooksEntriesController(GetCenter));
            valueBooksEntriesController valController = (valueBooksEntriesController)loadEntries.getController();
            valController.setappController(this);
            AnchorPane root = loadEntries.load();
            app.getCenterPane().getChildren().clear();
            app.getCenterPane().getChildren().add(root);
        }
    }

   
    
}
