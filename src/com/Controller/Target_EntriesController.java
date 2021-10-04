/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import com.Controller.Gets.GetRevCenter;
import com.Controller.Gets.GetTargetEnt;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;
import com.revenue_report.DBConnection;

/**
 * FXML com.Controller class
 *
 * @author HP
 */
public class Target_EntriesController implements Initializable {

    @FXML
    private Button btnClose;
    @FXML
    private TextField txtEntAmt;
    @FXML
    private Spinner<Integer> spnTargYear;
    @FXML
    private Text lblEdit;
    @FXML
    private Text lblDup;
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
    private Text lblDeleteWarn;
    private final GetRevCenter GetCenter;
    @FXML
    private AnchorPane collectEntPane;
    @FXML
    private TableView<GetTargetEnt> tblCollectEnt;
    @FXML
    private Button btnClearEntr;

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
    GetTargetEnt getData, getReport, addEntries;
    entries_sideController app;

    ObservableList<String> registerItem = FXCollections.observableArrayList();
        
         boolean Condition = true;
    private final Connection con;
    private PreparedStatement stmnt;
    private ResultSet rs;
    private  Calendar cal;
    private String regex = "(?<=[\\d])(,)(?=[\\d])";
    private Pattern p = Pattern.compile(regex);
    private Matcher m;

    private String RevCent, RevCentID, Amount, Year, entriesID;
    

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
        RevCentID = GetCenter.getCenterID();
        RevCent = GetCenter.getRevCenter();
        cal = Calendar.getInstance();
        spnTargYear.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1900, 2090, cal.get(Calendar.YEAR)));
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
              btnFetchUpdate.setVisible(true);
              btnClearUpdate.setVisible(true);
              btnDeleteUpdate.setVisible(true);
              btnUpdateEntries.setVisible(true);
              colCenter.setVisible(true);
              btnEnter.setVisible(false);
              btnClear.setVisible(false);
              btnClearEntr.setVisible(false);
              btnSaveEntries.setVisible(false);
              btnDelete.setVisible(false);
              tblCollectEnt.getItems().clear();
              tblCollectEnt.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
              clear();

          }else {
              cmbUpdateYear.setVisible(false);
              btnFetchUpdate.setVisible(false);
              btnClearUpdate.setVisible(false);
              btnDeleteUpdate.setVisible(false);
              btnUpdateEntries.setVisible(false);
              colCenter.setVisible(false);
              btnEnter.setVisible(true);
              btnClear.setVisible(true);
              btnClearEntr.setVisible(true);
              btnSaveEntries.setVisible(true);
              btnDelete.setVisible(true);
              tblCollectEnt.getItems().clear();
              tblCollectEnt.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
              clear();
          }
      });
        tblCollectEnt.setOnMouseClicked(e -> {
            if (!chkUpdate.isSelected()){
                lblDeleteWarn.setVisible(false);lblDup.setVisible(false);lblEdit.setVisible(false);
            }else {
                if (tblCollectEnt.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1){
                    setEntries();
                    if (lblControlWarn.isVisible()){
                        lblControlWarn.setVisible(false);
                    }
                }
            }
        });
        tblCollectEnt.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

  private void GetRevenueYears() throws SQLException {
      stmnt = con.prepareStatement("SELECT `Year` FROM `target_entries` WHERE `target_revCenter` = '"+RevCentID+"' GROUP BY `Year`");
      rs = stmnt.executeQuery();
      cmbUpdateYear.getItems().clear();
      while (rs.next()){
          cmbUpdateYear.getItems().add(rs.getString("Year"));
      }
  }

    @FXML
    void fetchEntries(ActionEvent event) throws SQLException {
        if (!cmbUpdateYear.getSelectionModel().isEmpty()) {
            String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
                stmnt = con.prepareStatement("SELECT `target_ID`, `Year`, `Amount` FROM `target_entries` WHERE `target_revCenter` = '" + RevCentID + "' AND `Year` ='" + year + "'");
        }
        else {
            stmnt = con.prepareStatement("SELECT `target_ID`, `Year`, `Amount` FROM `target_entries` WHERE `target_revCenter` = '" + RevCentID + "'");
        }
            rs = stmnt.executeQuery();
            tblCollectEnt.getItems().clear();
            colCenter.setCellValueFactory(data -> data.getValue().IDProperty());
            colAmount.setCellValueFactory(data -> data.getValue().YearProperty());
            colYear.setCellValueFactory(data -> data.getValue().AmountProperty());
            while (rs.next()) {
                addEntries = new GetTargetEnt(rs.getString("target_ID"), rs.getString("Amount"), rs.getString("Year"));
                tblCollectEnt.getItems().add(addEntries);

            }
    }

    private void setEntries() {
        GetTargetEnt entries = tblCollectEnt.getSelectionModel().getSelectedItem();
        entriesID = entries.getID();
        spnTargYear.getValueFactory().setValue(Integer.parseInt(entries.getYear()));
        m = p.matcher(entries.getAmount());
        String amount =  m.replaceAll("");
        txtEntAmt.setText(amount);
    }

    @FXML
    void updateEntries(ActionEvent event) throws SQLException {
        if (entriesID != null){
            Integer Year = spnTargYear.getValue();
            Condition = true;
            if(spnTargYear.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please select Year");
                alert.showAndWait();
                Condition = false;
            }else {
                while(Condition) {
                    if(txtEntAmt.getText().isEmpty()){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please Enter \"Amount\"");
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
                    else{
                        stmnt = con.prepareStatement("UPDATE `target_entries` SET  " +
                                "`Amount`= '"+Float.parseFloat(txtEntAmt.getText())+"',`Year`= '"+Year+"' WHERE   " +
                                "`target_ID`= '"+entriesID+"' AND `target_revCenter`= '"+RevCentID+"'");
                        stmnt.executeUpdate();
                        clear();
                        loadRevenueCollectionTable();
                        Condition = false;
                    }
                }
            }
        }else{
            lblControlWarn.setVisible(true);
        }
    }

    void clear(){
        entriesID = null;
        spnTargYear.getValueFactory().setValue(cal.get(Calendar.YEAR));
        txtEntAmt.setText(null);
    }

    @FXML
    private void showClose(ActionEvent event) {        
        getentries_sideController().getappController().getCenterPane().getChildren().clear();
    }


    private void loadRevenueCollectionTable() throws SQLException {
        if (!cmbUpdateYear.getSelectionModel().isEmpty()) {
            String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
            stmnt = con.prepareStatement("SELECT `target_ID`, `Year`, `Amount` FROM `target_entries` WHERE `target_revCenter` = '" + RevCentID + "' AND `Year` ='" + year + "'");
        }
        else {
            stmnt = con.prepareStatement("SELECT `target_ID`, `Year`, `Amount` FROM `target_entries` WHERE `target_revCenter` = '" + RevCentID + "'");
        }
        rs = stmnt.executeQuery();
        tblCollectEnt.getItems().clear();
        colCenter.setCellValueFactory(data -> data.getValue().IDProperty());
        colAmount.setCellValueFactory(data -> data.getValue().YearProperty());
        colYear.setCellValueFactory(data -> data.getValue().AmountProperty());
        while (rs.next()) {
            addEntries = new GetTargetEnt(rs.getString("target_ID"), rs.getString("Amount"), rs.getString("Year"));
            tblCollectEnt.getItems().add(addEntries);

        }
    }

    @FXML
    private void saveEntries(ActionEvent event) {
        Year = Integer.toString(spnTargYear.getValue());
        Condition = true;
        while (Condition){
            if( Year.length() > 4 /*|| !"2".equals(txtEntYear.getText()) /*|| txtEntYear.getText().charAt(1)!= 0*/){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please ENTER YEAR correctly");
                alert.showAndWait();
                spnTargYear.getValueFactory().setValue(Calendar.getInstance().get(Calendar.YEAR));
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
                
                
            }else if(txtEntAmt.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Make All Entries");
                alert.showAndWait();
                Condition =false;
//                }
            }else if(registerItem.contains(Year)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Year has already been entered for");
                alert.showAndWait();

                Condition =false;
                }
            else{
                colAmount.setCellValueFactory( data -> data.getValue().AmountProperty());
                colYear.setCellValueFactory( data -> data.getValue().YearProperty());
        double initeAmount = Double.parseDouble(txtEntAmt.getText());
        NumberFormat formatter = new DecimalFormat("#,###.00");
                Amount= formatter.format(initeAmount);
        if("0.00".equals(Amount)){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please Amount cannot be '0'");
                alert.showAndWait();
                txtEntAmt.clear();
                Condition =false;
                
            }else{
            getReport = new GetTargetEnt(Amount, Year);
            tblCollectEnt.getItems().add(getReport);
            registerItem.add(Year);
            Condition = false;
            spnTargYear.getValueFactory().setValue(cal.get(Calendar.YEAR));
        txtEntAmt.clear();
        }
            }
        }
    }

    @FXML
    private void clearEntries(ActionEvent event) {
        if (!chkUpdate.isSelected()){
        GetTargetEnt targ = tblCollectEnt.getSelectionModel().getSelectedItem();
        if (tblCollectEnt.getSelectionModel().isEmpty()){
            lblEdit.setText("Please select a row in the table to "+'"'+"Edit"+'"');
            lblEdit.setVisible(true);
        }else {
            m = p.matcher(targ.getAmount());
            txtEntAmt.setText(m.replaceAll(""));
            spnTargYear.getValueFactory().setValue(Integer.parseInt(targ.getYear()));
            ObservableList<GetTargetEnt> selectedRows = tblCollectEnt.getSelectionModel().getSelectedItems();
            ArrayList<GetTargetEnt> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> tblCollectEnt.getItems().remove(row));
        }
        }else{
            clear();
        }
    }

    @FXML
    private void SaveToDatabase(ActionEvent event) throws SQLException {
        getData = new GetTargetEnt();
        Map<String, ArrayList<String>> duplicate = new HashMap<>();
        ResultSet rs;
        
        for(int j=0; j<=tblCollectEnt.getItems().size(); j++){
            if (j != tblCollectEnt.getItems().size()){
            getData = tblCollectEnt.getItems().get(j);
            String acCenter = RevCent;
            String amount = getData.getAmount();
            m = p.matcher(amount);
            amount = m.replaceAll("");
            float acAmount = Float.parseFloat(amount);
            int acYear = Integer.parseInt(getData.getYear());
            stmnt = con.prepareStatement("SELECT `revenue_center`, `Year` FROM `target_entries`, `revenue_centers` WHERE `revenue_centers`.`CenterID` = `target_revCenter` AND `revenue_centers`.`revenue_center`  = '"+
                    acCenter+"' AND `Year` = '"+acYear+"'");
            rs = stmnt.executeQuery();
            while(rs.next()){
                        String cent = rs.getString("revenue_center");
                        duplicate.put(cent, new ArrayList<>());

                        String year = rs.getString("Year");
                        duplicate.get(cent).add(year);

            }
            if(duplicate.containsKey(acCenter)){
                if(duplicate.get(acCenter).contains(getData.getYear())){
                    lblDup.setText("There is REVENUE TARGET Amount for "+'"'+acCenter+'"'+" for "+'"'+getData.getYear()+'"'+" already. Please select duplicate data in the table to edit or delete.");
                    lblDup.setVisible(true);
                    j = tblCollectEnt.getItems().size() + 1;
                }
            }else{
                stmnt = con.prepareStatement("INSERT INTO `target_entries`(`target_revCenter`, `Amount`," +
                        " `Year`) VALUES ('"+RevCentID+"', '"+acAmount+"', '"+acYear+"')");
                stmnt.executeUpdate();
            }
            } else {
                tblCollectEnt.getItems().clear();
            }
        }
    }

    @FXML
    private void CancelEntries(ActionEvent event) {
        clear();
        tblCollectEnt.getItems().clear();
    }

    @FXML
    private void processKeyYear(KeyEvent event) {
        String c = event.getCharacter();
        String d = Integer.toString(spnTargYear.getValue());
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

    public void deleteSelection(ActionEvent actionEvent) throws SQLException {
        if (!chkUpdate.isSelected()){
            if(tblCollectEnt.getSelectionModel().isEmpty()){
                lblDeleteWarn.setVisible(true);
            }else {
                ObservableList<GetTargetEnt> selectedRows = tblCollectEnt.getSelectionModel().getSelectedItems();
                ArrayList<GetTargetEnt> rows = new ArrayList<>(selectedRows);
                rows.forEach(row -> tblCollectEnt.getItems().remove(row));
            }
        }else {
            if (entriesID != null){
                stmnt = con.prepareStatement("DELETE FROM `target_entries` WHERE `target_ID` = '"+entriesID+"'");
                stmnt.executeUpdate();
                clear();
                loadRevenueCollectionTable();
            }else {
                lblControlWarn.setVisible(true);
            }
        }
    }
}
    

