package Controller;

import Controller.Gets.GetBankDetails;
import Controller.Gets.GetEntries;
import Controller.Gets.GetFunctions;
import Controller.Gets.GetRevCenter;
import com.jfoenix.controls.JFXButton;
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
import revenue_report.DBConnection;

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
    private TableColumn<GetEntries, String> colQuarter;

    @FXML
    private TableColumn<GetEntries, String> colYear;

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
    Revenue_EntriesController app;

    GetEntries addEntries;

    GetRevCenter GetCenter;
    GetFunctions getFunctions = new GetFunctions();
    String RevCent, Date, Year, Qtr, Week, Month, Amount;
    float amount;
    boolean Condition;
    private final Connection con;
    public commissionEntriesController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }

    Map<String, String> registerAmount=new HashMap<>();
    public void setappController(Revenue_EntriesController app){
        this.app = app;
    }
    public Revenue_EntriesController getRevenue_EntriesController (){
        return app;
    }
    Stage stage =  new Stage()/*anchBankDetails.getScene().getWindow()*/;
    public void setStage(Stage stage){
        this.stage = stage;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registerAmount.put("", "");
        entDatePck.setOnMouseClicked(e ->{
            lblDateWarn.setVisible(false);
        });
        txtEntCCAmt.setOnMouseClicked(e ->{
            lblAmountWarn.setVisible(false);
        });
        commTable.setOnMouseClicked(e ->{
            lblDeleteWarn.setVisible(false);lblDup.setVisible(false);lblEdit.setVisible(false);
        });
        commTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
            closeConfirmation.setContentText("Are you sure you want to exit without saving cheque details? ");
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
        RevCent = getRevenue_EntriesController().getentries_sideController().getRevCent().getSelectionModel().
                getSelectedItem().toString();
        System.out.println(RevCent);
        float deduction = 0;
        GetEntries getData = new GetEntries();
        ObservableList<String> duplicate = FXCollections.observableArrayList();
        for (int i=0; i<=commTable.getItems().size(); i++){
            if (i != commTable.getItems().size()){
            getData = commTable.getItems().get(i);
            String regex = "(?<=[\\d])(,)(?=[\\d])";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(getData.getAmount());
            float ccAmount = Float.parseFloat(m.replaceAll(""));
            int week = Integer.parseInt(getData.getWeek()), year = Integer.parseInt(getData.getYear()),
                qtr = Integer.parseInt(getData.getQuarter());
            String date = getData.getDate(), month = getData.getMonth(), fini = getID();
            PreparedStatement stmnt = con.prepareStatement("SELECT `commission_date` FROM `commission_details`" +
                    " WHERE `commission_center` = '"+RevCent+"'");
                ResultSet rs = stmnt.executeQuery();
                while (rs.next()){
                    duplicate.add(rs.getString("commission_date"));
                }
                if (duplicate.contains(date)){
                    lblDup.setText("Amount for "+'"'+getData.getDate()+'"'+ " already exist. Please delete or edit duplicate.");
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
                            "`commission_center`, `commission_amount`, `commission_date`, `commission_week`," +
                            " `commission_month`,`commission_quarter`, `commission_year`) VALUES ('"+fini+"', '"+RevCent+"', '"+ccAmount
                            +"', '"+date+"', '"+week+"', '"+month+"', '"+qtr+"', '"+year+"')");
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
    }

    @FXML
    void deleteSelection(ActionEvent event) {
        if(commTable.getSelectionModel().isEmpty()) {
            lblDeleteWarn.setVisible(true);
        }else{
            ObservableList<GetEntries> selectedRows = commTable.getSelectionModel().getSelectedItems();
            ArrayList<GetEntries> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> commTable.getItems().remove(row));
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
                    colMonth.setCellValueFactory(d -> d.getValue().MonthProperty());
                    colQuarter.setCellValueFactory(d -> d.getValue().QuarterProperty());
                    colWeek.setCellValueFactory(d -> d.getValue().WeekProperty());
                    colYear.setCellValueFactory(d -> d.getValue().YearProperty());
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
}