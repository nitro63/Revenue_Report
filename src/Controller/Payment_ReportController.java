/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetPaymentDetails;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
    private TableColumn<?, ?> revenueCenter;
    @FXML
    private TableColumn<?, ?> year;
    @FXML
    private ComboBox<String> cmbReportMonth;
    @FXML
    private TableView<GetPaymentDetails> tblPaymentDetails;
    @FXML
    private TableColumn<?, ?> MONTH;
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
    
    private GetPaymentDetails getReport;
    
    
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    int Year;

    
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
        
            stmnt = con.prepareStatement("SELECT `revCenter` FROM `collection_payment_entries` WHERE 1 GROUP BY `revCenter` ");
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
        stmnt = con.prepareStatement(" SELECT `Year` FROM `collection_payment_entries` WHERE `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `Year`");
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
        System.out.println(year);
        stmnt = con.prepareStatement(" SELECT `Month` FROM `collection_payment_entries` WHERE  `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `Month`");
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
        revenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        year.setText("Year: "+ cmbReportYear.getSelectionModel().getSelectedItem());
        MONTH.setText("Month: "+cmbReportMonth.getSelectionModel().getSelectedItem());
    } 
    
    private void setItems() throws SQLException{
        String Date = "", GCR = "", payment_type = "", acAmount = "", acCumuAmount = "";
        float amount = 0, cumuAmount = 0;
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        stmnt = con.prepareStatement("SELECT `GCR`,`Date`,`payment_type`,`Amount` FROM `collection_payment_entries` WHERE `Month`= '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData rm = rs.getMetaData();
        int col = rm.getColumnCount();
        
        colDate.setCellValueFactory(data -> data.getValue().dateProperty());
        colGCR.setCellValueFactory(data -> data.getValue().GCRProperty());
        colAmount.setCellValueFactory(data -> data.getValue().amountProperty());
        colPaymentType.setCellValueFactory(data -> data.getValue().paymentTypeProperty());
        colTotalAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
        while(rs.next()){
            for(int i = 1; i<= col; i++){
                switch (i) {
                    case 1:
                        GCR = rs.getObject(i).toString();
                        break;
                    case 2:
                        Date = rs.getObject(i).toString();
                        break;
                    case 3:
                        payment_type = rs.getObject(i).toString();
                        break;
                    case 4:
                        amount = Float.parseFloat(rs.getObject(i).toString());
                        acAmount = formatter.format(amount);
                        cumuAmount+= amount;
                        acCumuAmount= formatter.format(cumuAmount);
                        break;
                    default:
                        break;
                }
            }
            getReport = new GetPaymentDetails(Date, GCR, payment_type, acAmount, acCumuAmount);
            tblPaymentDetails.getItems().add(getReport);
            System.out.println(GCR+" "+Date+" "+payment_type+" "+amount+" "+cumuAmount);
        }
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
    void printReport(ActionEvent event) {

    }

    @FXML
    void showBankDetails(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        if (tblPaymentDetails.getItems().isEmpty()){
            event.consume();
        }else {
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
        }
    }


}
