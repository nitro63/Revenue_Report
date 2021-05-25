package Controller;
import Controller.Gets.Conditioner;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import revenue_report.DBConnection;


public class AddUserController  implements Initializable {

    @FXML
    private AnchorPane empPane;

    @FXML
    private JFXTextField txtUsername;

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

    @FXML
    private TableColumn<GetUser, String> colRevenueCenter;

    @FXML
    private JFXTextField txtLname;

    @FXML
    private JFXTextField txtFname;

    @FXML
    private JFXComboBox<String> cmbRevenueCenter;

    private final Connection con;
    private PreparedStatement stmnt;
    private ResultSet rs;
    private String username;
    private InitializerController Init = new InitializerController();
    Map<String, String> centerID = new HashMap<>();
    Map<String, String> LevelID = new HashMap<>();

    public AddUserController ()throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtPasswordConfirmShown.setVisible(false);
        txtPasswordShown.setVisible(false);
        try {
            setCenters();
            setLevel();
            setAddUser();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void setLevel() throws SQLException {
        stmnt = con.prepareStatement("SELECT `level`, `access_ID` FROM `access_levels` WHERE 1");
        rs = stmnt.executeQuery();
        while (rs.next()){
            LevelID.put(rs.getString("level"), rs.getString("access_ID"));
            cboAccessLevel.getItems().add(rs.getString("level"));
        }
    }
    private void setCenters() throws SQLException {
        stmnt = con.prepareStatement("SELECT `CenterID`, `revenue_center` FROM `revenue_centers` WHERE 1");
        rs = stmnt.executeQuery();
        while (rs.next()){
            centerID.put(rs.getString("revenue_center"), rs.getString("CenterID"));
            cmbRevenueCenter.getItems().add(rs.getString("revenue_center"));
        }
    }

    private void setAddUser() throws SQLException {
        username = LogInController.loggerUsername;
        if (LogInController.OverAllAdmin)
            colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        colFirstName.setCellValueFactory(d -> d.getValue().firstNameProperty());
        colLastName.setCellValueFactory(d -> d.getValue().lastNameProperty());
        colUserName.setCellValueFactory(d -> d.getValue().usernameProperty());
        colLevel.setCellValueFactory(d ->d.getValue().levelProperty());
        colRevenueCenter.setCellValueFactory(d -> d.getValue().user_centerProperty());
        colPass.setCellValueFactory(d -> d.getValue().passwordProperty());

            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `password`, `revenue_center` FROM `access_levels`, `revenue_centers`, `user` WHERE `center` IS NOT NULL AND `center` = `CenterID` AND `access_level` = `access_ID`");
        rs = stmnt.executeQuery();
        while (rs.next()){
            GetUser newUser =new GetUser(
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("level"),
                    rs.getString("revenue_center")
            );
            tblAddUser.getItems().add(newUser);
        }
        stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `password` FROM `access_levels`, `user` WHERE `center` IS NULL AND `access_level` = `access_ID`");
        rs = stmnt.executeQuery();
        while (rs.next()){
            GetUser newUser =new GetUser(
                    rs.getString("username"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("level"),
                    "NA"
            );
            tblAddUser.getItems().add(newUser);
        }

    }

    @FXML
    void chkPasswordMaskAction(ActionEvent event) {
        if (chkPasswordMask.isSelected())
        {
            txtPasswordShown.setText(txtPass.getText());
            txtPasswordShown.setVisible(true);
            txtPasswordConfirmShown.setText(txtPassConf.getText());
            txtPasswordConfirmShown.setVisible(true);
            txtPass.setVisible(false);
            txtPassConf.setVisible(false);
        } else {
            txtPass.setText(txtPasswordShown.getText());
            txtPass.setVisible(true);
            txtPasswordShown.setVisible(false);
            txtPassConf.setText(txtPasswordConfirmShown.getText());
            txtPassConf.setVisible(true);
            txtPasswordConfirmShown.setVisible(false);
        }
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
