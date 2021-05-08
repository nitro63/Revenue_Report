/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetMstrQuarterCenters;

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

import Controller.Gets.GetQuarterReport;
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

/**
 * FXML Controller class
 *
 * @author HP
 */
public class MasterQuarterCentersController implements Initializable {

    @FXML
    private VBox quarterlyTemplate;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private ComboBox<String> cmMstCentersYear;
    @FXML
    private ComboBox<String> cmbMstCentersQuarter;
    @FXML
    private Button btnShowReport;
    @FXML
    private TableView<GetMstrQuarterCenters> quarterMastCentersTable;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> revenueCenter;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> quarter;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> month1;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> month2;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> month3;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> totalAmount;
    @FXML
    private TableView<GetMstrQuarterCenters> quarterMastCentersTableAll;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> revenueCenterAll;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> quarterAll;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> month1All;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> month2All;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> month3All;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> month4All;
    @FXML
    private TableColumn<GetMstrQuarterCenters, String> totalAmountAll;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblQtrWarn;
    @FXML
    private Label lblQuarter;
    @FXML
    private Label lblYear;
    
    GetMstrQuarterCenters getReport;

    /**
     * Initializes the controller class.
     */
    
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowQuater =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowCenters =FXCollections.observableArrayList();
    int Year;
   private boolean allQuarters, singleQuarters;
    
    public MasterQuarterCentersController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbMstCentersQuarter.setOnMouseClicked(e -> {
            lblQtrWarn.setVisible(false);
        });
        cmMstCentersYear.setOnMouseClicked(e -> {
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
         cmMstCentersYear.getItems().clear();
         cmMstCentersYear.setItems(rowYear);
         cmMstCentersYear.setVisibleRowCount(5);
    }
    
    private void getQuarter() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueQuarter` FROM `daily_entries` WHERE  `revenueYear` = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueQuarter`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowQuater.clear();
        while(rs.next()){
            rowQuater.add(rs.getString("revenueQuarter"));
        }
        rowQuater.add("All Quarters");
        cmbMstCentersQuarter.getItems().clear();
        cmbMstCentersQuarter.getItems().addAll(rowQuater);
        cmbMstCentersQuarter.setVisibleRowCount(5);
    }
    
    private void getMonths() throws SQLException{
        if (!cmbMstCentersQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
            allQuarters = false;
            singleQuarters = true;
            quarterMastCentersTable.setVisible(true);
            quarterMastCentersTableAll.setVisible(false);
        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE   `revenueYear` = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbMstCentersQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueMonth`");
        ResultSet Rs = stmnt.executeQuery();
        rowMonths.clear();
        while(Rs.next()){
            rowMonths.add(Rs.getString("revenueMonth"));
        }
        month1.setText("MONTH");
        month2.setText("MONTH");
        month3.setText("MONTH");
        String rowSize = cmbMstCentersQuarter.getSelectionModel().getSelectedItem();
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
            singleQuarters = false;
            quarterMastCentersTableAll.setVisible(true);
            quarterMastCentersTable.setVisible(false);
        rowMonths.addAll("1", "2", "3","4");
    }
    }
    
    
    private void changeNames() {
        lblQuarter.setText(cmbMstCentersQuarter.getSelectionModel().getSelectedItem());
        lblYear.setText(cmMstCentersYear.getSelectionModel().getSelectedItem());
    }
    
    private void setItems() throws SQLException{
        if (!cmbMstCentersQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
        stmnt = con.prepareStatement("  SELECT `revenue_center` FROM `daily_entries`,`revenue_centers` WHERE `revenueYear` = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbMstCentersQuarter.getSelectionModel().getSelectedItem()+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` GROUP BY `revenue_center`");
        ResultSet rs = stmnt.executeQuery();
        rowCenters.clear();
        while(rs.next()){
                    rowCenters.add(rs.getString("revenue_center"));
        } }else {
            stmnt = con.prepareStatement(" SELECT `revenue_center` FROM `daily_entries`,`revenue_centers` WHERE `revenueYear` = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` GROUP BY `revenue_center`");
            ResultSet rs = stmnt.executeQuery();
            rowCenters.clear();
            while(rs.next()){
                rowCenters.add(rs.getString("revenue_center"));
            }
        }
          Map<String, ArrayList<Float>> monthAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
          Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview 
          rowCenters.forEach((rowItem) -> {
              forEntry.put(rowItem, new HashMap());
      });
          rowMonths.forEach((rowMonth) -> {
              monthAmount.put(rowMonth, new ArrayList());
          });
          try {
              if (!cmbMstCentersQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
          for(String month : rowMonths) {
              for(String Item : rowCenters) {
                  float monthSum;
                  monthSum = setMonthSum(Item, month, cmMstCentersYear.getSelectionModel().getSelectedItem(), cmbMstCentersQuarter.getSelectionModel().getSelectedItem());
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
          } else {
                  for(String month : rowMonths) {
                      for(String Item : rowCenters) {
                          float monthSum = 0;
                          monthSum = setMonthSum( Item, cmMstCentersYear.getSelectionModel().getSelectedItem(), month);
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
         }
                  catch (SQLException ex) {
                      Logger.getLogger(weeklyReportController.class.getName()).log(Level.SEVERE, null, ex);
                  }
          NumberFormat formatter = new DecimalFormat("#,##0.00");
         
       for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
           String mon1 = "0.00", mon2 = "0.00", mon3 = "0.00", mon4 = "0.00", totalAmnt = "0.00";
           float Mon1 = 0, Mon2 = 0, Mon3 = 0, Mon4 = 0, total_amount;
           for(Map.Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
               String reveItem = Items.getKey();
               System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
               if (!cmbMstCentersQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
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
               else if (allQuarters){
                   if( Dates.getKey().equals("1")){
                       mon1 = formatter.format(forEntry.get(Items.getKey()).get("1").get(0));
                       Mon1 = forEntry.get(Items.getKey()).get("1").get(0);
                   }
                   else if(Dates.getKey().equals("2")){
                       mon2 = formatter.format(forEntry.get(Items.getKey()).get("2").get(0));
                       Mon2 = forEntry.get(Items.getKey()).get("2").get(0);
                   }
                   else if(Dates.getKey().equals("3")){
                       mon3 = formatter.format(forEntry.get(Items.getKey()).get("3").get(0));
                       Mon3 = forEntry.get(Items.getKey()).get("3").get(0);
                   }
                   else if(Dates.getKey().equals("4")){
                       mon4 = formatter.format(forEntry.get(Items.getKey()).get("4").get(0));
                       Mon4 = forEntry.get(Items.getKey()).get("4").get(0);
                   }
               }
           }
           if (singleQuarters) {
               total_amount = Mon3 + Mon2 + Mon1;
               totalAmnt = formatter.format(total_amount);
               revenueCenter.setCellValueFactory(data -> data.getValue().revenueCenterProperty());
               month1.setCellValueFactory(data -> data.getValue().firstMonthProperty());
               month2.setCellValueFactory(data -> data.getValue().secondMonthProperty());
               month3.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
               totalAmount.setCellValueFactory(data -> data.getValue().totalAmountProperty());
               getReport = new GetMstrQuarterCenters(mon1, mon2, mon3, Items.getKey(), totalAmnt);
               quarterMastCentersTable.getItems().add(getReport);
           }
           else if (allQuarters){
               total_amount = Mon3 + Mon2 + Mon1+ Mon4;
               totalAmnt = formatter.format(total_amount);
               revenueCenterAll.setCellValueFactory(data -> data.getValue().revenueCenterProperty());
               month1All.setCellValueFactory(data -> data.getValue().firstMonthProperty());
               month2All.setCellValueFactory(data -> data.getValue().secondMonthProperty());
               month3All.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
               month4All.setCellValueFactory(data -> data.getValue().fourthMonthProperty());
               totalAmountAll.setCellValueFactory(data -> data.getValue().totalAmountProperty());
               getReport = new GetMstrQuarterCenters( mon1, mon2, mon3, mon4, Items.getKey(), totalAmnt);
               quarterMastCentersTableAll.getItems().add(getReport);
           }
       }
    }

    public Float setMonthSum(String Center, String Year, String Quarter) throws SQLException{
        float totalAmunt;
        stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers` WHERE  `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_center` = '"+Center+"'");
        ResultSet rs = stmnt.executeQuery();
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
    
       public Float setMonthSum(String Center, String Month, String Year, String Quarter) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount` FROM `daily_entries`,`revenue_centers` WHERE `revenueMonth` = '"+Month+"' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_center` = '"+Center+"'");
       ResultSet rs = stmnt.executeQuery();
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
    private void SelectedYear(ActionEvent event) throws SQLException {
        getQuarter();
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        if(cmMstCentersYear.getSelectionModel().isEmpty()){
            lblYearWarn.setVisible(true);
        }else if (cmbMstCentersQuarter.getSelectionModel().isEmpty()){
            lblQtrWarn.setVisible(true);
        } else {
        quarterMastCentersTable.getItems().clear();
        getMonths();
        changeNames();
        setItems();
        }
    }

    @FXML
    void printReport(ActionEvent event) throws JRException, FileNotFoundException {
        if (quarterMastCentersTable.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetMstrQuarterCenters> items = new ArrayList<GetMstrQuarterCenters>();
            for (int j = 0; j < quarterMastCentersTable.getItems().size(); j++) {
                GetMstrQuarterCenters getdata = new GetMstrQuarterCenters();
                getdata = quarterMastCentersTable.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                    file = this.getClass().getResource("/Assets/masterQuarterCentersPotrait.jrxml");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmMstCentersYear.getSelectionModel().getSelectedItem(),
                    first = month1.getText(),
                    second = month2.getText(),
                    third = month3.getText();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("FirstMonth", first);
            parameters.put("year", year); parameters.put("SecondMonth", second);
            parameters.put("timeStamp", date); parameters.put("ThirdMonth", third);

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
