package com.Controller;
import com.Controller.Gets.GetUser;
import com.jfoenix.controls.*;
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
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import com.revenue_report.DBConnection;
import org.apache.commons.lang3.ObjectUtils;


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

    @FXML
    private JFXButton btnClear;

    private final Connection con;
    private PreparedStatement stmnt;
    private ResultSet rs;
    private String username;
    private boolean user;
    private InitializerController Init = new InitializerController();
    Map<String, String> centerID = new HashMap<>();
    Map<String, String> LevelID = new HashMap<>();
    GetUser newUser;


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
        for (GetUser s : InitializerController.userList){
            System.out.println("Username: "+ s.getUsername()+ " Email: "+ s.getEmail()+" UserCenter: "+ s.getUser_center());
        }
        System.out.println(InitializerController.userList);
        txtPasswordConfirmShown.setVisible(false);
        txtPasswordShown.setVisible(false);
        tblAddUser.setOnMouseClicked(e -> {
            if (tblAddUser.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1){
                cleanUp();
                setUser();
                addUser.setDisable(true);
            }
        });
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
        cboAccessLevel.getItems().clear();
        while (rs.next()){
            LevelID.put(rs.getString("level"), rs.getString("access_ID"));
            cboAccessLevel.getItems().add(rs.getString("level"));
        }
        if (LogInController.admin){
            cboAccessLevel.getItems().remove("Overall Administrator");
            cboAccessLevel.getItems().remove("Accountant");
        }
    }
    private void setCenters() throws SQLException {
        stmnt = con.prepareStatement("SELECT `CenterID`, `revenue_center` FROM `revenue_centers` WHERE 1");
        rs = stmnt.executeQuery();
        cmbRevenueCenter.getItems().add("None");
        while (rs.next()){
            centerID.put(rs.getString("revenue_center"), rs.getString("CenterID"));
            cmbRevenueCenter.getItems().add(rs.getString("revenue_center"));
        }
    }

    @FXML
    void clearEntries(ActionEvent event) {
        cleanUp();
        addUser.setDisable(false);
    }

    void cleanUp(){
        username = null;
        user = false;
        txtFname.clear();
        txtLname.clear();
        txtUsername.clear();
        txtEmail.clear();
        if (chkPasswordMask.isSelected()){
            txtPasswordShown.clear();
            txtPasswordConfirmShown.clear();
        }else {
            txtPassConf.clear();
            txtPass.clear();
        }
        cmbRevenueCenter.getSelectionModel().clearSelection();
        cboAccessLevel.getSelectionModel().clearSelection();
    }

    void setUser(){
        user = true;
        newUser = tblAddUser.getSelectionModel().getSelectedItem();
        username = newUser.getUsername();
        txtUsername.setText(newUser.getUsername());
        txtEmail.setText(newUser.getEmail());
        if (!chkPasswordMask.isSelected()){
        txtPass.setText(newUser.getPassword());
        txtPassConf.setText(newUser.getPassword());
        }else{
            txtPasswordShown.setText(newUser.getPassword());
            txtPasswordConfirmShown.setText(newUser.getPassword());
        }
        txtFname.setText(newUser.getFirstName());
        txtLname.setText(newUser.getLastName());
        cboAccessLevel.getSelectionModel().select(newUser.getLevel());
        if (!newUser.getUser_center().equals("NA")){
        cmbRevenueCenter.getSelectionModel().select(newUser.getUser_center());
        }
    }

    private void setAddUser() throws SQLException {
        username = LogInController.loggerUsername;
        if (LogInController.OverAllAdmin){
            tblAddUser.getItems().clear();
            colEmail.setCellValueFactory(d -> d.getValue().emailProperty());
        colFirstName.setCellValueFactory(d -> d.getValue().firstNameProperty());
        colLastName.setCellValueFactory(d -> d.getValue().lastNameProperty());
        colUserName.setCellValueFactory(d -> d.getValue().usernameProperty());
        colLevel.setCellValueFactory(d ->d.getValue().levelProperty());
        colRevenueCenter.setCellValueFactory(d -> d.getValue().user_centerProperty());
        colPass.setCellValueFactory(d -> d.getValue().passwordProperty());

            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `password`, `revenue_center` FROM `access_levels`, `revenue_centers`, `user` WHERE `center` IS NOT NULL AND `center` = `CenterID` AND `access_level` = `access_ID`");
        rs = stmnt.executeQuery();
        while (rs.next()) {
            newUser = new GetUser(
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
            newUser =new GetUser(
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
        }else if (LogInController.admin){
            tblAddUser.getItems().clear();
            colEmail.setCellValueFactory(d -> d.getValue().emailProperty());
            colFirstName.setCellValueFactory(d -> d.getValue().firstNameProperty());
            colLastName.setCellValueFactory(d -> d.getValue().lastNameProperty());
            colUserName.setCellValueFactory(d -> d.getValue().usernameProperty());
            colLevel.setCellValueFactory(d ->d.getValue().levelProperty());
            colRevenueCenter.setCellValueFactory(d -> d.getValue().user_centerProperty());
            colPass.setCellValueFactory(d -> d.getValue().passwordProperty());
                if (LogInController.hasCenter){
                    String Center = LogInController.loggerCenter;
            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `password`, `revenue_center` FROM `access_levels`, `revenue_centers`, `user` WHERE `access_level` != 'Lvl_1' AND `access_level` != 'Lvl_3' AND `center` = '"+Center+"' AND `center` = `CenterID` AND `access_level` = `access_ID`");
            rs = stmnt.executeQuery();
            while (rs.next()) {
                newUser = new GetUser(
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
                }else {
            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `password` FROM `access_levels`, `user` WHERE `access_level` != 'Lvl_1' AND `access_level` != 'Lvl_3' AND `center` IS NULL AND `access_level` = `access_ID`");
            rs = stmnt.executeQuery();
            while (rs.next()){
                newUser =new GetUser(
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
    void deleteUserDetails(ActionEvent event) throws SQLException {
        delUser();
    }

    void delUser() throws SQLException {
        if (user){
            stmnt = con.prepareStatement("DELETE FROM `user` WHERE `username` = '"+username+"'");
            stmnt.executeUpdate();
            cleanUp();
            setAddUser();
            addUser.setDisable(false);
        }else {
            JFXSnackbar s = new JFXSnackbar(empPane);
            s.setStyle("-fx-text-fill: red");
            s.show("Double-Click a row to edit 🙄!", 2000);
        }
    }

    @FXML
    void onEnterKey(KeyEvent event) {

    }

    @FXML
    void saveUser(ActionEvent event) throws SQLException {
        addUser(event);
    }

    void addUser(ActionEvent event) throws SQLException{
        if (!user){
            boolean flag = true;
            txtPasswordShown.setText(txtPass.getText());
            txtPasswordConfirmShown.setText(txtPassConf.getText());
            txtPass.setText(txtPasswordShown.getText());
            txtPassConf.setText(txtPasswordConfirmShown.getText());

            if(txtUsername.getText().equals("") || txtUsername.getText().matches("\\s+") ||
                    txtEmail.getText().matches("\\s+") || txtEmail.getText().equals("") ||
                    txtFname.getText().matches("\\s+") || txtLname.getText().matches("\\s+") ||
                    txtFname.getText().equals("") || txtLname.getText().equals("")
                    || cboAccessLevel.getSelectionModel().isEmpty()) {
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Fields can not be empty!3", 2000);
            } else if(!txtPass.getText().equals(txtPassConf.getText()) || !txtPasswordShown.getText().equals
                    (txtPasswordConfirmShown.getText())) {
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Passwords did not match", 2000);
            } else if (cmbRevenueCenter.getSelectionModel().isEmpty()){
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Select Center!", 2000);
            }else if (chkPasswordMask.isSelected()){
                if (txtPasswordShown.getText().matches("\\s+") || txtPasswordShown.getText().equals("") || txtPasswordConfirmShown.getText().matches("\\s+") || txtPasswordConfirmShown.getText().equals("") )
                {
                    flag = false;
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Fields can not be empty1!", 2000);
                }
            }else if (!chkPasswordMask.isSelected()){
                if (txtPass.getText().matches("\\s+") || txtPass.getText().equals("") || txtPassConf.getText().matches("\\s+") || txtPassConf.getText().equals("")
                ){
                    flag = false;
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Fields can not be empty2!", 2000);
                }
            }

            if (flag) {
                String levelID = LevelID.get(cboAccessLevel.getSelectionModel().getSelectedItem()), lname = txtLname.getText().trim(),
                        fname = txtFname.getText().trim(), mail = txtEmail.getText().trim(), uname = txtUsername.getText().trim(), password = "";
                if(chkPasswordMask.isSelected()) {
                    password = txtPasswordShown.getText().trim();
                }
                else if (!chkPasswordMask.isSelected()){
                    password = txtPass.getText().trim();
                }
                stmnt = con.prepareStatement("SELECT `username` FROM `user` WHERE `username` = '"+txtUsername.getText()+"'");
                rs = stmnt.executeQuery();
                String dupUser = "";
                while (rs.next()){
                    dupUser = rs.getString("username").toLowerCase(Locale.ROOT);
                }
                if (dupUser.equals(txtUsername.getText().toLowerCase(Locale.ROOT))){
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Username "+txtUsername.getText()+" already exists.", 2000);
                }else {
                    if (LogInController.hasCenter || !cmbRevenueCenter.getSelectionModel().getSelectedItem().equals("None")){
                        String center = centerID.get(cmbRevenueCenter.getSelectionModel().getSelectedItem());
                        stmnt = con.prepareStatement("INSERT INTO `user`(`last_name`, `first_name`, `email`, `password`, `access_level`, `username`, `center`) VALUES ('"+lname+"', '"+fname+"', '"+mail+"', '"+password+"', '"+levelID+"', '"+uname+"', '"+center+"')");
                    }else {
                        stmnt = con.prepareStatement("INSERT INTO `user`(`last_name`, `first_name`, `email`, `password`, `access_level`, `username`, `center`) VALUES ('"+lname+"', '"+fname+"', '"+mail+"', '"+password+"', '"+levelID+"', '"+uname+"', NULL)");
                    }
                    stmnt.executeUpdate();
                    cleanUp();
                    setAddUser();
                }
            }
        }else {
            event.consume();
        }
    }

    @FXML
    void updateUserDetails(ActionEvent event) throws SQLException {
        UpdateUser();
    }

    void UpdateUser() throws SQLException {
        if (user){
            boolean flag = true;

            if(txtUsername.getText().equals("") || txtUsername.getText().matches("\\s+") ||
                    txtEmail.getText().matches("\\s+") || txtEmail.getText().equals("") ||
                    txtFname.getText().matches("\\s+") || txtLname.getText().matches("\\s+") ||
                    txtFname.getText().equals("") || txtLname.getText().equals("")
                    || cboAccessLevel.getSelectionModel().isEmpty()) {
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Fields can not be empty!3", 2000);
            } else if(!txtPass.getText().equals(txtPassConf.getText()) || !txtPasswordShown.getText().equals
                    (txtPasswordConfirmShown.getText())) {
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Passwords did not match", 2000);
            } else if (LogInController.hasCenter && cmbRevenueCenter.getSelectionModel().getSelectedItem().equals("None")){
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Select Center!", 2000);
            }else if (chkPasswordMask.isSelected()){
                if (txtPasswordShown.getText().matches("\\s+") || txtPasswordShown.getText().equals("") || txtPasswordConfirmShown.getText().matches("\\s+") || txtPasswordConfirmShown.getText().equals("") )
            {
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Fields can not be empty1!", 2000);
            }
            }else if (!chkPasswordMask.isSelected()){
                if (txtPass.getText().matches("\\s+") || txtPass.getText().equals("") || txtPassConf.getText().matches("\\s+") || txtPassConf.getText().equals("")
            ){
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-text-fill: red");
                s.show("Fields can not be empty2!", 2000);
                }
            }
            if (flag){
                String levelID = LevelID.get(cboAccessLevel.getSelectionModel().getSelectedItem()), lname = txtLname.getText().trim(),
                        fname = txtFname.getText().trim(), mail = txtEmail.getText().trim(), uname = txtUsername.getText().trim(), password ;
                if(chkPasswordMask.isSelected()) {
                    password = txtPasswordShown.getText().trim();
                }
                else{
                    password = txtPass.getText().trim();
                }
                stmnt = con.prepareStatement("SELECT `username` FROM `user` WHERE `username` = '"+txtUsername.getText()+"'");
                rs = stmnt.executeQuery();
                String dupUser = "";
                while (rs.next()){
                    dupUser = rs.getString("username");
                }
                if (dupUser.equals(txtUsername.getText()) && !dupUser.equals(username)){
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Username "+txtUsername.getText().trim()+" already exists.", 2000);
                }else {
                    if (!cmbRevenueCenter.getSelectionModel().getSelectedItem().equals("None")){
                        String center = centerID.get(cmbRevenueCenter.getSelectionModel().getSelectedItem());
                        stmnt = con.prepareStatement("UPDATE `user` SET `last_name` = '"+lname+"', `first_name` = '"+fname+"', `email` = '"+mail+"', `password` = '"+password+"', `access_level` = '"+levelID+"', `username` = '"+uname+"', `center` = '"+center+"' WHERE `username` =  '"+username+"'");
                }else {
                        stmnt = con.prepareStatement("UPDATE `user` SET `last_name` = '"+lname+"', `first_name` = '"+fname+"', `email` = '"+mail+"', `password` = '"+password+"', `access_level` = '"+levelID+"', `username` = '"+uname+"', `center` = NULL WHERE `username` =  '"+username+"'");
                 }
                    stmnt.executeUpdate();
                    cleanUp();
                    setAddUser();
                    addUser.setDisable(false);
                 }
            }

        }else {
            JFXSnackbar s = new JFXSnackbar(empPane);
            s.setStyle("-fx-text-fill: red");
            s.show("Double-Click a row to edit 🙄!", 2000);
        }
    }
}

/*
    boolean flag = true;

        if(txtUser.getText().equals("") || txtEmail.getText().equals("") || txtPass.equals("") || txtPass.equals("")) {
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-background-color: red");
                s.show("Fields can not be empty!", 3000);
                } else if(!txtPass.getText().equals(txtPassConf.getText())) {
                flag = false;
                JFXSnackbar s = new JFXSnackbar(empPane);
                s.setStyle("-fx-background-color: red");
                s.show("Passwords did not match", 3000);
                }

                if (flag) {
                Connection con = DBConnection.getConnection();
                try {
                PreparedStatement ps = con.prepareStatement("INSERT INTO user VALUES(?, ?, ?, ?)");
                ps.setString(1, txtUser.getText());
                ps.setString(2, txtPass.getText());
                ps.setString(3, txtEmail.getText());
                ps.setString(4, cboAccessLevel.getValue());

                ps.executeUpdate();

                txtPass.getScene().getWindow().hide();
                new PromptDialogController("Operation Successful", "New Employee added! You can now log in with the given credentials.");
                } catch (SQLException e) {
                if(e.getErrorCode() == 1062) {
                new PromptDialogController("Operation failed", "This username is already taken. Try another!");
                }
                }
                }*/
