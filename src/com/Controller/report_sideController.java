/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.Controller.Gets.Conditioner;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.revenue_report.DBConnection;

/**
 * FXML com.Controller class
 *
 * @author HP
 */
public class report_sideController implements Initializable {

    @FXML
    private VBox entriesMain;

    @FXML
    private MaterialDesignIconView iconForward;

    @FXML
    private Text txtTitle;

    @FXML
    private Text txtReport;

    @FXML
    private MenuButton paneMaster;

    @FXML
    private MenuItem btnRevenueItemsMaster;

    @FXML
    private MenuItem btnRevenueCenters;

    @FXML
    private MenuItem btnCollectionPayment;

    @FXML
    private MenuItem btnRevenueItemsQuarterly;

    @FXML
    private MenuItem btnQuarterlyCentersBasis;

    @FXML
    private HBox containerDailies;

    @FXML
    private JFXButton btnViewReport;

    @FXML
    private Button btnCloseDaily;

    @FXML
    private HBox containerWeekly;

    @FXML
    private JFXButton btnWeeklyReport;

    @FXML
    private Button btnCloseWeekly;

    @FXML
    private HBox containerMonthly;

    @FXML
    private JFXButton btnMonthlyReport;

    @FXML
    private Button btnCloseMonthly;

    @FXML
    private HBox containerStock;

    @FXML
    private JFXButton btnValueBooksReport;

    @FXML
    private Button btnCloseValue;

    @FXML
    private HBox containerQuarterly;

    @FXML
    private HBox containerMaster;

    @FXML
    private JFXButton btnQuarterlyReport;

    @FXML
    private Button btnCloseQuarterly;

    @FXML
    private HBox containerYearly;

    @FXML
    private JFXButton btnYearlyReport;

    @FXML
    private Button btnCloseYearly;

    @FXML
    private HBox containerPayment;

    @FXML
    private JFXButton btnPaymentDetails;

    @FXML
    private Button btnClosePayment;

    @FXML
    private Button btnCloseMaster;

    @FXML
    private HBox containerTarget;

    @FXML
    private JFXButton btnRevenueTarget;

    @FXML
    private Button btnCloseTarget;

    @FXML
    private VBox paneEntriesArea;

    @FXML
    private AnchorPane noEntriesPane;

    @FXML
    private Text txtEntriesMainNoPane;
    private appController app;
    Conditioner conditioner = new Conditioner();

    private final Connection con;

    public report_sideController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
     public void setappController(appController app){
         this.app = app;
     }

     Map<String, ArrayList<String>> catCenter = new HashMap<>();
    ObservableList<JFXButton> menuButtons = FXCollections.observableArrayList();
    ObservableList <HBox> menuContainers = FXCollections.observableArrayList();
    ObservableList <Button> menuCloseButton = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtReport.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        menuButtons.addAll(Arrays.asList(btnViewReport,btnWeeklyReport,btnMonthlyReport,btnValueBooksReport,btnQuarterlyReport,btnYearlyReport,btnPaymentDetails,btnRevenueTarget));
        menuContainers.addAll(Arrays.asList(containerMaster,containerStock,containerDailies,containerTarget,containerPayment,containerWeekly,containerMonthly,containerQuarterly,containerYearly));
        menuCloseButton.addAll(Arrays.asList(btnCloseMaster,btnCloseDaily,btnCloseValue,btnClosePayment,btnCloseTarget,btnCloseWeekly,btnCloseMonthly,btnCloseQuarterly,btnCloseYearly));
        if (LogInController.hasCenter){
            paneMaster.setVisible(false);
        }
        try {
            PreparedStatement stmnt = con.prepareStatement("SELECT `revenue_category` FROM `revenue_centers` WHERE 1 GROUP BY `revenue_category`");
            ResultSet rt = stmnt.executeQuery();
            while (rt.next()){
                catCenter.put(rt.getString("revenue_category"), new ArrayList<>());
            }
            stmnt = con.prepareStatement("SELECT `revenue_category`, `revenue_center` FROM `revenue_centers` WHERE 1 GROUP BY `revenue_category`");
            rt = stmnt.executeQuery();
            while (rt.next()){
                catCenter.get(rt.getString("revenue_category")).add(rt.getString("revenue_center"));
            }
            conditioner.setMapper(catCenter);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }    

    @FXML
    private void Show_report(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/Report.fxml"));
        loader.setController(new ReportController());
        ReportController rep = (ReportController)loader.getController();
        for (JFXButton btn: menuButtons){
            if (btn.equals(btnViewReport)){
                btn.getStyleClass().removeAll("menu-title-button1, focus");
            }
        }
        for (HBox container : menuContainers){
            if (container.equals(containerDailies)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseDaily)){
                close.setVisible(false);
            }else {
                btnCloseDaily.setVisible(true);
            }
        }
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Daily Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showWeeklyReport(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/weeklyReport.fxml"));
        loader.setController(new weeklyReportController());
        weeklyReportController week = (weeklyReportController)loader.getController();
        week.setReportSide(this);
        for (JFXButton btn: menuButtons){
            if (btn.equals(btnWeeklyReport)){
                btn.getStyleClass().removeAll("menu-title-button1, focus");
            }
        }
        for (HBox container : menuContainers){
            if (container.equals(containerWeekly)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseWeekly)){
                close.setVisible(false);
            }else {
                btnCloseWeekly.setVisible(true);
            }
        }
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Weekly Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showMonthlyReport(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/MonthlyReport.fxml"));
        loader.setController(new MonthlyReportController());
        for (JFXButton btn: menuButtons){
            if (btn.equals(btnMonthlyReport)){
                btn.getStyleClass().removeAll("menu-title-button1, focus");
            }
        }
        for (HBox container : menuContainers){
            if (container.equals(containerMonthly)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseMonthly)){
                close.setVisible(false);
            }else {
                btnCloseMonthly.setVisible(true);
            }
        }
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Monthly Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showQuarterlyReport(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/QuarterlyReport.fxml"));
        loader.setController(new QuarterlyReportController());
        for (JFXButton btn: menuButtons){
            if (btn.equals(btnQuarterlyReport)){
                btn.getStyleClass().removeAll("menu-title-button1, focus");
            }
        }
        for (HBox container : menuContainers){
            if (container.equals(containerQuarterly)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseQuarterly)){
                close.setVisible(false);
            }else {
                btnCloseQuarterly.setVisible(true);
            }
        }
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Quarterly Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showYearlyReport(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/yearlyReport.fxml"));
        loader.setController(new YearlyReportController());
        for (JFXButton btn: menuButtons){
            if (btn.equals(btnYearlyReport)){
                btn.getStyleClass().removeAll("menu-title-button1, focus");
            }
        }
        for (HBox container : menuContainers){
            if (container.equals(containerYearly)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseYearly)){
                close.setVisible(false);
            }else {
                btnCloseYearly.setVisible(true);
            }
        }
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Yearly Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showRevenueTarget(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/Target_Analysis.fxml"));
        loader.setController(new Target_AnalysisController());
        for (JFXButton btn: menuButtons){
            if (btn.equals(btnRevenueTarget)){
                btn.getStyleClass().removeAll("menu-title-button1, focus");
            }
        }
        for (HBox container : menuContainers){
            if (container.equals(containerTarget)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseTarget)){
                close.setVisible(false);
            }else {
                btnCloseTarget.setVisible(true);
            }
        }
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Annual Target Analysis");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    void showValueBooksReport(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/ValueBooksStockReport.fxml"));
        loader.setController(new ValueBooksStockReportController());
        for (JFXButton btn: menuButtons){
            if (btn.equals(btnValueBooksReport)){
                btn.getStyleClass().removeAll("menu-title-button1, focus");
            }
        }
        for (HBox container : menuContainers){
            if (container.equals(containerStock)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseValue)){
                close.setVisible(false);
            }else {
                btnCloseValue.setVisible(true);
            }
        }
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Value Books Stock Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showRevenueItemsMaster(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/MasterItems.fxml"));
        loader.setController(new MasterItemsController());
        paneMaster.getStyleClass().removeAll("menu-title-button1, focus");
        for (HBox container : menuContainers){
            if (container.equals(containerTarget)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseMaster)){
                close.setVisible(false);
            }else {
                btnCloseMaster.setVisible(true);
            }
        }
        txtReport.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        for (HBox container : menuContainers){
            container.getStyleClass().remove("big");}
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Revenue Items Annual Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showRevenueCenters(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/MasterCenters.fxml"));
        loader.setController(new MasterCentersController());
        paneMaster.getStyleClass().removeAll("menu-title-button1, focus");
        for (HBox container : menuContainers){
            if (container.equals(containerTarget)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseMaster)){
                close.setVisible(false);
            }else {
                btnCloseMaster.setVisible(true);
            }
        }
        txtReport.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        for (HBox container : menuContainers){
            container.getStyleClass().remove("big");}
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Revenue Centers Annual Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showCollectionPayment(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/Collection_PaymentAnalysisReport.fxml"));
        loader.setController(new Collection_PaymentAnalysisReportController());
        paneMaster.getStyleClass().removeAll("menu-title-button1, focus");
        for (HBox container : menuContainers){
            if (container.equals(containerTarget)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseMaster)){
                close.setVisible(false);
            }else {
                btnCloseMaster.setVisible(true);
            }
        }
        txtReport.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        for (HBox container : menuContainers){
            container.getStyleClass().remove("big");}
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Revenue VS Payment Analysis");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showRevenueItemsQuarterly(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/MasterQuarterItems.fxml"));
        loader.setController(new MasterQuarterItemsController());
        paneMaster.getStyleClass().removeAll("menu-title-button1, focus");
        for (HBox container : menuContainers){
            if (container.equals(containerTarget)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseMaster)){
                close.setVisible(false);
            }else {
                btnCloseMaster.setVisible(true);
            }
        }
        txtReport.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        for (HBox container : menuContainers){
            container.getStyleClass().remove("big");}
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Revenue Items Quarterly Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showQuarterlyCenters(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/MasterQuarterCenters.fxml"));
        loader.setController(new MasterQuarterCentersController());
        paneMaster.getStyleClass().removeAll("menu-title-button1, focus");
        for (HBox container : menuContainers){
            if (container.equals(containerTarget)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnCloseMaster)){
                close.setVisible(false);
            }else {
                btnCloseMaster.setVisible(true);
            }
        }
        txtReport.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        for (HBox container : menuContainers){
            container.getStyleClass().remove("big");}
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Revenue Centers Quarterly Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showPaymentDetails(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/Payment_Report.fxml"));
        loader.setController(new Payment_ReportController());
        for (JFXButton btn: menuButtons){
            if (btn.equals(btnPaymentDetails)){
                btn.getStyleClass().removeAll("menu-title-button1, focus");
            }
        }
        for (HBox container : menuContainers){
            if (container.equals(containerPayment)){
                container.getStyleClass().removeAll("menu-title, focus");
                container.getStyleClass().remove("big");
                container.getStyleClass().add("big");
            }else {
                container.getStyleClass().remove("big");
            }
        }
        for (Button close : menuCloseButton){
            if (!close.equals(btnClosePayment)){
                close.setVisible(false);
            }else {
                btnClosePayment.setVisible(true);
            }
        }
        txtReport.setVisible(true);
        iconForward.setVisible(true);
        txtTitle.setText("Payments Report");
        txtTitle.setVisible(true);
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showClose(ActionEvent event) {
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(noEntriesPane);
        txtEntriesMainNoPane.setVisible(true);
        for (Button close : menuCloseButton){
            if (close.isVisible()){
                close.setVisible(false);
            }
        }
        txtReport.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        for (HBox container : menuContainers){
            container.getStyleClass().remove("big");}
    }
}
