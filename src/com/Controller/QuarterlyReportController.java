/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

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
import com.Controller.Gets.GetQuarterReport;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * FXML com.Controller class
 *
 * @author NiTrO
 */
public class QuarterlyReportController implements Initializable {


    @FXML
    private AnchorPane paneSingle;
    @FXML
    private AnchorPane paneAll;
    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private JFXButton btnPrint;
    @FXML
    private ComboBox<String> cmbReportYear;
    @FXML
    private ComboBox<String> cmbReportQuarter;
    @FXML
    private JFXButton btnShowReport;
    @FXML
    private Label lblCenterWarn;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblQuarterWarn;
    @FXML
    private TableView<GetQuarterReport> quarterTable;
    @FXML
    private VBox quarterlyTemplate;
    @FXML
    private TableColumn<GetQuarterReport, String> colType;
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
    private TableColumn<GetQuarterReport, String> colTypeAll;
    @FXML
    private TableColumn<GetQuarterReport, String> totalAmountAll;
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
    Logger log = Logger.getLogger(QuarterlyReportController.class.getName());
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
        cmbReportCent.setOnMouseClicked(e -> lblCenterWarn.setVisible(false));
        cmbReportYear.setOnMouseClicked(e -> lblYearWarn.setVisible(false));
        cmbReportQuarter.setOnMouseClicked(e -> lblQuarterWarn.setVisible(false));
        try {
            getRevCenters();
        } catch (SQLException | ClassNotFoundException ex) {
            log.error(ex.getMessage(), ex);
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
            paneAll.setVisible(false);
            paneSingle.setVisible(true);
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
            singleQuarter = false;
            month1All.setText(month1All.getText().toUpperCase());
            month2All.setText(month2All.getText().toUpperCase());
            month3All.setText(month3All.getText().toUpperCase());
            month4All.setText(month4All.getText().toUpperCase());
            paneAll.setVisible(true);
            paneSingle.setVisible(false);
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
        PreparedStatement stmnt_itemCategories;
        ResultSet rs, rs_itemsCategories;
        quarterTable.setFixedCellSize(30.0);
        quarterTableAll.setFixedCellSize(30.0);
        boolean Category = false;
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        String mon1 = "0.00", mon2 = "0.00", mon3 = "0.00", mon4 = "0.00", totalAmnt = "0.00", totmon1 = "0.00", totmon2 = "0.00", totmon3 = "0.00", totmon4 = "0.00", summation = "0.00";
        float Mon1 = 0, Mon2 = 0, Mon3 = 0, Mon4 = 0, total_amount, totMon1 = 0, totMon2 = 0, totMon3 = 0, totMon4 = 0, totQuarterSum = 0;
        if (!cmbReportQuarter.getSelectionModel().getSelectedItem().equals("All Quarters")){
            if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
                stmnt = con.prepareStatement(" SELECT `revenue_item`, `item_category`, `revenueAmount`, `revenueMonth`  FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_item` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_item`, `item_category` FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");
            } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
                stmnt = con.prepareStatement(" SELECT `revenue_item`, `item_category`, `revenueAmount`, `revenueMonth`  FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_item` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_item`, `item_category` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");
            }
            else {
                stmnt = con.prepareStatement(" SELECT `revenue_item`, `item_category`, `revenueAmount`, `revenueMonth`  FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_item` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_item`, `item_category` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' AND `revenueQuarter` = '"+cmbReportQuarter.getSelectionModel().getSelectedItem()+"' GROUP BY `revenue_item`");
            }
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
            colType.setCellValueFactory(data -> data.getValue().typeProperty());
            totalAmount.setCellValueFactory(data -> data.getValue().totalAmountProperty());
            boolean finalCategory = Category;
            /*revenueItem.setCellFactory(param -> new TableCell<GetQuarterReport, String>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    if (!empty)
                    {
                        int currentIndex = indexProperty()
                                .getValue() < 0 ? 0
                                : indexProperty().getValue();
                        String type = param
                                .getTableView().getItems()
                                .get(currentIndex).getType();
                        if (type.equals("A"))
                        {
                            getTableRow().setTextFill(Color.GREEN);
                            setTextFill(Color.GREEN);
                            getTextFill();
                            setText(item.toUpperCase());
                            setStyle("-fx-alignment: CENTER; -fx-text-fill: GREEN; -fx-font-weight: BOLD; -fx-font-size: 16;");
                        } else if (type.equals("C"))
                        {
                            TableRow row = getTableRow();
                            setText(item);
                            row.setStyle("-fx-background-color: #62ef73ad;");
                        }else {
                            setText(item);
                        }
                    }
                }
            });*/
            revenueItem.setSortable(false); month1.setSortable(false); month2.setSortable(false);
            month3.setSortable(false); totalAmount.setSortable(false);
            for (String category : rowItems){
                revenueItem.setStyle("-fx-alignment: CENTER; ");
                Category = true;
                getReport = new GetQuarterReport("", "", "", category.toUpperCase(), "", "A");
                quarterTable.getItems().add(getReport);
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
                        float amot= itemQuarterSum.get(item).get(rs.getString("revenueMonth").toUpperCase());
                        amot += rs.getFloat("revenueAmount");
                        itemQuarterSum.get(item).put(rs.getString("revenueMonth").toUpperCase(), amot);
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
                Category = false;
                getReport = new GetQuarterReport(mon1, mon2, mon3, item, totalAmnt, "B");
                quarterTable.getItems().add(getReport); Mon1 = 0; Mon2 = 0; Mon3 = 0; Mon4 = 0;
            }
            subtotal_amount = subMon1 + subMon2 + subMon3;
            submon1 = formatter.format(subMon1); submon2 = formatter.format(subMon2); submon3 = formatter.format(subMon3); subtotalAmnt = formatter.format(subtotal_amount);
                Category = true;
                getReport = new GetQuarterReport(submon1, submon2, submon3, "SUB-TOTAL", subtotalAmnt, "C");
                quarterTable.getItems().add(getReport);
            }
            totmon1 = formatter.format(totMon1); totmon2 = formatter.format(totMon2); totmon3 = formatter.format(totMon3); summation = formatter.format(totQuarterSum);
            Category = true;
            getReport = new GetQuarterReport(totmon1, totmon2, totmon3, "TOTAL", summation, "C");
            quarterTable.getItems().add(getReport);
            lblMonth1.setText(totmon1); lblMonth2.setText(totmon2); lblMonth3.setText(totmon3); lblTotalAmount.setText(summation);
        }
        else {
            if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
                stmnt = con.prepareStatement(" SELECT `revenue_item`, `item_category`, `revenueAmount`, `revenueQuarter` FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_item` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_item`, `item_category` FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'GROUP BY `revenue_item`");
            } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
                stmnt = con.prepareStatement(" SELECT `revenue_item`, `item_category`, `revenueAmount`, `revenueQuarter` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_item` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_item`, `item_category` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'GROUP BY `revenue_item`");
            }
            else {
                stmnt = con.prepareStatement(" SELECT `revenue_item`, `item_category`, `revenueAmount`, `revenueQuarter` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' ORDER BY `revenue_item` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                stmnt_itemCategories = con.prepareStatement(" SELECT `revenue_item`, `item_category` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND `revenueYear` = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"'GROUP BY `revenue_item`");
            }
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
            colTypeAll.setCellValueFactory(data -> data.getValue().typeProperty());
            totalAmountAll.setCellValueFactory(data -> data.getValue().totalAmountProperty());
            revenueItemAll.setSortable(false); month1All.setSortable(false); month2All.setSortable(false);
            month3All.setSortable(false); month4All.setSortable(false); totalAmountAll.setSortable(false);
           /* revenueItemAll.setCellFactory(param -> new TableCell<GetQuarterReport, String>()
            {
                @Override
                protected void updateItem(String item, boolean empty)
                {
                    if (!empty)
                    {
                        int currentIndex = indexProperty()
                                .getValue() < 0 ? 0
                                : indexProperty().getValue();
                        String type = param
                                .getTableView().getItems()
                                .get(currentIndex).getType();
                        if (type.equals("A"))
                        {
                            setText(item.toUpperCase());
                            setStyle("-fx-alignment: CENTER; -fx-text-fill: GREEN; -fx-font-weight: BOLD; -fx-font-size: 16;");
                        } else if (type.equals("C"))
                        {
                            TableRow row = getTableRow();
                            setText(item);
                            row.setStyle("-fx-background-color: #62ef73ad;");
                        }else {
                            setText(item);
                        }
                    }
                }
            });*/
            for (String category : rowItems){
                revenueItem.setStyle("-fx-alignment: CENTER; -fx-text-fill: #000;");
                Category = true;
                getReport = new GetQuarterReport("", "", "", "", category.toUpperCase(), "", "A");
                quarterTableAll.getItems().add(getReport);
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
                Category = false;
                    getReport = new GetQuarterReport(mon1, mon2, mon3, mon4, item, totalAmnt, "B");
                    quarterTableAll.getItems().add(getReport); Mon1 = 0; Mon2 = 0; Mon3 = 0; Mon4 = 0;
                }
                subtotal_amount = subMon1 + subMon2 + subMon3 + subMon4;
                submon1 = formatter.format(subMon1); submon2 = formatter.format(subMon2); submon3 = formatter.format(subMon3); submon4 = formatter.format(subMon4); subtotalAmnt = formatter.format(subtotal_amount);
                Category = true;
                getReport = new GetQuarterReport(submon1, submon2, submon3, submon4, "SUB-TOTAL", subtotalAmnt, "C");
                quarterTableAll.getItems().add(getReport);
            }
            totmon1 = formatter.format(totMon1); totmon2 = formatter.format(totMon2); totmon3 = formatter.format(totMon3); totmon4 = formatter.format(totMon4); summation = formatter.format(totQuarterSum);
            Category = true;
            getReport = new GetQuarterReport(totmon1, totmon2, totmon3, totmon4, "TOTAL", summation, "C");
            quarterTableAll.getItems().add(getReport);
            lblMonth1All.setText(totmon1); lblMonth2All.setText(totmon2); lblMonth3All.setText(totmon3); lblMonth4All.setText(totmon4); lblTotalAmountAll.setText(summation);
        }
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
        if (cmbReportCent.getSelectionModel().isEmpty()){
            lblCenterWarn.setVisible(true);
        }else if (cmbReportYear.getSelectionModel().isEmpty()){
            lblYearWarn.setVisible(true);
        }else if (cmbReportQuarter.getSelectionModel().isEmpty()){
            lblQuarterWarn.setVisible(true);
        }else {
        quarterTableAll.getItems().clear();
        quarterTable.getItems().clear();
        getMonths();
        changeNames();
        setItems();
        }
    }

    @FXML
    void printReport(ActionEvent event) {
        if (!quarterTable.getItems().isEmpty()){
            InputStream input = null;
            try {
                Date date = new Date();
                List<GetQuarterReport> items = new ArrayList<>();
                for (int j = 0; j < quarterTable.getItems().size(); j++) {
                    GetQuarterReport getdata;
                    getdata = quarterTable.getItems().get(j);
                    items.add(getdata);
                }   URL url = this.getClass().getResource("/com/Assets/kmalogo.png")/*,
                        file = this.getClass().getResource("/com.Assets/quarterlyPortrait.jrxml")*/;
                JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
                String year = cmbReportYear.getSelectionModel().getSelectedItem(),
                        center = cmbReportCent.getSelectionModel().getSelectedItem(),
                        first = month1.getText(),
                        second = month2.getText(),
                        third = month3.getText();
                /* Map to hold Jasper report Parameters */
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("CollectionBean", itemsJRBean);
                parameters.put("logo", url);
                parameters.put("FirstMonth", first);
                parameters.put("year", year);
                parameters.put("SecondMonth", second);
                parameters.put("timeStamp", date);
                parameters.put("ThirdMonth", third);
                parameters.put("center", center);
                //read jrxml file and creating jasperdesign object
//                assert file != null;
                input = this.getClass().getResourceAsStream("/com/Assets/quarterlyPortrait.jrxml");
//                input = new FileInputStream(file.getPath());
                JasperDesign jasperDesign = JRXmlLoader.load(input);
                /*compiling jrxml with help of JasperReport class*/
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                /* Using jasperReport object to generate PDF */
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                /*call jasper engine to display report in jasperviewer window*/
                JasperViewer.viewReport(jasperPrint, false);
            }/*catch (IOException ex) {
                    log.error(ex.getMessage(), ex);
                }*/ catch (JRException ex) {
                log.error(ex.getMessage(), ex);
            }
        }else if (!quarterTableAll.getItems().isEmpty()){
            InputStream input = null;
            try {
                Date date = new Date();
                List<GetQuarterReport> items = new ArrayList<>();
                for (int j = 0; j < quarterTableAll.getItems().size(); j++) {
                    GetQuarterReport getdata;
                    getdata = quarterTableAll.getItems().get(j);
                    items.add(getdata);
                }   URL url = this.getClass().getResource("/com/Assets/kmalogo.png");
                JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
                String Aear = cmbReportYear.getSelectionModel().getSelectedItem(),
                        center = cmbReportCent.getSelectionModel().getSelectedItem(),
                        first = month1All.getText(),
                        second = month2All.getText(),
                        third = month3All.getText(),
                        fourth = month4All.getText();
                /* Map to hold Jasper report Parameters */
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("CollectionBean", itemsJRBean);
                parameters.put("logo", url);
                parameters.put("FirstMonth", first);
                parameters.put("year", Aear);
                parameters.put("SecondMonth", second);
                parameters.put("timeStamp", date);
                parameters.put("ThirdMonth", third);
                parameters.put("FourthMonth", fourth);
                parameters.put("center", center);
                //read jrxml file and creating jasperdesign object
                input = this.getClass().getResourceAsStream("/com/Assets/AllquarterlyPortrait.jrxml");
                JasperDesign jasperDesign = JRXmlLoader.load(input);
                /*compiling jrxml with help of JasperReport class*/
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                /* Using jasperReport object to generate PDF */
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                /*call jasper engine to display report in jasperviewer window*/
                JasperViewer.viewReport(jasperPrint, false);
            } catch (RuntimeException ex) {
                log.error(ex.getMessage(), ex);
            } catch (JRException ex) {
                log.error(ex.getMessage(), ex);
            } finally {
                try {
                    input.close();
                } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
                }
            }
        }else {
            event.consume();
        }
    }
    
}
/**
 *
 { Map<String, ArrayList<Float>> monthAmount = new HashMap<>();//HashMap to store revenue Amounts on their respective weeks
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
 log.error(ex.getMessage(), ex);
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
 *
 */