package com.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.revenue_report.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ConnectionConfiguration implements Initializable {

    @FXML
    private AnchorPane paneConfigurations;

    @FXML
    private JFXTextField txtUrl;

    @FXML
    private JFXTextField txtPort;

    @FXML
    private JFXTextField txtUser;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private HBox paneTimeZone;

    @FXML
    private HBox paneConnect;

    @FXML
    private HBox paneSSL;

    @FXML
    private JFXRadioButton radTimezone;

    @FXML
    private JFXComboBox<String> cmbTimezone;

    @FXML
    private JFXRadioButton radUseSSL;

    @FXML
    private JFXComboBox<String> cmbSSLMode;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnTestConnection;

    @FXML
    private Label lblConnectionStatus;
    private final ObservableList<String> _sslModes = FXCollections.observableArrayList("REQUIRED", "PREFERRED");
    static File jarPath=new File(DBConnection.class.getProtectionDomain().getCodeSource().getLocation().getPath());

    File directory = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
     String propertiesPath= directory.getParentFile().getPath();
    private final String file = /*propertiesPath+*/"./connection.properties";
    private final Properties prop = new Properties();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print(jarPath.getParentFile());
        cmbSSLMode.getItems().addAll(_sslModes);
        paneSSL.setVisible(false);
        paneTimeZone.setVisible(false);
        paneConnect.setLayoutY(234);
        paneConfigurations.setPrefHeight(330);
        lblConnectionStatus.setVisible(false);
        loadConfigurations();
        btnSave.setDisable(true);

    }

    private void loadConfigurations(){
        try {

            InputStream in = new FileInputStream(file);
            prop.load(in);
            txtUrl.setText(prop.getProperty("URL"));
            txtPort.setText(prop.getProperty("Port"));
            txtPassword.setText(prop.getProperty("Password"));
            txtUser.setText(prop.getProperty("User"));/*
            if (prop.getProperty("useTimeZone").equals("yes")){
                radTimezone.setSelected(true);
            }else{
                radTimezone.setSelected(false);
            }
            cmbTimezone.getSelectionModel().select(prop.getProperty("serverTimeZone"));*/
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void loadTimeZone(ActionEvent event) {

    }

    @FXML
    void saveConnectionConfiguration(ActionEvent event) {
        testConnection(null);
        if (!btnSave.isDisable()){
            prop.setProperty("Password", txtPassword.getText());
            prop.setProperty("User", txtUser.getText());
            prop.setProperty("URL", txtUrl.getText());
            prop.setProperty("Port", txtPort.getText());
            try {
                OutputStream out = new FileOutputStream(file);
                prop.store(out, null);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    @FXML
    void selectUseSSL(ActionEvent event) {

    }

    @FXML
    void testConnection(ActionEvent event) {
        String userT = prop.getProperty("useTimeZone"), servTZ = prop.getProperty("serverTimeZone"), autoRect = prop.getProperty("autoReconnect"),
                vSC = prop.getProperty("VerifyServerCertificate"), uSSl= prop.getProperty("UseSSL"), rSSL = prop.getProperty("RequireSSL"),
                pass = txtPassword.getText(), sslMode = prop.getProperty("sslMode"), user = txtUser.getText(), port = txtPort.getText(),
                url = txtUrl.getText();
        final String URL = "jdbc:mysql://"+url+":"+port+"/revenue_monitoring?useTimezone="+userT+"&serverTimezone="+servTZ+"&sslmode= "+sslMode+"&useSSL="+uSSl+"&verifyServerCertificate="+vSC+"&requireSSL="+rSSL;
        System.out.println(URL);
        try {
            DriverManager.getConnection(URL, user, pass);
            lblConnectionStatus.setVisible(true);
            lblConnectionStatus.setText("Connection Successful");
            btnSave.setDisable(false);
        } catch (SQLException e) {
            lblConnectionStatus.setVisible(true);
            lblConnectionStatus.setText("Connection Failed");
            btnSave.setDisable(true);
            e.printStackTrace();
        }
    }
}
