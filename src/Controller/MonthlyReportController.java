/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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

import Controller.Gets.GetItemsReport;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import revenue_report.DBConnection;
import Controller.Gets.GetMonthlyReport;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class MonthlyReportController implements Initializable {

    @FXML
    private VBox monthlyTemplate;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private ComboBox<String> cmbReportYear;
    @FXML
    private Button btnShowReport;
    @FXML
    private TableView<GetMonthlyReport> monthlyTable;
    @FXML
    private TableColumn<?, ?> revenueCenter;
    @FXML
    private TableColumn<GetMonthlyReport, String> revenueItem;
    @FXML
    private TableColumn<?, ?> year;
    @FXML
    private TableColumn<GetMonthlyReport, String> january;
    @FXML
    private TableColumn<GetMonthlyReport, String> february;
    @FXML
    private TableColumn<GetMonthlyReport, String> march;
    @FXML
    private TableColumn<GetMonthlyReport, String> april;
    @FXML
    private TableColumn<GetMonthlyReport, String> may;
    @FXML
    private TableColumn<GetMonthlyReport, String> june;
    @FXML
    private TableColumn<GetMonthlyReport, String> july;
    @FXML
    private TableColumn<GetMonthlyReport, String> august;
    @FXML
    private TableColumn<GetMonthlyReport, String> september;
    @FXML
    private TableColumn<GetMonthlyReport, String> october;
    @FXML
    private TableColumn<GetMonthlyReport, String> november;
    @FXML
    private TableColumn<GetMonthlyReport, String> december;
    @FXML
    private TableColumn<GetMonthlyReport, String> totalAmount;
    @FXML
    private Label lblCenterWarn;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblRevenueCenter;
    
    private GetMonthlyReport getReport;

    /**
     * Initializes the controller class.
     */
    
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    int Year;
    
    public MonthlyReportController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbReportCent.setOnMouseClicked(e -> {
            lblCenterWarn.setVisible(false);
        });
        cmbReportYear.setOnMouseClicked(e -> {
            lblYearWarn.setVisible(false);
        });
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
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem());
    }
    
    private void setItems() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int col = meta.getColumnCount();
        rowItems.clear();
        while(rs.next()){
            for(int i=1; i<=col; i++){
                if(i == 1){
                    
                    rowItems.add(rs.getObject(i).toString());
                    
                }
            }
        }        
        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueMonth`");
        ResultSet Rs = stmnt.executeQuery();
        ResultSetMetaData Meta = Rs.getMetaData();
        int Col = Meta.getColumnCount();
        rowMonths.clear();
        while(Rs.next()){
            for(int i=1; i<=Col; i++){
                if(i == 1){
                    
                    rowMonths.add(Rs.getObject(i).toString());
                    
                }
            }
        }        
          Map<String, ArrayList<Float>> monthAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
          Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview 
          rowItems.forEach((rowItem) -> {
              forEntry.put(rowItem, new HashMap());
      });
          rowMonths.forEach((rowMonth) -> {
              monthAmount.put(rowMonth, new ArrayList());
          });
          try {
          for(String month : rowMonths) {
              for(String Item : rowItems) {
                  float monthSum;
                  monthSum = setMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), Item, month, cmbReportYear.getSelectionModel().getSelectedItem());
                  for(Map.Entry<String, ArrayList<Float>> Dates : monthAmount.entrySet()){
                          for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
                              if (Items.getKey().equals(Item)  && Dates.getKey().equals(month)){
                                  if(forEntry.containsKey(Item) && !forEntry.get(Item).containsValue(month)){
                                      forEntry.get(Item).put(month, new ArrayList());
                                      forEntry.get(Item).get(month).add(monthSum);
                                  }else if(forEntry.containsKey(Item) && forEntry.get(Item).containsValue(month)){
                                      forEntry.get(Item).get(month).add(monthSum);
                                  } 
                              };
                          };
                      }
                  
              }
          }
         }
                  catch (SQLException ex) {
                      Logger.getLogger(weeklyReportController.class.getName()).log(Level.SEVERE, null, ex);
                  }
          NumberFormat formatter = new DecimalFormat("#,##0.00");
         
       for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
           String jan1 = "0.00", feb1 = "0.00", apr1 = "0.00", mai1 = "0.00", jun1 = "0.00", jul1 = "0.00", aug1 = "0.00", sep1 = "0.00",
                   oct1 = "0.00", mar1 = "0.00", nov1 = "0.00", dec1 = "0.00", totalAmnt = "0.00";
           float jan = 0, feb = 0, apr = 0, mai = 0, jun = 0, jul = 0, aug = 0, sep = 0, oct = 0, mar = 0, nov = 0, dec = 0, total_amount;
           for(Map.Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
               String reveItem = Items.getKey();
               System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
               if(Dates.getKey() == null ? january.getText() == null : Dates.getKey().equals(january.getText())){
                   jan1 = formatter.format(forEntry.get(Items.getKey()).get(january.getText()).get(0));
                   jan = forEntry.get(Items.getKey()).get(january.getText()).get(0);
               }
               else if(Dates.getKey() == null ? february.getText() == null : Dates.getKey().equals(february.getText())){
                   feb1 = formatter.format(forEntry.get(Items.getKey()).get(february.getText()).get(0));
                   feb = forEntry.get(Items.getKey()).get(february.getText()).get(0);
               }
               else if(Dates.getKey() == null ? march.getText() == null : Dates.getKey().equals(march.getText())){
                   mar1 = formatter.format(forEntry.get(Items.getKey()).get(march.getText()).get(0));
                   mar = forEntry.get(Items.getKey()).get(march.getText()).get(0);
               }
               else if(Dates.getKey() == null ? april.getText() == null : Dates.getKey().equals(april.getText())){
                   apr1 = formatter.format(forEntry.get(Items.getKey()).get(april.getText()).get(0));
                   apr = forEntry.get(Items.getKey()).get(april.getText()).get(0);
               }
               else if(Dates.getKey() == null ? may.getText() == null : Dates.getKey().equals(may.getText())){
                   mai1 = formatter.format(forEntry.get(Items.getKey()).get(may.getText()).get(0));
                   mai = forEntry.get(Items.getKey()).get(may.getText()).get(0);
               }
               else if(Dates.getKey() == null ? june.getText() == null : Dates.getKey().equals(june.getText())){
                   jun1 = formatter.format(forEntry.get(Items.getKey()).get(june.getText()).get(0));
                   jun = forEntry.get(Items.getKey()).get(june.getText()).get(0);
               }
               else if(Dates.getKey() == null ? july.getText() == null : Dates.getKey().equals(july.getText())){
                   jul1 = formatter.format(forEntry.get(Items.getKey()).get(july.getText()).get(0));
                   jul = forEntry.get(Items.getKey()).get(july.getText()).get(0);
               }
               else if(Dates.getKey() == null ? august.getText() == null : Dates.getKey().equals(august.getText())){
                   aug1 = formatter.format(forEntry.get(Items.getKey()).get(august.getText()).get(0));
                   aug = forEntry.get(Items.getKey()).get(august.getText()).get(0);
               }
               else if(Dates.getKey() == null ? september.getText() == null : Dates.getKey().equals(september.getText())){
                   sep1 = formatter.format(forEntry.get(Items.getKey()).get(september.getText()).get(0));
                   sep = forEntry.get(Items.getKey()).get(september.getText()).get(0);
               }
               else if(Dates.getKey() == null ? october.getText() == null : Dates.getKey().equals(october.getText())){
                   oct1 = formatter.format(forEntry.get(Items.getKey()).get(october.getText()).get(0));
                   oct = forEntry.get(Items.getKey()).get(october.getText()).get(0);
               }
               else if(Dates.getKey() == null ? november.getText() == null : Dates.getKey().equals(november.getText())){
                   nov1 = formatter.format(forEntry.get(Items.getKey()).get(november.getText()).get(0));
                   nov = forEntry.get(Items.getKey()).get(november.getText()).get(0);
               }
               else if(Dates.getKey() == null ? december.getText() == null : Dates.getKey().equals(december.getText())){
                   dec1 = formatter.format(forEntry.get(Items.getKey()).get(december.getText()).get(0));
                   dec = forEntry.get(Items.getKey()).get(december.getText()).get(0);
               }
           }
           total_amount = jan + feb + mar + apr + mai + jun + jul + aug + oct + nov + sep + dec;
           totalAmnt = formatter.format(total_amount);       
           revenueItem.setCellValueFactory(data -> data.getValue().RevenueItemProperty());
           january.setCellValueFactory(data -> data.getValue().JanProperty());
           february.setCellValueFactory(data -> data.getValue().FebProperty());
           march.setCellValueFactory(data -> data.getValue().MarProperty());
           april.setCellValueFactory(data -> data.getValue().AprProperty());
           may.setCellValueFactory(data -> data.getValue().MayProperty());
           june.setCellValueFactory(data -> data.getValue().JunProperty());
           july.setCellValueFactory(data -> data.getValue().JulProperty());
           august.setCellValueFactory(data -> data.getValue().AugProperty());
           september.setCellValueFactory(data -> data.getValue().SepProperty());
           october.setCellValueFactory(data -> data.getValue().OctProperty());
           november.setCellValueFactory(data -> data.getValue().NovProperty());
           december.setCellValueFactory(data -> data.getValue().DecProperty());
           totalAmount.setCellValueFactory(data -> data.getValue().Total_AmountProperty());
           getReport = new GetMonthlyReport(Items.getKey(), jan1, feb1, mar1, apr1, mai1, jun1, jul1, aug1, sep1, oct1, nov1, dec1, totalAmnt);
           monthlyTable.getItems().add(getReport);                                           
       }
          
    }
    
    
       public Float setMonthSum(String Center, String item, String Month, String Year) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE  `revenueItem` = '"+item+"' AND `revenueMonth` = '"+Month+"' AND `revCenter` = '"+Center+"' AND `revenueYear` = '"+Year+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ResultSetMetaData meta= rs.getMetaData();
       int row = 0 ;        
       int col = meta.getColumnCount();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           for(int j=1; j<=col; j++){
               if(j == 1){
           String revitem =rs.getObject(j).toString();
           Amount.add(Float.parseFloat(revitem));//adding revenue items to list
           }
           }     
       }
        totalAmunt = 0;
        for(int i = 0; i < Amount.size(); i++){
            totalAmunt += Amount.get(i);
        }
        return totalAmunt;
    }

    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void SelectedYear(ActionEvent event) {
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        if (cmbReportYear.getSelectionModel().isEmpty())
        monthlyTable.getItems().clear();
        changeNames();
        setItems();
        
    }

    @FXML
    void printReport(ActionEvent event) throws FileNotFoundException, JRException {
        if (monthlyTable.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetMonthlyReport> items = new ArrayList<GetMonthlyReport>();
            for (int j = 0; j < monthlyTable.getItems().size(); j++) {
                GetMonthlyReport getdata = new GetMonthlyReport();
                getdata = monthlyTable.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                    file = this.getClass().getResource("/Assets/monthlyPotrait.jrxml");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmbReportYear.getSelectionModel().getSelectedItem(),
            center = cmbReportCent.getSelectionModel().getSelectedItem();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url);
            parameters.put("year", year);
            parameters.put("timeStamp", date);
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
    
}
