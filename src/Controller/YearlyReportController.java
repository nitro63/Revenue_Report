/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetQuarterReport;
import Controller.Gets.GetYearlyReport;

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
 * @author NiTrO
 */
public class YearlyReportController implements Initializable {

    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private ComboBox<String> cmbReportYear1;
    @FXML
    private ComboBox<String> cmbReportYear2;
    @FXML
    private Button btnShowReport;
    @FXML
    private TableView<GetYearlyReport> yearlyTable;
    @FXML
    private TableColumn<?, ?> yearRange;
    @FXML
    private VBox yearlyTemplate;
    @FXML
    private TableColumn<GetYearlyReport, String> revenueCenter;
    @FXML
    private TableColumn<GetYearlyReport, String> revenueItem;
    @FXML
    private TableColumn<GetYearlyReport, String> year1;
    @FXML
    private TableColumn<GetYearlyReport, String> year2;
    @FXML
    private TableColumn<GetYearlyReport, String> year3;
    @FXML
    private TableColumn<GetYearlyReport, String> year4;
    @FXML
    private TableColumn<GetYearlyReport, String> year5;
    @FXML
    private TableColumn<GetYearlyReport, String> totalAmount;
    @FXML
    private Label lblYear2;
    @FXML
    private Label lblYear1;
    @FXML
    private Label lblRevenueCenter;

    GetYearlyReport getReport;
    
    
    private final Connection con;
    private PreparedStatement stmnt;
    ObservableList<String> rowDate =FXCollections.observableArrayList();
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowYear1 =FXCollections.observableArrayList();
    ObservableList<String> rowYear2 =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    
    public YearlyReportController() throws ClassNotFoundException, SQLException{
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
            Logger.getLogger(QuarterlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(QuarterlyReportController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    private void getReportYear1() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowYear1.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 String value = rs.getObject(i).toString();
                 
                 rowYear1.add(value);
                 
             }
        }
        cmbReportYear1.getItems().clear();
        cmbReportYear1.getItems().setAll(rowYear1);
        cmbReportYear1.setVisibleRowCount(5);
    }
    
    private void getReportYear2() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` >= '"+cmbReportYear1.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueYear` LIMIT 5");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        int colum = meta.getColumnCount();
        rowYear2.clear();
        while(rs.next()){
            for(int i= 1; i<=colum; i++)
             {
                 String value = rs.getObject(i).toString();
                 
                 rowYear2.add(value);
                 
             }
        }
        cmbReportYear2.getItems().clear();
        cmbReportYear2.getItems().setAll(rowYear2);
        cmbReportYear2.setVisibleRowCount(5);
    }
    
    private void getYears() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueYear` FROM `daily_entries` WHERE `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` >= '"+cmbReportYear1.getSelectionModel().getSelectedItem()+"' AND `revenueYear` <= '"+cmbReportYear2.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueYear` ");
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
        year1.setText("Year 1");
        year2.setText("Year 2");
        year3.setText("Year 3");
        year4.setText("Year 4");
        year5.setText("Year 5");
        int rowSize = rowYear.size();
        switch(rowSize){
            case 1:
                year1.setText(rowYear.get(0));
                break;
            case 2: 
                year1.setText(rowYear.get(0));
                year2.setText(rowYear.get(1));
                break;
            case 3:
                year1.setText(rowYear.get(0));
                year2.setText(rowYear.get(1));
                year3.setText(rowYear.get(2));
                break;
            case 4:
                year1.setText(rowYear.get(0));
                year2.setText(rowYear.get(1));
                year3.setText(rowYear.get(2));
                year4.setText(rowYear.get(3));
                break;
            case 5:
                year1.setText(rowYear.get(0));
                year2.setText(rowYear.get(1));
                year3.setText(rowYear.get(2));
                year4.setText(rowYear.get(3));
                year5.setText(rowYear.get(4));
                break;        
    }
    }
    
    private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblYear1.setText(cmbReportYear1.getSelectionModel().getSelectedItem());
        lblYear2.setText(cmbReportYear2.getSelectionModel().getSelectedItem());
    }
    
      private void setItems() throws SQLException{
        stmnt = con.prepareStatement(" SELECT `revenueItem` FROM `daily_entries` WHERE   `revenueYear` >= '"+cmbReportYear1.getSelectionModel().getSelectedItem()+"' AND `revCenter` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` <= '"+cmbReportYear2.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueItem`");
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
          Map<String, ArrayList<Float>> weekAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
          Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview 
          rowItems.forEach((rowItem) -> {
              forEntry.put(rowItem, new HashMap());
      });
          rowYear.forEach((rowDates) -> {
              weekAmount.put(rowDates, new ArrayList());
          });
          try {
          for(String year : rowYear) {
              for(String Item : rowItems) {
                  float weekSum;
                   weekSum = setYearSum(cmbReportCent.getSelectionModel().getSelectedItem(), Item, year);
                      for(Map.Entry<String, ArrayList<Float>> Dates : weekAmount.entrySet()){
                          for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
                              if (Items.getKey().equals(Item)  && Dates.getKey().equals(year)){
                                  if(forEntry.containsKey(Item) && !forEntry.get(Item).containsValue(year)){
                                      forEntry.get(Item).put(year, new ArrayList());
                                      forEntry.get(Item).get(year).add(weekSum);
                                  }else if(forEntry.containsKey(Item) && forEntry.get(Item).containsValue(year)){
                                      forEntry.get(Item).get(year).add(weekSum);
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
           String yer1 = "0.00", yer2 = "0.00", yer3 = "0.00", yer4 = "0.00", yer5 = "0.00", totalAmnt = "0.00";
           float yr1 = 0, yr2 = 0, yr3 = 0, yr4 = 0, yr5 = 0, total_amount;
           for(Map.Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
               String reveItem = Items.getKey();
               System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
               if(Dates.getKey() == null ? year1.getText() == null : Dates.getKey().equals(year1.getText())){
                   yer1 = formatter.format(forEntry.get(Items.getKey()).get(year1.getText()).get(0));
                   yr1 = forEntry.get(Items.getKey()).get(year1.getText()).get(0);
               }else if(Dates.getKey() == null ? year2.getText() == null : Dates.getKey().equals(year2.getText())){
                   yer2 = formatter.format(forEntry.get(Items.getKey()).get(year2.getText()).get(0));
                   yr2 = forEntry.get(Items.getKey()).get(year2.getText()).get(0);
               }else if(Dates.getKey() == null ? year3.getText() == null : Dates.getKey().equals(year3.getText())){
                   yer3 = formatter.format(forEntry.get(Items.getKey()).get(year3.getText()).get(0));
                   yr3 = forEntry.get(Items.getKey()).get(year3.getText()).get(0);
               }else if(Dates.getKey() == null ? year4.getText() == null : Dates.getKey().equals(year4.getText())){
                   yer4 = formatter.format(forEntry.get(Items.getKey()).get(year4.getText()).get(0));
                   yr4 = forEntry.get(Items.getKey()).get(year4.getText()).get(0);
               }else if(Dates.getKey() == null ? year5.getText() == null : Dates.getKey().equals(year5.getText())){
                   yer5 = formatter.format(forEntry.get(Items.getKey()).get(year5.getText()).get(0));
                   yr5 = forEntry.get(Items.getKey()).get(year5.getText()).get(0);
               }
           }
           total_amount = yr1 + yr2 + yr3 + yr4 + yr5;
           totalAmnt = formatter.format(total_amount);       
           revenueItem.setCellValueFactory(data -> data.getValue().revenueItemProperty());
           year1.setCellValueFactory(data -> data.getValue().year1Property());
           year2.setCellValueFactory(data -> data.getValue().year2Property());
           year3.setCellValueFactory(data -> data.getValue().year3Property());
           year4.setCellValueFactory(data -> data.getValue().year4Property());
           year5.setCellValueFactory(data -> data.getValue().year5Property());
           totalAmount.setCellValueFactory(data -> data.getValue().totalAmountProperty());
           getReport = new GetYearlyReport(yer1, yer2, yer3, yer4, yer5, Items.getKey(),  totalAmnt);
           yearlyTable.getItems().add(getReport);                                           
       }
          
      }
      
      
       public Float setYearSum(String Center, String item, String Year1) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries` WHERE  `revenueItem` = '"+item+"' AND `revenueYear` = '"+Year1+"'AND `revCenter` = '"+Center+"'");
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
        getReportYear1();
    }

    @FXML
    private void SelectedYear(ActionEvent event) throws SQLException {
        getReportYear2();
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        yearlyTable.getItems().clear();
        getYears();
        changeNames();
        setItems();
    }

    @FXML
    void printReport(ActionEvent event) throws JRException, FileNotFoundException {
        if (yearlyTable.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetYearlyReport> items = new ArrayList<GetYearlyReport>();
            for (int j = 0; j < yearlyTable.getItems().size(); j++) {
                GetYearlyReport getdata = new GetYearlyReport();
                getdata = yearlyTable.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png"),
                    file = this.getClass().getResource("/Assets/yearlyPotrait.jrxml");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmbReportYear1.getSelectionModel().getSelectedItem(),
                    secondyear = cmbReportYear2.getSelectionModel().getSelectedItem(),
                    center = cmbReportCent.getSelectionModel().getSelectedItem(),
                    first = year1.getText(),
                    second = year2.getText(),
                    third = year3.getText(),
                    fourth = year4.getText(),
                    fifth = year5.getText();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("firstYear", first);
            parameters.put("year", year); parameters.put("secondYear", second);
            parameters.put("timeStamp", date); parameters.put("thirdYear", third);
            parameters.put("center", center); parameters.put("fourthYear", fourth);
            parameters.put("year2", secondyear); parameters.put("fifthYear", fifth);

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
