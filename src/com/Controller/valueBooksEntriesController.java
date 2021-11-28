package com.Controller;

import com.Controller.Gets.GetFunctions;
import com.Controller.Gets.GetRevCenter;
import com.Controller.Gets.GetValueBooksEntries;
import com.Enums.Months;
import com.jfoenix.controls.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.StringUtils;
import com.revenue_report.DBConnection;

public class valueBooksEntriesController implements Initializable {

    @FXML
    private AnchorPane CollectionEntPane;

    @FXML
    private JFXButton btnClose;

    @FXML
    private DatePicker entDatePck;

    @FXML
    private JFXComboBox<String> cmbTypeOfValueBook;

    @FXML
    private JFXTextField txtUnitAmount;

    @FXML
    private JFXTextField txtSerialFrom;

    @FXML
    private JFXTextField txtSerialTo;

    @FXML
    private JFXButton btnClear;

    @FXML
    private JFXButton btnEnter;

    @FXML
    private TableView<GetValueBooksEntries> tblValueBookStocks;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colYear;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colMonth;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colQuarter;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colID;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colDATE;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colTypeVB;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colSerialFrom;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colSerialTo;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colQuantity;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colAmtVal;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colCumuAmount;

    @FXML
    private TableColumn<GetValueBooksEntries, String> colPurchAmount;
    @FXML
    private TableColumn<GetValueBooksEntries, String> colRemarks;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXButton btnClearEntr;

    @FXML
    private JFXButton btnSaveEntries;
    @FXML
    private Text lblDeleteWarn;
    @FXML
    private Text lblEdit;
    @FXML
    private Text lblDup;
    @FXML
    private Label lblDateWarn;
    @FXML
    private Label lblToWarn;
    @FXML
    private Label lblFromWarn;
    @FXML
    private Label lblAmountWarn;
    @FXML
    private Label lblValueBookWarn;

    @FXML
    private JFXButton btnClearUpdate;

    @FXML
    private JFXButton btnUpdateEntries;

    @FXML
    private JFXButton btnDeleteUpdate;

    @FXML
    private JFXCheckBox chkUpdate;

    @FXML
    private ComboBox<String> cmbUpdateYear;

    @FXML
    private ComboBox<Months> cmbUpdateMonth;

    @FXML
    private JFXButton btnFetchUpdate;

    @FXML
    private Label lblControlWarn;

    private entries_sideController app;
    private GetValueBooksEntries addEntries, getData;
    private final Bank_DetailsEntriesController bnkDtls = new Bank_DetailsEntriesController();
    private final GetFunctions getDates = new GetFunctions();
    private final GetRevCenter GetCenter;
    private Payment_EntriesController pay ;
    boolean Condition = true;
    private final Connection con;
    float amount =0, cumuamount, puramount;
    private final String regex = "(?<=[\\d])(,)(?=[\\d])";
    private final Pattern p = Pattern.compile(regex);
    private Matcher m;
    private PreparedStatement stmnt;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private ResultSet rs;
    private Map<String, ArrayList<Range<Integer>>> regValSerial = new HashMap<>();
    private String revCent;
    private String revCentID;
    private String typeOfValBk;
    private String Quarter;
    private String entriesID, setLastSerial, setFirstSerial;
   private String remarks = "Initial Entry";
   private final Map<String, Float> priceBook = new HashMap<>();
    private final Map<String, String> valueBookID = new HashMap<>();


    public valueBooksEntriesController(GetRevCenter GetCenter) throws SQLException, ClassNotFoundException {
        this.con = DBConnection.getConn();
        this.GetCenter = GetCenter;
    }

    public void setappController(entries_sideController app){
        this.app = app;
    }


    public entries_sideController getentries_sideController(){
        return app;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PreparedStatement stmnt = null;
        revCent = GetCenter.getRevCenter();
        revCentID = GetCenter.getCenterID();
        getDates.datePicker(entDatePck);
        try {
            stmnt = con.prepareStatement("SELECT * FROM `value_books_details`");
            ResultSet res = stmnt.executeQuery();
            cmbTypeOfValueBook.getItems().clear();
            while (res.next()){
                cmbTypeOfValueBook.getItems().add(res.getString("value_books"));
                valueBookID.put(res.getString("value_books"), res.getString("value_book_ID"));
                priceBook.put(res.getString("value_book_ID"), res.getFloat("price"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            GetRevenueYears();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        chkUpdate.setVisible(LogInController.Accountant || LogInController.OverAllAdmin);
      chkUpdate.setOnMouseClicked(event -> {
          try {
              GetRevenueYears();
          } catch (SQLException throwables) {
              throwables.printStackTrace();
          }
          if (chkUpdate.isSelected()){
              cmbUpdateYear.setVisible(true);
              cmbUpdateMonth.setVisible(true);
              btnFetchUpdate.setVisible(true);
              btnClearUpdate.setVisible(true);
              btnDeleteUpdate.setVisible(true);
              btnUpdateEntries.setVisible(true);
              colID.setVisible(true);
              btnEnter.setVisible(false);
              btnClear.setVisible(false);
              btnClearEntr.setVisible(false);
              btnSaveEntries.setVisible(false);
              btnDelete.setVisible(false);
              tblValueBookStocks.getItems().clear();
              tblValueBookStocks.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
              clear();

          }else {
              cmbUpdateYear.setVisible(false);
              cmbUpdateMonth.setVisible(false);
              btnFetchUpdate.setVisible(false);
              btnClearUpdate.setVisible(false);
              btnDeleteUpdate.setVisible(false);
              btnUpdateEntries.setVisible(false);
              colID.setVisible(false);
              btnEnter.setVisible(true);
              btnClear.setVisible(true);
              btnClearEntr.setVisible(true);
              btnSaveEntries.setVisible(true);
              btnDelete.setVisible(true);
              tblValueBookStocks.getItems().clear();
              tblValueBookStocks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
              clear();
          }
      });
        tblValueBookStocks.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblValueBookStocks.setOnMouseClicked(e -> {
            if (!chkUpdate.isSelected()){
                lblDeleteWarn.setVisible(false);lblDup.setVisible(false);lblEdit.setVisible(false);
            }else {
                if (tblValueBookStocks.getSelectionModel().getSelectedItem() != null && e.getClickCount() > 1){
                    setEntries();
                    if (lblControlWarn.isVisible()){
                        lblControlWarn.setVisible(false);
                    }
                }
            }
        });
        entDatePck.setOnMouseClicked(event -> {
            lblDateWarn.setVisible(false);
        });
        cmbTypeOfValueBook.setOnMouseClicked(event -> {
            lblValueBookWarn.setVisible(false);
        });
        txtUnitAmount.setOnMouseClicked(event -> {
            lblAmountWarn.setVisible(false);
        });
        txtSerialFrom.setOnMouseClicked(event -> {
            lblFromWarn.setVisible(false);
        });
        txtSerialTo.setOnMouseClicked(event -> {
            lblToWarn.setVisible(false);
        });
    }

  private void GetRevenueYears() throws SQLException {
      stmnt = con.prepareStatement("SELECT YEAR(date) AS `year` FROM `value_books_stock_record` WHERE `value_stock_revCenter` = '"+revCentID+"' GROUP BY `year`");
      rs = stmnt.executeQuery();
      cmbUpdateYear.getItems().clear();
      while (rs.next()){
          cmbUpdateYear.getItems().add(rs.getString("year"));
      }
  }

    @FXML
    void selectedYear(ActionEvent event) throws SQLException {
      String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
        stmnt = con.prepareStatement("SELECT MONTH(date) AS `month` FROM `value_books_stock_record` WHERE `value_stock_revCenter` = '"+revCentID+"' AND YEAR(date) ='"+year+"' GROUP BY `month`");
        rs = stmnt.executeQuery();
        cmbUpdateMonth.getItems().clear();
        while (rs.next()){
            cmbUpdateMonth.getItems().add(Months.get(rs.getInt("month")));
        }
    }

    @FXML
    void fetchEntries(ActionEvent event) throws SQLException {
        if (!cmbUpdateYear.getSelectionModel().isEmpty()) {
            String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
            Months month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
            if (!cmbUpdateMonth.getSelectionModel().isEmpty()) {
                stmnt = con.prepareStatement("SELECT `value_record_ID`, `date`, `value_books`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks` FROM `value_books_stock_record`, `value_books_details` WHERE `value_stock_revCenter` = '" + revCentID + "' AND `value_book_ID` = `value_book` AND YEAR(date) ='" + year + "' AND `month` = '" + month + "' ");
            } else {
                stmnt = con.prepareStatement("SELECT `value_record_ID`, `date`, `value_books`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks` FROM `value_books_stock_record`, `value_books_details` WHERE `value_stock_revCenter` = '" + revCentID + "' AND `value_book_ID` = `value_book` AND YEAR(date) ='" + year + "'");
            }
            rs = stmnt.executeQuery();
            tblValueBookStocks.getItems().clear();
            colID.setCellValueFactory(data -> data.getValue().weekProperty());
            colDATE.setCellValueFactory(data -> data.getValue().dateProperty());
            colTypeVB.setCellValueFactory(data -> data.getValue().valueBookProperty());
            colSerialFrom.setCellValueFactory(data -> data.getValue().firstSerialProperty());
            colSerialTo.setCellValueFactory(data -> data.getValue().lastSerialProperty());
            colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty());
            colAmtVal.setCellValueFactory(data -> data.getValue().valAmountProperty());
            colCumuAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
            colPurchAmount.setCellValueFactory(data -> data.getValue().purAmountProperty());
            colRemarks.setCellValueFactory(d -> d.getValue().remarksProperty());
            float amount = 0; String cumuAmounts;
            while (rs.next()) {
                amount += rs.getFloat("amount");
                cumuAmounts = getDates.getAmount(Float.toString(amount));
                addEntries = new GetValueBooksEntries(rs.getString("value_record_ID"), rs.getString("date"), rs.getString("value_books"), rs.getString("first_serial"), rs.getString("last_serial"),
                        rs.getString("quantity"), getDates.getAmount(rs.getString("amount")), cumuAmounts, getDates.getAmount(rs.getString("purchase_amount")), rs.getString("remarks"));
                tblValueBookStocks.getItems().add(addEntries);

            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Please select Year");
            alert.showAndWait();
        }
    }

    private void setEntries() {
        GetValueBooksEntries entries = tblValueBookStocks.getSelectionModel().getSelectedItem();
        entriesID = entries.getWeek();
        setLastSerial = entries.getLastSerial();
        setFirstSerial = entries.getFirstSerial();
        entDatePck.setValue(LocalDate.parse(entries.getDate(), format));
        m = p.matcher(entries.getPurAmount());
        String amount =  m.replaceAll("");
        float price = Float.parseFloat(amount);
        int quantity = Integer.parseInt(entries.getQuantity());
        float unitAmount = price / quantity;
        txtUnitAmount.setText(Float.toString(unitAmount));
        cmbTypeOfValueBook.getSelectionModel().select(entries.getValueBook());
        txtSerialFrom.setText(entries.getFirstSerial());
        txtSerialTo.setText(entries.getLastSerial());
    }

    @FXML
    void updateEntries(ActionEvent event) throws SQLException {
        if (entriesID != null){
            String  lastSerial = txtSerialTo.getText(),
                    firstSerial = txtSerialFrom.getText(),
                    Quantity, typeOfValBk = cmbTypeOfValueBook.getSelectionModel().getSelectedItem(),
                    purAmount, valAmount,
                    Month = getDates.getMonth(entDatePck.getValue()),
                    Date = getDates.getSqlDate(entDatePck.getValue());
            float amount = 0, cumuamount = 0, puramount = 0;
            int serialChecker = ((Integer.parseInt(lastSerial) - Integer.parseInt(firstSerial)) + 1)/*,
                    year = Integer.parseInt(getDates.getYear(entDatePck.getValue())),
                    week = Integer.parseInt(getDates.getWeek(entDatePck.getValue())),
                    quarter = Integer.parseInt(getDates.getQuarter(entDatePck.getValue()))*/;
            int quantity = ((serialChecker) / 100);
            if ((serialChecker) < 100 || serialChecker % 100 != 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Please check Serials");
                alert.showAndWait();
            } else {
                Quantity = Integer.toString(quantity);
                for (Map.Entry<String, Float> calValAmount : priceBook.entrySet()){
                    if (calValAmount.getKey().equals(typeOfValBk)){
                        amount = quantity * calValAmount.getValue() * 100;
                    }
                }
                puramount = (Float.parseFloat(txtUnitAmount.getText()) * quantity);
                purAmount = getDates.getAmount(Float.toString(puramount));
                valAmount = getDates.getAmount(Float.toString((amount)));
                PreparedStatement stmnt = con.prepareStatement("SELECT * FROM `value_books_stock_record`, `revenue_centers` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `revenue_centers`.`revenue_center` = '"+revCent+"' AND `value_book` = '"+valueBookID.get(typeOfValBk)+"' AND `first_serial` !='"+setFirstSerial+"' AND `last_serial` !='"+setLastSerial+"'");
                ResultSet res = stmnt.executeQuery();
                ArrayList<String> duplicate = new ArrayList<>();
                while (res.next()){
                    if(res.getInt("first_serial")<=Integer.parseInt(getData.getFirstSerial()) && Integer.parseInt(getData.getFirstSerial())<= res.getInt("last_serial")&& !duplicate.contains(getData.getFirstSerial())){
                        duplicate.add(getData.getFirstSerial());
                    }
                    if(res.getInt("first_serial")<=Integer.parseInt(getData.getLastSerial()) && Integer.parseInt(getData.getLastSerial())<= res.getInt("last_serial") && !duplicate.contains(getData.getLastSerial())){
                        duplicate.add(getData.getLastSerial());
                    }
                }
                if (duplicate.contains(getData.getFirstSerial()) && duplicate.contains(getData.getLastSerial())){
                    lblDup.setText("Range of Serials for "+'"'+typeOfValBk+'"'+"" +
                            " already exist. Please select row of duplicate data to Edit or Delete." );
                    lblDup.setVisible(true);
                }else
                if (duplicate.contains(getData.getLastSerial())){
                    lblDup.setText("Last Serial"+'"'+lastSerial+'"'+" for "+'"'+typeOfValBk+'"'+"" +
                            " already exist. Please select row of duplicate data to Edit or Delete." );
                    lblDup.setVisible(true);
                }else
                if (duplicate.contains(getData.getFirstSerial())){
                    lblDup.setText("First Serial"+'"'+firstSerial+'"'+" for "+'"'+typeOfValBk+'"'+"" +
                            " already exist. Please select row of duplicate data to Edit or Delete." );
                    lblDup.setVisible(true);
                }else
                if ("0.00".equals(purAmount)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Please Amount cannot be '0'");
                    alert.showAndWait();
                    txtUnitAmount.clear();
                } else {
                    stmnt = con.prepareStatement("UPDATE `value_books_stock_record` SET `date` = '"+Date+"'," +
                            " `value_book` = '"+valueBookID.get(typeOfValBk)+"', `first_serial` = '"+firstSerial+"', `last_serial` = '"+
                            lastSerial+"', `quantity` = '"+Quantity+"', `amount` = '"+amount+"', `purchase_amount` = '"+
                            puramount+"', `remarks` = 'Updated' WHERE `value_record_ID` = '"+entriesID+"'");
                    stmnt.executeUpdate();
                    loadRevenueCollectionTable();
                    clear();
                }
            }
        }else{
            lblControlWarn.setVisible(true);
        }
    }


    void clear(){
        entriesID = null;
        entDatePck.setValue(null);
        txtUnitAmount.setText(null);
        txtSerialFrom.setText(null);
        txtSerialTo.setText(null);
        cmbTypeOfValueBook.getSelectionModel().clearSelection();
    }


    public void showClose(ActionEvent actionEvent) {
        getentries_sideController().getappController().getCenterPane().getChildren().clear();
    }


    private void loadRevenueCollectionTable() throws SQLException {
        String year = cmbUpdateYear.getSelectionModel().getSelectedItem();
        Months month = cmbUpdateMonth.getSelectionModel().getSelectedItem();
        if (!cmbUpdateMonth.getSelectionModel().isEmpty()) {
            stmnt = con.prepareStatement("SELECT `value_record_ID`, `date`, `value_books`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks` FROM `value_books_stock_record`, `value_books_details` WHERE `value_stock_revCenter` = '" + revCentID + "' AND `value_book_ID` = `value_book` AND YEAR(date) ='" + year + "' AND MONTH(date) = '" + month.getValue() + "' ");
        } else {
            stmnt = con.prepareStatement("SELECT `value_record_ID`, `date`, `value_books`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`, `remarks` FROM `value_books_stock_record`, `value_books_details` WHERE `value_stock_revCenter` = '" + revCentID + "' AND `value_book_ID` = `value_book` AND YEAR(date) ='" + year + "'");
        }
        rs = stmnt.executeQuery();
        tblValueBookStocks.getItems().clear();
        colID.setCellValueFactory(data -> data.getValue().weekProperty());
        colDATE.setCellValueFactory(data -> data.getValue().dateProperty());
        colTypeVB.setCellValueFactory(data -> data.getValue().valueBookProperty());
        colSerialFrom.setCellValueFactory(data -> data.getValue().firstSerialProperty());
        colSerialTo.setCellValueFactory(data -> data.getValue().lastSerialProperty());
        colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty());
        colAmtVal.setCellValueFactory(data -> data.getValue().valAmountProperty());
        colCumuAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
        colPurchAmount.setCellValueFactory(data -> data.getValue().purAmountProperty());
        colRemarks.setCellValueFactory(d -> d.getValue().remarksProperty());
        float amount = 0; String cumuAmounts;
        while (rs.next()) {
            amount += rs.getFloat("amount");
            cumuAmounts = getDates.getAmount(Float.toString(amount));
            addEntries = new GetValueBooksEntries(rs.getString("value_record_ID"), rs.getString("date"), rs.getString("value_books"), rs.getString("first_serial"), rs.getString("last_serial"),
                    rs.getString("quantity"), getDates.getAmount(rs.getString("amount")), cumuAmounts, getDates.getAmount(rs.getString("purchase_amount")), rs.getString("remarks"));
            tblValueBookStocks.getItems().add(addEntries);

        }
    }


    public void saveEntries(ActionEvent actionEvent) throws SQLException {
        regValSerial.put("", new ArrayList<>());
        LocalDate date = entDatePck.getValue();
        typeOfValBk = cmbTypeOfValueBook.getSelectionModel().getSelectedItem();
        String firstSerial = txtSerialFrom.getText();
        String lastSerial = txtSerialTo.getText();
        if(entDatePck.getValue() == null){
            lblDateWarn.setVisible(true);
            Condition = false;
        }else{
            Condition =true;/*
            Quarter = getDates.getQuarter(date);
            Month = getDates.getMonth(date);
            Week = getDates.getWeek(date);*/
            String date1 = getDates.getDate(date);
//            Year = getDates.getYear(date);
            while (Condition){
                    if(regValSerial.containsKey(typeOfValBk) && regValSerial.get(typeOfValBk).contains(firstSerial)){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Serials have already been entered");
                        alert.showAndWait();
                        Condition =false;
                    }else if (regValSerial.containsKey(typeOfValBk) && regValSerial.get(typeOfValBk).contains
                            (lastSerial)){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Serials have already been entered");
                        alert.showAndWait();
                        Condition =false;
                    }
                else if(cmbTypeOfValueBook.getSelectionModel().isEmpty()){
                    lblValueBookWarn.setVisible(true);
                    Condition = false;
                }
                else if(txtUnitAmount.getText().isEmpty()){
                    lblAmountWarn.setVisible(true);
                    Condition = false;
                }
                else if(txtSerialFrom.getText().isEmpty()){
                    lblFromWarn.setVisible(true);
                    Condition = false;
                }
                else if(txtSerialTo.getText().isEmpty()){
                    lblToWarn.setVisible(true);
                    Condition = false;
                }
                else if(StringUtils.countMatches(txtUnitAmount.getText(), ".") > 1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Please Enter Amount");
                    alert.setContentText("Please check the number of '.' in the amount");
                    alert.showAndWait();
                    txtUnitAmount.clear();
                    Condition = false;
                }
                else {
                    int serialChecker = (Integer.parseInt(lastSerial) - Integer.parseInt(firstSerial))+1 ;
                    int quantity = ((serialChecker)/ 100);
                    if(/*(serialChecker)<100 ||*/ serialChecker % 100 !=0){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Warning Dialog");
                        alert.setHeaderText("Please check Serials");
                        alert.showAndWait();
                        Condition =false;
                    }else {
                        String quantity1 = Integer.toString(quantity);
                        for (Map.Entry<String, Float> calValAmount : priceBook.entrySet()){
                            if (calValAmount.getKey().equals(valueBookID.get(typeOfValBk))){
                                amount = quantity * calValAmount.getValue() * 100;
                            }
                        }
                        cumuamount += amount;
                        puramount = (Float.parseFloat(txtUnitAmount.getText()) * quantity);
                        String purAmount = getDates.getAmount(Float.toString(puramount));
                        String valAmount = getDates.getAmount(Float.toString((amount)));
                        String cumuAmount = getDates.getAmount(Float.toString((cumuamount)));
                        int serial1 = Integer.parseInt(firstSerial), serial2 = Integer.parseInt(lastSerial);
                        ArrayList<String> duplicate = new ArrayList<>();
                        if(!regValSerial.get(typeOfValBk).isEmpty()){
                        regValSerial.get(typeOfValBk).forEach(row->{
                            if(row.contains(serial1)){
                                duplicate.add(firstSerial);
                            }
                            if(row.contains(serial2)){
                                duplicate.add(lastSerial);
                            }
                        });}
                        if ("0.00".equals(purAmount)) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("Please Amount cannot be '0'");
                            alert.showAndWait();
                            txtUnitAmount.clear();
                            Condition = false;

                        }else if (duplicate.contains(firstSerial))
                        {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("Please" + firstSerial+" already entered");
                            alert.showAndWait();
                            Condition = false;
                        }else if (duplicate.contains(lastSerial))
                        {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning Dialog");
                            alert.setHeaderText("Please" + firstSerial+" already entered");
                            alert.showAndWait();
                            Condition = false;
                        }else {/*
                            colYear.setCellValueFactory(data -> data.getValue().yearProperty());
                            colMonth.setCellValueFactory(data -> data.getValue().monthProperty());
                            colQuarter.setCellValueFactory(data -> data.getValue().quarterProperty());
                            colWeek.setCellValueFactory(data -> data.getValue().dateProperty());*/
                            colDATE.setCellValueFactory(data -> data.getValue().dateProperty());
                            colTypeVB.setCellValueFactory(data -> data.getValue().valueBookProperty());
                            colSerialFrom.setCellValueFactory(data -> data.getValue().firstSerialProperty());
                            colSerialTo.setCellValueFactory(data -> data.getValue().lastSerialProperty());
                            colQuantity.setCellValueFactory(data -> data.getValue().quantityProperty());
                            colAmtVal.setCellValueFactory(data -> data.getValue().valAmountProperty());
                            colCumuAmount.setCellValueFactory(data -> data.getValue().cumuAmountProperty());
                            colPurchAmount.setCellValueFactory(data -> data.getValue().purAmountProperty());
                            colRemarks.setCellValueFactory(d -> d.getValue().remarksProperty());
                            addEntries = new GetValueBooksEntries(/*Year, Month, Quarter, Week, */date1, typeOfValBk, firstSerial,
                                    lastSerial, quantity1, valAmount, cumuAmount, purAmount, remarks);
                            tblValueBookStocks.getItems().add(addEntries);

                            if (!regValSerial.containsKey(typeOfValBk)) {
                                regValSerial.put(typeOfValBk, new ArrayList<>());
                                regValSerial.get(typeOfValBk).add(Range.between(serial1, serial2));
                            }
                            else if (regValSerial.containsKey(typeOfValBk) && !regValSerial.get(typeOfValBk).
                                    contains(Range.between(serial1, serial2))) {
                                regValSerial.get(typeOfValBk).add(Range.between(serial1, serial2));
                            }/*else if (regValSerial.containsKey(typeOfValBk) && !regValSerial.get(typeOfValBk).
                                    contains(lastSerial)) {
                                regValSerial.get(typeOfValBk).add(lastSerial);
                            }*/
                            clearEnt();
                            Condition = false;
                        }
                    }
                }
            }
        }
    }


    void clearEnt(){
        cmbTypeOfValueBook.getSelectionModel().clearSelection();
        entDatePck.setValue(null);
        txtUnitAmount.clear();
        txtSerialTo.clear();
        txtSerialFrom.clear();
    }


    public void clearEntries(ActionEvent actionEvent) {
        if (!chkUpdate.isSelected()){
        GetValueBooksEntries valueBooks = tblValueBookStocks.getSelectionModel().getSelectedItem();
        if (tblValueBookStocks.getSelectionModel().isEmpty()){
            lblEdit.setText("Please select a row in the table to "+'"'+"Edit"+'"');
            lblEdit.setVisible(true);
        }else {
            entDatePck.setValue(LocalDate.parse(valueBooks.getDate(),
                    DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            cmbTypeOfValueBook.getSelectionModel().select(valueBooks.getValueBook());
                    m = p.matcher(valueBooks.getPurAmount());
            String amount = "";
            amount = m.replaceAll("");
//            if (mac.matches()){
//
//            }else {
//                amount = valueBooks.getPurAmount();
//            }
            float price = Float.parseFloat(amount);
            int quantity = Integer.parseInt(valueBooks.getQuantity());
            txtUnitAmount.setText(Float.toString(price / quantity));
            txtSerialTo.setText(valueBooks.getLastSerial());
            txtSerialFrom.setText(valueBooks.getFirstSerial());
            if (regValSerial.get(cmbTypeOfValueBook.getSelectionModel().getSelectedItem()).contains(txtSerialTo.
                    getText()) || regValSerial.get(cmbTypeOfValueBook.getSelectionModel().getSelectedItem()).
                    contains(txtSerialFrom.getText())){
                regValSerial.get(cmbTypeOfValueBook.getSelectionModel().getSelectedItem()).remove(txtSerialFrom.
                        getText());
                regValSerial.get(cmbTypeOfValueBook.getSelectionModel().getSelectedItem()).remove(txtSerialTo.
                        getText());
            }
            ObservableList<GetValueBooksEntries> selectedRows = tblValueBookStocks.getSelectionModel().getSelectedItems();
            ArrayList<GetValueBooksEntries> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> tblValueBookStocks.getItems().remove(row));
        }
        }else {
            clear();
        }
    }


    public void SaveToDatabase(ActionEvent actionEvent) throws SQLException {
        revCent = GetCenter.getRevCenter();
        ArrayList<String> duplicate = new ArrayList<>();
        getData = new GetValueBooksEntries();
        ResultSet rs;
        ResultSetMetaData rm;
        String regex = "(?<=[\\d])(,)(?=[\\d])";
        Pattern p = Pattern.compile(regex);
        Matcher m ;
        for(int f = 0; f <= tblValueBookStocks.getItems().size(); f++) {
            if (f != tblValueBookStocks.getItems().size()){
            getData = tblValueBookStocks.getItems().get(f);
            String acDate = getDates.setSqlDate(getData.getDate());
            typeOfValBk = getData.getValueBook();
            String acFirstSerial = getData.getFirstSerial(),
             acLastSerial = getData.getLastSerial();
            int acQuantity = Integer.parseInt(getData.getQuantity());
            m = p.matcher(getData.getValAmount());
            float acAmount = Float.parseFloat(m.replaceAll(""));
            m = p.matcher(getData.getPurAmount());
            float acPurAmount = Float.parseFloat(m.replaceAll(""));
            PreparedStatement stmnt = con.prepareStatement("SELECT * FROM `value_books_stock_record`, `revenue_centers` WHERE `revenue_centers`.`CenterID` = `value_stock_revCenter` AND `revenue_centers`.`revenue_center` = '"+revCent+"' AND `value_book` = '"+valueBookID.get(typeOfValBk)+"'");
            ResultSet res = stmnt.executeQuery();
            while (res.next()){
                if(res.getInt("first_serial")<=Integer.parseInt(getData.getFirstSerial()) && Integer.parseInt(getData.getFirstSerial())<= res.getInt("last_serial")&& !duplicate.contains(getData.getFirstSerial())){
                duplicate.add(getData.getFirstSerial());
                }
                if(res.getInt("first_serial")<=Integer.parseInt(getData.getLastSerial()) && Integer.parseInt(getData.getLastSerial())<= res.getInt("last_serial") && !duplicate.contains(getData.getLastSerial())){
                    duplicate.add(getData.getLastSerial());
                }
            }
            if (duplicate.contains(getData.getFirstSerial()) && duplicate.contains(getData.getLastSerial())){
                lblDup.setText("Range of Serials for "+'"'+typeOfValBk+'"'+"" +
                        " already exist. Please select row of duplicate data to Edit or Delete." );
                lblDup.setVisible(true);
                f = tblValueBookStocks.getItems().size() + 1;
            }else
            if (duplicate.contains(getData.getLastSerial())){
                lblDup.setText("Last Serial"+'"'+acLastSerial+'"'+" for "+'"'+typeOfValBk+'"'+"" +
                        " already exist. Please select row of duplicate data to Edit or Delete." );
                lblDup.setVisible(true);
                f = tblValueBookStocks.getItems().size() + 1;
            }else
            if (duplicate.contains(getData.getFirstSerial())){
                lblDup.setText("First Serial"+'"'+acFirstSerial+'"'+" for "+'"'+typeOfValBk+'"'+"" +
                        " already exist. Please select row of duplicate data to Edit or Delete." );
                lblDup.setVisible(true);
                f = tblValueBookStocks.getItems().size() + 1;
            }else {
                stmnt = con.prepareStatement("INSERT INTO `value_books_stock_record`(`value_stock_revCenter`, " +
                        "`date`, `value_book`, `first_serial`, `last_serial`, `quantity`, `amount`, `purchase_amount`," +
                        " `remarks`) VALUES ('" + revCentID + "','" + acDate + "', '" + valueBookID.get(typeOfValBk)+ "', '" +
                        acFirstSerial + "','" + acLastSerial + "', '" + acQuantity + "', '" + acAmount+ "', '" + acPurAmount
                        + "','" + remarks + "')");
                stmnt.executeUpdate();
            }
            } else {
                tblValueBookStocks.getItems().clear();
                regValSerial.clear();
            }
        }
    }


    public void CancelEntries(ActionEvent actionEvent) {
        tblValueBookStocks.getItems().clear();
    }


    public void onlyNumbers(KeyEvent keyEvent) {
        bnkDtls.onlyNumbers(keyEvent);
    }


    public void onlyAmount(KeyEvent keyEvent) {
        bnkDtls.onlyIntegers(keyEvent);
    }


    public void deleteSelection(ActionEvent actionEvent) throws SQLException {
        if (!chkUpdate.isSelected()){
        if(tblValueBookStocks.getSelectionModel().isEmpty()){
            lblDeleteWarn.setVisible(true);
        } else {
            ObservableList<GetValueBooksEntries> selectedRows = tblValueBookStocks.getSelectionModel().getSelectedItems();
            ArrayList<GetValueBooksEntries> rows = new ArrayList<>(selectedRows);
            rows.forEach(row -> tblValueBookStocks.getItems().remove(row));
        }
        }else {
            stmnt = con.prepareStatement("DELETE FROM `value_books_stock_record` WHERE `value_record_ID` = '"+entriesID+"' ");
            stmnt.executeUpdate();
            loadRevenueCollectionTable();
            clear();
        }
    }


}
