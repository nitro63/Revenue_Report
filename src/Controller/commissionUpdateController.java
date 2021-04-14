package Controller;
import Controller.Gets.GetEntries;
import Controller.Gets.GetFunctions;
import Controller.Gets.GetRevCenter;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang3.StringUtils;
import revenue_report.DBConnection;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class commissionUpdateController implements Initializable {

    @FXML
    private AnchorPane anchPane;

    @FXML
    private AnchorPane topPane;

    @FXML
    private Label lblTitle;

    @FXML
    private TableView<GetEntries> commTable;

    @FXML
    private TableColumn<GetEntries, String> colID;
    @FXML
    private TableColumn<GetEntries, String> colDate;

    @FXML
    private TableColumn<GetEntries, String> colWeek;

    @FXML
    private TableColumn<GetEntries, String> colMonth;

    @FXML
    private TableColumn<GetEntries, String> colQuarter;

    @FXML
    private TableColumn<GetEntries, String> colYear;

    @FXML
    private TableColumn<GetEntries, String> colCCAmount;

    @FXML
    private Label lblTotalAmount;

    @FXML
    private TextField txtEntCCAmt;

    @FXML
    private DatePicker entDatePck;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnUpdateEntries;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private Label lblDateWarn;

    @FXML
    private Label lblAmountWarn;
    @FXML
    private Label lblControlWarn;
    UpdateEntriesController app;

    GetEntries addEntries, getCollectionData;

    GetRevCenter GetCenter;
    GetFunctions getFunctions = new GetFunctions();
    String regex = "(?<=[\\d])(,)(?=[\\d])";
    Pattern p = Pattern.compile(regex);
    Matcher m, match, mac;
    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    LocalDate date;
    String RevCent, Date, Year, Qtr, Week, Month, Amount, entryID;
    float amount;
    boolean Condition;
    private final Connection con;
    private PreparedStatement stmnt;
    public commissionUpdateController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }

    Map<String, String> registerAmount=new HashMap<>();
    public void setappController(UpdateEntriesController app){
        this.app = app;
    }
    public UpdateEntriesController getRevenue_EntriesController (){
        return app;
    }
    Stage stage =  new Stage()/*anchBankDetails.getScene().getWindow()*/;
    public void setStage(Stage stage){
        this.stage = stage;
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadRevenueCollectionTable();
        } catch (SQLException | FileNotFoundException | JRException throwables) {
            throwables.printStackTrace();
        }
        commTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        commTable.setOnMouseClicked(e ->{
            if (commTable.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1){
                setCommissionUpdate();
            }
            if (lblControlWarn.isVisible()){
                lblControlWarn.setVisible(false);
            }
        });
    }


    void loadRevenueCollectionTable() throws SQLException, FileNotFoundException, JRException {
        commTable.getItems().clear();
        ResultSet rs;
        String ID = "", Date = "", Month = "", Amount = "", Week = "", Year = "", Qtr = "",
                entryTypeYear = app.getEntryYear(),
                entryTypeMonth = app.getEntryMonth(), revCenter = app.getRevCenter();
        colID.setCellValueFactory(d -> d.getValue().IDProperty());
        colDate.setCellValueFactory(d -> d.getValue().DateProperty());
        colCCAmount.setCellValueFactory(d -> d.getValue().AmountProperty());
        colMonth.setCellValueFactory(d -> d.getValue().MonthProperty());
        colWeek.setCellValueFactory(d -> d.getValue().WeekProperty());
        colQuarter.setCellValueFactory(d -> d.getValue().QuarterProperty());
        colYear.setCellValueFactory(d -> d.getValue().YearProperty());
        if (app.getEntryMonth() == null){
            stmnt = con.prepareStatement("SELECT * FROM `commission_details` WHERE  `commission_year` = '"+entryTypeYear+"' " +
                    "AND `commission_center` = '"+revCenter+"'");
        }else{
            stmnt = con.prepareStatement("SELECT * FROM `commission_details` WHERE  `commission_year` = '"+entryTypeYear+"'" +
                    " AND `commission_month` = '"+entryTypeMonth+"' AND `commission_center` = '"+revCenter+"'");
        }
        rs = stmnt.executeQuery();
        while (rs.next()){
            ID = rs.getString("commission_ID");
            Date = rs.getString("commission_date");
            Month = rs.getString("commission_month");
            Qtr = rs.getString("commission_quarter");
            Week = rs.getString("commission_week");
            Year = rs.getString("commission_year");
            Amount = getFunctions.getAmount(rs.getString("commission_amount"));
            getCollectionData = new GetEntries(ID, Date, Month, Week, Year, Qtr, Amount);
            commTable.getItems().add(getCollectionData);
        }

    }

    @FXML
    void clearEntries(ActionEvent event) {

    }

    @FXML
    void deleteSelection(ActionEvent event) {

    }

    @FXML
    void processKeyEvent(KeyEvent event) {
        String c = event.getCharacter();
        if (!"1234567890.".contains(c)) {
            event.consume();
        }
    }

    @FXML
    void updateEntries(ActionEvent event) {
        String ccAmount = txtEntCCAmt.getText();
        LocalDate date = entDatePck.getValue();
        if (entDatePck.getValue() == null) {
            lblDateWarn.setVisible(true);
            Condition = false;
        } else {
            Condition = true;
            Date = getFunctions.getDate(date);
            Year = getFunctions.getYear(date);
            Qtr = getFunctions.getQuarter(date);
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
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Please Enter Amount");
                    alert.showAndWait();
                    Condition =false;
                }else if(StringUtils.countMatches(txtEntCCAmt.getText(), ".") >1){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Incorrect amount format");
                    alert.setContentText("Please check the number of '.' in the amount");
                    alert.showAndWait();
                    Condition =false;
                }else{
                    colCCAmount.setCellValueFactory(d -> d.getValue().AmountProperty());
                    colDate.setCellValueFactory(d -> d.getValue().DateProperty());
                    colMonth.setCellValueFactory(d -> d.getValue().MonthProperty());
                    colQuarter.setCellValueFactory(d -> d.getValue().QuarterProperty());
                    colWeek.setCellValueFactory(d -> d.getValue().WeekProperty());
                    colYear.setCellValueFactory(d -> d.getValue().YearProperty());
                    Amount= getFunctions.getAmount(ccAmount);
                    if("0.00".equals(Amount)){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please Amount cannot be '0'");
                        alert.showAndWait();
                        Condition =false;
                    }else{
                        amount += Float.parseFloat(ccAmount);
                        lblTotalAmount.setText(getFunctions.getAmount(Float.toString(amount)));
                        addEntries = new GetEntries(Date, Month, Week, Year, Qtr, Amount);
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

    private void setCommissionUpdate(){
        addEntries = commTable.getSelectionModel().getSelectedItem();
        date = LocalDate.parse(addEntries.getDate(), format);
        entDatePck.setValue(date);
        mac = p.matcher(addEntries.getAmount());
        entryID = addEntries.getID();
        txtEntCCAmt.setText(mac.replaceAll(""));
    }
}
