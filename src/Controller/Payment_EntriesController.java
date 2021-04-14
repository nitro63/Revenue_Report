/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetCollectEnt;
import Controller.Gets.GetFunctions;
import Controller.Gets.GetRevCenter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import org.apache.commons.lang3.StringUtils;
import revenue_report.DBConnection;
import revenue_report.Main;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class Payment_EntriesController implements Initializable {

    @FXML
    private AnchorPane CollectionEntPane;
    @FXML
    private Button btnClose;
    @FXML
    private DatePicker entDatePck;
    @FXML
    private TextField txtEntAmt;
    @FXML
    private Button btnEnter;
    @FXML
    private Button btnClear;
    @FXML
    private TableView<GetCollectEnt> tblCollection;
    @FXML
    private TableColumn<GetCollectEnt, String> colCENTER;
    @FXML
    private TableColumn<GetCollectEnt, String> colGCR;
    @FXML
    private TableColumn<GetCollectEnt, String> colAMOUNT;
    @FXML
    private TableColumn<GetCollectEnt, String> colDATE;
    @FXML
    private TableColumn<GetCollectEnt, String> colMonth;
    @FXML
    private Button btnSaveEntries;
    @FXML
    private Label lblDeleteWarn;
    @FXML
    private Label lblEdit;
    @FXML
    private Label lblDup;
    @FXML
    private JFXButton btnDelete;

    
    GetCollectEnt getData, getReport;
    GetFunctions getFunctions = new GetFunctions();
    
    entries_sideController app;
    
    public final GetRevCenter GetCenter;
   
        
        Map<String, ArrayList<String>> registerItem = new HashMap<>();
        ObservableList<String> paymentType = FXCollections.observableArrayList("Cash", "Cheque",
                "Cash Deposit Slip","Cheque Deposit Slip");
        ObservableList<String> collectionMonth = FXCollections.observableArrayList("January", "February", "March"
                , "April", "May", "June", "July", "August", "September", "October", "November", "December");
        
         boolean Condition = true;
    private final Connection con;
    private PreparedStatement stmnt;

    String regex = "(?<=[\\d])(,)(?=[\\d])";
   public Pattern p = Pattern.compile(regex);
//    public  Stage stg;
        
        String Date, Item, Code, Month, Amount, Week, Year, RevCent, GCR, Type;
    Map<String, ArrayList<String>> regGcr = new HashMap<>();
    Map<String, ArrayList<String>> monthGCR = new HashMap<>();
    Map<String, Map<String, ArrayList<String>>> dateGCR = new HashMap<>();
    Map<String, ArrayList<String>> typeSerials = new HashMap<>();
    Map<String, String> codeGCR = new HashMap<>();
    Map<String, String> gcrID = new HashMap<>();
    int count;
    @FXML
    private TextField txtEntGCR;
    @FXML
    private TableColumn<GetCollectEnt, String> colYEAR;
    @FXML
    private ComboBox<String> cmbColMonth;
    @FXML
    private ComboBox<String> cmbPayType;
    @FXML
    private TableColumn<GetCollectEnt, String> colPayType;
    @FXML
    private Spinner<Integer> spnColYear;

    /**
     * Initializes the controller class.
     * @param GetCenter
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */

    public Payment_EntriesController(GetRevCenter GetCenter) throws SQLException, ClassNotFoundException{
        this.GetCenter = GetCenter;
        this.con = DBConnection.getConn();
    }



    public void setappController(entries_sideController app){
        this.app = app;
    }
     public entries_sideController getentries_sideController(){
         return app;
     }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Calendar cal = Calendar.getInstance();
        spnColYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2090, cal.get
                (Calendar.YEAR)));
        cmbColMonth.setItems(collectionMonth);
        cmbPayType.setItems(paymentType);
        cmbColMonth.setVisibleRowCount(4);
        cmbPayType.setVisibleRowCount(3);
        tblCollection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblCollection.setOnMouseClicked(e ->{
            lblDeleteWarn.setVisible(false);
            lblDup.setVisible(false);
            lblEdit.setVisible(false);
        });
        typeSerials.put("Cheque", new ArrayList<>());
        typeSerials.put("Cheque Deposit Slip", new ArrayList<>());
    }  
        
    @FXML
    private void showClose(ActionEvent event) {        
        getentries_sideController().getappController().getCenterPane().getChildren().clear();
    }

    @FXML
    private void processKeyEvent(KeyEvent event) {
        String c = event.getCharacter();
    if("1234567890.".contains(c)) {}
    else {
        event.consume();
    }
    }

    @FXML
    private void saveEntries(ActionEvent event) {
      RevCent = GetCenter.getRevCenter();
      GCR = txtEntGCR.getText();
      Type = cmbPayType.getSelectionModel().getSelectedItem();
      registerItem.put("", new ArrayList());
               
        LocalDate date = entDatePck.getValue();// Assigning the date picker value to a variable 
        //Making sure Datepicker is never empty :)
         if(entDatePck.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Date");
                alert.showAndWait();
                
                Condition = false;
            }else{
             Condition =true;
        Date = getFunctions.getDate(date);
        Year = Integer.toString(spnColYear.getValue());
        Month = cmbColMonth.getSelectionModel().getSelectedItem();
       while(Condition) {
            String i=txtEntGCR.getText();
           if(registerItem.containsKey(Type) && registerItem.get(Type).contains(i)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Revenue already entered");
                alert.showAndWait();
                
                Condition =false;
            }else if(txtEntAmt.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please enter Amount");
                alert.showAndWait();
                
                Condition =false;
            }else if(txtEntGCR.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please enter GCR number");
                alert.showAndWait();
                
                Condition =false;
            }else if(RevCent == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Revenue Center");
                alert.showAndWait();
                Condition =false;
                
            }
            else if(txtEntAmt.getText().startsWith(".") && txtEntAmt.getText().endsWith(".")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Enter Amount");
                alert.showAndWait();
                Condition =false;
                
           
            }else if(cmbPayType.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please select PAYMENT TYPE");
                alert.showAndWait();
                Condition =false;
                
                
            }else if(cmbColMonth.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Collection Month");
                alert.showAndWait();
                Condition =false;
                
                
            }else if(spnColYear.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Collection Year");
                alert.showAndWait();
                Condition =false;
                
                
            }else if(StringUtils.countMatches(txtEntAmt.getText(), ".") >1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Enter Amount");
                alert.setContentText("Please check the number of '.' in the amount");
                alert.showAndWait();
                Condition =false;
            }
            else if(txtEntAmt.getText().isEmpty()|| txtEntGCR.getText().isEmpty() || registerItem.containsKey(Type) &&
                   registerItem.get(Type).contains(i)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Make All Entries");
                alert.showAndWait();
                Condition =false;
//                }
            }else if(txtEntGCR.getText().length() > 10 || txtEntGCR.getText().length() < 7){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Check GCR number");
                alert.showAndWait();
                
                Condition =false;
            }
            else{
        colAMOUNT.setCellValueFactory(data -> data.getValue().AmountProperty());
        colDATE.setCellValueFactory(data -> data.getValue().DateProperty());
        colGCR.setCellValueFactory(data -> data.getValue().GCRProperty());
        colMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
        colCENTER.setCellValueFactory(data -> data.getValue().CenterProperty());
        colYEAR.setCellValueFactory(data -> data.getValue().YearProperty());
        colPayType.setCellValueFactory(data -> data.getValue().TypeProperty());
        double initeAmount = Double.parseDouble(txtEntAmt.getText());
        NumberFormat formatter = new DecimalFormat("#,###.00");
        String initAmount = formatter.format(initeAmount);
        Amount= initAmount;
        if("0.00".equals(Amount)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Amount cannot be '0'");
                alert.showAndWait();
                Condition =false;
                
            }else{
            getReport = new GetCollectEnt(RevCent, Amount, GCR, Month, Date, Year, Type);
            tblCollection.getItems().add(getReport);
            Condition = false;
            entDatePck.setValue(null);
            txtEntGCR.clear();
            txtEntAmt.clear();
            cmbColMonth.getSelectionModel().clearSelection();
            cmbPayType.getSelectionModel().clearSelection();

                if(!registerItem.containsKey(Type)){
                    registerItem.put(Type, new ArrayList());
                    registerItem.get(Type).add(i);
                }else if(registerItem.containsKey(Type) && !registerItem.get(Type).contains(i)){
                    registerItem.get(Type).add(i);
                }
        }
            }
       }
       
         }
    }

    @FXML
    private void clearEntries(ActionEvent event) {
        if (tblCollection.getSelectionModel().isEmpty()){
            lblEdit.setText("Please select a row in the table to "+'"'+"Edit"+'"');
            lblEdit.setVisible(true);
        }else{
            String regex = "(?<=[\\d])(,)(?=[\\d])";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(tblCollection.getSelectionModel().getSelectedItem().getAmount());
            cmbColMonth.getSelectionModel().select(tblCollection.getSelectionModel().getSelectedItem().getMonth());
            cmbPayType.getSelectionModel().select(tblCollection.getSelectionModel().getSelectedItem().getType());
            entDatePck.setValue(LocalDate.parse(tblCollection.getSelectionModel().getSelectedItem().getDate(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            txtEntAmt.setText(m.replaceAll(""));
            txtEntGCR.setText(tblCollection.getSelectionModel().getSelectedItem().getGCR());
            if (registerItem.get(cmbPayType.getSelectionModel().getSelectedItem()).contains(txtEntGCR.getText())){
                registerItem.get(cmbPayType.getSelectionModel().getSelectedItem()).remove(txtEntGCR.getText());
            }
            ObservableList<GetCollectEnt> selectedRows = tblCollection.getSelectionModel().getSelectedItems();
            ArrayList<GetCollectEnt> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> tblCollection.getItems().remove(row));
        }
    }

    public String getID(){
        Date day = new Date();
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
                fini = is+ID+si.concat(Day).concat(date).concat(month).
                        concat(year).concat(hour).concat(minute).concat(second).concat(milli);
        return fini;
    }

    @FXML
    private void SaveToDatabase(ActionEvent event) throws SQLException {
        getData = new GetCollectEnt();
         ArrayList<String> duplicate = new ArrayList<>();
         String fini;
        
        for(int h = 0; h<=tblCollection.getItems().size(); h++){
            System.out.println(/*h+"\n"+*/tblCollection.getItems().size());
            if (h != tblCollection.getItems().size()) {
            getData = tblCollection.getItems().get(h);
            int acGCR = Integer.parseInt(getData.getGCR());
            String amount = getData.getAmount();
            Matcher m = p.matcher(amount);
            amount = m.replaceAll("");
            float acAMOUNT =Float.parseFloat(amount);
            String acDate =getData.getDate();
            String acMonth =getData.getMonth();
            String acCENTER =getData.getCenter();
            String acType = getData.getType();
            String acYear = getData.getYear();
            fini = getID();
            int acYEAR = Integer.parseInt(getData.getYear());

                if (acType.equals("Cheque") || acType.equals("Cheque Deposit Slip")) {
                    count++;

                    if (dateGCR.isEmpty() || !dateGCR.containsKey(acDate)) {
                        dateGCR.put(acDate, new HashMap<>());
                        dateGCR.get(acDate).put(acMonth, new ArrayList<>());
                        if (acType.equals("Cheque")){
                        dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR());
                        }else {
                            dateGCR.get(acDate).get(acMonth).add("ChqDps-"+getData.getGCR());
                        }
                    } else if (dateGCR.containsKey(acDate) && !dateGCR.get(acDate).containsKey(acMonth)) {
                        dateGCR.get(acDate).put(acMonth, new ArrayList<>());
                        if (acType.equals("Cheque")){
                        dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR());
                        }else {
                            dateGCR.get(acDate).get(acMonth).add("ChqDps-"+getData.getGCR());
                        }
                    } else if (dateGCR.containsKey(acDate) && dateGCR.get(acDate).containsKey(acMonth) && !dateGCR.
                            get(acDate).get(acMonth).contains("Chq-" + getData.getGCR())
                            && acType.equals("Cheque")) {
                        dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR());
                    }else if (dateGCR.containsKey(acDate) && dateGCR.get(acDate).containsKey(acMonth) && !dateGCR.
                            get(acDate).get(acMonth).contains("ChqDps-" + getData.getGCR())
                            && acType.equals("Cheque Deposit Slip")) {
                        dateGCR.get(acDate).get(acMonth).add("ChqDps-"+getData.getGCR());
                    }


                    if (monthGCR.isEmpty() || !monthGCR.containsKey(acMonth)) {
                        monthGCR.put(acMonth, new ArrayList<>());
                        if (acType.equals("Cheque")){
                        monthGCR.get(acMonth).add("Chq-"+getData.getGCR());
                        }else {
                            monthGCR.get(acMonth).add("ChqDps-"+getData.getGCR());
                        }
                    } else if (monthGCR.containsKey(acMonth) && !monthGCR.get(acMonth).contains("Chq-" + getData.getGCR())
                            && acType.equals("Cheque")) {
                        monthGCR.get(acMonth).add("Chq-"+getData.getGCR());
                    }else if (monthGCR.containsKey(acMonth) && !monthGCR.get(acMonth).contains("ChqDps-" + getData.getGCR())
                            && acType.equals("Cheque Deposit Slip")) {
                        monthGCR.get(acMonth).add("ChqDps-"+getData.getGCR());
                    }

                    if (regGcr.isEmpty() || !regGcr.containsKey(acYear)) {
                        regGcr.put(acYear, new ArrayList<>());
                        if (acType.equals("Cheque")) {
                            regGcr.get(acYear).add("Chq-" + getData.getGCR());
                            codeGCR.put("Chq-" + getData.getGCR(), getData.getGCR());
                        } else {
                            regGcr.get(acYear).add("ChqDpS-" + getData.getGCR());
                            codeGCR.put("ChqDpS-" + getData.getGCR(), getData.getGCR());
                        }
                    } else if (regGcr.containsKey(acYear) && !regGcr.get(acYear).contains("Chq-" + getData.getGCR()) &&
                            acType.equals("Cheque")) {
                            regGcr.get(acYear).add("Chq-" + getData.getGCR());
                        codeGCR.put("Chq-" + getData.getGCR(), getData.getGCR());
                    }else if (regGcr.containsKey(acYear) && !regGcr.get(acYear).contains("ChqDps-" + getData.getGCR())
                            && acType.equals("Cheque Deposit Slip")) {
                            regGcr.get(acYear).add("ChqDpS-" + getData.getGCR());
                            codeGCR.put("ChqDpS-" + getData.getGCR(), getData.getGCR());
                    }
                }
                stmnt = con.prepareStatement("SELECT * FROM `collection_payment_entries` WHERE  " +
                        "`pay_revCenter` = '" + acCENTER + "' AND `GCR` = '" + acGCR + "' AND `payment_type` = '" + acType + "'");
                ResultSet res = stmnt.executeQuery();
                while (res.next()) {
                    duplicate.add(res.getString("GCR"));
                }
                if (duplicate.contains(getData.getGCR())) {
                    lblDup.setText('"' + getData.getGCR() + '"' + " of payment method " + '"' + acType + '"' + " already exist. Please select " +
                            "row of duplicate data in table to edit or delete.");
                    lblDup.setVisible(true);
                    h = tblCollection.getItems().size() + 1;
                } else {
                    boolean condition = true;
                    ArrayList<String> dup = new ArrayList<>();
                    while (condition) {
                        stmnt = con.prepareStatement("SELECT `pay_ID` FROM `collection_payment_entries` WHERE `pay_ID` = " +
                                "'" + fini + "' ");
                        ResultSet rt = stmnt.executeQuery();
                        while (rt.next()) {
                            dup.add(rt.getString("pay_ID"));
                        }
                        if (dup.contains(fini)) {
                            fini = getID();
                        } else {
                            condition = false;
                        }
                    }
                    if (acType.equals("Cheque")) {
                        gcrID.put("Chq-" + getData.getGCR(), fini);
                    }
                    if (acType.equals("Cheque Deposit Slip")) {
                        gcrID.put("ChqDpS-" + getData.getGCR(), fini);
                    }

                    if (acType.equals("Cheque") || acType.equals("Cheque Deposit Slip")) {
                        if (!typeSerials.get(acType).contains(fini)) {
                            typeSerials.get(acType).add(fini);
                        }
                    }
                    stmnt = con.prepareStatement("INSERT INTO `collection_payment_entries`(`pay_revCenter`, `GCR`," +
                            " `Amount`, `Date`, `Month`, `Year`, `payment_type`, `pay_ID`) VALUES ('" + acCENTER + "', '" +
                            acGCR + "' ,'" + acAMOUNT + "', '" + acDate + "', '" + acMonth + "', '" + acYEAR + "', '" +
                            acType + "', '" + fini + "')");
                    stmnt.executeUpdate();
                }
            }else {
                tblCollection.getItems().clear();
            }
        }
        if(!regGcr.isEmpty()){
            System.out.println(typeSerials);
            Main st =new Main();
            try {
                FXMLLoader bankDetails = new FXMLLoader();
                bankDetails.setLocation(getClass().getResource("/Views/fxml/Bank_DetailsEntries.fxml"));
                bankDetails.setController(new Bank_DetailsEntriesController());
                Bank_DetailsEntriesController bnkDtls = (Bank_DetailsEntriesController)bankDetails.getController();
                bnkDtls.setAppController(this);
                Parent root = bankDetails.load();
                Scene s = new Scene(root);
                Stage stg = new Stage();
                bnkDtls.setStage(stg);
                stg.initModality(Modality.APPLICATION_MODAL);
                stg.initOwner(st.stage);
                stg.initStyle(StageStyle.UTILITY);
                stg.setScene(s);
                stg.show();
                stg.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
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
                        closeConfirmation.setHeaderText("Confirm Exit");
                        if(!bnkDtls.getTableView().getItems().isEmpty()) {
                            closeConfirmation.setContentText("Are you sure you want to exit without saving cheque details? ");
                        }else{
                            closeConfirmation.setContentText("Are you sure you want to Exit");
                        }
                        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
                        closeConfirmation.initOwner(stg);
                        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
                        if (!ButtonType.OK.equals(closeResponse.get())) {
                            we.consume();
                        }else{
                            regGcr.clear();
                            count = 0;

                        }
                    }
                });

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        
    }



    @FXML
    private void CancelEntries(ActionEvent event) {
        tblCollection.getItems().clear();
    }

    @FXML
    private void processKeyGCR(KeyEvent event) {
        String c = event.getCharacter();
    if("1234567890".contains(c)) {}
    else {
        event.consume();
    }
    }

    public void deleteSelection(ActionEvent actionEvent) {
        if(tblCollection.getSelectionModel().isEmpty()){
            lblDeleteWarn.setVisible(true);
        }else{
        ObservableList<GetCollectEnt> selectedRows = tblCollection.getSelectionModel().getSelectedItems();
        ArrayList<GetCollectEnt> rows = new ArrayList<>(selectedRows);
        rows.forEach(row -> tblCollection.getItems().remove(row));
        }
    }
    
}
