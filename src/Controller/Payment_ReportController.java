/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetPaymentDetails;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import Controller.Gets.GetYearlyReport;
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
import revenue_report.DBConnection;
import revenue_report.Main;

/**
 * FXML Controller class
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
    private ComboBox<String> cmbReportCent;
    @FXML
    private ComboBox<String> cmbReportYear;
    @FXML
    private Button btnShowReport;
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
    
    private GetPaymentDetails getReport;
    
    
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    String Year, Month, Center;
    boolean cond = false;

    
    public Payment_ReportController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getRevCenters();
        } catch (SQLException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    
    private void getRevCenters() throws SQLException, ClassNotFoundException{
        
            stmnt = con.prepareStatement("SELECT `pay_revCenter` FROM `collection_payment_entries` WHERE 1 GROUP BY `pay_revCenter` ");
         ResultSet rs = stmnt.executeQuery();
         ResultSetMetaData metadata = rs.getMetaData();
         int columns = metadata.getColumnCount();
         
         while(rs.next()){
             for(int i= 1; i<=columns; i++)
             {
                 String value = rs.getObject(i).toString();
                 rowCent.add(value);
                 
             }
         }
         cmbReportCent.getItems().clear();
         cmbReportCent.setItems(rowCent);
         cmbReportCent.setVisibleRowCount(5);
    
         
    }
    
    private void getReportYear() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `Year` FROM `collection_payment_entries` WHERE `pay_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `Year`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowYear.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 String value = rs.getObject(i).toString();
                 
                 rowYear.add(value);
                 
             }
        }
        cmbReportYear.getItems().clear();
        cmbReportYear.getItems().setAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
    }
    
    private void getMonths() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `Month` FROM `collection_payment_entries` WHERE  `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `pay_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowMonths.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 String value = rs.getObject(i).toString();
                 
                 rowMonths.add(value);
                 
             }
        }
        cmbReportMonth.getItems().clear();
        cmbReportMonth.getItems().setAll(rowMonths);
        cmbReportMonth.setVisibleRowCount(5);
    }
    
    private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem());
        lblMonth.setText(cmbReportMonth.getSelectionModel().getSelectedItem());
    } 
    
    private void setItems() throws SQLException{
        String Date = "", GCR = "", payment_type = "", acAmount = "", acCumuAmount = "";
        float amount = 0, cumuAmount = 0;
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        stmnt = con.prepareStatement("SELECT `GCR`,`Date`,`payment_type`,`Amount` FROM `collection_payment_entries` WHERE `Month`= '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `pay_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData rm = rs.getMetaData();
        int col = rm.getColumnCount();
        
        colDate.setCellValueFactory(data -> data.getValue().dateProperty());
        colGCR.setCellValueFactory(data -> data.getValue().GCRProperty());
        colAmount.setCellValueFactory(data -> data.getValue().amountProperty());
        colPaymentType.setCellValueFactory(data -> data.getValue().paymentTypeProperty());
        colTotalAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
        while(rs.next()){
            GCR = rs.getString("GCR");
            Date = rs.getString("Date");
            payment_type = rs.getString("payment_type");
            amount = rs.getFloat("Amount");
            acAmount = rs.getString("Amount");
            cumuAmount+= amount;
            acCumuAmount= formatter.format(cumuAmount);
            if (payment_type.equals("Cheque") || payment_type.equals("Cheque Deposit Slip")){
                cond = true;
            }
            }
            getReport = new GetPaymentDetails(Date, GCR, payment_type, acAmount, acCumuAmount);
            tblPaymentDetails.getItems().add(getReport);
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
        tblPaymentDetails.getItems().clear();
        changeNames();
        setItems();
    }

    @FXML
    void printReport(ActionEvent event) throws FileNotFoundException, JRException {
        if (tblPaymentDetails.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetPaymentDetails> items = new ArrayList<GetPaymentDetails>();
            for (int j = 0; j < tblPaymentDetails.getItems().size(); j++) {
                GetPaymentDetails getdata = new GetPaymentDetails();
                getdata = tblPaymentDetails.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                    file = this.getClass().getResource("/Assets/paymentPotrait.jrxml");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmbReportYear.getSelectionModel().getSelectedItem(),
                    center = cmbReportCent.getSelectionModel().getSelectedItem(),
                    month = cmbReportMonth.getSelectionModel().getSelectedItem();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url);
            parameters.put("year", year);
            parameters.put("timeStamp", date); parameters.put("month", month);
            parameters.put("center", center);


            //read jrxml file and creating jasperdesign object
            InputStream input = new FileInputStream(new File(file.getPath()));

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
    void showBankDetails(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        if (tblPaymentDetails.getItems().isEmpty()){
            event.consume();
        }else {
            if (cond){
            Year = cmbReportYear.getSelectionModel().getSelectedItem();
            Center = cmbReportCent.getSelectionModel().getSelectedItem();
            Month = cmbReportMonth.getSelectionModel().getSelectedItem();
            Main st = new Main();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Views/fxml/BankDetailsReport.fxml"));
            loader.setController(new BankDetailsReportController());
            BankDetailsReportController bnkDtls = (BankDetailsReportController) loader.getController();
            bnkDtls.setAppController(this);
            Parent root = loader.load();
            Scene s = new Scene(root);
            Stage stg = new Stage();
            bnkDtls.setStage(stg);
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.initOwner(st.stage);
            stg.initStyle(StageStyle.UTILITY);
            stg.setScene(s);
            stg.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Notice!");
                alert.setHeaderText("There is no Bank Details for this record!");
                alert.showAndWait();
            }
        }
    }


}
