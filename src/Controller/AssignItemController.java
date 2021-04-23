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
import javafx.scene.control.*;
import revenue_report.DBConnection;

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
        stmnt = con.prepareStatement("SELECT `revenue_center`, `CenterID` FROM `revenue_centers` WHERE 1");
        rs = stmnt.executeQuery();
        cmbCenters.getItems().clear();
        centerId.clear();
        while(rs.next()){
            cmbCenters.getItems().add(rs.getString("revenue_center"));
            centerId.put(rs.getString("revenue_center"), rs.getString("CenterID"));
        }
    }

    void loadItems() throws SQLException {
        stmnt = con.prepareStatement("SELECT `revenue_item`, `revenue_item_ID` FROM `revenue_items` WHERE 1");
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
        if (!cmbCenters.getSelectionModel().isEmpty()){
        colCenter.setCellValueFactory(e ->e.getValue().centerProperty());
        colCode.setCellValueFactory(e -> e.getValue().IDProperty());
        colItem.setCellValueFactory(e -> e.getValue().categoryProperty());
        String centId = centerId.get(cmbCenters.getSelectionModel().getSelectedItem());
        stmnt = con.prepareStatement("SELECT `revenue_items`.`revenue_item`, `revenue_items`.`revenue_item_ID`," +
                " `revenue_centers`.`revenue_center` FROM `revenue_items`,`center_items`, `revenue_centers` WHERE" +
                " `center_items`.`assign_center` = '"+centId+"' AND `revenue_centers`.`CenterID` =" +
                " `center_items`.`assign_center` AND `revenue_items`.`revenue_item_ID` = `center_items`.`assign_item`");
        rs = stmnt.executeQuery();
        tblAssign.getItems().clear();
        while (rs.next()){
            getData = new GetDetails(rs.getString("revenue_center"), rs.getString("revenue_item_ID")
                    , rs.getString("revenue_item"));
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
                minute = Integer.toString(cal.get(Calendar.MINUTE)),
                hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
        String id = UUID.randomUUID().toString(),
                is = id.replace("-", "").substring(9,12),
                ID = id.replace("-", "").substring(17,19),
                si = id.replace("-", "").substring(23,24),
                fini = is+ID+si.concat(Day).concat(date).concat(month).
                        concat(year).concat(hour).concat(minute);
        return fini;
    }

    @FXML
    void addItem(ActionEvent event) throws SQLException {
        ObservableList<String> dup = FXCollections.observableArrayList();
         if (!cmbCenters.getSelectionModel().isEmpty() && !lstItemList.getSelectionModel().isEmpty()){
             itemSelected = lstItemList.getSelectionModel().getSelectedItems();
             String cent  = centerId.get(cmbCenters.getSelectionModel().getSelectedItem());
             stmnt = con.prepareStatement("SELECT `assign_item` FROM `center_items` WHERE `assign_center` = '"+cent+"'");
             rs = stmnt.executeQuery();
             dup.clear();
             while (rs.next()){
                 dup.add(rs.getString("assign_item"));
             }
             String id;
        for(String I : itemSelected){
            String itemID = itemId.get(I);
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
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Duplicate Entry");
                alert.setHeaderText("DUPLICATE");
                alert.setContentText("Please '" + itemId.get(I) + "' already exist");
                alert.showAndWait();
            }
        }
        loadTable();
         }else if (lstItemList.getSelectionModel().isEmpty()){
             event.consume();
         }else if (cmbCenters.getSelectionModel().isEmpty()){
             lblCentWarn.setVisible(true);
         } else if (lstItemList.getSelectionModel().isEmpty()){

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
