package Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class add_sideController implements Initializable {

    @FXML
    private Button btnAddItem;

    @FXML
    private Button btnAddCenter;

    @FXML
    private Button btnView;

    @FXML
    private Button btnAssignItem;
    appController app;
    public void setappController(appController app){
        this.app = app;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    void ShowAddItem(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/revenueItems.fxml"));
        loader.setController(new RevenueItemsController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    void showAddCenter(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/revenueCenters.fxml"));
        loader.setController(new RevenueCenterController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    void showAssignItem(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/Views/fxml/assignItem.fxml"));
        loader.setController(new AssignItemController());
        app.getCenterPane().getChildren().clear();
        app.getCenterPane().getChildren().add(loader.load());
    }

    @FXML
    void showView(ActionEvent event) {

    }
}
