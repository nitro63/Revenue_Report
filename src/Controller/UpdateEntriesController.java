/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class UpdateEntriesController implements Initializable {

    @FXML
    private TableView<GetValueBooksEntries> tblValueBookStocks;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colStckMonth;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colStckDATE;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colStckTypeVB;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colSerialFrom;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colSerialTo;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colStckQuantity;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colStckAmtVal;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colStckCumuAmt;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colStckPurAmt;
    @FXML
    private TableView<GetTargetEnt> tblTargetEntries;
    @FXML
    private TableColumn<GetTargetEnt, String> colTargCenter;
    @FXML
    private TableColumn<GetTargetEnt, String> colTargAmount;
    @FXML
    private TableColumn<GetTargetEnt, String> colTargYear;
    @FXML
    private TableView<GetEntries> tblCollectionEntries;
    @FXML
    private TableColumn<GetEntries, String> colColCenter;
    @FXML
    private TableColumn<GetEntries, String> colItemCode;
    @FXML
    private TableColumn<GetEntries, String> colRevItem;
    @FXML
    private TableColumn<GetEntries, String> colRevAmt;
    @FXML
    private TableColumn<GetEntries, String> colRevDate;
    @FXML
    private TableColumn<GetEntries, String> colRevWeek;
    @FXML
    private TableColumn<GetEntries, String> colRevMonth;
    @FXML
    private TableColumn<GetEntries, String> colRevQuarter;
    @FXML
    private TableColumn<GetEntries, String> colRevYear;
    @FXML
    private TableView<GetCollectEnt> tblPaymentEntries;
    @FXML
    private TableColumn<GetCollectEnt, String> colPayCenter;
    @FXML
    private TableColumn<GetCollectEnt, String> colPayGCR;
    @FXML
    private TableColumn<GetCollectEnt, String> colPayAmount;
    @FXML
    private TableColumn<GetCollectEnt, String> colPayDATE;
    @FXML
    private TableColumn<GetCollectEnt, String> colColPayMonth;
    @FXML
    private TableColumn<GetCollectEnt, String> colPayYear;
    @FXML
    private TableColumn<GetCollectEnt, String> colPayType;
    @FXML
    private Label lblTitle;
    @FXML
    private TableView<GetBankDetails> tblChequeDetails;
    @FXML
    private TableColumn<GetBankDetails, String> colChqGCR;
    @FXML
    private TableColumn<GetBankDetails, String> colPayChqDate;
    @FXML
    private TableColumn<GetBankDetails, String> colChqDate;
    @FXML
    private TableColumn<GetBankDetails, String> colChqNmb;
    @FXML
    private TableColumn<GetBankDetails, String> colBank;
    @FXML
    private TableColumn<GetBankDetails, String> colChqAmount;
    @FXML
    private AnchorPane paneValueBooks;
    @FXML
    private JFXDatePicker entDatePck;
    @FXML
    private Label lblStockDateWarn;
    @FXML
    private JFXComboBox<String> cmbTypeOfValueBook;
    @FXML
    private Label lblValueBookWarn;
    @FXML
    private JFXTextField txtUnitAmount;
    @FXML
    private Label lblUnitAmountWarn;
    @FXML
    private JFXTextField txtSerialFrom;
    @FXML
    private JFXTextField txtSerialTo;
    @FXML
    private Label lblFromWarn;
    @FXML
    private Label lblToWarn;
    @FXML
    private JFXButton btnReload;
    @FXML
    private JFXComboBox<String> cmbEntryType;
    @FXML
    private JFXComboBox<String> cmbEntryYear;
    @FXML
    private JFXComboBox<String> cmbEntryMonth;
    @FXML
    private JFXButton btnGetEntries;
    @FXML
    private AnchorPane paneTarget;
    @FXML
    private Spinner<?> spnTargYear;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblTargYearWarn;
    @FXML
    private JFXTextField txtTargetAmount;
    @FXML
    private Label lblTargetWarn;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnUpdateEntries;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private AnchorPane paneRevenueCollection;
    @FXML
    private JFXDatePicker entDatePckRevCol;
    @FXML
    private JFXComboBox<String> cmbRevenueItem;
    @FXML
    private JFXTextField txtRevenueAmount;
    @FXML
    private Label lblDatePckRevWarn;
    @FXML
    private Label lblRevItemWarn;
    @FXML
    private Label lblRevAmountWarn;
    @FXML
    private AnchorPane panePayment;
    @FXML
    private Spinner<?> spnPaymentYear;
    @FXML
    private Label lblYearPayment;
    @FXML
    private JFXTextField txtPaymentAmount;
    @FXML
    private JFXDatePicker entDatePckPayment;
    @FXML
    private JFXTextField txtPaymentGCR;
    @FXML
    private JFXComboBox<String> cmbCollectionMonth;
    @FXML
    private JFXComboBox<String> cmbPaymentType;
    @FXML
    private Label lblDatePayWarn;
    @FXML
    private Label lblPayGCRWarn;
    @FXML
    private Label lblPayAmtWarn;
    @FXML
    private Label lblColMonthWarn;
    @FXML
    private Label lblPayYearWarn;
    @FXML
    private Label lblPayTpeWarn;
    @FXML
    private AnchorPane paneCheque;
    @FXML
    private JFXComboBox<String> cmbGCR;
    @FXML
    private JFXDatePicker dtpckChequeDate;
    @FXML
    private JFXTextField txtChqNmb;
    @FXML
    private JFXTextField txtBankName;
    @FXML
    private JFXTextField txtChqAmount;
    @FXML
    private Label lblGCRChqWarn;
    @FXML
    private Label lblChequeDateWarn;
    @FXML
    private Label lblChqNmbWarn;
    @FXML
    private Label lblBankNameWarn;
    @FXML
    private Label lblChqAmtWarn;
    @FXML
    private Label lblEntryTypeWarn;
    @FXML
    private Label lblGetYearWarn;
    @FXML
    private Label lblGetMonthWarn;
    @FXML
    private AnchorPane paneNothing;
    @FXML
    private AnchorPane paneUpdateNothing;

    GetEntries getCollectionData, getCollectionReport;
    GetBankDetails getBankData, getBankReport;
    GetValueBooksEntries getValueData, getValueReport;
    GetCollectEnt getPaymentData, getPaymentReport;
    GetTargetEnt getTargetData, getTargetReport;
    GetFunctions getFunctions = new GetFunctions();

    entries_sideController app;
    GetFunctions getDates = new GetFunctions();
    private final GetRevCenter GetCenter;
    private final Connection con;
    private PreparedStatement stmnt;
    boolean targetCondition, collectionCondition, paymentCondition, bankCondition, valueBookCondition;
    String revCenter;

    public UpdateEntriesController(GetRevCenter getRevCenter) throws SQLException, ClassNotFoundException {
        this.GetCenter = getRevCenter;
        this.con = DBConnection.getConn();
        revCenter = GetCenter.getRevCenter();
    }

    public void setappController(entries_sideController app){
        this.app = app;
    }
    public entries_sideController getentries_sideController(){
        return app;
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbEntryType.setOnMouseClicked(e -> {
            lblEntryTypeWarn.setVisible(false);
        });
        cmbEntryYear.setOnMouseClicked(e -> {
            lblGetYearWarn.setVisible(false);
        });
        cmbEntryMonth.setOnMouseClicked(e -> {
            lblGetMonthWarn.setVisible(false);
        });
        txtTargetAmount.setOnMouseClicked(e -> {
            lblTargetWarn.setVisible(false);
        });
        if (!targetCondition){
            paneTarget.setVisible(false);
            tblTargetEntries.setVisible(false);
        }
        if (!bankCondition){
            paneCheque.setVisible(false);
            tblChequeDetails.setVisible(false);
        }
        if (!collectionCondition){
            paneRevenueCollection.setVisible(false);
            tblCollectionEntries.setVisible(false);
        }
        ObservableList<String> entryType = FXCollections.observableArrayList("Bank Details", "Payments",
                "Revenue Collection", "Revenue Target", "Value Books Stock");
        cmbEntryType.getItems().addAll(entryType);
    }

    @FXML
    void loadEntryYears(ActionEvent event) throws SQLException {
        ObservableList<String> entryYears = FXCollections.observableArrayList();
        String entryYear = "", entryTypeTable = "", acEntryYear = "";
         switch(cmbEntryType.getSelectionModel().getSelectedItem()){
             case "Bank Details":
                 entryTypeTable = "`cheque_details`";
                 entryYear = "`year`";
                 acEntryYear = "year";

                 break;
             case "Payments":
                 entryTypeTable = "`collection_payment_entries`";
                 entryYear = "`Year`";
                 acEntryYear = "Year";

                 break;
             case "Revenue Collection":
                 entryTypeTable = "`daily_entries`";
                 entryYear = "`revenueYear`";
                 acEntryYear = "revenueYear";
                 break;
             case "Revenue Target":
                 entryTypeTable = "`target_entries`";
                 entryYear = "`Year`";
                 acEntryYear = "Year";
                 break;
             case "Value Books Stock":
                 entryTypeTable = "`value_books_stock_record`";
                 entryYear = "`year`";
                 acEntryYear = "year";
                 break;
        }
        if (cmbEntryType.getSelectionModel().getSelectedItem().equals("Revenue Target")){
            cmbEntryMonth.setDisable(true);
        }else{
            cmbEntryMonth.setDisable(false);
        }
        stmnt = con.prepareStatement("SELECT "+entryYear+" FROM "+entryTypeTable+" WHERE `revCenter` =" +
                " '"+revCenter+"' GROUP  BY "+entryYear+"");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData rm = rs.getMetaData();
        while(rs.next()){
            String Year = rs.getString(acEntryYear);
            entryYears.add(Year);
        }
        cmbEntryYear.getItems().clear();
        cmbEntryYear.getItems().addAll(entryYears);
        System.out.println(entryTypeTable+"\n"+entryYear+"\n"+entryYears+"\n"+acEntryYear);
    }

    @FXML
    void loadEntryMonths(ActionEvent event) throws SQLException {
        ObservableList<String> entryMonths = FXCollections.observableArrayList();
        String entryYear = "", entryTypeTable = "", entryMonth = "", acEntryMonth = "";
        switch(cmbEntryType.getSelectionModel().getSelectedItem()){
            case "Bank Details":
                entryTypeTable = "`cheque_details`";
                entryYear = "`year`";
                entryMonth = "`month`";
                acEntryMonth = "month";
                break;
            case "Payments":
                entryTypeTable = "`collection_payment_entries`";
                entryYear = "`Year`";
                entryMonth = "`Month`";
                acEntryMonth = "Month";
                break;
            case "Revenue Collection":
                entryTypeTable = "`daily_entries`";
                entryYear = "`revenueYear`";
                acEntryMonth = "revenueMonth";
                entryMonth = "`revenueMonth`";
                break;
            case "Revenue Target":
                entryTypeTable = "`target_entries`";
                entryYear = "`Year`";
                break;
            case "Value Books Stock":
                entryTypeTable = "`value_books_stock_record`";
                entryYear = "`year`";
                acEntryMonth = "month";
                entryMonth = "`month`";
                break;
        }
        stmnt = con.prepareStatement("SELECT "+entryMonth+" FROM "+entryTypeTable+" WHERE `revCenter` = " +
                "'"+revCenter+"' AND "+entryYear+" = '"+cmbEntryYear.getSelectionModel().
                getSelectedItem()+"' GROUP  BY "+entryMonth+"");
        ResultSet rs = stmnt.executeQuery();
        ResultSetMetaData rm = rs.getMetaData();
        while(rs.next()){
            String Month = rs.getString(acEntryMonth);
            entryMonths.add(Month);
        }
        cmbEntryMonth.getItems().clear();
        cmbEntryMonth.getItems().addAll(entryMonths);
        System.out.println(entryTypeTable+"\n"+entryMonth+"\n"+entryMonth+"\n"+acEntryMonth);
    }

    @FXML
    private void onlyAmount(KeyEvent event) {
    }

    @FXML
    private void onlyNumbers(KeyEvent event) {
    }

    @FXML
    private void reloadAll(ActionEvent event) {
    }

    @FXML
    private void showEntries(ActionEvent event) throws SQLException {
        if (cmbEntryType.getSelectionModel().isEmpty()){
            lblEntryTypeWarn.setVisible(true);
        }else {
            switch (cmbEntryType.getSelectionModel().getSelectedItem()){
                case "Revenue Target":
                    targetCondition = true;
                    bankCondition = false;
                    valueBookCondition = false;
                    paymentCondition = false;
                    collectionCondition = false;
                    getTargetEntries();
                    break;
                case "Bank Details":
                    targetCondition = false;
                    bankCondition = true;
                    valueBookCondition = false;
                    paymentCondition = false;
                    collectionCondition = false;
                    break;

            }
        }
    }

    @FXML
    private void clearEntries(ActionEvent event) {
    }

    @FXML
    private void updateEntries(ActionEvent event) {
    }

    @FXML
    private void deleteSelection(ActionEvent event) {
    }

    void getTargetEntries() throws SQLException {
        paneNothing.setVisible(false);
        paneUpdateNothing.setVisible(false);
        if (targetCondition){
            paneTarget.setVisible(true);
            tblTargetEntries.setVisible(true);
        }
        lblTitle.setText("Target Entries");
        ResultSet rs;
        ResultSetMetaData rm;
        String center = "", amount = "", year = "";
        colTargCenter.setCellValueFactory(data -> data.getValue().CenterProperty());
        colTargAmount.setCellValueFactory(data -> data.getValue().AmountProperty());
        colTargYear.setCellValueFactory(data -> data.getValue().YearProperty());
        if (cmbEntryYear.getSelectionModel().isEmpty()) {
            stmnt = con.prepareStatement("SELECT  * FROM `target_entries` WHERE `revCenter` = '" + revCenter + "'");
        } else {
            stmnt = con.prepareStatement("SELECT  * FROM `target_entries` WHERE `revCenter` = '" + revCenter + "'" +
                    " AND `Year` = '" + cmbEntryYear.getSelectionModel().getSelectedItem() + "'");
        }
        rs = stmnt.executeQuery();
        rm = rs.getMetaData();
        while (rs.next()) {
            center = rs.getString("revCenter");
            amount = getFunctions.getAmount(rs.getString("Amount"));
            year = rs.getString("Year");
            getTargetReport = new GetTargetEnt(center, amount, year);
            tblTargetEntries.getItems().add(getTargetReport);
        }
    }

    void getBankDetails() throws SQLException {
        paneNothing.setVisible(false);
        paneUpdateNothing.setVisible(false);
        if (bankCondition){
            paneCheque.setVisible(true);
            tblChequeDetails.setVisible(true);
        }
        lblTitle.setText("Cheque Details");
        ResultSet rs;
        ResultSetMetaData rm;
        String GCR = "", Year = "", date = "", Month = "", chqDate = "", chqNumber = "", Bank = "", Amount = "",
        month = cmbEntryMonth.getSelectionModel().getSelectedItem();
        colBank.setCellValueFactory(d -> d.getValue().bankProperty());
        colChqAmount.setCellValueFactory(d -> d.getValue().amountProperty());
        colChqDate.setCellValueFactory(d -> d.getValue().chequeDateProperty());
        colChqGCR.setCellValueFactory(d -> d.getValue().GCRProperty());
        colChqNmb.setCellValueFactory(d -> d.getValue().chequeNumberProperty());
        colPayChqDate.setCellValueFactory(d -> d.getValue().dateProperty());
        if (cmbEntryYear.getSelectionModel().isEmpty()){
            stmnt = con.prepareStatement("SELECT * FROM `cheque_details` WHERE `revCenter` = '"+revCenter+"'");
        }else if (!cmbEntryYear.getSelectionModel().isEmpty() && cmbEntryMonth.getSelectionModel().isEmpty()){
            stmnt = con.prepareStatement("SELECT * FROM  `cheque_details` WHERE `revCenter` = '"+revCenter+"' AND " +
                    "`year` = '"+cmbEntryYear.getSelectionModel().getSelectedItem()+"'");
        }else{
            stmnt = con.prepareStatement("SELECT * FROM  `cheque_details` WHERE `revCenter` = '"+revCenter+"' AND " +
                    "`year` = '"+cmbEntryYear.getSelectionModel().getSelectedItem()+"' AND `month` = '"+month+"'");
        }
        rs = stmnt.executeQuery();
        while(rs.next()) {
                getBankReport = new GetBankDetails(GCR, Year, Month, date, chqDate, chqNumber, Bank, Amount);
        }
    }


    
}
