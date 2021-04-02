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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private TableColumn<GetValueBooksEntries, String> colRemarks;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnClearEntr;

    @FXML
    private JFXButton btnSaveEntries;
    @FXML
    private Label lblDeleteWarn;
    @FXML
    private Label lblEdit;
    @FXML
    private Label lblDup;
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
    float amount =0, cumuamount, puramount;
    String regex = "(?<=[\\d])(,)(?=[\\d])";
    Pattern p = Pattern.compile(regex);
    Matcher mac;
    Map<String, ArrayList<String>> regValSerial = new HashMap<>();
    String revCent, Year, Month, Date, typeOfValBk, firstSerial, lastSerial, Quantity, valAmount, cumuAmount, purAmount;
    String remarks = "Initial Entry";
    Map<String, Float> priceBook = new HashMap<>();


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
        PreparedStatement stmnt = null;
        try {
            stmnt = con.prepareStatement("SELECT * FROM `value_books_details`");
            ResultSet res = stmnt.executeQuery();
            cmbTypeOfValueBook.getItems().clear();
            while (res.next()){
                cmbTypeOfValueBook.getItems().add(res.getString("value_books"));
                priceBook.put(res.getString("value_books"), res.getFloat("price"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tblValueBookStocks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblValueBookStocks.setOnMouseClicked(e -> {
            lblDeleteWarn.setVisible(false);
            lblDup.setVisible(false);
            lblEdit.setVisible(false);
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
    }

    public void showClose(ActionEvent actionEvent) {
        getentries_sideController().getappController().getCenterPane().getChildren().clear();
    }

    public void saveEntries(ActionEvent actionEvent) throws SQLException {
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
                    if(regValSerial.get(typeOfValBk).contains(firstSerial) || regValSerial.get(typeOfValBk).
                            contains(lastSerial)){
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
                    colRemarks.setCellValueFactory(d -> d.getValue().remarksProperty());
                    int serialChecker = (Integer.parseInt(lastSerial) - Integer.parseInt(firstSerial))+1 ;
                    int quantity = ((serialChecker)/ 100);
                    if((serialChecker)<100 || serialChecker % 100 !=0){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please check Serials");
                        alert.showAndWait();
                        Condition =false;
                    }else {
                        Quantity = Integer.toString(quantity);
                        for (Map.Entry<String, Float> calValAmount : priceBook.entrySet()){
                            if (calValAmount.getKey().equals(typeOfValBk)){
                                amount = quantity * calValAmount.getValue() * 100;
                            }
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
                            addEntries = new GetValueBooksEntries(Year, Month, Date, typeOfValBk, firstSerial,
                                    lastSerial, Quantity, valAmount, cumuAmount, purAmount, remarks);
                            tblValueBookStocks.getItems().add(addEntries);
                            if (!regValSerial.containsKey(typeOfValBk)) {
                                regValSerial.put(typeOfValBk, new ArrayList<>());
                                regValSerial.get(typeOfValBk).add(firstSerial);
                                regValSerial.get(typeOfValBk).add(lastSerial);
                            } else if (regValSerial.containsKey(typeOfValBk) && !regValSerial.get(typeOfValBk).
                                    contains(firstSerial)) {
                                regValSerial.get(typeOfValBk).add(firstSerial);
                            } else if (regValSerial.containsKey(typeOfValBk) && !regValSerial.get(typeOfValBk).
                                    contains(lastSerial)) {
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

    public void clearEntries(ActionEvent actionEvent) {
        GetValueBooksEntries valueBooks = tblValueBookStocks.getSelectionModel().getSelectedItem();
        if (tblValueBookStocks.getSelectionModel().isEmpty()){
            lblEdit.setText("Please select a row in the table to "+'"'+"Edit"+'"');
            lblEdit.setVisible(true);
        }else {
            entDatePck.setValue(LocalDate.parse(valueBooks.getDate(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            cmbTypeOfValueBook.getSelectionModel().select(valueBooks.getValueBook());
                    mac = p.matcher(valueBooks.getPurAmount());
            String amount = "";
            if (mac.matches()){
                amount = mac.replaceAll("");
            }else {
                amount = valueBooks.getPurAmount();
            }
            float price = Float.parseFloat(amount);
            int quantity = Integer.parseInt(valueBooks.getQuantity());
            txtUnitAmount.setText(Float.toString(price / quantity));
            txtSerialTo.setText(valueBooks.getLastSerial());
            txtSerialFrom.setText(valueBooks.getFirstSerial());
            if (regValSerial.get(cmbTypeOfValueBook.getSelectionModel().getSelectedItem()).contains(txtSerialTo.
                    getText()) || regValSerial.get(cmbTypeOfValueBook.getSelectionModel().getSelectedItem()).
                    contains(txtSerialFrom.getText())){
                regValSerial.get(cmbTypeOfValueBook.getSelectionModel().getSelectedItem()).remove(txtSerialFrom.
                        getText());
                regValSerial.get(cmbTypeOfValueBook.getSelectionModel().getSelectedItem()).remove(txtSerialTo.
                        getText());
            }

        }
    }

    public void SaveToDatabase(ActionEvent actionEvent) throws SQLException {
        revCent = GetCenter.getRevCenter();
        ArrayList<String> duplicate = new ArrayList<>();
        getData = new GetValueBooksEntries();
        ResultSet rs;
        ResultSetMetaData rm;
        for(int f = 0; f<tblValueBookStocks.getItems().size(); f++) {
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
            PreparedStatement stmnt = con.prepareStatement("SELECT * FROM `value_books_stock_record` WHERE " +
                    "`revCenter` = '"+revCent+"' AND `value_book` = '"+typeOfValBk+"' AND `first_serial` = " +
                    "'"+acFirstSerial+"' AND `last_serial` = '"+acLastSerial+"'");
            ResultSet res = stmnt.executeQuery();
            while (res.next()){
                duplicate.add(res.getString("first_serial"));
            }
            if (duplicate.contains(getData.getFirstSerial())){
                lblDup.setText('"'+acFirstSerial+'"'+" to "+'"'+acLastSerial+'"'+" for "+'"'+typeOfValBk+'"'+"" +
                        " already exist. Please select row of duplicate data to Edit or Delete." );
                lblDup.setVisible(true);
                f = tblValueBookStocks.getItems().size();
            }else {
                stmnt = con.prepareStatement("INSERT INTO `value_books_stock_record`(`revCenter`, `year`, `month`," +
                        " `date`, `value_book`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`," +
                        " `remarks`) VALUES ('" + revCent + "', '" + acYear + "', '" + Month + "', '" + Date + "', '" +
                        typeOfValBk + "', '" + acFirstSerial + "','" + acLastSerial + "', '" + acQuantity + "', '" +
                        acAmount + "', '" + acPurAmount + "','" + remarks + "')");
                stmnt.executeUpdate();
                tblValueBookStocks.getItems().remove(f);
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
