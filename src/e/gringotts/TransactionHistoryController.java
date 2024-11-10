package e.gringotts;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.time.LocalDate;

public class TransactionHistoryController extends Account implements Initializable {

    @FXML private TableColumn<Transaction, Double> colam;
    @FXML private TableColumn<Transaction, String> colca;
    @FXML private TableColumn<Transaction, Date> colda;
    @FXML private TableColumn<Transaction, String> colrn;
    @FXML private TableColumn<Transaction, String> colsn;
    @FXML private TableColumn<Transaction, String> coltr;
    @FXML private TableView<Transaction> tableTransaction;
    @FXML private TextField filterField;
    @FXML private DatePicker endDate;
    @FXML private DatePicker startDate;
    @FXML private TextField max;
    @FXML private TextField min;
    @FXML private FontAwesomeIconView buttonRefresh;
    @FXML private FontAwesomeIconView buttonfilter;

    private ObservableList<Transaction> transactions;
    private FilteredList<Transaction> filteredData;
    private String loginname;
    private int userId;

    @FXML private ImageView imageBubble;
    @FXML private Label line1;
    @FXML private Label line2;
    @FXML private Label line3;
    @FXML private HBox homebox;
    @FXML private HBox currencyBox;
    @FXML private FontAwesomeIconView iconCurrency;
    @FXML private FontAwesomeIconView iconHome;
    @FXML private FontAwesomeIconView iconInsight;
    @FXML private FontAwesomeIconView iconLogout;
    @FXML private FontAwesomeIconView iconProfile;
    @FXML private FontAwesomeIconView iconTransaction;
    @FXML private HBox insightBox;
    @FXML private Label labelCurrency;
    @FXML private Label labelHome;
    @FXML private Label labelInsight;
    @FXML private Label labelLogout;
    @FXML private Label labelProfile;
    @FXML private Label labelTransaction;
    @FXML private HBox logoutBox;
    @FXML private HBox profileBox;
    @FXML private HBox transactionBox;
    @FXML private FontAwesomeIconView iconHistory;
    @FXML private Label labelHistory;
    @FXML private HBox historyBox;

   
    private PreparedStatement st = null;
    private ResultSet rs = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the table columns
        coltr.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        colsn.setCellValueFactory(new PropertyValueFactory<>("senderName"));
        colrn.setCellValueFactory(new PropertyValueFactory<>("receiverName"));
        colam.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colda.setCellValueFactory(new PropertyValueFactory<>("dateOfTransaction"));
        colca.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    public void setUsernameId(String name, int id) {
        this.loginname = name;
        this.userId = id;
        loadTransactions();
        setupFiltering();
    }

    //load all the transaction
    private void loadTransactions() {
        transactions = FXCollections.observableArrayList();
        String query = "SELECT * FROM account.transaction WHERE senderName = ? OR receiverName = ? ORDER BY timeOfTransaction DESC";
        try {
            st = this.con.prepareStatement(query);
            st.setString(1, loginname);
            st.setString(2, loginname);
            rs = st.executeQuery();
            while (rs.next()) {
                Transaction tr = new Transaction();
                tr.setTransactionId(rs.getString("transactionId"));
                tr.setSenderName(rs.getString("senderName"));
                tr.setReceiverName(rs.getString("receiverName"));
                tr.setAmount(rs.getDouble("amount"));
                tr.setDateTime(rs.getDate("dateOfTransaction"));
                tr.setCategory(rs.getString("category"));
                transactions.add(tr);
            }
            tableTransaction.setItems(transactions);
        } catch (SQLException e) {
            System.out.println("Error Connect With Database");
        }
    }
    
    // Set up filtering
    private void setupFiltering() {
        filteredData = new FilteredList<>(transactions, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        startDate.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        endDate.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        min.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        max.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        SortedList<Transaction> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableTransaction.comparatorProperty());
        tableTransaction.setItems(sortedData);
    }
    
    // Apply all filters
    private void applyFilters() {
        filteredData.setPredicate(transaction -> {
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            Double minAmount = null, maxAmount = null;
            
            // Parse the min and max key in by user to double
            if (!min.getText().isEmpty()) {
                minAmount = Double.parseDouble(min.getText());
            }
            if (!max.getText().isEmpty()) {
                maxAmount = Double.parseDouble(max.getText());
            }
            
            // Apply date range filter
            if (start != null && transaction.getDateOfTransaction().before(java.sql.Date.valueOf(start))) {
                return false;
            }
            if (end != null && transaction.getDateOfTransaction().after(java.sql.Date.valueOf(end))) {
                return false;
            }
            
            //Apply amount range filter
            if (minAmount != null && transaction.getAmount() < minAmount) {
                return false;
            }
            if (maxAmount != null && transaction.getAmount() > maxAmount) {
                return false;
            }
            
            // Apply keyword filter
            String lowerCaseFilter = filterField.getText().toLowerCase();
            if (!lowerCaseFilter.isEmpty()) {
                if (transaction.getSenderName().toLowerCase().contains(lowerCaseFilter)
                        || transaction.getReceiverName().toLowerCase().contains(lowerCaseFilter)
                        || transaction.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        });
    }

   // Clear all the input columns and refresh
    @FXML
    void clear(MouseEvent event) {
        filterField.clear();
        startDate.setValue(null);
        endDate.setValue(null);
        min.clear();
        max.clear();
        applyFilters();
    }

    

    // to show the bubble dialog when the mouse move to it
    @FXML
    void showBubble(MouseEvent event) {
        imageBubble.setVisible(true);
        line1.setVisible(true);
        line2.setVisible(true);
        line3.setVisible(true);
    }

    // to set invisible of the bubble dialog when the mouse move away from it
    @FXML
    void disableBubble(MouseEvent event) {
        imageBubble.setVisible(false);
        line1.setVisible(false);
        line2.setVisible(false);
        line3.setVisible(false);
    }

    // for showing the selection of the menu bar when the mouse move to the menu bar
    @FXML
    void moveToHome(MouseEvent event) {
        homebox.setStyle("-fx-background-color: #FFFFFF;");
        labelHome.setTextFill(Color.web("#000000"));
        iconHome.setFill(Color.web("#000000"));

    }

    @FXML
    void leaveHome(MouseEvent event) {
        homebox.setStyle("-fx-background-color: #000000;");
        labelHome.setTextFill(Color.web("#FFFFFF"));
        iconHome.setFill(Color.web("#FFFFFF"));

    }

    @FXML
    void moveToTransaction(MouseEvent event) {
        transactionBox.setStyle("-fx-background-color: #FFFFFF;");
        labelTransaction.setTextFill(Color.web("#000000"));
        iconTransaction.setFill(Color.web("#000000"));

    }

    @FXML
    void leaveTransaction(MouseEvent event) {
        transactionBox.setStyle("-fx-background-color: #000000;");
        labelTransaction.setTextFill(Color.web("#FFFFFF"));
        iconTransaction.setFill(Color.web("#FFFFFF"));

    }

    @FXML
    void moveToCurrency(MouseEvent event) {
        currencyBox.setStyle("-fx-background-color: #FFFFFF;");
        labelCurrency.setTextFill(Color.web("#000000"));
        iconCurrency.setFill(Color.web("#000000"));

    }

    @FXML
    void leaveCurrency(MouseEvent event) {
        currencyBox.setStyle("-fx-background-color: #000000;");
        labelCurrency.setTextFill(Color.web("#FFFFFF"));
        iconCurrency.setFill(Color.web("#FFFFFF"));

    }

    @FXML
    void moveToInsight(MouseEvent event) {
        insightBox.setStyle("-fx-background-color: #FFFFFF;");
        labelInsight.setTextFill(Color.web("#000000"));
        iconInsight.setFill(Color.web("#000000"));

    }

    @FXML
    void leaveInsight(MouseEvent event) {
        insightBox.setStyle("-fx-background-color: #000000;");
        labelInsight.setTextFill(Color.web("#FFFFFF"));
        iconInsight.setFill(Color.web("#FFFFFF"));

    }

    @FXML
    void moveToProfile(MouseEvent event) {
        profileBox.setStyle("-fx-background-color: #FFFFFF;");
        labelProfile.setTextFill(Color.web("#000000"));
        iconProfile.setFill(Color.web("#000000"));

    }

    @FXML
    void leaveProfile(MouseEvent event) {
        profileBox.setStyle("-fx-background-color: #000000;");
        labelProfile.setTextFill(Color.web("#FFFFFF"));
        iconProfile.setFill(Color.web("#FFFFFF"));

    }

    @FXML
    void moveToLogout(MouseEvent event) {
        logoutBox.setStyle("-fx-background-color: #FFFFFF;");
        labelLogout.setTextFill(Color.web("#000000"));
        iconLogout.setFill(Color.web("#000000"));

    }

    @FXML
    void leaveLogout(MouseEvent event) {
        logoutBox.setStyle("-fx-background-color: #000000;");
        labelLogout.setTextFill(Color.web("#FFFFFF"));
        iconLogout.setFill(Color.web("#FFFFFF"));

    }

    @FXML
    public void JumpToInsight(MouseEvent event) throws IOException {
  //Switch to Scene Insight
        InsightController ic = new InsightController();
         ic.setUsernameId(loginname, userId);
         FXMLLoader loader = new FXMLLoader(getClass().getResource("UserInsight.fxml"));
         loader.setController(ic);
         Parent root = loader.load();
         
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void JumpToAccount(MouseEvent event) throws IOException {

     //Jump to User Profile Page
         UserProfileController UPC = new UserProfileController();
         UPC.setUsernameId(loginname, userId);
         FXMLLoader loader = new FXMLLoader(getClass().getResource("UserProfile.fxml"));
         loader.setController(UPC);
         Parent root = loader.load();
       
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void LogOut(MouseEvent event) throws IOException {

        //Switch to Scene Login
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void JumpToTransaction(MouseEvent event) throws IOException {

        //Switch to Scene Transaction
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Search.fxml"));
        Parent root = loader.load();
        SearchController SC = loader.getController();
        SC.setUsernameId(this.loginname, this.userId);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void JumpToCurrency(MouseEvent event) throws IOException, SQLException {

        //Switch to Scene conversion currency
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CurrencyConversion.fxml"));
        Parent root = loader.load();
        CurrencyConversionController CCC = loader.getController();
        CCC.setUsernameId(this.loginname, this.userId);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void JumpToHome(MouseEvent event) throws IOException, SQLException {

        //Switch to Scene Home
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();
        DashboardController db = loader.getController();
        db.setUsernameId(this.loginname, this.userId);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
