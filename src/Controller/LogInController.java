package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class LogInController {
    private JFXTextField txtUsername;
    private JFXCheckBox chkSaveCredentials;
    private Label lblWarnPassword;
    private Label lblWarnUsername;
    private AnchorPane topPane;
    private JFXTextField txtPasswordShown;
    private JFXButton btnLogIn;
    private JFXCheckBox chkPasswordMask;
    private JFXPasswordField txtPassword;
    private AnchorPane paneLogIn;
}
