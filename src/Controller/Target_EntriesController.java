/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.Gets.GetRevCenter;
import Controller.Gets.GetTargetEnt;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.StringUtils;
import revenue_report.DBConnection;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class Target_EntriesController implements Initializable {

    @FXML
    private Button btnClose;
    @FXML
    private TextField txtEntYear;
    @FXML
    private TextField txtEntAmt;
    @FXML
    private Button btnEnter;
    @FXML
    private Button btnClear;
    @FXML
    private TableColumn<GetTargetEnt, String> colCenter;
    @FXML
    private TableColumn<GetTargetEnt, String> colAmount;
    @FXML
    private TableColumn<GetTargetEnt, String> colYear;
    @FXML
    private Button btnSaveEntries;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private Label lblDeleteWarn;
    
    GetTargetEnt getData, getReport;
    
    entries_sideController app; 
    
    private final GetRevCenter GetCenter;
    @FXML
    private AnchorPane collectEntPane;
    @FXML
    private TableView<GetTargetEnt> tblCollectEnt;
    @FXML
    private Button btnClearEntr;
    
        ObservableList<String> registerItem = FXCollections.observableArrayList(); 
        
         boolean Condition = true;
    private final Connection con;
    private PreparedStatement stmnt;
        
        String RevCent, Amount, Year;
    

    /**
     * Initializes the controller class.
     * @param GetCenter
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     */
    public Target_EntriesController(GetRevCenter GetCenter) throws SQLException, ClassNotFoundException{
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
        tblCollectEnt.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblCollectEnt.setOnMouseClicked(e -> {
            lblDeleteWarn.setVisible(false);
        });
    }    

    @FXML
    private void showClose(ActionEvent event) {        
        getentries_sideController().getappController().getCenterPane().getChildren().clear();
    }


    @FXML
    private void saveEntries(ActionEvent event) {
        RevCent = GetCenter.getRevCenter();
        Year = txtEntYear.getText();
        Condition = true;
        while (Condition){
            if(registerItem.contains(Year)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Year has already been entered for");
                alert.showAndWait();
                
                Condition =false;
                
            }else if(txtEntAmt.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please enter Amount");
                alert.showAndWait();
                
                Condition =false;
            }else if(RevCent == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Select Revenue Center");
                alert.showAndWait();
                Condition =false;
                
            }
            else if(txtEntAmt.getText().startsWith(".") && txtEntAmt.getText().endsWith(".") || StringUtils.countMatches(txtEntAmt.getText(), ".") >1){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Enter Amount correctly");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
                
                
            }else if(txtEntYear.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Enter YEAR");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
                
                
            }else if(txtEntAmt.getText().isEmpty()|| txtEntYear.getText().isEmpty() || registerItem.contains(Year)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Make All Entries");
                alert.showAndWait();
                Condition =false;
//                }
            }else if( txtEntYear.getText().length() > 4 /*|| !"2".equals(txtEntYear.getText()) /*|| txtEntYear.getText().charAt(1)!= 0*/){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please ENTER YEAR correctly");
                alert.showAndWait();
                txtEntYear.clear();               
                Condition =false;
            }
            else{
                colCenter.setCellValueFactory( data -> data.getValue().CenterProperty());
                colAmount.setCellValueFactory( data -> data.getValue().AmountProperty());
                colYear.setCellValueFactory( data -> data.getValue().YearProperty());
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
            getReport = new GetTargetEnt(RevCent, Amount, Year);
            tblCollectEnt.getItems().add(getReport);
            registerItem.add(txtEntYear.getText());
            Condition = false;
        txtEntYear.clear();
        txtEntAmt.clear();
        }
            }
        }
    }

    @FXML
    private void clearEntries(ActionEvent event) {
        txtEntYear.clear();
        txtEntAmt.clear();
    }

    @FXML
    private void SaveToDatabase(ActionEvent event) throws SQLException {
        getData = new GetTargetEnt();
        Map<String, ArrayList<String>> duplicate = new HashMap<>();
        ResultSet rs;
        ResultSetMetaData rm;
        
        for(int j=0; j<tblCollectEnt.getItems().size(); j++){
            getData = tblCollectEnt.getItems().get(j);
            String acCenter = getData.getCenter();
            String regex = "(?<=[\\d])(,)(?=[\\d])";
            Pattern p = Pattern.compile(regex);
            String amount = getData.getAmount();
            Matcher m = p.matcher(amount);
            amount = m.replaceAll("");
            float acAmount = Float.parseFloat(amount);
            int acYear = Integer.parseInt(getData.getYear());
            stmnt = con.prepareStatement("SELECT * FROM `target_entries` WHERE `revCenter` = '"+acCenter+"' AND `Year` = '"+acYear+"'");
            rs = stmnt.executeQuery();
            rm = rs.getMetaData();
            int col = rm.getColumnCount();
            
            while(rs.next()){
                for(int k= 1; k<= col; k++){
                    if(k == 1){
                        String cent = rs.getObject(1).toString();
                        duplicate.put(cent, new ArrayList<>());
                    }
                    if(k == 3){
                        String year = rs.getObject(3).toString();
                        duplicate.get(rs.getObject(1).toString()).add(year);
                    }
                }
            }
            if(duplicate.containsKey(acCenter)){
                if(duplicate.get(acCenter).contains(getData.getYear())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("TARGET already ENTERED for YEAR");
                alert.showAndWait();
                tblCollectEnt.getItems().remove(j);
                }
            }else{
                stmnt = con.prepareStatement("INSERT INTO `target_entries`(`revCenter`, `Amount`, `Year`) VALUES ('"+acCenter+"', '"+acAmount+"', '"+acYear+"')");
                stmnt.executeUpdate();
            }
        }
        System.out.println(duplicate);
        tblCollectEnt.getItems().clear();
    }

    @FXML
    private void CancelEntries(ActionEvent event) {
        tblCollectEnt.getItems().clear();
    }

    @FXML
    private void processKeyYear(KeyEvent event) {
        String c = event.getCharacter();
        String d = txtEntYear.getText();
    if("1234567890".contains(c) ) {}
    else if(d.length()>4) {
        event.consume();
    }
    }

    @FXML
    private void processKeyAmount(KeyEvent event) {
        String c = event.getCharacter();
    if("1234567890.".contains(c)) {}
    else {
        event.consume();
    }
    }

    public void deleteSelection(ActionEvent actionEvent) {
        if(tblCollectEnt.getSelectionModel().isEmpty()){
            lblDeleteWarn.setVisible(true);
        }else {
        ObservableList<GetTargetEnt> selectedRows = tblCollectEnt.getSelectionModel().getSelectedItems();
        ArrayList<GetTargetEnt> rows = new ArrayList<>(selectedRows);
        rows.forEach(row -> tblCollectEnt.getItems().remove(row));
        }
    }
    
}
