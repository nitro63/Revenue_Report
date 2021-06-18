/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetTargAnalReport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class Target_AnalysisController implements Initializable {

    @FXML
    private Label lblMonth;
    @FXML
    private Label lblYear;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private Label lblRevenueCenter;
    @FXML
    private VBox monthlyTemplate;
    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private ComboBox<String> cmbReportYear;
    @FXML
    private Button btnShowReport;
    @FXML
    private Text txtAnnualTarget;
    @FXML
    private TableView<GetTargAnalReport> tblColPayAnalysis;
    @FXML
    private TableColumn<GetTargAnalReport, String> colMonth;
    @FXML
    private TableColumn<GetTargAnalReport, String> colTolRev;
    @FXML
    private TableColumn<GetTargAnalReport, String> colAmtAchv;
    @FXML
    private TableColumn<GetTargAnalReport, String> colPercAchv;
    @FXML
    private TableColumn<GetTargAnalReport, String> colAmtOut;
    @FXML
    private TableColumn<GetTargAnalReport, String> colPercOut;


    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    String targ;
    Map<String, String> centerID = new HashMap<>();
    boolean subMetroPR, Condition;
    
    public Target_AnalysisController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getRevCenters();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    
    
    private void getRevCenters() throws SQLException, ClassNotFoundException{
            stmnt = con.prepareStatement("SELECT `target_revCenter`, `revenue_centers`.`revenue_category`, `revenue_centers`.`revenue_center` FROM `target_entries`, `revenue_centers` WHERE `revenue_centers`.`CenterID` = `target_revCenter` GROUP BY `target_revCenter` ");
         ResultSet rs = stmnt.executeQuery();
         while(rs.next()){
             rowCent.add(rs.getString("revenue_center"));
             centerID.put(rs.getString("revenue_center"), rs.getString("target_revCenter"));
             if (rs.getString("revenue_category").equals("PROPERTY RATE SECTION")){
                 Condition = true;
             }
             if (rs.getString("target_revCenter").equals("K0201") || rs.getString("target_revCenter").equals("K0202") || rs.getString("target_revCenter").equals("K0203") || rs.getString("target_revCenter").equals("K0204") || rs.getString("target_revCenter").equals("K0205")){
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
    
         
    }
    
     
    private void getReportYear() throws SQLException{
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `Year` FROM `revenue_centers`,`target_entries` WHERE `revenue_centers`.`CenterID` = `target_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `Year`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `Year` FROM `target_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `target_revCenter` AND `target_revCenter` = 'K0201' OR `target_revCenter` = 'K0202' OR `target_revCenter` = 'K0203' OR `target_revCenter` = 'K0204' OR `target_revCenter` = 'K0205' GROUP BY `Year`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `Year` FROM `target_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `target_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `Year`");
        }
//        stmnt = con.prepareStatement(" SELECT `Year` FROM `target_entries`, `revenue_centers` WHERE `revenue_centers`.`CenterID` = `target_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `Year`");
        ResultSet rs = stmnt.executeQuery();
        rowYear.clear();
        while(rs.next()){
            rowYear.add(rs.getString("Year"));
        }
        cmbReportYear.getItems().clear();
        cmbReportYear.getItems().setAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
    }
    
    private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem());
    } 
    
    private void setItems() throws SQLException {
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `Amount` FROM `revenue_centers`,`target_entries` WHERE `revenue_centers`.`CenterID` = `target_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `Amount` FROM `target_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `target_revCenter` AND `target_revCenter` = 'K0201' OR `target_revCenter` = 'K0202' OR `target_revCenter` = 'K0203' OR `target_revCenter` = 'K0204' OR `target_revCenter` = 'K0205' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `Amount` FROM `target_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `target_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'");
        }
//        stmnt = con.prepareStatement(" SELECT `Amount`   FROM `target_entries`,`revenue_centers` WHERE  `revenue_centers`.`CenterID` = `target_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'");
       ResultSet rs = stmnt.executeQuery();
       float targAmount=0 ;
       String acTargAmount="";
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        NumberFormat percent = new DecimalFormat("0.00%");
//        NumberFormat percent = NumberFormat.getPercentInstance();
//        percent.setMinimumFractionDigits(4);
       while(rs.next()){
                   targAmount = rs.getFloat("Amount");
       }
       acTargAmount = formatter.format(targAmount);
       targ = acTargAmount;
       txtAnnualTarget.setText(acTargAmount);
       System.out.println(targAmount);
        ObservableList<String> collectionMonth = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");  
        float totRevenue, cumuRevenue = 0, outRevenue, cumuPercent, outPercent;
        String actotRevenue, acCumuRevenue = "", acOutRevenue = "", acCumPercent = "", acOutPercent = "";
        for(String month : collectionMonth){            
           totRevenue = setReptMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), month, cmbReportYear.getSelectionModel().getSelectedItem());
           cumuRevenue += totRevenue;
           cumuPercent = (cumuRevenue / targAmount);
           outRevenue = targAmount - cumuRevenue;
           outPercent = (outRevenue / targAmount);
           if (outRevenue<0){               
           acOutRevenue = "("+formatter.format(Math.abs(outRevenue))+")";
           acOutPercent = "("+percent.format(Math.abs(outPercent))+")";
           }else{
               acOutRevenue = formatter.format(outRevenue);
               acOutPercent = percent.format(outPercent);
           } 
           if(cumuRevenue<0){
           acCumuRevenue = "("+formatter.format(Math.abs(cumuRevenue))+")"; 
           acCumPercent = "("+percent.format(Math.abs(cumuPercent))+")";              
           }else{
               acCumuRevenue = formatter.format(cumuRevenue);
               
               acCumPercent = percent.format(cumuPercent);
               
           }
           
           actotRevenue = formatter.format(Math.abs(totRevenue));
           
           colMonth.setCellValueFactory(data -> data.getValue().monthProperty());
           colTolRev.setCellValueFactory(data -> data.getValue().totalRevenueProperty());
           colAmtAchv.setCellValueFactory(data -> data.getValue().achvAmtProperty());
           colPercAchv.setCellValueFactory(data -> data.getValue().achvPercentProperty());
           colAmtOut.setCellValueFactory(data -> data.getValue().outAmtProperty());
           colPercOut.setCellValueFactory(data -> data.getValue().outPercentProperty());
            GetTargAnalReport getReport = new GetTargAnalReport(month, actotRevenue, acCumuRevenue, acCumPercent, acOutRevenue, acOutPercent);
           tblColPayAnalysis.getItems().add(getReport);
           totRevenue = 0;
           }
       }

         
       public Float setReptMonthSum(String Center, String Month, String Year) throws SQLException{
        float totalAmunt;
           if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
               stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueMonth`= '"+Month+"'");
           } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
               stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_revCenter` AND `target_revCenter` = 'K0201' OR `target_revCenter` = 'K0202' OR `target_revCenter` = 'K0203' OR `target_revCenter` = 'K0204' OR `target_revCenter` = 'K0205' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueMonth`= '"+Month+"'");
           }
           else {
               stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_revCenter` AND `revenue_centers`.`revenue_center` = '"+Center+"'AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueMonth`= '"+Month+"'");
           }
//       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_revCenter` AND `revenue_centers`.`revenue_center` = '"+Center+"' AND `revenueMonth` = '"+Month+"' AND `revenueYear` = '"+Year+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           Amount.add(rs.getFloat("revenueAmount"));//adding revenue items to list
       }
        totalAmunt = 0;
        if(Amount.isEmpty()){
            totalAmunt = 0;
        }else{
            for(int i = 0; i < Amount.size(); i++){
            totalAmunt += Amount.get(i);
            }
        }
        return totalAmunt;
    }

    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        tblColPayAnalysis.getItems().clear();
        changeNames();
        setItems();
    }

    @FXML
    void printReport(ActionEvent event) throws JRException, FileNotFoundException {
        if (tblColPayAnalysis.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetTargAnalReport> items = new ArrayList<>();
            for (int j = 0; j < tblColPayAnalysis.getItems().size(); j++) {
                GetTargAnalReport getdata;
                getdata = tblColPayAnalysis.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmbReportYear.getSelectionModel().getSelectedItem(),
                    center = cmbReportCent.getSelectionModel().getSelectedItem();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url);
            parameters.put("year", year);
            parameters.put("timeStamp", date);
            parameters.put("center", center); parameters.put("target", targ);

            //read jrxml file and creating jasperdesign object
            InputStream input = this.getClass().getResourceAsStream("/Assets/targetAnalysisPotrait.jrxml");

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
