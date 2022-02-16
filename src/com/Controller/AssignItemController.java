package com.Controller;
import com.Models.GetAssignItems;
import com.Models.GetDetails;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import com.revenue_report.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AssignItemController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbCenters;

    @FXML
    private JFXListView<String> lstItemList;

    @FXML
    private TableView<GetDetails> tblAssign;

    @FXML
    private TableColumn<GetDetails, String> colCenter;

    @FXML
    private TableColumn<GetDetails, String> colCode;

    @FXML
    private TableColumn<GetDetails, String> colItem;

    @FXML
    private TableView<GetAssignItems> tblItems;

    @FXML
    private TableColumn<GetAssignItems, String> colCodeID;

    @FXML
    private TableColumn<GetAssignItems, String> colCategory;

    @FXML
    private TableColumn<GetAssignItems, String> colRevItem;

    @FXML
    private JFXButton btnLoad;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private Label lblCentWarn;

    @FXML
    private Label lblControlWarn;

    @FXML
    private Label lblCenter;

    private PreparedStatement stmnt;
    private ResultSet rs;
    private final Connection con;
    GetDetails getData;

    ObservableList<String> items = FXCollections.observableArrayList();
    ObservableList<GetAssignItems> itemSelected = FXCollections.observableArrayList();
    Map<String, String> itemId = new HashMap<>();
    Map<String, String> centerId = new HashMap<>();

    public AssignItemController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbCenters.setOnMouseClicked(e -> {
            lblCentWarn.setVisible(false);
        });
        tblAssign.setOnMouseClicked(e ->{
            if (lblControlWarn.isVisible()){
                lblControlWarn.setVisible(false);
            }
        });
        lstItemList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblAssign.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblItems.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        try {
            getRevenueCenters();
            loadItems();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void getRevenueCenters() throws SQLException {
        stmnt = con.prepareStatement("SELECT `revenue_center`, `CenterID` FROM `revenue_centers` WHERE 1");
        rs = stmnt.executeQuery();
        cmbCenters.getItems().clear();
        centerId.clear();
        while(rs.next()){
            cmbCenters.getItems().add(rs.getString("revenue_center"));
//            Collections.sort(cmbCenters.getItems());
            centerId.put(rs.getString("revenue_center"), rs.getString("CenterID"));
        }
    }

    void loadItems() throws SQLException {
        stmnt = con.prepareStatement("SELECT `item_Sub`, `revenue_item_ID`, `item_category` FROM `revenue_items` WHERE 1 ORDER BY `item_category` ASC");
        rs = stmnt.executeQuery();
        items.clear();
        tblItems.getItems().clear();
        colCodeID.setCellValueFactory(e ->e.getValue().IDProperty());
        colCategory.setCellValueFactory(e -> e.getValue().categoryProperty());
        colRevItem.setCellValueFactory(e -> e.getValue().subItemProperty());
        while (rs.next()){
            tblItems.getItems().add(new GetAssignItems(rs.getString("item_Sub"), rs.getString("revenue_item_ID"), rs.getString("item_category")));
            itemId.put(rs.getString("item_Sub"), rs.getString("revenue_item_ID"));
            items.add(rs.getString("item_Sub"));
        }
        Collections.sort(items);
        lstItemList.getItems().clear();
        lstItemList.getItems().addAll(items);
    }

    @FXML
    void LoadItems(ActionEvent event) throws SQLException {
        loadTable();
    }

    void loadTable() throws SQLException {
        if (!cmbCenters.getSelectionModel().isEmpty()){
        colCenter.setCellValueFactory(e ->e.getValue().centerProperty());
        colCode.setCellValueFactory(e -> e.getValue().IDProperty());
        colItem.setCellValueFactory(e -> e.getValue().categoryProperty());
        String centId = centerId.get(cmbCenters.getSelectionModel().getSelectedItem());
        stmnt = con.prepareStatement("SELECT `item_Sub`, `revenue_items`.`revenue_item_ID`," +
                " `revenue_center`, `item_category`,`assign_code` FROM `revenue_items`,`center_items`, `revenue_centers` WHERE" +
                " `center_items`.`assign_center` = '"+centId+"' AND `revenue_centers`.`CenterID` =" +
                " `center_items`.`assign_center` AND `revenue_items`.`revenue_item_ID` = `center_items`.`assign_item`");
        rs = stmnt.executeQuery();
        tblAssign.getItems().clear();
        while (rs.next()){
            lblCenter.setText(rs.getString("revenue_center"));
            getData = new GetDetails(rs.getString("item_category"), rs.getString("assign_code")
                    , rs.getString("item_Sub"));
            tblAssign.getItems().add(getData);
        }
        }else {
            lblCentWarn.setVisible(true);
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
                minute = Integer.toString(cal.get(Calendar.MILLISECOND)),
                hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String id = UUID.randomUUID().toString(),
                is = id.replace("-", "").substring(9,12),
                ID = id.replace("-", "").substring(17,19),
                si = id.replace("-", "").substring(23,24),
                fini = is+ID+si.concat(second).concat(date).concat(month).
                        concat(year).concat(hour).concat(minute);
        return fini;
    }

    @FXML
    void addItem(ActionEvent event) throws SQLException {
        ObservableList<String> dup = FXCollections.observableArrayList();
         if (!cmbCenters.getSelectionModel().isEmpty() && !tblItems.getSelectionModel().isEmpty()){
             itemSelected = tblItems.getSelectionModel().getSelectedItems();
             String cent  = centerId.get(cmbCenters.getSelectionModel().getSelectedItem());
             stmnt = con.prepareStatement("SELECT `assign_item` FROM `center_items` WHERE `assign_center` = '"+cent+"'");
             rs = stmnt.executeQuery();
             dup.clear();
             while (rs.next()){
                 dup.add(rs.getString("assign_item"));
             }
             String id;
        for(GetAssignItems I : itemSelected){
            String itemID = itemId.get(I.getSubItem());
//            System.out.println("true");
            if (!dup.contains(itemID)){
                id = getID();
                boolean condition = true;
                ArrayList<String> dupID = new ArrayList<>();
                while (condition) {
                    stmnt = con.prepareStatement("SELECT `assign_code` FROM `center_items` WHERE `assign_code` = " +
                            "'" + id + "' ");
                    ResultSet rt = stmnt.executeQuery();
                    while (rt.next()) {
                        dupID.add(rt.getString("assign_code"));
                    }
                    if (dupID.contains(id)) {
                        id = getID();
                    } else {
                        condition = false;
                    }
                }
                stmnt = con.prepareStatement("INSERT INTO `center_items`(`assign_item`, `assign_center`, " +
                        "`assign_code`) VALUES ('"+itemID+"', '"+cent+"', '"+id+"')");
                stmnt.executeUpdate();
            } /*else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText("DUPLICATE");
                alert.setContentText("Please '" + I.getSubItem() + "' already exist");
                alert.showAndWait();
            }*/
        }
        loadTable();
        resetFields();

         }else if (cmbCenters.getSelectionModel().isEmpty()){
             lblCentWarn.setVisible(true);
         }else if (tblItems.getSelectionModel().isEmpty()){
             event.consume();
         } else if (tblItems.getSelectionModel().isEmpty()){
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Warning");
             alert.setContentText("Please select Item from the list to add");
             alert.showAndWait();
         }
    }

    void resetFields(){
        lstItemList.getSelectionModel().clearSelection();
        cmbCenters.getSelectionModel().clearSelection();
    }

    @FXML
    void deleteItem(ActionEvent event) throws SQLException {
        ObservableList<GetDetails> assignedItemSelected = FXCollections.observableArrayList();
        assignedItemSelected = tblAssign.getSelectionModel().getSelectedItems();
        if (!tblAssign.getSelectionModel().isEmpty()){
        for (GetDetails I : assignedItemSelected){
        String id = I.getID();
        stmnt = con.prepareStatement("DELETE FROM `center_items` WHERE `assign_code` = '"+id+"'");
        stmnt.executeUpdate();
//            resetFields();
//        id = null;
        }
            loadTable();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please select Item from table to delete");
            alert.showAndWait();
        }
    }
}
