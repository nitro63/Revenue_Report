/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.Conditioner;
import Controller.Gets.GetFunctions;
import Controller.Gets.ReportGenerator;

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
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import revenue_report.DBConnection;
import Controller.Gets.GetReportgen;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class weeklyReportController implements Initializable {

    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private ComboBox<String> cmbReportYear;
    @FXML
    private ComboBox<String> cmbReportMonth;
    @FXML
    private Button btnShowReport;
    @FXML
    private VBox weekTemplate;
    @FXML
    private AnchorPane generalPane;
    @FXML
    private TableView<GetReportgen> weekTable;
    @FXML
    private TableColumn<GetReportgen, String> revenueCENTER;
    @FXML
    private TableColumn<GetReportgen, String> revenueITEM;
    @FXML
    private TableColumn<GetReportgen, String> MONTH;
    @FXML
    private TableColumn<GetReportgen, String> week1;
    @FXML
    private Label lblWeek1gen;
    @FXML
    private TableColumn<GetReportgen, String> week2;
    @FXML
    private Label lblWeek2gen;
    @FXML
    private TableColumn<GetReportgen, String> week3;
    @FXML
    private Label lblWeek3gen;
    @FXML
    private TableColumn<GetReportgen, String> week4;
    @FXML
    private Label lblWeek4gen;
    @FXML
    private TableColumn<GetReportgen, String> week5;
    @FXML
    private Label lblWeek5gen;
    @FXML
    private TableColumn<GetReportgen, String> week6;
    @FXML
    private Label lblWeek6gen;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private TableColumn<GetReportgen, String> totalAmount;
    @FXML
    private Label lblWk1SumGen;
    @FXML
    private Label lblWk2SumGen;
    @FXML
    private Label lblWk3SumGen;
    @FXML
    private Label lblWk4SumGen;
    @FXML
    private Label lblWk5SumGen;
    @FXML
    private Label lblTotalSumGen;
    @FXML
    private Label lblWk6SumGen;
    @FXML
    private Label lblMonth;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblRevenueCenter;
    @FXML
    private ScrollPane subPane;
    @FXML
    private TableView<GetReportgen> weekTableSub;
    @FXML
    private TableColumn<GetReportgen, String> revenueITEMSub;
    @FXML
    private TableColumn<GetReportgen, String> week1Sub;
    @FXML
    private TableColumn<GetReportgen, String> week2Sub;
    @FXML
    private TableColumn<GetReportgen, String> week3Sub;
    @FXML
    private TableColumn<GetReportgen, String> week4Sub;
    @FXML
    private TableColumn<GetReportgen, String> week5Sub;
    @FXML
    private TableColumn<GetReportgen, String> week6Sub;
    @FXML
    private TableColumn<GetReportgen, String> totalAmountSub;
    @FXML
    private Label lblCumuAmountTot;
    @FXML
    private Label lblCCAmountTot;
    @FXML
    private Label lblCommissionTot;
    @FXML
    private Label lblCostValueBooksTot;
    @FXML
    private Label lblDiffTot;
    @FXML
    private Label lblNetRevenueTot;
    @FXML
    private Label lblAmtDueSubTot;
    @FXML
    private Label lblAmtDueKMATot;
    @FXML
    private Label lblCumuAmount1;
    @FXML
    private Label lblCumuAmount2;
    @FXML
    private Label lblCumuAmount3;
    @FXML
    private Label lblCumuAmount4;
    @FXML
    private Label lblCumuAmount5;
    @FXML
    private Label lblCumuAmount6;
    @FXML
    private Label lblCCAmount1;
    @FXML
    private Label lblCommission1;
    @FXML
    private Label lblCostValueBooks1;
    @FXML
    private Label lblDiff1;
    @FXML
    private Label lblNetRevenue1;
    @FXML
    private Label lblAmtDueSub1;
    @FXML
    private Label lblAmtDueKMA1;
    @FXML
    private Label lblCCAmount2;
    @FXML
    private Label lblCommission2;
    @FXML
    private Label lblCostValueBooks2;
    @FXML
    private Label lblDiff2;
    @FXML
    private Label lblNetRevenue2;
    @FXML
    private Label lblAmtDueSub2;
    @FXML
    private Label lblAmtDueKMA2;
    @FXML
    private Label lblCCAmount3;
    @FXML
    private Label lblCommission3;
    @FXML
    private Label lblCostValueBooks3;
    @FXML
    private Label lblDiff3;
    @FXML
    private Label lblNetRevenue3;
    @FXML
    private Label lblAmtDueSub3;
    @FXML
    private Label lblAmtDueKMA3;
    @FXML
    private Label lblCCAmount4;
    @FXML
    private Label lblCommission4;
    @FXML
    private Label lblCostValueBooks4;
    @FXML
    private Label lblDiff4;
    @FXML
    private Label lblNetRevenue4;
    @FXML
    private Label lblAmtDueSub4;
    @FXML
    private Label lblAmtDueKMA4;
    @FXML
    private Label lblCCAmount5;
    @FXML
    private Label lblCommission5;
    @FXML
    private Label lblCostValueBooks5;
    @FXML
    private Label lblDiff5;
    @FXML
    private Label lblNetRevenue5;
    @FXML
    private Label lblAmtDueSub5;
    @FXML
    private Label lblAmtDueKMA5;
    @FXML
    private Label lblCCAmount6;
    @FXML
    private Label lblCommission6;
    @FXML
    private Label lblCostValueBooks6;
    @FXML
    private Label lblDiff6;
    @FXML
    private Label lblNetRevenue6;
    @FXML
    private Label lblAmtDueSub6;
    @FXML
    private Label lblAmtDueKMA6;
    @FXML
    private Label lblWk1SumSub;
    @FXML
    private Label lblWk2SumSub;
    @FXML
    private Label lblWk3SumSub;
    @FXML
    private Label lblWk4SumSub;
    @FXML
    private Label lblWk5SumSub;
    @FXML
    private Label lblTotalSumSub;
    @FXML
    private Label lblWk6SumSub;

    report_sideController app;
    /**
     * Initializes the controller class.
     */
    Conditioner conditioner ;
    private final Connection con;
    private PreparedStatement stmnt;
    Map<String, String> centerID = new HashMap<>();
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonth =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowWeek =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    int Year;
    public boolean condSubMetro, subMetroPR, Condition;
    GetFunctions getFunctions = new GetFunctions();
    
    public weeklyReportController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    public void setReportSide(report_sideController app){
        this.app = app;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        subPane.setVisible(false);
        try {
            getRevCenters();
        } catch (SQLException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    
    private void getRevCenters() throws SQLException, ClassNotFoundException{
        stmnt = con.prepareStatement("SELECT `daily_entries`.`daily_revCenter`, `revenue_centers`.`revenue_category`, `revenue_centers`.`revenue_center` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` GROUP BY `daily_revCenter` ");
        ResultSet rs = stmnt.executeQuery();
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
         if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
             stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueYear` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `daily_entries`.`revenueYear`");
         } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
             stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' GROUP BY `daily_entries`.`revenueYear`");
         }
         else {
             stmnt = con.prepareStatement(" SELECT /*`daily_entries`.*/`revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
         }
        ResultSet rs = stmnt.executeQuery();
        rowYear.clear();
        while(rs.next()){
            rowYear.add(rs.getString("revenueYear"));
        }
        cmbReportYear.getItems().clear();
        cmbReportYear.getItems().setAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
         String center = cmbReportCent.getSelectionModel().getSelectedItem();
         if (!cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL") || !cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS"))
         {
         stmnt = con.prepareStatement(" SELECT `revenue_category` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+
                 cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenue_category`");
         rs = stmnt.executeQuery();
         while(rs.next()){
             if (rs.getString("revenue_category").equals("SUB-METROS")){
                 condSubMetro = true;
             }else {
                 condSubMetro = false;
             }
         }
         }
         System.out.println(app.catCenter+"\n"+condSubMetro);
    }
     
     private void getReportMonth() throws SQLException{
         if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
             stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueMonth` FROM `revenue_centers`,`daily_entries` WHERE `revenueYear` = '" + cmbReportYear.getSelectionModel().getSelectedItem() + "' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `daily_entries`.`revenueMonth`");
         } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
             stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueMonth` FROM `daily_entries` WHERE `revenueYear` = '" + cmbReportYear.getSelectionModel().getSelectedItem() + "' AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' GROUP BY `daily_entries`.`revenueMonth`");
         }
         else {
             stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_center` = '" + cmbReportCent.getSelectionModel().getSelectedItem() + "' AND `revenueYear` = '" + cmbReportYear.getSelectionModel().getSelectedItem() + "' GROUP BY `revenueMonth`");
         }
//        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE `revenueYear`='"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueMonth`");
        ResultSet rs = stmnt.executeQuery();
        rowMonth.clear();
        while(rs.next()){
            rowMonth.add(rs.getString("revenueMonth"));
        }
        cmbReportMonth.getItems().clear();
        cmbReportMonth.getItems().setAll(rowMonth);
        cmbReportMonth.setVisibleRowCount(5);
    }
     
//      private void getWeekly() throws SQLException{
//        stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries` WHERE   `revenueMonth` = '"+
//                cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+
//                cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.
//                getSelectionModel().getSelectedItem()+"' GROUP BY `revenueWeek`");
//        ResultSet rs = stmnt.executeQuery();
//        ResultSetMetaData meta = rs.getMetaData();
//        int col = meta.getColumnCount();
//        rowWeek.clear();
//        while(rs.next()){
//            for(int i=1; i<=col; i++){
//                if(i == 1){
//
//                    rowWeek.add(rs.getObject(i).toString());
//
//                }
//            }
//        }
//        week1.setText("WEEK");
//        week2.setText("WEEK");
//        week3.setText("WEEK");
//        week4.setText("WEEK");
//        week5.setText("WEEK");
//        week6.setText("WEEK");
//        System.out.println(rowWeek.size());
//        int rowSize = rowWeek.size();
//        switch(rowSize){
//            case 1:
//                week1.setText(rowWeek.get(0));
//                break;
//            case 2:
//                week1.setText(rowWeek.get(0));
//                week2.setText(rowWeek.get(1));
//                break;
//            case 3:
//                week1.setText(rowWeek.get(0));
//                week2.setText(rowWeek.get(1));
//                week3.setText(rowWeek.get(2));
//                break;
//            case 4:
//                week1.setText(rowWeek.get(0));
//                week2.setText(rowWeek.get(1));
//                week3.setText(rowWeek.get(2));
//                week4.setText(rowWeek.get(3));
//                break;
//            case 5:
//                week1.setText(rowWeek.get(0));
//                week2.setText(rowWeek.get(1));
//                week3.setText(rowWeek.get(2));
//                week4.setText(rowWeek.get(3));
//                week5.setText(rowWeek.get(4));
//                break;
//            case 6:
//                week1.setText(rowWeek.get(0));
//                week2.setText(rowWeek.get(1));
//                week3.setText(rowWeek.get(2));
//                week4.setText(rowWeek.get(3));
//                week5.setText(rowWeek.get(4));
//                week6.setText(rowWeek.get(5));
//                break;
//
//        }
//
//
//
//   }
      
      private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblMonth.setText(cmbReportMonth.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem());
    }
      /***
       * have to create to calculate the methods externally and inherit them 
       * In weekly class method
       * select revenue items base on daily- so it will all revolve the result from the dailies
       * then get the total for the week and return that in the weekly method 
       * the required fields will be centre, revenueItem, month, week, year
       * then we get de revenue Items per day 
       * 
       */
      private void setItems() throws SQLException{
          if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")){
              stmnt = con.prepareStatement(" SELECT `revenue_items`.`revenue_item`   FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND  `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"'AND `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_items`.`revenue_item` ");
          }else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
              stmnt = con.prepareStatement(" SELECT `revenue_items`.`revenue_item`   FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND  `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"'AND `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_items`.`revenue_item` ");
          }else {
              stmnt = con.prepareStatement(" SELECT `revenue_items`.`revenue_item`   FROM `daily_entries`,`revenue_items`,`revenue_centers` WHERE  `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND  `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_center` = '" + cmbReportCent.getSelectionModel().getSelectedItem() + "' AND `daily_entries`.`revenueMonth` = '" + cmbReportMonth.getSelectionModel().getSelectedItem() + "'AND `daily_entries`.`revenueYear` = '" + cmbReportYear.getSelectionModel().getSelectedItem() + "' GROUP BY `revenue_items`.`revenue_item` ");
          }
//        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem`");
        ResultSet rs = stmnt.executeQuery();
        rowItems.clear();
        while(rs.next()){
            rowItems.add(rs.getString("revenue_item"));
        }
          if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")){
              stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueWeek` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter`  AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"'  GROUP BY `daily_entries`.`revenueWeek`");
          } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
              stmnt = con.prepareStatement(" SELECT `daily_entries`.`revenueWeek` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter`  AND `daily_entries`.`revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205'  GROUP BY `daily_entries`.`revenueWeek`");
          }else{
              stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueWeek`");
          }
//          stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries` WHERE   `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueWeek`");
          rs = stmnt.executeQuery();
          rowWeek.clear();
          while(rs.next()){
              rowWeek.add(rs.getString("revenueWeek"));
          }
//          Map<String, ArrayList<Float>> weekAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
          Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview 
          rowItems.forEach((rowItem) -> {
              forEntry.put(rowItem, new HashMap<>());
      });
//          rowWeek.forEach((rowDates) -> {
//              weekAmount.put(rowDates, new ArrayList<>());
//          });
          try {
          for(String week : rowWeek) {
              for(String Item : rowItems) {
                  float weekSum;
                   weekSum = setWeekSum(cmbReportCent.getSelectionModel().getSelectedItem(), Item, cmbReportMonth.getSelectionModel().getSelectedItem(), week, cmbReportYear.getSelectionModel().getSelectedItem());
//                      for(Entry<String, ArrayList<Float>> Dates : weekAmount.entrySet()){
                          for(Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
                              if (Items.getKey().equals(Item)  ){
                                  if(forEntry.containsKey(Item) && !forEntry.get(Item).containsKey(week)){
                                      forEntry.get(Item).put(week, new ArrayList<>());
                                      forEntry.get(Item).get(week).add(weekSum);
                                  }else if(forEntry.containsKey(Item) && forEntry.get(Item).containsKey(week)){
                                      forEntry.get(Item).get(week).add(weekSum);
                                  } 
                              };
                          };
//                      }
                  
              }
          }
         }
                  catch (SQLException ex) {
                      Logger.getLogger(weeklyReportController.class.getName()).log(Level.SEVERE, null, ex);
                  }
          NumberFormat formatter = new DecimalFormat("#,##0.00");
         
       for(Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
           String wek1 = "0.00", wek2 = "0.00", wek3 = "0.00", wek4 = "0.00", wek5 = "0.00", wek6 = "0.00",totalAmnt = "0.00",
           totWek1 = "0.00", totWek2 = "0.00", totWek3 = "0.00", totWek4 = "0.00", totWek5 = "0.00", totWek6 = "0.00", summation = "0.00";
           float wk1 = 0, wk2 = 0, wk3 = 0, wk4 = 0, wk5 = 0, wk6 = 0, total_amount, totwek1 = 0, totwek2 = 0,totwek3 = 0,totwek4 = 0,
                   totwek5 = 0,totwek6 = 0,totweksum = 0;

           for(Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
               String reveItem = Items.getKey();
               System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
               if(Dates.getKey() == null ? week1.getText() == null : Dates.getKey().equals(week1.getText())){
                   wek1 = formatter.format(forEntry.get(Items.getKey()).get(week1.getText()).get(0));
                   wk1 = forEntry.get(Items.getKey()).get(week1.getText()).get(0);
               }else if(Dates.getKey() == null ? week2.getText() == null : Dates.getKey().equals(week2.getText())){
                   wek2 = formatter.format(forEntry.get(Items.getKey()).get(week2.getText()).get(0));
                   wk2 = forEntry.get(Items.getKey()).get(week2.getText()).get(0);
               }else if(Dates.getKey() == null ? week3.getText() == null : Dates.getKey().equals(week3.getText())){
                   wek3 = formatter.format(forEntry.get(Items.getKey()).get(week3.getText()).get(0));
                   wk3 = forEntry.get(Items.getKey()).get(week3.getText()).get(0);
               }else if(Dates.getKey() == null ? week4.getText() == null : Dates.getKey().equals(week4.getText())){
                   wek4 = formatter.format(forEntry.get(Items.getKey()).get(week4.getText()).get(0));
                   wk4 = forEntry.get(Items.getKey()).get(week4.getText()).get(0);
               }else if(Dates.getKey() == null ? week5.getText() == null : Dates.getKey().equals(week5.getText())){
                   wek5 = formatter.format(forEntry.get(Items.getKey()).get(week5.getText()).get(0));
                   wk5 = forEntry.get(Items.getKey()).get(week5.getText()).get(0);
               } else if(Dates.getKey() == null ? week6.getText() == null : Dates.getKey().equals(week6.getText())){
               wek5 = formatter.format(forEntry.get(Items.getKey()).get(week6.getText()).get(0));
               wk5 = forEntry.get(Items.getKey()).get(week6.getText()).get(0);
           }
           }
           total_amount = wk1 + wk2 + wk3 + wk4 + wk5 + wk6;
           totalAmnt = formatter.format(total_amount);
           totwek1 += wk1; totWek1 = formatter.format(totwek1);
           totwek2 += wk2; totWek2 = formatter.format(totwek2);
           totwek3 += wk3; totWek3 = formatter.format(totwek3);
           totwek4 += wk4; totWek4 = formatter.format(totwek4);
           totwek5 += wk5; totWek5 = formatter.format(totwek5);
           totwek6 += wk6; totWek6 = formatter.format(totwek6);
           totweksum += total_amount; summation = formatter.format(totweksum);
           revenueITEM.setCellValueFactory(data -> data.getValue().RevenueItemProperty());
           week1.setCellValueFactory(data -> data.getValue().week1Property());
           week2.setCellValueFactory(data -> data.getValue().week2Property());
           week3.setCellValueFactory(data -> data.getValue().week3Property());
           week4.setCellValueFactory(data -> data.getValue().week4Property());
           week5.setCellValueFactory(data -> data.getValue().week5Property());
           week6.setCellValueFactory(data -> data.getValue().week6Property());
           totalAmount.setCellValueFactory(data -> data.getValue().Total_AmountProperty());
           GetReportgen getReport = new GetReportgen(Items.getKey(), wek1, wek2, wek3, wek4, wek5, wek6, totalAmnt);
           weekTable.getItems().add(getReport);
           lblWk1SumGen.setText(totWek1);   lblWk2SumGen.setText(totWek2);
           lblWk3SumGen.setText(totWek3);   lblWk4SumGen.setText(totWek4);
           lblWk5SumGen.setText(totWek5);   lblWk6SumGen.setText(totWek6);
           lblTotalSumGen.setText(summation);
       }
      }


    private void setItemsSub() throws SQLException{
        ObservableList<String> commWeek =FXCollections.observableArrayList();
        ObservableList<String> valWeek =FXCollections.observableArrayList();
          String totWek1 = "0.00", totWek2 = "0.00", totWek3 = "0.00", totWek4 = "0.00", totWek5 = "0.00", totWek6 = "0.00", summation = "0.00";
          float commAmt1 = 0, commAmt2 = 0, commAmt3 = 0, commAmt4 = 0, commAmt5 = 0, commAmt6 = 0, val1 = 0, val2 = 0,
                  val3 = 0, val4 = 0, val5 = 0, val6 = 0, _18_1 = 0, _18_2 = 0, _18_3 = 0, _18_4 = 0, _18_5 = 0,
                  _18_6 = 0, netRev1 = 0, netRev2 = 0, netRev3 = 0, netRev4 = 0, netRev5 = 0, netRev6 = 0, cost1 = 0,
                  cost2 = 0, cost3 = 0, cost4 = 0, cost5 = 0, cost6 = 0, amtDue1 = 0, amtDue2 = 0, amtDue3 = 0, _18Tot = 0,
                  amtDue4 = 0, amtDue5 = 0, amtDue6 = 0, commTot = 0, valTot = 0, netTot = 0, costTot = 0, amtDTot = 0, totwek1 = 0, totwek2 = 0,totwek3 = 0, totwek4 = 0,
                    totwek5 = 0,totwek6 = 0,totweksum = 0;
              stmnt = con.prepareStatement(" SELECT `revenue_items`.`revenue_item`   FROM `daily_entries`,`revenue_items`,`revenue_centers` WHERE   `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_center` = '" + cmbReportCent.getSelectionModel().getSelectedItem() + "' AND `daily_entries`.`revenueMonth` = '" + cmbReportMonth.getSelectionModel().getSelectedItem() + "'AND `daily_entries`.`revenueYear` = '" + cmbReportYear.getSelectionModel().getSelectedItem() + "'AND `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` GROUP BY `revenue_items`.`revenue_item` ");
//        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem`");
        ResultSet rs = stmnt.executeQuery();
        rowItems.clear();
        while(rs.next()){
            rowItems.add(rs.getString("revenue_item"));
        }
              stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueWeek`");
//        stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries` WHERE   `revenueMonth` = '"+cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueWeek`");
        rs = stmnt.executeQuery();
        rowWeek.clear();
        while(rs.next()){
            rowWeek.add(rs.getString("revenueWeek"));
        }

        stmnt = con.prepareStatement(" SELECT `commission_week` FROM `commission_details`,`revenue_centers` WHERE   `commission_month` = '"+
                cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `revenue_centers`.`CenterID` = `commission_details`.`commission_center` AND `revenue_centers`.`revenue_center` = '"+
                cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `commission_year` = '"+cmbReportYear.
                getSelectionModel().getSelectedItem()+"' GROUP BY `commission_week`");
        rs = stmnt.executeQuery();
        commWeek.clear();
        while(rs.next()){
            commWeek.add(rs.getString("commission_week"));
        }

        stmnt = con.prepareStatement(" SELECT `week` FROM `value_books_stock_record`,`revenue_centers` WHERE   `month` = '"+
                cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `revenue_centers`.`CenterID` =`value_stock_revCenter` AND `revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `year` = '"+cmbReportYear.
                getSelectionModel().getSelectedItem()+"' GROUP BY `week`");
        rs = stmnt.executeQuery();
        valWeek.clear();
        while(rs.next()){
            valWeek.add(rs.getString("week"));
        }
//          Map<String, ArrayList<Float>> weekAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
        Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview
        rowItems.forEach((rowItem) -> {
            forEntry.put(rowItem, new HashMap<>());
        });
//          rowWeek.forEach((rowDates) -> {
//              weekAmount.put(rowDates, new ArrayList<>());
//          });

        try {
            for(String week : rowWeek) {
                for(String Item : rowItems) {
                    float weekSum;
                    weekSum = setWeekSum(cmbReportCent.getSelectionModel().getSelectedItem(), Item, cmbReportMonth.getSelectionModel().getSelectedItem(), week, cmbReportYear.getSelectionModel().getSelectedItem());
//                      for(Entry<String, ArrayList<Float>> Dates : weekAmount.entrySet()){
                    for(Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
                        if (Items.getKey().equals(Item)  ){
                            if(forEntry.containsKey(Item) && !forEntry.get(Item).containsKey(week)){
                                forEntry.get(Item).put(week, new ArrayList<>());
                                forEntry.get(Item).get(week).add(weekSum);
                            }else if(forEntry.containsKey(Item) && forEntry.get(Item).containsKey(week)){
                                forEntry.get(Item).get(week).add(weekSum);
                            }
                        };
                    };
//                      }

                }
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(weeklyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
            for(String week : commWeek) {
                String month = cmbReportMonth.getSelectionModel().getSelectedItem(), year = cmbReportYear.
                        getSelectionModel().getSelectedItem(), cent = cmbReportCent.getSelectionModel().getSelectedItem();
                stmnt = con.prepareStatement("SELECT  `commission_amount` FROM `commission_details`,`revenue_centers` WHERE " +
                        "`commission_week` = '"+week+"' AND `commission_month` = '"+month+"' AND `commission_year` = '"+
                        year+"'AND `revenue_centers`.`CenterID` =`commission_center` AND `revenue_centers`.`revenue_center` = '"+cent+"'");
                rs = stmnt.executeQuery();
                while (rs.next()){
                    switch (week){
                        case "1":
                            commAmt1 += rs.getFloat("commission_amount");
                            break;
                        case "2":
                            commAmt2 += rs.getFloat("commission_amount");
                            break;
                        case "3":
                            commAmt3 += rs.getFloat("commission_amount");
                            break;
                        case "4":
                            commAmt4 += rs.getFloat("commission_amount");
                            break;
                        case "5":
                            commAmt5 += rs.getFloat("commission_amount");
                            break;
                        case "6":
                            commAmt6 += rs.getFloat("commission_amount");
                            break;
                    }
                }
            }
            for(String week : valWeek) {
                String month = cmbReportMonth.getSelectionModel().getSelectedItem(), year = cmbReportYear.
                        getSelectionModel().getSelectedItem(), cent = cmbReportCent.getSelectionModel().getSelectedItem();
                stmnt = con.prepareStatement("SELECT  `purchase_amount` FROM  `value_books_stock_record`,`revenue_centers` WHERE `week` = '"+week+"' AND`year` = '"
                        +year+"' AND `month` = '"+month+"'AND `revenue_centers`.`CenterID` =`value_stock_revCenter` AND `revenue_centers`.`revenue_center` = '"+cent+"'");
                rs = stmnt.executeQuery();
                while (rs.next()){
                    switch (week){
                        case "1":
                            val1 += rs.getFloat("purchase_amount");
                            break;
                        case "2":
                            val2 += rs.getFloat("purchase_amount");
                            break;
                        case "3":
                            val3 += rs.getFloat("purchase_amount");
                            break;
                        case "4":
                            val4 += rs.getFloat("purchase_amount");
                            break;
                        case "5":
                            val5 += rs.getFloat("purchase_amount");
                            break;
                        case "6":
                            val6 += rs.getFloat("purchase_amount");
                            break;
                    }
                }}

        for (String week : commWeek){
            switch (week){
                case "1":
                    _18_1 = (18 * commAmt1)/100;
                    break;
                case "2":
                    _18_2 = (18 * commAmt2)/100;
                    break;
                case "3":
                    _18_3 = (18 * commAmt3)/100;
                    break;
                case "4":
                    _18_4 = (18 * commAmt4)/100;
                    break;
                case "5":
                    _18_5 = (18 * commAmt5)/100;
                    break;
                case "6":
                    _18_6 = (18 * commAmt6)/100;
                    break;
            }
        }
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        for(Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
            String wek1 = "0.00", wek2 = "0.00", wek3 = "0.00", wek4 = "0.00", wek5 = "0.00", wek6 = "0.00",totalAmnt = "0.00";
            float wk1 = 0, wk2 = 0, wk3 = 0, wk4 = 0, wk5 = 0, wk6 = 0, total_amount;
            for(Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ) {
                String reveItem = Items.getKey();
                if (Dates.getKey() == null ? week1Sub.getText() == null : Dates.getKey().equals(week1Sub.getText())) {
                    wek1 = formatter.format(forEntry.get(Items.getKey()).get(week1Sub.getText()).get(0));
                    wk1 = forEntry.get(Items.getKey()).get(week1Sub.getText()).get(0);
                }
                else if (Dates.getKey() == null ? week2Sub.getText() == null : Dates.getKey().equals(week2Sub.getText())) {
                    wek2 = formatter.format(forEntry.get(Items.getKey()).get(week2Sub.getText()).get(0));
                    wk2 = forEntry.get(Items.getKey()).get(week2Sub.getText()).get(0);
                }
                else if ( Dates.getKey().equals("3")) {
                    wk3 = forEntry.get(Items.getKey()).get("3").get(0);
                    wek3 = formatter.format(wk3);
                }
                else if (Dates.getKey().equals("4")) {
                    wk4 = forEntry.get(Items.getKey()).get("4").get(0);
                    wek4 = formatter.format(wk4);
                }
                else if (Dates.getKey().equals("5")) {
                    wek5 = formatter.format(forEntry.get(Items.getKey()).get("5").get(0));
                    wk5 = forEntry.get(Items.getKey()).get("5").get(0);
                }
                else if (Dates.getKey().equals("6")) {
                    wek6 = formatter.format(forEntry.get(Items.getKey()).get("6").get(0));
                    wk6 = forEntry.get(Items.getKey()).get("6").get(0);
                }
            }

        revenueITEMSub.setCellValueFactory(data -> data.getValue().RevenueItemProperty());
        week1Sub.setCellValueFactory(data -> data.getValue().week1Property());
        week2Sub.setCellValueFactory(data -> data.getValue().week2Property());
        week3Sub.setCellValueFactory(data -> data.getValue().week3Property());
        week4Sub.setCellValueFactory(data -> data.getValue().week4Property());
        week5Sub.setCellValueFactory(data -> data.getValue().week5Property());
        week6Sub.setCellValueFactory(data -> data.getValue().week6Property());
        totalAmountSub.setCellValueFactory(data -> data.getValue().Total_AmountProperty());

            total_amount = wk1 + wk2 + wk3 + wk4 + wk5 + wk6;
            totalAmnt = formatter.format(total_amount);
            totwek1 += wk1; totWek1 = formatter.format(totwek1);
            totwek2 += wk2; totWek2 = formatter.format(totwek2);

            totwek3 += wk3; totWek3 = formatter.format(totwek3);

            totwek4 += wk4; totWek4 = formatter.format(totwek4);

            totwek5 += wk5; totWek5 = formatter.format(totwek5);

            totwek6 += wk6; totWek6 = formatter.format(totwek6);

            totweksum += total_amount; summation = formatter.format(totweksum);

            GetReportgen getReport = new GetReportgen(Items.getKey(), wek1, wek2, wek3, wek4, wek5, wek6, totalAmnt);
            weekTableSub.getItems().add(getReport);
            System.out.println(wek3+"\n"+wek4);
}
        System.out.println(forEntry);
        lblWk1SumSub.setText(totWek1);   lblWk2SumSub.setText(totWek2);
        lblWk3SumSub.setText(totWek3);   lblWk4SumSub.setText(totWek4);
        lblWk5SumSub.setText(totWek5);   lblWk6SumSub.setText(totWek6);
        lblTotalSumSub.setText(summation);

        cost1 = val1 + _18_1;
        netRev1 = totwek1 - cost1;
        amtDue1 = netRev1/2 ;
        cost2 = val2 + _18_2;
        netRev2 = totwek2 - cost2;
        amtDue2 = netRev2/2 ;
        cost3 = (val3 + (_18_3));
        netRev3 = totwek3 - cost3;
        amtDue3 = netRev3/2 ;
        cost4 = (val4 + (_18_4));
        netRev4 = totwek4 - cost4;
        amtDue4 = netRev4/2 ;
        cost5 = (val5 + (_18_5));
        netRev5 = totwek5 - cost5;
        amtDue5 = netRev5/2 ;
        cost6 = (val6 + (_18_6));
        netRev6 = totwek6 - cost6;
        amtDue6 = netRev6/2 ;
        commTot = (commAmt1+commAmt2+commAmt3+commAmt4+commAmt5+commAmt6);
        amtDTot = (amtDue5+amtDue1+amtDue6+amtDue2+amtDue3+amtDue4);
        valTot = (val1+val2+val3+val4+val5+val6);
        _18Tot = (_18_1+_18_2+_18_3+_18_4+_18_5+_18_6);
        netTot = (netRev1+netRev2+netRev3+netRev4+netRev5+netRev6);
        costTot = (cost1+cost2+cost3+cost4+cost5+cost6);
        lblAmtDueKMA1.setText(getFunctions.getAmount(Float.toString(amtDue1))); lblAmtDueKMA2.setText(getFunctions.getAmount(Float.toString(amtDue2)));
        lblAmtDueKMA3.setText(getFunctions.getAmount(Float.toString(amtDue3))); lblAmtDueKMA4.setText(getFunctions.getAmount(Float.toString(amtDue4)));
        lblAmtDueKMA5.setText(getFunctions.getAmount(Float.toString(amtDue5))); lblAmtDueKMA6.setText(getFunctions.getAmount(Float.toString(amtDue6)));
        lblAmtDueKMATot.setText(getFunctions.getAmount(Float.toString(amtDTot))); lblAmtDueSub1.setText(getFunctions.getAmount(Float.toString(amtDue1)));
        lblAmtDueSub2.setText(getFunctions.getAmount(Float.toString(amtDue2))); lblAmtDueSub3.setText(getFunctions.getAmount(Float.toString(amtDue3)));
        lblAmtDueSub4.setText(getFunctions.getAmount(Float.toString(amtDue4))); lblAmtDueSub5.setText(getFunctions.getAmount(Float.toString(amtDue5)));
        lblAmtDueSub6.setText(getFunctions.getAmount(Float.toString(amtDue6))); lblAmtDueSubTot.setText(getFunctions.getAmount(Float.toString(amtDTot)));
        lblCCAmount1.setText(getFunctions.getAmount(Float.toString(commAmt1))); lblCCAmount2.setText(getFunctions.getAmount(Float.toString(commAmt2)));
        lblCCAmount3.setText(getFunctions.getAmount(Float.toString(commAmt3))); lblCCAmount4.setText(getFunctions.getAmount(Float.toString(commAmt4)));
        lblCCAmount5.setText(getFunctions.getAmount(Float.toString(commAmt5))); lblCCAmount6.setText(getFunctions.getAmount(Float.toString(commAmt6)));
        lblCCAmountTot.setText(getFunctions.getAmount(Float.toString(commTot))); lblCostValueBooks1.setText(getFunctions.getAmount(Float.toString(val1)));
        lblCostValueBooks2.setText(getFunctions.getAmount(Float.toString(val2))); lblCostValueBooks3.setText(getFunctions.getAmount(Float.toString(val3)));
        lblCostValueBooks4.setText(getFunctions.getAmount(Float.toString(val4))); lblCostValueBooks5.setText(getFunctions.getAmount(Float.toString(val5)));
        lblCostValueBooks6.setText(getFunctions.getAmount(Float.toString(val6))); lblCostValueBooksTot.setText(getFunctions.getAmount(Float.toString(valTot)));
        lblCommission1.setText(getFunctions.getAmount(Float.toString(_18_1))); lblCommission2.setText(getFunctions.getAmount(Float.toString(_18_2)));
        lblCommission3.setText(getFunctions.getAmount(Float.toString(_18_3))); lblCommission4.setText(getFunctions.getAmount(Float.toString(_18_4)));
        lblCommission5.setText(getFunctions.getAmount(Float.toString(_18_5))); lblCommission6.setText(getFunctions.getAmount(Float.toString(_18_6)));
        lblCommissionTot.setText(getFunctions.getAmount(Float.toString(_18Tot))); lblDiff1.setText(getFunctions.getAmount(Float.toString(cost1)));
        lblDiff2.setText(getFunctions.getAmount(Float.toString(cost2))); lblDiff3.setText(getFunctions.getAmount(Float.toString(cost3)));
        lblDiff4.setText(getFunctions.getAmount(Float.toString(cost4))); lblDiff5.setText(getFunctions.getAmount(Float.toString(cost5)));
        lblDiff6.setText(getFunctions.getAmount(Float.toString(cost6))); lblDiffTot.setText(getFunctions.getAmount(Float.toString(costTot)));
        lblNetRevenue1.setText(getFunctions.getAmount(Float.toString(netRev1)));
        lblNetRevenue2.setText(getFunctions.getAmount(Float.toString(netRev2))); lblNetRevenue3.setText(getFunctions.getAmount(Float.toString(netRev3)));
        lblNetRevenue4.setText(getFunctions.getAmount(Float.toString(netRev4))); lblNetRevenue5.setText(getFunctions.getAmount(Float.toString(netRev5)));
        lblNetRevenue6.setText(getFunctions.getAmount(Float.toString(netRev6))); lblNetRevenueTot.setText(getFunctions.getAmount(Float.toString(netTot)));

        System.out.println(cost1);
        System.out.println(cost2);
        System.out.println(cost3);
        System.out.println(cost4);
        System.out.println(cost5);
        System.out.println(cost6);
    }
      
       public Float setWeekSum(String Center, String item, String month, String week, String Year) throws SQLException{
        float totalAmunt;
        if (Center.equals("PROPERTY RATE ALL")){
            stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"' AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+Year+"' AND  `revenueMonth` = '"+month+"' AND `revenueWeek` = '"+week+"'  ");
        }else if (Center.equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"' AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+Year+"' AND  `revenueMonth` = '"+month+"' AND `revenueWeek` = '"+week+"'  ");
        }else {
            stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `revenue_centers`,`daily_entries`, `revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"' AND `daily_entries`.`daily_revCenter` = `revenue_centers`.`CenterID` AND `revenue_centers`.`revenue_center` = '"+Center+"' AND `revenueYear` = '"+Year+"' AND  `revenueMonth` = '"+month+"' AND `revenueWeek` = '"+week+"'  ");
        }
       ResultSet rs = stmnt.executeQuery();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           Amount.add(rs.getFloat("revenueAmount"));
       }
        totalAmunt = 0;
       for(Float n : Amount){
           totalAmunt += n;
       }
        return totalAmunt;
    }

    public void setCommission() throws SQLException {
          String month = cmbReportMonth.getSelectionModel().getSelectedItem(), year = cmbReportYear.getSelectionModel().getSelectedItem(), cent = cmbReportCent.getSelectionModel().getSelectedItem();
          stmnt = con.prepareStatement("SELECT `commission_details`.`commission_amount`, `value_books_stock_record`.`purchase_amount` FROM `commission_details`, `value_books_stock_record` WHERE `commission_details`.`center` AND `commission_details`.`commission_month`AND `commission_details`.`commission_year`");
    }

    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void SelectedYear(ActionEvent event) throws SQLException {
        getReportMonth();
    }

    void setGeneralNull(){
        lblWk1SumGen.setText("-");   lblWk2SumGen.setText("-");
        lblWk3SumGen.setText("-");   lblWk4SumGen.setText("-");
        lblWk5SumGen.setText("-");   lblWk6SumGen.setText("-");
        lblTotalSumGen.setText("-");
    }

    void setSubNull(){
        lblWk1SumSub.setText("-");   lblWk2SumSub.setText("-");
        lblWk3SumSub.setText("-");   lblWk4SumSub.setText("-");
        lblWk5SumSub.setText("-");   lblWk6SumSub.setText("-");
        lblTotalSumSub.setText("-");

        lblAmtDueKMA1.setText("-"); lblAmtDueKMA2.setText("-");
        lblAmtDueKMA3.setText("-"); lblAmtDueKMA4.setText("-");
        lblAmtDueKMA5.setText("-"); lblAmtDueKMA6.setText("-");
        lblAmtDueKMATot.setText("-"); lblAmtDueSub1.setText("-");
        lblAmtDueSub2.setText("-"); lblAmtDueSub3.setText("-");
        lblAmtDueSub4.setText("-"); lblAmtDueSub5.setText("-");
        lblAmtDueSub6.setText("-"); lblAmtDueSubTot.setText("-");
        lblCCAmount1.setText("-"); lblCCAmount2.setText("-");
        lblCCAmount3.setText("-"); lblCCAmount4.setText("-");
        lblCCAmount5.setText("-"); lblCCAmount6.setText("-");
        lblCCAmountTot.setText("-"); lblCostValueBooks1.setText("-");
        lblCostValueBooks2.setText("-"); lblCostValueBooks3.setText("-");
        lblCostValueBooks4.setText("-"); lblCostValueBooks5.setText("-");
        lblCostValueBooks6.setText("-"); lblCostValueBooksTot.setText("-");
        lblCommission1.setText("-"); lblCommission2.setText("-");
        lblCommission3.setText("-"); lblCommission4.setText("-");
        lblCommission5.setText("-"); lblCommission6.setText("-");
        lblCommissionTot.setText("-"); lblDiff1.setText("-");
        lblDiff2.setText("-"); lblDiff3.setText("-");
        lblDiff4.setText("-"); lblDiff5.setText("-");
        lblDiff6.setText("-"); lblDiffTot.setText("-");
        lblNetRevenue1.setText("-");
        lblNetRevenue2.setText("-"); lblNetRevenue3.setText("-");
        lblNetRevenue4.setText("-"); lblNetRevenue5.setText("-");
        lblNetRevenue6.setText("-"); lblNetRevenueTot.setText("-");
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        changeNames();
        if (condSubMetro){
            subPane.setVisible(true);
            generalPane.setVisible(false);
            weekTable.getItems().clear();
            weekTableSub.getItems().clear();
            setItemsSub();
        } else {
            generalPane.setVisible(true);
            subPane.setVisible(false);
            weekTableSub.getItems().clear();
        weekTable.getItems().clear();
        setItems();
        }
        
    }

    @FXML
    void printReport(ActionEvent event) throws JRException, FileNotFoundException {
          if (!weekTable.getItems().isEmpty()){
              Date date = new Date();
              List<GetReportgen> items = new ArrayList<GetReportgen>();
              for (int j = 0; j < weekTable.getItems().size(); j++) {
                  GetReportgen getdata = new GetReportgen();
                  getdata = weekTable.getItems().get(j);
                  items.add(getdata);
              }
              URL url = this.getClass().getResource("/Assets/kmalogo.png");

              System.out.println(items + "\n" + url);
              JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
              String center = cmbReportCent.getSelectionModel().getSelectedItem(),
                      year = cmbReportYear.getSelectionModel().getSelectedItem(),
                      month = cmbReportMonth.getSelectionModel().getSelectedItem();

              /* Map to hold Jasper report Parameters */
              Map<String, Object> parameters = new HashMap<>();
              parameters.put("CollectionBean", itemsJRBean);
              parameters.put("logo", url);
              parameters.put("center",center);
              parameters.put("month", month);
              parameters.put("year", year);
              parameters.put("timeStamp", date);

              //read jrxml file and creating jasperdesign object
              InputStream input = this.getClass().getResourceAsStream("/Assets/weeklyPotrait.jrxml");

              JasperDesign jasperDesign = JRXmlLoader.load(input);

              /*compiling jrxml with help of JasperReport class*/
              JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

              /* Using jasperReport object to generate PDF */
              JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

              /*call jasper engine to display report in jasperviewer window*/
              JasperViewer.viewReport(jasperPrint, false);
          }else if (!weekTableSub.getItems().isEmpty()){
              Date date = new Date();
              List<GetReportgen> items = new ArrayList<>();
              for (int j = 0; j < weekTableSub.getItems().size(); j++) {
                  GetReportgen getdata;
                  getdata = weekTableSub.getItems().get(j);
                  items.add(getdata);
              }
              URL url = this.getClass().getResource("/Assets/kmalogo.png");
              JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
              String center = cmbReportCent.getSelectionModel().getSelectedItem(),tot1 = lblWk1SumSub.getText(),tot2 = lblWk2SumSub.getText(),
                      tot3 = lblWk3SumSub.getText(), tot4 = lblWk4SumSub.getText(), tot5 = lblWk5SumSub.getText(), tot6 = lblWk6SumSub.getText(),
                      tot7 = lblTotalSumSub.getText(), CC1 = lblCCAmount1.getText(), CC2 = lblCCAmount2.getText(), CC3 = lblCCAmount3.getText(),
                      CC4 = lblCCAmount4.getText(), CC5 = lblCCAmount5.getText(), CC6 = lblCCAmount6.getText(), CC7 = lblCCAmountTot.getText(),
                      CO1 = lblCommission1.getText(),CO2 = lblCommission2.getText(),CO3 = lblCommission3.getText(),CO4 = lblCommission4.getText(),
                      CO5 = lblCommission5.getText(),CO6 = lblCommission6.getText(), CO7 = lblCommissionTot.getText(),CV1 = lblCostValueBooks1.getText(),
                      CV2 = lblCostValueBooks2.getText(),CV3 = lblCostValueBooks3.getText(),CV4 = lblCostValueBooks4.getText(),CV5 = lblCostValueBooks5.getText(),
                      CV6 = lblCostValueBooks6.getText(),CV7 = lblCostValueBooksTot.getText(), NR1 = lblNetRevenue1.getText(), NR2 = lblNetRevenue2.getText(),
                      NR3 = lblNetRevenue3.getText(), NR4 = lblNetRevenue4.getText(), NR5 = lblNetRevenue5.getText(), NR6 = lblNetRevenue6.getText(), NR7 = lblNetRevenueTot.getText(),
                      AS1 = lblAmtDueSub1.getText(), AS2 = lblAmtDueSub2.getText(), AS3 = lblAmtDueSub3.getText(), AS4 = lblAmtDueSub4.getText(), AS5 = lblAmtDueSub5.getText(),
                      AS6 = lblAmtDueSub6.getText(), AS7 = lblAmtDueSubTot.getText(), AK1 = lblAmtDueKMA1.getText(), AK2 = lblAmtDueKMA2.getText(), AK3 = lblAmtDueKMA3.getText(),
                      AK4 = lblAmtDueKMA4.getText(), AK5 = lblAmtDueKMA5.getText(), AK6 = lblAmtDueKMA6.getText(), AK7 = lblAmtDueKMATot.getText(),
                      TC1 = lblDiff1.getText(), TC2 = lblDiff2.getText(), TC3 = lblDiff3.getText(), TC4 = lblDiff4.getText(), TC5 = lblDiff5.getText(), TC6 = lblDiff6.getText(),
                      TC7 = lblDiffTot.getText(), year = cmbReportYear.getSelectionModel().getSelectedItem(), month = cmbReportMonth.getSelectionModel().getSelectedItem();

              /* Map to hold Jasper report Parameters */
              Map<String, Object> par = new HashMap<>();
              par.put("CollectionBean", itemsJRBean);par.put("logo", url);par.put("center",center);par.put("month", month);par.put("year", year);par.put("timeStamp", date);
              par.put("TT1", tot1);par.put("TT2", tot2);par.put("TT3", tot3);par.put("TT4", tot4);par.put("TT5", tot5);par.put("TT6", tot6);par.put("TT7", tot7);par.put("TC1", TC1);
              par.put("TC2", TC2);par.put("TC3", TC3);par.put("TC4", TC4);par.put("TC5", TC5);par.put("TC6", TC6);par.put("TC7", TC7);par.put("NR1", NR1);par.put("NR2", NR2);
              par.put("NR3", NR3);par.put("NR4", NR4);par.put("NR5", NR5);par.put("NR6", NR6);par.put("NR7", NR7);par.put("CCA1", CC1);par.put("CCA2", CC2);par.put("CCA3", CC3);
              par.put("CCA4", CC4);par.put("CCA5", CC5);par.put("CCA6", CC6);par.put("CCA7", CC7);par.put("COM1", CO1);par.put("COM2", CO2);par.put("COM3", CO3);par.put("COM4", CO4);
              par.put("COM5", CO5);par.put("COM6", CO6);par.put("COM7", CO7);par.put("COV1", CV1);par.put("COV2", CV2);par.put("COV3", CV3);par.put("COV4", CV4);par.put("COV5", CV5);
              par.put("COV6", CV6);par.put("COV7", CV7);par.put("ADS1", AS1);par.put("ADS2", AS2);par.put("ADS3", AS3);par.put("ADS4", AS4);par.put("ADS5", AS5);par.put("ADS6", AS6);
              par.put("ADS7", AS7);par.put("ADK1", AK1);par.put("ADK2", AK2);par.put("ADK3", AK3);par.put("ADK4", AK4);par.put("ADK5", AK5);par.put("ADK6", AK6);par.put("ADK7", AK7);

              //read jrxml file and creating jasperdesign object
              InputStream input = this.getClass().getResourceAsStream("/Assets/weeklySubMetroPotrait.jrxml");

              JasperDesign jasperDesign = JRXmlLoader.load(input);

              /*compiling jrxml with help of JasperReport class*/
              JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

              /* Using jasperReport object to generate PDF */
              JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, par, new JREmptyDataSource());

              /*call jasper engine to display report in jasperviewer window*/
              JasperViewer.viewReport(jasperPrint, false);
          }else {
              event.consume();
          }
    }


}
