/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author NiTrO
 */
public class UpdateEntriesController implements Initializable {

    @FXML
    private TableView<?> tblValueBookStocks;
    @FXML
    private TableColumn<?, ?> colStckMonth;
    @FXML
    private TableColumn<?, ?> colStckDATE;
    @FXML
    private TableColumn<?, ?> colStckTypeVB;
    @FXML
    private TableColumn<?, ?> colSerialFrom;
    @FXML
    private TableColumn<?, ?> colSerialTo;
    @FXML
    private TableColumn<?, ?> colStckQuantity;
    @FXML
    private TableColumn<?, ?> colStckAmtVal;
    @FXML
    private TableColumn<?, ?> colStckCumuAmt;
    @FXML
    private TableColumn<?, ?> colStckPurAmt;
    @FXML
    private TableView<?> tblTargetEntries;
    @FXML
    private TableColumn<?, ?> colTargCenter;
    @FXML
    private TableColumn<?, ?> colTargAmount;
    @FXML
    private TableColumn<?, ?> colTargYear;
    @FXML
    private TableView<?> tblCollectionEntries;
    @FXML
    private TableColumn<?, ?> colColCenter;
    @FXML
    private TableColumn<?, ?> colItemCode;
    @FXML
    private TableColumn<?, ?> colRevItem;
    @FXML
    private TableColumn<?, ?> colRevAmt;
    @FXML
    private TableColumn<?, ?> colRevDate;
    @FXML
    private TableColumn<?, ?> colRevWeek;
    @FXML
    private TableColumn<?, ?> colRevMonth;
    @FXML
    private TableColumn<?, ?> colRevQuarter;
    @FXML
    private TableColumn<?, ?> colRevYear;
    @FXML
    private TableView<?> tblPaymentEntries;
    @FXML
    private TableColumn<?, ?> colPayCenter;
    @FXML
    private TableColumn<?, ?> colPayGCR;
    @FXML
    private TableColumn<?, ?> colPayAmount;
    @FXML
    private TableColumn<?, ?> colPayDATE;
    @FXML
    private TableColumn<?, ?> colColPayMonth;
    @FXML
    private TableColumn<?, ?> colPayYear;
    @FXML
    private TableColumn<?, ?> colPayType;
    @FXML
    private Label lblTitle;
    @FXML
    private TableView<?> tblChequeDetails;
    @FXML
    private TableColumn<?, ?> colChqGCR;
    @FXML
    private TableColumn<?, ?> colPayChqDate;
    @FXML
    private TableColumn<?, ?> colChqDate;
    @FXML
    private TableColumn<?, ?> colChqNmb;
    @FXML
    private TableColumn<?, ?> colBank;
    @FXML
    private TableColumn<?, ?> colChqAmount;
    @FXML
    private AnchorPane paneValueBooks;
    @FXML
    private JFXDatePicker entDatePck;
    @FXML
    private Label lblStockDateWarn;
    @FXML
    private JFXComboBox<?> cmbTypeOfValueBook;
    @FXML
    private Label lblValueBookWarn;
    @FXML
    private JFXTextField txtUnitAmount;
    @FXML
    private Label lblUnitAmountWarn;
    @FXML
    private JFXTextField txtSerialFrom;
    @FXML
    private JFXTextField txtSerialTo;
    @FXML
    private Label lblFromWarn;
    @FXML
    private Label lblToWarn;
    @FXML
    private JFXButton btnReload;
    @FXML
    private JFXComboBox<?> cmbEntryType;
    @FXML
    private JFXComboBox<?> cmbEntryYear;
    @FXML
    private JFXComboBox<?> cmbEntryMonth;
    @FXML
    private JFXButton btnGetEntries;
    @FXML
    private AnchorPane paneTarget;
    @FXML
    private Spinner<?> spnTargYear;
    @FXML
    private Label lblYear;
    @FXML
    private Label lblTargYearWarn;
    @FXML
    private JFXTextField txtTargetAmount;
    @FXML
    private Label lblTargetWarn;
    @FXML
    private JFXButton btnClear;
    @FXML
    private JFXButton btnUpdateEntries;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private AnchorPane paneRevenueCollection;
    @FXML
    private JFXDatePicker entDatePckRevCol;
    @FXML
    private JFXComboBox<?> cmbRevenueItem;
    @FXML
    private JFXTextField txtRevenueAmount;
    @FXML
    private Label lblDatePckRevWarn;
    @FXML
    private Label lblRevItemWarn;
    @FXML
    private Label lblRevAmountWarn;
    @FXML
    private AnchorPane panePayment;
    @FXML
    private Spinner<?> spnPaymentYear;
    @FXML
    private Label lblYearPayment;
    @FXML
    private JFXTextField txtPaymentAmount;
    @FXML
    private JFXDatePicker entDatePckPayment;
    @FXML
    private JFXTextField txtPaymentGCR;
    @FXML
    private JFXComboBox<?> cmbCollectionMonth;
    @FXML
    private JFXComboBox<?> cmbPaymentType;
    @FXML
    private Label lblDatePayWarn;
    @FXML
    private Label lblPayGCRWarn;
    @FXML
    private Label lblPayAmtWarn;
    @FXML
    private Label lblColMonthWarn;
    @FXML
    private Label lblPayYearWarn;
    @FXML
    private Label lblPayTpeWarn;
    @FXML
    private AnchorPane paneCheque;
    @FXML
    private JFXComboBox<?> cmbGCR;
    @FXML
    private JFXDatePicker dtpckChequeDate;
    @FXML
    private JFXTextField txtChqNmb;
    @FXML
    private JFXTextField txtBankName;
    @FXML
    private JFXTextField txtChqAmount;
    @FXML
    private Label lblGCRChqWarn;
    @FXML
    private Label lblChequeDateWarn;
    @FXML
    private Label lblChqNmbWarn;
    @FXML
    private Label lblBankNameWarn;
    @FXML
    private Label lblChqAmtWarn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void onlyAmount(KeyEvent event) {
    }

    @FXML
    private void onlyNumbers(KeyEvent event) {
    }

    @FXML
    private void reloadAll(ActionEvent event) {
    }

    @FXML
    private void showEntries(ActionEvent event) {
    }

    @FXML
    private void clearEntries(ActionEvent event) {
    }

    @FXML
    private void updateEntries(ActionEvent event) {
    }

    @FXML
    private void deleteSelection(ActionEvent event) {
    }
    
}
