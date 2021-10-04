package com.Controller;

import com.Controller.Gets.GetDetails;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import com.revenue_report.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RevenueCenterController implements Initializable {

    @FXML
    private JFXTextField txtCenter;

    @FXML
    private JFXComboBox<String> cmbCategory;

    @FXML
    private TableView<GetDetails> tblCenters;

    @FXML
    private TableColumn<GetDetails, String> colID;

    @FXML
    private TableColumn<GetDetails, String> colCenter;

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
    private Label lblCentWarn;

    @FXML
    private Label lblControlWarn;

    @FXML
    private Label lblCodeWarn;

    @FXML
    private Label lblCategWarn;

    private PreparedStatement stmnt;
    private ResultSet rs;
    private final Connection con;
    boolean Condition;
    GetDetails getData;
    String entryID, centr;
    ObservableList<String> centers = FXCollections.observableArrayList("KMA MAIN", "OUTSOURCED", "SUB-METROS", "PROPERTY RATE SECTION");

    public RevenueCenterController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblCenters.setOnMouseClicked(e ->{
            if (!tblCenters.getSelectionModel().isEmpty() && e.getClickCount() >1){
                setCenters();
            }
            if (lblControlWarn.isVisible()){
                lblControlWarn.setVisible(false);
            }
        });
        txtCenter.setOnMouseClicked(e -> {
            lblCentWarn.setVisible(false);
        });
        txtCode.setOnMouseClicked(e -> {
            lblCodeWarn.setVisible(false);
        });
        cmbCategory.setOnMouseClicked(e -> {
            lblCategWarn.setVisible(false);
        });
        cmbCategory.getItems().addAll(centers);
        try {
            loadTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setCenters() {
        getData = tblCenters.getSelectionModel().getSelectedItem();
        txtCenter.setText(getData.getCenter());
        txtCode.setText(getData.getID());
        centr = txtCenter.getText();
        cmbCategory.getSelectionModel().select(getData.getCategory());
        entryID = getData.getID();
    }/*
    void resetFields(){
        txtCode.setText(null);
        txtCenter.setText(null);
        cmbCategory.getSelectionModel().clearSelection();
        entryID = null;
    }*/

    private void loadTable() throws SQLException {
        stmnt = con.prepareStatement("SELECT * FROM `revenue_centers` WHERE 1");
        rs = stmnt.executeQuery();
        tblCenters.getItems().clear();
        colCateg.setCellValueFactory(e -> e.getValue().categoryProperty());
        colCenter.setCellValueFactory(e -> e.getValue().centerProperty());
        colID.setCellValueFactory(e -> e.getValue().IDProperty());
        tblCenters.getItems().clear();
        while (rs.next()){
            getData = new GetDetails(rs.getString("revenue_center"), rs.getString("CenterID"), rs.getString("revenue_category"));
            tblCenters.getItems().add(getData);
        }
    }


    @FXML
    void UpdateItem(ActionEvent event) throws SQLException {
        ObservableList<String> dup = FXCollections.observableArrayList();
        String cent = txtCenter.getText().trim(), code = txtCode.getText(), cat = cmbCategory.getSelectionModel().getSelectedItem();
        if (entryID != null) {
            if (txtCode.getText().isEmpty()) {
                lblCodeWarn.setVisible(true);
            } else if (txtCenter.getText().isEmpty()) {
                lblCentWarn.setVisible(true);
            } else if (cmbCategory.getSelectionModel().isEmpty()) {
                lblCategWarn.setVisible(true);
            } else {
                stmnt = con.prepareStatement("SELECT `revenue_center`, `CenterID`FROM `revenue_centers` WHERE `revenue_center` = '" + cent + "'OR `CenterID` = '" + code + "'");
                rs = stmnt.executeQuery();
                while (rs.next()) {
                    dup.add(rs.getString("revenue_center"));
                    dup.add(rs.getString("CenterID"));
                }
                if (dup.contains(txtCenter.getText()) && !centr.equals(txtCenter.getText())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Duplicate Entry");
                    alert.setHeaderText("DUPLICATE");
                    alert.setContentText("Please '" + txtCode.getText() + "' already exist");
                    alert.showAndWait();
                } else if (dup.contains(txtCode.getText()) && !txtCode.getText().equals(entryID)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Duplicate Entry");
                    alert.setHeaderText("DUPLICATE");
                    alert.setContentText("Please '" + txtCode.getText() + "' already exist");
                    alert.showAndWait();
                } else {
                    stmnt = con.prepareStatement("UPDATE `revenue_centers` SET `revenue_center` = '" + cent + "'," +
                            " `revenue_category` = '" + cat + "', `CenterID` = '" + code + "' WHERE `CenterID` = '" + entryID + "'");
                    stmnt.executeUpdate();
                    loadTable();
                    resetFields();
                }
            }
        }else {
            lblControlWarn.setVisible(true);
        }

    }

    @FXML
    void addItem(ActionEvent event) throws SQLException {
        ObservableList<String> dup = FXCollections.observableArrayList();
        if (entryID == null){
            String cent = txtCenter.getText().trim(), code = txtCode.getText(), cat = cmbCategory.getSelectionModel().getSelectedItem();
        if (txtCode.getText().isEmpty()){
            lblCodeWarn.setVisible(true);
        } else if(txtCenter.getText().isEmpty()){
            lblCentWarn.setVisible(true);
        }else if (cmbCategory.getSelectionModel().isEmpty()){
            lblCategWarn.setVisible(true);
        } else {
            stmnt = con.prepareStatement("SELECT `revenue_center`, `CenterID`FROM `revenue_centers` WHERE `revenue_center` = '"+cent+"'OR `CenterID` = '"+code+"'");
            rs = stmnt.executeQuery();
            while (rs.next()){
                dup.add(rs.getString("revenue_center"));
                dup.add(rs.getString("CenterID"));
            }
            if (dup.contains(txtCenter.getText())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText("DUPLICATE");
                alert.setContentText("Please '"+txtCode.getText()+"' already exist");
                alert.showAndWait();
            }else if (dup.contains(txtCode.getText())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText("DUPLICATE");
                alert.setContentText("Please '"+txtCode.getText()+"' already exist");
                alert.showAndWait();
            }else {
                stmnt = con.prepareStatement("INSERT INTO `revenue_centers`(`revenue_center`, `revenue_category`," +
                        " `CenterID`) VALUES ('" + cent + "', '" + cat + "', '" + code + "')");
                stmnt.executeUpdate();
                loadTable();
                resetFields();
            }
        }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("WRONG BUTTON CLICK");
            alert.setContentText("Please click on Update Button to update Item.");
            alert.showAndWait();
        }
    }

    @FXML
    void deleteItem(ActionEvent event) throws SQLException {
        if (entryID != null){
            stmnt = con.prepareStatement("DELETE FROM `revenue_centers` WHERE `CenterID` = '"+entryID+"'");
            stmnt.executeUpdate();
            resetFields();
            loadTable();
        }else {
            lblControlWarn.setVisible(true);
        }
    }

    @FXML
    void codeKey(KeyEvent event) {
        String c = event.getCharacter();
        if("1234567890K".contains(c)) {}
        else {
            event.consume();
        }
    }


    private void resetFields() {
        txtCode.setText(null);
        txtCenter.setText(null);
        cmbCategory.getSelectionModel().clearSelection();
        entryID = null;
    }

}
