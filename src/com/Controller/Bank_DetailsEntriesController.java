/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.Controller.Gets.GetBankDetails;
import com.Controller.Gets.GetFunctions;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import org.apache.commons.lang3.StringUtils;
import com.revenue_report.DBConnection;

/**
 * FXML com.Controller class
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
    private DatePicker dtpckChequeDate;

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
    private JFXSnackbar s;

    GetBankDetails getReport, getData;

    Payment_EntriesController colEnt ;
    GetFunctions getFunctions = new GetFunctions();

    private  boolean Condition = true;
    private final Connection con;
    private PreparedStatement stmnt;
    public Bank_DetailsEntriesController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
    public TableView<GetBankDetails> getTableView(){
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
        Collections.sort(GCRs);
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
            if(gCrID.getKey().equals(Gcr)){
                id = gCrID.getValue();
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
            if (s != null) {
                s.unregisterSnackbarContainer(anchBankDetails);
            }
        getData = new GetBankDetails();
        for(int i = 0; i < tblCollectEnt.getItems().size(); i++){
            getData = tblCollectEnt.getItems().get(i);
            int payYear = Integer.parseInt(colEnt.payChq.get(getData.getGCR()).get("Year"));
            String acChqNmb = getData.getChequeNumber(); int payGCR = Integer.parseInt(colEnt.payChq.get(getData.getGCR()).get("GCR"));
            String payMonth = colEnt.payChq.get(getData.getGCR()).get("Month");
            String amount = getData.getAmount();
            Matcher m = colEnt.p.matcher(amount);
            amount = m.replaceAll("");

            float acAMOUNT = Float.parseFloat(amount), payAmount = Float.parseFloat(colEnt.payChq.get(getData.getGCR()).get("Amount"));

            String check = colEnt.payChq.get(getData.getGCR()).get("Date");

            String payDATE = colEnt.payChq.get(getData.getGCR()).get("Date"), payType = colEnt.payChq.get(getData.getGCR()).get("Type");

            String acChqDate = getFunctions.setSqlDate(getData.getChequeDate());

            String acBank = getData.getBank();

            String payCenter = colEnt.payChq.get(getData.getGCR()).get("Center"), payID = colEnt.payChq.get(getData.getGCR()).get("PayID");

            ID = getGcrID(getData.getGCR(), colEnt.gcrID);

            String fini = "chq"+colEnt.getID(colEnt.GetCenter.getCenterID());

            boolean condition = true;

            ArrayList<String> dup = new ArrayList<>();

            stmnt = con.prepareStatement("SELECT * FROM `cheque_details` WHERE " +
                    " `cheque_number` = '"+acChqNmb+"' AND `bank` = '"+acBank+"'");

            ResultSet res = stmnt.executeQuery();

            while (res.next()){
                duplicate.add(res.getString("cheque_number"));
            }
            if (duplicate.contains(acChqNmb)){
                lblDup.setText("Cheque Number "+'"'+acChqNmb+'"'+" for "+'"'+acBank+'"'+" already exist. " +
                        "Please select row of duplicate data in table to edit or delete.");
                lblDup.setVisible(true);
                i=tblCollectEnt.getItems().size();
            }else {
                stmnt = con.prepareStatement("INSERT INTO `collection_payment_entries`(`pay_revCenter`, `GCR`," +
                        " `Amount`, `Date`, `Month`, `Year`, `payment_type`, `pay_ID`) VALUES ('" + payCenter + "', '" +
                        payGCR + "' ,'" + payAmount + "', '" + payDATE + "', '" + payMonth + "', '" + payYear + "', '" +
                        payType + "', '" + payID + "')");
                stmnt.executeUpdate();
                colEnt.getTableView().getItems().removeIf(item -> item.getGCR().equals(Integer.toString(payGCR)) && item.getType().equals(payType)&&item.getAmount().equals(Float.toString(payAmount)));
                colEnt.payChq.remove(getData.getGCR());
                while (condition){
                    stmnt = con.prepareStatement("SELECT `cheque_ID` FROM `cheque_details` WHERE `cheque_ID` = " +
                            "'" + fini + "' ");
                    ResultSet rt = stmnt.executeQuery();
                    while (rt.next()){
                        dup.add(rt.getString("cheque_ID"));
                    }
                    if (dup.contains(fini)){
                        fini = "chq"+colEnt.getID(colEnt.GetCenter.getCenterID());
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
                for (Entry<String, ArrayList<String>> gcr: colEnt.regGcr.entrySet()) {
                    gcr.getValue().remove(getData.getGCR());
                }
                colEnt.monthGCR.get(getData.getMonth()).remove(getData.getGCR());
                colEnt.dateGCR.get(getData.getDate()).get(getData.getMonth()).remove(getData.getGCR());
                colEnt.count--;

            }
        }
            if(tblCollectEnt.getItems().isEmpty()){
                                s = new JFXSnackbar(anchBankDetails);
                                s.setStyle("-fx-text-fill: green");
                                s.show("Cheque Details successfully saved!", 3000);
        Alert saveConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION);
        Button saveButton = (Button) saveConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        ), closeButton = (Button) saveConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
        if (colEnt.count == 0){
            saveButton.setText("Yes");
            saveConfirmation.setHeaderText("All related payments Cheque details Successfully saved. ");
            saveConfirmation.setContentText("Do you want to Close this window?");
        }else {
            closeButton.setText("No");
            closeButton.setDefaultButton(true);
            saveConfirmation.setHeaderText("\u001B[1m Not All payments' related Cheque details were saved. ");
            saveConfirmation.setContentText("Do you want to Continue?");
        }
        saveConfirmation.initModality(Modality.APPLICATION_MODAL);
        saveConfirmation.initOwner(stage);
        saveConfirmation.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> saveResponse = saveConfirmation.showAndWait();
        if (!ButtonType.OK.equals(saveResponse.get())) {
            event.consume();
        }else{
                stage.close();

        } }
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
        System.out.println(GCR+"\n"+colEnt.monthGCR+"\n"+colEnt.dateGCR);
        for(Map.Entry<String, ArrayList<String>>GcRdate :colEnt.monthGCR.entrySet()) {
            if(GcRdate.getValue().contains(GCR)){
                Month = GcRdate.getKey();
                System.out.println(Month);
            }
        }
        chqNumber = txtChqNmb.getText();
        for(Map.Entry<String, Map<String, ArrayList<String>>>GCRdate :colEnt.dateGCR.entrySet()){
                if (GCRdate.getValue().containsKey(Month)) {
                    Date = GCRdate.getKey();
                    System.out.println(Date);
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
                    }/*else if (true){
                        System.out.println("");
                    }*/else{
                        String id = colEnt.gcrID.get(GCR);
                        String gcr = cmbGCR.getSelectionModel().getSelectedItem().substring(cmbGCR.getSelectionModel().getSelectedItem().lastIndexOf("-")+1);
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

/**
 *
 for(int i = 0; i < tblCollectEnt.getItems().size(); i++){
 getData = tblCollectEnt.getItems().get(i);
 int payYear = Integer.parseInt(colEnt.payChq.get(getData.getGCR()).get("Year"));
 int acChqNmb = Integer.parseInt(getData.getChequeNumber()), payGCR = Integer.parseInt(colEnt.payChq.get(getData.getGCR()).get("GCR"));
 String payMonth = colEnt.payChq.get(getData.getGCR()).get("Month");
 String amount = getData.getAmount();
 Matcher m = colEnt.p.matcher(amount);
 amount = m.replaceAll("");
 float acAMOUNT = Float.parseFloat(amount), payAmount = Float.parseFloat(colEnt.payChq.get(getData.getGCR()).get("Amount"));
 String payDATE = colEnt.payChq.get(getData.getGCR()).get("Date"), payType = colEnt.payChq.get(getData.getGCR()).get("Type");
 String acChqDate = getData.getChequeDate();
 String acBank = getData.getBank();
 String payCenter = colEnt.payChq.get(getData.getGCR()).get("Center"), payID = colEnt.payChq.get(getData.getGCR()).get("PayID");
 String year = getData.getYear();
 ID = getGcrID(getData.getGCR(), colEnt.gcrID);
 String fini = "chq"+colEnt.getID(colEnt.GetCenter.getCenterID());
 boolean condition = true;
 ArrayList<String> dup = new ArrayList<>();
 stmnt = con.prepareStatement("SELECT * FROM `cheque_details` WHERE " +
 " `cheque_number` = '"+acChqNmb+"' AND `bank` = '"+acBank+"'");
 ResultSet res = stmnt.executeQuery();
 while (res.next()){
 duplicate.add(res.getString("cheque_number"));
 }
 if (duplicate.contains(acChqNmb)){
 lblDup.setText("Cheque Number "+'"'+acChqNmb+'"'+" for "+'"'+acBank+'"'+" already exist. " +
 "Please select row of duplicate data in table to edit or delete.");
 lblDup.setVisible(true);
 i=tblCollectEnt.getItems().size();
 }else {
 stmnt = con.prepareStatement("INSERT INTO `collection_payment_entries`(`pay_revCenter`, `GCR`," +
 " `Amount`, `Date`, `Month`, `Year`, `payment_type`, `pay_ID`) VALUES ('" + payCenter + "', '" +
 payGCR + "' ,'" + payAmount + "', '" + payDATE + "', '" + payMonth + "', '" + payYear + "', '" +
 payType + "', '" + payID + "')");
 stmnt.executeUpdate();
 colEnt.getTableView().getItems().removeIf(item -> item.getGCR().equals(Integer.toString(payGCR)) && item.getType().equals(payType));
 colEnt.payChq.remove(getData.getGCR());
 while (condition){
 stmnt = con.prepareStatement("SELECT `cheque_ID` FROM `cheque_details` WHERE `cheque_ID` = " +
 "'" + fini + "' ");
 ResultSet rt = stmnt.executeQuery();
 while (rt.next()){
 dup.add(rt.getString("cheque_ID"));
 }
 if (dup.contains(fini)){
 fini = "chq"+colEnt.getID(colEnt.GetCenter.getCenterID());
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
 for (Entry<String, ArrayList<String>> gcr: colEnt.regGcr.entrySet()) {
 gcr.getValue().remove(getData.getGCR());
 }
 colEnt.monthGCR.get(getData.getMonth()).remove(getData.getGCR());
 colEnt.dateGCR.get(getData.getDate()).get(getData.getMonth()).remove(getData.getGCR());
 colEnt.count--;

 }
 }
 */
}
