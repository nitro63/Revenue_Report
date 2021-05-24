package Controller;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewUserController implements Initializable {

    @FXML
    private AnchorPane empPane;

    @FXML
    private AnchorPane paneUpdate;

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXPasswordField txtPass;

    @FXML
    private JFXPasswordField txtPassConf;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXButton btnUpdateUser;

    @FXML
    private JFXTextField txtPasswordShown;

    @FXML
    private JFXTextField txtPasswordConfirmShown;

    @FXML
    private Label lblAccessLevelUpdate;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private AnchorPane paneView;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblUsername;

    @FXML
    private Label lblAccessLevel;

    @FXML
    private Label lblLastName;

    @FXML
    private Label lblFirstName;

    @FXML
    private JFXCheckBox chkUpdateMask;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void chkUpdateMaskAction(ActionEvent event) {

    }

    @FXML
    void onEnterKey(KeyEvent event) {

    }

    @FXML
    void updateUser(ActionEvent event) {

    }
}
