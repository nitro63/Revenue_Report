package Controller;
import Controller.Gets.GetUser;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;
import revenue_report.DBConnection;


public class AddUserController  implements Initializable {

    @FXML
    private AnchorPane empPane;

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXPasswordField txtPass;

    @FXML
    private JFXPasswordField txtPassConf;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXComboBox<String> cboAccessLevel;

    @FXML
    private JFXButton addUser;

    @FXML
    private JFXTextField txtPasswordShown;

    @FXML
    private JFXTextField txtPasswordConfirmShown;

    @FXML
    private JFXCheckBox chkPasswordMask;

    @FXML
    private JFXButton updateUser;

    @FXML
    private JFXButton deleteUser;

    @FXML
    private TableView<GetUser> tblAddUser;

    @FXML
    private TableColumn<GetUser, String> colUserName;

    @FXML
    private TableColumn<GetUser, String> colFirstName;

    @FXML
    private TableColumn<GetUser, String> colLastName;

    @FXML
    private TableColumn<GetUser, String> colEmail;

    @FXML
    private TableColumn<GetUser, String> colLevel;

    @FXML
    private TableColumn<GetUser, String> colPass;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void chkPasswordMaskAction(ActionEvent event) {

    }

    @FXML
    void deleteUserDetails(ActionEvent event) {

    }

    @FXML
    void onEnterKey(KeyEvent event) {

    }

    @FXML
    void saveUser(ActionEvent event) {

    }

    @FXML
    void updateUserDetails(ActionEvent event) {

    }
}
