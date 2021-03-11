/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetTargAnalReport;
import java.math.BigDecimal;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class Target_AnalysisController implements Initializable {

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
    private TableColumn<?, ?> revenueCenter;
    @FXML
    private TableColumn<?, ?> year;
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
    
    private GetTargAnalReport getReport;
    
    
     
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    int Year;
    
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
        } catch (SQLException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    
    
    private void getRevCenters() throws SQLException, ClassNotFoundException{
        
            stmnt = con.prepareStatement("SELECT `revCenter` FROM `daily_entries` WHERE 1 GROUP BY `revCenter` ");
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
        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
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
    
    private void changeNames() {
        revenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        year.setText("Year: "+ cmbReportYear.getSelectionModel().getSelectedItem());
    } 
    
    private void setItems() throws SQLException {
        stmnt = con.prepareStatement(" SELECT `Amount`   FROM `target_entries` WHERE  `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `Year` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ResultSetMetaData meta= rs.getMetaData();
       float targAmount=0 ;
       String acTargAmount="";
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMinimumFractionDigits(4);
       int row = 0 ;        
       int col = meta.getColumnCount();
       while(rs.next()){
           for (int g = 1; g<= col; g++){
               if(g==1){
                   String amount = rs.getObject(g).toString();
                   targAmount = Float.parseFloat(amount);
                   
               }
           }
       }
           BigDecimal rp= new BigDecimal(targAmount);
       acTargAmount = formatter.format(targAmount);
       txtAnnualTarget.setText(acTargAmount);
       System.out.println(targAmount);
        ObservableList<String> collectionMonth = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");  
        float totRevenue, cumuRevenue = 0, outRevenue, cumuPercent, outPercent;
        String actotRevenue, acCumuRevenue = "", acOutRevenue = "", acCumPercent = "", acOutPercent = "";
        for(String month : collectionMonth){            
           totRevenue = setReptMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), month, cmbReportYear.getSelectionModel().getSelectedItem());
           cumuRevenue += totRevenue;
           cumuPercent = cumuRevenue / targAmount;
           outRevenue = targAmount - cumuRevenue;
           outPercent = outRevenue / targAmount;
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
           getReport = new GetTargAnalReport(month, actotRevenue, acCumuRevenue, acCumPercent,acOutRevenue, acOutPercent);
           tblColPayAnalysis.getItems().add(getReport);
           System.out.println(totRevenue +" "+cumuRevenue+" "+outRevenue+" "+cumuPercent+ " "+outPercent);
           totRevenue = 0;
        
    }
   }
         
       public Float setReptMonthSum(String Center, String Month, String Year) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE `revenueMonth` = '"+Month+"' AND `revCenter` = '"+Center+"' AND `revenueYear` = '"+Year+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ResultSetMetaData meta= rs.getMetaData();
       int row = 0 ;        
       int col = meta.getColumnCount();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           for(int j=1; j<=col; j++){
               if(j == 1){
           String revitem =rs.getObject(j).toString();
           BigDecimal rp= new BigDecimal(Float.parseFloat(revitem));
           rp.stripTrailingZeros().toPlainString();
//           int rev = Integer.parseInt(revitem);
           Amount.add(Float.parseFloat(revitem));//adding revenue items to list
           System.out.println(rp.stripTrailingZeros().toPlainString());
           }
           }     
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
    
}
