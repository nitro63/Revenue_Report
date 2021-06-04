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

import Controller.Gets.GetMonthlyReport;
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
import Controller.Gets.GetQuarterReport;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class QuarterlyReportController implements Initializable {

    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private ComboBox<String> cmbReportYear;
    @FXML
    private ComboBox<String> cmbReportQuarter;
    @FXML
    private Button btnShowReport;
    @FXML
    private TableView<GetQuarterReport> quarterTable;
    @FXML
    private VBox quarterlyTemplate;
    @FXML
    private TableColumn<GetQuarterReport, String> revenueItem;
    @FXML
    private TableColumn<GetQuarterReport, String> month1;
    @FXML
    private TableColumn<GetQuarterReport, String> month2;
    @FXML
    private TableColumn<GetQuarterReport, String> month3;
    @FXML
    private TableColumn<GetQuarterReport, String> totalAmount;
    @FXML
    private TableView<GetQuarterReport> quarterTableAll;
    @FXML
    private TableColumn<GetQuarterReport, String> revenueItemAll;
    @FXML
    private TableColumn<GetQuarterReport, String> month1All;
    @FXML
    private TableColumn<GetQuarterReport, String> month2All;
    @FXML
    private TableColumn<GetQuarterReport, String> month3All;
    @FXML
    private TableColumn<GetQuarterReport, String> month4All;
    @FXML
    private TableColumn<GetQuarterReport, String> totalAmountAll;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblRevenueCenter;
    @FXML
    private Label lblQuarter;
    
    GetQuarterReport getReport;

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
    Map<String, String> centerID = new HashMap<>();
    boolean subMetroPR, Condition, allQuarters, singleQuarter;
    int Year;
    ResultSet rs;
    @FXML
    private TableColumn<?, ?> year;
    
    public QuarterlyReportController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getRevCenters();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(QuarterlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getRevCenters() throws SQLException, ClassNotFoundException{

//            stmnt = con.prepareStatement("SELECT `daily_revCenter` FROM `daily_entries` WHERE 1 GROUP BY `daily_revCenter` ");
//         rs = stmnt.executeQuery();
//         while(rs.next()){
//                 rowCent.add(rs.getString("revenue_center"));
//         }
         cmbReportCent.getItems().clear();
        stmnt = con.prepareStatement("SELECT `daily_entries`.`daily_revCenter`, `revenue_centers`.`revenue_category`, `revenue_centers`.`revenue_center` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` GROUP BY `daily_revCenter` ");
        rs = stmnt.executeQuery();
        rowCent.clear();
        cmbReportCent.getItems().clear();
        while(rs.next()){
            rowCent.add(rs.getString("revenue_center"));
            centerID.put(rs.getString("daily_revCenter"), rs.getString("revenue_center"));
            if (rs.getString("revenue_category").equals("PROPERTY RATE SECTION")){
                Condition = true;
            }
            if (rs.getString("daily_revCenter").equals("K0201") || rs.getString("daily_revCenter").equals("K0202") || rs.getString("daily_revCenter").equals("K0203") || rs.getString("daily_revCenter").equals("K0204") || rs.getString("daily_revCenter").equals("K0205")){
                subMetroPR = true;
            }
        }
        if (Condition){
            cmbReportCent.getItems().add("PROPERTY RATE ALL");
        }
        if (subMetroPR){
            cmbReportCent.getItems().add("PROPERTY RATE SUB-METROS");
        }
         cmbReportCent.getItems().addAll(rowCent);
         cmbReportCent.setVisibleRowCount(5);     
    } 
     
    private void getReportYear() throws SQLException{
    }
    
    private void getQuarter() throws SQLException{
    }
    
    
    private void getMonths() throws SQLException{
        if (!cmbReportQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
            allQuarters = false;
            singleQuarter = true;
            quarterTableAll.setVisible(false);
            quarterTable.setVisible(true);
            if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
                stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueMonth` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `daily_entries`.`revenueMonth`");
            } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
                stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueMonth` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `daily_entries`.`revenueMonth`");
            }
            else {
                stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueMonth` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueMonth`");
            }
        rs = stmnt.executeQuery();
        rowMonths.clear();
        while(rs.next()){
            rowMonths.add(rs.getString("revenueMonth"));
        }
        month1.setText("MONTH");
        month2.setText("MONTH");
        month3.setText("MONTH");
        String rowSize = cmbReportQuarter.getSelectionModel().getSelectedItem();
        switch(rowSize){
            case "1":
                month1.setText("January");
                month2.setText("February");
                month3.setText("March");
                break;
            case "2":
                month1.setText("April");
                month2.setText("May");
                month3.setText("June");
                break;
            case "3":
                month1.setText("July");
                month2.setText("August");
                month3.setText("September");
                break;
            case "4":
                month1.setText("October");
                month2.setText("November");
                month3.setText("December");
                break;
        }
        }else {
            allQuarters = true;
            singleQuarter = false;
            quarterTableAll.setVisible(true);
            quarterTable.setVisible(false);
            rowMonths.clear();
            rowMonths.addAll("1", "2", "3", "4");
        }
    }
    
    
    private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblQuarter.setText( cmbReportQuarter.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem());
    }
    
    private void setItems() throws SQLException{
        Map<String, ArrayList<Float>> monthAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
        Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview
        if (!cmbReportQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
                if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
                    stmnt = con.prepareStatement(" SELECT `revenue_item` FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");
                } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
                    stmnt = con.prepareStatement(" SELECT `revenue_item` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");
                }
                else {
                    stmnt = con.prepareStatement(" SELECT `revenue_item` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");
                }
//        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");
        ResultSet rs = stmnt.executeQuery();
        rowItems.clear();
        while(rs.next()){
            rowItems.add(rs.getString("revenue_item"));
        }
        }
        else {
            if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
                stmnt = con.prepareStatement(" SELECT `revenue_item` FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'GROUP BY `revenue_item`");
            } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
                stmnt = con.prepareStatement(" SELECT `revenue_item` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'GROUP BY `revenue_item`");
            }
            else {
                stmnt = con.prepareStatement(" SELECT `revenue_item` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'GROUP BY `revenue_item`");
            }
//            stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem`");
            ResultSet rs = stmnt.executeQuery();
            rowItems.clear();
            while(rs.next()){
                        rowItems.add(rs.getString("revenue_item"));
            }
        }
          rowItems.forEach((rowItem) -> forEntry.put(rowItem, new HashMap<>()));
          rowMonths.forEach((rowMonth) -> monthAmount.put(rowMonth, new ArrayList<>()));
          try {
              if (!cmbReportQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
          for(String month : rowMonths) {
              for(String Item : rowItems) {
                  float monthSum;
                  monthSum = setMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), Item, month, cmbReportYear.getSelectionModel().getSelectedItem(), cmbReportQuarter.getSelectionModel().getSelectedItem());
                  for(Map.Entry<String, ArrayList<Float>> Dates : monthAmount.entrySet()){
                          for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
                              if (Items.getKey().equals(Item)  && Dates.getKey().equals(month)){
                                  if(forEntry.containsKey(Item) && !forEntry.get(Item).containsKey(month)){
                                      forEntry.get(Item).put(month, new ArrayList<>());
                                      forEntry.get(Item).get(month).add(monthSum);
                                  }else if(forEntry.containsKey(Item) && forEntry.get(Item).containsKey(month)){
                                      forEntry.get(Item).get(month).add(monthSum);
                                  } 
                              };
                          };
                      }
                  
              }
          }
              }
              else if (allQuarters){
                  for(String month : rowMonths) {
                      for(String Item : rowItems) {
                          float monthSum = 0;
                          monthSum = setMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), Item, cmbReportYear.getSelectionModel().getSelectedItem(), month);
                          for(Map.Entry<String, ArrayList<Float>> Dates : monthAmount.entrySet()){
                              for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
                                  if (Items.getKey().equals(Item)  && Dates.getKey().equals(month)){
                                      if(forEntry.containsKey(Item) && !forEntry.get(Item).containsKey(month)){
                                          forEntry.get(Item).put(month, new ArrayList<>());
                                          forEntry.get(Item).get(month).add(monthSum);
                                      }else if(forEntry.containsKey(Item) && forEntry.get(Item).containsKey(month)){
                                          forEntry.get(Item).get(month).add(monthSum);
                                      }
                                  };
                              };
                          }

                      }
                  }
                  System.out.println(forEntry+""+rowItems+""+rowMonths);
              }
         }
                  catch (SQLException ex) {
                      Logger.getLogger(weeklyReportController.class.getName()).log(Level.SEVERE, null, ex);
                  }
          NumberFormat formatter = new DecimalFormat("#,##0.00");
         
       for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()) {
           String mon1 = "0.00", mon2 = "0.00", mon3 = "0.00", mon4 = "0.00", totalAmnt;
           float Mon1 = 0, Mon2 = 0, Mon3 = 0, Mon4 = 0, total_amount;
           for (Map.Entry<String, ArrayList<Float>> Dates : forEntry.get(Items.getKey()).entrySet()) {
               if (!cmbReportQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")) {
                   if (Dates.getKey() == null ? month1.getText() == null : Dates.getKey().equals(month1.getText())) {
                       mon1 = formatter.format(forEntry.get(Items.getKey()).get(month1.getText()).get(0));
                       Mon1 = forEntry.get(Items.getKey()).get(month1.getText()).get(0);
                   } else if (Dates.getKey() == null ? month2.getText() == null : Dates.getKey().equals(month2.getText())) {
                       mon2 = formatter.format(forEntry.get(Items.getKey()).get(month2.getText()).get(0));
                       Mon2 = forEntry.get(Items.getKey()).get(month2.getText()).get(0);
                   } else if (Dates.getKey() == null ? month3.getText() == null : Dates.getKey().equals(month3.getText())) {
                       mon3 = formatter.format(forEntry.get(Items.getKey()).get(month3.getText()).get(0));
                       Mon3 = forEntry.get(Items.getKey()).get(month3.getText()).get(0);
                   }
               } else if (allQuarters){
                   if (Dates.getKey().equals("1")) {
                       mon1 = formatter.format(forEntry.get(Items.getKey()).get("1").get(0));
                       Mon1 = forEntry.get(Items.getKey()).get("1").get(0);
                   } else if (Dates.getKey().equals("2")) {
                       mon2 = formatter.format(forEntry.get(Items.getKey()).get("2").get(0));
                       Mon2 = forEntry.get(Items.getKey()).get("2").get(0);
                   } else if (Dates.getKey().equals("3")) {
                       mon3 = formatter.format(forEntry.get(Items.getKey()).get("3").get(0));
                       Mon3 = forEntry.get(Items.getKey()).get("3").get(0);
                   } else if (Dates.getKey().equals("4")) {
                       mon4 = formatter.format(forEntry.get(Items.getKey()).get("4").get(0));
                       Mon4 = forEntry.get(Items.getKey()).get("4").get(0);
                   }
               }
           }
           if (singleQuarter){
           total_amount = Mon3 + Mon2 + Mon1;
           totalAmnt = formatter.format(total_amount);
           revenueItem.setCellValueFactory(data -> data.getValue().revenueItemProperty());
           month1.setCellValueFactory(data -> data.getValue().firstMonthProperty());
           month2.setCellValueFactory(data -> data.getValue().secondMonthProperty());
           month3.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
           totalAmount.setCellValueFactory(data -> data.getValue().totalAmountProperty());
           getReport = new GetQuarterReport(mon1, mon2, mon3, Items.getKey(), totalAmnt);
           quarterTable.getItems().add(getReport);
           }else if (allQuarters){
               total_amount = Mon3 + Mon2 + Mon1 + Mon4;
               totalAmnt = formatter.format(total_amount);
               revenueItemAll.setCellValueFactory(data -> data.getValue().revenueItemProperty());
               month1All.setCellValueFactory(data -> data.getValue().firstMonthProperty());
               month2All.setCellValueFactory(data -> data.getValue().secondMonthProperty());
               month3All.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
               month4All.setCellValueFactory(data -> data.getValue().fourthMonthProperty());
               totalAmountAll.setCellValueFactory(data -> data.getValue().totalAmountProperty());
               getReport = new GetQuarterReport( mon1, mon2, mon3, mon4, Items.getKey(), totalAmnt);
               quarterTableAll.getItems().add(getReport);
           }
       }
    }

    public Float setMonthSum(String Center, String item, String Year, String Quarter) throws SQLException{
        float totalAmunt;
        if (Center.equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"'");
        } else if (Center.equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement("SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' AND `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `CenterID` = 'K0201' OR `CenterID` = 'K0202' OR `CenterID` = 'K0203' OR `CenterID` = 'K0204' OR `CenterID` = 'K0205'");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+Center+"' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"'");
        }
        rs = stmnt.executeQuery();
        ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
        while(rs.next()){//looping through the retrieved revenueItems result set
            Amount.add(rs.getFloat("revenueAmount"));//adding revenue items to list
        }
        totalAmunt = 0;
        for(int i = 0; i < Amount.size(); i++){
            totalAmunt += Amount.get(i);
        }
        return totalAmunt;
    }
    
       public Float setMonthSum(String Center, String item, String Month, String Year, String Quarter) throws SQLException{
        float totalAmunt;
           if (Center.equals("PROPERTY RATE ALL")) {
               stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' AND `revenueMonth` = '"+Month+"'");
           } else if (Center.equals("PROPERTY RATE SUB-METROS")){
               stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' AND `revenueMonth` = '"+Month+"'");
           }
           else {
               stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+Center+"' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' AND `revenueMonth` = '"+Month+"'");
           }
       rs = stmnt.executeQuery();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           Amount.add(rs.getFloat("revenueAmount"));//adding revenue items to list
       }
        totalAmunt = 0;
        for(int i = 0; i < Amount.size(); i++){
            totalAmunt += Amount.get(i);
        }
        return totalAmunt;
    }

    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueYear` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `daily_entries`.`revenueYear`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `CenterID` = 'K0201' OR `CenterID` = 'K0202' OR `CenterID` = 'K0203' OR `CenterID` = 'K0204' OR `CenterID` = 'K0205' GROUP BY `daily_entries`.`revenueYear`");
        } else {
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `daily_entries`.`revenueYear`");
        }
        rs = stmnt.executeQuery();
        rowYear.clear();
        while(rs.next()){
            rowYear.add(rs.getString("revenueYear"));
        }
        System.out.println(rowYear);
        cmbReportYear.getItems().clear();
        cmbReportYear.getItems().addAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
    }

    @FXML
    private void SelectedYear(ActionEvent event) throws SQLException {
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueQuarter` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `daily_entries`.`revenueQuarter`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueQuarter` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `daily_entries`.`revenueQuarter`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueQuarter` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueQuarter`");
        }
        rs = stmnt.executeQuery();
        rowQuater.clear();
        while(rs.next()){
            rowQuater.add(rs.getString("revenueQuarter"));
        }
        rowQuater.add("All Quarters");
        cmbReportQuarter.getItems().clear();
        cmbReportQuarter.getItems().addAll(rowQuater);
        cmbReportQuarter.setVisibleRowCount(5);
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        quarterTableAll.getItems().clear();
        quarterTable.getItems().clear();
        getMonths();
        changeNames();
        setItems();
    }

    @FXML
    void printReport(ActionEvent event) throws FileNotFoundException, JRException {
        if (!quarterTable.getItems().isEmpty()){
            Date date = new Date();
            List<GetQuarterReport> items = new ArrayList<GetQuarterReport>();
            for (int j = 0; j < quarterTable.getItems().size(); j++) {
                GetQuarterReport getdata = new GetQuarterReport();
                getdata = quarterTable.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                    file = this.getClass().getResource("/Assets/quarterlyPortrait.jrxml");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmbReportYear.getSelectionModel().getSelectedItem(),
                    center = cmbReportCent.getSelectionModel().getSelectedItem(),
                    first = month1.getText(),
                    second = month2.getText(),
                    third = month3.getText();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("FirstMonth", first);
            parameters.put("year", year); parameters.put("SecondMonth", second);
            parameters.put("timeStamp", date); parameters.put("ThirdMonth", third);
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
        }else if (!quarterTableAll.getItems().isEmpty()){
            Date date = new Date();
            List<GetQuarterReport> items = new ArrayList<GetQuarterReport>();
            for (int j = 0; j < quarterTableAll.getItems().size(); j++) {
                GetQuarterReport getdata = new GetQuarterReport();
                getdata = quarterTableAll.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                    file = this.getClass().getResource("/Assets/AllquarterlyPortrait.jrxml");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmbReportYear.getSelectionModel().getSelectedItem(),
                    center = cmbReportCent.getSelectionModel().getSelectedItem(),
                    first = month1All.getText(),
                    second = month2All.getText(),
                    third = month3All.getText(),
                    fourth = month4All.getText();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("FirstMonth", first);
            parameters.put("year", year); parameters.put("SecondMonth", second);
            parameters.put("timeStamp", date); parameters.put("ThirdMonth", third);parameters.put("FourthMonth", fourth);
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
        }else {
            event.consume();
        }
    }
    
}
