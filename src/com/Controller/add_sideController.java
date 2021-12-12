package com.Controller;
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ResourceBundle;

public class add_sideController implements Initializable {

    @FXML
    private VBox administrativeMain;

    @FXML
    private Text txtAdministrative;

    @FXML
    private MaterialDesignIconView iconForward;

    @FXML
    private Text txtTitle;

    @FXML
    private HBox containerAddItem;

    @FXML
    private JFXButton btnAddItem;

    @FXML
    private Button btnCloseAddItem;

    @FXML
    private HBox containerAssignItem;

    @FXML
    private JFXButton btnAssignItem;

    @FXML
    private Button btnCloseAssignItem;

    @FXML
    private HBox containerAddCenter;

    @FXML
    private JFXButton btnAddCenter;

    @FXML
    private Button btnCloseCenter;

    @FXML
    private HBox containerAddUser;

    @FXML
    private JFXButton btnAddUser;

    @FXML
    private Button btnCloseAddUser;

    @FXML
    private HBox containerAddTicketDetails;

    @FXML
    private JFXButton btnAddTicketDetails;

    @FXML
    private Button btnCloseAddTicketDetails;

    @FXML
    private VBox paneEntriesArea;

    @FXML
    private AnchorPane noEntriesPane;

    @FXML
    private Button btnView;
    appController app;
    public void setappController(appController app){
        this.app = app;
    }
    public appController getappController(){
        return app;
    }
    private ObservableList<JFXButton> menuButtons = FXCollections.observableArrayList();
    private ObservableList <HBox> menuContainers = FXCollections.observableArrayList();
    private ObservableList <Button> menuCloseButton = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb){
        txtAdministrative.setVisible(false);
        iconForward.setVisible(false);
        txtTitle.setText(null);
        txtTitle.setVisible(false);
        menuButtons.addAll(Arrays.asList(btnAddCenter,btnAddItem,btnAddUser,btnAssignItem));
        menuContainers.addAll(Arrays.asList(containerAddCenter,containerAddItem,containerAddUser,containerAssignItem));
        menuCloseButton.addAll(Arrays.asList(btnCloseAddItem,btnCloseAddUser,btnCloseCenter,btnCloseAssignItem));
    }

    @FXML
    void ShowAddItem(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/revenueItems.fxml"));
        loader.setController(new RevenueItemsController());
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    void showAddCenter(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/revenueCenters.fxml"));
        loader.setController(new RevenueCenterController());
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    void showAssignItem(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/com/Views/fxml/assignItem.fxml"));
        loader.setController(new AssignItemController());
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loader.load());
    }

    @FXML
    private void showClose(ActionEvent event) {
    }

    @FXML
    void showAddUser(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loadAddUser = new FXMLLoader();
        loadAddUser.setLocation(getClass().getResource("/com/Views/fxml/addUser.fxml"));
        loadAddUser.setController(new AddUserController());
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loadAddUser.load());
    }

    @FXML
    private void showTicketDetails(ActionEvent event) throws IOException {
        FXMLLoader loadTicketDetails = new FXMLLoader();
        loadTicketDetails.setLocation(getClass().getResource("/com/Views/fxml/addTicketDetails.fxml"));
        loadTicketDetails.setController(new AddTicketDetailsController());
        paneEntriesArea.getChildren().clear();
        paneEntriesArea.getChildren().add(loadTicketDetails.load());
    }

    @FXML
    void showView(ActionEvent event) {

    }
}
