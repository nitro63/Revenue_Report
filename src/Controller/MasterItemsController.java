/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import revenue_report.DBConnection;
import Controller.Gets.GetItemsReport;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class MasterItemsController implements Initializable {

    @FXML
    private VBox masterItemsTemplate;
    @FXML
    private ComboBox<String> cmbMasterItemsYear;
    @FXML
    private Button btnShowReport;
    @FXML
    private TableView<GetItemsReport> revenueItemsTable;
    @FXML
    private TableColumn<?, ?> revenueItems;
    @FXML
    private TableColumn<GetItemsReport, String> revenueItem;
    @FXML
    private TableColumn<?, ?> year;
    @FXML
    private TableColumn<GetItemsReport, String> january;
    @FXML
    private TableColumn<GetItemsReport, String> february;
    @FXML
    private TableColumn<GetItemsReport, String> march;
    @FXML
    private TableColumn<GetItemsReport, String> april;
    @FXML
    private TableColumn<GetItemsReport, String> may;
    @FXML
    private TableColumn<GetItemsReport, String> june;
    @FXML
    private TableColumn<GetItemsReport, String> july;
    @FXML
    private TableColumn<GetItemsReport, String> august;
    @FXML
    private TableColumn<GetItemsReport, String> september;
    @FXML
    private TableColumn<GetItemsReport, String> october;
    @FXML
    private TableColumn<GetItemsReport, String> november;
    @FXML
    private TableColumn<GetItemsReport, String> december;
    @FXML
    private TableColumn<GetItemsReport, String> totalAmount;
    @FXML
    private Label lblYearWarn;
    
    private GetItemsReport getReport;
    
    /**
     * Initializes the controller class.
     */
    
    private  Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    int Year;
    
    public MasterItemsController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbMasterItemsYear.setOnMouseClicked(e -> {
            lblYearWarn.setVisible(false);
        });
        try {
            getRevenueYears();
        } catch (SQLException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }        
    } 
    
    private void getRevenueYears() throws SQLException, ClassNotFoundException{
        
            stmnt = con.prepareStatement("SELECT `revenueYear` FROM `daily_entries` WHERE 1 GROUP BY `revenueYear` ");
         ResultSet rs = stmnt.executeQuery();
         ResultSetMetaData metadata = rs.getMetaData();
         int columns = metadata.getColumnCount();
         
         while(rs.next()){
             for(int i= 1; i<=columns; i++)
             {
                 String value = rs.getObject(i).toString();
                 rowYear.add(value);
                 
             }
         }
         cmbMasterItemsYear.getItems().clear();
         cmbMasterItemsYear.setItems(rowYear);
         cmbMasterItemsYear.setVisibleRowCount(5);
    
         
    }
    
    private void changeNames() {
        revenueItems.setText("All Revenue Items");
        year.setText("Year: "+ cmbMasterItemsYear.getSelectionModel().getSelectedItem());
    }
    
   private void setItems() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbMasterItemsYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem`");
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
        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbMasterItemsYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueMonth`");
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
          rowItems.forEach((Center) -> {
              forEntry.put(Center, new HashMap());
      });
          rowMonths.forEach((rowMonth) -> {
              monthAmount.put(rowMonth, new ArrayList());
          });
          try {
          for(String month : rowMonths) {
              for(String Item : rowItems) {
                  float monthSum;
                  monthSum = setCentersSum(Item, month, cmbMasterItemsYear.getSelectionModel().getSelectedItem());
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
          NumberFormat formatter = new DecimalFormat("#,###.00");
         
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
           getReport = new GetItemsReport(Items.getKey(), jan1, feb1, mar1, apr1, mai1, jun1, jul1, aug1, sep1, oct1, nov1, dec1, totalAmnt);
           revenueItemsTable.getItems().add(getReport);                                           
       }
          
    } 
   
   
       public Float setCentersSum(String Item, String Month, String Year) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE  `revenueItem` = '"+Item+"' AND `revenueMonth` = '"+Month+"' AND `revenueYear` = '"+Year+"'  ");
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
    private void SelectedYear(ActionEvent event) {
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        if(cmbMasterItemsYear.getSelectionModel().isEmpty()){
            lblYearWarn.setVisible(true);
        }else {
        revenueItemsTable.getItems().clear();
        changeNames();
        setItems();
        }
    }
    
}
