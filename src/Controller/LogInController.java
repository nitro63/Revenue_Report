package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import revenue_report.DBConnection;
import revenue_report.PromptDialogController;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    private AnchorPane paneLogIn;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXCheckBox chkPasswordMask;

    @FXML
    private JFXButton btnLogIn;

    @FXML
    private JFXTextField txtPasswordShown;

    @FXML
    private AnchorPane topPane;

    @FXML
    private Label lblWarnUsername;

    @FXML
    private Label lblWarnPassword;

    @FXML
    private JFXCheckBox chkSaveCredentials;
    private final Connection con;
    private PreparedStatement stmnt;
    public static String loggerUsername = "";
    public static String loggerAccessLevel = "";

    public LogInController() throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtPasswordShown.setVisible(false);
    }

    void userLogger(){
        //Taking input from the username & password fields
        String username = txtUsername.getText();
        String password;

        //Getting input from the field in which
        //user inputted password.
        //Note: We have two password field.
        //One for visible password and another for hidden.
        if(chkPasswordMask.isSelected())
            password = txtPasswordShown.getText();
        else
            password = txtPassword.getText();

        //Checking if any fields were blank
        //If not then we're attempting to connect to DB

        if (username.matches("\\s+")) {
            lblWarnUsername.setVisible(true);
        } else if(password.matches("\\s+")) {
            lblWarnPassword.setVisible(true);
        } else {
            try {
                stmnt = con.prepareStatement("SELECT * FROM user WHERE username = '"+username+"' AND password = '"+username+"'");
                ResultSet rs = stmnt.executeQuery();

                if (rs.next()) {
                    //Setting user credentials for further processing
                    loggerUsername = rs.getString("username");
                    loggerAccessLevel = rs.getString("accessLevel");

                    //Checking for Save Credential CheckBox
                    //Upon true value saving new credents in DataBase
                    if(chkSaveCredentials.isSelected()) {
                        PreparedStatement delPrevCredents = con.prepareStatement("DELETE FROM usercredents");
                        delPrevCredents.executeUpdate();

                        PreparedStatement saveCredents = con.prepareStatement("INSERT INTO usercredents VALUES ('"+username+"',"+"'"+password+"')");
                        saveCredents.executeUpdate();
                    }

                    Stage logIn = (Stage) btnLogIn.getScene().getWindow(); //Getting current window

                    Stage base = new Stage();
                    Parent root = null;

                    //Moving to InitializerController Class to load all required main.resources
                    try {
                        root = FXMLLoader.load(getClass().getResource("/main/resources/view/initializer.fxml"));
                        Scene s = new Scene(root);
                        logIn.setScene(s);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    new PromptDialogController("Authentication Error!", "Either username or password did not match!");
                }

                con.close();
            } catch (SQLException e) {
                System.out.println(e.getErrorCode());
                e.printStackTrace();

            }
        }
    }

    @FXML
    void chkPasswordMaskAction(ActionEvent event) {
        if (chkPasswordMask.isSelected())
        {
            txtPasswordShown.setText(txtPassword.getText());
            txtPasswordShown.setVisible(true);
            txtPassword.setVisible(false);
        } else {
            txtPassword.setText(txtPasswordShown.getText());
            txtPassword.setVisible(true);
            txtPasswordShown.setVisible(false);
        }
    }

    @FXML
    void ctrlLogInCheck(ActionEvent event) {

    }

    @FXML
    void onEnterKey(KeyEvent event) {

    }
}
