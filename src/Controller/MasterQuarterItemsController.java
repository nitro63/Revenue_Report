/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetMstrQuarterCenters;
import Controller.Gets.GetMstrQuarterItems;

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
import javafx.scene.layout.AnchorPane;
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
public class MasterQuarterItemsController implements Initializable {

    @FXML
    private AnchorPane paneSingle;
    @FXML
    private AnchorPane paneAll;
    @FXML
    private VBox quarterlyTemplate;
    @FXML
    private JFXButton btnPrint;
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
    private TableView<GetMstrQuarterItems> quarterMastItemsTableAll;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> revenueItemAll;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> quarterAll;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> month1All;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> month2All;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> month3All;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> month4All;
    @FXML
    private TableColumn<GetMstrQuarterItems, String> totalAmountAll;
    @FXML
    private Label lblMonth1;
    @FXML
    private Label lblMonth2;
    @FXML
    private Label lblMonth3;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private Label lblMonth1All;
    @FXML
    private Label lblMonth2All;
    @FXML
    private Label lblMonth4All;
    @FXML
    private Label lblTotalAmountAll;
    @FXML
    private Label lblMonth3All;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblQtrWarn;
    @FXML
    private Label lblQuarter;
    @FXML
    private Label lblYear;

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
        } catch (SQLException | ClassNotFoundException ex) {
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
            rowQuater.add(rs.getString("revenueQuarter"));
        }
        rowQuater.add("All Quarters");
        cmbMstItemsQuarter.getItems().clear();
        cmbMstItemsQuarter.getItems().addAll(rowQuater);
        cmbMstItemsQuarter.setVisibleRowCount(5);
    }
    
    private void getMonths() throws SQLException{
        boolean allQuarters;
        boolean singleQuarters;
        if (!cmbMstItemsQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
            allQuarters = false;
            singleQuarters = true;
            paneAll.setVisible(false);
            paneSingle.setVisible(true);
        stmnt = con.prepareStatement(" SELECT `revenueMonth` FROM `daily_entries` WHERE   `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbMstItemsQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueMonth`");
        ResultSet Rs = stmnt.executeQuery();
        rowMonths.clear();
        while(Rs.next()){
            rowMonths.add(Rs.getString("revenueMonth"));
        }
        month1.setText("MONTH");
        month2.setText("MONTH");
        month3.setText("MONTH");
        String rowSize = cmbMstItemsQuarter.getSelectionModel().getSelectedItem();
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
            paneAll.setVisible(true);
            paneSingle.setVisible(false);
            rowMonths.addAll("1", "2", "3","4");
        }
    }
    
    
    private void changeNames() {
        lblQuarter.setText(cmbMstItemsQuarter.getSelectionModel().getSelectedItem());
        lblYear.setText(cmMstItemsYear.getSelectionModel().getSelectedItem());
    }

    private void setItems() throws SQLException{
        Map<String, ArrayList<Float>> monthAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
        Map<String, Map<String, ArrayList<Float>>> forEntry = new HashMap<>();//HashMap to store entries for tableview
        PreparedStatement stmnt_itemCategories;
        ResultSet rs, rs_itemsCategories;
        quarterMastItemsTable.setFixedCellSize(30.0);
        quarterMastItemsTableAll.setFixedCellSize(30.0);
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        String mon1 = "0.00", mon2 = "0.00", mon3 = "0.00", mon4 = "0.00", totalAmnt = "0.00", totmon1 = "0.00", totmon2 = "0.00", totmon3 = "0.00", totmon4 = "0.00", summation = "0.00";
        float Mon1 = 0, Mon2 = 0, Mon3 = 0, Mon4 = 0, total_amount, totMon1 = 0, totMon2 = 0, totMon3 = 0, totMon4 = 0, totQuarterSum = 0;
        GetMstrQuarterItems getReport;
        if (!cmbMstItemsQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
            stmnt = con.prepareStatement(" SELECT `revenue_item`, `item_category`, `revenueAmount`, `revenueMonth`  FROM `daily_entries`,`revenue_items` WHERE `revenue_item_ID` = `revenueItem` AND `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbMstItemsQuarter.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_item` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_item`, `item_category` FROM `daily_entries`,`revenue_items` WHERE `revenue_item_ID` = `revenueItem` AND `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbMstItemsQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");

            rs = stmnt.executeQuery();
            rs_itemsCategories = stmnt_itemCategories.executeQuery();
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
            revenueItem.setCellValueFactory(data -> data.getValue().revenueItemProperty());
            month1.setCellValueFactory(data -> data.getValue().firstMonthProperty());
            month2.setCellValueFactory(data -> data.getValue().secondMonthProperty());
            month3.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
            totalAmount.setCellValueFactory(data -> data.getValue().totalAmountProperty());
            revenueItem.setSortable(false); month1.setSortable(false); month2.setSortable(false);
            month3.setSortable(false); totalAmount.setSortable(false);
            for (String category : rowItems){
                revenueItem.setStyle("-fx-alignment: CENTER; -fx-text-fill: #000;");
                getReport = new GetMstrQuarterItems("", "", "", category, "");
                quarterMastItemsTable.getItems().add(getReport);
                String submon1 = "0.00", submon2 = "0.00", submon3 = "0.00", submon4 = "0.00", subtotalAmnt = "0.00";
                float subMon1 = 0, subMon2 = 0, subMon3 = 0, subMon4 = 0, subtotal_amount;
                for (String item : categoriesItem.get(category)) {
                    Map<String, Map<String, Float>> itemQuarterSum = new HashMap<>();
                    Map<String, Float> quarterSum = new HashMap<>();
                    quarterSum.put(month1.getText(), Mon1); quarterSum.put(month2.getText(), Mon2); quarterSum.put(month3.getText(), Mon3);
                    itemQuarterSum.put(item, quarterSum);
                    boolean resultSetState = true;
                    while (resultSetState){
                        rs.next();
                        if (item.equals(rs.getString("revenue_item"))){
                            float amot= itemQuarterSum.get(item).get(rs.getString("revenueMonth"));
                            amot += rs.getFloat("revenueAmount");
                            itemQuarterSum.get(item).put(rs.getString("revenueMonth"), amot);
                        }
                        if (rs.isLast()){
                            resultSetState = false;
                        }
                    }
                    if (rs.isLast()){
                        rs.beforeFirst();
                    }
                    subMon1 += itemQuarterSum.get(item).get(month1.getText()); subMon2 += itemQuarterSum.get(item).get(month2.getText()); subMon3 += itemQuarterSum.get(item).get(month3.getText());
                    mon1 = formatter.format( itemQuarterSum.get(item).get(month1.getText())); mon2 = formatter.format(itemQuarterSum.get(item).get(month2.getText())); mon3 = formatter.format(itemQuarterSum.get(item).get(month3.getText()));
                    total_amount = itemQuarterSum.get(item).get(month1.getText()) + itemQuarterSum.get(item).get(month2.getText()) + itemQuarterSum.get(item).get(month3.getText()); totQuarterSum += total_amount;
                    totalAmnt = formatter.format(total_amount);
                    totMon1 += itemQuarterSum.get(item).get(month1.getText()); totMon2 += itemQuarterSum.get(item).get(month2.getText()); totMon3 += itemQuarterSum.get(item).get(month3.getText());
                    getReport = new GetMstrQuarterItems(mon1, mon2, mon3, item, totalAmnt);
                    quarterMastItemsTable.getItems().add(getReport); Mon1 = 0; Mon2 = 0; Mon3 = 0; Mon4 = 0;
                }
                subtotal_amount = subMon1 + subMon2 + subMon3;
                submon1 = formatter.format(subMon1); submon2 = formatter.format(subMon2); submon3 = formatter.format(subMon3); subtotalAmnt = formatter.format(subtotal_amount);
                getReport = new GetMstrQuarterItems(submon1, submon2, submon3, "SUB-TOTAL", subtotalAmnt);
                quarterMastItemsTable.getItems().add(getReport);
            }
            totmon1 = formatter.format(totMon1); totmon2 = formatter.format(totMon2); totmon3 = formatter.format(totMon3); summation = formatter.format(totQuarterSum);
            getReport = new GetMstrQuarterItems(totmon1, totmon2, totmon3, "TOTAL", summation);
            quarterMastItemsTable.getItems().add(getReport);
            lblMonth1.setText(totmon1); lblMonth2.setText(totmon2); lblMonth3.setText(totmon3); lblTotalAmount.setText(summation);
        }
        else {
            stmnt = con.prepareStatement(" SELECT `revenue_item`, `item_category`, `revenueAmount`, `revenueQuarter` FROM `daily_entries`,`revenue_items` WHERE `revenue_item_ID` = `revenueItem` AND `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_item` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_item`, `item_category` FROM `daily_entries`,`revenue_items` WHERE `revenue_item_ID` = `revenueItem` AND `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"'GROUP BY `revenue_item`");

            rs = stmnt.executeQuery();
            rs_itemsCategories = stmnt_itemCategories.executeQuery();
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
            revenueItemAll.setCellValueFactory(data -> data.getValue().revenueItemProperty());
            month1All.setCellValueFactory(data -> data.getValue().firstMonthProperty());
            month2All.setCellValueFactory(data -> data.getValue().secondMonthProperty());
            month3All.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
            month4All.setCellValueFactory(data -> data.getValue().fourthMonthProperty());
            totalAmountAll.setCellValueFactory(data -> data.getValue().totalAmountProperty());
            revenueItemAll.setSortable(false); month1All.setSortable(false); month2All.setSortable(false);
            month3All.setSortable(false); month4All.setSortable(false); totalAmountAll.setSortable(false);
            for (String category : rowItems){
                revenueItemAll.setStyle("-fx-alignment: CENTER; -fx-text-fill: #000;");
                getReport = new GetMstrQuarterItems("", "", "", "", category, "");
                quarterMastItemsTableAll.getItems().add(getReport);
                String submon1 = "0.00", submon2 = "0.00", submon3 = "0.00", submon4 = "0.00", subtotalAmnt = "0.00";
                float subMon1 = 0, subMon2 = 0, subMon3 = 0, subMon4 = 0, subtotal_amount;
                for (String item : categoriesItem.get(category)) {
                    Map<String, Map<String, Float>> itemQuarterSum = new HashMap<>();
                    Map<String, Float> quarterSum = new HashMap<>();
                    quarterSum.put("1", Mon1); quarterSum.put("2", Mon2); quarterSum.put("3", Mon3); quarterSum.put("4", Mon4);
                    itemQuarterSum.put(item, quarterSum);
                    boolean resultSetState = true;
                    while (resultSetState){
                        rs.next();
                        if (item.equals(rs.getString("revenue_item"))){
                            float amot= itemQuarterSum.get(item).get(rs.getString("revenueQuarter"));
                            amot += rs.getFloat("revenueAmount");
                            itemQuarterSum.get(item).put(rs.getString("revenueQuarter"), amot);
                        }
                        if (rs.isLast()){
                            resultSetState = false;
                        }
                    }
                    if (rs.isLast()){
                        rs.beforeFirst();
                    }
                    subMon1 += itemQuarterSum.get(item).get("1"); subMon2 += itemQuarterSum.get(item).get("2"); subMon3 += itemQuarterSum.get(item).get("3"); subMon4 += itemQuarterSum.get(item).get("4");
                    mon1 = formatter.format( itemQuarterSum.get(item).get("1")); mon2 = formatter.format(itemQuarterSum.get(item).get("2")); mon3 = formatter.format(itemQuarterSum.get(item).get("3")); mon4 = formatter.format(itemQuarterSum.get(item).get("4"));
                    total_amount = itemQuarterSum.get(item).get("1") + itemQuarterSum.get(item).get("2") + itemQuarterSum.get(item).get("3")  + itemQuarterSum.get(item).get("4"); totQuarterSum += total_amount;
                    totalAmnt = formatter.format(total_amount);
                    totMon1 += itemQuarterSum.get(item).get("1"); totMon2 += itemQuarterSum.get(item).get("2"); totMon3 += itemQuarterSum.get(item).get("3"); totMon4 += itemQuarterSum.get(item).get("4");
                    getReport = new GetMstrQuarterItems(mon1, mon2, mon3, mon4, item, totalAmnt);
                    quarterMastItemsTableAll.getItems().add(getReport); Mon1 = 0; Mon2 = 0; Mon3 = 0; Mon4 = 0;
                }
                subtotal_amount = subMon1 + subMon2 + subMon3 + subMon4;
                submon1 = formatter.format(subMon1); submon2 = formatter.format(subMon2); submon3 = formatter.format(subMon3); submon4 = formatter.format(subMon4); subtotalAmnt = formatter.format(subtotal_amount);
                getReport = new GetMstrQuarterItems(submon1, submon2, submon3, submon4, "SUB-TOTAL", subtotalAmnt);
                quarterMastItemsTableAll.getItems().add(getReport);
            }
            totmon1 = formatter.format(totMon1); totmon2 = formatter.format(totMon2); totmon3 = formatter.format(totMon3); totmon4 = formatter.format(totMon4); summation = formatter.format(totQuarterSum);
            getReport = new GetMstrQuarterItems(totmon1, totmon2, totmon3, totmon4, "TOTAL", summation);
            quarterMastItemsTableAll.getItems().add(getReport);
            lblMonth1All.setText(totmon1); lblMonth2All.setText(totmon2); lblMonth3All.setText(totmon3); lblMonth4All.setText(totmon4); lblTotalAmountAll.setText(summation);
        }
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

    @FXML
    void printReport(ActionEvent event) throws JRException, FileNotFoundException {
        if (!quarterMastItemsTable.getItems().isEmpty()){
            Date date = new Date();
            List<GetMstrQuarterItems> items = new ArrayList<>();
            for (int j = 0; j < quarterMastItemsTable.getItems().size(); j++) {
                GetMstrQuarterItems getdata;
                getdata = quarterMastItemsTable.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmMstItemsYear.getSelectionModel().getSelectedItem(),
                    first = month1.getText(),
                    second = month2.getText(),
                    third = month3.getText();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("FirstMonth", first);
            parameters.put("year", year); parameters.put("SecondMonth", second);
            parameters.put("timeStamp", date); parameters.put("ThirdMonth", third);

            //read jrxml file and creating jasperdesign object
            InputStream input = this.getClass().getResourceAsStream("/Assets/masterQuarterItemsPotrait.jrxml");

            JasperDesign jasperDesign = JRXmlLoader.load(input);

            /*compiling jrxml with help of JasperReport class*/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            /* Using jasperReport object to generate PDF */
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            /*call jasper engine to display report in jasperviewer window*/
            JasperViewer.viewReport(jasperPrint, false);
        } else if (!quarterMastItemsTableAll.getItems().isEmpty()){
            Date date = new Date();
            List<GetMstrQuarterItems> items = new ArrayList<>();
            for (int j = 0; j < quarterMastItemsTableAll.getItems().size(); j++) {
                GetMstrQuarterItems getdata;
                getdata = quarterMastItemsTableAll.getItems().get(j);
                items.add(getdata);
            }
            URL url = this.getClass().getResource("/Assets/kmalogo.png");

            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
            String year = cmMstItemsYear.getSelectionModel().getSelectedItem(),
                    first = month1All.getText(),
                    second = month2All.getText(),
                    third = month3All.getText(),
                    fourth = month4All.getText();

            /* Map to hold Jasper report Parameters */
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("FirstMonth", first);
            parameters.put("year", year); parameters.put("SecondMonth", second);
            parameters.put("timeStamp", date); parameters.put("ThirdMonth", third); parameters.put("FourthMonth", fourth);

            //read jrxml file and creating jasperdesign object
            InputStream input = this.getClass().getResourceAsStream("/Assets/masterAllQuarterItemsPotrait.jrxml");

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


/**
 *

 private void setItems() throws SQLException{
 if (!cmbMstItemsQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
 stmnt = con.prepareStatement(" SELECT `revenue_item` FROM `daily_entries`,`revenue_items` WHERE   `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbMstItemsQuarter.getSelectionModel().getSelectedItem()+"' AND `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` GROUP BY `revenue_item`");
 ResultSet rs = stmnt.executeQuery();
 rowItems.clear();
 while(rs.next()){
 rowItems.add(rs.getString("revenue_item"));
 }
 } else {
 stmnt = con.prepareStatement(" SELECT `revenue_item` FROM `daily_entries`,`revenue_items` WHERE   `revenueYear` = '"+cmMstItemsYear.getSelectionModel().getSelectedItem()+"' AND `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` GROUP BY `revenue_item`");
 ResultSet rs = stmnt.executeQuery();
 rowItems.clear();
 while(rs.next()){
 rowItems.add(rs.getString("revenue_item"));
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
 if (!cmbMstItemsQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
 for(String month : rowMonths) {
 for(String Item : rowItems) {
 float monthSum;
 monthSum = setMonthSum(Item, month, cmMstItemsYear.getSelectionModel().getSelectedItem(), cmbMstItemsQuarter.getSelectionModel().getSelectedItem());
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
 }else if (allQuarters){
 for(String month : rowMonths) {
 for(String Item : rowItems) {
 float monthSum;
 monthSum = setMonthSum(Item, cmMstItemsYear.getSelectionModel().getSelectedItem(), month);
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
 if (!cmbMstItemsQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
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
 }else if (allQuarters){
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
 if (singleQuarters){
 total_amount = Mon3 + Mon2 + Mon1;
 totalAmnt = formatter.format(total_amount);
 revenueItem.setCellValueFactory(data -> data.getValue().revenueItemProperty());
 month1.setCellValueFactory(data -> data.getValue().firstMonthProperty());
 month2.setCellValueFactory(data -> data.getValue().secondMonthProperty());
 month3.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
 totalAmount.setCellValueFactory(data -> data.getValue().totalAmountProperty());
 getReport = new GetMstrQuarterItems( mon1, mon2, mon3, Items.getKey(), totalAmnt);
 quarterMastItemsTable.getItems().add(getReport);
 }else if (allQuarters){
 total_amount = Mon3 + Mon2 + Mon1 + Mon4;
 totalAmnt = formatter.format(total_amount);
 revenueItemAll.setCellValueFactory(data -> data.getValue().revenueItemProperty());
 month1All.setCellValueFactory(data -> data.getValue().firstMonthProperty());
 month2All.setCellValueFactory(data -> data.getValue().secondMonthProperty());
 month3All.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
 month4All.setCellValueFactory(data -> data.getValue().fourthMonthProperty());
 totalAmountAll.setCellValueFactory(data -> data.getValue().totalAmountProperty());
 getReport = new GetMstrQuarterItems( mon1, mon2, mon3, mon4, Items.getKey(), totalAmnt);
 quarterMastItemsTableAll.getItems().add(getReport);
 }
 }
 }

 public Float setMonthSum(String item, String Year, String Quarter) throws SQLException{
 float totalAmunt;
 stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries`,`revenue_items` WHERE  `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' AND `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"'");
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

 public Float setMonthSum(String item, String Month, String Year, String Quarter) throws SQLException{
 float totalAmunt;
 stmnt = con.prepareStatement(" SELECT `revenueAmount`   FROM `daily_entries`,`revenue_items`WHERE `revenueMonth` = '"+Month+"' AND `revenueYear` = '"+Year+"' AND `revenueQuarter` = '"+Quarter+"' AND `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_item` = '"+item+"'");
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
 */
