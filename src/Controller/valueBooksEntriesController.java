package Controller;

import Controller.Gets.GetFunctions;
import Controller.Gets.GetRevCenter;
import Controller.Gets.GetValueBooksEntries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;
import revenue_report.DBConnection;

public class valueBooksEntriesController implements Initializable {

    @FXML
    private AnchorPane CollectionEntPane;

    @FXML
    private JFXButton btnClose;

    @FXML
    private JFXDatePicker entDatePck;

    @FXML
    private JFXComboBox<String> cmbTypeOfValueBook;

    @FXML
    private JFXTextField txtUnitAmount;

    @FXML
    private JFXTextField txtSerialFrom;

    @FXML
    private JFXTextField txtSerialTo;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnEnter;

    @FXML
    private TableView<GetValueBooksEntries> tblValueBookStocks;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colYear;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colMonth;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colDATE;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colTypeVB;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colSerialFrom;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colSerialTo;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colQuantity;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colAmtVal;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colCumuAmount;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colPurchAmount;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnClearEntr;

    @FXML
    private JFXButton btnSaveEntries;
    @FXML
    private Label lblDeleteWarn;
    @FXML
    private Label lblDateWarn;
    @FXML
    private Label lblToWarn;
    @FXML
    private Label lblFromWarn;
    @FXML
    private Label lblAmountWarn;
    @FXML
    private Label lblValueBookWarn;


    entries_sideController app;
    GetValueBooksEntries addEntries, getData;
    Bank_DetailsEntriesController bnkDtls = new Bank_DetailsEntriesController();
    GetFunctions getDates = new GetFunctions();
    private final GetRevCenter GetCenter;
    boolean Condition = true;
    private final Connection con;
    private PreparedStatement stmnt;
    float amount =0, cumuamount, puramount;
    Map<String, ArrayList<String>> regValSerial = new HashMap<>();
    ObservableList<String> valueBookType = FXCollections.observableArrayList("GCR", "Car-Park(GH₵ 1.00)", "Car-Park(GH₵ 2.00)","Market-tolls(GH₵ 1.00)");
    String revCent, Year, Month, Date, typeOfValBk, firstSerial, lastSerial, Quantity, valAmount, cumuAmount, purAmount;

    public valueBooksEntriesController(GetRevCenter GetCenter) throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
        this.GetCenter = GetCenter;
    }

    public void setappController(entries_sideController app){
        this.app = app;
    }
    public entries_sideController getentries_sideController(){
        return app;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblValueBookStocks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblValueBookStocks.setOnMouseClicked(e -> {
            lblDeleteWarn.setVisible(false);
        });
        entDatePck.setOnMouseClicked(event -> {
            lblDateWarn.setVisible(false);
        });
        cmbTypeOfValueBook.setOnMouseClicked(event -> {
            lblValueBookWarn.setVisible(false);
        });
        txtUnitAmount.setOnMouseClicked(event -> {
            lblAmountWarn.setVisible(false);
        });
        txtSerialFrom.setOnMouseClicked(event -> {
            lblFromWarn.setVisible(false);
        });
        txtSerialTo.setOnMouseClicked(event -> {
            lblToWarn.setVisible(false);
        });
        cmbTypeOfValueBook.getItems().addAll(valueBookType);
    }

    public void showClose(ActionEvent actionEvent) {
        getentries_sideController().getappController().getCenterPane().getChildren().clear();
    }

    public void saveEntries(ActionEvent actionEvent) {
        regValSerial.put("", new ArrayList<>());
        revCent = GetCenter.getRevCenter();
        LocalDate date = entDatePck.getValue();
        typeOfValBk = cmbTypeOfValueBook.getSelectionModel().getSelectedItem();
        firstSerial = txtSerialFrom.getText();
        lastSerial = txtSerialTo.getText();
        if(entDatePck.getValue() == null){
            lblDateWarn.setVisible(true);
            Condition = false;
        }else{
            Condition =true;
            Month = getDates.getMonth(date);
            Date = getDates.getDate(date);
            Year = getDates.getYear(date);
            while (Condition){
                if(regValSerial.containsKey(typeOfValBk)){
                    if(regValSerial.get(typeOfValBk).contains(firstSerial) || regValSerial.get(typeOfValBk).contains(lastSerial)){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Serials have already been entered");
                        alert.showAndWait();
                        Condition =false;
                    }
                }
                else if(cmbTypeOfValueBook.getSelectionModel().isEmpty()){
                    lblValueBookWarn.setVisible(true);
                    Condition = false;
                }
                else if(txtUnitAmount.getText().isEmpty()){
                    lblAmountWarn.setVisible(true);
                    Condition = false;
                }
                else if(txtSerialFrom.getText().isEmpty()){
                    lblFromWarn.setVisible(true);
                    Condition = false;
                }
                else if(txtSerialTo.getText().isEmpty()){
                    lblToWarn.setVisible(true);
                    Condition = false;
                }
                else if(StringUtils.countMatches(txtUnitAmount.getText(), ".") > 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Please Enter Amount");
                    alert.setContentText("Please check the number of '.' in the amount");
                    alert.showAndWait();
                    txtUnitAmount.clear();
                    Condition = false;
                }
                else {
                    colYear.setCellValueFactory(data -> data.getValue().yearProperty());
                    colMonth.setCellValueFactory(data -> data.getValue().monthProperty());
                    colDATE.setCellValueFactory(data -> data.getValue().dateProperty());
                    colTypeVB.setCellValueFactory(data -> data.getValue().valueBookProperty());
                    colSerialFrom.setCellValueFactory(data -> data.getValue().firstSerialProperty());
                    colSerialTo.setCellValueFactory(data -> data.getValue().lastSerialProperty());
                    colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty());
                    colAmtVal.setCellValueFactory(data -> data.getValue().valAmountProperty());
                    colCumuAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
                    colPurchAmount.setCellValueFactory(data -> data.getValue().purAmountProperty());
                    int serialChecker = Integer.parseInt(lastSerial) - Integer.parseInt(firstSerial) +1;
                    int quantity = ((serialChecker + 1)/ 100);
                    if(serialChecker / 100 != 1){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please check Serials");
                        alert.showAndWait();
                        Condition =false;
                    }else {
                        Quantity = Integer.toString(quantity);
                        if (typeOfValBk.equals("GCR")) {
                            amount = 0;
                        } else if (typeOfValBk.equals("Car-Park(GH₵ 2.00)")) {
                            amount = 2 * quantity * 100;
                        } else if (typeOfValBk.equals("Car-Park(GH₵ 1.00)") || typeOfValBk.equals("Market-tolls(GH₵ 1.00)")) {
                            amount = quantity * 100;
                        }
                        cumuamount += amount;
                        puramount = (Float.parseFloat(txtUnitAmount.getText()) * quantity);
                        purAmount = getDates.getAmount(Float.toString(puramount));
                        valAmount = getDates.getAmount(Float.toString((amount)));
                        cumuAmount = getDates.getAmount(Float.toString((cumuamount)));
                        if ("0.00".equals(purAmount)) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("Please Amount cannot be '0'");
                            alert.showAndWait();
                            txtUnitAmount.clear();
                            Condition = false;

                        } else {
                            addEntries = new GetValueBooksEntries(Year, Month, Date, typeOfValBk, firstSerial, lastSerial, Quantity, valAmount, cumuAmount, purAmount);
                            tblValueBookStocks.getItems().add(addEntries);
                            if (!regValSerial.containsKey(typeOfValBk)) {
                                regValSerial.put(typeOfValBk, new ArrayList<>());
                                regValSerial.get(typeOfValBk).add(firstSerial);
                                regValSerial.get(typeOfValBk).add(lastSerial);
                            } else if (regValSerial.containsKey(typeOfValBk) && !regValSerial.get(typeOfValBk).contains(firstSerial)) {
                                regValSerial.get(typeOfValBk).add(firstSerial);
                            } else if (regValSerial.containsKey(typeOfValBk) && !regValSerial.get(typeOfValBk).contains(lastSerial)) {
                                regValSerial.get(typeOfValBk).add(lastSerial);
                            }
                            clearEnt();
                            Condition = false;
                        }
                    }
                }
            }
        }
    }


    void clearEnt(){
        cmbTypeOfValueBook.getSelectionModel().clearSelection();
        entDatePck.setValue(null);
        txtUnitAmount.clear();
        txtSerialTo.clear();
        txtSerialFrom.clear();
    }

    public void clearEntries(ActionEvent actionEvent) {clearEnt(); }

    public void SaveToDatabase(ActionEvent actionEvent) throws SQLException {
        revCent = GetCenter.getRevCenter();
        getData = new GetValueBooksEntries();
        ResultSet rs;
        ResultSetMetaData rm;
        for(int f = 0; f<=tblValueBookStocks.getItems().size(); f++) {
            if (f == tblValueBookStocks.getItems().size()) {
                tblValueBookStocks.getItems().clear();
            }else {
            getData = tblValueBookStocks.getItems().get(f);
            Month = getData.getMonth();
            Date = getData.getDate();
            typeOfValBk = getData.getValueBook();
            int acYear = Integer.parseInt(getData.getYear());
            int acFirstSerial = Integer.parseInt(getData.getFirstSerial());
            int acLastSerial = Integer.parseInt(getData.getLastSerial());
            int acQuantity = Integer.parseInt(getData.getQuantity());
            float acAmount = Float.parseFloat(getData.getValAmount());
            float acPurAmount = Float.parseFloat(getData.getPurAmount());
            String remarks = "Initial Entry";
            stmnt = con.prepareStatement("INSERT INTO `value_books_stock_record`(`revCenter`, `year`, `month`, `date`, `value_book`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks`) VALUES ('" + revCent + "', '" + acYear + "', '" + Month + "', '" + Date + "', '" + typeOfValBk + "', '" + acFirstSerial + "','" + acLastSerial + "', '" + acQuantity + "', '" + acAmount + "', '" + acPurAmount + "','" + remarks + "')");
            stmnt.executeUpdate();
        }
    }
}

    public void CancelEntries(ActionEvent actionEvent) {
        tblValueBookStocks.getItems().clear();
    }

    public void onlyNumbers(KeyEvent keyEvent) {
        bnkDtls.onlyNumbers(keyEvent);
    }

    public void onlyAmount(KeyEvent keyEvent) {
        bnkDtls.onlyIntegers(keyEvent);
    }

    public void deleteSelection(ActionEvent actionEvent) {
        if(tblValueBookStocks.getSelectionModel().isEmpty()){
            lblDeleteWarn.setVisible(true);
        } else {
            ObservableList<GetValueBooksEntries> selectedRows = tblValueBookStocks.getSelectionModel().getSelectedItems();
            ArrayList<GetValueBooksEntries> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> tblValueBookStocks.getItems().remove(row));
        }
    }
}
