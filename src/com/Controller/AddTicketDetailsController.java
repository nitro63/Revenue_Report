package com.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddTicketDetailsController implements Initializable {

    @FXML
    private ImageView imgTicketImage;

    @FXML
    private Text textFileName;

    @FXML
    private JFXButton btnSelectImage;

    @FXML
    private JFXTextField txtBook;

    @FXML
    private JFXTextField txtValue;

    @FXML
    private JFXTextField txtPurchasePrice;

    @FXML
    private JFXTextArea txtAreaDescription;

    @FXML
    private JFXButton btnAddTicket;

    @FXML
    private JFXButton btnEditTicket;

    @FXML
    private JFXButton btnCancelEntries;

    @FXML
    private VBox ticketsBox;



    @Override
    public void initialize(URL location, ResourceBundle resource){
        
    }

    @FXML
    void AddTicket(ActionEvent event) throws IOException {
    }

    @FXML
    void CancelEntries(ActionEvent event) {

    }

    @FXML
    void EditTicket(ActionEvent event) {

    }

    @FXML
    void SelectImage(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        File file = fileChooser.showOpenDialog(null);

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imgTicketImage.setImage(image);
        } catch (IOException ex) {
            Logger.getLogger(AddTicketDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/**
 *
 FXMLLoader loader = new FXMLLoader();
 loader.setLocation(getClass().getResource("/com/Views/fxml/ticketDetails.fxml"));
 loader.setController(new TicketDetailsController());
 ticketsBox.getChildren().add(loader.load());
 */