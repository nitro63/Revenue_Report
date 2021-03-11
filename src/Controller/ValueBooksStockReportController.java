/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetFunctions;
import Controller.Gets.GetValueBooksEntries;
import Controller.Gets.GetValueBooksReport;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Resultset;
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
public class ValueBooksStockReportController implements Initializable {

    @FXML
    private TableView<GetValueBooksEntries> tblValBookStocksRep;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colDate;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colValueBook;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colFirstSerial;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colLastSerial;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colQuantity;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colValAmount;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colCumuAmount;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colPurAmount;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colRemarks;
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
    private Label lblRevenueCenterWarn;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblMonthWarn;
    GetValueBooksEntries getReport;
    GetFunctions getFunctions = new GetFunctions();
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent = FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();

    public ValueBooksStockReportController() throws SQLException, ClassNotFoundException {
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
        stmnt = con.prepareStatement("SELECT `revCenter` FROM `value_books_stock_record` WHERE 1 GROUP BY `revCenter` ");
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
        stmnt = con.prepareStatement("SELECT `year` FROM `value_books_stock_record` WHERE `revCenter` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"' GROUP BY `year`");
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
        stmnt = con.prepareStatement("SELECT `month` FROM `value_books_stock_record` WHERE `revCenter` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"' AND `year` = '"+cmbYear.getSelectionModel().getSelectedItem()+"'GROUP BY `month`");
        ResultSet rs= stmnt.executeQuery();
        ResultSetMetaData rm = rs.getMetaData();

        while(rs.next()){
            rowMonths.add(rs.getString("month"));
        }
        cmbMonth.getItems().clear();
        cmbMonth.setItems(rowMonths);
        cmbMonth.setVisibleRowCount(5);
    }

    void changeNames(){
        lblMonth.setText(cmbMonth.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbYear.getSelectionModel().getSelectedItem());
        lblRevenueCenter.setText(cmbRevCenter.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void showReport(ActionEvent event) throws SQLException {
        if (cmbRevCenter.getSelectionModel().isEmpty()) {
            lblRevenueCenterWarn.setVisible(true);
        } else if (cmbYear.getSelectionModel().isEmpty()) {
            lblYearWarn.setVisible(true);
        } else if (cmbMonth.getSelectionModel().isEmpty()) {
            lblMonthWarn.setVisible(true);
        } else {
            changeNames();
            tblValBookStocksRep.getItems().clear();
            String Date, valBook, remarks, firstSerial, lastSerial, quantity, valAmount, cumuAmount, purAmount;
//        int ;
            float cumuamount = 0;
            String amount;
            stmnt = con.prepareStatement("SELECT * FROM `value_books_stock_record` WHERE `year`= '" + cmbYear.getSelectionModel().getSelectedItem() + "' AND `revCenter` = '" + cmbRevCenter.getSelectionModel().getSelectedItem() + "' AND `month` = '" + cmbMonth.getSelectionModel().getSelectedItem() + "'");
            ResultSet rs = stmnt.executeQuery();
            ResultSetMetaData rm = rs.getMetaData();
            colDate.setCellValueFactory(data -> data.getValue().dateProperty());
            colValueBook.setCellValueFactory(data -> data.getValue().valueBookProperty());
            colFirstSerial.setCellValueFactory(data -> data.getValue().firstSerialProperty());
            colLastSerial.setCellValueFactory(data -> data.getValue().lastSerialProperty());
            colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty());
            colValAmount.setCellValueFactory(data -> data.getValue().valAmountProperty());
            colCumuAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
            colPurAmount.setCellValueFactory(data -> data.getValue().purAmountProperty());
            colRemarks.setCellValueFactory(data -> data.getValue().remarksProperty());
            while (rs.next()) {
                Date = rs.getString("date");
                valBook = rs.getString("value_book");
                remarks = rs.getString("remarks");
                firstSerial = rs.getString("first_serial");
                lastSerial = rs.getString("last_serial");
                quantity = rs.getString("quantity");
                valAmount = getFunctions.getAmount(rs.getString("amount"));
                cumuamount += rs.getFloat("amount");
                cumuAmount = getFunctions.getAmount(Float.toString(cumuamount));
                purAmount = rs.getString("purchase_amount");
                getReport = new GetValueBooksEntries(Date, valBook, firstSerial, lastSerial, quantity, valAmount, cumuAmount, purAmount, remarks);
                tblValBookStocksRep.getItems().add(getReport);
            }
        }
    }
}


