/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.Gets.GetCollectEnt;
import com.Controller.Gets.GetFunctions;
import com.Controller.Gets.GetRevCenter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSnackbar;
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
import javafx.scene.text.Text;
import javafx.stage.*;
import org.apache.commons.lang3.StringUtils;
import com.revenue_report.DBConnection;
import com.revenue_report.Main;

/**
 * FXML com.Controller class
 *
 * @author HP
 */
public class Payment_EntriesController implements Initializable {

    @FXML
    private AnchorPane anchPane;
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
    private TableColumn<GetCollectEnt, String> colID;
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
    private Text lblDeleteWarn;
    @FXML
    private Text lblEdit;
    @FXML
    private Text lblDup;
    @FXML
    private Button btnClearEntr;
    @FXML
    private JFXButton btnDelete;

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
    private ComboBox<String> cmbUpdateMonth;

    @FXML
    private JFXButton btnFetchUpdate;

    @FXML
    private Label lblControlWarn;
    
    private GetCollectEnt getData, getReport;
    private final GetFunctions getFunctions = new GetFunctions();
    
    private entries_sideController app;
    
    public final GetRevCenter GetCenter;
   
    protected TableView<GetCollectEnt> getTableView(){
        return tblCollection;
    }
      private  Map<String, ArrayList<String>> registerItem = new HashMap<>();
    private  Map<String, String> dateGCR1 = new HashMap<>();
    private final ObservableList<String> paymentType = FXCollections.observableArrayList("Cash", "Cheque",
                "Cash Deposit Slip","Cheque Deposit Slip");
    private final ObservableList<String> collectionMonth = FXCollections.observableArrayList("January", "February", "March"
                , "April", "May", "June", "July", "August", "September", "October", "November", "December");
        
      private   boolean Condition = true, typeA_B, typeB_A, typeB_B, update, delete;
    private final Connection con;
    private Matcher m;
    private PreparedStatement stmnt;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private ResultSet rs;

   private final String regex = "(?<=[\\d])(,)(?=[\\d])";
   public Pattern p = Pattern.compile(regex);
   private byte chk , chkd , chk1 , chkd1 ;
        
   private     String Date, Item, Code, Month, Amount, Week, Year, RevCent, RevCentID, GCR, Type, type, entriesID;
    protected Map<String, ArrayList<String>> regGcr = new HashMap<>();
    protected Map<String, ArrayList<String>> monthGCR = new HashMap<>();
    protected Map<String, Map<String, ArrayList<String>>> dateGCR = new HashMap<>();
    protected Map<String, Map<String, String>> payChq = new HashMap<>();
    protected Map<String, ArrayList<String>> typeSerials = new HashMap<>();
    protected Map<String, String> codeGCR = new HashMap<>();
    protected Map<String, String> amountGCR = new HashMap<>();
    protected Map<String, String> gcrID = new HashMap<>();
    protected int count, typePB = 0;
    private Calendar cal;
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
    private JFXSnackbar s;

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

    enum PType {
        A, B
    }
    PType Ptype, newType;

    public void setappController(entries_sideController app){
        this.app = app;
    }
     public entries_sideController getentries_sideController(){
         return app;
     }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        RevCent = GetCenter.getRevCenter();
        RevCentID = GetCenter.getCenterID();
        cal = Calendar.getInstance();
        spnColYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2090, cal.get
                (Calendar.YEAR)));
        cmbColMonth.setItems(collectionMonth);
        cmbPayType.setItems(paymentType);
        cmbColMonth.setVisibleRowCount(4);
        cmbPayType.setVisibleRowCount(3);
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
              colYEAR.setVisible(false);
              btnEnter.setVisible(false);
              btnClear.setVisible(false);
              btnClearEntr.setVisible(false);
              btnSaveEntries.setVisible(false);
              btnDelete.setVisible(false);
              tblCollection.getItems().clear();
              tblCollection.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
              clear();

          }else {
              cmbUpdateYear.setVisible(false);
              cmbUpdateMonth.setVisible(false);
              btnFetchUpdate.setVisible(false);
              btnClearUpdate.setVisible(false);
              btnDeleteUpdate.setVisible(false);
              btnUpdateEntries.setVisible(false);
              colID.setVisible(false);
              colYEAR.setVisible(true);
              btnEnter.setVisible(true);
              btnClear.setVisible(true);
              btnClearEntr.setVisible(true);
              btnSaveEntries.setVisible(true);
              btnDelete.setVisible(true);
              tblCollection.getItems().clear();
              tblCollection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
              clear();
          }
      });
        tblCollection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblCollection.setOnMouseClicked(e ->{
            if (!chkUpdate.isSelected()){
                lblDeleteWarn.setVisible(false);lblDup.setVisible(false);lblEdit.setVisible(false);
            }else {
                if (tblCollection.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1){
                    setEntries();
                    if (lblControlWarn.isVisible()){
                        lblControlWarn.setVisible(false);
                    }
                }
            }
        });
        typeSerials.put("Cheque", new ArrayList<>());
        typeSerials.put("Cheque Deposit Slip", new ArrayList<>());
    }

  private void GetRevenueYears() throws SQLException {
      stmnt = con.prepareStatement("SELECT `Year` FROM `collection_payment_entries` WHERE `pay_revCenter` = '"+RevCentID+"' GROUP BY `Year`");
      rs = stmnt.executeQuery();
      cmbUpdateYear.getItems().clear();
      while (rs.next()){
          cmbUpdateYear.getItems().add(rs.getString("Year"));
      }
  }

    @FXML
    void selectedYear(ActionEvent event) throws SQLException {
      String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
        stmnt = con.prepareStatement("SELECT `Month` FROM `collection_payment_entries` WHERE `pay_revCenter` = '"+RevCentID+"' AND `Year` ='"+year+"' GROUP BY `Month`");
        rs = stmnt.executeQuery();
        cmbUpdateMonth.getItems().clear();
        while (rs.next()){
            cmbUpdateMonth.getItems().add(rs.getString("Month"));
        }
    }

    @FXML
   void fetchEntries(ActionEvent event) throws SQLException {
        if (!cmbUpdateYear.getSelectionModel().isEmpty()) {
            String year = cmbUpdateYear.getSelectionModel().getSelectedItem(), month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
            if (!cmbUpdateMonth.getSelectionModel().isEmpty()) {
                stmnt = con.prepareStatement("SELECT `pay_ID`, `Date`, `Month`, `GCR`, `payment_type`, `Amount` FROM `collection_payment_entries` WHERE `pay_revCenter` = '" + RevCentID + "' AND `Year` ='" + year + "' AND `Month` = '" + month + "' ");
            } else {
                stmnt = con.prepareStatement("SELECT `pay_ID`, `Date`, `Month`, `GCR`, `payment_type`, `Amount` FROM `collection_payment_entries` WHERE `pay_revCenter` = '" + RevCentID + "' AND `Year` ='" + year + "'");
            }
            rs = stmnt.executeQuery();
            tblCollection.getItems().clear();
            colID.setCellValueFactory(data -> data.getValue().YearProperty());
            colDATE.setCellValueFactory(data -> data.getValue().DateProperty());
            colGCR.setCellValueFactory(data -> data.getValue().GCRProperty());
            colAMOUNT.setCellValueFactory(data -> data.getValue().AmountProperty());
            colMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
            colPayType.setCellValueFactory(data -> data.getValue().TypeProperty());
            while (rs.next()) {
                getData = new GetCollectEnt(rs.getString("Amount"), rs.getString("GCR"), rs.getString("Month"), rs.getString("Date"), rs.getString("pay_ID"),
                        rs.getString("payment_type"));
                tblCollection.getItems().add(getData);

            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Please select Year");
            alert.showAndWait();
        }
    }

    private void setEntries() {
        GetCollectEnt entries = tblCollection.getSelectionModel().getSelectedItem();
        entriesID = entries.getYear();
        type = entries.getType();
        entDatePck.setValue(LocalDate.parse(entries.getDate(), format));
        m = p.matcher(entries.getAmount());
        String amount =  m.replaceAll("");
        txtEntAmt.setText(amount);
        cmbPayType.getSelectionModel().select(entries.getType());
        cmbColMonth.getSelectionModel().select(entries.getMonth());
        spnColYear.getValueFactory().setValue(Integer.parseInt(cmbUpdateYear.getValue()));/*getSelectionModel().getSelectedItem()));*/
        txtEntGCR.setText(entries.getGCR());
        switch (type){
            case "Cash":
            case "Cash Deposit Slip":
                Ptype = PType.A;
                break;
            case "Cheque":
            case "Cheque Deposit Slip":
                Ptype = PType.B;
                break;
        }
    }

    @FXML
    void updateEntries(ActionEvent event) throws SQLException {
        if (entriesID != null){
            if (s != null) {
                s.unregisterSnackbarContainer(anchPane);
            }
            ArrayList<String> dupGCR = new ArrayList<>();
            String payDate = getFunctions.getDate(entDatePck.getValue()),
                    payGCR = txtEntGCR.getText(),
                    payType = cmbPayType.getSelectionModel().getSelectedItem(),
                    payMonth = cmbColMonth.getSelectionModel().getSelectedItem();
            int payYear = spnColYear.getValue();
            float payAmount = Float.parseFloat(txtEntAmt.getText());
            stmnt = con.prepareStatement("SELECT `GCR` FROM `collection_payment_entries` WHERE `GCR` = '"+
                    payGCR+"' AND `pay_ID` != '"+entriesID+"'AND `payment_type` = '"+payType+"' AND `pay_revCenter` = '" +
                    RevCentID+"'");
            ResultSet rt = stmnt.executeQuery();
            while (rt.next()){
                dupGCR.add(rt.getString("GCR"));
            }
            Condition = true;
            if(entDatePck.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Date");
                alert.showAndWait();
                Condition = false;
            }else{
                Condition =true;
                while(Condition) {
                    String i=txtEntGCR.getText();
                    if(dupGCR.contains(i)){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("This record already exists");
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
                    }else if(txtEntAmt.getText().startsWith(".") && txtEntAmt.getText().endsWith(".")){
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
                    }else if(txtEntGCR.getText().length() > 10 || txtEntGCR.getText().length() < 7){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please Check GCR number");
                        alert.showAndWait();
                        Condition =false;
                    }
                    else{
                        switch (type){
                            case "Cash":
                            case "Cash Deposit Slip":
                                newType = PType.A;
                                break;
                            case "Cheque":
                            case "Cheque Deposit Slip":
                                newType = PType.B;
                                break;
                        }
                        if (newType.equals(PType.B) && Ptype.equals(PType.A)){
                            typeA_B = true;
                            typeB_A = false;
                            typeB_B = false;
                        }else if (newType.equals(PType.A) && Ptype.equals(PType.B)){
                            typeA_B = false;
                            typeB_A = true;
                            typeB_B = false;
                        }else if (newType.equals(PType.B) && Ptype.equals(PType.B)){
                            typeA_B = false;
                            typeB_A = false;
                            typeB_B = true;
                        }
                        if(payAmount == 0){
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("Please Amount cannot be '0'");
                            alert.showAndWait();
                            Condition =false;
                        }else{
                            checkPaymentType();
                            if (update){
                            stmnt = con.prepareStatement("UPDATE `collection_payment_entries` SET  " +
                                    "`Date`= '"+payDate+"',`GCR`= '"+payGCR+"'," +
                                    "`Month` = '"+payMonth+"', `payment_type` = '"+payType+"'" +
                                    ",`Amount` = '"+payAmount+"', `Year` = '"+payYear+"' WHERE   " +
                                    "`pay_ID`= '"+entriesID+"' AND `pay_revCenter`= '"+RevCentID+"' ");
                            stmnt.executeUpdate();
                                s = new JFXSnackbar(anchPane);
                                s.setStyle("-fx-text-fill: green");
                                s.show("Update Successful", 3000);
                            clear();
                            loadRevenueCollectionTable();
                            }else {
                                s = new JFXSnackbar(anchPane);
                                    s.setStyle("-fx-text-fill: red");
                                    s.show("Update Unsuccessful", 3000);
                            }
                            Condition = false;


                        }
                    }
                }

            }
        }else{
            lblControlWarn.setVisible(true);
        }

    }
    private void checkPaymentType(){
        update = true;
    }

    void clear(){
        entriesID = null;
        type = null;
        typeA_B = false;
        typeB_A = false;
        entDatePck.setValue(null);
        txtEntAmt.setText(null);
        txtEntGCR.setText(null);
        cmbPayType.getSelectionModel().clearSelection();
        cmbColMonth.getSelectionModel().clearSelection();
        spnColYear.getValueFactory().setValue(cal.get(Calendar.YEAR));
    }



    private void loadRevenueCollectionTable() throws SQLException {
        String year = cmbUpdateYear.getSelectionModel().getSelectedItem(), month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
        if (!cmbUpdateMonth.getSelectionModel().isEmpty()) {
            stmnt = con.prepareStatement("SELECT `pay_ID`, `Date`, `Month`, `GCR`, `payment_type`, `Amount` FROM `collection_payment_entries` WHERE `pay_revCenter` = '" + RevCentID + "' AND `Year` ='" + year + "' AND `Month` = '" + month + "' ");
        } else {
            stmnt = con.prepareStatement("SELECT `pay_ID`, `Date`, `Month`, `GCR`, `payment_type`, `Amount` FROM `collection_payment_entries` WHERE `pay_revCenter` = '" + RevCentID + "' AND `Year` ='" + year + "'");
        }
        rs = stmnt.executeQuery();
        tblCollection.getItems().clear();
        colID.setCellValueFactory(data -> data.getValue().YearProperty());
        colDATE.setCellValueFactory(data -> data.getValue().DateProperty());
        colGCR.setCellValueFactory(data -> data.getValue().GCRProperty());
        colAMOUNT.setCellValueFactory(data -> data.getValue().AmountProperty());
        colMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
        colPayType.setCellValueFactory(data -> data.getValue().TypeProperty());
        while (rs.next()) {
            getData = new GetCollectEnt(rs.getString("Amount"), rs.getString("GCR"), rs.getString("Month"), rs.getString("Date"), rs.getString("pay_ID"),
                    rs.getString("payment_type"));
            tblCollection.getItems().add(getData);

        }
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
      GCR = txtEntGCR.getText();
      Type = cmbPayType.getSelectionModel().getSelectedItem();
      registerItem.put("", new ArrayList<>());
      dateGCR1.put("", "");
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
            boolean trueChq = true;
            if (Type.equals("Cheque") || Type.equals("Cheque Deposit Slip")){
                trueChq = false;
            }
           if(registerItem.containsKey(Type) && registerItem.get(Type).contains(i)&&trueChq){
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
            }else if (dateGCR1.containsKey(GCR) && !dateGCR1.get(GCR).equals(Date)){

               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.setTitle("Warning Dialog");
               alert.setHeaderText("Please check Date.");
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
        colMonth.setCellValueFactory(data -> data.getValue().MonthProperty());/*
        colID.setCellValueFactory(data -> data.getValue().CenterProperty());*/
        colYEAR.setCellValueFactory(data -> data.getValue().YearProperty());
        colPayType.setCellValueFactory(data -> data.getValue().TypeProperty());
        double initeAmount = Double.parseDouble(txtEntAmt.getText());
        NumberFormat formatter = new DecimalFormat("#,###.00");
               Amount= formatter.format(initeAmount);
        if("0.00".equals(Amount)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Amount cannot be '0'");
                alert.showAndWait();
                Condition =false;
                
            }else{
            getReport = new GetCollectEnt(/*RevCent, */Amount, GCR, Month, Date, Year, Type);
            tblCollection.getItems().add(getReport);
            Condition = false;
            entDatePck.setValue(null);
            txtEntGCR.clear();
            txtEntAmt.clear();
            cmbColMonth.getSelectionModel().clearSelection();
            cmbPayType.getSelectionModel().clearSelection();

                if(!registerItem.containsKey(Type)&&trueChq){
                    registerItem.put(Type, new ArrayList<>());
                    registerItem.get(Type).add(i);
                }else if(registerItem.containsKey(Type) && !registerItem.get(Type).contains(i)&&trueChq){
                    registerItem.get(Type).add(i);
                }
                if (!dateGCR1.containsKey(GCR)){
                    dateGCR1.put(GCR, Date);
                }
        }
            }
       }
       
         }
    }

    @FXML
    private void clearEntries(ActionEvent event) {
        if (!chkUpdate.isSelected()){
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
            registerItem.get(cmbPayType.getSelectionModel().getSelectedItem()).remove(txtEntGCR.getText());
            ObservableList<GetCollectEnt> selectedRows = tblCollection.getSelectionModel().getSelectedItems();
            ArrayList<GetCollectEnt> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> tblCollection.getItems().remove(row));
        }
        }else{
            clear();
        }
    }

    public String getID(String Center){
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
                is = id.replace("-", "").substring(9,11),
                ID = id.replace("-", "").substring(17,19),
                si = id.replace("-", "").substring(23,25);
        return Center.substring(2, 5) + is+ID+si.concat(date).concat(month).
                concat(year.substring(2,4)).concat(hour).concat(minute);
    }

    protected void saveTableItems() throws SQLException {
        getData = new GetCollectEnt();
        ArrayList<String> duplicate = new ArrayList<>();
        String fini;for(int h = 0; h<tblCollection.getItems().size(); h++){
            if (h != tblCollection.getItems().size()) {
            System.out.println(tblCollection.getItems().size());
            getData = tblCollection.getItems().get(h);
            int acGCR = Integer.parseInt(getData.getGCR());
            String amount = getData.getAmount();
            Matcher m = p.matcher(amount);
            amount = m.replaceAll("");
            float acAMOUNT =Float.parseFloat(amount);
            String acDate =getData.getDate();
            String acMonth =getData.getMonth();
            String acCENTER =RevCent;
            String acType = getData.getType();
            String acYear = getData.getYear();
            fini = getID(RevCentID);
            int acYEAR = Integer.parseInt(getData.getYear());

            if (acType.equals("Cheque") || acType.equals("Cheque Deposit Slip")) {
                count++;

                if (dateGCR.isEmpty() || !dateGCR.containsKey(acDate)) {
                    dateGCR.put(acDate, new HashMap<>());
                    dateGCR.get(acDate).put(acMonth, new ArrayList<>());
                    if (acType.equals("Cheque")){
                        dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR());
                    }else {
                        dateGCR.get(acDate).get(acMonth).add("ChqDpS-"+getData.getGCR());
                    }
                } else if (dateGCR.containsKey(acDate) && !dateGCR.get(acDate).containsKey(acMonth)) {
                    dateGCR.get(acDate).put(acMonth, new ArrayList<>());
                    if (acType.equals("Cheque")){
                        dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR());
                    }else {
                        dateGCR.get(acDate).get(acMonth).add("ChqDpS-"+getData.getGCR());
                    }
                } else if (dateGCR.containsKey(acDate) && dateGCR.get(acDate).containsKey(acMonth) && !dateGCR.
                        get(acDate).get(acMonth).contains("Chq-" + getData.getGCR())
                        && acType.equals("Cheque")) {
                    dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR());
                }else if (dateGCR.containsKey(acDate) && dateGCR.get(acDate).containsKey(acMonth) && !dateGCR.
                        get(acDate).get(acMonth).contains("ChqDpS-" + getData.getGCR())
                        && acType.equals("Cheque Deposit Slip")) {
                    dateGCR.get(acDate).get(acMonth).add("ChqDpS-"+getData.getGCR());
                }


                if (monthGCR.isEmpty() || !monthGCR.containsKey(acMonth)) {
                    monthGCR.put(acMonth, new ArrayList<>());
                    if (acType.equals("Cheque")){
                        monthGCR.get(acMonth).add("Chq-"+getData.getGCR());
                    }else {
                        monthGCR.get(acMonth).add("ChqDpS-"+getData.getGCR());
                    }
                } else if (monthGCR.containsKey(acMonth) && !monthGCR.get(acMonth).contains("Chq-" + getData.getGCR())
                        && acType.equals("Cheque")) {
                    monthGCR.get(acMonth).add("Chq-"+getData.getGCR());
                }else if (monthGCR.containsKey(acMonth) && !monthGCR.get(acMonth).contains("ChqDpS-" + getData.getGCR())
                        && acType.equals("Cheque Deposit Slip")) {
                    monthGCR.get(acMonth).add("ChqDpS-"+getData.getGCR());
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
                }else if (regGcr.containsKey(acYear) && !regGcr.get(acYear).contains("ChqDpS-" + getData.getGCR())
                        && acType.equals("Cheque Deposit Slip")) {
                    regGcr.get(acYear).add("ChqDpS-" + getData.getGCR());
                    codeGCR.put("ChqDpS-" + getData.getGCR(), getData.getGCR());
                }
            }
            stmnt = con.prepareStatement("SELECT `GCR` FROM `revenue_centers`,`collection_payment_entries` WHERE `revenue_centers`.`CenterID` = `collection_payment_entries`.`pay_revCenter` AND `revenue_centers`.`revenue_center`= '" + acCENTER + "' AND `GCR` = '" + acGCR + "' AND `payment_type` = '" + acType + "'");
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
                    stmnt = con.prepareStatement("SELECT `pay_ID` FROM `collection_payment_entries` WHERE `pay_ID` = '" + fini + "' ");
                    ResultSet rt = stmnt.executeQuery();
                    while (rt.next()) {
                        dup.add(rt.getString("pay_ID"));
                    }
                    if (dup.contains(fini)) {
                        fini = getID(RevCentID);
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
                if (!acType.equals("Cheque") && !acType.equals("Cheque Deposit Slip")){
                    stmnt = con.prepareStatement("INSERT INTO `collection_payment_entries`(`pay_revCenter`, `GCR`," +
                            " `Amount`, `Date`, `Month`, `Year`, `payment_type`, `pay_ID`) VALUES ('" + RevCentID + "', '" +
                            acGCR + "' ,'" + acAMOUNT + "', '" + acDate + "', '" + acMonth + "', '" + acYEAR + "', '" +
                            acType + "', '" + fini + "')");
                    stmnt.executeUpdate();
                    tblCollection.getItems().remove(h);
                }/*else {
                }*/
            }
            }else {
                if (!tblCollection.getItems().isEmpty()){
                    if(!regGcr.isEmpty()){
                        Main st =new Main();
                        try {
                            FXMLLoader bankDetails = new FXMLLoader();
                            bankDetails.setLocation(getClass().getResource("/com/Views/fxml/Bank_DetailsEntries.fxml"));
                            bankDetails.setController(new Bank_DetailsEntriesController());
                            Bank_DetailsEntriesController bnkDtls = bankDetails.getController();
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
                            System.out.println("Yes I run only after you are done");

                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @FXML
    private void SaveToDatabase(ActionEvent event) throws SQLException {
        getData = new GetCollectEnt();
         ArrayList<String> duplicate = new ArrayList<>();
         String fini;
        
        for(int h = 0; h<tblCollection.getItems().size(); h++){
//            if (h != tblCollection.getItems().size()) {
            getData = tblCollection.getItems().get(h);
            int acGCR = Integer.parseInt(getData.getGCR());
            String amount = getData.getAmount();
            Matcher m = p.matcher(amount);
            amount = m.replaceAll("");
            float acAMOUNT =Float.parseFloat(amount);
            String acDate =getData.getDate();
            String acMonth =getData.getMonth();
            String acCENTER =RevCent;
            String acType = getData.getType();
            String acYear = getData.getYear(), typeGCR = null;
            fini = getID(RevCentID);
            int acYEAR = Integer.parseInt(getData.getYear());
                if (acType.equals("Cheque") || acType.equals("Cheque Deposit Slip")) {
                    chk = 0; chkd = 0;
                    count++;
                    if (regGcr.isEmpty() || !regGcr.containsKey(acYear)) {
                        regGcr.put(acYear, new ArrayList<>());
                        if (acType.equals("Cheque")) {
                            regGcr.get(acYear).add("Chq-" + getData.getGCR());
                            typeGCR = "Chq-"+getData.getGCR();
                            codeGCR.put("Chq-" + getData.getGCR(), getData.getGCR());
                        } else {
                            regGcr.get(acYear).add("ChqDpS-" + getData.getGCR());
                            typeGCR = "ChqDpS-"+getData.getGCR();
                            codeGCR.put("ChqDpS-" + getData.getGCR(), getData.getGCR());
                        }
                    } else if (regGcr.containsKey(acYear) && !regGcr.get(acYear).contains("Chq-" + getData.getGCR()) &&
                            acType.equals("Cheque")) {
                        regGcr.get(acYear).add("Chq-" + getData.getGCR());
                        typeGCR = "Chq-"+getData.getGCR();
                        codeGCR.put("Chq-" + getData.getGCR(), getData.getGCR());
                    }else if (regGcr.containsKey(acYear) && !regGcr.get(acYear).contains("ChqDpS-" + getData.getGCR())
                            && acType.equals("Cheque Deposit Slip")) {
                        regGcr.get(acYear).add("ChqDpS-" + getData.getGCR());
                        typeGCR = "ChqDpS-"+getData.getGCR();
                        codeGCR.put("ChqDpS-" + getData.getGCR(), getData.getGCR());
                    }else if (regGcr.containsKey(acYear) && regGcr.get(acYear).contains("ChqDpS-" + getData.getGCR())
                            && acType.equals("Cheque Deposit Slip")) {
                        chkd++;
                        regGcr.get(acYear).add("ChqDpS-" + getData.getGCR()+chkd);
                        typeGCR = "ChqDpS-"+getData.getGCR()+chkd;
                        codeGCR.put("ChqDpS-" + getData.getGCR()+chkd, getData.getGCR());
                    } else if (regGcr.containsKey(acYear) && regGcr.get(acYear).contains("Chq-" + getData.getGCR()) &&
                            acType.equals("Cheque")) {
                        chk++;
                        regGcr.get(acYear).add("Chq-" + getData.getGCR()+chk);
                        typeGCR = "Chq-"+getData.getGCR()+chk;
                        codeGCR.put("Chq-" + getData.getGCR()+chk, getData.getGCR());
                    }
                    if (dateGCR.isEmpty() || !dateGCR.containsKey(acDate)) {
                        dateGCR.put(acDate, new HashMap<>());
                        dateGCR.get(acDate).put(acMonth, new ArrayList<>());
                        if (acType.equals("Cheque")){
                        dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR());
                        }else {
                            dateGCR.get(acDate).get(acMonth).add("ChqDpS-"+getData.getGCR());
                        }
                    } else if (dateGCR.containsKey(acDate) && !dateGCR.get(acDate).containsKey(acMonth)) {
                        dateGCR.get(acDate).put(acMonth, new ArrayList<>());
                        if (acType.equals("Cheque")){
                        dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR());
                        }else {
                            dateGCR.get(acDate).get(acMonth).add("ChqDpS-"+getData.getGCR());
                        }
                    } else if (dateGCR.containsKey(acDate) && dateGCR.get(acDate).containsKey(acMonth) && !dateGCR.
                            get(acDate).get(acMonth).contains("Chq-" + getData.getGCR())
                            && acType.equals("Cheque")) {
                        dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR());
                    } else if (dateGCR.containsKey(acDate) && dateGCR.get(acDate).containsKey(acMonth) && dateGCR.
                            get(acDate).get(acMonth).contains("Chq-" + getData.getGCR())
                            && acType.equals("Cheque")) {
                        dateGCR.get(acDate).get(acMonth).add("Chq-"+getData.getGCR()+chk);
                    }else if (dateGCR.containsKey(acDate) && dateGCR.get(acDate).containsKey(acMonth) && !dateGCR.
                            get(acDate).get(acMonth).contains("ChqDpS-" + getData.getGCR())
                            && acType.equals("Cheque Deposit Slip")) {
                        dateGCR.get(acDate).get(acMonth).add("ChqDpS-"+getData.getGCR());
                    }else if (dateGCR.containsKey(acDate) && dateGCR.get(acDate).containsKey(acMonth) && dateGCR.
                            get(acDate).get(acMonth).contains("ChqDpS-" + getData.getGCR())
                            && acType.equals("Cheque Deposit Slip")) {
                        dateGCR.get(acDate).get(acMonth).add("ChqDpS-"+getData.getGCR()+chkd);
                    }


                    if (monthGCR.isEmpty() || !monthGCR.containsKey(acMonth)) {
                        monthGCR.put(acMonth, new ArrayList<>());
                        if (acType.equals("Cheque")){
                        monthGCR.get(acMonth).add("Chq-"+getData.getGCR());
                        }else {
                            monthGCR.get(acMonth).add("ChqDpS-"+getData.getGCR());
                        }
                    } else if (monthGCR.containsKey(acMonth) && !monthGCR.get(acMonth).contains("Chq-" + getData.getGCR())
                            && acType.equals("Cheque")) {
                        monthGCR.get(acMonth).add("Chq-"+getData.getGCR());
                    }else if (monthGCR.containsKey(acMonth) && !monthGCR.get(acMonth).contains("ChqDpS-" + getData.getGCR())
                            && acType.equals("Cheque Deposit Slip")) {
                        monthGCR.get(acMonth).add("ChqDpS-"+getData.getGCR());
                    } else if (monthGCR.containsKey(acMonth) && monthGCR.get(acMonth).contains("Chq-" + getData.getGCR())
                            && acType.equals("Cheque")) {
                        monthGCR.get(acMonth).add("Chq-"+getData.getGCR()+chk);
                    }else if (monthGCR.containsKey(acMonth) && monthGCR.get(acMonth).contains("ChqDpS-" + getData.getGCR())
                            && acType.equals("Cheque Deposit Slip")) {
                        monthGCR.get(acMonth).add("ChqDpS-"+getData.getGCR()+chkd);
                    }/*
                    if (amountGCR.isEmpty() || !amountGCR.containsKey(acMonth)) {

                    }*/

                }
                stmnt = con.prepareStatement("SELECT `GCR` FROM `revenue_centers`,`collection_payment_entries` WHERE `revenue_centers`.`CenterID` = `collection_payment_entries`.`pay_revCenter` AND `revenue_centers`.`revenue_center`= '" + acCENTER + "' AND `GCR` = '" + acGCR + "' AND `payment_type` = '" + acType + "'");
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
                        stmnt = con.prepareStatement("SELECT `pay_ID` FROM `collection_payment_entries` WHERE `pay_ID` = '" + fini + "' ");
                        ResultSet rt = stmnt.executeQuery();
                        while (rt.next()) {
                            dup.add(rt.getString("pay_ID"));
                        }
                        if (dup.contains(fini)) {
                            fini = getID(RevCentID);
                        } else {
                            condition = false;
                        }
                    }
                    chk1 =0; chkd1 = 0;
                    if (acType.equals("Cheque")) {
                        if (!gcrID.containsKey("Chq-" + getData.getGCR())) {
                            gcrID.put("Chq-" + getData.getGCR(), fini);
                        }else{
                            chk1++;
                            gcrID.put("Chq-" + getData.getGCR()+chk1, fini);}
                    }
                    if (acType.equals("Cheque Deposit Slip")) {
                        if (!gcrID.containsKey("ChqDpS-" + getData.getGCR())){
                        gcrID.put("ChqDpS-" + getData.getGCR(), fini);}
                        else{
                            chkd1++;
                            gcrID.put("ChqDpS-" + getData.getGCR()+chkd1, fini);
                        }
                    }

                    if (acType.equals("Cheque") || acType.equals("Cheque Deposit Slip")) {
                        if (!typeSerials.get(acType).contains(fini)) {
                            typeSerials.get(acType).add(fini);
                        }
                    }
                    if (!acType.equals("Cheque") && !acType.equals("Cheque Deposit Slip")){
                    stmnt = con.prepareStatement("INSERT INTO `collection_payment_entries`(`pay_revCenter`, `GCR`," +
                            " `Amount`, `Date`, `Month`, `Year`, `payment_type`, `pay_ID`) VALUES ('" + RevCentID + "', '" +
                            acGCR + "' ,'" + acAMOUNT + "', '" + acDate + "', '" + acMonth + "', '" + acYEAR + "', '" +
                            acType + "', '" + fini + "')");
                    stmnt.executeUpdate();
                    tblCollection.getItems().remove(h);
                    }else {
                        typePB++;
                        payChq.put(typeGCR, new HashMap<>()); payChq.get(typeGCR).put("Center", RevCentID); payChq.get(typeGCR).put("GCR", getData.getGCR());
                        payChq.get(typeGCR).put("Amount", Float.toString(acAMOUNT)); payChq.get(typeGCR).put("Date", acDate); payChq.get(typeGCR).put("Month", acMonth);
                        payChq.get(typeGCR).put("Year", acYear); payChq.get(typeGCR).put("Type", acType); payChq.get(typeGCR).put("PayID", fini);
                        System.out.println(typeGCR+"\n"+monthGCR+"\n"+dateGCR);
                    }
                }/*
            }else {
                tblCollection.getItems().clear();
            }*/
        }
        System.out.println(typePB);
        if(!regGcr.isEmpty()){
            Main st =new Main();
            try {
                FXMLLoader bankDetails = new FXMLLoader();
                bankDetails.setLocation(getClass().getResource("/com/Views/fxml/Bank_DetailsEntries.fxml"));
                bankDetails.setController(new Bank_DetailsEntriesController());
                Bank_DetailsEntriesController bnkDtls = bankDetails.getController();
                bnkDtls.setAppController(this);
                Parent root = bankDetails.load();
                Scene s = new Scene(root);
                Stage stg = new Stage();
                bnkDtls.setStage(stg);
                stg.initModality(Modality.APPLICATION_MODAL);
                stg.initOwner(st.stage);
                stg.initStyle(StageStyle.UTILITY);
                stg.setScene(s);/*
                stg.setOnHidden(e -> {
                    System.out.println("Yes I run only after you are done");
                });*/
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

    public void deleteSelection(ActionEvent actionEvent) throws SQLException {
        if (!chkUpdate.isSelected()){
        if(tblCollection.getSelectionModel().isEmpty()){
            lblDeleteWarn.setVisible(true);
        }else{
        ObservableList<GetCollectEnt> selectedRows = tblCollection.getSelectionModel().getSelectedItems();
        ArrayList<GetCollectEnt> rows = new ArrayList<>(selectedRows);
        rows.forEach(row -> tblCollection.getItems().remove(row));
        }
        }else {
            if (entriesID != null){
                stmnt = con.prepareStatement("DELETE FROM `collection_payment_entries` WHERE `pay_ID` = '"+entriesID+"' ");
                stmnt.executeUpdate();
                clear();
                loadRevenueCollectionTable();
            }else {
                lblControlWarn.setVisible(true);
            }
        }
    }
    
}
