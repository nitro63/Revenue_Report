/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import com.Controller.Gets.GetRevCenter;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.revenue_report.DBConnection;

import java.sql.SQLException;


/**
 * FXML com.Controller class
 *
 * @author HP
 */
public class entries_sideController extends VBox implements Initializable {

    @FXML
    private VBox entriesMain;
    @FXML
    private VBox vboxEntries;
    @FXML
    private MaterialDesignIconView iconForward;
    @FXML
    private Text txtTitle;
    @FXML
    private HBox containerDailies;
    @FXML
    private HBox containerTarget;
    @FXML
    private HBox containerStock;
    @FXML
    private HBox containerPayment;
    @FXML
    private JFXComboBox<String> cmbRevGroup;
    @FXML
    private JFXComboBox<String> cmbRevCent;

    @FXML
    private Button btnCloseTarget;

    @FXML
    private Button btnCloseValue;

    @FXML
    private Button btnClosePayment;

    @FXML
    private Button btnCloseRevenue;

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
     private JFXButton btnPayment;
    @FXML
    private MenuButton btnPaymentEntries;

    @FXML
    private VBox paneEntriesArea;
    @FXML
    private AnchorPane noEntriesPane;
    @FXML
    private JFXButton btnTargEntries;
    @FXML
    private JFXButton btnPaymentDetails;
    @FXML
    private Text txtEntries;
    @FXML
    private Text txtEntriesMain;
    @FXML
    private Text txtEntriesMainNoPane;
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
    
     public ComboBox<String> getRevGroup(){
         return cmbRevGroup;
     }
    public VBox getEntryMain(){
        return entriesMain;
    }
     
     public ComboBox <String> getRevCent(){
         return cmbRevCent;
     }
    ObservableList<JFXButton> menuButtons = FXCollections.observableArrayList();
    ObservableList <HBox> menuContainers = FXCollections.observableArrayList();
    ObservableList <Button> menuCloseButton = FXCollections.observableArrayList();


    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuButtons.addAll(Arrays.asList(btnVLBStcxEntries,btnDailies,btnTargEntries,btnPayment));
        menuContainers.addAll(Arrays.asList(containerStock,containerDailies,containerTarget,containerPayment));
        menuCloseButton.addAll(Arrays.asList(btnCloseValue,btnCloseRevenue,btnClosePayment,btnCloseTarget));
        VBox.setVgrow(entriesMain, Priority.ALWAYS);
        entriesMain.setFillWidth(true);
        btnCloseRevenue.setVisible(false);
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
        }/*
        if (LogInController.OverAllAdmin || LogInController.Accountant){
            btnEntriesUpdate.setVisible(true);
        }else {
            btnEntriesUpdate.setVisible(false);
        }*/
        txtEntries.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        containerPayment.setVisible(LogInController.Accountant || LogInController.OverAllAdmin);
        if (!containerPayment.isVisible()){containerStock.setLayoutX(306) ;containerTarget.setLayoutX(182);}
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
        txtEntries.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        for (Button close : menuCloseButton){
            if (close.isVisible()){
                close.setVisible(false);
            }
        }
        txtEntries.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        for (HBox container : menuContainers){
            container.getStyleClass().remove("big");}

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
        loadEntries.setLocation(getClass().getResource("/com/Views/fxml/Revenue_Entries.fxml"));
       loadEntries.setController(new Revenue_EntriesController(GetCenter));
       Revenue_EntriesController revEnt = (Revenue_EntriesController)loadEntries.getController();
       revEnt.setappController(this);
       for (JFXButton btn: menuButtons){
           if (btn.equals(btnDailies)){
               btn.getStyleClass().removeAll("menu-title-button1, focus");
           }
       }
       for (HBox container : menuContainers){
           if (container.equals(containerDailies)){
               container.getStyleClass().removeAll("menu-title, focus");
               container.getStyleClass().remove("big");
               container.getStyleClass().add("big");
           }else {
               container.getStyleClass().remove("big");
           }
       }
       for (Button close : menuCloseButton){
        if (!close.equals(btnCloseRevenue)){
            close.setVisible(false);
        }else {
            btnCloseRevenue.setVisible(true);
        }
       }
       txtEntries.setVisible(true);
       iconForward.setVisible(true);
       txtTitle.setText("Daily Revenue");
       txtTitle.setVisible(true);
       AnchorPane root = loadEntries.load();
            paneEntriesArea.getChildren().clear();
            paneEntriesArea.getChildren().add(root);
//            app.getCenterPane().getChildren().add(root);
        }
    }
    
    
    @FXML
    private void setCenters(ActionEvent event) throws SQLException {
        SetCenters();
        }
    
    @FXML
    private void SelectedCenter(ActionEvent event) {
           if(paneEntriesArea.getChildren() != null){
               paneEntriesArea.getChildren().clear();
               paneEntriesArea.getChildren().add(noEntriesPane);
               txtEntriesMain.setVisible(false);
               txtEntriesMainNoPane.setVisible(true);
               txtEntries.setVisible(false);
               iconForward.setVisible(false);
               txtTitle.setText(null);
               for (Button close : menuCloseButton){
                   if (close.isVisible()){
                       close.setVisible(false);
                   }
               }
               txtEntries.setVisible(false);
               iconForward.setVisible(false);
               txtTitle.setText(null);
               txtTitle.setVisible(false);
               for (HBox container : menuContainers){
                   container.getStyleClass().remove("big");}
           }
        SelItem = cmbRevCent.getSelectionModel().getSelectedItem();
           SelRev = centerID.get(cmbRevCent.getSelectionModel().getSelectedItem());
        GetCenter.setRevCenter(SelItem);
        GetCenter.setCenterID(SelRev);
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
        loadEntries.setLocation(getClass().getResource("/com/Views/fxml/Payment_Entries.fxml"));
       loadEntries.setController(new Payment_EntriesController(GetCenter));
       Payment_EntriesController revEnt = (Payment_EntriesController)loadEntries.getController();
       revEnt.setappController(this);
       AnchorPane root = loadEntries.load();
       paneEntriesArea.getChildren().clear();
       paneEntriesArea.getChildren().add(root);
            for (JFXButton btn: menuButtons){
                if (btn.equals(btnPaymentEntries)){
                    btn.getStyleClass().removeAll("menu-title-button1, focus");
                }
            }
            for (HBox container : menuContainers){
                if (container.equals(containerPayment)){
                    container.getStyleClass().removeAll("menu-title, focus");
                    container.getStyleClass().remove("big");
                    container.getStyleClass().add("big");
                }else {
                    container.getStyleClass().remove("big");
                }
            }
            for (Button close : menuCloseButton){
                if (!close.equals(btnClosePayment)){
                    close.setVisible(false);
                }else {
                    close.setVisible(true);
                }
            }
            txtEntries.setVisible(true);
            iconForward.setVisible(true);
            txtTitle.setText("Payment");
            txtTitle.setVisible(true);
        }            
    }

    @FXML
    void showBankDetails(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        if(cmbRevCent.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Select Revenue Center");
            alert.showAndWait();
        }else{
            FXMLLoader loadEntries = new FXMLLoader();
            loadEntries.setLocation(getClass().getResource("/com/Views/fxml/Bank_DetailsEntries.fxml"));
            loadEntries.setController(new Bank_DetailsEntriesController(GetCenter));
            Bank_DetailsEntriesController revEnt = (Bank_DetailsEntriesController) loadEntries.getController();
            revEnt.setappController(this);
            AnchorPane root = loadEntries.load();
            paneEntriesArea.getChildren().clear();
            paneEntriesArea.getChildren().add(root);
            for (JFXButton btn: menuButtons){
                if (btn.equals(btnPaymentEntries)){
                    btn.getStyleClass().removeAll("menu-title-button1, focus");
                }
            }
            for (HBox container : menuContainers){
                if (container.equals(containerPayment)){
                    container.getStyleClass().removeAll("menu-title, focus");
                    container.getStyleClass().remove("big");
                    container.getStyleClass().add("big");
                }else {
                    container.getStyleClass().remove("big");
                }
            }
            for (Button close : menuCloseButton){
                if (!close.equals(btnClosePayment)){
                    close.setVisible(false);
                }else {
                    close.setVisible(true);
                }
            }
            txtEntries.setVisible(true);
            iconForward.setVisible(true);
            txtTitle.setText("Cheque Details");
            txtTitle.setVisible(true);
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
        loadEntries.setLocation(getClass().getResource("/com/Views/fxml/Target_Entries.fxml"));
       loadEntries.setController(new Target_EntriesController(GetCenter));
       Target_EntriesController revEnt = (Target_EntriesController)loadEntries.getController();
       revEnt.setappController(this);
       AnchorPane root = loadEntries.load();
       paneEntriesArea.getChildren().clear();
       paneEntriesArea.getChildren().add(root);
            for (JFXButton btn: menuButtons){
                if (btn.equals(btnTargEntries)){
                    btn.getStyleClass().removeAll("menu-title-button1, focus");
                }
            }
            for (HBox container : menuContainers){
                if (container.equals(containerTarget)){
                    container.getStyleClass().removeAll("menu-title, focus");
                    container.getStyleClass().remove("big");
                    container.getStyleClass().add("big");
                }else {
                    container.getStyleClass().remove("big");
                }
            }
            for (Button close : menuCloseButton){
                if (!close.equals(btnCloseTarget)){
                    close.setVisible(false);
                }else {
                    close.setVisible(true);
                }
            }
            txtEntries.setVisible(true);
            iconForward.setVisible(true);
            txtTitle.setText("Annual Target");
            txtTitle.setVisible(true);
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
            loadEntries.setLocation(getClass().getResource("/com/Views/fxml/UpdateEntries.fxml"));
            loadEntries.setController(new UpdateEntriesController(GetCenter));
            UpdateEntriesController valController = (UpdateEntriesController)loadEntries.getController();
            valController.setappController(this);
            AnchorPane root = loadEntries.load();
            paneEntriesArea.getChildren().clear();
            paneEntriesArea.getChildren().add(root);
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
            loadEntries.setLocation(getClass().getResource("/com/Views/fxml/valueBooks_stockEntries.fxml"));
            loadEntries.setController(new valueBooksEntriesController(GetCenter));
            valueBooksEntriesController valController = (valueBooksEntriesController)loadEntries.getController();
            valController.setappController(this);
            AnchorPane root = loadEntries.load();
            paneEntriesArea.getChildren().clear();
            paneEntriesArea.getChildren().add(root);
            for (JFXButton btn: menuButtons){
                if (btn.equals(btnVLBStcxEntries)){
                    btn.getStyleClass().removeAll("menu-title-button1, focus");
                }
            }
            for (HBox container : menuContainers){
                if (container.equals(containerStock)){
                    container.getStyleClass().removeAll("menu-title, focus");
                    container.getStyleClass().remove("big");
                    container.getStyleClass().add("big");
                }else {
                    container.getStyleClass().remove("big");
                }
            }
            for (Button close : menuCloseButton){
                if (!close.equals(btnCloseValue)){
                    close.setVisible(false);
                }else {
                    close.setVisible(true);
                }
            }
            txtEntries.setVisible(true);
            iconForward.setVisible(true);
            txtTitle.setText("Value Books Stock");
            txtTitle.setVisible(true);
        }
    }

    @FXML
    private void showClose(ActionEvent event) {
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(noEntriesPane);
        txtEntriesMain.setVisible(false);
        txtEntriesMainNoPane.setVisible(true);
        for (Button close : menuCloseButton){
            if (close.isVisible()){
                close.setVisible(false);
            }
        }
        txtEntries.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        for (HBox container : menuContainers){
                container.getStyleClass().remove("big");}
    }

   
    
}
