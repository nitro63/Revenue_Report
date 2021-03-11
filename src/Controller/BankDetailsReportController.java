/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetBankDetails;
import Controller.Gets.GetFunctions;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class BankDetailsReportController implements Initializable {


    @FXML
    private TableView<GetBankDetails> tblBankDetails;
    @FXML
    private TableColumn<GetBankDetails, String> colDate;
    @FXML
    private TableColumn<GetBankDetails, String> colGCRNumber;
    @FXML
    private TableColumn<GetBankDetails, String> colChqDate;
    @FXML
    private TableColumn<GetBankDetails, String> colChqNumber;
    @FXML
    private TableColumn<GetBankDetails, String> colBank;
    @FXML
    private TableColumn<GetBankDetails, String> colAmount;
    @FXML
    private TableColumn<GetBankDetails, String> colRemarks;
    @FXML
    private Label lblMonth;
    @FXML
    private JFXComboBox<String> cmbRevCenter;
    @FXML
    private JFXComboBox<String> cmbYear;
    @FXML
    private JFXComboBox<String> cmbMonth;
    @FXML
    private JFXButton btnShowReport;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblRevenueCenter;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblRevenueCenterWarn;
    @FXML
    private Label lblMonthWarn;
    GetBankDetails getReport;
    GetFunctions getFunctions = new GetFunctions();
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent = FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();

    public BankDetailsReportController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbRevCenter.setOnMouseClicked(mouseEvent -> {
            lblRevenueCenterWarn.setVisible(false);
        });
        cmbYear.setOnMouseClicked(mouseEvent -> {
            lblYearWarn.setVisible(false);
        });
        cmbMonth.setOnMouseClicked(mouseEvent -> {
            lblMonthWarn.setVisible(false);
        });
        try {
            getRevenueCenters();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void getRevenueCenters() throws SQLException {
        stmnt = con.prepareStatement("SELECT `revCenter` FROM `cheque_details` WHERE 1 GROUP BY `revCenter` ");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();
        int columns = metadata.getColumnCount();

        while(rs.next()){
            rowCent.add(rs.getString("revCenter"));
        }
        cmbRevCenter.getItems().clear();
        cmbRevCenter.setItems(rowCent);
        cmbRevCenter.setVisibleRowCount(5);
    }

    @FXML
    private void loadYears(ActionEvent event) throws SQLException {
        stmnt = con.prepareStatement("SELECT `year` FROM `cheque_details` WHERE `revCenter` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"' GROUP BY `year`");
        ResultSet rs= stmnt.executeQuery();
        ResultSetMetaData rm = rs.getMetaData();

        while(rs.next()){
            rowYear.add(rs.getString("year"));
        }
        cmbYear.getItems().clear();
        cmbYear.setItems(rowYear);
        cmbYear.setVisibleRowCount(5);
    }

    @FXML
    private void loadMonths(ActionEvent event) throws SQLException {
        stmnt = con.prepareStatement("SELECT `month` FROM `cheque_details` WHERE `revCenter` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"' AND `year` = '"+cmbYear.getSelectionModel().getSelectedItem()+"'GROUP BY `month`");
        ResultSet rs= stmnt.executeQuery();
        ResultSetMetaData rm = rs.getMetaData();

        while(rs.next()){
            rowMonths.add(rs.getString("month"));
        }
        cmbMonth.getItems().clear();
        cmbMonth.setItems(rowMonths);
        cmbMonth.setVisibleRowCount(5);
    }

    @FXML
    private void showReport(ActionEvent event) {
        if (cmbRevCenter.getSelectionModel().isEmpty()) {
            lblRevenueCenterWarn.setVisible(true);
        } else if (cmbYear.getSelectionModel().isEmpty()) {
            lblYearWarn.setVisible(true);
        } else if (cmbMonth.getSelectionModel().isEmpty()) {
            lblMonthWarn.setVisible(true);
        } else {

        }
    }

}
