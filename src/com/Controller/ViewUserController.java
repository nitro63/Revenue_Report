package com.Controller;
import com.Models.GetUser;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import com.revenue_report.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewUserController implements Initializable {

    @FXML
    private AnchorPane empPane;

    @FXML
    private AnchorPane paneUpdate;

    @FXML
    private Label lblUsernameUp;

    @FXML
    private JFXPasswordField txtOldPass;

    @FXML
    private JFXTextField txtOldPasswordShown;

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
    private JFXCheckBox chkPasswordMask;

    @FXML
    private Label lblLastName;

    @FXML
    private Label lblFirstName;

    @FXML
    private JFXCheckBox chkUpdateMask;

    @FXML
    private Label lblnice;

    private final Connection con;
    private PreparedStatement stmnt;
    private ResultSet rs;
    private final String username = LogInController.loggerUsername;
    String genPass;
    ObservableList<GetUser> user = FXCollections.observableArrayList();
    home_sideController app;
    GetUser User;

    public void setController(home_sideController app){
        this.app = app;
    }
    public ViewUserController(){
        this.con = DBConnection.getConn();
    }
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        paneUpdate.setVisible(false);
        try{
            setPas();
            setPaneView();
        }catch (SQLException e){
            new PromptDialogController("Operation failed", "Failed to get user details!");
        }
    }
     public void setPas() throws SQLException {
         stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `username`, `password`, `level` FROM `user`, `access_levels` WHERE `username` = '"+username+"' AND `access_ID` = `access_level` ");
         rs = stmnt.executeQuery();
         while (rs.next()){
             User = new GetUser(
                     rs.getString("username"),
                     rs.getString("first_name"),
                     rs.getString("last_name"),
                     rs.getString("email"),
                     rs.getString("level"),
                     rs.getString("password"));
             user.add(User);
         }
     }

    void setPaneView(){
        for (GetUser j: user){
            lblFirstName.setText(j.getFirstName());
            lblLastName.setText(j.getLastName());
            lblUsername.setText(j.getUsername());
            lblEmail.setText(j.getEmail());
            String pass = j.getPassword();
            int i = pass.length();
            String tyr = pass.substring(3, i), ty = pass.substring(0, 2);
            StringBuilder p = new StringBuilder(ty);
            i = tyr.length();
            for (int k = 0; k<=i; k++ ){
                p.append("*");
            }
            lblPassword.setText(p.toString());
            lblAccessLevel.setText(j.getLevel());
        }

//        lblFirstName.setText();
    }
    void setPaneUpdate(){
        for (GetUser i : user){
            txtLastName.setText(i.getLastName());
            txtFirstName.setText(i.getFirstName());
            txtEmail.setText(i.getEmail());
            lblUsernameUp.setText(i.getUsername());
            lblAccessLevelUpdate.setText(i.getLevel());
            genPass = i.getPassword();
        }
    }

    @FXML
    void chkUpdateMaskAction(ActionEvent event) throws SQLException, IOException {
        if (chkUpdateMask.isSelected())
        {
            /*Stage btn = (Stage) chkUpdateMask.getScene().getWindow();
            FXMLLoader loada = new FXMLLoader();
            loada.setLocation(getClass().getResource("/Views/fxml/passwordPrompt.fxml"));
            loada.setController(this);
            Parent root = loada.load();
            Scene s = new Scene(root);
            Stage stg = new Stage();
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.initOwner(btn);
            stg.initStyle(StageStyle.UTILITY);
            stg.setScene(s);
            stg.show();*/
            paneUpdate.setVisible(true);
            paneView.setVisible(false);
            setPaneUpdate();
        } else if (!chkUpdateMask.isSelected()){
            setPas();
            setPaneView();
            paneView.setVisible(true);
            paneUpdate.setVisible(false);
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
            txtOldPasswordShown.setText(txtOldPass.getText());
            txtOldPasswordShown.setVisible(true);
            txtPass.setVisible(false);
            txtPassConf.setVisible(false);
            txtOldPass.setVisible(false);
        } else {
            txtOldPass.setText(txtOldPasswordShown.getText());
            txtOldPass.setVisible(true);
            txtOldPasswordShown.setVisible(false);
            txtPass.setText(txtPasswordShown.getText());
            txtPass.setVisible(true);
            txtPasswordShown.setVisible(false);
            txtPassConf.setText(txtPasswordConfirmShown.getText());
            txtPassConf.setVisible(true);
            txtPasswordConfirmShown.setVisible(false);
        }
    }

    @FXML
    void onEnterKey(KeyEvent event) {

    }

    @FXML
    void updateUser(ActionEvent event) throws SQLException {
        PreparedStatement stmntp = null;
            boolean flag = true;

            if(txtEmail.getText().matches("\\s+") || txtEmail.getText().equals("") ||
                    txtFirstName.getText().matches("\\s+") || txtLastName.getText().matches("\\s+") ||
                    txtFirstName.getText().equals("") || txtLastName.getText().equals("")) {
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Fields can not be empty!", 2000);
            } else if(!txtPass.getText().equals(txtPassConf.getText()) || !txtPasswordShown.getText().equals
                    (txtPasswordConfirmShown.getText())) {
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Passwords did not match", 2000);
            }/*else if (chkUpdateMask.isSelected()){
                if (txtPasswordShown.getText().matches("\\s+") || txtPasswordShown.getText().equals("") || txtPasswordConfirmShown.getText().matches("\\s+") || txtPasswordConfirmShown.getText().equals("") || txtOldPasswordShown.getText().matches("\\s+") || txtOldPasswordShown.getText().equals(""))
                {
                    flag = false;
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Fields can not be empty!", 2000);
                }
            }else if (!chkUpdateMask.isSelected()){
                if (txtPass.getText().matches("\\s+") || txtPass.getText().equals("") || txtPassConf.getText().matches("\\s+") || txtPassConf.getText().equals("") || txtOldPass.getText().matches("\\s+") || txtOldPass.getText().equals("")
                ){
                    flag = false;
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Fields can not be empty!", 2000);
                }
            }*/

            if (flag){
                String  lname = txtLastName.getText().trim(),
                        fname = txtFirstName.getText().trim(), mail = txtEmail.getText().trim(), newpassword = "", oldpassword = "";
                if(chkPasswordMask.isSelected()) {
                    newpassword = txtPasswordShown.getText().trim();
                    oldpassword = txtOldPasswordShown.getText().trim();
                }
                else if (!chkPasswordMask.isSelected()){
                    newpassword = txtPass.getText().trim();
                    oldpassword = txtOldPass.getText().trim();
                }
                if ( oldpassword.isEmpty()){
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Please enter Old Password", 2000);
                }else if (newpassword.isEmpty() && oldpassword.equals(genPass)){
                    stmntp = con.prepareStatement("UPDATE `user` SET `last_name` = '"+lname+"', `first_name` = '"+fname+"', `email` = '"+mail+"', `password` = '"+oldpassword+"' WHERE `username` =  '"+username+"'");
                }else if (!newpassword.isEmpty() && oldpassword.equals(genPass)){
                    stmntp = con.prepareStatement("UPDATE `user` SET `last_name` = '"+lname+"', `first_name` = '"+fname+"', `email` = '"+mail+"', `password` = '"+newpassword+"' WHERE `username` =  '"+username+"'");
                }else{
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Old Password is Incorrect", 2000);
                }
                if (stmntp != null) {
                    stmntp.executeUpdate();
                    txtLastName.setText(null);
                    txtFirstName.setText(null);
                    txtEmail.setText(null);
                    lblUsernameUp.setText(null);
                    lblAccessLevelUpdate.setText(null);
                    if (chkPasswordMask.isSelected()){
                        txtPasswordShown.setText(null);
                        txtPasswordShown.setVisible(true);
                        txtPasswordConfirmShown.setText(null);
                        txtPasswordConfirmShown.setVisible(true);
                        txtOldPasswordShown.setText(null);
                        txtOldPasswordShown.setVisible(true);
                        txtPass.setVisible(true);
                        txtPassConf.setVisible(true);
                        txtOldPass.setVisible(true);
                        chkPasswordMask.setSelected(false);
                    }else {
                        txtOldPass.setText(null);
                        txtPass.setText(null);
                        txtPassConf.setText(null);
                    }
                    chkUpdateMask.setSelected(false);
                    paneUpdate.setVisible(false);
                    paneView.setVisible(true);
                    setPas();
                    setPaneView();
                }
            }

    }
}
