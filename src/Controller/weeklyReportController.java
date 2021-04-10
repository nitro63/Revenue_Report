/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.Conditioner;
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
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonth =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowWeek =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    int Year;
    boolean condSubMetro;
    
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
        
            stmnt = con.prepareStatement("SELECT `daily_revCenter` FROM `daily_entries` WHERE 1 GROUP BY `daily_revCenter` ");
         ResultSet rs = stmnt.executeQuery();
         while(rs.next()){
                 rowCent.add(rs.getString("daily_revCenter"));
         }
         cmbReportCent.getItems().clear();
         cmbReportCent.setItems(rowCent);
         cmbReportCent.setVisibleRowCount(5);
    
         
    }
     private void getReportYear() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE `daily_revCenter` = '"+
                cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
        ResultSet rs = stmnt.executeQuery();
        rowYear.clear();
        while(rs.next()){
            rowYear.add(rs.getString("revenueYear"));
        }
        cmbReportYear.getItems().clear();
        cmbReportYear.getItems().setAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
         String center = cmbReportCent.getSelectionModel().getSelectedItem();
         for (Map.Entry<String, ArrayList<String>> cond : app.catCenter.entrySet()){
             if (cond.getValue().contains(center) && cond.getKey().equals("SUB-METROS")){
                 condSubMetro = true;
             }
         }
         System.out.println(app.catCenter+"\n"+condSubMetro);
    }
     
     private void getReportMonth() throws SQLException{
      /*  if (!cmbReportYear.getItems().isEmpty() || !cmbReportYear.getSelectionModel().isEmpty()){
        Year = Integer.parseInt(cmbReportYear.getSelectionModel().getSelectedItem());}*/
        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE `revenueYear`='"+
                cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.
                getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueMonth`");
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
        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueMonth` = '"+
                cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.
                getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().
                getSelectedItem()+"' GROUP BY `revenueItem`");
        ResultSet rs = stmnt.executeQuery();
        rowItems.clear();
        while(rs.next()){
            rowItems.add(rs.getString("revenueItem"));
        }
          stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries` WHERE   `revenueMonth` = '"+
                  cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+
                  cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.
                  getSelectionModel().getSelectedItem()+"' GROUP BY `revenueWeek`");
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
          
          System.out.println(rowWeek +"\n"+ forEntry);
      }


    private void setItemsSub() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueMonth` = '"+
                cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.
                getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().
                getSelectedItem()+"' GROUP BY `revenueItem`");
        ResultSet rs = stmnt.executeQuery();
        rowItems.clear();
        while(rs.next()){
            rowItems.add(rs.getString("revenueItem"));
        }
        stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries` WHERE   `revenueMonth` = '"+
                cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+
                cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.
                getSelectionModel().getSelectedItem()+"' GROUP BY `revenueWeek`");
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
        stmnt = con.prepareStatement(" SELECT `revenueWeek` FROM `daily_entries` WHERE   `revenueMonth` = '"+
                cmbReportMonth.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+
                cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.
                getSelectionModel().getSelectedItem()+"' GROUP BY `revenueWeek`");
        rs = stmnt.executeQuery();
while (rs.next()){
        for(Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
            String wek1 = "0.00", wek2 = "0.00", wek3 = "0.00", wek4 = "0.00", wek5 = "0.00", wek6 = "0.00",totalAmnt = "0.00",
                    totWek1 = "0.00", totWek2 = "0.00", totWek3 = "0.00", totWek4 = "0.00", totWek5 = "0.00", totWek6 = "0.00", summation = "0.00";
            float wk1 = 0, wk2 = 0, wk3 = 0, wk4 = 0, wk5 = 0, wk6 = 0, total_amount, totwek1 = 0, totwek2 = 0,totwek3 = 0,totwek4 = 0,
                    totwek5 = 0,totwek6 = 0,totweksum = 0;
            for(Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
                String reveItem = Items.getKey();
                System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
                if(Dates.getKey() == null ? week1Sub.getText() == null : Dates.getKey().equals(week1Sub.getText())){
                    wek1 = formatter.format(forEntry.get(Items.getKey()).get(week1Sub.getText()).get(0));
                    wk1 = forEntry.get(Items.getKey()).get(week1Sub.getText()).get(0);
                }else if(Dates.getKey() == null ? week2Sub.getText() == null : Dates.getKey().equals(week2Sub.getText())){
                    wek2 = formatter.format(forEntry.get(Items.getKey()).get(week2Sub.getText()).get(0));
                    wk2 = forEntry.get(Items.getKey()).get(week2Sub.getText()).get(0);
                }else if(Dates.getKey() == null ? week3Sub.getText() == null : Dates.getKey().equals(week3Sub.getText())){
                    wek3 = formatter.format(forEntry.get(Items.getKey()).get(week3Sub.getText()).get(0));
                    wk3 = forEntry.get(Items.getKey()).get(week3Sub.getText()).get(0);
                }else if(Dates.getKey() == null ? week4Sub.getText() == null : Dates.getKey().equals(week4Sub.getText())){
                    wek4 = formatter.format(forEntry.get(Items.getKey()).get(week4Sub.getText()).get(0));
                    wk4 = forEntry.get(Items.getKey()).get(week4Sub.getText()).get(0);
                }else if(Dates.getKey() == null ? week5Sub.getText() == null : Dates.getKey().equals(week5Sub.getText())){
                    wek5 = formatter.format(forEntry.get(Items.getKey()).get(week5Sub.getText()).get(0));
                    wk5 = forEntry.get(Items.getKey()).get(week5Sub.getText()).get(0);
                } else if(Dates.getKey() == null ? week6Sub.getText() == null : Dates.getKey().equals(week6Sub.getText())){
                    wek5 = formatter.format(forEntry.get(Items.getKey()).get(week6Sub.getText()).get(0));
                    wk5 = forEntry.get(Items.getKey()).get(week6Sub.getText()).get(0);
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
            lblWk1SumSub.setText(totWek1);   lblWk2SumSub.setText(totWek2);
            lblWk3SumSub.setText(totWek3);   lblWk4SumSub.setText(totWek4);
            lblWk5SumSub.setText(totWek5);   lblWk6SumSub.setText(totWek6);
            lblTotalSumSub.setText(summation);
        }
}

        /*System.out.println(rowWeek +"\n"+ forEntry);*/
    }
      
       public Float setWeekSum(String Center, String item, String month, String week, String Year) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE  `revenueItem` = '"+
               item+"' AND `revenueWeek` = '"+week+"' AND `daily_revCenter` = '"+Center+"' AND `revenueMonth` = '"+
               month+"' AND `revenueYear` = '"+Year+"'  ");
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

    public void setCommission(){
          stmnt = con.prepareStatement("SELECT ``");
    }

    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void SelectedYear(ActionEvent event) throws SQLException {
        getReportMonth();
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        changeNames();
        if (condSubMetro){
            subPane.setVisible(true);
            generalPane.setVisible(false);
            weekTableSub.getItems().clear();
            setItemsSub();
        } else {
            generalPane.setVisible(true);
            subPane.setVisible(false);
        weekTable.getItems().clear();
        setItems();
        }
        
    }

    @FXML
    void printReport(ActionEvent event) throws JRException, FileNotFoundException {
          if (weekTable.getItems().isEmpty()){
              event.consume();
          }else {
              Date date = new Date();
              List<GetReportgen> items = new ArrayList<GetReportgen>();
              for (int j = 0; j < weekTable.getItems().size(); j++) {
                  GetReportgen getdata = new GetReportgen();
                  getdata = weekTable.getItems().get(j);
                  items.add(getdata);
              }
              URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                      file = this.getClass().getResource("/Assets/weeklyPotrait.jrxml");

              System.out.println(items + "\n" + url);
              JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
              String center = cmbReportCent.getSelectionModel().getSelectedItem(),
              year = cmbReportYear.getSelectionModel().getSelectedItem(),
              month = cmbReportMonth.getSelectionModel().getSelectedItem();

              /* Map to hold Jasper report Parameters */
              Map<String, Object> parameters = new HashMap<String, Object>();
              parameters.put("CollectionBean", itemsJRBean);
              parameters.put("logo", url);
              parameters.put("center",center);
              parameters.put("month", month);
              parameters.put("year", year);
              parameters.put("timeStamp", date);

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
