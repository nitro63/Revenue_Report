package Controller;
import Controller.Gets.GetUser;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import revenue_report.DBConnection;

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

    private final Connection con;
    private PreparedStatement stmnt;
    private ResultSet rs;
    private String username = LogInController.loggerUsername;
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

    @FXML
    void chkUpdateMaskAction(ActionEvent event) throws SQLException {
        if (chkUpdateMask.isSelected())
        {
            setPas();
            setPaneView();
            paneUpdate.setVisible(true);
            paneView.setVisible(false);
        } else {
            paneView.setVisible(true);
            paneUpdate.setVisible(false);
        }
    }

    @FXML
    void onEnterKey(KeyEvent event) {

    }

    @FXML
    void updateUser(ActionEvent event) {
            boolean flag = true;

            if(txtEmail.getText().matches("\\s+") || txtEmail.getText().equals("") ||
                    txtFirstName.getText().matches("\\s+") || txtLastName.getText().matches("\\s+") ||
                    txtFirstName.getText().equals("") || txtLastName.getText().equals("")) {
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
            }else if (chkUpdateMask.isSelected()){
                if (txtPasswordShown.getText().matches("\\s+") || txtPasswordShown.getText().equals("") || txtPasswordConfirmShown.getText().matches("\\s+") || txtPasswordConfirmShown.getText().equals("") )
                {
                    flag = false;
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Fields can not be empty1!", 2000);
                }
            }else if (!chkUpdateMask.isSelected()){
                if (txtPass.getText().matches("\\s+") || txtPass.getText().equals("") || txtPassConf.getText().matches("\\s+") || txtPassConf.getText().equals("")
                ){
                    flag = false;
                    JFXSnackbar s = new JFXSnackbar(empPane);
                    s.setStyle("-fx-text-fill: red");
                    s.show("Fields can not be empty2!", 2000);
                }
            }

    }
}
