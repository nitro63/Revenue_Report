package com.Controller;

import com.Models.GetBankDetails;
import com.Models.GetFunctions;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.revenue_report.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BankDetailsUpdateController implements Initializable {

        @FXML
        private AnchorPane anchPane;

        @FXML
        private AnchorPane topPane;

        @FXML
        private Label lblTitle;

        @FXML
        private TableView<?> tblCollectUpdate;

        @FXML
        private TableColumn<?, ?> colID;

        @FXML
        private TableColumn<?, ?> colGCR;

        @FXML
        private TableColumn<?, ?> colChqDate;

        @FXML
        private TableColumn<?, ?> colChqNmb;

        @FXML
        private TableColumn<?, ?> colBank;

        @FXML
        private TableColumn<?, ?> colAmount;

        @FXML
        private Label lblDeleteWarn;

        @FXML
        private Label lblEdit;

        @FXML
        private Label lblDup;

        @FXML
        private JFXButton btnDelete;

        @FXML
        private JFXButton btnUpdateEntries;

        @FXML
        private JFXButton btnClear;

        @FXML
        private JFXComboBox<?> cmbGCR;

        @FXML
        private Label lblGCRWarn;

        @FXML
        private JFXDatePicker dtpckChequeDate;

        @FXML
        private Label lblChqdatewarn;

        @FXML
        private JFXTextField txtChqNmb;

        @FXML
        private Label lblChqNmbwarn;

        @FXML
        private JFXTextField txtBankName;

        @FXML
        private Label lblBankwarn;

        @FXML
        private JFXTextField txtAmount;

        @FXML
        private Label lblAmountwarn;

        @FXML
        private JFXButton btnSaveEntries;

    GetBankDetails getReport, getData;

    UpdateEntriesController colUpdate ;
    GetFunctions getFunctions = new GetFunctions();

    private  boolean Condition = true;
    private final Connection con;

    public BankDetailsUpdateController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
    public TableView getTableView(){
        return tblCollectUpdate;
    }

    public void setAppController(UpdateEntriesController app){
        this.colUpdate = app;
    }
    Stage stage =  new Stage()/*anchBankDetails.getScene().getWindow()*/;
    public void setStage(Stage stage){
        this.stage = stage;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
        @FXML
        void SaveToDatabase(ActionEvent event) {

        }

        @FXML
        void clearEntries(ActionEvent event) {

        }

        @FXML
        void deleteSelection(ActionEvent event) {

        }

        @FXML
        void onlyIntegers(KeyEvent event) {

        }

        @FXML
        void onlyNumbers(KeyEvent event) {

        }

        @FXML
        void updateEntries(ActionEvent event) {

        }



}
