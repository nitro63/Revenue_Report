/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
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
import revenue_report.DBConnection;

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
    private JFXButton btnDailies;

    @FXML
    private JFXButton btnVLBStcxEntries;

    @FXML
    private JFXButton btnEntriesUpdate;
    
       
    private final GetRevCenter GetCenter = new GetRevCenter();
    
    public  ObservableList<String> RevenueCenters = FXCollections.observableArrayList("KMA MAIN","OUTSOURCED","SUB-METROS","OTHER REVENUES");
    private final Connection con;
    private PreparedStatement stmnt;
      String SelItem, SelRev ;
     GetRevCenter u = new GetRevCenter();
     private final Map<String, String> centerID = new HashMap<>();
     
     appController app;
    @FXML
    private JFXButton btnPaymentEntries;
    @FXML
    private JFXButton btnTargEntries;
    @FXML
    private JFXButton btnPaymentDetails;
    InitializerController init = new InitializerController();

     /***
      */
     public entries_sideController(/*GetRevCenter GetCenter*/) throws SQLException, ClassNotFoundException {
         this.con = DBConnection.getConn();
//         this.GetCenter = GetCenter;
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
        try {
            SetRevenueCenters();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }try {
            if (LogInController.hasCenter){
            cmbRevGroup.getSelectionModel().select(InitializerController.userCategory);
            cmbRevGroup.setDisable(true);
            SetCenters();
            cmbRevCent.getSelectionModel().select(InitializerController.userCenter);
                SelItem = cmbRevCent.getSelectionModel().getSelectedItem();
                SelRev = centerID.get(cmbRevCent.getSelectionModel().getSelectedItem());
                GetCenter.setRevCenter(SelItem);
                GetCenter.setCenterID(SelRev);
            cmbRevCent.setDisable(true);
            }
        } catch (SQLException throwables) {
                throwables.printStackTrace();
        }
        if (LogInController.OverAllAdmin || LogInController.Accountant){
            btnEntriesUpdate.setVisible(true);
        }else {
            btnEntriesUpdate.setVisible(false);
        }
    }
    
    
    public void SetRevenueCenters() throws SQLException {
        stmnt = con.prepareStatement("SELECT `revenue_category` FROM `revenue_centers` WHERE 1 GROUP BY `revenue_category`");
        ResultSet rt = stmnt.executeQuery();
        cmbRevGroup.getItems().clear();
        while (rt.next()){
            cmbRevGroup.getItems().add(rt.getString("revenue_category"));
        }


    }
    
    private void SetCenters() throws SQLException {
        String Center = cmbRevGroup.getSelectionModel().getSelectedItem();
        stmnt = con.prepareStatement("SELECT `revenue_center`, `CenterID` FROM `revenue_centers` WHERE `revenue_category` = '"+
                Center+"'");
        ResultSet rt = stmnt.executeQuery();
        cmbRevCent.getItems().clear();
        while (rt.next()){
            cmbRevCent.getItems().add(rt.getString("revenue_center"));
            centerID.put(rt.getString("revenue_center"), rt.getString("CenterID"));
        }
//        switch(Center){
//
//            case "KMA MAIN":
//                cmbRevCent.getItems().clear();
//                cmbRevCent.getItems().addAll("Marriage Unit", " Licence Unit", "Environmental", "Waste Management");
//                break;
//            case "OUTSOURCED":
//                cmbRevCent.getItems().clear();
//                cmbRevCent.getItems().addAll("SKYMOUNT", "GREENFIELD", "DEGEON", "GOLDSTREET", "MY COMPANY 360");
//                break;
//            case "SUB-METROS":
//                 cmbRevCent.getItems().clear();
//                cmbRevCent.getItems().addAll("BANTAMA", "MANHYIA SOUTH", "MANHYIA NORTH", "SUBIN", "NHYIAESO");
//                break;
//            case "OTHER REVENUES":
//                cmbRevCent.getItems().clear();
//                cmbRevCent.getItems().addAll("SOKOBAN WOOD VILLAGE","ABINKYI","AFIA KOBI", "ASAFO MARKET");
//                break;
//
//    }
    }

   
    @FXML
    private void ShowRevenue_Entries(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
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
    private void setCenters(ActionEvent event) throws SQLException {
        SetCenters();
        }
    
    @FXML
    private void SelectedCenter(ActionEvent event) {
           if(app.getCenterPane().getChildren() != null){
               app.getCenterPane().getChildren().clear();
           }
        SelItem = cmbRevCent.getSelectionModel().getSelectedItem();
           SelRev = centerID.get(cmbRevCent.getSelectionModel().getSelectedItem());
        GetCenter.setRevCenter(SelItem);
        GetCenter.setCenterID(SelRev);
        System.out.println(GetCenter.getCenterID());
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
    void showEntriesUpdate(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if(cmbRevCent.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Select Revenue Center");
            alert.showAndWait();
        }else{
            FXMLLoader loadEntries = new FXMLLoader();
            loadEntries.setLocation(getClass().getResource("/Views/fxml/UpdateEntries.fxml"));
            loadEntries.setController(new UpdateEntriesController(GetCenter));
            UpdateEntriesController valController = (UpdateEntriesController)loadEntries.getController();
            valController.setappController(this);
            AnchorPane root = loadEntries.load();
            app.getCenterPane().getChildren().clear();
            app.getCenterPane().getChildren().add(root);
        }
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
