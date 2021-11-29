/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.Gets.GetMonthlyReport;
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
import org.apache.log4j.Logger;
import com.revenue_report.DBConnection;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * FXML com.Controller class
 *
 * @author NiTrO
 */
public class MonthlyReportController implements Initializable {

    @FXML
    private VBox monthlyTemplate;
    @FXML
    private JFXButton btnPrint;

    @FXML
    private Label lblJanSum;

    @FXML
    private Label lblFebSum;

    @FXML
    private Label lblMarSum;

    @FXML
    private Label lblAprSum;

    @FXML
    private Label lblMaySum;

    @FXML
    private Label lblJunSum;

    @FXML
    private Label lblJulSum;

    @FXML
    private Label lblAugSum;

    @FXML
    private Label lblSepSum;

    @FXML
    private Label lblOctSum;

    @FXML
    private Label lblNovSum;

    @FXML
    private Label lblDecSum;

    @FXML
    private Label lblTotSum;
    @FXML
    private ComboBox<String> cmbReportCent;
    @FXML
    private ComboBox<String> cmbReportYear;
    @FXML
    private JFXButton btnShowReport;
    @FXML
    private AnchorPane generalPane;
    @FXML
    private TableView<GetMonthlyReport> monthlyTable;
    @FXML
    private TableColumn<GetMonthlyReport, String> revenueItem;
    @FXML
    private TableColumn<GetMonthlyReport, String> january;
    @FXML
    private TableColumn<GetMonthlyReport, String> february;
    @FXML
    private TableColumn<GetMonthlyReport, String> march;
    @FXML
    private TableColumn<GetMonthlyReport, String> april;
    @FXML
    private TableColumn<GetMonthlyReport, String> may;
    @FXML
    private TableColumn<GetMonthlyReport, String> june;
    @FXML
    private TableColumn<GetMonthlyReport, String> july;
    @FXML
    private TableColumn<GetMonthlyReport, String> august;
    @FXML
    private TableColumn<GetMonthlyReport, String> september;
    @FXML
    private TableColumn<GetMonthlyReport, String> october;
    @FXML
    private TableColumn<GetMonthlyReport, String> november;
    @FXML
    private TableColumn<GetMonthlyReport, String> december;
    @FXML
    private TableColumn<GetMonthlyReport, String> totalAmount;
    @FXML
    private Label lblCenterWarn;
    @FXML
    private Label lblYearWarn;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblRevenueCenter;
    @FXML
    private AnchorPane subPane;
    @FXML
    private TableView<GetMonthlyReport> monthlyTableSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> revenueItemSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> januarySub;
    @FXML
    private TableColumn<GetMonthlyReport, String> februarySub;
    @FXML
    private TableColumn<GetMonthlyReport, String> marchSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> aprilSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> maySub;
    @FXML
    private TableColumn<GetMonthlyReport, String> juneSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> julySub;
    @FXML
    private TableColumn<GetMonthlyReport, String> augustSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> septemberSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> octoberSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> novemberSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> decemberSub;
    @FXML
    private TableColumn<GetMonthlyReport, String> totalAmountSub;
    @FXML
    private Label lblCumuAmount;
    @FXML
    private Label lblCCAmount;
    @FXML
    private Label lblCommission;
    @FXML
    private Label lblCostValueBooks;
    @FXML
    private Label lblDiff;
    @FXML
    private Label lblNetRevenue;
    @FXML
    private Label lblAmtDueSub;
    @FXML
    private Label lblAmtDueKMA;


    /**
     * Initializes the controller class.
     */
    
    private final Connection con;
    private PreparedStatement stmnt;
    ResultSet rs;
    ObservableList<String> rowCent =FXCollections.observableArrayList();
    ObservableList<String> rowMonths =FXCollections.observableArrayList();
    ObservableList<String> rowYear =FXCollections.observableArrayList();
    ObservableList<String> rowItems =FXCollections.observableArrayList();
    Map<String, String> centerID = new HashMap<>();
    Logger log = Logger.getLogger(MonthlyReportController.class.getName());
    private boolean subMetroPR, Condition;
    private String SelectedCenter;
    
    public MonthlyReportController() throws SQLException, ClassNotFoundException{
        this.con = DBConnection.getConn();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbReportCent.setOnMouseClicked(e -> lblCenterWarn.setVisible(false));
        cmbReportYear.setOnMouseClicked(e -> lblYearWarn.setVisible(false));
        try {
            getRevCenters();
        } catch (SQLException | ClassNotFoundException ex) {
            log.error(ex.getMessage(), ex);
        }
        if (LogInController.hasCenter){
            try {
                getReportYear();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
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
        if (LogInController.hasCenter){
            cmbReportCent.getSelectionModel().select(LogInController.loggerCenterName);
            SelectedCenter = cmbReportCent.getSelectionModel().getSelectedItem();
            cmbReportCent.setDisable(true);
        }
    

    }
    
    private void getReportYear() throws SQLException{
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT YEAR(revenueDate) AS `revenueYear` FROM `revenue_centers`,`daily_entries` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' GROUP BY `daily_entries`.`revenueYear`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT YEAR(revenueDate) AS `revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' GROUP BY `daily_entries`.`revenueYear`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT YEAR(revenueDate) AS `revenueYear` FROM `daily_entries`,`revenue_centers` WHERE `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"'  GROUP BY `revenueYear`");
        }
        rs = stmnt.executeQuery();
        rowYear.clear();
        cmbReportYear.getItems().clear();
        while(rs.next()){
                 rowYear.add(rs.getString("revenueYear"));
        }
        cmbReportYear.getItems().addAll(rowYear);
        cmbReportYear.setVisibleRowCount(5);
    }
    
    private void changeNames() {
        lblRevenueCenter.setText(cmbReportCent.getSelectionModel().getSelectedItem());
        lblYear.setText(cmbReportYear.getSelectionModel().getSelectedItem());
    }
    
    private void setItems() throws SQLException{
        monthlyTable.getItems().clear();
        String totJan = "0.00", totFeb = "0.00", totMar = "0.00", totApr = "0.00", totMay = "0.00", totJun = "0.00", totJul = "0.00", totAug = "0.00", totSep = "0.00", totOct = "0.00", totNov = "0.00", totDec = "0.00", summation = "0.00", Jan = "0.00", Feb = "0.00", Mar = "0.00", Apr = "0.00", May = "0.00", Jun = "0.00", Jul = "0.00", Aug = "0.00", Sep = "0.00", Oct = "0.00", Nov = "0.00", Dec = "0.00", totalAmnt = "0.00";
        float  totjan = 0, totfeb = 0,totmar = 0,totapr = 0, totmay = 0, totjun = 0, totjul = 0, totaug = 0,totsep = 0,totoct = 0, totnov = 0, totdec = 0, totMonthsum = 0,  jan = 0, feb = 0, apr = 0, mai = 0, jun = 0, jul = 0, aug = 0, sep = 0, oct = 0, mar = 0, nov = 0, dec = 0, total_amount = 0;
        NumberFormat formatter = new DecimalFormat("#,##0.00");
        PreparedStatement stmnt_itemsCategories;
        ResultSet rs, rs_itemsCategories;
        if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE ALL")) {
            stmnt = con.prepareStatement(" SELECT `item_Sub`, `item_category`, `revenueAmount`, MONTH(revenueDate) AS `revenueMonth` FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND YEAR(revenueDate)  = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' ORDER BY `item_Sub` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmnt_itemsCategories = con.prepareStatement(" SELECT `item_Sub`, `item_category` FROM `revenue_centers`,`daily_entries`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `revenue_centers`.`revenue_category` = 'PROPERTY RATE SECTION' AND YEAR(revenueDate)  = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `item_Sub`");
        } else if (cmbReportCent.getSelectionModel().getSelectedItem().equals("PROPERTY RATE SUB-METROS")){
            stmnt = con.prepareStatement(" SELECT `item_Sub`, `item_category`, `revenueAmount`, MONTH(revenueDate) AS `revenueMonth` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND YEAR(revenueDate)  = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' ORDER BY `item_Sub` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmnt_itemsCategories = con.prepareStatement(" SELECT `item_Sub`, `item_category` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND `daily_entries`.`daily_revCenter` = 'K0201' OR `daily_entries`.`daily_revCenter` = 'K0202' OR `daily_entries`.`daily_revCenter` = 'K0203' OR `daily_entries`.`daily_revCenter` = 'K0204' OR `daily_entries`.`daily_revCenter` = 'K0205' AND YEAR(revenueDate)  = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `item_Sub`");
        }
        else {
            stmnt = con.prepareStatement(" SELECT `item_Sub`, `item_category`, `revenueAmount`, MONTH(revenueDate) AS `revenueMonth` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND YEAR(revenueDate)  = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' ORDER BY `item_Sub` ASC", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stmnt_itemsCategories = con.prepareStatement(" SELECT `item_Sub`, `item_category` FROM `daily_entries`,`revenue_centers`,`revenue_items` WHERE `revenue_items`.`revenue_item_ID` = `daily_entries`.`revenueItem` AND `revenue_centers`.`CenterID` = `daily_entries`.`daily_revCenter` AND`revenue_centers`.`revenue_center` = '"+cmbReportCent.getSelectionModel().getSelectedItem()+"' AND YEAR(revenueDate)  = '"+cmbReportYear.getSelectionModel().getSelectedItem()+"' GROUP BY `item_Sub`");
        }
       rs = stmnt.executeQuery();
        rs_itemsCategories = stmnt_itemsCategories.executeQuery();
        rowItems.clear();
        Map<String, ArrayList<String>> categoriesItem = new HashMap<>();
        categoriesItem.put("", new ArrayList<>());
        while (rs_itemsCategories.next()){
            if (!rowItems.contains(rs_itemsCategories.getString("item_category"))){
                rowItems.add(rs_itemsCategories.getString("item_category"));
            }
            if (!categoriesItem.containsKey(rs_itemsCategories.getString("item_category"))){
                categoriesItem.put(rs_itemsCategories.getString("item_category"), new ArrayList<>());
                categoriesItem.get(rs_itemsCategories.getString("item_category")).add(rs_itemsCategories.getString("item_Sub"));
            }else if (categoriesItem.containsKey(rs_itemsCategories.getString("item_category")) && !categoriesItem.get(rs_itemsCategories.getString("item_category")).contains(rs_itemsCategories.getString("item_Sub"))){
                categoriesItem.get(rs_itemsCategories.getString("item_category")).add(rs_itemsCategories.getString("item_Sub"));
            }
        }
        revenueItem.setCellValueFactory(data -> data.getValue().RevenueItemProperty());
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
        revenueItem.setSortable(false); march.setSortable(false); april.setSortable(false); may.setSortable(false); june.setSortable(false);
        january.setSortable(false); july.setSortable(false); august.setSortable(false); september.setSortable(false); october.setSortable(false);
        february.setSortable(false); november.setSortable(false); december.setSortable(false); totalAmount.setSortable(false);
        GetMonthlyReport getReport;
        for (String category : rowItems){
            revenueItem.setStyle("-fx-alignment: CENTER; -fx-text-fill: #5a5959;");
            getReport = new GetMonthlyReport(category, "", "", "", "", "", "", "", "", "", "", "", "", "");
            monthlyTable.getItems().add(getReport);
            float subjan = 0, subfeb = 0, subapr = 0, submai = 0, subjun = 0, subjul = 0, subaug = 0, subsep = 0, suboct = 0, submar = 0, subnov = 0, subdec = 0, subtotal_amount;
            String subJan = "0.00", subFeb = "0.00", subMar = "0.00", subApr = "0.00", subMay = "0.00", subJun = "0.00", subJul = "0.00", subAug = "0.00", subSep = "0.00", subOct = "0.00", subNov = "0.00", subDec = "0.00", subTotalAmnt = "0.00";
            for (String item : categoriesItem.get(category)) {
                Map<String, Map<String, Float>> itemMonthSum = new HashMap<>();
                Map<String, Float> monthSum = new HashMap<>();
                monthSum.put(january.getText().toUpperCase(), jan); monthSum.put(february.getText().toUpperCase(), feb); monthSum.put(march.getText().toUpperCase(), mar);
                monthSum.put(april.getText().toUpperCase(), apr); monthSum.put(may.getText().toUpperCase(), mai); monthSum.put(june.getText().toUpperCase(), jun);
                monthSum.put(july.getText().toUpperCase(), jul); monthSum.put(august.getText().toUpperCase(), aug); monthSum.put(september.getText().toUpperCase(), sep);
                monthSum.put(october.getText().toUpperCase(), oct); monthSum.put(november.getText().toUpperCase(), nov); monthSum.put(december.getText().toUpperCase(), dec);
                itemMonthSum.put(item, monthSum);
                boolean resultSetState = true;
                while (resultSetState){
                    rs.next();
                    if (item.equals(rs.getString("item_Sub"))){
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
                subjan += itemMonthSum.get(item).get(january.getText().toUpperCase()); subfeb += itemMonthSum.get(item).get(february.getText().toUpperCase()); submar += itemMonthSum.get(item).get(march.getText().toUpperCase()); subapr += itemMonthSum.get(item).get(april.getText().toUpperCase()); submai += itemMonthSum.get(item).get(may.getText().toUpperCase()); subjun += itemMonthSum.get(item).get(june.getText().toUpperCase());
                subjul += itemMonthSum.get(item).get(july.getText().toUpperCase()); subaug += itemMonthSum.get(item).get(august.getText().toUpperCase()); subsep += itemMonthSum.get(item).get(september.getText().toUpperCase()); suboct += itemMonthSum.get(item).get(october.getText().toUpperCase()); subnov += itemMonthSum.get(item).get(november.getText().toUpperCase()); subdec += itemMonthSum.get(item).get(december.getText().toUpperCase());
                totjan += itemMonthSum.get(item).get(january.getText().toUpperCase()); totfeb += itemMonthSum.get(item).get(february.getText().toUpperCase()); totmar += itemMonthSum.get(item).get(march.getText().toUpperCase()); totapr += itemMonthSum.get(item).get(april.getText().toUpperCase()); totmay += itemMonthSum.get(item).get(may.getText().toUpperCase()); totjun += itemMonthSum.get(item).get(june.getText().toUpperCase());
                totjul += itemMonthSum.get(item).get(july.getText().toUpperCase()); totaug += itemMonthSum.get(item).get(august.getText().toUpperCase()); totsep += itemMonthSum.get(item).get(september.getText().toUpperCase()); totoct += itemMonthSum.get(item).get(october.getText().toUpperCase()); totnov += itemMonthSum.get(item).get(november.getText().toUpperCase()); totdec += itemMonthSum.get(item).get(december.getText().toUpperCase());
                Jan = formatter.format(itemMonthSum.get(item).get(january.getText().toUpperCase())); Feb = formatter.format(itemMonthSum.get(item).get(february.getText().toUpperCase())); Mar = formatter.format(itemMonthSum.get(item).get(march.getText().toUpperCase())); Apr = formatter.format(itemMonthSum.get(item).get(april.getText().toUpperCase())); May = formatter.format(itemMonthSum.get(item).get(may.getText().toUpperCase()));
                Jun = formatter.format(itemMonthSum.get(item).get(june.getText().toUpperCase())); Jul = formatter.format(itemMonthSum.get(item).get(july.getText().toUpperCase())); Aug = formatter.format(itemMonthSum.get(item).get(august.getText().toUpperCase())); Sep = formatter.format(itemMonthSum.get(item).get(september.getText().toUpperCase())); Oct = formatter.format(itemMonthSum.get(item).get(october.getText().toUpperCase()));
                Nov = formatter.format(itemMonthSum.get(item).get(november.getText().toUpperCase())); Dec = formatter.format(itemMonthSum.get(item).get(december.getText().toUpperCase()));
                total_amount = itemMonthSum.get(item).get(january.getText().toUpperCase()) + itemMonthSum.get(item).get(february.getText().toUpperCase()) + itemMonthSum.get(item).get(march.getText().toUpperCase()) + itemMonthSum.get(item).get(april.getText().toUpperCase()) + itemMonthSum.get(item).get(may.getText().toUpperCase()) +itemMonthSum.get(item).get(june.getText().toUpperCase()) + itemMonthSum.get(item).get(july.getText().toUpperCase()) + itemMonthSum.get(item).get(august.getText().toUpperCase()) + itemMonthSum.get(item).get(september.getText().toUpperCase()) + itemMonthSum.get(item).get(october.getText().toUpperCase()) + itemMonthSum.get(item).get(november.getText().toUpperCase()) + itemMonthSum.get(item).get(december.getText().toUpperCase()); totMonthsum += total_amount;
                totalAmnt = formatter.format(total_amount);
                getReport = new GetMonthlyReport(item, Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec, totalAmnt);
                monthlyTable.getItems().add(getReport);
                jan = 0; feb = 0; apr = 0; mai = 0; jun = 0; jul = 0; aug = 0; sep = 0; oct = 0; mar = 0; nov = 0; dec = 0;
            }
            subtotal_amount = subjan + subfeb + submar + subapr + submai + subjun + subjul + subaug + subsep + suboct + subnov + subdec;
            subJan = formatter.format(subjan); subFeb = formatter.format(subfeb); subMar = formatter.format(submar); subApr = formatter.format(subapr);
            subMay = formatter.format(submai);subJun = formatter.format(subjun); subJul = formatter.format(subjul); subAug = formatter.format(subaug); subSep = formatter.format(subsep);
            subOct = formatter.format(suboct); subNov = formatter.format(subnov); subDec = formatter.format(subdec); subTotalAmnt = formatter.format(subtotal_amount);
            getReport = new GetMonthlyReport("SUB-TOTAL", subJan, subFeb, subMar, subApr, subMay, subJun, subJul,subAug,subSep,subOct,subNov,subDec, subTotalAmnt);
            monthlyTable.getItems().add(getReport);
        }
        totJan = formatter.format(totjan); totFeb = formatter.format(totfeb); totMar = formatter.format(totmar); totApr = formatter.format(totapr);
        totMay = formatter.format(totmay); totJun = formatter.format(totjun); totJul = formatter.format(totjul); totAug = formatter.format(totaug); totSep = formatter.format(totsep);
        totOct = formatter.format(totoct); totNov = formatter.format(totnov); totDec = formatter.format(totdec); summation = formatter.format(totMonthsum);
                getReport = new GetMonthlyReport("TOTAL", totJan, totFeb, totMar, totApr, totMay, totJun, totJul, totAug, totSep, totOct, totNov, totDec, summation);
        monthlyTable.getItems().add(getReport);
//        lblJanSum.setText(totJan); lblFebSum.setText(totFeb); lblMarSum.setText(totMar); lblAprSum.setText(totApr); lblMaySum.setText(totMay); lblJunSum.setText(totJun); lblJulSum.setText(totJul);
    }

    @FXML
    private void SelectedCenter(ActionEvent event) throws SQLException {
        getReportYear();
    }

    @FXML
    private void SelectedYear(ActionEvent event) {
    }

    @FXML
    private void ShowReport(ActionEvent event) throws SQLException {
        if (cmbReportCent.getSelectionModel().isEmpty()){
            lblCenterWarn.setVisible(true);
        } else if (cmbReportYear.getSelectionModel().isEmpty()){
            lblYearWarn.setVisible(true);
        }else {
        monthlyTable.getItems().clear();
        changeNames();
        setItems();
        }
        
    }

    @FXML
    void printReport(ActionEvent event){
        if (monthlyTable.getItems().isEmpty()){
            event.consume();
        }else {
            try {
                Date date = new Date();
                List<GetMonthlyReport> items = new ArrayList<>(monthlyTable.getItems());
                URL url = this.getClass().getResource("/com/Assets/kmalogo.png");
                JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);
                String year = cmbReportYear.getSelectionModel().getSelectedItem(),
                        center = cmbReportCent.getSelectionModel().getSelectedItem();
                /* Map to hold Jasper report Parameters */
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("CollectionBean", itemsJRBean);
                parameters.put("logo", url);
                parameters.put("year", year);
                parameters.put("timeStamp", date);
                parameters.put("center", center);
                //read jrxml file and creating jasperdesign object
                InputStream input = this.getClass().getResourceAsStream("/com/Assets/monthlyPotrait.jrxml");
                JasperDesign jasperDesign = JRXmlLoader.load(input);
                /*compiling jrxml with help of JasperReport class*/
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                /* Using jasperReport object to generate PDF */
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                /*call jasper engine to display report in jasperviewer window*/
                JasperViewer.viewReport(jasperPrint, false);
            } catch (JRException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
    
}
