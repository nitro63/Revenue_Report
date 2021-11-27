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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.Controller.Gets.GetFunctions;
import com.Enums.Months;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.StringUtils;
import com.Controller.Gets.GetEntries;
import com.Controller.Gets.GetRevCenter;
import com.revenue_report.AutoCompleteComboBox;
import com.revenue_report.DBConnection;
import com.revenue_report.Main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * FXML com.Controller class
 *
 * @author NiTrO
 */
public class Revenue_EntriesController implements Initializable {

    @FXML
    private AnchorPane DailyEntPane;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private Label lblControlWarn;
    @FXML
    private Label lblTot;
    @FXML
    private DatePicker entDatePck;
    @FXML
    private ComboBox<String> cmbEntRevItem;
    @FXML
    private TextField txtEntAmt;
    @FXML
    private TableView<GetEntries> revTable;
    @FXML
    private TableColumn<GetEntries, String> revCode;
    @FXML
    private TableColumn<GetEntries, String> revItem;
    @FXML
    private TableColumn<GetEntries, String> revAmount;
    @FXML
    private TableColumn<GetEntries, String> revDate;
    @FXML
    private Text lblEdit;
    @FXML
    private Text lblDup;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private Text lblDeleteWarn;

    @FXML
    private JFXCheckBox chkUpdate;

    @FXML
    private ComboBox<String> cmbUpdateYear;

    @FXML
    private ComboBox<Months> cmbUpdateMonth;

    @FXML
    private JFXButton btnFetchUpdate;

    @FXML
    private ComboBox<String> cmbUpdateDate;

    @FXML
    private JFXButton btnClearUpdate;

    @FXML
    private JFXButton btnUpdateEntries;

    @FXML
    private JFXButton btnDeleteUpdate;
    private entries_sideController app;
    private GetFunctions getFunctions = new GetFunctions();
    private String regex = "(?<=[\\d])(,)(?=[\\d])";
    private Pattern p = Pattern.compile(regex);
    private Matcher m;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * initializes the controller class.
     * 
     * @param app
     */

    private Map<String, String> RevenueMap = new HashMap<>();
    private ObservableList<String> RevenueItems = FXCollections.observableArrayList();
    private ObservableList<String> ItemsCode = FXCollections.observableArrayList(RevenueMap.values());
    private Map<String, ArrayList<String>> registerItem = new HashMap<>();

    private PreparedStatement stmnt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private final Connection con;
    private boolean Condition = true, ccCheck;
    private float totAmount = 0;

    private String Date, Item, Code, Month, Amount, CCAmount, Week, Year, RevCent, entriesID, Qtr, totalAmount;
    // private static final int JANUARY = 1, FEBRUARY = 2;

    @FXML
    private Button btnEnter;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnSaveEntries;
    @FXML
    private JFXButton btnComm;
    @FXML
    private Button btnClearEntr;

    private GetEntries addEntries = new GetEntries();

    private final GetRevCenter GetCenter;
    @FXML
    private Button btnClose;

    public Revenue_EntriesController(GetRevCenter GetCenter) throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
        this.GetCenter = GetCenter;
    }

    public void setappController(entries_sideController app) {
        this.app = app;
    }

    public entries_sideController getentries_sideController() {
        return app;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            GetRevenueYears();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        chkUpdate.setVisible(LogInController.Accountant || LogInController.OverAllAdmin);
        chkUpdate.setOnMouseClicked(event -> {
            try {
                GetRevenueYears();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (chkUpdate.isSelected()) {
                cmbUpdateYear.setVisible(true);
                cmbUpdateMonth.setVisible(true);
                cmbUpdateDate.setVisible(true);
                btnFetchUpdate.setVisible(true);
                revDate.setText("ID");
                revCode.setText("Date");
                btnEnter.setVisible(false);
                btnClear.setVisible(false);
                btnClearEntr.setVisible(false);
                btnSaveEntries.setVisible(false);
                btnDelete.setVisible(false);
                btnClearUpdate.setVisible(true);
                btnDeleteUpdate.setVisible(true);
                btnUpdateEntries.setVisible(true);
                lblTot.setVisible(false);
                lblTotalAmount.setVisible(false);
                revTable.getItems().clear();
                revTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                clear();

            } else {
                cmbUpdateYear.setVisible(false);
                cmbUpdateMonth.setVisible(false);
                btnFetchUpdate.setVisible(false);
                cmbUpdateDate.setVisible(false);
                revDate.setText("Date");
                revCode.setText("Code");
                btnEnter.setVisible(true);
                btnClear.setVisible(true);
                btnClearEntr.setVisible(true);
                btnSaveEntries.setVisible(true);
                btnDelete.setVisible(true);
                btnClearUpdate.setVisible(false);
                btnDeleteUpdate.setVisible(false);
                btnUpdateEntries.setVisible(false);
                lblTot.setVisible(true);
                lblTotalAmount.setVisible(true);
                revTable.getItems().clear();
                revTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                clear();
            }
        });

        RevCent = GetCenter.getCenterID();
        String cent = GetCenter.centerIDProperty().getValue();
        registerItem.put("", new ArrayList<>());
        revTable.setOnMouseClicked(e -> {
            if (!chkUpdate.isSelected()) {
                lblDeleteWarn.setVisible(false);
                lblDup.setVisible(false);
                lblEdit.setVisible(false);
            } else {
                if (revTable.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1) {
                    setEntries();
                    if (lblControlWarn.isVisible()) {
                        lblControlWarn.setVisible(false);
                    }
                }
            }
        });

        revTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        try {
            GetRevenueItems();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (app.getRevGroup().getSelectionModel().getSelectedItem().toString().toLowerCase(Locale.ROOT)
                .equals("sub-metros")) {
            btnComm.setVisible(true);
            ccCheck = true;
        } else {
            btnComm.setVisible(false);
            ccCheck = false;
        }
        cmbEntRevItem.setEditable(true);
    }

    private void GetRevenueYears() throws SQLException {
        stmnt = con.prepareStatement(
                "SELECT YEAR(`revenueDate`) AS `revenueYear` FROM `daily_entries` WHERE `daily_revCenter` = '" + RevCent
                        + "' GROUP BY `revenueYear`");
        rs = stmnt.executeQuery();
        cmbUpdateYear.getItems().clear();
        while (rs.next()) {
            cmbUpdateYear.getItems().add(rs.getString("revenueYear"));
        }
    }

    private void GetRevenueItems() throws SQLException {
        stmnt = con.prepareStatement(
                "SELECT `item_Sub`, `assign_item`, `item_category` FROM `center_items`, `revenue_items` WHERE `assign_center` = '"
                        + RevCent + "' AND `revenue_item_ID` = `assign_item`");
        rs = stmnt.executeQuery();
        while (rs.next()) {
            RevenueItems.add(rs.getString("item_Sub"));
            RevenueMap.put(rs.getString("item_Sub"), rs.getString("assign_item"));
        }
        Collections.sort(RevenueItems);
        cmbEntRevItem.getItems().clear();
        cmbEntRevItem.getItems().addAll(RevenueItems);
        new AutoCompleteComboBox<String>(cmbEntRevItem);
    }

    private void setEntries() {
        GetEntries entries = revTable.getSelectionModel().getSelectedItem();
        entriesID = entries.getDate();
        entDatePck.setValue(LocalDate.parse(entries.getCode(), format));
        cmbEntRevItem.getSelectionModel().select(entries.getItem());
        m = p.matcher(entries.getAmount());
        String amount = m.replaceAll("");
        txtEntAmt.setText(amount);
    }

    @FXML
    private void selectedYear(ActionEvent event) throws SQLException {
        String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
        stmnt = con.prepareStatement(
                "SELECT MONTH(`revenueDate`) AS `revenueMonth` FROM `daily_entries` WHERE `daily_revCenter` = '"
                        + RevCent + "' AND YEAR(`revenueDate`) ='" + year + "' GROUP BY `revenueMonth`");
        rs = stmnt.executeQuery();
        cmbUpdateMonth.getItems().clear();
        while (rs.next()) {
            cmbUpdateMonth.getItems().add(Months.get(rs.getInt("revenueMonth")));
        }
    }

    @FXML
    void LoadDates(ActionEvent event) throws SQLException {
        String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
        Months month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
        stmnt = con.prepareStatement(
                "SELECT DAY(`revenueDate`) AS `revenueDay` FROM `daily_entries` WHERE `daily_revCenter` = '" + RevCent
                        + "' AND YEAR(`revenueDate`) ='" + year + "' AND MONTH(`revenueDate`) = '" + month.getValue()
                        + "' GROUP BY `revenueDay`");
        rs = stmnt.executeQuery();
        cmbUpdateDate.getItems().clear();
        while (rs.next()) {
            cmbUpdateDate.getItems().add(rs.getString("revenueDay"));
        }
    }

    @FXML
    private void fetchEntries(ActionEvent event) throws SQLException {
        if (!cmbUpdateYear.getSelectionModel().isEmpty()) {
            String year = cmbUpdateYear.getSelectionModel().getSelectedItem(),
                    day = cmbUpdateDate.getSelectionModel().getSelectedItem();
            Months month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
            if (!cmbUpdateMonth.getSelectionModel().isEmpty() && cmbUpdateDate.getSelectionModel().isEmpty()) {
                stmnt = con.prepareStatement(
                        "SELECT `entries_ID`, `item_Sub`, `revenueDate`, `revenueAmount` FROM `daily_entries`, `revenue_items` WHERE `daily_revCenter` = '"
                                + RevCent + "' AND `revenueItem` = `revenue_item_ID` AND YEAR(`revenueDate`) ='" + year
                                + "' AND MONTH(`revenueDate`) = '" + month.getValue() + "' ");
            } else if (!cmbUpdateMonth.getSelectionModel().isEmpty() && cmbUpdateDate.getSelectionModel().isEmpty()) {
                stmnt = con.prepareStatement(
                        "SELECT `entries_ID`, `item_Sub`, `revenueDate`, `revenueAmount` FROM `daily_entries`, `revenue_items` WHERE `daily_revCenter` = '"
                                + RevCent + "' AND `revenueItem` = `revenue_item_ID` AND YEAR(`revenueDate`) ='" + year
                                + "' AND MONTH(`revenueDate`) = '" + month.getValue() + "' AND DAY(`revenueDate`) = '"
                                + day + "'");
            } else {
                stmnt = con.prepareStatement(
                        "SELECT `entries_ID`, `item_Sub`, `revenueDate`, `revenueAmount` FROM `daily_entries`, `revenue_items` WHERE `daily_revCenter` = '"
                                + RevCent + "' AND `revenueItem` = `revenue_item_ID` AND YEAR(`revenueDate`) ='" + year
                                + "'");
            }
            rs = stmnt.executeQuery();
            revTable.getItems().clear();
            revCode.setCellValueFactory(data -> data.getValue().CodeProperty());
            revItem.setCellValueFactory(data -> data.getValue().ItemProperty());
            revAmount.setCellValueFactory(data -> data.getValue().AmountProperty());
            revDate.setCellValueFactory(data -> data.getValue().DateProperty());
            while (rs.next()) {
                addEntries = new GetEntries(rs.getString("revenueDate"), rs.getString("item_Sub"),
                        rs.getString("entries_ID"), getFunctions.getAmount(rs.getString("revenueAmount")));
                revTable.getItems().add(addEntries);

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Please select Year");
            alert.showAndWait();
        }
    }

    private void loadRevenueCollectionTable() throws SQLException {
        String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
        Months month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
        if (!cmbUpdateMonth.getSelectionModel().isEmpty()) {
            stmnt = con.prepareStatement(
                    "SELECT `entries_ID`, `item_Sub`, `revenueDate`, `revenueAmount` FROM `daily_entries`, `revenue_items` WHERE `daily_revCenter` = '"
                            + RevCent + "' AND `revenueItem` = `revenue_item_ID` AND `revenueYear` ='" + year
                            + "' AND `revenueMonth` = '" + month + "' ");
        } else {
            stmnt = con.prepareStatement(
                    "SELECT `entries_ID`, `item_Sub`, `revenueDate`, `revenueAmount` FROM `daily_entries`, `revenue_items` WHERE `daily_revCenter` = '"
                            + RevCent + "' AND `revenueItem` = `revenue_item_ID` AND `revenueYear` ='" + year + "'");
        }
        rs = stmnt.executeQuery();
        revTable.getItems().clear();
        revCode.setCellValueFactory(data -> data.getValue().CodeProperty());
        revItem.setCellValueFactory(data -> data.getValue().ItemProperty());
        revAmount.setCellValueFactory(data -> data.getValue().AmountProperty());
        revDate.setCellValueFactory(data -> data.getValue().DateProperty());
        while (rs.next()) {
            addEntries = new GetEntries(rs.getString("revenueDate"), rs.getString("item_Sub"),
                    rs.getString("entries_ID"), getFunctions.getAmount(rs.getString("revenueAmount")));
            revTable.getItems().add(addEntries);
        }
    }

    @FXML
    private void saveEntries(ActionEvent event) throws IOException, SQLException {
        if (!chkUpdate.isSelected()) {
            LocalDate date = entDatePck.getValue();// Assigning the date picker value to a variable
            // Making sure Datepicker is never empty :)
            if (entDatePck.getValue() == null) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Date");
                alert.showAndWait();
                Condition = false;
            } else {
                Condition = true;
                Date = getFunctions.getDate(date);
                while (Condition) {
                    String i = cmbEntRevItem.getSelectionModel().getSelectedItem();
                    if (registerItem.containsKey(Date) && registerItem.get(Date).contains(i)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Revenue Item already entered");
                        alert.showAndWait();
                        Condition = false;
                    } else if (cmbEntRevItem.getSelectionModel().isEmpty()) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Select Revenue Item");
                        alert.showAndWait();

                        Condition = false;
                    } else if (txtEntAmt.getText().isEmpty()) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please enter Amount");
                        alert.showAndWait();
                        Condition = false;
                    } else if (txtEntAmt.getText().isEmpty() || cmbEntRevItem.getSelectionModel().isEmpty()) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please Make All Entries");
                        alert.showAndWait();
                        Condition = false;
                    } else if (txtEntAmt.getText().startsWith(".") && txtEntAmt.getText().endsWith(".")) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please Enter Amount");
                        alert.showAndWait();
                        Condition = false;
                    } else if (StringUtils.countMatches(txtEntAmt.getText(), ".") > 1) {
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Incorrect amount format");
                        alert.setContentText("Please check the number of '.' in the amount");
                        alert.showAndWait();
                        Condition = false;
                    } else {
                        revCode.setCellValueFactory(data -> data.getValue().CodeProperty());
                        revItem.setCellValueFactory(data -> data.getValue().ItemProperty());
                        revAmount.setCellValueFactory(data -> data.getValue().AmountProperty());
                        revDate.setCellValueFactory(data -> data.getValue().DateProperty());
                        Amount = getFunctions.getAmount(txtEntAmt.getText());
                        if ("0.00".equals(Amount)) {
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("Please Amount cannot be '0'");
                            alert.showAndWait();
                            txtEntAmt.clear();
                            Condition = false;
                        } else {
                            Code = RevenueMap.get(cmbEntRevItem.getSelectionModel().getSelectedItem());// Getting the
                                                                                                       // Code of the
                                                                                                       // Revenue Items
                            Item = cmbEntRevItem.getSelectionModel().getSelectedItem();// Assigning Selected Revenue
                                                                                       // Item to the Item Variable
                            totAmount += Float.parseFloat(txtEntAmt.getText());
                            totalAmount = getFunctions.getAmount(Float.toString(totAmount));
                            lblTotalAmount.setText(totalAmount);
                            addEntries = new GetEntries(Code, Item, Date, /* Month, */ Amount/* , Week, Year, Qtr */);
                            revTable.getItems().add(addEntries);
                            if (!registerItem.containsKey(Date)) {
                                registerItem.put(Date, new ArrayList<>());
                                registerItem.get(Date).add(i);
                            } else if (registerItem.containsKey(Date) && !registerItem.get(Date).contains(i)) {
                                registerItem.get(Date).add(i);
                            }

                            // registerItem.add(cmbEntRevItem.getSelectionModel().getSelectedIndex());

                            cmbEntRevItem.getSelectionModel().clearSelection();
                            txtEntAmt.clear();
                            Condition = false;
                        }
                    }
                }
            }
        } else {
            if (entriesID != null) {
                ArrayList<String> dupItem = new ArrayList<>();
                String Date = getFunctions.getDate(entDatePck.getValue()),
                        Year = getFunctions.getYear(entDatePck.getValue()),
                        Qtr = getFunctions.getQuarter(entDatePck.getValue()),
                        Week = getFunctions.getWeek(entDatePck.getValue()),
                        Month = getFunctions.getMonth(entDatePck.getValue());
                String code = RevenueMap.get(cmbEntRevItem.getSelectionModel().getSelectedItem());
                stmnt = con.prepareStatement("SELECT `revenueItem` FROM `daily_entries` WHERE  `daily_revCenter` = '"
                        + RevCent + "' AND `entries_ID` != '" + entriesID + "' AND `revenueItem` = '" + code
                        + "' AND `revenueDate` = '" + Date + "' ");/* AND `entries_ID` != '"+entriesID+"' */
                ResultSet rt = stmnt.executeQuery();
                while (rt.next()) {
                    dupItem.add(rt.getString("revenueItem"));
                }
                Condition = true;
                if (entDatePck.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Please select Date");
                    alert.showAndWait();
                    // lblDatePckRevWarn.setVisible(true);
                    Condition = false;
                } else {
                    while (Condition) {
                        String i = cmbEntRevItem.getSelectionModel().getSelectedItem();
                        if (dupItem.contains(i)) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("This record already exists");
                            alert.showAndWait();
                            Condition = false;
                        } else if (cmbEntRevItem.getValue().isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("Please select \"Revenue Item\"");
                            alert.showAndWait();
                            // lblRevItemWarn.setVisible(true);
                            Condition = false;
                        } else if (txtEntAmt.getText().isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("Please Enter \"Amount\"");
                            alert.showAndWait();
                            // lblRevAmountWarn.setVisible(true);
                            Condition = false;
                        } else if (StringUtils.countMatches(txtEntAmt.getText(), ".") > 1) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("Please Enter Amount");
                            alert.setContentText("Please check the number of '.' in the amount");
                            alert.showAndWait();
                            Condition = false;
                        } else {
                            stmnt = con.prepareStatement("UPDATE `daily_entries` SET  " + "`revenueAmount`= '"
                                    + Float.parseFloat(txtEntAmt.getText()) + "',`revenueYear`= '" + Year + "',"
                                    + "`revenueDate` = '" + Date + "', `revenueItem` = '" + code + "'"
                                    + ",`revenueWeek` = '" + Week + "', `revenueMonth` = '" + Month
                                    + "', `revenueQuarter` = '" + Qtr + "' WHERE   " + "`entries_ID`= '" + entriesID
                                    + "' AND `daily_revCenter`= '" + RevCent + "'");
                            stmnt.executeUpdate();
                            clear();
                            loadRevenueCollectionTable();
                            Condition = false;
                        }
                    }
                }
            } else {
                lblControlWarn.setVisible(true);
            }
        }
    }

    @FXML
    private void clearEntries(ActionEvent event) {
        if (!chkUpdate.isSelected()) {
            GetEntries entries = revTable.getSelectionModel().getSelectedItem();
            if (revTable.getSelectionModel().isEmpty()) {
                lblEdit.setText("Please select a row in the table to " + '"' + "Edit" + '"');
                lblEdit.setVisible(true);
            } else {/*
                     * String regex = "(?<=[\\d])(,)(?=[\\d])"; p = Pattern.compile(regex);
                     */
                m = p.matcher(entries.getAmount());
                entDatePck.setValue(LocalDate.parse(entries.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                cmbEntRevItem.getSelectionModel().select(entries.getItem());
                txtEntAmt.setText(m.replaceAll(""));
                totAmount -= Float.parseFloat(m.replaceAll(""));
                totalAmount = getFunctions.getAmount(Float.toString(totAmount));
                lblTotalAmount.setText(totalAmount);
                registerItem.get(entries.getDate()).remove(entries.getItem());
                ObservableList<GetEntries> selectedRows = revTable.getSelectionModel().getSelectedItems();
                ArrayList<GetEntries> rows = new ArrayList<>(selectedRows);
                rows.forEach(row -> revTable.getItems().remove(row));
            }
        } else {
            clear();
        }
    }

    @FXML
    private void SaveToDatabase(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {
        float deduction = 0;
        GetEntries getData = new GetEntries();
        ObservableList<String> duplicate = FXCollections.observableArrayList();
        List<List<String>> arrList = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i <= revTable.getItems().size(); i++) {
            if (i != revTable.getItems().size()) {
                getData = revTable.getItems().get(i);
                String acDate = getData.getDate();
                LocalDate date = LocalDate.parse(acDate, dtf);
                String sqlDate = date.format(dtf2);
                String acCode = getData.getCode();
                String acItem = getData.getCode();
                String acQtr = getFunctions.getQuarter(date);
                String CcAmt;
                String regex = "(?<=[\\d])(,)(?=[\\d])";
                Pattern p = Pattern.compile(regex);
                String amount = getData.getAmount();
                Matcher m = p.matcher(amount);
                float acAmount = Float.parseFloat(m.replaceAll(""));
                int acWeek = Integer.parseInt(getFunctions.getWeek(date));
                String acMonth = getFunctions.getMonth(date);
                int acYear = Integer.parseInt(getFunctions.getYear(date));
                stmnt = con.prepareStatement("SELECT * FROM `daily_entries` WHERE `revenueItem` = '" + acCode + "'"
                        + " AND `revenueDate` = '" + sqlDate + "' AND `daily_revCenter` = '" + RevCent + "' ");
                rs = stmnt.executeQuery();
                while (rs.next()) {
                    String dup = rs.getString("revenueItem");
                    duplicate.add(dup);
                }
                if (duplicate.contains(acCode)) {
                    lblDup.setText("Revenue for " + '"' + getData.getItem() + '"' + " on " + '"' + getData.getDate()
                            + '"' + " already exist. Please delete or edit duplicate.");
                    lblDup.setVisible(true);
                    i = revTable.getItems().size() + 1;
                } else {
                    deduction += acAmount;
                    stmnt = con.prepareStatement("INSERT INTO `daily_entries`(`daily_revCenter`, "
                            + "`revenueItem`, `revenueAmount`, `revenueDate`) VALUES('" + RevCent + "'," + " '" + acItem
                            + "', '" + acAmount + "',  '" + sqlDate + "'");
                    stmnt.executeUpdate();
                    registerItem.clear();

                }
            } else {
                totAmount -= deduction;
                revTable.getItems().clear();
                lblTotalAmount.setText(getFunctions.getAmount(Float.toString(totAmount)));
            }
        }
    }

    private void clear() {
        entriesID = null;
        entDatePck.setValue(null);
        cmbEntRevItem.getSelectionModel().clearSelection();
        cmbEntRevItem.setValue(null);
        txtEntAmt.setText(null);
    }

    @FXML
    private void CancelEntries(ActionEvent event) throws SQLException {
        if (!chkUpdate.isSelected()) {
            registerItem.clear();
            revTable.getItems().clear();
        } else {
            if (entriesID != null) {
                stmnt = con.prepareStatement("DELETE FROM `daily_entries` WHERE `entries_ID` = '" + entriesID + "'");
                stmnt.executeUpdate();
                clear();
                loadRevenueCollectionTable();
            } else {
                lblControlWarn.setVisible(true);
            }
        }
    }

    @FXML
    private void processKeyEvent(KeyEvent event) {
        String c = event.getCharacter();
        if ("1234567890.".contains(c)) {
        } else {
            event.consume();
        }
    }

    @FXML
    private void showClose(ActionEvent event) {
        getentries_sideController().getappController().getCenterPane().getChildren().clear();
    }

    public void deleteSelection(ActionEvent actionEvent) {
        GetEntries entries = revTable.getSelectionModel().getSelectedItem();
        if (revTable.getSelectionModel().isEmpty()) {
            lblDeleteWarn.setVisible(true);
        } else {
            /*
             * String regex = "(?<=[\\d])(,)(?=[\\d])"; Pattern p = Pattern.compile(regex);
             * Matcher
             */m = p.matcher(entries.getAmount());
            totAmount -= Float.parseFloat(m.replaceAll(""));
            totalAmount = getFunctions.getAmount(Float.toString(totAmount));
            lblTotalAmount.setText(totalAmount);
            ObservableList<GetEntries> selectedRows = revTable.getSelectionModel().getSelectedItems();
            ArrayList<GetEntries> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> revTable.getItems().remove(row));
            registerItem.get(entries.getDate()).remove(entries.getItem());
        }

    }

    @FXML
    void showCommission(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        Main st = new Main();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/commissionEntries.fxml"));
        loader.setController(new commissionEntriesController(GetCenter));
        commissionEntriesController bnkDtls = loader.getController();
        bnkDtls.setappController(this);
        Parent root = loader.load();
        Scene s = new Scene(root);
        Stage stg = new Stage();
        bnkDtls.setStage(stg);
        stg.initModality(Modality.APPLICATION_MODAL);
        stg.initOwner(st.stage);
        stg.initStyle(StageStyle.UTILITY);
        stg.setScene(s);
        stg.show();
    }

}
