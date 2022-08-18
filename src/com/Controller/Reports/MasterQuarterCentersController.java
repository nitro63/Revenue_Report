/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller.Reports;

import com.Models.GetMstrQuarterCenters;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import com.revenue_report.DBConnection;

/**
 * FXML com.Controller class
 *
 * @author HP
 */
public class MasterQuarterCentersController implements Initializable {

    @FXML
    private AnchorPane paneSingle;
    @FXML
    private AnchorPane paneAll;
    @FXML
    private VBox quarterlyTemplate;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private ComboBox<String> cmMstCentersYear;
    @FXML
    private ComboBox<String> cmbMstCentersQuarter;
    @FXML
    private JFXButton btnShowReport;
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
         cmMstCentersYear.getItems().clear();
         cmMstCentersYear.setItems(rowYear);
         cmMstCentersYear.setVisibleRowCount(5);
    }
    
    private void getQuarter() throws SQLException{
        stmnt = con.prepareStatement(" SELECT QUARTER(revenueDate) AS `revenueQuarter` FROM `daily_entries` WHERE  YEAR(revenueDate) = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueQuarter`");
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
            paneAll.setVisible(false);
            paneSingle.setVisible(true);
        stmnt = con.prepareStatement(" SELECT MONTH(revenueDate) AS `revenueMonth` FROM `daily_entries` WHERE   YEAR(revenueDate) = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"' AND QUARTER(revenueDate) = '"+cmbMstCentersQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenueMonth`");
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
                month1.setText("January".toUpperCase());
                month2.setText("February".toUpperCase());
                month3.setText("March".toUpperCase());
                break;
            case "2":
                month1.setText("April".toUpperCase());
                month2.setText("May".toUpperCase());
                month3.setText("June".toUpperCase());
                break;
            case "3":
                month1.setText("July".toUpperCase());
                month2.setText("August".toUpperCase());
                month3.setText("September".toUpperCase());
                break;
            case "4":
                month1.setText("October".toUpperCase());
                month2.setText("November".toUpperCase());
                month3.setText("December".toUpperCase());
                break;
        }
    }else {
            allQuarters = true;
            singleQuarters = false;
            month1All.setText(month1All.getText().toUpperCase());
            month2All.setText(month2All.getText().toUpperCase());
            month3All.setText(month3All.getText().toUpperCase());
            month4All.setText(month4All.getText().toUpperCase());
            paneAll.setVisible(true);
            paneSingle.setVisible(false);
        rowMonths.addAll("1", "2", "3","4");
    }
    }
    
    
    private void changeNames() {
        lblQuarter.setText(cmbMstCentersQuarter.getSelectionModel().getSelectedItem());
        lblYear.setText(cmMstCentersYear.getSelectionModel().getSelectedItem());
    }

    private void setItems() throws SQLException{
        Map<String, ArrayList<Double>> monthAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
        Map<String, Map<String, ArrayList<Double>>> forEntry = new HashMap<>();//HashMap to store entries for tableview
        PreparedStatement stmnt_itemCategories;
        ResultSet rs, rs_itemsCategories;
        quarterMastCentersTable.setFixedCellSize(30.0);
        quarterMastCentersTableAll.setFixedCellSize(30.0);
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        String mon1 = "0.00", mon2 = "0.00", mon3 = "0.00", mon4 = "0.00", totalAmnt = "0.00", totmon1 = "0.00", totmon2 = "0.00", totmon3 = "0.00", totmon4 = "0.00", summation = "0.00";
        double Mon1 = 0, Mon2 = 0, Mon3 = 0, Mon4 = 0, total_amount, totMon1 = 0, totMon2 = 0, totMon3 = 0, totMon4 = 0, totQuarterSum = 0;
        if (!cmbMstCentersQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
                stmnt = con.prepareStatement(" SELECT `revenue_center`, `revenue_category`, `revenueAmount`, MONTH(revenueDate) AS `revenueMonth`  FROM `daily_entries`,`revenue_centers` WHERE `CenterID` = `daily_revCenter` AND YEAR(revenueDate) = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"' AND QUARTER(revenueDate) = '"+cmbMstCentersQuarter.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_center` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_center`, `revenue_category` FROM `daily_entries`,`revenue_centers` WHERE `CenterID` = `daily_revCenter` AND YEAR(revenueDate) = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"' AND QUARTER(revenueDate) = '"+cmbMstCentersQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_center`");

            rs = stmnt.executeQuery();
            rs_itemsCategories = stmnt_itemCategories.executeQuery();
            rowCenters.clear();
            Map<String, ArrayList<String>> categoriesItem = new HashMap<>();
            categoriesItem.put("", new ArrayList<>());
            while(rs_itemsCategories.next()){
                if (!rowCenters.contains(rs_itemsCategories.getString("revenue_category"))){
                    rowCenters.add(rs_itemsCategories.getString("revenue_category"));
                }
                if (!categoriesItem.containsKey(rs_itemsCategories.getString("revenue_category"))){
                    categoriesItem.put(rs_itemsCategories.getString("revenue_category"), new ArrayList<>());
                    categoriesItem.get(rs_itemsCategories.getString("revenue_category")).add(rs_itemsCategories.getString("revenue_center"));
                }else if (categoriesItem.containsKey(rs_itemsCategories.getString("revenue_category")) && !categoriesItem.get(rs_itemsCategories.getString("revenue_category")).contains(rs_itemsCategories.getString("revenue_center"))){
                    categoriesItem.get(rs_itemsCategories.getString("revenue_category")).add(rs_itemsCategories.getString("revenue_center"));
                }
            }
            revenueCenter.setCellValueFactory(data -> data.getValue().revenueCenterProperty());
            month1.setCellValueFactory(data -> data.getValue().firstMonthProperty());
            month2.setCellValueFactory(data -> data.getValue().secondMonthProperty());
            month3.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
            totalAmount.setCellValueFactory(data -> data.getValue().totalAmountProperty());
            revenueCenter.setSortable(false); month1.setSortable(false); month2.setSortable(false);
            month3.setSortable(false); totalAmount.setSortable(false);
            for (String category : rowCenters){
                revenueCenter.setStyle("-fx-alignment: CENTER; -fx-text-fill: #000;");
                getReport = new GetMstrQuarterCenters("", "", "", category, "");
                quarterMastCentersTable.getItems().add(getReport);
                String submon1 = "0.00", submon2 = "0.00", submon3 = "0.00", submon4 = "0.00", subtotalAmnt = "0.00";
                double subMon1 = 0, subMon2 = 0, subMon3 = 0, subMon4 = 0, subtotal_amount;
                for (String item : categoriesItem.get(category)) {
                    Map<String, Map<String, Double>> itemQuarterSum = new HashMap<>();
                    Map<String, Double> quarterSum = new HashMap<>();
                    quarterSum.put(month1.getText(), Mon1); quarterSum.put(month2.getText(), Mon2); quarterSum.put(month3.getText(), Mon3);
                    itemQuarterSum.put(item, quarterSum);
                    boolean resultSetState = true;
                    while (resultSetState){
                        rs.next();
                        if (item.equals(rs.getString("revenue_center"))){
                            double amot= itemQuarterSum.get(item).get(Months.get(rs.getInt("revenueMonth")).toString());
                            amot += rs.getDouble("revenueAmount");
                            itemQuarterSum.get(item).put(Months.get(rs.getInt("revenueMonth")).toString(), amot);
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
                    getReport = new GetMstrQuarterCenters(mon1, mon2, mon3, item, totalAmnt);
                    quarterMastCentersTable.getItems().add(getReport); Mon1 = 0; Mon2 = 0; Mon3 = 0; Mon4 = 0;
                }
                subtotal_amount = subMon1 + subMon2 + subMon3;
                submon1 = formatter.format(subMon1); submon2 = formatter.format(subMon2); submon3 = formatter.format(subMon3); subtotalAmnt = formatter.format(subtotal_amount);
                getReport = new GetMstrQuarterCenters(submon1, submon2, submon3, "SUB-TOTAL", subtotalAmnt);
                quarterMastCentersTable.getItems().add(getReport);
            }
            totmon1 = formatter.format(totMon1); totmon2 = formatter.format(totMon2); totmon3 = formatter.format(totMon3); summation = formatter.format(totQuarterSum);
            getReport = new GetMstrQuarterCenters(totmon1, totmon2, totmon3, "TOTAL", summation);
            quarterMastCentersTable.getItems().add(getReport);
            lblMonth1.setText(totmon1); lblMonth2.setText(totmon2); lblMonth3.setText(totmon3); lblTotalAmount.setText(summation);
        }
        else {
                stmnt = con.prepareStatement(" SELECT `revenue_center`, `revenue_category`, `revenueAmount`, QUARTER(revenueDate) AS `revenueQuarter` FROM `daily_entries`,`revenue_centers` WHERE `CenterID` = `daily_revCenter` AND YEAR(revenueDate) = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_center` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_center`, `revenue_category` FROM `daily_entries`,`revenue_centers` WHERE `CenterID` = `daily_revCenter` AND YEAR(revenueDate) = '"+cmMstCentersYear.getSelectionModel().getSelectedItem()+"'GROUP BY `revenue_center`");

            rs = stmnt.executeQuery();
            rs_itemsCategories = stmnt_itemCategories.executeQuery();
            rowCenters.clear();
            Map<String, ArrayList<String>> categoriesItem = new HashMap<>();
            categoriesItem.put("", new ArrayList<>());
            while(rs_itemsCategories.next()){
                if (!rowCenters.contains(rs_itemsCategories.getString("revenue_category"))){
                    rowCenters.add(rs_itemsCategories.getString("revenue_category"));
                }
                if (!categoriesItem.containsKey(rs_itemsCategories.getString("revenue_category"))){
                    categoriesItem.put(rs_itemsCategories.getString("revenue_category"), new ArrayList<>());
                    categoriesItem.get(rs_itemsCategories.getString("revenue_category")).add(rs_itemsCategories.getString("revenue_center"));
                }else if (categoriesItem.containsKey(rs_itemsCategories.getString("revenue_category")) && !categoriesItem.get(rs_itemsCategories.getString("revenue_category")).contains(rs_itemsCategories.getString("revenue_center"))){
                    categoriesItem.get(rs_itemsCategories.getString("revenue_category")).add(rs_itemsCategories.getString("revenue_center"));
                }
            }
            revenueCenterAll.setCellValueFactory(data -> data.getValue().revenueCenterProperty());
            month1All.setCellValueFactory(data -> data.getValue().firstMonthProperty());
            month2All.setCellValueFactory(data -> data.getValue().secondMonthProperty());
            month3All.setCellValueFactory(data -> data.getValue().thirdMonthProperty());
            month4All.setCellValueFactory(data -> data.getValue().fourthMonthProperty());
            totalAmountAll.setCellValueFactory(data -> data.getValue().totalAmountProperty());
            revenueCenterAll.setSortable(false); month1All.setSortable(false); month2All.setSortable(false);
            month3All.setSortable(false); month4All.setSortable(false); totalAmountAll.setSortable(false);
            for (String category : rowCenters){
                revenueCenterAll.setStyle("-fx-alignment: CENTER; -fx-text-fill: #000;");
                getReport = new GetMstrQuarterCenters("", "", "", "", category, "");
                quarterMastCentersTableAll.getItems().add(getReport);
                String submon1 = "0.00", submon2 = "0.00", submon3 = "0.00", submon4 = "0.00", subtotalAmnt = "0.00";
                double subMon1 = 0, subMon2 = 0, subMon3 = 0, subMon4 = 0, subtotal_amount;
                for (String item : categoriesItem.get(category)) {
                    Map<String, Map<String, Double>> itemQuarterSum = new HashMap<>();
                    Map<String, Double> quarterSum = new HashMap<>();
                    quarterSum.put("1", Mon1); quarterSum.put("2", Mon2); quarterSum.put("3", Mon3); quarterSum.put("4", Mon4);
                    itemQuarterSum.put(item, quarterSum);
                    boolean resultSetState = true;
                    while (resultSetState){
                        rs.next();
                        if (item.equals(rs.getString("revenue_center"))){
                            double amot= itemQuarterSum.get(item).get(rs.getString("revenueQuarter"));
                            amot += rs.getDouble("revenueAmount");
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
                    getReport = new GetMstrQuarterCenters(mon1, mon2, mon3, mon4, item, totalAmnt);
                    quarterMastCentersTableAll.getItems().add(getReport); Mon1 = 0; Mon2 = 0; Mon3 = 0; Mon4 = 0;
                }
                subtotal_amount = subMon1 + subMon2 + subMon3 + subMon4;
                submon1 = formatter.format(subMon1); submon2 = formatter.format(subMon2); submon3 = formatter.format(subMon3); submon4 = formatter.format(subMon4); subtotalAmnt = formatter.format(subtotal_amount);
                getReport = new GetMstrQuarterCenters(submon1, submon2, submon3, submon4, "SUB-TOTAL", subtotalAmnt);
                quarterMastCentersTableAll.getItems().add(getReport);
            }
            totmon1 = formatter.format(totMon1); totmon2 = formatter.format(totMon2); totmon3 = formatter.format(totMon3); totmon4 = formatter.format(totMon4); summation = formatter.format(totQuarterSum);
            getReport = new GetMstrQuarterCenters(totmon1, totmon2, totmon3, totmon4, "TOTAL", summation);
            quarterMastCentersTableAll.getItems().add(getReport);
            lblMonth1All.setText(totmon1); lblMonth2All.setText(totmon2); lblMonth3All.setText(totmon3); lblMonth4All.setText(totmon4); lblTotalAmountAll.setText(summation);
        }
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
        if (!quarterMastCentersTable.getItems().isEmpty()){
            Date date = new Date();
            List<GetMstrQuarterCenters> items = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>();
            URL url = this.getClass().getResource("/com/Assets/kmalogo.png");
            for (int j = 0; j < quarterMastCentersTable.getItems().size(); j++) {
                GetMstrQuarterCenters getdata;
                getdata = quarterMastCentersTable.getItems().get(j);
                items.add(getdata);
            }
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);

            String year = cmMstCentersYear.getSelectionModel().getSelectedItem(),
                    first = month1.getText(),
                    second = month2.getText(),
                    third = month3.getText();

            //* Map to hold Jasper report Parameters *//*
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("FirstMonth", first);
            parameters.put("year", year); parameters.put("SecondMonth", second);
            parameters.put("timeStamp", date); parameters.put("ThirdMonth", third);


            //read jrxml file and creating jasperdesign object
            InputStream input =  this.getClass().getResourceAsStream("/com/Assets/masterQuarterCentersPotrait.jrxml");

            JasperDesign jasperDesign = JRXmlLoader.load(input);

            //*compiling jrxml with help of JasperReport class*//*
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            //* Using jasperReport object to generate PDF *//*
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            //*call jasper engine to display report in jasperviewer window*//*
            JasperViewer.viewReport(jasperPrint, false);
        }else if (!quarterMastCentersTableAll.getItems().isEmpty()){
            Date date = new Date();
            List<GetMstrQuarterCenters> items = new ArrayList<>();
            Map<String, Object> parameters = new HashMap<>();
            URL url = this.getClass().getResource("/com/Assets/kmalogo.png");
            for (int j = 0; j < quarterMastCentersTableAll.getItems().size(); j++) {
                GetMstrQuarterCenters getdata/* = new GetMstrQuarterCenters()*/;
                getdata = quarterMastCentersTableAll.getItems().get(j);
                items.add(getdata);
            }
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);

            String year = cmMstCentersYear.getSelectionModel().getSelectedItem(),
                    first = month1All.getText(),
                    second = month2All.getText(),
                    third = month3All.getText(),
                    fourth = month4All.getText();

            /* Map to hold Jasper report Parameters */
            parameters.put("CollectionBean", itemsJRBean);
            parameters.put("logo", url); parameters.put("FirstMonth", first);
            parameters.put("year", year); parameters.put("SecondMonth", second);
            parameters.put("timeStamp", date); parameters.put("ThirdMonth", third); parameters.put("FourthMonth", fourth);


            //read jrxml file and creating jasperdesign object
            InputStream input = this.getClass().getResourceAsStream("/com/Assets/masterAllQuarterCentersPotrait.jrxml");

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
 */
