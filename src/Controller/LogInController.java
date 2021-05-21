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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import revenue_report.DBConnection;
import revenue_report.Main;

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

    @FXML
    private Label warnlabel;
//    private final Connection con;
    private PreparedStatement stmnt;
    public static String loggerUsername = null;
    public static String loggerAccessLevel = null;
    public static String loggerCenter = null;
    public static boolean userCenter, submetro, admin, OverAllAdmin, Accountant, clerk;

    public LogInController() throws SQLException, ClassNotFoundException {
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection con = DBConnection.getConn();
        try {
            stmnt = con.prepareStatement("SELECT * FROM `usercredent` WHERE 1");
            ResultSet resultSet = stmnt.executeQuery();

            if (resultSet.next()) {
                txtUsername.setText(resultSet.getString("username")); //Getting Saved Username
                txtPassword.setText(resultSet.getString("password")); //Getting Saved Password
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        btnLogIn.setOnMouseClicked(e -> warnlabel.setVisible(false));
        txtPasswordShown.setVisible(false);
        txtUsername.setOnMouseClicked(event -> lblWarnUsername.setVisible(false));
        txtPasswordShown.setOnMouseClicked(event -> lblWarnPassword.setVisible(false));
        txtPassword.setOnMouseClicked(event -> lblWarnPassword.setVisible(false));
    }

        void userLogger() throws SQLException, IOException {
        //Taking input from the username & password fields
        String username = txtUsername.getText();
        String password;
        Connection con = DBConnection.getConn();

        //Getting input from the field in which
        //user inputted password.
        //Note: We have two password field.
        //One for visible password and another for hidden.
        if(chkPasswordMask.isSelected()) {
            password = txtPasswordShown.getText();
        }
        else{
            password = txtPassword.getText();
        }

        //Checking if any fields were blank
        //If not then we're attempting to connect to DB

        if (username.matches("\\s+") || username.isEmpty()) {
            lblWarnUsername.setVisible(true);
        } else if(password.matches("\\s+") || password.isEmpty()) {
            lblWarnPassword.setVisible(true);
        } else {
                stmnt = con.prepareStatement("SELECT `username`, `level`, `center`, `access_level` FROM `user`, `access_levels`WHERE `username` = '"+username+"' AND `password` = '"+password+"' AND `access_ID` = `access_level`");
                ResultSet rs = stmnt.executeQuery();

                if (rs.next()) {
                    //Setting user credentials for further processing
                    loggerUsername = rs.getString("username");
                    loggerAccessLevel = rs.getString("level");
                    loggerCenter = rs.getString("center");


                    //Checking for Save Credential CheckBox
                    //Upon true value saving new credents in DataBase
                    if(chkSaveCredentials.isSelected()) {
                        PreparedStatement delPrevCredents = con.prepareStatement("DELETE FROM `usercredent`");
                        delPrevCredents.executeUpdate();

                        PreparedStatement saveCredents = con.prepareStatement("INSERT INTO `usercredent`(`username`, `password`) VALUES ('"+username+"', '"+password+"')");
                        saveCredents.executeUpdate();
                    }

                    Stage login = (Stage) btnLogIn.getScene().getWindow(); //Getting current window
//                    Stage logIn = new Main().stage;
                    Stage logIn = new Stage();
                    Parent root = null;

                    //Moving to InitializerController Class to load all required main.resources
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/fxml/app.fxml"));
                        loader.setController(new appController());
                        root = loader.load();
                        Scene s = new Scene(root);
                        logIn.setTitle("Revenue Monitoring System");
                        logIn.getIcons().add(new Image("/Assets/kmalogo.png"));
                        logIn.setScene(s);
                        login.close();
                        logIn.show();
                    System.out.println(username+"\n"+password);
                } else {
                    warnlabel.setVisible(true);
                }
                if (!loggerCenter.isEmpty()){
                    userCenter = true;
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
    void ctrlLogInCheck(ActionEvent event) throws SQLException, IOException {
        userLogger();
    }

    @FXML
    void onEnterKey(KeyEvent event) throws SQLException, IOException {
        if(event.getCode().equals(KeyCode.ENTER)) {
            userLogger();
        }
    }
}
