package com.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.revenue_report.Main;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.revenue_report.DBConnection;

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
    @FXML
    private JFXButton btnConnectionConfiguration;
//    private final Connection con;
    private PreparedStatement stmnt;
    public static String loggerUsername, loggerAccessLevel, loggerCenter, loggerCenterName;
    public static String accessID;
    public static boolean hasCenter, admin, OverAllAdmin, Accountant, clerk, supervisor;
//    protected static



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hasCenter = false; admin = false; OverAllAdmin = false; Accountant = false; clerk = false;
                supervisor = false;
        /*Connection con = DBConnection.getConn();
        try {
            stmnt = con.prepareStatement("SELECT * FROM `usercredent` WHERE 1");
            ResultSet resultSet = stmnt.executeQuery();

            if (resultSet.next()) {
                txtUsername.setText(resultSet.getString("username")); //Getting Saved Username
                txtPassword.setText(resultSet.getString("password")); //Getting Saved Password
            }
        } catch (SQLException  e) {
            e.printStackTrace();
        }*/
        if (warnlabel.isVisible()){
        btnLogIn.setOnMouseClicked(e -> warnlabel.setVisible(false));}
        txtPasswordShown.setVisible(false);
        txtUsername.setOnMouseClicked(event -> lblWarnUsername.setVisible(false));
        txtPasswordShown.setOnMouseClicked(event -> lblWarnPassword.setVisible(false));
        txtPassword.setOnMouseClicked(event -> lblWarnPassword.setVisible(false));
    }

    @FXML
    void showConnectionConfiguration(ActionEvent event) {
        Stage current = (Stage) btnConnectionConfiguration.getScene().getWindow();
        FXMLLoader connectConfig = new FXMLLoader();
        connectConfig.setLocation(getClass().getResource("/com/Views/fxml/ConnectionConfiguration.fxml"));
        try {
            Stage stg = new Stage();
            Parent root = connectConfig.load();
            Scene s = new Scene(root);
            stg.setTitle("Connection Configuration");
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.initOwner(current);
            stg.initStyle(StageStyle.UTILITY);
            stg.setScene(s);
            stg.setResizable(false);
            stg.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        void userLogger() throws ClassNotFoundException, IOException {
        //Taking input from the username & password fields
        String username = txtUsername.getText();
        String password;

        //Getting input from the field in which
        //user inputted password.
        //Note: We have two password field.
        //One for visible password and another for hidden.
        if(chkPasswordMask.isSelected()) {/*
            txtPassword.setText(txtPasswordShown.getText());*/
            password = txtPasswordShown.getText();
        }
        else{/*
            if(!txtPasswordShown.getText().isEmpty()){
            txtPassword.setText(txtPasswordShown.getText());}*/
            password = txtPassword.getText();
        }

        //Checking if any fields were blank
        //If not then we're attempting to connect to DB

        if (username.matches("\\s+") || username.isEmpty()) {
            lblWarnUsername.setVisible(true);
        } else if(password.matches("\\s+") || password.isEmpty()) {
            lblWarnPassword.setVisible(true);
        } else {
            try {
                Connection con = DBConnection.getConn();
                stmnt = con.prepareStatement("SELECT `username`, `level`, `center`, `access_level` FROM `user`, `access_levels` WHERE `access_ID` = `access_level` AND BINARY `password` = '"+password+"' AND BINARY `username` = '"+username+"' ");

            ResultSet rs = stmnt.executeQuery();

                if (rs.next()) {
                    //Setting user credentials for further processing
                    loggerUsername = rs.getString("username");
                    loggerAccessLevel = rs.getString("level");
                    loggerCenter = rs.getString("center");
                    accessID = rs.getString("access_level");


                    //Checking for Save Credential CheckBox
                    //Upon true value saving new credents in DataBase
                    if(chkSaveCredentials.isSelected()) {
                        PreparedStatement delPrevCredents = con.prepareStatement("DELETE FROM `usercredent`");
                        delPrevCredents.executeUpdate();

                        PreparedStatement saveCredents = con.prepareStatement("INSERT INTO `usercredent`(`username`, `password`) VALUES ('"+username+"', '"+password+"')");
                        saveCredents.executeUpdate();
                    }
                    //Checking to see if we could exclude Overall Admin and Yah we have a way to do that
                    /*
                    stmnt = con.prepareStatement("SELECT `level`, `username`, `access_level` FROM `user`, `access_levels` WHERE `access_level` != 'Lvl_1' AND `access_ID` = `access_level`");
                    rs = stmnt.executeQuery();
                    while (rs.next()){
                        if (rs.getString("access_level").equals("Lvl_1")){
                            System.out.println("Shit it got selected");
                        }else {
                            System.out.println("less Privileged " + rs.getString("username"));
                        }
                    }*/
                    Stage login = (Stage) btnLogIn.getScene().getWindow(); //Getting current window
//                    Stage logIn = new Main().stage;
                    Stage logIn = new Stage();
                    Parent root = null;

                    //Moving to InitializerController Class to load all required main.resources
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/Views/fxml/Initializer.fxml"));
                        loader.setController(new InitializerController());
                        root = loader.load();
                        Scene s = new Scene(root);
                        logIn.setTitle("Revenue Monitoring System");
                        logIn.getIcons().add(new Image("/com/Assets/kmalogo.png"));
                        logIn.setScene(s);
                        if (!chkSaveCredentials.isSelected()){
                            txtPassword.setText("");
                            txtPasswordShown.setText("");
                            txtUsername.setText("");
                            password = "";
                            username = "";
                        }
                        login.close();
                        logIn.show();
                    hasCenter = loggerCenter != null;
                    switch (accessID){
                        case "Lvl_1":
                            OverAllAdmin = true;
                            break;
                        case "Lvl_2":
                            admin = true;
                            break;
                        case "Lvl_3":
                            Accountant = true;
                            break;
                        case "Lvl_5":
                            clerk = true;
                            break;
                        case "Lvl_4":
                            supervisor = true;
                            break;

                    }
                } else {
                    warnlabel.setVisible(true);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
    void ctrlLogInCheck(ActionEvent event) throws SQLException, IOException, ClassNotFoundException {
        userLogger();
    }

    @FXML
    void onEnterKey(KeyEvent event) throws SQLException, IOException, ClassNotFoundException {
        if(event.getCode().equals(KeyCode.ENTER)) {
            userLogger();
        }
    }
}
