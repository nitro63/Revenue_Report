package Controller;

import Controller.Gets.GetUser;
import com.jfoenix.controls.JFXProgressBar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import revenue_report.DBConnection;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InitializerController implements Initializable {

    private static final int THREAD_SLEEP_INTERVAL = 200;
    @FXML
    private JFXProgressBar progressIndicator;
    Stage currentStage, appStage;
    @FXML
    private Label taskName;
    private PreparedStatement stmnt;
    private ResultSet rs;
    public String sessionUser = LogInController.loggerUsername;
    public static String userCenter, userCategory;
    Stage base = new Stage();
    GetUser getUser;
    ObservableList<GetUser> usersList = FXCollections.observableArrayList();
    ObservableList<GetUser> userList = FXCollections.observableArrayList();
                                                   //The field is initiated from LogInController Class
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        LoadRecords initializerTask = new LoadRecords();
        progressIndicator.progressProperty().unbind();
       // progressIndicator.progressProperty().bind(t.progressProperty());
        taskName.textProperty().unbind();
        taskName.textProperty().bind(initializerTask.messageProperty());

        new Thread(initializerTask).start();

        //Loading Main Application upon initializer task's succession
        initializerTask.setOnSucceeded(e -> {
            //Closing Current Stage
            currentStage = (Stage) taskName.getScene().getWindow();
            currentStage.close();
            try {
                loadRecords();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            loadApplication();
        });
    }

    public ObservableList<GetUser> getUsersList(){
        return this.usersList;
    }
    private void loadApplication() {
        //Creating a new stage for main application
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/fxml/app.fxml"));
            loader.setController(new appController());
            appController app = loader.getController();
            root = loader.load();
            Scene scene = new Scene(root);/*
            String css = this.getClass().getResource("/Views/Stylesheets/app.css").toExternalForm(); // Getting stylesheet
            scene.getStylesheets().add(css);*/
            base.setTitle("Revenue Monitoring System");
            base.getIcons().add(new Image("/Assets/kmalogo.png"));
            base.setScene(scene);
            base.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.appStage = base;
    }

    public void loadRecords() throws SQLException {
        Connection con = DBConnection.getConn();
        InitializerController init = new InitializerController();
        if (LogInController.hasCenter){
            stmnt = con.prepareStatement("SELECT `revenue_category`, `revenue_center` FROM `user`, `revenue_centers` WHERE `username` = '"+LogInController.loggerUsername+"' AND `center` = `CenterID`");
            rs = stmnt.executeQuery();
            while (rs.next()){
               userCenter = rs.getString("revenue_center");
                userCategory = rs.getString("revenue_category");
            }
        }
        if (LogInController.OverAllAdmin){
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
                usersList.add(newUser);
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
                usersList.add(newUser);
            }
        }
        if (LogInController.admin){
            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `revenue_center` FROM `access_levels`, `revenue_centers`, `user` WHERE `center` IS NOT NULL AND `center` = `CenterID` AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID`");
            rs = stmnt.executeQuery();
            while (rs.next()){
                GetUser newUser =new GetUser(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("level"),
                        rs.getString("revenue_center")
                );
                usersList.add(newUser);
            }
            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username` FROM `access_levels`, `user` WHERE `center` IS NULL AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID`");
            rs = stmnt.executeQuery();
            while (rs.next()){
                GetUser newUser =new GetUser(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("level")
                );
                usersList.add(newUser);
            }
        }
        if (LogInController.Accountant || LogInController.clerk){
            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `revenue_center` FROM `access_levels`, `revenue_centers`, `user` WHERE `center` IS NOT NULL AND `center` = `CenterID` AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID` AND `username` = '"+LogInController.loggerUsername+"'");
            rs = stmnt.executeQuery();
            while (rs.next()){
                GetUser newUser =new GetUser(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("level"),
                        rs.getString("revenue_center")
                );
                userList.add(newUser);
            }
            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username` FROM `access_levels`, `user` WHERE `center` IS NULL AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID` AND `username` = '"+LogInController.loggerUsername+"'");
            rs = stmnt.executeQuery();
            while (rs.next()){
                GetUser newUser =new GetUser(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("level")
                );
                userList.add(newUser);
            }
        }
        if (LogInController.supervisor){
            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `revenue_center` FROM `access_levels`, `revenue_centers`, `user` WHERE `center` IS NOT NULL AND `center` = `CenterID` AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID` AND `access_level` = 'Lvl_4' OR `access_level` = 'Lvl_5'");
            rs = stmnt.executeQuery();
            while (rs.next()){
                GetUser newUser =new GetUser(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("level"),
                        rs.getString("revenue_center")
                );
                usersList.add(newUser);
            }
            stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username` FROM `access_levels`, `user` WHERE `center` IS NULL AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID` AND `access_level` = 'Lvl_4' OR `access_level` = 'Lvl_5'");
            rs = stmnt.executeQuery();
            while (rs.next()){
                GetUser newUser =new GetUser(
                        rs.getString("username"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("level")
                );
                usersList.add(newUser);
            }
        }
    }

    class LoadRecords extends Task{
        @Override
        protected Object call() throws Exception{
            Connection con = DBConnection.getConn();
            ObservableList<GetUser> usersList = FXCollections.observableArrayList();
            ObservableList<GetUser> userList = FXCollections.observableArrayList();
            if (LogInController.OverAllAdmin){
                this.updateMessage("Loading users details....");
                Thread.sleep(THREAD_SLEEP_INTERVAL);
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
                    usersList.add(newUser);
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
                    usersList.add(newUser);
                }
            }
            if (LogInController.admin){
                this.updateMessage("Loading users details....");
                Thread.sleep(THREAD_SLEEP_INTERVAL);
                stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `revenue_center` FROM `access_levels`, `revenue_centers`, `user` WHERE `center` IS NOT NULL AND `center` = `CenterID` AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID`");
                rs = stmnt.executeQuery();
                while (rs.next()){
                    GetUser newUser =new GetUser(
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("level"),
                            rs.getString("revenue_center")
                    );
                    usersList.add(newUser);
                }
                stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username` FROM `access_levels`, `user` WHERE `center` IS NULL AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID`");
                rs = stmnt.executeQuery();
                while (rs.next()){
                    GetUser newUser =new GetUser(
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("level")
                    );
                    usersList.add(newUser);
                }
            }
            if (LogInController.Accountant || LogInController.clerk){
                this.updateMessage("Loading user details....");
                Thread.sleep(THREAD_SLEEP_INTERVAL);
                stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `revenue_center` FROM `access_levels`, `revenue_centers`, `user` WHERE `center` IS NOT NULL AND `center` = `CenterID` AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID` AND `username` = '"+LogInController.loggerUsername+"'");
                rs = stmnt.executeQuery();
                while (rs.next()){
                    GetUser newUser =new GetUser(
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("level"),
                            rs.getString("revenue_center")
                    );
                    userList.add(newUser);
                }
                stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username` FROM `access_levels`, `user` WHERE `center` IS NULL AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID` AND `username` = '"+LogInController.loggerUsername+"'");
                rs = stmnt.executeQuery();
                while (rs.next()){
                    GetUser newUser =new GetUser(
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("level")
                    );
                    userList.add(newUser);
                }
            }
            if (LogInController.supervisor){
                this.updateMessage("Loading users details....");
                Thread.sleep(THREAD_SLEEP_INTERVAL);
                stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username`, `revenue_center` FROM `access_levels`, `revenue_centers`, `user` WHERE `center` IS NOT NULL AND `center` = `CenterID` AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID` AND `access_level` = 'Lvl_4' OR `access_level` = 'Lvl_5'");
                rs = stmnt.executeQuery();
                while (rs.next()){
                    GetUser newUser =new GetUser(
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("level"),
                            rs.getString("revenue_center")
                    );
                    usersList.add(newUser);
                }
                stmnt = con.prepareStatement("SELECT `first_name`, `last_name`, `email`, `level`, `username` FROM `access_levels`, `user` WHERE `center` IS NULL AND `access_level` != 'Lvl_1' AND `access_level` = `access_ID` AND `access_level` = 'Lvl_4' OR `access_level` = 'Lvl_5'");
                rs = stmnt.executeQuery();
                while (rs.next()){
                    GetUser newUser =new GetUser(
                            rs.getString("username"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("level")
                    );
                    usersList.add(newUser);
                }
            }
            return null;
        }
    }


}
