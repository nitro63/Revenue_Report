/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetCollectEnt;
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
    
    entries_sideController app;
    
    public final GetRevCenter GetCenter;
   
        
        ObservableList<String> registerItem = FXCollections.observableArrayList(); 
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
//      registerItem.put("", new ArrayList());
               
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
        
        Calendar cal = Calendar.getInstance();
        
        
        int actMonth = date.getMonthValue() -1;//Converting Datepicker month value from 1-12 format to 0-11 format
        
        cal.set(date.getYear(), actMonth, date.getDayOfMonth());//Initialising assigning datepicker value to Calendar
             // item
        
        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for
             // further use
        SimpleDateFormat Dateformat = new SimpleDateFormat("dd-MM-yyyy");// Date format for Date 
        Date = Dateformat.format(setDate);// Assigning converted date with "MM/dd/YY" format to "Date" variable
        cal.setTime(setDate);//Setting time to Calendar variable
        cal.setMinimalDaysInFirstWeek(3);//Setting the minimal days to make a week;
//        int initYear = cal.get(Calendar.YEAR);
        int initYear = spnColYear.getValue();
        Year = Integer.toString(initYear);
//        Year = spnColYear.getValue();
//        Month = new SimpleDateFormat("MMMM").format(cal.getTime());
        Month = cmbColMonth.getSelectionModel().getSelectedItem();
        
        

       while(Condition) {
            String i=txtEntGCR.getText();
//       if(registerItem.containsKey(Date)){
           if(registerItem.contains(i)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Revenue already entered");
                alert.showAndWait();
                
                Condition =false;
//           }
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
                txtEntAmt.clear();
                Condition =false;
                
           
            }else if(cmbPayType.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please select PAYMENT TYPE");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
                
                
            }else if(cmbColMonth.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Collection Month");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
                
                
            }else if(spnColYear.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Collection Year");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
                
                
            }else if(StringUtils.countMatches(txtEntAmt.getText(), ".") >1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Enter Amount");
                alert.setContentText("Please check the number of '.' in the amount");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
                
                
            }else if(txtEntAmt.getText().isEmpty()|| txtEntGCR.getText().isEmpty() || registerItem.contains(i)){
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
                txtEntAmt.clear();
                Condition =false;
                
            }else{
            getReport = new GetCollectEnt(RevCent, Amount, GCR, Month, Date, Year, Type);
            tblCollection.getItems().add(getReport);
            registerItem.add(txtEntGCR.getText());
            Condition = false;
            entDatePck.setValue(null);
            txtEntGCR.clear();
            txtEntAmt.clear();
            cmbColMonth.getSelectionModel().clearSelection();
            cmbPayType.getSelectionModel().clearSelection();
//                
//                if(!registerItem.containsKey(Date)){
//                    registerItem.put(Date, new ArrayList());
//                    registerItem.get(Date).add(i);
//                }else if(registerItem.containsKey(Date) && !registerItem.get(Date).contains(i)){
//                    registerItem.get(Date).add(i);
//                }
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
            registerItem.remove(tblCollection.getSelectionModel().getSelectedItem().getGCR());
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
        
        for(int h = 0; h<tblCollection.getItems().size(); h++){
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
                if(acType.equals("Cheque") || acType.equals("Cheque Deposit Slip")){
                    count++;
                    if(dateGCR.isEmpty() || !dateGCR.containsKey(acDate)){
                        dateGCR.put(acDate, new HashMap<>());
                        dateGCR.get(acDate).put(acMonth, new ArrayList<>());
                        dateGCR.get(acDate).get(acMonth).add(getData.getGCR());
                    }else if(dateGCR.containsKey(acDate) && !dateGCR.get(acDate).containsKey(acMonth)){
                        dateGCR.get(acDate).put(acMonth, new ArrayList<>());
                        dateGCR.get(acDate).get(acMonth).add(getData.getGCR());
                    }else if (dateGCR.containsKey(acDate) && dateGCR.get(acDate).containsKey(acMonth) && !dateGCR.
                            get(acDate).get(acMonth).contains(getData.getGCR())){
                        dateGCR.get(acDate).get(acMonth).add(getData.getGCR());
                    }
                    if(monthGCR.isEmpty() || !monthGCR.containsKey(acMonth)){
                        monthGCR.put(acMonth, new ArrayList<>());
                        monthGCR.get(acMonth).add(getData.getGCR());
                    }else if(monthGCR.containsKey(acMonth) && !monthGCR.get(acMonth).contains(getData.getGCR())){
                        monthGCR.get(acMonth).add(getData.getGCR());
                    }
                    if(regGcr.isEmpty() || !regGcr.containsKey(acYear)){
                        regGcr.put(acYear, new ArrayList<>());
                        regGcr.get(acYear).add(getData.getGCR());
                    }else if(regGcr.containsKey(acYear) && !regGcr.get(acYear).contains(getData.getGCR())){
                        regGcr.get(acYear).add(getData.getGCR());
                    }
                }
                stmnt = con.prepareStatement("SELECT * FROM `collection_payment_entries` WHERE  " +
                        "`revCenter` = '"+acCENTER+"' AND `GCR` = '"+acGCR+"' AND `payment_type` = '"+acType+"'");
                ResultSet res = stmnt.executeQuery();
                while (res.next()){
                    duplicate.add(res.getString("GCR"));
                }
                if (duplicate.contains(getData.getGCR())){
                    lblDup.setText('"'+getData.getGCR()+'"'+" of payment method "+'"'+acType+'"'+" already exist. Please select " +
                            "row of duplicate data in table to edit or delete.");
                    lblDup.setVisible(true);
                    h=tblCollection.getItems().size();
                }else {
                    boolean condition = true;
                    ArrayList<String> dup = new ArrayList<>();
                    while (condition){
                        stmnt = con.prepareStatement("SELECT `ID` FROM `collection_payment_entries` WHERE `ID` = " +
                                "'" + fini + "' ");
                        ResultSet rt = stmnt.executeQuery();
                        while (rt.next()){
                            dup.add(rt.getString("ID"));
                        }
                        if (dup.contains(fini)){
                            fini = getID();
                        }else {
                            condition = false;
                        }
                    }
                    if (acType.equals("Cheque") || acType.equals("Cheque Deposit Slip")){
                        gcrID.put(fini, getData.getGCR());
                    }
                    stmnt = con.prepareStatement("INSERT INTO `collection_payment_entries`(`revCenter`, `GCR`," +
                            " `Amount`, `Date`, `Month`, `Year`, `payment_type`, `ID`) VALUES ('" + acCENTER + "', '" +
                            acGCR + "' ,'" + acAMOUNT + "', '" + acDate + "', '" + acMonth + "', '" + acYEAR + "', '" +
                            acType + "', '" + fini + "')");
                    stmnt.executeUpdate();
                    tblCollection.getItems().remove(h);
                }
        }
        if(!regGcr.isEmpty()){
            System.out.println(regGcr);
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

                        // normally, you would just use the default alert positioning,
                        // but for this simple sample the main stage is small,
                        // so explicitly position the alert so that the main window can still be seen.
                        closeConfirmation.setX(stg.getX()*2);
                        closeConfirmation.setY(stg.getY()*2);

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
