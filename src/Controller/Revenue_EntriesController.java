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
import java.util.ResourceBundle;

import Controller.Gets.GetFunctions;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private TableColumn<GetEntries, String> revCenter;
    @FXML
    private TableColumn<GetEntries, String> revItem;
    @FXML
    private TableColumn<GetEntries, String> revAmount;
    @FXML
    private TableColumn<GetEntries, String> revDate;
    @FXML
    private TableColumn<GetEntries, String> revWeek;
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

        
        
        
         boolean Condition = true;
        
        String Date, Item, Code, Month, Amount, Week, Year, RevCent, Qtr;
        
        
                
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
          lblDeleteWarn.setVisible(false);
      });
        GetRevenueItems();
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
                txtEntAmt.clear();
                Condition =false;
                
                
            }else if(StringUtils.countMatches(txtEntAmt.getText(), ".") >1){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Enter Amount");
                alert.setContentText("Please check the number of '.' in the amount");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
            }
            else{
                revCode.setCellValueFactory(data -> data.getValue().CodeProperty());
        revItem.setCellValueFactory(data -> data.getValue().ItemProperty());
        revAmount.setCellValueFactory(data -> data.getValue().AmountProperty());
        revCenter.setCellValueFactory(data -> data.getValue().CenterProperty());
        revDate.setCellValueFactory(data -> data.getValue().DateProperty());
        revWeek.setCellValueFactory(data -> data.getValue().WeekProperty());
        revMonth.setCellValueFactory(data -> data.getValue().MonthProperty());
        revQuarter.setCellValueFactory(data -> data.getValue().QuarterProperty());
        revYear.setCellValueFactory(data -> data.getValue().YearProperty());
        Amount= getFunctions.getAmount(txtEntAmt.getText());
        if("0.00".equals(Amount)){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Amount cannot be '0'");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
                
            }else{
               addEntries = new GetEntries(Code,RevCent, Item, Date, Month, Amount, Week, Year, Qtr);
                revTable.getItems().add(addEntries);
                
                if(!registerItem.containsKey(Date)){
                    registerItem.put(Date, new ArrayList());
                    registerItem.get(Date).add(i);
                }else if(registerItem.containsKey(Date) && !registerItem.get(Date).contains(i)){
                    registerItem.get(Date).add(i);
                }
     
//                registerItem.add(cmbEntRevItem.getSelectionModel().getSelectedIndex());
                
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
        cmbEntRevItem.getSelectionModel().clearSelection();
        entDatePck.setValue(null);
        txtEntAmt.clear();
    }

    @FXML
    private void SaveToDatabase(ActionEvent event) throws ClassNotFoundException, SQLException {
        
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
        
        
        for(int i = 0; i < revTable.getItems().size(); i++){
            getData = revTable.getItems().get(i);
            String acDate = getData.getDate();
            String acCenter = getData.getCenter();
            String acCode = getData.getCode();
            String acItem = getData.getItem();
            String acQtr = getData.getQuarter();
            String regex = "(?<=[\\d])(,)(?=[\\d])";
            Pattern p = Pattern.compile(regex);
            String amount = getData.getAmount();
            Matcher m = p.matcher(amount);
            amount = m.replaceAll("");
            float acAmount = Float.parseFloat(amount);
            int acWeek = Integer.parseInt(getData.getWeek());
            String acMonth = getData.getMonth();
            int acYear = Integer.parseInt(getData.getYear());
            stmnt = connection.prepareStatement("SELECT * FROM `daily_entries` WHERE `Code` = '"+acCode+"' AND `revenueDate` = '"+acDate+"' AND `revCenter` = '"+RevCent+"' ");
            rs = stmnt.executeQuery();       
            metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            while(rs.next()){
                for(int k = 1; k<=columns; k++){
                    
                    if(k == 2){
                        String dup = rs.getObject(k).toString();
                        duplicate.add(dup);
                    }
                }
            }
            if(duplicate.contains(acCode)){
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Clear Duplicate data");
                alert.showAndWait();
                revTable.getItems().remove(i);
            }else{
                stmnt = connection.prepareStatement("INSERT INTO `daily_entries`(`revCenter`, `Code`, `revenueItem`, `revenueAmount`, `revenueDate`, `revenueWeek`, `revenueMonth`, `revenueYear`, `revenueQuarter`)"
                        +"VALUES('"+acCenter+"', '"+acCode+"', '"+acItem+"', '"+acAmount+"', '"+acDate+"', '"+acWeek+"','"+acMonth+"', '"+acYear+"', '"+acQtr+"')");
                stmnt.executeUpdate();
            }
            forDatabase.get("revCenter").add(RevCent);
            forDatabase.get("revCode").add(getData.getCode());
            forDatabase.get("revItem").add(getData.getItem());
            forDatabase.get("revDate").add(getData.getDate());
            forDatabase.get("revMonth").add(getData.getMonth());
            forDatabase.get("revAmount").add(getData.getAmount());
            forDatabase.get("revWeek").add(getData.getWeek());
            arrList.add(new ArrayList<>());
            arrList.get(i).add(forDatabase.get("revCenter").toString());
            arrList.get(i).add(forDatabase.get("revCode").toString());
            arrList.get(i).add(forDatabase.get("revItem").toString());
            arrList.get(i).add(forDatabase.get("revDate").toString());
            arrList.get(i).add(forDatabase.get("revMonth").toString());
            arrList.get(i).add(forDatabase.get("revAmount").toString());
            arrList.get(i).add(forDatabase.get("revWeek").toString());            
        }
        
         for (Entry<String, ArrayList<String>> entry : forDatabase.entrySet()) {
        System.out.print(entry.getKey()+" | ");
        for(String fruitNo : entry.getValue()){
            System.out.print(fruitNo+" ");
        }
        System.out.println();
    }
    
for(int i = 0; i<arrList.size(); i++){
    for(int j = 0; j<arrList.get(i).size(); j++){
        System.out.println(arrList.get(i).get(j));
    }
}

                revTable.getItems().clear();

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
      if(revTable.getSelectionModel().isEmpty()){
          lblDeleteWarn.setVisible(true);
      }else{
        ObservableList<GetEntries> selectedRows = revTable.getSelectionModel().getSelectedItems();
        ArrayList<GetEntries> rows = new ArrayList<>(selectedRows);
        rows.forEach(row -> revTable.getItems().remove(row));
      }

    }

    
}
