/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller.Reports;

import com.Models.GetPaymentDetails;
import com.Controller.InitializerController;
import com.Controller.LogInController;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import com.revenue_report.DBConnection;
import com.revenue_report.Main;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML com.Controller class
 *
 * @author NiTrO
 */
public class Payment_ReportController implements Initializable {

    @FXML
    private VBox monthlyTemplate;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private JFXButton btnBankDetails;
    @FXML
    private Label lblCenterWarn;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblMonthWarn;
    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private ComboBox<Integer> cmbReportYear;
    @FXML
    private JFXButton btnShowReport;
    @FXML
    private ComboBox<String> cmbReportMonth;
    @FXML
    private TableView<GetPaymentDetails> tblPaymentDetails;
    @FXML
    private TableColumn<GetPaymentDetails, String> colDate;
    @FXML
    private TableColumn<GetPaymentDetails, String> colGCR;
    @FXML
    private TableColumn<GetPaymentDetails, String> colPaymentType;
    @FXML
    private TableColumn<GetPaymentDetails, String> colAmount;
    @FXML
    private TableColumn<GetPaymentDetails, String> colTotalAmount;
    @FXML
    private Label lblMonth;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblRevenueCenter;
    @FXML
    private Label lblTotalAmount;


    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<Integer> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    Map<String, String> centerID = new HashMap<>();

    String Year, Month, Center, RevCenterID, SelectedCenter;
    boolean cond = false, subMetroPR, Condition;

    
    public Payment_ReportController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    
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
            getRevCenters();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (LogInController.hasCenter){
            try {
                getReportYear();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }   
    
    
    private void getRevCenters() throws SQLException, ClassNotFoundException
    {
            stmnt = con.prepareStatement("SELECT `pay_revCenter`, `revenue_centers`.`revenue_category`, `revenue_centers`.`revenue_center` FROM `revenue_centers`,`collection_payment_entries` WHERE `revenue_centers`.`CenterID` = `collection_payment_entries`.pay_revCenter GROUP BY `pay_revCenter` ");
         ResultSet rs = stmnt.executeQuery();
         
         while(rs.next()){
             rowCent.add(rs.getString("revenue_center"));
             centerID.put(rs.getString("revenue_center"), rs.getString("pay_revCenter"));
             if (rs.getString("revenue_category").equals("PROPERTY RATE SECTION")){
                 Condition = true;
             }
             if (rs.getString("pay_revCenter").equals("K0201") || rs.getString("pay_revCenter").equals("K0202") || rs.getString("pay_revCenter").equals("K0203") || rs.getString("pay_revCenter").equals("K0204") || rs.getString("pay_revCenter").equals("K0205")){
                 subMetroPR = true;
             }
         }
        if (Condition){
            rowCent.add("PROPERTY RATE ALL");
        }
        if (subMetroPR){
            rowCent.add("PROPERTY RATE SUB-METROS");
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
            stmnt = con.prepareStatement(" SELECT `Year` FROM `revenue_centers`,`collection_payment_entries` WHERE `revenue_centers`.`CenterID` = `pay_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `Year`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `Year` FROM `collection_payment_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `pay_revCenter` AND `pay_revCenter` = 'K0201' OR `pay_revCenter` = 'K0202' OR `pay_revCenter` = 'K0203' OR `pay_revCenter` = 'K0204' OR `pay_revCenter` = 'K0205' GROUP BY `Year`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `Year` FROM `collection_payment_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `pay_revCenter` AND`revenue_centers`.`revenue_center` = '"+SelectedCenter+"'  GROUP BY `Year`");
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
            stmnt = con.prepareStatement(" SELECT `Month` FROM `revenue_centers`,`collection_payment_entries` WHERE `revenue_centers`.`CenterID` = `pay_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `Month` FROM `collection_payment_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `pay_revCenter` AND `pay_revCenter` = 'K0201' OR `pay_revCenter` = 'K0202' OR `pay_revCenter` = 'K0203' OR `pay_revCenter` = 'K0204' OR `pay_revCenter` = 'K0205' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `Month` FROM `collection_payment_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `pay_revCenter` AND`revenue_centers`.`revenue_center` = '"+SelectedCenter+"' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
        }
//        stmnt = con.prepareStatement(" SELECT `Month` FROM `collection_payment_entries`,`revenue_centers` WHERE  `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenue_centers`.`CenterID` = `collection_payment_entries`.`pay_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
        ResultSet rs = stmnt.executeQuery();
        rowMonths.clear();
        while(rs.next()){
            rowMonths.add(rs.getString("Month"));
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
    
    private void setItems() throws SQLException{
        String Date, GCR, payment_type, acAmount, acCumuAmount = "";
        float amount, cumuAmount = 0;
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `GCR`,`Date`,`payment_type`,`Amount` FROM `revenue_centers`,`collection_payment_entries` WHERE `revenue_centers`.`CenterID` = pay_revCenter AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `Month` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' ORDER BY `Date`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `GCR`,`Date`,`payment_type`,`Amount` FROM `collection_payment_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = pay_revCenter AND `pay_revCenter` = 'K0201' OR `pay_revCenter` = 'K0202' OR `pay_revCenter` = 'K0203' OR `pay_revCenter` = 'K0204' OR `pay_revCenter` = 'K0205' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `Month` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' ORDER BY `Date`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `GCR`,`Date`,`payment_type`,`Amount` FROM `collection_payment_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `pay_revCenter` AND `revenue_centers`.`revenue_center` = '"+SelectedCenter+"'AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `Month` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' ORDER BY `Date`");
        }
//        stmnt = con.prepareStatement("SELECT `GCR`,`Date`,`payment_type`,`Amount` FROM `collection_payment_entries` WHERE `Month`= '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `pay_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'");
        ResultSet rs = stmnt.executeQuery();
        
        colDate.setCellValueFactory(data -> data.getValue().dateProperty());
        colGCR.setCellValueFactory(data -> data.getValue().GCRProperty());
        colAmount.setCellValueFactory(data -> data.getValue().amountProperty());
        colPaymentType.setCellValueFactory(data -> data.getValue().paymentTypeProperty());
        colTotalAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
        colDate.setStyle( "-fx-alignment: CENTER-LEFT;");
        colGCR.setStyle( "-fx-alignment: CENTER-LEFT;");
        colPaymentType.setStyle( "-fx-alignment: CENTER-LEFT;");
        while(rs.next()){
            GCR = rs.getString("GCR");
            Date = rs.getString("Date");
            payment_type = rs.getString("payment_type");
            amount = rs.getFloat("Amount");
            acAmount = formatter.format(rs.getFloat("Amount"));
            cumuAmount+= amount;
            acCumuAmount= formatter.format(cumuAmount);
            if (payment_type.equals("Cheque") || payment_type.equals("Cheque Deposit Slip")){
                cond = true;
            }
            GetPaymentDetails getReport = new GetPaymentDetails(Date, GCR, payment_type, acAmount, acCumuAmount);
            tblPaymentDetails.getItems().add(getReport);
            }
        lblTotalAmount.setText(acCumuAmount);
        }


    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void SelectedYear(ActionEvent event) throws SQLException {
        getMonths();
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        if (cmbReportCent.getSelectionModel().isEmpty()){
            lblCenterWarn.setVisible(true);
        } else if (cmbReportYear.getSelectionModel().isEmpty()){
            lblYearWarn.setVisible(true);
        }else if (cmbReportMonth.getSelectionModel().isEmpty()){
            lblMonthWarn.setVisible(true);
        }else{
        tblPaymentDetails.getItems().clear();
        changeNames();
        setItems();}
    }

    @FXML
    void printReport(ActionEvent event) throws FileNotFoundException, JRException {
        if (tblPaymentDetails.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetPaymentDetails> items = new ArrayList<>();
            for (int j = 0; j < tblPaymentDetails.getItems().size(); j++) {
                GetPaymentDetails getdata = tblPaymentDetails.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/com/Assets/kmalogo.png");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmbReportYear.getSelectionModel().getSelectedItem().toString(),
                    center = cmbReportCent.getSelectionModel().getSelectedItem(),
                    month = cmbReportMonth.getSelectionModel().getSelectedItem();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url);
            parameters.put("year", year);
            parameters.put("timeStamp", date); parameters.put("month", month);
            parameters.put("center", center);


            //read jrxml file and creating jasperdesign object
            InputStream input = this.getClass().getResourceAsStream("/com/Assets/paymentPotrait.jrxml");

            JasperDesign jasperDesign = JRXmlLoader.load(input);

            /*compiling jrxml with help of JasperReport class*/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            /* Using jasperReport object to generate PDF */
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            /*call jasper engine to display report in jasperviewer window*/
            JasperViewer.viewReport(jasperPrint, false);
        }
    }

    @FXML
    void showBankDetails(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {/*
        if (tblPaymentDetails.getItems().isEmpty()){
            event.consume();
        }else {
            if (cond){*//*
            Year = cmbReportYear.getSelectionModel().getSelectedItem().toString();
            Center = cmbReportCent.getSelectionModel().getSelectedItem();
            Month = cmbReportMonth.getSelectionModel().getSelectedItem();
            RevCenterID = centerID.get(Center);*/
            Main st = new Main();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/Views/fxml/BankDetailsReport.fxml"));
            loader.setController(new BankDetailsReportController());
            BankDetailsReportController bnkDtls = loader.getController();
            bnkDtls.setAppController(this);
            Parent root = loader.load();
            Scene s = new Scene(root);
            Stage stg = new Stage();
            bnkDtls.setStage(stg);
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.initOwner(st.stage);
            stg.initStyle(StageStyle.UTILITY);
            stg.setScene(s);
            stg.show();/*
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Notice!");
                alert.setHeaderText("There is no Bank Details for this record!");
                alert.showAndWait();
            }
        }*/
    }


}
