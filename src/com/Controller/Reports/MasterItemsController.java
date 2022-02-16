/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller.Reports;

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

import com.Enums.Months;
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
import com.revenue_report.DBConnection;
import com.Models.GetItemsReport;

/**
 * FXML com.Controller class
 *
 * @author NiTrO
 */
public class MasterItemsController implements Initializable {

    @FXML
    private VBox masterItemsTemplate;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private ComboBox<String> cmbMasterItemsYear;
    @FXML
    private JFXButton btnShowReport;
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
    private Label lblYear;
    @FXML
    private Label lblRevenueCenter;
    @FXML
    private Label lblYearWarn;

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
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(MonthlyReportController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    private void getRevenueYears() throws SQLException, ClassNotFoundException{
        
            stmnt = con.prepareStatement("SELECT YEAR(revenueDate) AS `revenueYear` FROM `daily_entries` WHERE 1 GROUP BY `revenueYear` ");
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
        lblYear.setText(cmbMasterItemsYear.getSelectionModel().getSelectedItem());
    }
    
   private void setItems() throws SQLException{
       String totJan = "0.00", totFeb = "0.00", totMar = "0.00", totApr = "0.00", totMay = "0.00", totJun = "0.00", totJul = "0.00", totAug = "0.00", totSep = "0.00", totOct = "0.00", totNov = "0.00", totDec = "0.00", summation = "0.00", Jan = "0.00", Feb = "0.00", Mar = "0.00", Apr = "0.00", May = "0.00", Jun = "0.00", Jul = "0.00", Aug = "0.00", Sep = "0.00", Oct = "0.00", Nov = "0.00", Dec = "0.00", totalAmnt = "0.00";
       float  totjan = 0, totfeb = 0,totmar = 0,totapr = 0, totmay = 0, totjun = 0, totjul = 0, totaug = 0,totsep = 0,totoct = 0, totnov = 0, totdec = 0, totMonthsum = 0,  jan = 0, feb = 0, apr = 0, mai = 0, jun = 0, jul = 0, aug = 0, sep = 0, oct = 0, mar = 0, nov = 0, dec = 0, total_amount = 0;
       NumberFormat formatter = new DecimalFormat("#,##0.00");
       PreparedStatement stmnt_itemsCategories;
       ResultSet rs, rs_itemsCategories;
       stmnt = con.prepareStatement(" SELECT `revenue_item`, MONTH(revenueDate) AS `revenueMonth`, `item_category`, `revenueAmount` FROM `revenue_items`,`daily_entries` WHERE  `revenue_item_ID` = `revenueItem` AND YEAR(revenueDate) = '"+cmbMasterItemsYear.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_item` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       stmnt_itemsCategories = con.prepareStatement(" SELECT `revenue_item`, `item_category` FROM `revenue_items`,`daily_entries` WHERE  `revenue_item_ID` = `revenueItem` AND YEAR(revenueDate) = '"+cmbMasterItemsYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");
       rs = stmnt.executeQuery();
       rs_itemsCategories = stmnt_itemsCategories.executeQuery();
       rowItems.clear();
       Map<String, ArrayList<String>> categoriesItem = new HashMap<>();
       categoriesItem.put("", new ArrayList<>());
       while(rs_itemsCategories.next()){
           if (!rowItems.contains(rs_itemsCategories.getString("item_category"))){
               rowItems.add(rs_itemsCategories.getString("item_category"));
           }
           if (!categoriesItem.containsKey(rs_itemsCategories.getString("item_category"))){
               categoriesItem.put(rs_itemsCategories.getString("item_category"), new ArrayList<>());
               categoriesItem.get(rs_itemsCategories.getString("item_category")).add(rs_itemsCategories.getString("revenue_item"));
           }else if (categoriesItem.containsKey(rs_itemsCategories.getString("item_category")) && !categoriesItem.get(rs_itemsCategories.getString("item_category")).contains(rs_itemsCategories.getString("revenue_item"))){
               categoriesItem.get(rs_itemsCategories.getString("item_category")).add(rs_itemsCategories.getString("revenue_item"));
           }
       }

       revenueItem.setCellValueFactory(data -> data.getValue().RevenueItemProperty());
       revenueItem.setSortable(false); march.setSortable(false); april.setSortable(false); may.setSortable(false); june.setSortable(false);
       january.setSortable(false); july.setSortable(false); august.setSortable(false); september.setSortable(false); october.setSortable(false);
       february.setSortable(false); november.setSortable(false); december.setSortable(false); totalAmount.setSortable(false);
       revenueItem.getText().toUpperCase(); march.setText(Months.MARCH.toString()); april.setText(Months.APRIL.toString()); may.setText(Months.MAY.toString()); june.setText(Months.JUNE.toString());
       january.setText(Months.JANUARY.toString()); july.setText(Months.JULY.toString()); august.setText(Months.AUGUST.toString()); september.setText(Months.SEPTEMBER.toString()); october.setText(Months.OCTOBER.toString());
       february.setText(Months.FEBRUARY.toString()); november.setText(Months.NOVEMBER.toString()); december.setText(Months.DECEMBER.toString());
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
       revenueItem.setStyle("-fx-alignment: CENTER;-fx-wrap-text: TRUE;");
       GetItemsReport getReport;
       for (String category : rowItems){
           revenueItem.setStyle("-fx-alignment: CENTER; -fx-text-fill: #5a5959;");
           getReport = new GetItemsReport(category, "", "", "", "", "", "", "", "", "", "", "", "", "");
           revenueItemsTable.getItems().add(getReport);
           float subjan = 0, subfeb = 0, subapr = 0, submai = 0, subjun = 0, subjul = 0, subaug = 0, subsep = 0, suboct = 0, submar = 0, subnov = 0, subdec = 0, subtotal_amount;
           String subJan = "0.00", subFeb = "0.00", subMar = "0.00", subApr = "0.00", subMay = "0.00", subJun = "0.00", subJul = "0.00", subAug = "0.00", subSep = "0.00", subOct = "0.00", subNov = "0.00", subDec = "0.00", subTotalAmnt = "0.00";
           for (String item : categoriesItem.get(category)) {
               Map<String, Map<String, Float>> itemMonthSum = new HashMap<>();
               Map<String, Float> monthSum = new HashMap<>();
               monthSum.put(january.getText(), jan); monthSum.put(february.getText(), feb); monthSum.put(march.getText(), mar);
               monthSum.put(april.getText(), apr); monthSum.put(may.getText(), mai); monthSum.put(june.getText(), jun);
               monthSum.put(july.getText(), jul); monthSum.put(august.getText(), aug); monthSum.put(september.getText(), sep);
               monthSum.put(october.getText(), oct); monthSum.put(november.getText(), nov); monthSum.put(december.getText(), dec);
               itemMonthSum.put(item, monthSum);
               boolean resultSetState = true;
               while (resultSetState){
                   rs.next();
                   if (item.equals(rs.getString("revenue_item"))){
                       float amot= itemMonthSum.get(item).get(Months.get(rs.getInt("revenueMonth")).toString());
                       amot += rs.getFloat("revenueAmount");
                       itemMonthSum.get(item).put(Months.get(rs.getInt("revenueMonth")).toString(), amot);
                   }
                   if (rs.isLast()){
                       resultSetState = false;
                   }
               }
               if (rs.isLast()){
                   rs.beforeFirst();
               }
               subjan += itemMonthSum.get(item).get(january.getText()); subfeb += itemMonthSum.get(item).get(february.getText()); submar += itemMonthSum.get(item).get(march.getText()); subapr += itemMonthSum.get(item).get(april.getText()); submai += itemMonthSum.get(item).get(may.getText()); subjun += itemMonthSum.get(item).get(june.getText());
               subjul += itemMonthSum.get(item).get(july.getText()); subaug += itemMonthSum.get(item).get(august.getText()); subsep += itemMonthSum.get(item).get(september.getText()); suboct += itemMonthSum.get(item).get(october.getText()); subnov += itemMonthSum.get(item).get(november.getText()); subdec += itemMonthSum.get(item).get(december.getText());
               totjan += itemMonthSum.get(item).get(january.getText()); totfeb += itemMonthSum.get(item).get(february.getText()); totmar += itemMonthSum.get(item).get(march.getText()); totapr += itemMonthSum.get(item).get(april.getText()); totmay += itemMonthSum.get(item).get(may.getText()); totjun += itemMonthSum.get(item).get(june.getText());
               totjul += itemMonthSum.get(item).get(july.getText()); totaug += itemMonthSum.get(item).get(august.getText()); totsep += itemMonthSum.get(item).get(september.getText()); totoct += itemMonthSum.get(item).get(october.getText()); totnov += itemMonthSum.get(item).get(november.getText()); totdec += itemMonthSum.get(item).get(december.getText());
               Jan = formatter.format(itemMonthSum.get(item).get(january.getText())); Feb = formatter.format(itemMonthSum.get(item).get(february.getText())); Mar = formatter.format(itemMonthSum.get(item).get(march.getText())); Apr = formatter.format(itemMonthSum.get(item).get(april.getText())); May = formatter.format(itemMonthSum.get(item).get(may.getText()));
               Jun = formatter.format(itemMonthSum.get(item).get(june.getText())); Jul = formatter.format(itemMonthSum.get(item).get(july.getText())); Aug = formatter.format(itemMonthSum.get(item).get(august.getText())); Sep = formatter.format(itemMonthSum.get(item).get(september.getText())); Oct = formatter.format(itemMonthSum.get(item).get(october.getText()));
               Nov = formatter.format(itemMonthSum.get(item).get(november.getText())); Dec = formatter.format(itemMonthSum.get(item).get(december.getText()));
               total_amount = itemMonthSum.get(item).get(january.getText()) + itemMonthSum.get(item).get(february.getText()) + itemMonthSum.get(item).get(march.getText()) + itemMonthSum.get(item).get(april.getText()) + itemMonthSum.get(item).get(may.getText()) +itemMonthSum.get(item).get(june.getText()) + itemMonthSum.get(item).get(july.getText()) + itemMonthSum.get(item).get(august.getText()) + itemMonthSum.get(item).get(september.getText()) + itemMonthSum.get(item).get(october.getText()) + itemMonthSum.get(item).get(november.getText()) + itemMonthSum.get(item).get(december.getText()); totMonthsum += total_amount;
               totalAmnt = formatter.format(total_amount);
               getReport = new GetItemsReport(item, Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec, totalAmnt);
               revenueItemsTable.getItems().add(getReport);
               jan = 0; feb = 0; apr = 0; mai = 0; jun = 0; jul = 0; aug = 0; sep = 0; oct = 0; mar = 0; nov = 0; dec = 0;
           }
           subtotal_amount = subjan + subfeb + submar + subapr + submai + subjun + subjul + subaug + subsep + suboct + subnov + subdec;
           subJan = formatter.format(subjan); subFeb = formatter.format(subfeb); subMar = formatter.format(submar); subApr = formatter.format(subapr);
           subMay = formatter.format(submai);subJun = formatter.format(subjun); subJul = formatter.format(subjul); subAug = formatter.format(subaug); subSep = formatter.format(subsep);
           subOct = formatter.format(suboct); subNov = formatter.format(subnov); subDec = formatter.format(subdec); subTotalAmnt = formatter.format(subtotal_amount);
           getReport = new GetItemsReport("SUB-TOTAL", subJan, subFeb, subMar, subApr, subMay, subJun, subJul,subAug,subSep,subOct,subNov,subDec, subTotalAmnt);
           revenueItemsTable.getItems().add(getReport);
       }
       totJan = formatter.format(totjan); totFeb = formatter.format(totfeb); totMar = formatter.format(totmar); totApr = formatter.format(totapr);
       totMay = formatter.format(totmay); totJun = formatter.format(totjun); totJul = formatter.format(totjul); totAug = formatter.format(totaug); totSep = formatter.format(totsep);
       totOct = formatter.format(totoct); totNov = formatter.format(totnov); totDec = formatter.format(totdec); summation = formatter.format(totMonthsum);
       getReport = new GetItemsReport("TOTAL", totJan, totFeb, totMar, totApr, totMay, totJun, totJul, totAug, totSep, totOct, totNov, totDec, summation);
       revenueItemsTable.getItems().add(getReport);
          
    } 
   
   
       public Float setCentersSum(String Item, String Month, String Year) throws SQLException{
        float totalAmunt;
       stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `revenue_items`,`daily_entries` WHERE `revenue_item_ID` = `revenueItem` AND `revenue_item` = '"+Item+"' AND `revenueMonth` = '"+Month+"' AND `revenueYear` = '"+Year+"'  ");
       ResultSet rs = stmnt.executeQuery();
       ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
       while(rs.next()){//looping through the retrieved revenueItems result set
           Amount.add(rs.getFloat("revenueAmount"));
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

    @FXML
    void printReport(ActionEvent event) throws JRException, FileNotFoundException {
        if (revenueItemsTable.getItems().isEmpty()){
            event.consume();
        }else {
            Date date = new Date();
            List<GetItemsReport> items = new ArrayList<>();
            for (int j = 0; j < revenueItemsTable.getItems().size(); j++) {
                GetItemsReport getdata;
                getdata = revenueItemsTable.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/com/Assets/kmalogo.png");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String yr = cmbMasterItemsYear.getSelectionModel().getSelectedItem();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url);
            parameters.put("year", yr);
            parameters.put("timeStamp", date);

            //read jrxml file and creating jasperdesign object
            InputStream input = this.getClass().getResourceAsStream("/com/Assets/masterItemsPotrait.jrxml");

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


/***
 *
 *
 *    private void setItems() throws SQLException{
 *         stmnt = con.prepareStatement(" SELECT `revenue_item` FROM `revenue_items`,`daily_entries` WHERE  `revenue_item_ID` = `revenueItem` AND `revenueYear` = '"+cmbMasterItemsYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");
 *         ResultSet rs = stmnt.executeQuery();
 *         ResultSetMetaData meta = rs.getMetaData();
 *         int col = meta.getColumnCount();
 *         rowItems.clear();
 *         while(rs.next()){
 *             rowItems.add(rs.getString("revenue_item"));
 *         }
 *         stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE   `revenueYear` = '"+cmbMasterItemsYear.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueMonth`");
 *         ResultSet Rs = stmnt.executeQuery();
 *         ResultSetMetaData Meta = Rs.getMetaData();
 *         int Col = Meta.getColumnCount();
 *         rowMonths.clear();
 *         while(Rs.next()){
 *             for(int i=1; i<=Col; i++){
 *                 if(i == 1){
 *
 *                     rowMonths.add(Rs.getObject(i).toString());
 *
 *                 }
 *             }
 *         }
 *           Map<String, ArrayList<Float>> monthAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
 *           Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview
 *           rowItems.forEach((Center) -> {
 *               forEntry.put(Center, new HashMap<>());
 *       });
 *           rowMonths.forEach((rowMonth) -> {
 *               monthAmount.put(rowMonth, new ArrayList<>());
 *           });
 *           try {
 *           for(String month : rowMonths) {
 *               for(String Item : rowItems) {
 *                   float monthSum;
 *                   monthSum = setCentersSum(Item, month, cmbMasterItemsYear.getSelectionModel().getSelectedItem());
 *                   for(Map.Entry<String, ArrayList<Float>> Dates : monthAmount.entrySet()){
 *                           for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
 *                               if (Items.getKey().equals(Item)  && Dates.getKey().equals(month)){
 *                                   if(forEntry.containsKey(Item) && !forEntry.get(Item).containsKey(month)){
 *                                       forEntry.get(Item).put(month, new ArrayList<>());
 *                                       forEntry.get(Item).get(month).add(monthSum);
 *                                   }else if(forEntry.containsKey(Item) && forEntry.get(Item).containsKey(month)){
 *                                       forEntry.get(Item).get(month).add(monthSum);
 *                                   }
 *                               };
 *                           };
 *                       }
 *
 *               }
 *           }
 *          }
 *                   catch (SQLException ex) {
 *                       Logger.getLogger(weeklyReportController.class.getName()).log(Level.SEVERE, null, ex);
 *                   }
 *           NumberFormat formatter = new DecimalFormat("#,##0.00");
 *
 *        for(Map.Entry<String, Map<String, ArrayList<Float>>>Items : forEntry.entrySet()){
 *            String jan1 = "0.00", feb1 = "0.00", apr1 = "0.00", mai1 = "0.00", jun1 = "0.00", jul1 = "0.00", aug1 = "0.00", sep1 = "0.00",
 *                    oct1 = "0.00", mar1 = "0.00", nov1 = "0.00", dec1 = "0.00", totalAmnt = "0.00";
 *            float jan = 0, feb = 0, apr = 0, mai = 0, jun = 0, jul = 0, aug = 0, sep = 0, oct = 0, mar = 0, nov = 0, dec = 0, total_amount;
 *            for(Map.Entry<String, ArrayList<Float>> Dates :forEntry.get(Items.getKey()).entrySet() ){
 *                String reveItem = Items.getKey();
 *                System.out.println(reveItem+ "\n"+Items.getValue().get(Dates.getKey()));
 *                if(Dates.getKey() == null ? january.getText() == null : Dates.getKey().equals(january.getText())){
 *                    jan1 = formatter.format(forEntry.get(Items.getKey()).get(january.getText()).get(0));
 *                    jan = forEntry.get(Items.getKey()).get(january.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? february.getText() == null : Dates.getKey().equals(february.getText())){
 *                    feb1 = formatter.format(forEntry.get(Items.getKey()).get(february.getText()).get(0));
 *                    feb = forEntry.get(Items.getKey()).get(february.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? march.getText() == null : Dates.getKey().equals(march.getText())){
 *                    mar1 = formatter.format(forEntry.get(Items.getKey()).get(march.getText()).get(0));
 *                    mar = forEntry.get(Items.getKey()).get(march.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? april.getText() == null : Dates.getKey().equals(april.getText())){
 *                    apr1 = formatter.format(forEntry.get(Items.getKey()).get(april.getText()).get(0));
 *                    apr = forEntry.get(Items.getKey()).get(april.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? may.getText() == null : Dates.getKey().equals(may.getText())){
 *                    mai1 = formatter.format(forEntry.get(Items.getKey()).get(may.getText()).get(0));
 *                    mai = forEntry.get(Items.getKey()).get(may.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? june.getText() == null : Dates.getKey().equals(june.getText())){
 *                    jun1 = formatter.format(forEntry.get(Items.getKey()).get(june.getText()).get(0));
 *                    jun = forEntry.get(Items.getKey()).get(june.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? july.getText() == null : Dates.getKey().equals(july.getText())){
 *                    jul1 = formatter.format(forEntry.get(Items.getKey()).get(july.getText()).get(0));
 *                    jul = forEntry.get(Items.getKey()).get(july.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? august.getText() == null : Dates.getKey().equals(august.getText())){
 *                    aug1 = formatter.format(forEntry.get(Items.getKey()).get(august.getText()).get(0));
 *                    aug = forEntry.get(Items.getKey()).get(august.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? september.getText() == null : Dates.getKey().equals(september.getText())){
 *                    sep1 = formatter.format(forEntry.get(Items.getKey()).get(september.getText()).get(0));
 *                    sep = forEntry.get(Items.getKey()).get(september.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? october.getText() == null : Dates.getKey().equals(october.getText())){
 *                    oct1 = formatter.format(forEntry.get(Items.getKey()).get(october.getText()).get(0));
 *                    oct = forEntry.get(Items.getKey()).get(october.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? november.getText() == null : Dates.getKey().equals(november.getText())){
 *                    nov1 = formatter.format(forEntry.get(Items.getKey()).get(november.getText()).get(0));
 *                    nov = forEntry.get(Items.getKey()).get(november.getText()).get(0);
 *                }
 *                else if(Dates.getKey() == null ? december.getText() == null : Dates.getKey().equals(december.getText())){
 *                    dec1 = formatter.format(forEntry.get(Items.getKey()).get(december.getText()).get(0));
 *                    dec = forEntry.get(Items.getKey()).get(december.getText()).get(0);
 *                }
 *            }
 *            total_amount = jan + feb + mar + apr + mai + jun + jul + aug + oct + nov + sep + dec;
 *            totalAmnt = formatter.format(total_amount);
 *            revenueItem.setCellValueFactory(data -> data.getValue().RevenueItemProperty());
 *            january.setCellValueFactory(data -> data.getValue().JanProperty());
 *            february.setCellValueFactory(data -> data.getValue().FebProperty());
 *            march.setCellValueFactory(data -> data.getValue().MarProperty());
 *            april.setCellValueFactory(data -> data.getValue().AprProperty());
 *            may.setCellValueFactory(data -> data.getValue().MayProperty());
 *            june.setCellValueFactory(data -> data.getValue().JunProperty());
 *            july.setCellValueFactory(data -> data.getValue().JulProperty());
 *            august.setCellValueFactory(data -> data.getValue().AugProperty());
 *            september.setCellValueFactory(data -> data.getValue().SepProperty());
 *            october.setCellValueFactory(data -> data.getValue().OctProperty());
 *            november.setCellValueFactory(data -> data.getValue().NovProperty());
 *            december.setCellValueFactory(data -> data.getValue().DecProperty());
 *            totalAmount.setCellValueFactory(data -> data.getValue().Total_AmountProperty());
 *            GetItemsReport getReport = new GetItemsReport(Items.getKey(), jan1, feb1, mar1, apr1, mai1, jun1, jul1, aug1, sep1, oct1, nov1, dec1, totalAmnt);
 *            revenueItemsTable.getItems().add(getReport);
 *        }
 *
 *     }
 *
 *
 *        public Float setCentersSum(String Item, String Month, String Year) throws SQLException{
 *         float totalAmunt;
 *        stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `revenue_items`,`daily_entries` WHERE `revenue_item_ID` = `revenueItem` AND `revenue_item` = '"+Item+"' AND `revenueMonth` = '"+Month+"' AND `revenueYear` = '"+Year+"'  ");
 *        ResultSet rs = stmnt.executeQuery();
 *        ObservableList<Float> Amount = FXCollections.observableArrayList();//List to Store revenue items which have entries for the specified week
 *        while(rs.next()){//looping through the retrieved revenueItems result set
 *            Amount.add(rs.getFloat("revenueAmount"));
 *        }
 *         totalAmunt = 0;
 *         for(int i = 0; i < Amount.size(); i++){
 *             totalAmunt += Amount.get(i);
 *         }
 *         return totalAmunt;
 *     }
 */