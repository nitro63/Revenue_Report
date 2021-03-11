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
import java.util.Map.Entry;
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
import revenue_report.DBConnection;
import Controller.Gets.GetReport;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class ReportController implements Initializable {

    @FXML
    private TableView<GetReport> WEEKLY_TABLE;
   
    @FXML
    private TableColumn<GetReport, String> REVENUE_ITEM;
    @FXML
    private TableColumn <GetReport, String> DAY1;
    @FXML
    private TableColumn<GetReport, String> DAY2;
    @FXML
    private TableColumn<GetReport, String> DAY3;
    @FXML
    private TableColumn<GetReport, String> DAY4;
    @FXML
    private TableColumn<GetReport, String> DAY5;
    @FXML
    private TableColumn<GetReport, String> DAY6;
    @FXML
    private TableColumn<GetReport, String> DAY7;
    @FXML
    private VBox weekly_Template;
    @FXML
    private TableColumn<GetReport, String> Total_amt;
    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private ComboBox<String> cmbReportMonth;
    @FXML
    private ComboBox<String> cmbReportWeek;
    @FXML
    private TableColumn<?, ?> REVENUECENTER;
    @FXML
    private TableColumn<?, ?> MONTH;
    @FXML
    private TableColumn<?, ?> WEEK;
    /**
     * initialises the controller class.
     * @param url
     * @param rb
     */
    
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowDate =FXCollections.observableArrayList();
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonth =FXCollections.observableArrayList();
    ObservableList<String> rowWeek =FXCollections.observableArrayList();
    @FXML
    private Button btnShowReport;
    
    GetReport getReport;

    public ReportController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getRevCenters();
        } catch (SQLException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
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
         cmbReportCent.setItems(rowCent);
         cmbReportCent.setVisibleRowCount(5);
    }
    
    private void getReportMonth() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueMonth`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowMonth.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 String value = rs.getObject(i).toString();
                 
                 rowMonth.add(value);
                 
             }
        }
        cmbReportMonth.getItems().clear();
        cmbReportMonth.getItems().setAll(rowMonth);
        cmbReportMonth.setVisibleRowCount(5);
    }
    
    private void getReportWeek() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries` WHERE `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueWeek`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        boolean condition = true;
        rowWeek.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 
                 String value = rs.getObject(i).toString();
                 
                 rowWeek.add(value);
                 
             }
        }
        cmbReportWeek.getItems().clear();
        cmbReportWeek.getItems().setAll(rowWeek);
        cmbReportWeek.setVisibleRowCount(5);
    }
    
    private void changeNames() {
        REVENUECENTER.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        MONTH.setText(cmbReportMonth.getSelectionModel().getSelectedItem());
        String wk = "WEEK " + cmbReportWeek.getSelectionModel().getSelectedItem();
        WEEK.setText(wk);
    }

    
    private void getWeekly() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueDate` FROM `daily_entries` WHERE   `revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' AND `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueDate`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int col = meta.getColumnCount();
        rowDate.clear();
        while(rs.next()){
            for(int i=1; i<=col; i++){
                if(i == 1){
                    
                    rowDate.add(rs.getObject(i).toString());
                    
                }
            }
        }
        DAY1.setText("DAY");
        DAY2.setText("DAY");
        DAY3.setText("DAY");
        DAY4.setText("DAY");
        DAY5.setText("DAY");
        DAY6.setText("DAY");
        DAY7.setText("DAY");
        System.out.println(rowDate.size());
        int rowSize = rowDate.size();
        switch(rowSize){
            case 1:
                DAY1.setText(rowDate.get(0));
                break;
            case 2: 
                DAY1.setText(rowDate.get(0));
                DAY2.setText(rowDate.get(1));
                break;
            case 3:
                DAY1.setText(rowDate.get(0));
                DAY2.setText(rowDate.get(1));
                DAY3.setText(rowDate.get(2));
                break;
            case 4:
                DAY1.setText(rowDate.get(0));
                DAY2.setText(rowDate.get(1));
                DAY3.setText(rowDate.get(2));
                DAY4.setText(rowDate.get(3));
                break;
            case 5:
                DAY1.setText(rowDate.get(0));
                DAY2.setText(rowDate.get(1));
                DAY3.setText(rowDate.get(2));
                DAY4.setText(rowDate.get(3));
                DAY5.setText(rowDate.get(4));
                break;
            case 6:
                DAY1.setText(rowDate.get(0));
                DAY2.setText(rowDate.get(1));
                DAY3.setText(rowDate.get(2));
                DAY4.setText(rowDate.get(3));
                DAY5.setText(rowDate.get(4));
                DAY6.setText(rowDate.get(5));
                break;
            case 7:
                DAY1.setText(rowDate.get(0));
                DAY2.setText(rowDate.get(1));
                DAY3.setText(rowDate.get(2));
                DAY4.setText(rowDate.get(3));
                DAY5.setText(rowDate.get(4));
                DAY6.setText(rowDate.get(5));
                DAY7.setText(rowDate.get(6));
                break;
                
        }
        
        
       
   }
    
    @SuppressWarnings("empty-statement")
     private void setItems() throws SQLException{
         /***
          * Retrieving Revenue Items from database per week selected
          * Put Revenue Items into a list for later use
         ***/
        stmnt = con.prepareStatement(" SELECT `revenueItem`   FROM `daily_entries` WHERE   `revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' AND `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem` ");
       ResultSet rs = stmnt.executeQuery();
       ResultSetMetaData meta= rs.getMetaData();
       int row = 0 ;        
       int col = meta.getColumnCount();
       ObservableList<String> Item = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           for(int j=1; j<=col; j++){
           String revitem =rs.getObject(j).toString();
           Item.add(revitem);//adding revenue items to list
           }
       }
       /***
        * Retrieving revenue items and their respective Amount and Dates in an ordered form by revenueItem 
        * 
       ***/
       stmnt =con.prepareStatement(" SELECT  `revenueItem`, `revenueAmount`, `revenueDate` FROM `daily_entries` WHERE   `revenueWeek` = '"+cmbReportWeek.getSelectionModel().getSelectedItem()+"' AND `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' ORDER BY `revenueItem` ");
       ResultSet rt = stmnt.executeQuery();
       ResultSetMetaData Meta = rt.getMetaData();
       int Col = Meta.getColumnCount();
       Map<String, ArrayList<Float>> AmountDate = new HashMap<>();//HashMap to store revenue Amounts on their respective dates
       Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview 

       
       rowDate.forEach((rowDates) -> {
           AmountDate.put(rowDates, new ArrayList());
          
        });
       System.out.println(AmountDate);
       
       Item.forEach((Items) -> {
           forEntry.put(Items,  new HashMap());
        });
       System.out.println(forEntry); 
       while(rt.next()){
           row =rt.getRow();
           float object2 = Float.parseFloat(rt.getObject(2).toString());
           for(Entry<String, ArrayList<Float>> Dates : AmountDate.entrySet()){
               for(Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
                   if (Items.getKey().equals(rt.getObject(1).toString())  && Dates.getKey().equals(rt.getObject(3).toString()) ){
                           if(forEntry.containsKey(rt.getObject(1).toString()) && !forEntry.get(rt.getObject(1).toString()).containsValue(rt.getObject(3).toString())){
                           forEntry.get(rt.getObject(1).toString()).put(rt.getObject(3).toString(), new ArrayList());
                           forEntry.get(rt.getObject(1).toString()).get(rt.getObject(3).toString()).add(object2);
                       }else if(forEntry.containsKey(rt.getObject(1).toString()) && forEntry.get(rt.getObject(1).toString()).containsValue(rt.getObject(3).toString())){
                           forEntry.get(rt.getObject(1).toString()).get(rt.getObject(3).toString()).add(object2);
                       }
                   }
               }
           }
       }

       System.out.println(Item+"  "+col+"  "+ row+" "+ forEntry + " ");
       //Items.getValue().entrySet()
         final String Day1= DAY1.getText();
         NumberFormat formatter = new DecimalFormat("#,###.00");
         
       for(Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
           String da1 = "0.00", da2 = "0.00", da3 = "0.00", da4 = "0.00", da5 = "0.00", da6 = "0.00", da7 = "0.00", totalAmount = "0.00";
           float day1 = 0, day2 = 0, day3 = 0, day4 = 0, day5 = 0, day6 = 0, day7 = 0, total_amount;
           for(Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
               String reveItem = Items.getKey();
               System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
               if(Dates.getKey() == null ? DAY1.getText() == null : Dates.getKey().equals(DAY1.getText())){
                   da1 = formatter.format(forEntry.get(Items.getKey()).get(DAY1.getText()).get(0));
                   day1 = forEntry.get(Items.getKey()).get(DAY1.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY2.getText() == null : Dates.getKey().equals(DAY2.getText())){
                   da2 = formatter.format(forEntry.get(Items.getKey()).get(DAY2.getText()).get(0));
                   day2 = forEntry.get(Items.getKey()).get(DAY2.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY3.getText() == null : Dates.getKey().equals(DAY3.getText())){
                   da3 = formatter.format(forEntry.get(Items.getKey()).get(DAY3.getText()).get(0));
                   day3 = forEntry.get(Items.getKey()).get(DAY3.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY4.getText() == null : Dates.getKey().equals(DAY4.getText())){
                   da4 = formatter.format(forEntry.get(Items.getKey()).get(DAY4.getText()).get(0));
                   day4 = forEntry.get(Items.getKey()).get(DAY4.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY5.getText() == null : Dates.getKey().equals(DAY5.getText())){
                   da5 = formatter.format(forEntry.get(Items.getKey()).get(DAY5.getText()).get(0));
                   day5 = forEntry.get(Items.getKey()).get(DAY5.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY6.getText() == null : Dates.getKey().equals(DAY6.getText())){
                   da6 = formatter.format(forEntry.get(Items.getKey()).get(DAY6.getText()).get(0));
                   day6 = forEntry.get(Items.getKey()).get(DAY6.getText()).get(0);
               }else if(Dates.getKey() == null ? DAY7.getText() == null : Dates.getKey().equals(DAY7.getText())){
                   da7 = formatter.format(forEntry.get(Items.getKey()).get(DAY7.getText()).get(0));
                   day7 = forEntry.get(Items.getKey()).get(DAY7.getText()).get(0);
               }
           }
           total_amount = day1 + day2 + day3 +day4 + day5 + day6 + day7 ;
           totalAmount = formatter.format(total_amount);       
           REVENUE_ITEM.setCellValueFactory(data -> data.getValue().RevenueItemProperty());
           DAY1.setCellValueFactory(data -> data.getValue().DAY1Property());
           DAY2.setCellValueFactory(data -> data.getValue().DAY2Property());
           DAY3.setCellValueFactory(data -> data.getValue().DAY3Property());
           DAY4.setCellValueFactory(data -> data.getValue().DAY4Property());
           DAY5.setCellValueFactory(data -> data.getValue().DAY5Property());
           DAY6.setCellValueFactory(data -> data.getValue().DAY6Property());
           DAY7.setCellValueFactory(data -> data.getValue().DAY7Property());
           Total_amt.setCellValueFactory(data -> data.getValue().Total_AmountProperty());
           getReport = new GetReport(Items.getKey(), da1, da2, da3, da4, da5, da6, da7, totalAmount);
           WEEKLY_TABLE.getItems().add(getReport);                                           
       }
     }
     

    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportMonth();
    }

    @FXML
    private void SelectedMonth(ActionEvent event) throws SQLException {
        getReportWeek();
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        WEEKLY_TABLE.getItems().clear();
        changeNames();
        getWeekly();
        setItems();
    }

    

   
    
}
