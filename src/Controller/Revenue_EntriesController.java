/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import Controller.Gets.GetFunctions;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;

import java.time.LocalDate;
import java.util.Map.Entry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;
import Controller.Gets.GetEntries;
import Controller.Gets.GetRevCenter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class Revenue_EntriesController  implements Initializable {

    @FXML
    private AnchorPane DailyEntPane;
    @FXML
    private Label lblTotalAmount;
    @FXML
    private TextField txtEntCCAmt;
    @FXML
    private DatePicker entDatePck;
    @FXML
    private ComboBox<String> cmbEntRevItem;
    @FXML
    private TextField txtEntAmt;
    @FXML
    private TableView<GetEntries> revTable;
    @FXML
    private TableColumn<GetEntries, String> revCode;
    @FXML
    private TableColumn<GetEntries, String> colCCAmount;
    @FXML
    private TableColumn<GetEntries, String> revItem;
    @FXML
    private TableColumn<GetEntries, String> revAmount;
    @FXML
    private TableColumn<GetEntries, String> revDate;
    @FXML
    private TableColumn<GetEntries, String> revWeek;
    @FXML
    private Label lblEdit;
    @FXML
    private Label lblDup;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private Label lblDeleteWarn;
    @FXML
    private TableColumn<GetEntries, String> revMonth;
    entries_sideController app;
    GetFunctions getFunctions = new GetFunctions();

    /**
     * initializes the controller class.
     * @param app
     */
    
    Map <String, String> RevenueMap = new HashMap<String, String>()
        {{
            put("Market Tolls","1423001");
            put("Reg. of Marriage/Divorce", "1423011");
            put("Burial Permits and Cemetry", "13425");
            put("Sub Metro Managed Toilets / Surtax","685894");
        }};
        ObservableList<String> RevenueItems = FXCollections.observableArrayList(RevenueMap.keySet());
        ObservableList<String> ItemsCode = FXCollections.observableArrayList(RevenueMap.values());
        
        Map<String, ArrayList<String>> registerItem=new HashMap<>();

        
        
        
         boolean Condition = true, ccCheck;
         float totAmount = 0;
        
        String Date, Item, Code, Month, Amount, CCAmount, Week, Year, RevCent, Qtr, totalAmount;
        
        
                
    @FXML
    private Button btnEnter;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnSaveEntries;
    
     GetEntries addEntries = new GetEntries();
    
    private final GetRevCenter GetCenter;
    @FXML
    private TableColumn<GetEntries, String> revYear;
    @FXML
    private TableColumn<GetEntries, String> revQuarter;
    @FXML
    private Button btnClose;


    
  public Revenue_EntriesController(GetRevCenter GetCenter){
       this.GetCenter = GetCenter;
    }
  
    public void setappController(entries_sideController app){
         this.app = app;
     }
     public entries_sideController getentries_sideController(){
         return app;
     }  

        
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
      revTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
      registerItem.put("", new ArrayList());
      revTable.setOnMouseClicked(e -> {
          lblDeleteWarn.setVisible(false);lblDup.setVisible(false);lblEdit.setVisible(false);
      });
        GetRevenueItems();
        if (app.getRevGroup().getSelectionModel().getSelectedItem().toString().toLowerCase(Locale.ROOT).
                equals("sub-metros")){
            txtEntCCAmt.setDisable(false);
            ccCheck = true;
        }else{
            txtEntCCAmt.setDisable(true);
            ccCheck = false;
        }
  }

    
    private void GetRevenueItems(){
        cmbEntRevItem.getItems().clear();
        cmbEntRevItem.getItems().addAll(RevenueItems);
    }
    
   


   
    @FXML
    private void saveEntries(ActionEvent event) throws IOException {
      RevCent = GetCenter.getRevCenter();
      Code = RevenueMap.get(cmbEntRevItem.getSelectionModel().getSelectedItem());// Getting the Code of the Revenue Items
      Item = cmbEntRevItem.getSelectionModel().getSelectedItem();// Assigning Selected Revenue Item to the Item Variable
        
      LocalDate date = entDatePck.getValue();// Assigning the date picker value to a variable
      //Making sure Datepicker is never empty :)
      if(entDatePck.getValue() == null){
             Alert alert = new Alert(AlertType.WARNING);
             alert.setTitle("Warning Dialog");
             alert.setHeaderText("Please Select Date");
             alert.showAndWait();
             Condition = false;
            }else{
             Condition =true;
        Date = getFunctions.getDate(date);
        Year = getFunctions.getYear(date);
        Qtr = getFunctions.getQuarter(date);
        Week = getFunctions.getWeek(date);
        Month = getFunctions.getMonth(date);
       while(Condition) {
            String i=cmbEntRevItem.getSelectionModel().getSelectedItem();
       if(registerItem.containsKey(Date) && registerItem.get(Date).contains(i)){
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.setTitle("Warning Dialog");
               alert.setHeaderText("Revenue Item already entered");
               alert.showAndWait();
           Condition =false;
            }else if (cmbEntRevItem.getSelectionModel().isEmpty()){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Select Revenue Item");
                alert.showAndWait();
                
                Condition =false;
            }else if(txtEntAmt.getText().isEmpty()){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please enter Amount");
                alert.showAndWait();
                Condition =false;
            }else if(RevCent == null){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Revenue Center");
                alert.showAndWait();
                Condition =false;
            }else if(txtEntAmt.getText().isEmpty() || cmbEntRevItem.getSelectionModel().isEmpty() ){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Make All Entries");
                alert.showAndWait();
                Condition =false;
            }
            else if(txtEntAmt.getText().startsWith(".") && txtEntAmt.getText().endsWith(".")){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Enter Amount");
                alert.showAndWait();
                Condition =false;
            }else if(StringUtils.countMatches(txtEntAmt.getText(), ".") >1){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Incorrect amount format");
                alert.setContentText("Please check the number of '.' in the amount");
                alert.showAndWait();
                Condition =false;
            }else if(StringUtils.countMatches(txtEntCCAmt.getText(), ".") >1){
           Alert alert = new Alert(AlertType.WARNING);
           alert.setTitle("Warning Dialog");
           alert.setHeaderText("Incorrect CC Amount format");
           alert.setContentText("Please check the number of '.' in the CC amount");
           alert.showAndWait();
           Condition =false;
       }
            else{
                revCode.setCellValueFactory(data -> data.getValue().CodeProperty());
        revItem.setCellValueFactory(data -> data.getValue().ItemProperty());
        revAmount.setCellValueFactory(data -> data.getValue().AmountProperty());
        colCCAmount.setCellValueFactory(data -> data.getValue().CenterProperty());
        revDate.setCellValueFactory(data -> data.getValue().DateProperty());
        revWeek.setCellValueFactory(data -> data.getValue().WeekProperty());
        revMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
        revQuarter.setCellValueFactory(data -> data.getValue().QuarterProperty());
        revYear.setCellValueFactory(data -> data.getValue().YearProperty());
        Amount= getFunctions.getAmount(txtEntAmt.getText());
        if (ccCheck && !txtEntCCAmt.getText().isEmpty()){
            CCAmount = getFunctions.getAmount(txtEntCCAmt.getText());
        }else  if (ccCheck && txtEntCCAmt.getText().isEmpty()){
            CCAmount = "0.00";
        }else {CCAmount = "NA";}
        if("0.00".equals(Amount)){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Amount cannot be '0'");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
            }else{
            totAmount += Float.parseFloat(txtEntAmt.getText());
            totalAmount = getFunctions.getAmount(Float.toString(totAmount));
            lblTotalAmount.setText(totalAmount);
               addEntries = new GetEntries(Code, CCAmount, Item, Date, Month, Amount, Week, Year, Qtr);
                revTable.getItems().add(addEntries);
                if(!registerItem.containsKey(Date)){
                    registerItem.put(Date, new ArrayList());
                    registerItem.get(Date).add(i);
                }else if(registerItem.containsKey(Date) && !registerItem.get(Date).contains(i)){
                    registerItem.get(Date).add(i);
                }
     
//                registerItem.add(cmbEntRevItem.getSelectionModel().getSelectedIndex());
                if (ccCheck){
                    txtEntCCAmt.clear();
                }
                cmbEntRevItem.getSelectionModel().clearSelection();
                txtEntAmt.clear();
                Condition = false;
        }
            }
       }

         }
    }

    @FXML
    private void clearEntries(ActionEvent event) {
      GetEntries entries = revTable.getSelectionModel().getSelectedItem();
      if (revTable.getSelectionModel().isEmpty()){
          lblEdit.setText("Please select a row in the table to "+'"'+"Edit"+'"');
          lblEdit.setVisible(true);
      }else {
          String regex = "(?<=[\\d])(,)(?=[\\d])";
          Pattern p = Pattern.compile(regex);
          Matcher m = p.matcher(entries.getAmount());
          entDatePck.setValue(LocalDate.parse(entries.getDate(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
          cmbEntRevItem.getSelectionModel().select(entries.getItem());
          txtEntAmt.setText(m.replaceAll(""));
          totAmount -= Float.parseFloat(m.replaceAll(""));
          totalAmount = getFunctions.getAmount(Float.toString(totAmount));
          lblTotalAmount.setText(totalAmount);
          registerItem.get(entries.getDate()).remove(entries.getItem());
          ObservableList<GetEntries> selectedRows = revTable.getSelectionModel().getSelectedItems();
          ArrayList<GetEntries> rows = new ArrayList<>(selectedRows);
          rows.forEach(row -> revTable.getItems().remove(row));
      }
    }

    @FXML
    private void SaveToDatabase(ActionEvent event) throws ClassNotFoundException, SQLException {
      float deduction = 0;
        GetEntries getData = new GetEntries();
        ObservableList<String> duplicate = FXCollections.observableArrayList();
        String Username = "root";
        String Password = "";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/"
                + "revenue_monitoring"+"?useTimezone=true&serverTimezone=UTC", Username, Password);
        PreparedStatement stmnt;
        ResultSet rs;
        ResultSetMetaData metaData;
        
        List <List<String>> arrList = new ArrayList<>();
        Map<String, ArrayList<String>> forDatabase = new HashMap<>();
                    forDatabase.put("revCenter", new ArrayList<>() );
            forDatabase.put("revCode", new ArrayList<>());
            forDatabase.put("revItem", new ArrayList<>());
            forDatabase.put("revDate", new ArrayList<>());
            forDatabase.put("revMonth", new ArrayList<>());
            forDatabase.put("revAmount", new ArrayList<>());
            forDatabase.put("revWeek", new ArrayList<>());   
        
        
        for(int i = 0; i <= revTable.getItems().size(); i++){
            if (i != revTable.getItems().size()){
                getData = revTable.getItems().get(i);
            String acDate = getData.getDate();
            String acCode = getData.getCode();
            String acItem = getData.getItem();
            String acQtr = getData.getQuarter();
            String CcAmt;
            if (ccCheck){
                CcAmt = getData.getCenter();
            }else {
                CcAmt = "0.00";
            }
            String regex = "(?<=[\\d])(,)(?=[\\d])";
            Pattern p = Pattern.compile(regex);
            String amount = getData.getAmount();
            Matcher m = p.matcher(amount),
                    mac = p.matcher(CcAmt);
            float acAmount = Float.parseFloat(m.replaceAll("")),
                  acCCAmount = Float.parseFloat(mac.replaceAll(""));
            int acWeek = Integer.parseInt(getData.getWeek());
            String acMonth = getData.getMonth();
            int acYear = Integer.parseInt(getData.getYear());
            stmnt = connection.prepareStatement("SELECT * FROM `daily_entries` WHERE `Code` = '"+acCode+"'" +
                    " AND `revenueDate` = '"+acDate+"' AND `daily_revCenter` = '"+RevCent+"' ");
            rs = stmnt.executeQuery();       
            metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            while(rs.next()){

                        String dup = rs.getString("Code");
                        duplicate.add(dup);
                        System.out.println("Fred"+duplicate);


            }
            if(duplicate.contains(acCode)){
                lblDup.setText("Revenue for "+'"'+getData.getItem()+'"'+ " on "+'"'+ getData.getDate()+'"'+" already exist. Please delete or edit duplicate.");
                lblDup.setVisible(true);
                i = revTable.getItems().size() + 1;
            }else{
                deduction += acAmount;
                stmnt = connection.prepareStatement("INSERT INTO `daily_entries`(`daily_revCenter`, `commissionAmount`, " +
                        "`Code`, `revenueItem`, `revenueAmount`, `revenueDate`, `revenueWeek`, `revenueMonth`," +
                        " `revenueYear`, `revenueQuarter`) VALUES('"+RevCent+"','"+acCCAmount+"', '"+acCode+"'," +
                        " '"+acItem+"', '"+acAmount+"', '"+acDate+"', '"+acWeek+"','"+acMonth+"', '"+acYear+"'," +
                        " '"+acQtr+"')");
                stmnt.executeUpdate();
            }
            }else {
                totAmount -= deduction;
                revTable.getItems().clear();
            }
        }
    }






    @FXML
    private void CancelEntries(ActionEvent event) {
        registerItem.clear();
        revTable.getItems().clear();
//        getentries_sideController().getRevCent().getSelectionModel().clearSelection();
//        getentries_sideController().getRevGroup().getSelectionModel().clearSelection();
//        getentries_sideController().setRevCenter(null);
//        getentries_sideController().initialize(url, rb);
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
    private void showClose(ActionEvent event) {        
        getentries_sideController().getappController().getCenterPane().getChildren().clear();
    }
    public void deleteSelection(ActionEvent actionEvent) {
      GetEntries entries = revTable.getSelectionModel().getSelectedItem();
      if(revTable.getSelectionModel().isEmpty()){
          lblDeleteWarn.setVisible(true);
      }else{
          String regex = "(?<=[\\d])(,)(?=[\\d])";
          Pattern p = Pattern.compile(regex);
          Matcher m = p.matcher(entries.getAmount());
          totAmount -= Float.parseFloat(m.replaceAll(""));
          totalAmount = getFunctions.getAmount(Float.toString(totAmount));
          lblTotalAmount.setText(totalAmount);
        ObservableList<GetEntries> selectedRows = revTable.getSelectionModel().getSelectedItems();
        ArrayList<GetEntries> rows = new ArrayList<>(selectedRows);
        rows.forEach(row -> revTable.getItems().remove(row));
          registerItem.get(entries.getDate()).remove(entries.getItem());
      }

    }

    
}
