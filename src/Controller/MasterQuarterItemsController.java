/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetMstrQuarterItems;

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

/**
 * FXML Controller class
 *
 * @author HP
 */
public class MasterQuarterItemsController implements Initializable {

    @FXML
    private VBox quarterlyTemplate;
    @FXML
    private ComboBox<String> cmMstItemsYear;
    @FXML
    private ComboBox<String> cmbMstItemsQuarter;
    @FXML
    private Button btnShowReport;
    @FXML
    private TableView<GetMstrQuarterItems> quarterMastItemsTable;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> revenueItem;
    @FXML
    private TableColumn<?, ?> year;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> quarter;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> month1;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> month2;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> month3;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> totalAmount;
    @FXML
    private TableColumn<?, ?> revenueItems;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblQtrWarn;
    
    GetMstrQuarterItems getReport;

    /**
     * Initializes the controller class.
     */
    
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowQuater =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    int Year;
    
    public MasterQuarterItemsController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbMstItemsQuarter.setOnMouseClicked(e -> {
            lblQtrWarn.setVisible(false);
        });
        cmMstItemsYear.setOnMouseClicked(e -> {
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
         cmMstItemsYear.getItems().clear();
         cmMstItemsYear.setItems(rowYear);
         cmMstItemsYear.setVisibleRowCount(5);
    }
    
    private void getQuarter() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueQuarter` FROM `daily_entries` WHERE  `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueQuarter`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowQuater.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 String value = rs.getObject(i).toString();
                 
                 rowQuater.add(value);
                 
             }
        }
        cmbMstItemsQuarter.getItems().clear();
        cmbMstItemsQuarter.getItems().setAll(rowQuater);
        cmbMstItemsQuarter.setVisibleRowCount(5);
    }
    
    private void getMonths() throws SQLException{      
        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE   `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbMstItemsQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueMonth`");
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
        month1.setText("MONTH");
        month2.setText("MONTH");
        month3.setText("MONTH");
        int rowSize = rowMonths.size();
        switch(rowSize){
            case 1:
                month1.setText(rowMonths.get(0));
                break;
            case 2: 
                month1.setText(rowMonths.get(0));
                month2.setText(rowMonths.get(1));
                break;
            case 3:
                month1.setText(rowMonths.get(0));
                month2.setText(rowMonths.get(1));
                month3.setText(rowMonths.get(2));
                break;
        }
    }
    
    
    private void changeNames() {
        quarter.setText("Quarter: "+ cmbMstItemsQuarter.getSelectionModel().getSelectedItem());
        year.setText("Year: "+cmMstItemsYear.getSelectionModel().getSelectedItem());
    }
    
    private void setItems() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbMstItemsQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem`");
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
                  monthSum = setMonthSum(Item, month, cmMstItemsYear.getSelectionModel().getSelectedItem(), cmbMstItemsQuarter.getSelectionModel().getSelectedItem());
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
           String mon1 = "0.00", mon2 = "0.00", mon3 = "0.00", totalAmnt = "0.00";
           float Mon1 = 0, Mon2 = 0, Mon3 = 0, total_amount;
           for(Map.Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
               String reveItem = Items.getKey();
               System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
               if(Dates.getKey() == null ? month1.getText() == null : Dates.getKey().equals(month1.getText())){
                   mon1 = formatter.format(forEntry.get(Items.getKey()).get(month1.getText()).get(0));
                   Mon1 = forEntry.get(Items.getKey()).get(month1.getText()).get(0);
               }
               else if(Dates.getKey() == null ? month2.getText() == null : Dates.getKey().equals(month2.getText())){
                   mon2 = formatter.format(forEntry.get(Items.getKey()).get(month2.getText()).get(0));
                   Mon2 = forEntry.get(Items.getKey()).get(month2.getText()).get(0);
               }
               else if(Dates.getKey() == null ? month3.getText() == null : Dates.getKey().equals(month3.getText())){
                   mon3 = formatter.format(forEntry.get(Items.getKey()).get(month3.getText()).get(0));
                   Mon3 = forEntry.get(Items.getKey()).get(month3.getText()).get(0);
               }
           }
           total_amount = Mon3 + Mon2 + Mon1;
           totalAmnt = formatter.format(total_amount);       
           revenueItem.setCellValueFactory(data -> data.getValue().revenueItemProperty());
           month1.setCellValueFactory(data -> data.getValue().firstMonthProperty());
           month2.setCellValueFactory(data -> data.getValue().secondMonthProperty());
           month3.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
           totalAmount.setCellValueFactory(data -> data.getValue().totalAmountProperty());
           getReport = new GetMstrQuarterItems( mon1, mon2, mon3, Items.getKey(), totalAmnt);
           quarterMastItemsTable.getItems().add(getReport);                                           
       }
    }
    
       public Float setMonthSum(String item, String Month, String Year, String Quarter) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE  `revenueItem` = '"+item+"' AND `revenueMonth` = '"+Month+"' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' ");
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
    private void SelectedYear(ActionEvent event) throws SQLException {
        getQuarter();
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        if(cmMstItemsYear.getSelectionModel().isEmpty()){
            lblYearWarn.setVisible(true);
        }else if (cmbMstItemsQuarter.getSelectionModel().isEmpty()){
            lblQtrWarn.setVisible(true);
        }else {
        quarterMastItemsTable.getItems().clear();
        getMonths();
        changeNames();
        setItems();
        }
    }
    
}
