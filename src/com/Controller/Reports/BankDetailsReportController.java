/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller.Reports;

import com.Models.GetBankDetails;
import com.Models.GetFunctions;
import com.Controller.InitializerController;
import com.Controller.LogInController;
import com.Enums.Months;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
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
public class BankDetailsReportController implements Initializable {


    @FXML
    private TableView<GetBankDetails> tblBankDetails;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private TableColumn<GetBankDetails, String> colDate;
    @FXML
    private TableColumn<GetBankDetails, String> colReceivedFrom;
    @FXML
    private TableColumn<GetBankDetails, String> colChqDate;
    @FXML
    private TableColumn<GetBankDetails, String> colChqNumber;
    @FXML
    private TableColumn<GetBankDetails, String> colBank;
    @FXML
    private TableColumn<GetBankDetails, String> colAmount;
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

    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private Label lblCenterWarn;
    @FXML
    private ComboBox<Integer> cmbReportYear;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private ComboBox<Months> cmbReportMonth;
    GetBankDetails getReport;
    GetFunctions getFunctions = new GetFunctions();
    Payment_ReportController payRep;
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent = FXCollections.observableArrayList();
    ObservableList<Months> rowMonths =FXCollections.observableArrayList();
    ObservableList<Integer> rowYear =FXCollections.observableArrayList();
    Map<String, String> centerID = new HashMap<>();

    String Year, Month, Center, RevCenterID, SelectedCenter;
    private boolean Condition;

    public void setAppController(Payment_ReportController app){
        this.payRep = app;
    }
    Stage stage =  new Stage()/*anchBankDetails.getScene().getWindow()*/;
    public void setStage(Stage stage){
        this.stage = stage;
    }

    public BankDetailsReportController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbReportCent.setOnMouseClicked(e-> {
            lblCenterWarn.setVisible(false);
        });
        cmbReportYear.setOnMouseClicked(e -> {
            lblYearWarn.setVisible(false);
        });
        cmbReportMonth.setOnMouseClicked(e -> {
            lblMonthWarn.setVisible(false);
        });
        try {
            getRevenueCenters();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (LogInController.hasCenter){
            try {
                getReportYear();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    void getRevenueCenters() throws SQLException {
        stmnt = con.prepareStatement("SELECT `cheque_center`, `revenue_centers`.`revenue_category`, `revenue_centers`.`revenue_center` FROM `revenue_centers`, `cheque_details` WHERE`CenterID` = `cheque_center` GROUP BY `cheque_center` ");
        ResultSet rs = stmnt.executeQuery();

        while(rs.next()){
            rowCent.add(rs.getString("revenue_center"));
            centerID.put(rs.getString("revenue_center"), rs.getString("cheque_center"));
            if (rs.getString("revenue_category").equals("PROPERTY RATE SECTION")){
                Condition = true;
            }
        }
        if (Condition){
            rowCent.add("PROPERTY RATE ALL");
        }
        cmbReportCent.getItems().clear();
        cmbReportCent.setItems(rowCent);
        cmbReportCent.setVisibleRowCount(5);
        if (LogInController.hasCenter){
            cmbReportCent.getSelectionModel().select(InitializerController.userCenter);
            SelectedCenter = cmbReportCent.getSelectionModel().getSelectedItem();
            cmbReportCent.setDisable(true);

        }
    }

    private void getReportYear() throws SQLException{
        SelectedCenter = cmbReportCent.getSelectionModel().getSelectedItem();
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT YEAR(date_received) AS `Year` FROM `revenue_centers`,`cheque_details` WHERE `revenue_centers`.`CenterID` = `cheque_center` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `Year`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT YEAR(date_received) AS `Year` FROM `cheque_details`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `cheque_center` AND `cheque_center` = 'K0201' OR `cheque_center` = 'K0202' OR `cheque_center` = 'K0203' OR `cheque_center` = 'K0204' OR `cheque_center` = 'K0205' GROUP BY `Year`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT YEAR(date_received) AS `Year` FROM `cheque_details`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `cheque_center` AND`revenue_centers`.`revenue_center` = '"+SelectedCenter+"'  GROUP BY `Year`");
        }
//        stmnt = con.prepareStatement(" SELECT `Year` FROM `collection_payment_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `collection_payment_entries`.`pay_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `Year`");
        ResultSet rs = stmnt.executeQuery();
        rowYear.clear();
        while(rs.next()){
            rowYear.add(rs.getInt("Year"));
        }
        cmbReportYear.getItems().clear();
        cmbReportYear.getItems().setAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
    }

    private void getMonths() throws SQLException{
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT MONTH(date_received) AS `Month` FROM `revenue_centers`,`cheque_details` WHERE `revenue_centers`.`CenterID` = `cheque_center` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND YEAR(date_received) = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT MONTH(date_received) AS `Month` FROM `cheque_details`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `cheque_center` AND `cheque_center` = 'K0201' OR `cheque_center` = 'K0202' OR `cheque_center` = 'K0203' OR `cheque_center` = 'K0204' OR `cheque_center` = 'K0205' AND YEAR(date_received) = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT MONTH(date_received) AS `Month` FROM `cheque_details`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `cheque_center` AND`revenue_centers`.`revenue_center` = '"+SelectedCenter+"' AND YEAR(date_received) = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
        }
//        stmnt = con.prepareStatement(" SELECT `Month` FROM `collection_payment_entries`,`revenue_centers` WHERE  `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenue_centers`.`CenterID` = `collection_payment_entries`.`pay_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
        ResultSet rs = stmnt.executeQuery();
        rowMonths.clear();
        while(rs.next()){
            rowMonths.add(Months.get(rs.getInt("Month")));
        }
        cmbReportMonth.getItems().clear();
        cmbReportMonth.getItems().setAll(rowMonths);
        cmbReportMonth.setVisibleRowCount(5);
    }

    private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem().toString());
        lblMonth.setText(cmbReportMonth.getSelectionModel().getSelectedItem().toString());
    }

//    @FXML
//    private void loadYears(ActionEvent event) throws SQLException {
//        stmnt = con.prepareStatement("SELECT `year` FROM `cheque_details` WHERE `revCenter` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"' GROUP BY `year`");
//        ResultSet rs= stmnt.executeQuery();
//        ResultSetMetaData rm = rs.getMetaData();
//
//        while(rs.next()){
//            rowYear.add(rs.getString("year"));
//        }
//        cmbYear.getItems().clear();
//        cmbYear.setItems(rowYear);
//        cmbYear.setVisibleRowCount(5);
//    }
//
//    @FXML
//    private void loadMonths(ActionEvent event) throws SQLException {
//        stmnt = con.prepareStatement("SELECT `month` FROM `cheque_details` WHERE `revCenter` = '"+cmbRevCenter.getSelectionModel().getSelectedItem()+"' AND `year` = '"+cmbYear.getSelectionModel().getSelectedItem()+"'GROUP BY `month`");
//        ResultSet rs= stmnt.executeQuery();
//        ResultSetMetaData rm = rs.getMetaData();
//
//        while(rs.next()){
//            rowMonths.add(rs.getString("month"));
//        }
//        cmbMonth.getItems().clear();
//        cmbMonth.setItems(rowMonths);
//        cmbMonth.setVisibleRowCount(5);
//    }

//    @FXML
//    private void showReport(ActionEvent event) throws SQLException {
//            lblRevenueCenter.setText(payRep.Center);
//            lblYear.setText(payRep.Year);
//            lblMonth.setText(payRep.Month);
//            tblBankDetails.getItems().clear();
//            String Date = "", GCR = "", Amount = "", bankName = "", chqNumber = "", chqDate = "";
//
//
//            stmnt = con.prepareStatement("SELECT `collection_payment_entries`.`gcr`, " +
//                    "`collection_payment_entries`.`date`, `cheque_details`.`cheque_date`, " +
//                    "`cheque_details`.`cheque_number`, `cheque_details`.`bank`, `cheque_details`.`amount` FROM" +
//                    " `cheque_details`, `collection_payment_entries` WHERE `collection_payment_entries`.`pay_revCenter` =" +
//                    " '"+payRep.Center+"' AND `collection_payment_entries`.`year`" +
//                    " = '"+payRep.Year+"' AND `collection_payment_entries`.`month` " +
//                    "= '"+payRep.Month+"' AND `collection_payment_entries`.`ID` =" +
//                    " `cheque_details`.`payment_ID`");
//            ResultSet rs = stmnt.executeQuery();
//            ResultSetMetaData rm = rs.getMetaData();
//            int col = rm.getColumnCount();
//
//            colDate.setCellValueFactory(data -> data.getValue().dateProperty());
//            colGCRNumber.setCellValueFactory(data -> data.getValue().GCRProperty());
//            colChqDate.setCellValueFactory(data -> data.getValue().chequeDateProperty());
//            colChqNumber.setCellValueFactory(data -> data.getValue().chequeNumberProperty());
//            colBank.setCellValueFactory(data -> data.getValue().bankProperty());
//            colAmount.setCellValueFactory(data -> data.getValue().amountProperty());
//            while(rs.next()){
//                Date = rs.getString("date");
//                GCR = rs.getString("gcr");
//                Amount = getFunctions.getAmount(rs.getString("amount"));
//                chqNumber = rs.getString("cheque_number");
//                chqDate = rs.getString("cheque_date");
//                bankName = rs.getString("bank");
//                getReport = new GetBankDetails(GCR, Date, chqDate, chqNumber, bankName, Amount);
//                tblBankDetails.getItems().add(getReport);
//            }
//        }


    @FXML
    void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    void SelectedYear(ActionEvent event) throws SQLException {
        getMonths();
    }

    @FXML
    void ShowReport(ActionEvent event) throws SQLException {
        if (cmbReportCent.getSelectionModel().isEmpty()){
            lblCenterWarn.setVisible(true);
        } else if (cmbReportYear.getSelectionModel().isEmpty()){
            lblYearWarn.setVisible(true);
        }else if (cmbReportMonth.getSelectionModel().isEmpty()){
            lblMonthWarn.setVisible(true);
        }else{
            tblBankDetails.getItems().clear();
            changeNames();
            setTable();
    }
    }

    void setTable() throws SQLException {
        String Center = centerID.get(cmbReportCent.getSelectionModel().getSelectedItem()), Date = "", GCR = "", Amount = "",
                bankName = "", chqNumber = "", chqDate = "", totalAmount="";
        double totCumAmount = 0;
        Months months = cmbReportMonth.getSelectionModel().getSelectedItem();
        int year = cmbReportYear.getSelectionModel().getSelectedItem();
        stmnt = con.prepareStatement("SELECT * FROM `cheque_details` WHERE `cheque_center` = '"+Center
                +"' AND YEAR(date_received) = '"+year+"' AND MONTH(date_received) = '"+months.getValue()+"'");
        ResultSet rs = stmnt.executeQuery();

        colDate.setCellValueFactory(data -> data.getValue().dateProperty());
        colReceivedFrom.setCellValueFactory(data -> data.getValue().GCRProperty());
        colChqDate.setCellValueFactory(data -> data.getValue().chequeDateProperty());
        colChqNumber.setCellValueFactory(data -> data.getValue().chequeNumberProperty());
        colBank.setCellValueFactory(data -> data.getValue().bankProperty());
        colAmount.setCellValueFactory(data -> data.getValue().amountProperty());
        colChqDate.setStyle("-fx-alignment: CENTER;-fx-wrap-text: TRUE;");
        colBank.setStyle("-fx-alignment: CENTER;-fx-wrap-text: TRUE;");
        colDate.setStyle("-fx-alignment: CENTER;-fx-wrap-text: TRUE;");
        colReceivedFrom.setStyle("-fx-alignment: CENTER;-fx-wrap-text: TRUE;");
        while(rs.next()){
            Date = getFunctions.convertSqlDate(rs.getString("date_received"));
            GCR = rs.getString("payer");
            Amount = getFunctions.getAmount(rs.getString("amount"));
            double totAmount = rs.getDouble("amount");
            totCumAmount += totAmount;
            chqNumber = rs.getString("cheque_number");
            chqDate = getFunctions.convertSqlDate(rs.getString("cheque_date"));
            bankName = rs.getString("bank");
            getReport = new GetBankDetails(GCR, Date, chqDate, chqNumber, bankName, Amount);
            tblBankDetails.getItems().add(getReport);
        }
        totalAmount = getFunctions.getAmount(Double.toString(totCumAmount));
        lblTotalAmount.setText(totalAmount);
    }


    @FXML
    void printReport(ActionEvent event) throws JRException, FileNotFoundException {
        if (tblBankDetails.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetBankDetails> items = new ArrayList<>();
            for (int j = 0; j < tblBankDetails.getItems().size(); j++) {
                GetBankDetails getdata = tblBankDetails.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/com/Assets/kmalogo.png");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = lblYear.getText(),
                    center = lblRevenueCenter.getText(),
                    month = lblMonth.getText();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url);
            parameters.put("year", year);
            parameters.put("timeStamp", date); parameters.put("month", month);
            parameters.put("center", center);


            //read jrxml file and creating jasperdesign object
            InputStream input = this.getClass().getResourceAsStream("/com/Assets/bankDetailsPotrait.jrxml");

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
       /* `collection_payment_entries`.`gcr`, " +
                "`collection_payment_entries`.`date`, `cheque_details`.`cheque_date`, " +
                "`cheque_details`.`cheque_number`, `cheque_details`.`bank`, `cheque_details`.`amount` FROM" +
                " `cheque_details`, `collection_payment_entries` WHERE `collection_payment_entries`.`pay_revCenter` =" +
                " '"+payRep.RevCenterID+"' AND `collection_payment_entries`.`year`" +
                " = '"+payRep.Year+"' AND `collection_payment_entries`.`month` " +
                "= '"+payRep.Month+"' AND `collection_payment_entries`.`pay_ID` =" +
                " `cheque_details`.`payment_ID`*/
