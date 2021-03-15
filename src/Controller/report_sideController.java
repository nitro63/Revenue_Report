/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class report_sideController implements Initializable {

    @FXML
    private Button btnViewReport;
    
    private appController app;
    @FXML
    private Button btnWeeklyReport;
    @FXML
    private Button btnMonthlyReport;
    @FXML
    private Button btnQuarterlyReport;
    @FXML
    private Button btnYearlyReport;
    @FXML
    private Button btnBankDetails;
    @FXML
    private Button btnRevenueTarget;
    @FXML
    private Button btnRevenueItemsMaster;
    @FXML
    private Button btnRevenueCenters;
    @FXML
    private Button btnCollectionPayment;
    @FXML
    private HBox Master;
    @FXML
    private Button btnRevenueItemsQuarterly;
    @FXML
    private Button btnRevenueCenters1;
    @FXML
    private Button btnPaymentDetails;
    @FXML
    private Button btnValueBooksReport;
    
     public void setappController(appController app){
         this.app = app;
     }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void Show_report(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/Report.fxml"));
        loader.setController(new ReportController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    void showBankDetails(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/BankDetailsReport.fxml"));
        loader.setController(new BankDetailsReportController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    private void showWeeklyReport(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/weeklyReport.fxml"));
        loader.setController(new weeklyReportController());
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
