/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetBankDetails;
import Controller.Gets.GetFunctions;
import Controller.Gets.GetPaymentDetails;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
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
    private JFXButton btnPrint;
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
    Payment_ReportController payRep;
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent = FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();

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
        try {
            setTable();
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

    void setTable() throws SQLException {
        lblRevenueCenter.setText(payRep.Center);
        lblYear.setText(payRep.Year);
        lblMonth.setText(payRep.Month);
        tblBankDetails.getItems().clear();
        String Date = "", GCR = "", Amount = "", bankName = "", chqNumber = "", chqDate = "";


        stmnt = con.prepareStatement("SELECT `collection_payment_entries`.`gcr`, " +
                "`collection_payment_entries`.`date`, `cheque_details`.`cheque_date`, " +
                "`cheque_details`.`cheque_number`, `cheque_details`.`bank`, `cheque_details`.`amount` FROM" +
                " `cheque_details`, `collection_payment_entries` WHERE `collection_payment_entries`.`pay_revCenter` =" +
                " '"+payRep.RevCenterID+"' AND `collection_payment_entries`.`year`" +
                " = '"+payRep.Year+"' AND `collection_payment_entries`.`month` " +
                "= '"+payRep.Month+"' AND `collection_payment_entries`.`pay_ID` =" +
                " `cheque_details`.`payment_ID`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData rm = rs.getMetaData();
        int col = rm.getColumnCount();

        colDate.setCellValueFactory(data -> data.getValue().dateProperty());
        colGCRNumber.setCellValueFactory(data -> data.getValue().GCRProperty());
        colChqDate.setCellValueFactory(data -> data.getValue().chequeDateProperty());
        colChqNumber.setCellValueFactory(data -> data.getValue().chequeNumberProperty());
        colBank.setCellValueFactory(data -> data.getValue().bankProperty());
        colAmount.setCellValueFactory(data -> data.getValue().amountProperty());
        while(rs.next()){
            Date = rs.getString("date");
            GCR = rs.getString("gcr");
            Amount = getFunctions.getAmount(rs.getString("amount"));
            chqNumber = rs.getString("cheque_number");
            chqDate = rs.getString("cheque_date");
            bankName = rs.getString("bank");
            getReport = new GetBankDetails(GCR, Date, chqDate, chqNumber, bankName, Amount);
            tblBankDetails.getItems().add(getReport);
        }}


    @FXML
    void printReport(ActionEvent event) throws JRException, FileNotFoundException {
        if (tblBankDetails.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = null;
            List<GetBankDetails> items = new ArrayList<>();
            for (int j = 0; j < tblBankDetails.getItems().size(); j++) {
                new GetBankDetails();
                GetBankDetails getdata = tblBankDetails.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                    file = this.getClass().getResource("/Assets/bankDetailsPotrait.jrxml");

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
            InputStream input = new FileInputStream(file.getPath());

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
