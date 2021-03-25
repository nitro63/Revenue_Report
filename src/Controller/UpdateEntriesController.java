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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.collections.map.ReferenceMap;
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
public class UpdateEntriesController implements Initializable {

    @FXML
    private TableView<GetValueBooksReport> tblValueBookStocks;
    @FXML
    private TableColumn<GetValueBooksReport, String> colStckID;
    @FXML
    private TableColumn<GetValueBooksReport, String> colStckMonth;
    @FXML
    private TableColumn<GetValueBooksReport, String> colStckDATE;
    @FXML
    private TableColumn<GetValueBooksReport, String> colStckTypeVB;
    @FXML
    private TableColumn<GetValueBooksReport, String> colSerialFrom;
    @FXML
    private TableColumn<GetValueBooksReport, String> colSerialTo;
    @FXML
    private TableColumn<GetValueBooksReport, String> colStckQuantity;
    @FXML
    private TableColumn<GetValueBooksReport, String> colStckAmtVal;
    @FXML
    private TableColumn<GetValueBooksReport, String> colStckCumuAmt;
    @FXML
    private TableColumn<GetValueBooksReport, String> colStckPurAmt;
    @FXML
    private TableColumn<GetValueBooksReport, String> colRemarks;
    @FXML
    private TableView<GetTargetEnt> tblTargetEntries;
    @FXML
    private TableColumn<GetTargetEnt, String> colTargAmount;
    @FXML
    private TableColumn<GetTargetEnt, String> colTargID;
    @FXML
    private TableColumn<GetTargetEnt, String> colTargYear;
    @FXML
    private TableView<GetEntries> tblCollectionEntries;
    @FXML
    private TableColumn<GetEntries, String> colColID;
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
    private JFXDatePicker entValDatePck;
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
    private Spinner<Integer> spnTargYear;
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
    private Spinner<Integer> spnPaymentYear;
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
    private Label lblControlWarn;
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
    GetValueBooksReport getValueData, getValueReport;
    GetCollectEnt getPaymentData, getPaymentReport;
    GetTargetEnt getTargetData, getTargetReport;
    GetFunctions getFunctions = new GetFunctions();

    entries_sideController app;
    GetFunctions getDates = new GetFunctions();
    private final GetRevCenter GetCenter;
    private final Connection con;
    private PreparedStatement stmnt;
    boolean targetCondition, collectionCondition, paymentCondition, bankCondition, valueBookCondition;
    String revCenter, entryTypeMonth, entryTypeYear, oldTargetAmount,  oldTargetyear, entry_ID;
    float newTargetAmount;
    int newTargetYear;
    String regex = "(?<=[\\d])(,)(?=[\\d])";
    Pattern p = Pattern.compile(regex);
    Matcher m, match, mac;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate date;
    Map<Float, String> priceBook = new HashMap<>();
    Map<String, String> codeItem = new HashMap<>();
    ObservableList<String> Item = FXCollections.observableArrayList();

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
        try {
            stmnt = con.prepareStatement("SELECT * FROM `revenue_items`");
            ResultSet rs = stmnt.executeQuery();
            while (rs.next()){
                Item.add(rs.getString("revenue_item"));
                codeItem.put(rs.getString("ID"), rs.getString("revenue_item"));
            }
            cmbRevenueItem.getItems().clear();
            cmbRevenueItem.setItems(Item);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

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
        Calendar cal = Calendar.getInstance();
        spnTargYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2090, cal.get(Calendar.YEAR)));
        spnPaymentYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2090, cal.get(Calendar.YEAR)));
        tblCollectionEntries.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblPaymentEntries.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblValueBookStocks.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblChequeDetails.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tblTargetEntries.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<String> entryType = FXCollections.observableArrayList("Bank Details", "Payments Entries",
                "Revenue Entries", "Revenue Target", "Value Books Stock");
        cmbEntryType.getItems().addAll(entryType);
        tblValueBookStocks.setOnMouseClicked(e -> {
            if (tblValueBookStocks.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1){
                setValueBooksEntries();
            }
            if (lblControlWarn.isVisible()){
                lblControlWarn.setVisible(false);
            }
        });
        tblCollectionEntries.setOnMouseClicked(e ->{
            if (tblCollectionEntries.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1){
                setRevenueCollection();
            }
            if (lblControlWarn.isVisible()){
                lblControlWarn.setVisible(false);
            }
        });
        tblTargetEntries.setOnMouseClicked(e ->{
            if (tblTargetEntries.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1){
                setTargetEntries();
            }
            if (lblControlWarn.isVisible()){
                lblControlWarn.setVisible(false);
            }
        });
    }

    @FXML
    void loadEntryYears(ActionEvent event) throws SQLException {
        getYear();
    }

    void getYear() throws SQLException {
        ObservableList<String> entryYears = FXCollections.observableArrayList();
        String entryYear = "", entryTypeTable = "", acEntryYear = "";
        switch(cmbEntryType.getSelectionModel().getSelectedItem()){
            case "Bank Details":
                entryTypeTable = "`cheque_details`";
                entryYear = "`year`";
                acEntryYear = "year";

                break;
            case "Payments Entries":
                entryTypeTable = "`collection_payment_entries`";
                entryYear = "`Year`";
                acEntryYear = "Year";

                break;
            case "Revenue Entries":
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
        entryTypeYear = cmbEntryYear.getSelectionModel().getSelectedItem();
        ObservableList<String> entryMonths = FXCollections.observableArrayList();
        String entryYear = "", entryTypeTable = "", entryMonth = "", acEntryMonth = "";
        switch (cmbEntryType.getSelectionModel().getSelectedItem()) {
            case "Bank Details":
                entryTypeTable = "`cheque_details`";
                entryYear = "`year`";
                entryMonth = "`month`";
                acEntryMonth = "month";
                break;
            case "Payments Entries":
                entryTypeTable = "`collection_payment_entries`";
                entryYear = "`Year`";
                entryMonth = "`Month`";
                acEntryMonth = "Month";
                break;
            case "Revenue Entries":
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
        if (cmbEntryType.getSelectionModel().getSelectedItem() == "Revenue Target") {
        } else {
            stmnt = con.prepareStatement("SELECT " + entryMonth + " FROM " + entryTypeTable + " WHERE `revCenter` = " +
                    "'" + revCenter + "' AND " + entryYear + " = '" + cmbEntryYear.getSelectionModel().
                    getSelectedItem() + "' GROUP  BY " + entryMonth + "");
            ResultSet rs = stmnt.executeQuery();
            ResultSetMetaData rm = rs.getMetaData();
            int col = rs.getFetchSize();
            while (rs.next()) {
                String Month = rs.getString(acEntryMonth);
                System.out.println(Month);
                entryMonths.add(Month);
            }
            cmbEntryMonth.getItems().clear();
            cmbEntryMonth.getItems().addAll(entryMonths);
            System.out.println(entryTypeTable + "\n" + entryMonths + "\n" + entryMonth + "\n" + acEntryMonth + "\n" + rm + "\n" + col);
        }
    }

    @FXML
    private void onlyAmount(KeyEvent event) {
        String c = event.getCharacter();
        if("1234567890.".contains(c)) {}
        else {
            event.consume();
        }
    }

    @FXML
    private void onlyNumbers(KeyEvent event) {
    }

    @FXML
    private void reloadAll(ActionEvent event) {
    }

    void toggleViews(){

        if (!targetCondition){
            paneTarget.setVisible(false);
            tblTargetEntries.setVisible(false);
        }else {
            paneTarget.setVisible(true);
            tblTargetEntries.setVisible(true);
        }
        if (!bankCondition){
            paneCheque.setVisible(false);
            tblChequeDetails.setVisible(false);
        }else {
            paneCheque.setVisible(true);
            tblChequeDetails.setVisible(true);
        }
        if (!collectionCondition){
            paneRevenueCollection.setVisible(false);
            tblCollectionEntries.setVisible(false);
        }else{
            paneRevenueCollection.setVisible(true);
            tblCollectionEntries.setVisible(true);
        }
        if (!valueBookCondition){
            paneValueBooks.setVisible(false);
            tblValueBookStocks.setVisible(false);
        }else {
            paneValueBooks.setVisible(true);
            tblValueBookStocks.setVisible(true);
        }
        if (!paymentCondition){
            panePayment.setVisible(false);
            tblPaymentEntries.setVisible(false);
        }else {
            panePayment.setVisible(true);
            tblPaymentEntries.setVisible(true);
        }
    }

    @FXML
    private void showEntries(ActionEvent event) throws SQLException, FileNotFoundException, JRException {
        entry_ID = null;
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
                    if (cmbEntryYear.getSelectionModel().isEmpty()){
                        lblGetYearWarn.setVisible(true);
                    }else{
                    targetCondition = false;
                    bankCondition = true;
                    valueBookCondition = false;
                    paymentCondition = false;
                    collectionCondition = false;
                    getBankDetails();
                    }
                    break;
                case "Value Books Stock":
                    if (cmbEntryYear.getSelectionModel().isEmpty()){
                        lblGetYearWarn.setVisible(true);
                    }else{
                        targetCondition = false;
                        bankCondition = false;
                        valueBookCondition = true;
                        paymentCondition = false;
                        collectionCondition = false;
                        getValueBooks();
                    }
                    break;
                case "Revenue Entries":
                    if (cmbEntryYear.getSelectionModel().isEmpty()){
                        lblGetYearWarn.setVisible(true);
                    }else{
                        targetCondition = false;
                        bankCondition = false;
                        valueBookCondition = false;
                        paymentCondition = false;
                        collectionCondition = true;
                        getRevenueCollection();
                    }
                    break;
                case "Payments Entries":
                    if (cmbEntryYear.getSelectionModel().isEmpty()){
                        lblGetYearWarn.setVisible(true);
                    }else{
                        targetCondition = false;
                        bankCondition = false;
                        valueBookCondition = false;
                        paymentCondition = true;
                        collectionCondition = false;
                        getPayment();
                    }
                    break;


            }
        }
    }

    @FXML
    private void clearEntries(ActionEvent event) {
        if (paneTarget.isVisible()) {
            txtTargetAmount.setText(null);
            entry_ID = null;
        }else if (paneRevenueCollection.isVisible()){
            resetRevenueCollection();
        }
    }

    @FXML
    private void updateEntries(ActionEvent event) throws SQLException, FileNotFoundException, JRException {
        if (entry_ID != null) {
            if (paneTarget.isVisible()) {
                updateTarget();
            }else if (paneRevenueCollection.isVisible()){
                updateRevenueCollection(event);
            }else if (paneValueBooks.isVisible()){
                updateValueBooksEntries();
            }
        }else{
            lblControlWarn.setVisible(true);
        }
    }

    @FXML
    private void deleteSelection(ActionEvent event) throws SQLException, FileNotFoundException, JRException {
        if (entry_ID != null) {
            if (paneTarget.isVisible()) {
                deleteTarget();
            } else if(paneRevenueCollection.isVisible()){
                deleteRevenueCollection();
            } else if (paneValueBooks.isVisible()){
                deleteValueBooksEntries();
            }
        }else{
            lblControlWarn.setVisible(true);
        }
    }

    void getTargetEntries() throws SQLException {
        paneNothing.setVisible(false);
        paneUpdateNothing.setVisible(false);
        txtTargetAmount.setText(null);
        toggleViews();
        lblTitle.setText("Revenue Target Entries");
        loadTargetTable();
    }

    void loadTargetTable() throws SQLException {
        tblTargetEntries.getItems().clear();
        txtTargetAmount.setText(null);
        ResultSet rs;
        ResultSetMetaData rm;
        String center = "", amount = "", year = "";
        colTargAmount.setCellValueFactory(data -> data.getValue().AmountProperty());
        colTargYear.setCellValueFactory(data -> data.getValue().YearProperty());
        colTargID.setCellValueFactory(data -> data.getValue().IDProperty());
        if (cmbEntryYear.getSelectionModel().isEmpty()) {
            stmnt = con.prepareStatement("SELECT  * FROM `target_entries` WHERE `revCenter` = '" + revCenter + "'");
        } else {
            stmnt = con.prepareStatement("SELECT  * FROM `target_entries` WHERE `revCenter` = '" + revCenter + "'" +
                    " AND `Year` = '" + cmbEntryYear.getSelectionModel().getSelectedItem() + "'");
        }
        rs = stmnt.executeQuery();
        rm = rs.getMetaData();
        while (rs.next()) {
            amount = getFunctions.getAmount(rs.getString("Amount"));
            year = rs.getString("Year");
            getTargetReport = new GetTargetEnt(rs.getString("target_ID"), center, amount, year);
            tblTargetEntries.getItems().add(getTargetReport);
        }}

    void getBankDetails() throws SQLException {
        paneNothing.setVisible(false);
        paneUpdateNothing.setVisible(false);
        cmbGCR.getSelectionModel().clearSelection();
        dtpckChequeDate.setValue(null);
        txtChqAmount.setText(null);
        txtBankName.setText(null);
        txtChqNmb.setText(null);
        toggleViews();
        lblTitle.setText("Cheque Details");
        loadBankDetailsTable();
    }

    void loadBankDetailsTable() throws SQLException {
        tblChequeDetails.getItems().clear();
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
        if (cmbEntryMonth.getSelectionModel().isEmpty()){
            stmnt = con.prepareStatement("SELECT * FROM  `cheque_details` WHERE `revCenter` = '"+revCenter+"' AND " +
                    "`year` = '"+cmbEntryYear.getSelectionModel().getSelectedItem()+"'");
        }else{
            stmnt = con.prepareStatement("SELECT * FROM  `cheque_details` WHERE `revCenter` = '"+revCenter+"' AND " +
                    "`year` = '"+cmbEntryYear.getSelectionModel().getSelectedItem()+"' AND `month` = '"+month+"'");
        }
        rs = stmnt.executeQuery();
        while(rs.next()) {
            GCR = rs.getString("gcr");
            date = rs.getString("date");
            chqDate = rs.getString("cheque_date");
            chqNumber = rs.getString("cheque_number");
            Bank = rs.getString("bank");
            Amount = getFunctions.getAmount(rs.getString("amount"));
            getBankReport = new GetBankDetails(GCR, date, chqDate, chqNumber, Bank, Amount);
            tblChequeDetails.getItems().add(getBankReport);
        }}

    void getValueBooks() throws SQLException {
        paneNothing.setVisible(false);
        paneUpdateNothing.setVisible(false);
        entValDatePck.setValue(null);
        cmbTypeOfValueBook.getSelectionModel().clearSelection();
        txtUnitAmount.setText(null);
        txtSerialFrom.setText(null);
        txtSerialTo.setText(null);
        toggleViews();
        lblTitle.setText("Value Books Stock Record");
        loadValueBooksTable();
    }

    void loadValueBooksTable() throws SQLException {
        tblValueBookStocks.getItems().clear();
        ResultSet rs;
        String Year, Month = "", Date = "", typeOfValBk = "", firstSerial = "", lastSerial = "", Quantity = "",
                valAmount = "", cumuAmount = "", purAmount = "", remarks = "",
                month = cmbEntryMonth.getSelectionModel().getSelectedItem();
        float cumuAmounts = 0;

        stmnt = con.prepareStatement("SELECT * FROM `value_books_details`");
        ResultSet res = stmnt.executeQuery();
        cmbTypeOfValueBook.getItems().clear();
        while (res.next()){
            cmbTypeOfValueBook.getItems().add(res.getString("value_books"));
            priceBook.put(res.getFloat("price"), res.getString("value_books"));
        }
        colStckAmtVal.setCellValueFactory(d -> d.getValue().valAmountProperty());
        colStckDATE.setCellValueFactory(d -> d.getValue().dateProperty());
        colSerialFrom.setCellValueFactory(d -> d.getValue().firstSerialProperty());
        colSerialTo.setCellValueFactory(d -> d.getValue().lastSerialProperty());
        colStckMonth.setCellValueFactory(d -> d.getValue().monthProperty());
        colStckPurAmt.setCellValueFactory(d -> d.getValue().purAmountProperty());
        colStckQuantity.setCellValueFactory(d -> d.getValue().quantityProperty());
        colStckTypeVB.setCellValueFactory(d -> d.getValue().valueBookProperty());
        colStckID.setCellValueFactory(d -> d.getValue().IDProperty());
        colRemarks.setCellValueFactory(d -> d.getValue().remarksProperty());
        if (cmbEntryMonth.getSelectionModel().isEmpty()){
            stmnt = con.prepareStatement("SELECT * FROM `value_books_stock_record` WHERE `revCenter` = '"+revCenter+"'" +
                    " AND `year` = '"+cmbEntryYear.getSelectionModel().getSelectedItem()+"'");
        }else{
            stmnt = con.prepareStatement("SELECT * FROM `value_books_stock_record` WHERE `revCenter` = '"+revCenter+"'" +
                    " AND `year` = '"+cmbEntryYear.getSelectionModel().getSelectedItem()+"' AND `month` = '"+month+"'");
        }
        rs = stmnt.executeQuery();
        while (rs.next()){
            Month = rs.getString("month");
            Date = rs.getString("date");
            firstSerial = rs.getString("first_serial");
            lastSerial = rs.getString("last_serial");
            Quantity = rs.getString("quantity");
            typeOfValBk = rs.getString("value_book");
            valAmount = getFunctions.getAmount(rs.getString("amount"));
            purAmount = getFunctions.getAmount(rs.getString("purchase_amount"));
            remarks = rs.getString("remarks");
            getValueReport = new GetValueBooksReport(Month, rs.getString("ID"), Date, typeOfValBk, firstSerial,
                    lastSerial, Quantity, valAmount, purAmount, remarks);
            tblValueBookStocks.getItems().add(getValueReport);
        }}

    void getRevenueCollection() throws SQLException, FileNotFoundException, JRException {
        paneNothing.setVisible(false);
        paneUpdateNothing.setVisible(false);
        entDatePckRevCol.setValue(null);
        cmbRevenueItem.getSelectionModel().clearSelection();
        txtRevenueAmount.setText(null);
        toggleViews();
        lblTitle.setText("Revenue Entries");
        loadRevenueCollectionTable();
    }

    void loadRevenueCollectionTable() throws SQLException, FileNotFoundException, JRException {
        tblCollectionEntries.getItems().clear();
        ResultSet rs;
        String Code = "", Item = "", Date = "", Month = "", Amount = "", Week = "", Year = "", Qtr = "",
                entryTypeYear = cmbEntryYear.getSelectionModel().getSelectedItem(),
                entryTypeMonth = cmbEntryMonth.getSelectionModel().getSelectedItem();
        colColID.setCellValueFactory(d -> d.getValue().CenterProperty());
        colItemCode.setCellValueFactory(d -> d.getValue().CodeProperty());
        colRevItem.setCellValueFactory(d -> d.getValue().ItemProperty());
        colRevDate.setCellValueFactory(d -> d.getValue().DateProperty());
        colRevAmt.setCellValueFactory(d -> d.getValue().AmountProperty());
        colRevMonth.setCellValueFactory(d -> d.getValue().MonthProperty());
        colRevWeek.setCellValueFactory(d -> d.getValue().WeekProperty());
        colRevQuarter.setCellValueFactory(d -> d.getValue().QuarterProperty());
        colRevYear.setCellValueFactory(d -> d.getValue().YearProperty());
        if (cmbEntryMonth.getSelectionModel().isEmpty()){
            stmnt = con.prepareStatement("SELECT * FROM `daily_entries` WHERE  `revenueYear` = '"+entryTypeYear+"' " +
                    "AND `revCenter` = '"+revCenter+"'");
        }else{
            stmnt = con.prepareStatement("SELECT * FROM `daily_entries` WHERE  `revenueYear` = '"+entryTypeYear+"'" +
                    " AND `revenueMonth` = '"+entryTypeMonth+"' AND `revCenter` = '"+revCenter+"'");
        }
        rs = stmnt.executeQuery();
        while (rs.next()){
            Code = rs.getString("Code");
            Item = rs.getString("revenueItem");
            Date = rs.getString("revenueDate");
            Month = rs.getString("revenueMonth");
            Week = rs.getString("revenueWeek");
            Year = rs.getString("revenueYear");
            Qtr = rs.getString("revenueYear");
            Amount = getFunctions.getAmount(rs.getString("revenueAmount"));
            getCollectionData = new GetEntries(Code, rs.getString("entries_ID"), Item, Date, Month, Amount, Week, Year, Qtr);
            tblCollectionEntries.getItems().add(getCollectionData);
        }

        List<GetEntries> items = new ArrayList<GetEntries>();
        for (int j = 0; j< tblCollectionEntries.getItems().size(); j++){
            getCollectionReport = new GetEntries();
            getCollectionReport = tblCollectionEntries.getItems().get(j);
            items.add(getCollectionReport);
        }
        URL url = this.getClass().getResource("/Assets/kmalogo.png");
        System.out.println(items+"\n"+url);
        JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(items);

        /* Map to hold Jasper report Parameters */
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("CollectionBean", itemsJRBean);
        parameters.put("logo", url);

        //read jrxml file and creating jasperdesign object
        InputStream input = new FileInputStream(new File("J:\\Project\\Intelli\\copy\\5698\\Revenue_Report\\src\\Assets\\Test2_A4.jrxml"));

        JasperDesign jasperDesign = JRXmlLoader.load(input);

        /*compiling jrxml with help of JasperReport class*/
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        /* Using jasperReport object to generate PDF */
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

        /*call jasper engine to display report in jasperviewer window*/
        JasperViewer.viewReport(jasperPrint, false);
    }

    void getPayment() throws SQLException{
        paneUpdateNothing.setVisible(false);
        paneNothing.setVisible(false);
        entDatePckPayment.setValue(null);
        txtPaymentGCR.setText(null);
        txtPaymentAmount.setText(null);
        cmbCollectionMonth.getSelectionModel().clearSelection();
        cmbPaymentType.getSelectionModel().clearSelection();
        lblTitle.setText("Payment Entries");
        toggleViews();
        loadPaymentTable();
    }

    void loadPaymentTable() throws SQLException {
        tblPaymentEntries.getItems().clear();
        ResultSet rs;
        String Amount = "", GCR = "", Month = "", Date = "", Year = "", Type = "",
                entryTypeMonth = cmbEntryMonth.getSelectionModel().getSelectedItem(),
                entryTypeYear = cmbEntryYear.getSelectionModel().getSelectedItem();
        colColPayMonth.setCellValueFactory(d -> d.getValue().MonthProperty());
        colPayAmount.setCellValueFactory(d -> d.getValue().AmountProperty());
        colPayDATE.setCellValueFactory(d -> d.getValue().DateProperty());
        colPayGCR.setCellValueFactory(d -> d.getValue().GCRProperty());
        colPayType.setCellValueFactory(d -> d.getValue().TypeProperty());
        colPayYear.setCellValueFactory(d -> d.getValue().YearProperty());
        if (cmbEntryMonth.getSelectionModel().isEmpty()) {
            stmnt = con.prepareStatement("SELECT * FROM `collection_payment_entries` WHERE" +
                    "`Year` = '"+entryTypeYear+"'AND `revCenter` = '"+revCenter+"'");
        }else{
            stmnt = con.prepareStatement("SELECT * FROM `collection_payment_entries` WHERE" +
                    "`Year` = '"+entryTypeYear+"' AND `Month` = '"+entryTypeMonth+"'AND `revCenter` = '"+revCenter+"'");
        }
        rs = stmnt.executeQuery();
        while (rs.next()){
            Amount = getFunctions.getAmount(rs.getString("Amount"));
            GCR = rs.getString("GCR");
            Month = rs.getString("Month");
            Year = rs.getString("Year");
            Date = rs.getString("Date");
            Type = rs.getString("payment_type");
            getPaymentData = new GetCollectEnt(Amount, GCR, Month, Date, Year, Type);
            tblPaymentEntries.getItems().add(getPaymentData);
        }
    }

    void setTargetEntries(){
        GetTargetEnt target = tblTargetEntries.getSelectionModel().getSelectedItem();
        m = p.matcher(target.getAmount());
        Integer year = Integer.parseInt(target.getYear());
        txtTargetAmount.setText( m.replaceAll(""));
        spnTargYear.getValueFactory().setValue(year);
        entry_ID = target.getID();
    }

    void deleteTarget() throws SQLException {
        stmnt = con.prepareStatement("DELETE FROM `target_entries` WHERE `target_ID` = '"+entry_ID+"'");
        stmnt.executeUpdate();
        txtTargetAmount.setText(null);
        entry_ID = null;
        loadTargetTable();
    }

    void updateTarget() throws SQLException {
        newTargetYear = spnTargYear.getValue();
        Matcher m = p.matcher(txtTargetAmount.getText());
        newTargetAmount = Float.parseFloat(m.replaceAll(""));

        stmnt = con.prepareStatement("UPDATE `target_entries` SET `revCenter`= '"+revCenter+"', " +
                "`Amount`= '"+newTargetAmount+"',`Year`= '"+newTargetYear+"' WHERE   `target_ID`= '"+entry_ID+"' ");
        stmnt.executeUpdate();
        txtTargetAmount.setText(null);
        entry_ID = null;
        loadTargetTable();
    }

    void setRevenueCollection(){
        GetEntries entries = tblCollectionEntries.getSelectionModel().getSelectedItem();
        entry_ID = entries.getCenter();
        date = LocalDate.parse(entries.getDate(), format);
        entDatePckRevCol.setValue(date);
        cmbRevenueItem.getSelectionModel().select(entries.getItem());
        m = p.matcher(entries.getAmount());
        txtRevenueAmount.setText(m.replaceAll(""));
    }

    void deleteRevenueCollection() throws SQLException, FileNotFoundException, JRException {
        stmnt = con.prepareStatement("DELETE FROM `daily_entries` WHERE `entries_ID` = '"+entry_ID+"'");
        stmnt.executeUpdate();
        resetRevenueCollection();
        loadRevenueCollectionTable();
    }

    void updateRevenueCollection(ActionEvent e) throws SQLException, FileNotFoundException, JRException {
        String Date = getFunctions.getDate(entDatePckRevCol.getValue()),
        Year = getFunctions.getYear(entDatePckRevCol.getValue()),
        Qtr = getFunctions.getQuarter(entDatePckRevCol.getValue()),
        Week = getFunctions.getWeek(entDatePckRevCol.getValue()),
        Month = getFunctions.getMonth(entDatePckRevCol.getValue());
        stmnt = con.prepareStatement("UPDATE `daily_entries` SET `revCenter`= '"+revCenter+"', " +
                "`revenueAmount`= '"+Float.parseFloat(txtRevenueAmount.getText())+"',`revenueYear`= '"+Year+"'," +
                "`revenueDate` = '"+Date+"', `revenueItem` = '"+cmbRevenueItem.getSelectionModel().getSelectedItem()+"'" +
                ",`revenueWeek` = '"+Week+"', `revenueMonth` = '"+Month+"', `revenueQuarter` = '"+Qtr+"' WHERE   " +
                "`entries_ID`= '"+entry_ID+"' ");
        stmnt.executeUpdate();
        resetRevenueCollection();
        loadRevenueCollectionTable();

  /*      Node node =(Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob.showPrintDialog(stage) && printerJob.printPage(tblCollectionEntries))
        {
            printerJob.endJob();
            System.out.println("printed");
        }*/
    }

    void resetRevenueCollection() {
        entry_ID = null;
        date = null;
        entDatePckRevCol.setValue(null);
        cmbRevenueItem.getSelectionModel().clearSelection();
        txtRevenueAmount.setText(null);
    }

    void setValueBooksEntries(){
        GetValueBooksReport valueBooks = tblValueBookStocks.getSelectionModel().getSelectedItem();
        entry_ID = valueBooks.getID();
        mac = p.matcher(valueBooks.getPurAmount());
        String amount = "";
        if (mac.matches()){
             amount = mac.replaceAll("");
        }else {
            amount = valueBooks.getPurAmount();
        }
        float price = Float.parseFloat(amount);
        int quantity = Integer.parseInt(valueBooks.getQuantity());
        date = LocalDate.parse(valueBooks.getDate(), format);
        entValDatePck.setValue(date);
        cmbTypeOfValueBook.getSelectionModel().select(valueBooks.getValueBook());
        float unitAmount = price / quantity;
        txtUnitAmount.setText(Float.toString(unitAmount));
        txtSerialFrom.setText(valueBooks.getFirstSerial());
        txtSerialTo.setText(valueBooks.getLastSerial());
    }

    void deleteValueBooksEntries() throws SQLException {
        stmnt = con.prepareStatement("DELETE FROM `value_books_stock_record` WHERE `ID` = '"+entry_ID+"' ");
        stmnt.executeUpdate();
        loadTargetTable();
        resetValueBooksTable();
    }

    void updateValueBooksEntries() throws SQLException {
        String  lastSerial = txtSerialTo.getText(),
                firstSerial = txtSerialFrom.getText(),
                Quantity, typeOfValBk = cmbTypeOfValueBook.getSelectionModel().getSelectedItem(),
                purAmount, valAmount,
                Month = getFunctions.getMonth(entValDatePck.getValue()),
                Date = getFunctions.getDate(entValDatePck.getValue());
        float amount = 0, cumuamount = 0, puramount = 0;
        int serialChecker = (Integer.parseInt(lastSerial) - Integer.parseInt(firstSerial)) + 1;
        int quantity = ((serialChecker) / 100);
        if ((serialChecker) < 100 || serialChecker % 100 != 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Please check Serials");
            alert.showAndWait();
        } else {
            Quantity = Integer.toString(quantity);
            for (Map.Entry<Float, String> calValAmount : priceBook.entrySet()){
                if (calValAmount.getValue().equals(typeOfValBk)){
                    amount = quantity * calValAmount.getKey() * 100;
                }
            }
            puramount = (Float.parseFloat(txtUnitAmount.getText()) * quantity);
            purAmount = getFunctions.getAmount(Float.toString(puramount));
            valAmount = getFunctions.getAmount(Float.toString((amount)));
            if ("0.00".equals(purAmount)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Amount cannot be '0'");
                alert.showAndWait();
                txtUnitAmount.clear();
            } else {
                stmnt = con.prepareStatement("UPDATE `value_books_details` SET `date` = '"+Date+"'," +
                        " `month` = '"+Month+"', `value_book` = '"+typeOfValBk+"', `first_serial` = '"+firstSerial+"'," +
                        "`last_serial` = '"+lastSerial+"', `quantity` = '"+Quantity+"', `amount` = '"+valAmount+"'," +
                        "`purchase_amount` = '"+purAmount+"', `remarks` = 'Updated'");
                stmnt.executeUpdate();
                loadValueBooksTable();
                resetValueBooksTable();
            }
        }
    }

    void resetValueBooksTable(){
        entry_ID = null;
        date = null;
        entValDatePck.setValue(null);
        cmbTypeOfValueBook.getSelectionModel().clearSelection();
        txtUnitAmount.setText(null);
        txtSerialFrom.setText(null);
        txtSerialTo.setText(null);
    }


    
}

