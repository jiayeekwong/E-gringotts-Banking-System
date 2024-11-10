package e.gringotts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TransactionHistoryAdminController extends Account implements Initializable {

    @FXML
    private FontAwesomeIconView buttonRefresh;
    @FXML
    private FontAwesomeIconView buttonfilter;
    @FXML
    private TableColumn<Transaction, Double> colam;
    @FXML
    private TableColumn<Transaction, String> colca;
    @FXML
    private TableColumn<Transaction, Date> colda;
    @FXML
    private TableColumn<Transaction, Integer> coluser;
    @FXML
    private TableColumn<Transaction, String> colrn;
    @FXML
    private TableColumn<Transaction, String> colsn;
    @FXML
    private TableColumn<Transaction, String> coltr;
    @FXML
    private TableView<Transaction> table;
    @FXML
    private DatePicker endDate;
    @FXML
    private TextField filterField;
    @FXML
    private TextField max;
    @FXML
    private TextField min;
    @FXML
    private DatePicker startDate;
    @FXML
    private Label labelHome;
    @FXML
    private FontAwesomeIconView iconHome;
    @FXML
    private HBox homebox;
    @FXML
    private TextField filterUserId;
    @FXML
    private HBox searchUser;
    private ObservableList<Transaction> dataList;
    private FilteredList<Transaction> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showTransaction();
        setupFiltering();
    }

    // Get all the transactions of all users
    public ObservableList<Transaction> getTransactions() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        String query = "SELECT * FROM transaction ORDER BY timeOfTransaction DESC";

        try (PreparedStatement st = this.con.prepareStatement(query); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Transaction tr = new Transaction();
                tr.setUserId(rs.getInt("userId"));
                tr.setTransactionId(rs.getString("transactionId"));
                tr.setSenderName(rs.getString("senderName"));
                tr.setReceiverName(rs.getString("receiverName"));
                tr.setAmount(rs.getDouble("amount"));
                tr.setDateTime(rs.getDate("dateOfTransaction"));
                tr.setCategory(rs.getString("category"));
                transactions.add(tr);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    // Show the transaction in the table
    public void showTransaction() {
        dataList = getTransactions();
        table.setItems(dataList);
        coluser.setCellValueFactory(new PropertyValueFactory<>("userId"));
        coltr.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        colsn.setCellValueFactory(new PropertyValueFactory<>("senderName"));
        colrn.setCellValueFactory(new PropertyValueFactory<>("receiverName"));
        colam.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colda.setCellValueFactory(new PropertyValueFactory<>("dateOfTransaction"));
        colca.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    // Set up filtering
    private void setupFiltering() {
        filteredData = new FilteredList<>(dataList, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        startDate.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        endDate.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        min.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        max.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        filterUserId.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        SortedList<Transaction> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    // Apply all filters
    private void applyFilters() {
        filteredData.setPredicate(transaction -> {
            // Apply date range filter
            LocalDate start = startDate.getValue();
            LocalDate end = endDate.getValue();
            if (start != null && transaction.getDateOfTransaction().before(java.sql.Date.valueOf(start))) {
                return false;
            }
            if (end != null && transaction.getDateOfTransaction().after(java.sql.Date.valueOf(end))) {
                return false;
            }

            // Apply amount range filter
            Double minAmount = null, maxAmount = null;
            if (!min.getText().isEmpty()) {
                try {
                    minAmount = Double.parseDouble(min.getText());
                } catch (NumberFormatException e) {
                    // Handle invalid input
                }
            }
            if (!max.getText().isEmpty()) {
                try {
                    maxAmount = Double.parseDouble(max.getText());
                } catch (NumberFormatException e) {
                    // Handle invalid input
                }
            }
            if (minAmount != null && transaction.getAmount() < minAmount) {
                return false;
            }
            if (maxAmount != null && transaction.getAmount() > maxAmount) {
                return false;
            }

            // Apply user ID filter
            if (!filterUserId.getText().isEmpty()) {
                try {
                    int userId = Integer.parseInt(filterUserId.getText());
                    if (transaction.getUserId() != userId) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    return false;
                }
            }

            // Apply keyword filter
            String lowerCaseFilter = filterField.getText().toLowerCase();
            if (!lowerCaseFilter.isEmpty()) {
                if (transaction.getSenderName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (transaction.getReceiverName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(transaction.getAmount()).contains(lowerCaseFilter)) {
                    return true;
                } else if (transaction.getCategory().toLowerCase().contains(lowerCaseFilter)) {
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
        filterUserId.clear();
        applyFilters();
    }

    // For showing the selection of button back to homepage when the mouse moves to the button
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
    private void JumpToMenu(MouseEvent event) throws IOException {
        //Switch to Home
       Parent root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

   

}
