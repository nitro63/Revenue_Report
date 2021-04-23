/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Controller.Gets.GetDetails;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class RevenueItemsController implements Initializable {

    @FXML
    private JFXTextField txtItem;
    @FXML
    private JFXComboBox<String> cmbCategory;
    @FXML
    private TableView<GetDetails> tblAddItem;
    @FXML
    private TableColumn<GetDetails, String> colID;
    @FXML
    private TableColumn<GetDetails, String> colItem;
    @FXML
    private TableColumn<GetDetails, String> colCateg;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btnUpdate;
    @FXML
    private JFXTextField txtCode;

    @FXML
    private Label lblItemWarn;

    @FXML
    private Label lblControlWarn;

    @FXML
    private Label lblCategWarn;

    @FXML
    private Label lblCodeWarn;

    private PreparedStatement stmnt;
    private ResultSet rs;
    private final Connection con;
    boolean Condition;
    GetDetails getData;
    String entryID;
    ObservableList<String> categories = FXCollections.observableArrayList("Fines, Penalties & Forfeits",
            "Miscellaneous & Unidentified Revenue", "Fees", "Licences", "Rent of Land, Building & Houses",
            "Lands & Royalties", "Rates");

    public RevenueItemsController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cmbCategory.getItems().addAll(categories);
        tblAddItem.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblAddItem.setOnMouseClicked(e ->{
            if (!tblAddItem.getSelectionModel().isEmpty() && e.getClickCount() > 1){
                setItems();
            }
            if (lblControlWarn.isVisible()){
                lblControlWarn.setVisible(false);
            }
        });
        try {
            loadTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void loadTable() throws SQLException {
        stmnt = con.prepareStatement("SELECT * FROM `revenue_items` WHERE 1");
        rs = stmnt.executeQuery();
        colCateg.setCellValueFactory(e ->e.getValue().categoryProperty());
        colItem.setCellValueFactory(e -> e.getValue().centerProperty());
        colID.setCellValueFactory(e -> e.getValue().IDProperty());
        tblAddItem.getItems().clear();
        while(rs.next()){
            getData = new GetDetails(rs.getString("revenue_item"), rs.getString(
                    "revenue_Item_ID"), rs.getString("item_category"));
            tblAddItem.getItems().add(getData);
        }
    }

    void setItems(){
        getData = tblAddItem.getSelectionModel().getSelectedItem();
        txtCode.setText(getData.getID());
        txtItem.setText(getData.getCenter());
        cmbCategory.getSelectionModel().select(getData.getCategory());
        entryID = getData.getID();
    }

    @FXML
    void UpdateItem(ActionEvent event) throws SQLException {
        ObservableList<String> dup = FXCollections.observableArrayList();
        if (txtCode.getText().isEmpty()){
            lblCodeWarn.setVisible(true);
        }else if (cmbCategory.getSelectionModel().isEmpty()){
            lblCategWarn.setVisible(true);
        }else if (txtItem.getText().isEmpty()){
            lblItemWarn.setVisible(true);
        }else {
            stmnt = con.prepareStatement("SELECT `revenue_item`, `revenue_Item_ID` FROM `revenue_items` WHERE" +
                    " `revenue_Item_ID` = '"+txtCode.getText()+"' OR `revenue_item` = '"+txtItem.getText().trim()+"'");
            rs = stmnt.executeQuery();
            while (rs.next()){
                dup.addAll(rs.getString("revenue_item"), rs.getString("revenue_Item_ID"));
            }
            if (dup.contains(txtItem.getText())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText("DUPLICATE");
                alert.setContentText("Please '"+txtItem.getText()+"' already exist");
                alert.showAndWait();
            }else if (dup.contains(txtCode.getText())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText("DUPLICATE");
                alert.setContentText("Please '"+txtCode.getText()+"' already exist");
                alert.showAndWait();
            }else {
            stmnt = con.prepareStatement("UPDATE `revenue_items`SET `revenue_Item_ID` = '"+txtCode.getText()
                    +"', `revenue_item` = '"+txtItem.getText().trim()+"', `item_category` = '"+
                    cmbCategory.getSelectionModel().getSelectedItem()+"' WHERE `revenue_Item_ID` = '"+entryID+"'");
            stmnt.executeUpdate();
            }
        }
        resetFields();
        loadTable();
    }

    void resetFields(){
        txtCode.setText(null);
        txtItem.setText(null);
        cmbCategory.getSelectionModel().clearSelection();
        entryID = null;
    }

    @FXML
    void addItem(ActionEvent event) throws SQLException {
        ObservableList<String> dup = FXCollections.observableArrayList();
        if (txtCode.getText().isEmpty() || txtCode.getText().contains("\\s+")){
            lblCodeWarn.setVisible(true);
        }else if (cmbCategory.getSelectionModel().isEmpty()){
            lblCategWarn.setVisible(true);
        }else if (txtItem.getText().isEmpty()){
            lblItemWarn.setVisible(true);
        }else {
            stmnt = con.prepareStatement("SELECT `revenue_item`, `revenue_Item_ID` FROM `revenue_items` WHERE" +
                    " `revenue_Item_ID` = '"+txtCode.getText()+"' OR `revenue_item` = '"+txtItem.getText().trim()+"'");
            rs = stmnt.executeQuery();
            while (rs.next()){
                dup.addAll(rs.getString("revenue_item"), rs.getString("revenue_Item_ID"));
            }
            if (dup.contains(txtItem.getText())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText("DUPLICATE");
                alert.setContentText("Please '"+txtItem.getText()+"' already exist");
                alert.showAndWait();
            }else if (dup.contains(txtCode.getText())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText("DUPLICATE");
                alert.setContentText("Please '"+txtCode.getText()+"' already exist");
                alert.showAndWait();
            }else {
            stmnt = con.prepareStatement("INSERT INTO `revenue_items`(`revenue_Item_ID`, `revenue_item`," +
                    " `item_category`) VALUES ('"+txtCode.getText()+"', '"+txtItem.getText().trim()+"', '"+cmbCategory.
                    getSelectionModel().getSelectedItem()+"') ");
            stmnt.executeUpdate();
                resetFields();
                loadTable();
            }
        }
    }

    @FXML
    void deleteItem(ActionEvent event) throws SQLException {
        if (!entryID.isEmpty()){
            stmnt = con.prepareStatement("DELETE FROM `revenue_items` WHERE `revenue_Item_ID` = '"+entryID+"'");
            stmnt.executeUpdate();
            resetFields();
            loadTable();
        }else {
            lblControlWarn.setVisible(true);
        }
    }

    @FXML
    void onlyVarChar(KeyEvent event) {
        String c = event.getCharacter();
        if("1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ".contains(c)) {}
        else {
            event.consume();
        }
    }@FXML
    void onlyNumbers (KeyEvent event) {
        String c = event.getCharacter();
        if("1234567890".contains(c)) {}
        else {
            event.consume();
        }
    }


}
