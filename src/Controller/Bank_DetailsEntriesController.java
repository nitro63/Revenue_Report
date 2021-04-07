/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Controller.Gets.GetBankDetails;
import Controller.Gets.GetFunctions;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import org.apache.commons.lang3.StringUtils;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class Bank_DetailsEntriesController implements Initializable {
    @FXML
    private AnchorPane anchBankDetails;

    @FXML
    private JFXButton btnEnter;

    @FXML
    private JFXButton btnClear;

    @FXML
    private Label lblGCRWarn;

    @FXML
    private JFXComboBox<String> cmbGCR;
    @FXML
    private Label lblDeleteWarn;
    @FXML
    private Label lblEdit;
    @FXML
    private Label lblDup;
    @FXML
    private Label lblChqNmbwarn;

    @FXML
    private JFXTextField txtChqNmb;

    @FXML
    private JFXTextField txtBankName;

    @FXML
    private Label lblBankwarn;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private Label lblAmountwarn;

    @FXML
    private TableView<GetBankDetails> tblCollectEnt;

    @FXML
    private TableColumn<GetBankDetails, String> colGCR;

    @FXML
    private TableColumn<GetBankDetails, String> colYear;

    @FXML
    private TableColumn<GetBankDetails, String> colMonth;

    @FXML
    private TableColumn<GetBankDetails, String> colDate;

    @FXML
    private Label lblChqdatewarn;

    @FXML
    private JFXDatePicker dtpckChequeDate;

    @FXML
    private TableColumn<GetBankDetails, String> colChqDate;

    @FXML
    private TableColumn<GetBankDetails, String> colChqNmb;

    @FXML
    private TableColumn<GetBankDetails, String> colBank;

    @FXML
    private TableColumn<GetBankDetails, String> colAmount;

    @FXML
    private JFXButton btnClearEntr;
    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnSaveEntries;

    GetBankDetails getReport, getData;

    Payment_EntriesController colEnt ;
    GetFunctions getFunctions = new GetFunctions();

    private  boolean Condition = true;
    private final Connection con;
    private PreparedStatement stmnt;
    public Bank_DetailsEntriesController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
    public TableView getTableView(){
        return tblCollectEnt;
    }

    public void setAppController(Payment_EntriesController app){
        this.colEnt = app;
    }
    Stage stage =  new Stage()/*anchBankDetails.getScene().getWindow()*/;
    public void setStage(Stage stage){
        this.stage = stage;
    }

    String GCR, Date, chqDate, chqNumber, Bank, Amount, Month, Year, ID;
    ObservableList<String> GCRs = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (Entry<String, ArrayList<String>> gcr: colEnt.regGcr.entrySet()) {
            GCRs.addAll(gcr.getValue());
        }
        cmbGCR.getItems().clear();
        cmbGCR.setItems(GCRs);
        tblCollectEnt.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblCollectEnt.setOnMouseClicked(event -> {
            lblDeleteWarn.setVisible(false);
            lblDup.setVisible(false);
            lblEdit.setVisible(false);
        });
        cmbGCR.setOnMouseClicked(event -> {
            lblGCRWarn.setVisible(false);
        });
        txtBankName.setOnMouseClicked(event ->{
            lblBankwarn.setVisible(false);
        });
        txtChqNmb.setOnMouseClicked(event ->{
            lblChqNmbwarn.setVisible(false);
        });
        txtAmount.setOnMouseClicked(event ->{
            lblAmountwarn.setVisible(false);
        });
        dtpckChequeDate.setOnMouseClicked(event ->{
            lblChqdatewarn.setVisible(false);
        });
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
        if(!tblCollectEnt.getItems().isEmpty()) {
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
            colEnt.regGcr.clear();
            colEnt.monthGCR.clear();
            colEnt.dateGCR.clear();
            colEnt.count = 0;
        }

    }
    public String getGcrID(String Gcr, Map<String, String> gcrID){
        String id = "";
        for(Map.Entry<String, String>gCrID :gcrID.entrySet()) {
            if(gCrID.getValue().equals(Gcr)){
                id = gCrID.getKey();
            }
        }
        return id;
    }
    public String getcodeGcr(String Gcr, Map<String, String> codeGCR){
        String id = "";
        for(Map.Entry<String, String>gCrID :codeGCR.entrySet()) {
            if(gCrID.getKey().equals(Gcr)){
                id = gCrID.getValue();
            }
        }
        return id;
    }


    @FXML
    void SaveToDatabase(ActionEvent event) throws SQLException {
        ArrayList<String> duplicate = new ArrayList<>();
        Node node =(Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        getData = new GetBankDetails();
        Alert saveConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure you want to save cheque details?"
        );
        Button saveButton = (Button) saveConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        saveButton.setText("Save");
        saveConfirmation.setHeaderText("Confirm Save");
        saveConfirmation.setContentText("Are you sure you want to confirm and Exit?");
        saveConfirmation.initModality(Modality.APPLICATION_MODAL);
        saveConfirmation.initOwner(stage);
        saveConfirmation.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> saveResponse = saveConfirmation.showAndWait();
        if (!ButtonType.OK.equals(saveResponse.get())) {
            event.consume();
        }else{
            for(int i = 0; i < tblCollectEnt.getItems().size(); i++){
                    getData = tblCollectEnt.getItems().get(i);
                    int acChqNmb = Integer.parseInt(getData.getChequeNumber());
                    String acMonth = getData.getMonth();
                    String amount = getData.getAmount();
                    Matcher m = colEnt.p.matcher(amount);
                    amount = m.replaceAll("");
                    float acAMOUNT = Float.parseFloat(amount);
                    String acDATE = getData.getDate();
                    String acChqDate = getData.getChequeDate();
                    String acBank = getData.getBank();
                    String center = colEnt.GetCenter.getRevCenter();
                    String year = getData.getYear();
                    ID = getGcrID(getData.getGCR(), colEnt.gcrID);
                    System.out.println("Fred"+ID);
                String fini = "chq"+colEnt.getID();
                boolean condition = true;
                ArrayList<String> dup = new ArrayList<>();
                    stmnt = con.prepareStatement("SELECT * FROM `cheque_details` WHERE " +
                            " `cheque_number` = '"+acChqNmb+"' AND `bank` = '"+acBank+"'");
                    ResultSet res = stmnt.executeQuery();
                    while (res.next()){
                        duplicate.add(res.getString("GCR"));
                    }
                    if (duplicate.contains(getData.getGCR())){
                        lblDup.setText("Cheque Number "+'"'+acChqNmb+'"'+" for "+'"'+acBank+'"'+" already exist. " +
                                "Please select row of duplicate data in table to edit or delete.");
                        lblDup.setVisible(true);
                        i=tblCollectEnt.getItems().size();
                    }else {
                        while (condition){
                            stmnt = con.prepareStatement("SELECT `cheque_ID` FROM `cheque_details` WHERE `cheque_ID` = " +
                                    "'" + fini + "' ");
                            ResultSet rt = stmnt.executeQuery();
                            while (rt.next()){
                                dup.add(rt.getString("cheque_ID"));
                            }
                            if (dup.contains(fini)){
                                fini = "chq"+colEnt.getID();
                            }else {
                                condition = false;
                            }
                        }
                    stmnt = con.prepareStatement("INSERT INTO  `cheque_details`" +
                            "( `cheque_ID`, `cheque_date`, `cheque_number`, `bank`," +
                            " `amount`, `payment_ID`) VALUES ('" + fini + "','" + acChqDate + "', " +
                            "'" + acChqNmb + "', '" + acBank + "', '" + acAMOUNT + "',  '" + ID + "')");
                    stmnt.executeUpdate();
                        tblCollectEnt.getItems().remove(i);

                    }
            }
            if(tblCollectEnt.getItems().isEmpty()){
                tblCollectEnt.getItems().clear();
                colEnt.regGcr.clear();
                colEnt.monthGCR.clear();
                colEnt.dateGCR.clear();
                colEnt.count = 0;
                stage.close();
            }
        }
    }

    @FXML
    void clearEntries(ActionEvent event) {
        if (tblCollectEnt.getSelectionModel().isEmpty()){
            lblDeleteWarn.setVisible(true);
        }else{
            String regex = "(?<=[\\d])(,)(?=[\\d])";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(tblCollectEnt.getSelectionModel().getSelectedItem().getAmount());
            cmbGCR.getSelectionModel().select(tblCollectEnt.getSelectionModel().getSelectedItem().getGCR());
            dtpckChequeDate.setValue(LocalDate.parse(tblCollectEnt.getSelectionModel().getSelectedItem().getDate(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            txtChqNmb.setText(tblCollectEnt.getSelectionModel().getSelectedItem().getChequeNumber());
            txtAmount.setText(m.replaceAll(""));
            txtBankName.setText(tblCollectEnt.getSelectionModel().getSelectedItem().getBank());
            ObservableList<GetBankDetails> selectedRows = tblCollectEnt.getSelectionModel().getSelectedItems();
            ArrayList<GetBankDetails> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> tblCollectEnt.getItems().remove(row));
        }
    }

    @FXML
    void saveEntries(ActionEvent event) {
//        getcodeGcr(cmbGCR.getSelectionModel().getSelectedItem(),colEnt.codeGCR)
        GCR = cmbGCR.getSelectionModel().getSelectedItem();
        Bank = txtBankName.getText();
        for(Map.Entry<String, ArrayList<String>>GcRdate :colEnt.monthGCR.entrySet()) {
            if(GcRdate.getValue().contains(GCR)){
                Month = GcRdate.getKey();
            }
        }
        chqNumber = txtChqNmb.getText();
        for(Map.Entry<String, Map<String, ArrayList<String>>>GCRdate :colEnt.dateGCR.entrySet()){
                if (GCRdate.getValue().containsKey(Month)) {
                    Date = GCRdate.getKey();
                }

        }

        for(Map.Entry<String, ArrayList<String>>GCRdate :colEnt.regGcr.entrySet()){
            if (GCRdate.getValue().contains(GCR)) {
                Year = GCRdate.getKey();
            }

        }
        LocalDate date = dtpckChequeDate.getValue();

        if(GCR == null){
            lblGCRWarn.setVisible(true);
            Condition =false;
        }else if(!colEnt.typeSerials.get("Cheque Deposit Slip").contains(getGcrID(GCR, colEnt.gcrID)) && date == null){
            lblChqdatewarn.setVisible(true);
            Condition = false;
        }else{
            Condition =true;
            if (!colEnt.typeSerials.get("Cheque Deposit Slip").contains(getGcrID(GCR, colEnt.gcrID))){
            chqDate = getFunctions.getDate(date);
            }else {
                chqDate = "NA";
            }
            while(Condition) {
                if(txtBankName.getText().isEmpty() || Bank.matches("\\s+")){
                    lblBankwarn.setVisible(true);
                    Condition =false;
                }else if(!colEnt.typeSerials.get("Cheque Deposit Slip").contains(getGcrID(GCR, colEnt.gcrID)) && chqDate == null){
                    lblChqdatewarn.setVisible(true);
                    Condition =false;

                }else if(txtChqNmb.getText().isEmpty() || chqNumber.matches("\\s+")){
                    lblChqNmbwarn.setVisible(true);
                    Condition =false;
                }else if(txtAmount.getText().isEmpty()){
                    lblAmountwarn.setVisible(true);
                    Condition =false;
                }else if(StringUtils.countMatches(txtAmount.getText(), ".") >1){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Please Enter Amount");
                    alert.setContentText("Please check the number of '.' in the amount");
                    alert.showAndWait();
                    txtAmount.clear();
                    Condition =false;
                }else{
                    colGCR.setCellValueFactory(data -> data.getValue().GCRProperty());
                    colYear.setCellValueFactory(data -> data.getValue().yearProperty());
                    colMonth.setCellValueFactory(data -> data.getValue().monthProperty());
                    colDate.setCellValueFactory(data -> data.getValue().dateProperty());
                    colChqDate.setCellValueFactory(data -> data.getValue().chequeDateProperty());
                    colChqNmb.setCellValueFactory(data -> data.getValue().chequeNumberProperty());
                    colBank.setCellValueFactory(data -> data.getValue().bankProperty());
                    colAmount.setCellValueFactory(data -> data.getValue().amountProperty());
                    Amount= getFunctions.getAmount(txtAmount.getText());
                    if("0.00".equals(Amount)){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please Amount cannot be '0'");
                        alert.showAndWait();
                        txtAmount.clear();
                        Condition =false;
                    }else{
                        String gcr = cmbGCR.getSelectionModel().getSelectedItem().substring(cmbGCR.getSelectionModel().getSelectedItem().lastIndexOf("-")+1);
                        System.out.println("Fred"+"\n"+gcr+"\n"+getGcrID(cmbGCR.getSelectionModel().getSelectedItem(), colEnt.gcrID)+"\n"+colEnt.gcrID);
                        getReport = new GetBankDetails(GCR, Year, Month, Date, chqDate, chqNumber, Bank, Amount);
                        tblCollectEnt.getItems().add(getReport);
                        Condition = false;
                        clearEnt();
                    }
                }
            }
        }
    }
    void clearEnt(){
        txtAmount.clear();
        txtChqNmb.clear();
        txtBankName.clear();
        cmbGCR.getSelectionModel().clearSelection();
        dtpckChequeDate.setValue(null);
    }
    @FXML
    public void onlyIntegers(KeyEvent event) {
        String c = event.getCharacter();
        if("1234567890.".contains(c)) {}
        else {
            event.consume();
        }
    }

    @FXML
    public void onlyNumbers(KeyEvent event) {
        String c = event.getCharacter();
        if("1234567890".contains(c)) {}
        else {
            event.consume();
        }
    }

    public void deleteSelection(ActionEvent actionEvent) {
        if(tblCollectEnt.getSelectionModel().isEmpty()) {
            lblDeleteWarn.setVisible(true);
        }else{
            ObservableList<GetBankDetails> selectedRows = tblCollectEnt.getSelectionModel().getSelectedItems();
            ArrayList<GetBankDetails> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> tblCollectEnt.getItems().remove(row));
        }
    }


}
