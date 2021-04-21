package Controller;
import Controller.Gets.GetDetails;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import revenue_report.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

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
    private JFXButton btnLoad;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnAdd;

    @FXML
    private Label lblCentWarn;

    @FXML
    private Label lblControlWarn;

    private PreparedStatement stmnt;
    private ResultSet rs;
    private final Connection con;
    GetDetails getData;

    ObservableList<String> items = FXCollections.observableArrayList();
    ObservableList<String> itemSelected = FXCollections.observableArrayList();
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
        try {
            getRevenueCenters();
            loadItems();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    void getRevenueCenters() throws SQLException {
        stmnt = con.prepareStatement("SELECT `revenue_center`, `CenterID` FROM `revenue_centers` GROUP BY `revenue_center`");
        rs = stmnt.executeQuery();
        cmbCenters.getItems().clear();
        centerId.clear();
        while(rs.next()){
            cmbCenters.getItems().add(rs.getString("revenue_center"));
            centerId.put(rs.getString("revenue_center"), rs.getString("CenterID"));
        }
    }

    void loadItems() throws SQLException {
        stmnt = con.prepareStatement("SELECT `revenue_item`, `revenue_item_ID` FROM `revenue_items` ");
        rs = stmnt.executeQuery();
        items.clear();
        while (rs.next()){
            itemId.put(rs.getString("revenue_item"), rs.getString("revenue_item_ID"));
            items.add(rs.getString("revenue_item"));
        }
        lstItemList.getItems().clear();
        lstItemList.getItems().addAll(items);
    }

    @FXML
    void LoadItems(ActionEvent event) throws SQLException {
        loadTable();
    }

    void loadTable() throws SQLException {
        String cent  = cmbCenters.getSelectionModel().getSelectedItem();
        if (!cmbCenters.getSelectionModel().isEmpty()){
        colCenter.setCellValueFactory(e ->e.getValue().centerProperty());
        colCode.setCellValueFactory(e -> e.getValue().IDProperty());
        colItem.setCellValueFactory(e -> e.getValue().categoryProperty());
        stmnt = con.prepareStatement("SELECT * FROM `center_items` WHERE `assign_center` = '"+cent+"'");
        rs = stmnt.executeQuery();
        tblAssign.getItems().clear();
        while (rs.next()){
            getData = new GetDetails(rs.getString("assign_center"), rs.getString("assign_code"), rs.getString("assign_item"));
            tblAssign.getItems().add(getData);
        }
        }else {
            lblCentWarn.setVisible(true);
        }
    }
    @FXML
    void addItem(ActionEvent event) throws SQLException {
        itemSelected = lstItemList.getSelectionModel().getSelectedItems();
        ObservableList<String> dup = FXCollections.observableArrayList();
         if (!cmbCenters.getSelectionModel().isEmpty()){
             String cent  = cmbCenters.getSelectionModel().getSelectedItem();
             stmnt = con.prepareStatement("SELECT `assign_item` FROM `center_items` WHERE `assign_center` = '"+cent+"'");
             rs = stmnt.executeQuery();
             dup.clear();
             while (rs.next()){
                 dup.add(rs.getString("assign_item"));
             }
             String id;
        for(String I : itemSelected){
            if (!dup.contains(I)){
               id = itemId.get(I);

                stmnt = con.prepareStatement("INSERT INTO `center_items`(`assign_item`, `assign_center`, " +
                        "`assign_code`) VALUES ('"+I+"', '"+cent+"', '"+id+"')");
                stmnt.executeUpdate();
            }
        }
        loadTable();
         }else if (lstItemList.getSelectionModel().isEmpty()){
             event.consume();
         }else {
             lblCentWarn.setVisible(true);
         }

    }

    @FXML
    void deleteItem(ActionEvent event) throws SQLException {
        getData = tblAssign.getSelectionModel().getSelectedItem();
        String id = getData.getID();
        stmnt = con.prepareStatement("DELETE FROM `center_items` WHERE `assign_code` = '"+id+"' AND `assign_center`" +
                "= '"+getData.getCenter()+"'");
        stmnt.executeUpdate();
        loadTable();
    }
}
