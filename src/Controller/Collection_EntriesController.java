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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class Collection_EntriesController implements Initializable {

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
    
    GetCollectEnt getData, getReport;
    
    entries_sideController app;
    
    private final GetRevCenter GetCenter;
   
        
        ObservableList<String> registerItem = FXCollections.observableArrayList(); 
        ObservableList<String> paymentType = FXCollections.observableArrayList("Cash", "Cheque", "Cash Deposit Slip","Cheque Deposit Slip");
        ObservableList<String> collectionMonth = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
        
         boolean Condition = true;
    private final Connection con;
    private PreparedStatement stmnt;
        
        String Date, Item, Code, Month, Amount, Week, Year, RevCent, GCR, Type;
    ObservableList<String> regGcr = FXCollections.observableArrayList();
    Map<String, ArrayList<String>> dateGCR = new HashMap<>();
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
    public Collection_EntriesController(GetRevCenter GetCenter) throws SQLException, ClassNotFoundException{
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
        cmbColMonth.setItems(collectionMonth);
        cmbPayType.setItems(paymentType);
        cmbColMonth.setVisibleRowCount(4);
        cmbPayType.setVisibleRowCount(3);
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
        
        cal.set(date.getYear(), actMonth, date.getDayOfMonth());//Initialising assigning datepicker value to Calendar item
        
        java.util.Date setDate = cal.getTime();//Variable for converting DatePicker value from Calendar to Date for further use 
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
            }else if(txtEntGCR.getText().length() > 10){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Check GCR number");
                alert.showAndWait();
                
                Condition =false;
            }
            else{
                if(cmbPayType.equals("Cheque") || cmbPayType.equals("Cheque Deposit Slip")){
                    if(dateGCR.isEmpty() || !dateGCR.containsKey(Date)){
                        dateGCR.put(Date, new ArrayList<>());
                        dateGCR.get(Date).add(txtEntGCR.getText());
                    }else if(dateGCR.containsKey(Date) && !dateGCR.get(Date).contains(txtEntGCR.getText())){
                        dateGCR.get(Date).add(txtEntGCR.getText());
                            regGcr.add(txtEntGCR.getText());
                        }
                }
                
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
        txtEntGCR.clear();
        txtEntAmt.clear();
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
        entDatePck.setValue(null);
        txtEntGCR.clear();
        txtEntAmt.clear();
    }

    @FXML
    private void SaveToDatabase(ActionEvent event) throws SQLException {
        getData = new GetCollectEnt();
        Map<String, ArrayList<String>> duplicate = new HashMap<>();
        ResultSet rs;
        ResultSetMetaData rm;
        
        for(int h = 0; h<tblCollection.getItems().size(); h++){
            getData = tblCollection.getItems().get(h);
            int acGCR = Integer.parseInt(getData.getGCR());
            String regex = "(?<=[\\d])(,)(?=[\\d])";
            Pattern p = Pattern.compile(regex);
            String amount = getData.getAmount();
            Matcher m = p.matcher(amount);
            amount = m.replaceAll("");
            float acAMOUNT =Float.parseFloat(amount);
            String acDATE =getData.getDate();
            String acMonth =getData.getMonth();
            String acCENTER =getData.getCenter();
            String acType = getData.getType();
            int acYEAR = Integer.parseInt(getData.getYear());
//            stmnt = con.prepareStatement("SELECT `revCenter`, `GCR`  FROM `collection_payment_entries` WHERE `GCR` = '"+acGCR+"' ");
//            rs = stmnt.executeQuery();
//            rm = rs.getMetaData();
//            int col = rm.getColumnCount();
//            
//            while(rs.next()){
//                for(int j = 1; j <= col; j++){
//                    if(j == 1){
//                        String cent = rs.getObject(1).toString();
//                        duplicate.put(cent, new ArrayList<>());                        
//                    }
//                    if (j == 2){
//                        String gcr = rs.getObject(2).toString();
//                        duplicate.get(rs.getObject(1).toString()).add(gcr);                        
//                    }
//                }
//            }
//            if(duplicate.containsKey(acCENTER)&& duplicate.get(acCENTER).contains(getData.getGCR())){
//                
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Warning Dialog");
//                alert.setHeaderText("Payment already ENTERED for GCR:" + getData.getGCR());
//                alert.showAndWait();
//                tblCollection.getItems().remove(h);
//                
//            }else{
//                stmnt = con.prepareStatement("INSERT INTO `collection_payment_entries`(`revCenter`, `GCR`, `Amount`, `Date`, `Month`, `Year`) VALUES ('"+acCENTER+"', '"+acGCR+"' ,'"+acAMOUNT+"', '"+acDATE+"', '"+acMonth+"', '"+acYEAR+"')");
//                stmnt.executeUpdate();
//            }
            
                stmnt = con.prepareStatement("INSERT INTO `collection_payment_entries`(`revCenter`, `GCR`, `Amount`, `Date`, `Month`, `Year`, `payment_type`) VALUES ('"+acCENTER+"', '"+acGCR+"' ,'"+acAMOUNT+"', '"+acDATE+"', '"+acMonth+"', '"+acYEAR+"', '"+acType+"')");
                stmnt.executeUpdate();
        }
        if(!regGcr.isEmpty()){

            try {
                Parent bankDetails = FXMLLoader.load(getClass().getResource("/Views/fxml/Bank_DetailsEntries.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(duplicate);
        tblCollection.getItems().clear();
        
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
    
}
