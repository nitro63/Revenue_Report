/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import Controller.Gets.Conditioner;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class report_sideController implements Initializable {

    @FXML
    private AnchorPane paneReport;

    @FXML
    private JFXButton btnViewReport;
    
    private appController app;
    @FXML
    private JFXButton btnWeeklyReport;
    @FXML
    private JFXButton btnMonthlyReport;
    @FXML
    private JFXButton btnQuarterlyReport;
    @FXML
    private JFXButton btnYearlyReport;
    @FXML
    private JFXButton btnBankDetails;
    @FXML
    private JFXButton btnRevenueTarget;
    @FXML
    private JFXButton btnRevenueItemsMaster;
    @FXML
    private JFXButton btnRevenueCenters;
    @FXML
    private JFXButton btnCollectionPayment;
    @FXML
    private AnchorPane paneMaster;
    @FXML
    private JFXButton btnRevenueItemsQuarterly;
    @FXML
    private JFXButton btnQuarterlyCentersBasis;
    @FXML
    private JFXButton btnPaymentDetails;
    @FXML
    private JFXButton btnValueBooksReport;
    Conditioner conditioner = new Conditioner();

    private final Connection con;

    public report_sideController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
     public void setappController(appController app){
         this.app = app;
     }

     Map<String, ArrayList<String>> catCenter = new HashMap<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (LogInController.hasCenter){
            paneMaster.setVisible(false);
            paneReport.setPrefHeight(480);
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
        loader.setLocation(getClass().getResource("/Views/fxml/Report.fxml"));
        loader.setController(new ReportController());
        ReportController rep = (ReportController)loader.getController();
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showWeeklyReport(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/weeklyReport.fxml"));
        loader.setController(new weeklyReportController());
        weeklyReportController week = (weeklyReportController)loader.getController();
        week.setReportSide(this);
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showMonthlyReport(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/MonthlyReport.fxml"));
        loader.setController(new MonthlyReportController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showQuarterlyReport(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/QuarterlyReport.fxml"));
        loader.setController(new QuarterlyReportController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showYearlyReport(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/yearlyReport.fxml"));
        loader.setController(new YearlyReportController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showRevenueTarget(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/Target_Analysis.fxml"));
        loader.setController(new Target_AnalysisController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    void showValueBooksReport(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/ValueBooksStockReport.fxml"));
        loader.setController(new ValueBooksStockReportController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showRevenueItemsMaster(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/MasterItems.fxml"));
        loader.setController(new MasterItemsController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showRevenueCenters(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/MasterCenters.fxml"));
        loader.setController(new MasterCentersController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showCollectionPayment(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/Collection_PaymentAnalysisReport.fxml"));
        loader.setController(new Collection_PaymentAnalysisReportController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showRevenueItemsQuarterly(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/MasterQuarterItems.fxml"));
        loader.setController(new MasterQuarterItemsController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showQuarterlyCenters(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/MasterQuarterCenters.fxml"));
        loader.setController(new MasterQuarterCentersController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showPaymentDetails(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/Payment_Report.fxml"));
        loader.setController(new Payment_ReportController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }
    
}
