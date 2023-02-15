package com.Controller.Entries;

import com.Models.GetEntries;
import com.Models.GetFunctions;
import com.Models.GetRevCenter;
import com.Controller.LogInController;
import com.Enums.Months;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import com.revenue_report.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class commissionEntriesController implements Initializable {

    @FXML
    private AnchorPane anchPane;

    @FXML
    private AnchorPane topPane;

    @FXML
    private Label lblTitle;

    @FXML
    private TableView<GetEntries> commTable;

    @FXML
    private TableColumn<GetEntries, String> colDate;

    @FXML
    private TableColumn<GetEntries, String> colWeek;

    @FXML
    private TableColumn<GetEntries, String> colMonth;

    @FXML
    private TableColumn<GetEntries, String> colID;

    @FXML
    private TableColumn<GetEntries, String> colCCAmount;

    @FXML
    private Label lblDeleteWarn;

    @FXML
    private Label lblEdit;

    @FXML
    private Label lblDup;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private Button btnSaveEntries;

    @FXML
    private Button btnClearEntr;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private Label lblTot;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnEnter;

    @FXML
    private TextField txtEntCCAmt;

    @FXML
    private DatePicker entDatePck;
    @FXML
    private Label lblDateWarn;

    @FXML
    private Label lblAmountWarn;

    @FXML
    private JFXButton btnClearUpdate;

    @FXML
    private JFXButton btnUpdateEntries;

    @FXML
    private JFXButton btnDeleteUpdate;

    @FXML
    private JFXCheckBox chkUpdate;

    @FXML
    private ComboBox<String> cmbUpdateYear;

    @FXML
    private ComboBox<Months> cmbUpdateMonth;

    @FXML
    private JFXButton btnFetchUpdate;

    @FXML
    private Label lblControlWarn;
    private Revenue_EntriesController app;

    private GetEntries addEntries;

    private GetRevCenter GetCenter;
    private GetFunctions getFunctions = new GetFunctions();
    private String regex = "(?<=[\\d])(,)(?=[\\d])";
    private Pattern p = Pattern.compile(regex);
    private Matcher m;
    private DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private String RevCent, Date, Year, Qtr, Week, Month, entriesID, Amount;
    float amount;
    boolean Condition;
    private final Connection con;
    public commissionEntriesController(GetRevCenter GetCenter) throws SQLException, ClassNotFoundException {
        this.GetCenter = GetCenter;
        this.con = DBConnection.getConn();
    }

    private PreparedStatement stmnt;
    private ResultSet rs;
    private Map<String, String> registerAmount=new HashMap<>();
    public void setappController(Revenue_EntriesController app){
        this.app = app;
    }
    public Revenue_EntriesController getRevenue_EntriesController (){
        return app;
    }
    private Stage stage =  new Stage()/*anchBankDetails.getScene().getWindow()*/;
    public void setStage(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getFunctions.datePicker(entDatePck);
        RevCent = GetCenter.getCenterID();
        registerAmount.put("", "");
        entDatePck.setOnMouseClicked(e ->{
            lblDateWarn.setVisible(false);
        });
        txtEntCCAmt.setOnMouseClicked(e ->{
            lblAmountWarn.setVisible(false);
        });
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
          if (chkUpdate.isSelected()){
              cmbUpdateYear.setVisible(true);
              cmbUpdateMonth.setVisible(true);
              btnFetchUpdate.setVisible(true);
              btnClearUpdate.setVisible(true);
              btnDeleteUpdate.setVisible(true);
              btnUpdateEntries.setVisible(true);
              colID.setVisible(true);
              btnEnter.setVisible(false);
              btnClear.setVisible(false);
              btnClearEntr.setVisible(false);
              btnSaveEntries.setVisible(false);
              btnDelete.setVisible(false);
              lblTot.setVisible(false);
              lblTotalAmount.setVisible(false);
              commTable.getItems().clear();
              commTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
              clear();

          }else {
              cmbUpdateYear.setVisible(false);
              cmbUpdateMonth.setVisible(false);
              btnFetchUpdate.setVisible(false);
              btnClearUpdate.setVisible(false);
              btnDeleteUpdate.setVisible(false);
              btnUpdateEntries.setVisible(false);
              colID.setVisible(false);
              btnEnter.setVisible(true);
              btnClear.setVisible(true);
              btnClearEntr.setVisible(true);
              btnSaveEntries.setVisible(true);
              btnDelete.setVisible(true);
              lblTot.setVisible(true);
              lblTotalAmount.setVisible(true);
              commTable.getItems().clear();
              commTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
              clear();
          }
      });
        commTable.setOnMouseClicked(e ->{
            if (!chkUpdate.isSelected()){
                lblDeleteWarn.setVisible(false);lblDup.setVisible(false);lblEdit.setVisible(false);
            }else {
                if (commTable.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1){
                    setEntries();
                    if (lblControlWarn.isVisible()){
                        lblControlWarn.setVisible(false);
                    }
                }
            }
        });
        commTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

  private void GetRevenueYears() throws SQLException {
      stmnt = con.prepareStatement("SELECT YEAR(commission_date) AS `commission_year` FROM `commission_details` WHERE `commission_center` = '"+RevCent+"' GROUP BY `commission_year`");
      rs = stmnt.executeQuery();
      cmbUpdateYear.getItems().clear();
      while (rs.next()){
          cmbUpdateYear.getItems().add(rs.getString("commission_year"));
      }
  }

    @FXML
    void selectedYear(ActionEvent event) throws SQLException {
      String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
        stmnt = con.prepareStatement("SELECT MONTH(commission_date) AS `commission_month` FROM `commission_details` WHERE `commission_center` = '"+RevCent+"' AND YEAR(commission_date) ='"+year+"' GROUP BY `commission_month`");
        rs = stmnt.executeQuery();
        cmbUpdateMonth.getItems().clear();
        while (rs.next()){
            cmbUpdateMonth.getItems().add(Months.get(rs.getInt("commission_month")));
        }
    }

    @FXML
    void fetchEntries(ActionEvent event) throws SQLException {
        if (!cmbUpdateYear.getSelectionModel().isEmpty()) {
            String year = cmbUpdateYear.getSelectionModel().getSelectedItem(); Months month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
            if (!cmbUpdateMonth.getSelectionModel().isEmpty()) {
                stmnt = con.prepareStatement("SELECT `commission_ID`, `commission_date`, `commission_week`, `commission_amount`, MONTH(commission_date) AS `commission_month` FROM `commission_details` WHERE `commission_center` = '" + RevCent + "' AND YEAR(`commission_date`) ='" + year + "' AND MONTH(`commission_date`) = '" + month.getValue() + "' ");
            } else {
                stmnt = con.prepareStatement("SELECT `commission_ID`, `commission_date`, `commission_week`, `commission_amount`, MONTH(commission_date) AS `commission_month` FROM `commission_details` WHERE `commission_center` = '" + RevCent + "' AND YEAR(`commission_date`) ='" + year + "'");
            }
            rs = stmnt.executeQuery();
            commTable.getItems().clear();
            colID.setCellValueFactory(data -> data.getValue().IDProperty());
            colDate.setCellValueFactory(data -> data.getValue().DateProperty());
            colCCAmount.setCellValueFactory(data -> data.getValue().AmountProperty());
            colMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
            colWeek.setCellValueFactory(data -> data.getValue().WeekProperty());
            while (rs.next()) {
                addEntries = new GetEntries(rs.getString("commission_ID"), getFunctions.convertSqlDate(rs.getString("commission_date")), Months.get(rs.getInt("commission_month")).toString(), rs.getString("commission_week"), getFunctions.getAmount(rs.getString("commission_amount")));
                commTable.getItems().add(addEntries);

            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Please select Year");
            alert.showAndWait();
        }
    }

    private void setEntries() {
        GetEntries entries = commTable.getSelectionModel().getSelectedItem();
        entriesID = entries.getID();
        entDatePck.setValue(LocalDate.parse(entries.getDate(), format));
        m = p.matcher(entries.getAmount());
        String amount =  m.replaceAll("");
        txtEntCCAmt.setText(amount);
    }

    @FXML
    void updateEntries(ActionEvent event) throws SQLException {
        if (entriesID != null){
            String Date = getFunctions.getSqlDate(entDatePck.getValue()),
                    Year = getFunctions.getYear(entDatePck.getValue()),
                    Qtr = getFunctions.getQuarter(entDatePck.getValue()),
                    Week = getFunctions.getWeek(entDatePck.getValue()),
                    Month = getFunctions.getMonth(entDatePck.getValue());
            Condition = true;
            if(entDatePck.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please select Date");
                alert.showAndWait();
//              lblDatePckRevWarn.setVisible(true);
                Condition = false;
            }else {
                while(Condition) {
                    if(txtEntCCAmt.getText().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please Enter \"Amount\"");
                        alert.showAndWait();
//                      lblRevAmountWarn.setVisible(true);
                        Condition =false;
                    }else if(StringUtils.countMatches(txtEntCCAmt.getText(), ".") >1){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please Enter Amount");
                        alert.setContentText("Please check the number of '.' in the amount");
                        alert.showAndWait();
                        Condition =false;
                    }
                    else{
                        stmnt = con.prepareStatement("UPDATE `commission_details` SET  " +
                                "`commission_amount`= '"+Float.parseFloat(txtEntCCAmt.getText())+"'," +
                                "`commission_date` = '"+Date+"',`commission_week` = '"+Week+"' WHERE   " +
                                "`commission_ID`= '"+entriesID+"' AND `commission_center`= '"+RevCent+"'");
                        stmnt.executeUpdate();
                        clear();
                        loadRevenueCollectionTable();
                        Condition = false;
                    }
                }
            }
        }else{
            lblControlWarn.setVisible(true);
        }
    }

    void clear(){
        entriesID = null;
        entDatePck.setValue(null);
        txtEntCCAmt.setText(null);
    }

    @FXML
    void CancelEntries(ActionEvent event) {
        Node node =(Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?"
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        Button cancelButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.CANCEL
        );
        exitButton.setText("Yes");
        cancelButton.setText("No");
        closeConfirmation.setTitle("Abort Entries");
        closeConfirmation.setHeaderText("Confirm Exit");
        if(!commTable.getItems().isEmpty()) {
            closeConfirmation.setContentText("Are you sure you want to exit without saving commission details? ");
        }else{
            closeConfirmation.setContentText("Are you sure you want to Exit");
        }
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(stage);

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get()) || !closeResponse.isPresent()) {
            event.consume();
        }else{
            stage.close();
        }

    }

    public String getID(){
        java.util.Date day = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        String milli = Integer.toString(cal.get(Calendar.MILLISECOND)),
                date = Integer.toString(cal.get(Calendar.DATE)),
                month = Integer.toString(cal.get(Calendar.MONTH)+1),
                Day = Integer.toString(cal.get(Calendar.DAY_OF_WEEK)),
                year = Integer.toString(cal.get(Calendar.YEAR)),
                second = Integer.toString(cal.get(Calendar.SECOND)),
                minute = Integer.toString(cal.get(Calendar.MINUTE)),
                hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String id = UUID.randomUUID().toString(),
                is = id.replace("-", "").substring(9,12),
                ID = id.replace("-", "").substring(17,20),
                si = id.replace("-", "").substring(23,25),
                fini = is+ID+si.concat(Day).concat(date).concat(hour).concat(minute).concat(second).concat(milli);
        return fini;
    }

    @FXML
    void SaveToDatabase(ActionEvent event) throws SQLException {
        RevCent = GetCenter.getCenterID();
        float deduction = 0;
        GetEntries getData = new GetEntries();
        ObservableList<String> duplicate = FXCollections.observableArrayList();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (int i=0; i<=commTable.getItems().size(); i++){
            if (i != commTable.getItems().size()){
            getData = commTable.getItems().get(i);
            String acDate = getFunctions.setSqlDate(getData.getDate());/*
            LocalDate date = LocalDate.parse(acDate, dtf);
            String regex = "(?<=[\\d])(,)(?=[\\d])";*/
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(getData.getAmount());
            float ccAmount = Float.parseFloat(m.replaceAll(""));
            int week = Integer.parseInt(getData.getCode())/* year = Integer.parseInt(getFunctions.getYear(date)),
                qtr = Integer.parseInt(getFunctions.getQuarter(date))*/;
            String  month = getData.getItem(), fini = getID();
            PreparedStatement stmnt = con.prepareStatement("SELECT `commission_date` FROM `commission_details`" +
                    " WHERE `commission_center` = '"+RevCent+"'");
                ResultSet rs = stmnt.executeQuery();
                while (rs.next()){
                    duplicate.add(rs.getString("commission_date"));
                }
                if (duplicate.contains(acDate)){
                    lblDup.setText("Amount for "+'"'+acDate+'"'+ " already exist. Please delete or edit duplicate.");
                    lblDup.setVisible(true);
                    i = commTable.getItems().size() + 1;
            }else {
                    boolean condition = true;
                    ArrayList<String> dup = new ArrayList<>();
                    while (condition) {
                        stmnt = con.prepareStatement("SELECT `commission_ID` FROM `commission_details` WHERE `commission_ID` = " +
                                "'" + fini + "' ");
                        ResultSet rt = stmnt.executeQuery();
                        while (rt.next()) {
                            dup.add(rt.getString("commission_ID"));
                        }
                        if (dup.contains(fini)) {
                            fini = getID();
                        } else {
                            condition = false;
                        }
                    }
                    deduction += ccAmount;
                    stmnt = con.prepareStatement("INSERT INTO `commission_details`(`commission_ID`, " +
                            "`commission_center`, `commission_amount`, `commission_date`, `commission_week`, `commission_rate`) VALUES ('"+fini+"', '"+RevCent+"', '"+ccAmount
                            +"', '"+acDate+"', '"+week+"')");
                    stmnt.executeUpdate();
            }
        }else {
                amount -= deduction;
                commTable.getItems().clear();
                lblTotalAmount.setText(getFunctions.getAmount(Float.toString(amount)));
            }
    }
    }

    @FXML
    void clearEntries(ActionEvent event) {
        if (!chkUpdate.isSelected()){
        if (commTable.getSelectionModel().isEmpty()){
            lblDeleteWarn.setVisible(true);
        }else{
            String regex = "(?<=[\\d])(,)(?=[\\d])";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(commTable.getSelectionModel().getSelectedItem().getAmount());
            entDatePck.setValue(LocalDate.parse(commTable.getSelectionModel().getSelectedItem().getDate(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            txtEntCCAmt.setText(m.replaceAll(""));
            ObservableList<GetEntries> selectedRows = commTable.getSelectionModel().getSelectedItems();
            ArrayList<GetEntries> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> commTable.getItems().remove(row));
        }
        }else{
            clear();
        }
    }

    private void loadRevenueCollectionTable() throws SQLException {
        String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
        Months month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
        if (!cmbUpdateMonth.getSelectionModel().isEmpty()) {
            stmnt = con.prepareStatement("SELECT `commission_ID`, `commission_date`, `commission_week`, `commission_amount`, MONTH(commission_date) AS `commission_month` FROM `commission_details` WHERE `commission_center` = '" + RevCent + "' AND YEAR(commission_date) ='" + year + "' AND MONTH(commission_date) = '" + month.getValue() + "' ");
        } else {
            stmnt = con.prepareStatement("SELECT `commission_ID`, `commission_date`, `commission_week`, `commission_amount`, MONTH(commission_date) AS `commission_month` FROM `commission_details` WHERE `commission_center` = '" + RevCent + "' AND YEAR(commission_date) ='" + year + "'");
        }
        rs = stmnt.executeQuery();
        commTable.getItems().clear();
        colID.setCellValueFactory(data -> data.getValue().IDProperty());
        colDate.setCellValueFactory(data -> data.getValue().DateProperty());
        colCCAmount.setCellValueFactory(data -> data.getValue().AmountProperty());
        colMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
        colWeek.setCellValueFactory(data -> data.getValue().WeekProperty());
        while (rs.next()) {
            addEntries = new GetEntries(rs.getString("commission_ID"), getFunctions.convertSqlDate(rs.getString("commission_date")), Months.get(rs.getInt("commission_month")).toString(), rs.getString("commission_week"), getFunctions.getAmount(rs.getString("commission_amount")));
            commTable.getItems().add(addEntries);

        }
    }

    @FXML
    void deleteSelection(ActionEvent event) throws SQLException {
        if (!chkUpdate.isSelected()) {
            if (commTable.getSelectionModel().isEmpty()) {
                lblDeleteWarn.setVisible(true);
            } else {
                ObservableList<GetEntries> selectedRows = commTable.getSelectionModel().getSelectedItems();
                ArrayList<GetEntries> rows = new ArrayList<>(selectedRows);
                rows.forEach(row -> commTable.getItems().remove(row));
            }
        }else{
            if (entriesID != null){
                stmnt = con.prepareStatement("DELETE FROM `commission_details` WHERE `commission_ID` = '"+entriesID+"'");
                stmnt.executeUpdate();
                clear();
                loadRevenueCollectionTable();
            }else{
                lblControlWarn.setVisible(true);
            }
        }
    }

    @FXML
    void processKeyEvent(KeyEvent event) {
        String c = event.getCharacter();
        if("1234567890.".contains(c)) {}
        else {
            event.consume();
        }
    }

    @FXML
    void saveEntries(ActionEvent event) {
        String ccAmount = txtEntCCAmt.getText();
        LocalDate date = entDatePck.getValue();
        if (entDatePck.getValue() == null) {
            lblDateWarn.setVisible(true);
            Condition = false;
        } else {
            Condition = true;
            Date = getFunctions.getDate(date);
            Week = getFunctions.getWeek(date);
            Month = getFunctions.getMonth(date);

            while (Condition) {
                if (txtEntCCAmt.getText().isEmpty()){
                    lblAmountWarn.setVisible(true);
                    Condition = false;
                }
                else if (registerAmount.containsKey(Date)){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Amount for "+Date+" has already been entered");
                    alert.showAndWait();
                    Condition =false;
                }
                else if (txtEntCCAmt.getText().startsWith(".") && txtEntCCAmt.getText().endsWith(".")){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Please Enter Amount");
                    alert.showAndWait();
                    Condition =false;
                }else if(StringUtils.countMatches(txtEntCCAmt.getText(), ".") >1){
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Incorrect amount format");
                    alert.setContentText("Please check the number of '.' in the amount");
                    alert.showAndWait();
                    Condition =false;
                }else{
                    colCCAmount.setCellValueFactory(d -> d.getValue().AmountProperty());
                    colDate.setCellValueFactory(d -> d.getValue().DateProperty());
                    colMonth.setCellValueFactory(d -> d.getValue().ItemProperty());
                    colWeek.setCellValueFactory(d -> d.getValue().CodeProperty());
                    Amount= getFunctions.getAmount(ccAmount);
                    if("0.00".equals(Amount)){
                        Alert alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please Amount cannot be '0'");
                        alert.showAndWait();
                        Condition =false;
                    }else{
                        amount += Float.parseFloat(ccAmount);
                        lblTotalAmount.setText(getFunctions.getAmount(Float.toString(amount)));
                        addEntries = new GetEntries(Week, Month, Date, Amount);
                        commTable.getItems().add(addEntries);
                        if (!registerAmount.containsKey(Date)){
                            registerAmount.put(Date, ccAmount);
                        }
                        entDatePck.setValue(null);
                        txtEntCCAmt.setText(null);
                        Condition = false;
                    }
                }
            }
        }
    }
}