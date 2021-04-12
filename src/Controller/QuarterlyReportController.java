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
    private TableColumn<?, ?> revenueCenter;
    @FXML
    private TableColumn<GetQuarterReport, String> revenueItem;
    @FXML
    private TableColumn<?, ?> quarter;
    @FXML
    private TableColumn<GetQuarterReport, String> month1;
    @FXML
    private TableColumn<GetQuarterReport, String> month2;
    @FXML
    private TableColumn<GetQuarterReport, String> month3;
    @FXML
    private TableColumn<GetQuarterReport, String> totalAmount;
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
    int Year;
    @FXML
    private TableColumn<?, ?> year;
    
    public QuarterlyReportController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            getRevCenters();
        } catch (SQLException ex) {
            Logger.getLogger(QuarterlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QuarterlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getRevCenters() throws SQLException, ClassNotFoundException{
        
            stmnt = con.prepareStatement("SELECT `daily_revCenter` FROM `daily_entries` WHERE 1 GROUP BY `daily_revCenter` ");
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
        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
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
    
    private void getQuarter() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueQuarter` FROM `daily_entries` WHERE `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueQuarter`");
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
        rowQuater.add("All Quarters");
        cmbReportQuarter.getItems().clear();
        cmbReportQuarter.getItems().setAll(rowQuater);
        cmbReportQuarter.setVisibleRowCount(5);
    }
    
    
    private void getMonths() throws SQLException{
        if (!cmbReportQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueMonth`");
        ResultSet Rs = stmnt.executeQuery();
        rowMonths.clear();
        while(Rs.next()){
            rowMonths.add(Rs.getString("revenueMonth"));
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
        }else {
            month1.setText("1st Quarter");
            month2.setText("2nd Quarter");
            month3.setText("3rd Quarter");
            rowMonths.addAll("1", "2", "3");
        }
    }
    
    
    private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblQuarter.setText( cmbReportQuarter.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem());
    }
    
    private void setItems() throws SQLException{
        if (!cmbReportQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem`");
        ResultSet rs = stmnt.executeQuery();
        rowItems.clear();
        while(rs.next()){
            rowItems.add(rs.getString("revenueItem"));
        }
        } else {
            stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `daily_revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem`");
            ResultSet rs = stmnt.executeQuery();
            rowItems.clear();
            while(rs.next()){
                        rowItems.add(rs.getString("revenueItem"));
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
              if (!cmbReportQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
          for(String month : rowMonths) {
              for(String Item : rowItems) {
                  float monthSum;
                  monthSum = setMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), Item, month, cmbReportYear.getSelectionModel().getSelectedItem(), cmbReportQuarter.getSelectionModel().getSelectedItem());
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
              } else {
                  for(String month : rowMonths) {
                      for(String Item : rowItems) {
                          float monthSum = 0;
                          monthSum = setMonthSum(cmbReportCent.getSelectionModel().getSelectedItem(), Item, cmbReportYear.getSelectionModel().getSelectedItem(), month);
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
                  System.out.println(forEntry);
              }
         }
                  catch (SQLException ex) {
                      Logger.getLogger(weeklyReportController.class.getName()).log(Level.SEVERE, null, ex);
                  }
          NumberFormat formatter = new DecimalFormat("#,##0.00");
         
       for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
           String mon1 = "0.00", mon2 = "0.00", mon3 = "0.00", totalAmnt = "0.00";
           float Mon1 = 0, Mon2 = 0, Mon3 = 0, total_amount;
           for(Map.Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
               String reveItem = Items.getKey();
               System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
               if (!cmbReportQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
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
           } else{
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
               }
           }
           total_amount = Mon3 + Mon2 + Mon1;
           totalAmnt = formatter.format(total_amount);       
           revenueItem.setCellValueFactory(data -> data.getValue().revenueItemProperty());
           month1.setCellValueFactory(data -> data.getValue().firstMonthProperty());
           month2.setCellValueFactory(data -> data.getValue().secondMonthProperty());
           month3.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
           totalAmount.setCellValueFactory(data -> data.getValue().totalAmountProperty());
           getReport = new GetQuarterReport( mon1, mon2, mon3, Items.getKey(), totalAmnt);
           quarterTable.getItems().add(getReport);                                           
       }
    }

    public Float setMonthSum(String Center, String item, String Year, String Quarter) throws SQLException{
        float totalAmunt;
        stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE  `revenueItem` = '"+item+"' AND `daily_revCenter` = '"+Center+"' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' ");
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
    
       public Float setMonthSum(String Center, String item, String Month, String Year, String Quarter) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE  `revenueItem` = '"+item+"' AND `revenueMonth` = '"+Month+"' AND `daily_revCenter` = '"+Center+"' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' ");
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
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void SelectedYear(ActionEvent event) throws SQLException {
        getQuarter();
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        quarterTable.getItems().clear();
        getMonths();
        changeNames();
        setItems();
    }

    @FXML
    void printReport(ActionEvent event) throws FileNotFoundException, JRException {
        if (quarterTable.getItems().isEmpty()){
            event.consume();
        }else {
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
        }
    }
    
}
