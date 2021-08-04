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
import com.jfoenix.controls.JFXCheckBox;
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
   
        
      private  Map<String, ArrayList<String>> registerItem = new HashMap<>();
    private final ObservableList<String> paymentType = FXCollections.observableArrayList("Cash", "Cheque",
                "Cash Deposit Slip","Cheque Deposit Slip");
    private final ObservableList<String> collectionMonth = FXCollections.observableArrayList("January", "February", "March"
                , "April", "May", "June", "July", "August", "September", "October", "November", "December");
        
         boolean Condition = true;
    private final Connection con;
    private Matcher m;
    private PreparedStatement stmnt;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private ResultSet rs;

   private final String regex = "(?<=[\\d])(,)(?=[\\d])";
   public Pattern p = Pattern.compile(regex);
//    public  Stage stg;
        
   private     String Date, Item, Code, Month, Amount, Week, Year, RevCent, RevCentID, GCR, Type , entriesID;
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
        RevCent = GetCenter.getRevCenter();
        RevCentID = GetCenter.getCenterID();
        Calendar cal = Calendar.getInstance();
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
    //  stmnt = con.prepareStatement("SELECT `year` FROM `value_books_stock_record` WHERE `value_stock_revCenter` = '"+revCentID+"' GROUP BY `year`");
      //rs = stmnt.executeQuery();
      cmbUpdateYear.getItems().clear();
     // while (rs.next()){
     //     cmbUpdateYear.getItems().add(rs.getString("year"));
     // }
  }

    @FXML
    void selectedYear(ActionEvent event) throws SQLException {
      String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
        stmnt = con.prepareStatement("SELECT `month` FROM `value_books_stock_record` WHERE `value_stock_revCenter` = '"+RevCentID+"' AND `year` ='"+year+"' GROUP BY `month`");
        rs = stmnt.executeQuery();
        cmbUpdateMonth.getItems().clear();
        while (rs.next()){
            cmbUpdateMonth.getItems().add(rs.getString("month"));
        }
    }

    @FXML
   void fetchEntries(ActionEvent event) throws SQLException {/*
        if (!cmbUpdateYear.getSelectionModel().isEmpty()) {
            String year = cmbUpdateYear.getSelectionModel().getSelectedItem(), month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
            if (!cmbUpdateMonth.getSelectionModel().isEmpty()) {
                stmnt = con.prepareStatement("SELECT `value_record_ID`, `date`, `value_books`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks` FROM `value_books_stock_record`, `value_books_details` WHERE `value_stock_revCenter` = '" + revCentID + "' AND `value_book_ID` = `value_book` AND `year` ='" + year + "' AND `month` = '" + month + "' ");
            } else {
                stmnt = con.prepareStatement("SELECT `value_record_ID`, `date`, `value_books`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks` FROM `value_books_stock_record`, `value_books_details` WHERE `value_stock_revCenter` = '" + revCentID + "' AND `value_book_ID` = `value_book` AND `year` ='" + year + "'");
            }
            rs = stmnt.executeQuery();
            tblCollection.getItems().clear();
            colID.setCellValueFactory(data -> data.getValue().weekProperty());
            colDATE.setCellValueFactory(data -> data.getValue().dateProperty());
            colTypeVB.setCellValueFactory(data -> data.getValue().valueBookProperty());
            colSerialFrom.setCellValueFactory(data -> data.getValue().firstSerialProperty());
            colSerialTo.setCellValueFactory(data -> data.getValue().lastSerialProperty());
            colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty());
            colAmtVal.setCellValueFactory(data -> data.getValue().valAmountProperty());
            colCumuAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
            colPurchAmount.setCellValueFactory(data -> data.getValue().purAmountProperty());
            colRemarks.setCellValueFactory(d -> d.getValue().remarksProperty());
            float amount = 0; String cumuAmounts;
            while (rs.next()) {
                amount += rs.getFloat("amount");
                cumuAmounts = getDates.getAmount(Float.toString(amount));
                addEntries = new GetValueBooksEntries(rs.getString("value_record_ID"), rs.getString("date"), rs.getString("value_books"), rs.getString("first_serial"), rs.getString("last_serial"),
                        rs.getString("quantity"), rs.getString("amount"), cumuAmounts, rs.getString("purchase_amount"), rs.getString("remarks"));
                tblCollection.getItems().add(addEntries);

            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Please select Year");
            alert.showAndWait();
        }*/
    }

    private void setEntries() {/*
        GetValueBooksEntries entries = tblCollection.getSelectionModel().getSelectedItem();
        entriesID = entries.getWeek();
        entDatePck.setValue(LocalDate.parse(entries.getDate(), format));
        m = p.matcher(entries.getPurAmount());
        String amount =  m.replaceAll("");
        float price = Float.parseFloat(amount);
        int quantity = Integer.parseInt(entries.getQuantity());
        float unitAmount = price / quantity;
        txtUnitAmount.setText(Float.toString(unitAmount));
        cmbTypeOfValueBook.getSelectionModel().select(entries.getValueBook());
        txtSerialFrom.setText(entries.getFirstSerial());
        txtSerialTo.setText(entries.getLastSerial());
        */
    }

    @FXML
    void updateEntries(ActionEvent event) throws SQLException {/*
        if (entriesID != null){
            String  lastSerial = txtSerialTo.getText(),
                    firstSerial = txtSerialFrom.getText(),
                    Quantity, typeOfValBk = cmbTypeOfValueBook.getSelectionModel().getSelectedItem(),
                    purAmount, valAmount,
                    Month = getDates.getMonth(entDatePck.getValue()),
                    Date = getDates.getDate(entDatePck.getValue());
            float amount = 0, cumuamount = 0, puramount = 0;
            int serialChecker = ((Integer.parseInt(lastSerial) - Integer.parseInt(firstSerial)) + 1),
                    year = Integer.parseInt(getDates.getYear(entDatePck.getValue())),
                    week = Integer.parseInt(getDates.getWeek(entDatePck.getValue())),
                    quarter = Integer.parseInt(getDates.getQuarter(entDatePck.getValue()));
            int quantity = ((serialChecker) / 100);
            if ((serialChecker) < 100 || serialChecker % 100 != 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please check Serials");
                alert.showAndWait();
            } else {
                Quantity = Integer.toString(quantity);
                for (Map.Entry<String, Float> calValAmount : priceBook.entrySet()){
                    if (calValAmount.getKey().equals(typeOfValBk)){
                        amount = quantity * calValAmount.getValue() * 100;
                    }
                }
                puramount = (Float.parseFloat(txtUnitAmount.getText()) * quantity);
                purAmount = getDates.getAmount(Float.toString(puramount));
                valAmount = getDates.getAmount(Float.toString((amount)));
                if ("0.00".equals(purAmount)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Please Amount cannot be '0'");
                    alert.showAndWait();
                    txtUnitAmount.clear();
                } else {
                    stmnt = con.prepareStatement("UPDATE `value_books_stock_record` SET `year` = '"+year+"'," +
                            " `week` = '"+week+"', `quarter` = '"+quarter+"', `date` = '"+Date+"', `month` = '"+Month
                            +"', `value_book` = '"+valueBookID.get(typeOfValBk)+"', `first_serial` = '"+firstSerial+"', `last_serial` = '"+
                            lastSerial+"', `quantity` = '"+Quantity+"', `amount` = '"+valAmount+"', `purchase_amount` = '"+
                            purAmount+"', `remarks` = 'Updated' WHERE `value_record_ID` = '"+entriesID+"'");
                    stmnt.executeUpdate();
                    loadRevenueCollectionTable();
                    clear();
                }
            }
        }else{
            lblControlWarn.setVisible(true);
        }
        */
    }

    void clear(){/*
        entriesID = null;
        entDatePck.setValue(null);
        txtUnitAmount.setText(null);
        txtSerialFrom.setText(null);
        txtSerialTo.setText(null);
        cmbTypeOfValueBook.getSelectionModel().clearSelection();
        */
    }



    private void loadRevenueCollectionTable() throws SQLException {/*
        String year = cmbUpdateYear.getSelectionModel().getSelectedItem(), month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
        if (!cmbUpdateMonth.getSelectionModel().isEmpty()) {
            stmnt = con.prepareStatement("SELECT `value_record_ID`, `date`, `value_books`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks` FROM `value_books_stock_record`, `value_books_details` WHERE `value_stock_revCenter` = '" + revCentID + "' AND `value_book_ID` = `value_book` AND `year` ='" + year + "' AND `month` = '" + month + "' ");
        } else {
            stmnt = con.prepareStatement("SELECT `value_record_ID`, `date`, `value_books`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks` FROM `value_books_stock_record`, `value_books_details` WHERE `value_stock_revCenter` = '" + revCentID + "' AND `value_book_ID` = `value_book` AND `year` ='" + year + "'");
        }
        rs = stmnt.executeQuery();
        tblCollection.getItems().clear();
        colID.setCellValueFactory(data -> data.getValue().weekProperty());
        colDATE.setCellValueFactory(data -> data.getValue().dateProperty());
        colTypeVB.setCellValueFactory(data -> data.getValue().valueBookProperty());
        colSerialFrom.setCellValueFactory(data -> data.getValue().firstSerialProperty());
        colSerialTo.setCellValueFactory(data -> data.getValue().lastSerialProperty());
        colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty());
        colAmtVal.setCellValueFactory(data -> data.getValue().valAmountProperty());
        colCumuAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
        colPurchAmount.setCellValueFactory(data -> data.getValue().purAmountProperty());
        colRemarks.setCellValueFactory(d -> d.getValue().remarksProperty());
        float amount = 0; String cumuAmounts;
        while (rs.next()) {
            amount += rs.getFloat("amount");
            cumuAmounts = getDates.getAmount(Float.toString(amount));
            addEntries = new GetValueBooksEntries(rs.getString("value_record_ID"), rs.getString("date"), rs.getString("value_books"), rs.getString("first_serial"), rs.getString("last_serial"),
                    rs.getString("quantity"), rs.getString("amount"), cumuAmounts, rs.getString("purchase_amount"), rs.getString("remarks"));
            tblCollection.getItems().add(addEntries);

        }
        */
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

                if(!registerItem.containsKey(Type)){
                    registerItem.put(Type, new ArrayList<>());
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
            registerItem.get(cmbPayType.getSelectionModel().getSelectedItem()).remove(txtEntGCR.getText());
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
                si = id.replace("-", "").substring(23,25);
        return is+ID+si.concat(Day).concat(date).concat(month).
                concat(year).concat(hour).concat(minute).concat(second).concat(milli);
    }

    @FXML
    private void SaveToDatabase(ActionEvent event) throws SQLException {
        getData = new GetCollectEnt();
         ArrayList<String> duplicate = new ArrayList<>();
         String fini;
        
        for(int h = 0; h<=tblCollection.getItems().size(); h++){
            if (h != tblCollection.getItems().size()) {
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
                            " `Amount`, `Date`, `Month`, `Year`, `payment_type`, `pay_ID`) VALUES ('" + RevCentID + "', '" +
                            acGCR + "' ,'" + acAMOUNT + "', '" + acDate + "', '" + acMonth + "', '" + acYEAR + "', '" +
                            acType + "', '" + fini + "')");
                    stmnt.executeUpdate();
                }
            }else {
                tblCollection.getItems().clear();
            }
        }
        if(!regGcr.isEmpty()){
            Main st =new Main();
            try {
                FXMLLoader bankDetails = new FXMLLoader();
                bankDetails.setLocation(getClass().getResource("/Views/fxml/Bank_DetailsEntries.fxml"));
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
