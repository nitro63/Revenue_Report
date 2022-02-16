/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller.Reports;

import com.Models.GetFunctions;
import com.Models.GetValueBooksEntries;
import com.Controller.InitializerController;
import com.Controller.LogInController;
import com.Enums.Months;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import com.revenue_report.DBConnection;

/**
 * FXML com.Controller class
 *
 * @author NiTrO
 */
public class ValueBooksStockReportController implements Initializable {

    @FXML
    private TableView<GetValueBooksEntries> tblValBookStocksRep;
    @FXML
    private JFXButton btnPrint;
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
    private JFXComboBox<Months> cmbMonth;
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
    ObservableList<Months> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    Map<String, String> centerID = new HashMap<>();
    boolean subMetroPR, Condition;
    private String SelectedCenter;

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
        if (LogInController.hasCenter && !cmbRevCenter.getSelectionModel().isEmpty()){
            try {
                getReportYear();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    void getRevenueCenters() throws SQLException {
        stmnt = con.prepareStatement("SELECT `value_stock_revCenter`, `revenue_centers`.`revenue_category`, `revenue_centers`.`revenue_center` FROM `value_books_stock_record`, `revenue_centers` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` GROUP BY `value_stock_revCenter` ");
        ResultSet rs = stmnt.executeQuery();
        while(rs.next()){
            rowCent.add(rs.getString("revenue_center"));
            centerID.put(rs.getString("revenue_center"), rs.getString("value_stock_revCenter"));
            if (rs.getString("revenue_category").equals("PROPERTY RATE SECTION")){
                Condition = true;
            }
            if (rs.getString("value_stock_revCenter").equals("K0201") || rs.getString("value_stock_revCenter").equals("K0202") || rs.getString("value_stock_revCenter").equals("K0203") || rs.getString("value_stock_revCenter").equals("K0204") || rs.getString("value_stock_revCenter").equals("K0205")){
                subMetroPR = true;
            }
        }
        if (Condition){
            rowCent.add("PROPERTY RATE ALL");
        }
        if (subMetroPR){
            rowCent.add("PROPERTY RATE SUB-METROS");
        }
        cmbRevCenter.getItems().clear();
        cmbRevCenter.setItems(rowCent);
        cmbRevCenter.setVisibleRowCount(5);
        if (LogInController.hasCenter && cmbRevCenter.getItems().contains(InitializerController.userCenter)){
            cmbRevCenter.getSelectionModel().select(InitializerController.userCenter);
            SelectedCenter = cmbRevCenter.getSelectionModel().getSelectedItem();
            cmbRevCenter.setDisable(true);
        }
    }

    private void getReportYear() throws SQLException {
        SelectedCenter = cmbRevCenter.getSelectionModel().getSelectedItem();
        if (cmbRevCenter.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT YEAR(date) AS `year` FROM `revenue_centers`,`value_books_stock_record`" +
                    " WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `revenue_centers`.`revenue_category` " +
                    "= 'PROPERTY RATE SECTION' GROUP BY `year`");
        } else if (cmbRevCenter.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT YEAR(date) AS `year` FROM `value_books_stock_record`,`revenue_centers`" +
                    " WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `value_stock_revCenter` = 'K0201' OR" +
                    " `value_stock_revCenter` = 'K0202' OR `value_stock_revCenter` = 'K0203' OR `value_stock_revCenter` = " +
                    "'K0204' OR `value_stock_revCenter` = 'K0205' GROUP BY `year`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT YEAR(date) AS `year` FROM `value_books_stock_record`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"' GROUP BY `year`");
        }
//        stmnt = con.prepareStatement("SELECT `year` FROM `value_books_stock_record` WHERE `value_stock_revCenter` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"' GROUP BY `year`");
        ResultSet rs= stmnt.executeQuery();
        while(rs.next()){
            rowYear.add(rs.getString("year"));
        }
        cmbYear.getItems().clear();
        cmbYear.setItems(rowYear);
        cmbYear.setVisibleRowCount(5);
    }

    @FXML
    private void loadYears(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void loadMonths(ActionEvent event) throws SQLException {
        if (cmbRevCenter.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT MONTH(date) AS `month` FROM `revenue_centers`,`value_books_stock_record` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND YEAR(date) = '"+cmbYear.getSelectionModel().getSelectedItem()+"'GROUP BY `month`");
        } else if (cmbRevCenter.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT MONTH(date) AS `month` FROM `value_books_stock_record`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `value_stock_revCenter` = 'K0201' OR `value_stock_revCenter` = 'K0202' OR `value_stock_revCenter` = 'K0203' OR `value_stock_revCenter` = 'K0204' OR `value_stock_revCenter` = 'K0205' AND YEAR(date) = '"+cmbYear.getSelectionModel().getSelectedItem()+"'GROUP BY `month`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT MONTH(date) AS `month` FROM `value_books_stock_record`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"'AND YEAR(date) = '"+cmbYear.getSelectionModel().getSelectedItem()+"'GROUP BY `month`");
        }
//        stmnt = con.prepareStatement("SELECT `month` FROM `value_books_stock_record` WHERE `value_stock_revCenter` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"' AND `year` = '"+cmbYear.getSelectionModel().getSelectedItem()+"'GROUP BY `month`");
        ResultSet rs= stmnt.executeQuery();
        ResultSetMetaData rm = rs.getMetaData();

        while(rs.next()){
            rowMonths.add(Months.get(rs.getInt("month")));
        }
        cmbMonth.getItems().clear();
        cmbMonth.setItems(rowMonths);
        cmbMonth.setVisibleRowCount(5);
    }

/*   void changeNames(){
      lblMonth.setText(cmbMonth.getSelectionModel().getSelectedItem().toString());
        lblYear.setText(cmbYear.getSelectionModel().getSelectedItem());
        lblRevenueCenter.setText(cmbRevCenter.getSelectionModel().getSelectedItem());
    }*/

    @FXML
    private void showReport(ActionEvent event) throws SQLException {
        if (cmbRevCenter.getSelectionModel().isEmpty()) {
            lblRevenueCenterWarn.setVisible(true);
        } else if (cmbYear.getSelectionModel().isEmpty()) {
            lblYearWarn.setVisible(true);
        } else if (cmbMonth.getSelectionModel().isEmpty()) {
            lblMonthWarn.setVisible(true);
        } else {
            //changeNames();
            tblValBookStocksRep.getItems().clear();
            String Date, valBook, remarks, firstSerial, lastSerial, quantity, valAmount, cumuAmount, purAmount;
//        int ;
            float cumuamount = 0;
            String amount;
            if (cmbRevCenter.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
                stmnt = con.prepareStatement(" SELECT * FROM `revenue_centers`,`value_books_stock_record`, `value_books_details` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND YEAR(date) = '"+cmbYear.getSelectionModel().getSelectedItem()+"' AND MONTH(date) = '" + cmbMonth.getSelectionModel().getSelectedItem().getValue() + "' AND `value_book` = `value_book_ID` ORDER BY `date`");
            } else if (cmbRevCenter.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
                stmnt = con.prepareStatement(" SELECT * FROM `value_books_stock_record`,`revenue_centers`, `value_books_details` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `value_stock_revCenter` = 'K0201' OR `value_stock_revCenter` = 'K0202' OR `value_stock_revCenter` = 'K0203' OR `value_stock_revCenter` = 'K0204' OR `value_stock_revCenter` = 'K0205' AND YEAR(date) = '"+cmbYear.getSelectionModel().getSelectedItem()+"' AND MONTH(date) = '" + cmbMonth.getSelectionModel().getSelectedItem().getValue() + "' AND `value_book` = `value_book_ID` ORDER BY `date`");
            }
            else {
                stmnt = con.prepareStatement(" SELECT * FROM `value_books_stock_record`,`revenue_centers`, `value_books_details` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"'AND YEAR(date) = '"+cmbYear.getSelectionModel().getSelectedItem()+"' AND MONTH(date) = '" + cmbMonth.getSelectionModel().getSelectedItem().getValue() + "' AND `value_book` = `value_book_ID` ORDER BY `date`");
            }
//            stmnt = con.prepareStatement("SELECT * FROM `value_books_stock_record`, `revenue_centers` WHERE `year`= '" +cmbYear.getSelectionModel().getSelectedItem() + "' AND `revenue_center`.`revenue_centers` = `value_stock_revCenter` AND `revenue_centers`.`revenue_center` = '" +cmbRevCenter.getSelectionModel().getSelectedItem() + "' AND `month` = '" + cmbMonth.getSelectionModel().getSelectedItem() + "'");
            ResultSet rs = stmnt.executeQuery();
            colDate.setCellValueFactory(data -> data.getValue().dateProperty());
            colValueBook.setCellValueFactory(data -> data.getValue().valueBookProperty());
            colFirstSerial.setCellValueFactory(data -> data.getValue().firstSerialProperty());
            colLastSerial.setCellValueFactory(data -> data.getValue().lastSerialProperty());
            colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty());
            colValAmount.setCellValueFactory(data -> data.getValue().valAmountProperty());
            colCumuAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
            colPurAmount.setCellValueFactory(data -> data.getValue().purAmountProperty());
            colRemarks.setCellValueFactory(data -> data.getValue().remarksProperty());
            colDate.setStyle( "-fx-alignment: CENTER-LEFT;");
            colValueBook.setStyle( "-fx-alignment: CENTER-LEFT;");
            colFirstSerial.setStyle( "-fx-alignment: CENTER-LEFT;");
            colLastSerial.setStyle( "-fx-alignment: CENTER-LEFT;");
            colQuantity.setStyle( "-fx-alignment: CENTER-LEFT;");

            while (rs.next()) {
                Date = getFunctions.convertSqlDate(rs.getString("date"));
                valBook = rs.getString("value_books");
                remarks = rs.getString("remarks");
                firstSerial = rs.getString("first_serial");
                lastSerial = rs.getString("last_serial");
                quantity = rs.getString("quantity");
                valAmount = getFunctions.getAmount(rs.getString("amount"));
                cumuamount += rs.getFloat("amount");
                cumuAmount = getFunctions.getAmount(Float.toString(cumuamount));
                purAmount = getFunctions.getAmount(rs.getString("purchase_amount"));
                getReport = new GetValueBooksEntries(Date, valBook, firstSerial, lastSerial, quantity, valAmount, cumuAmount, purAmount, remarks);
                tblValBookStocksRep.getItems().add(getReport);
            }
        }
    }

    @FXML
    void printReport(ActionEvent event) throws FileNotFoundException, JRException {
        if (tblValBookStocksRep.getItems().isEmpty()){
            event.consume();
        }else {
            java.util.Date date = new Date();
            List<GetValueBooksEntries> items = new ArrayList<>();
            for (int j = 0; j < tblValBookStocksRep.getItems().size(); j++) {
                GetValueBooksEntries getdata;
                getdata = tblValBookStocksRep.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/com/Assets/kmalogo.png");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmbYear.getSelectionModel().getSelectedItem(),
                    month = cmbMonth.getSelectionModel().getSelectedItem().toString(),
                    center = cmbRevCenter.getSelectionModel().getSelectedItem();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("month", month);
            parameters.put("year", year);
            parameters.put("timeStamp", date);
            parameters.put("center", center);

            //read jrxml file and creating jasperdesign object
            InputStream input = this.getClass().getResourceAsStream("/com/Assets/valueBooksStock.jrxml");

            JasperDesign jasperDesign = JRXmlLoader.load(input);

            /*compiling jrxml with help of JasperReport class*/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            /* Using jasperReport object to generate PDF */
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            /*call jasper engine to display report in jasperviewer window*/
            JasperViewer.viewReport(jasperPrint, false);
        }
    }
}


