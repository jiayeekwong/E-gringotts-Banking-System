
package e.gringotts;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;

import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox; 
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;


public class SearchController extends Account implements Initializable{
    @FXML
    private TextField amount;
    @FXML
    private Button comfirm;
    private ObservableList<Account> userData;
    private Label insufficient;
    @FXML
    private TextField name;
    @FXML
    private TableColumn<Account,String> col1;
    @FXML
    private TableColumn<Account,String> col2;
    @FXML
    private TableColumn<Account,String> col3;
    @FXML
    private TableView<Account> Table;
    @FXML
    private ChoiceBox<String> Choicebox;
     @FXML
    private MediaView backgroundMediaView;
    @FXML
    private TextField pin;
    @FXML
    private PasswordField pinhidden;
    @FXML
    private CheckBox showPin;
    @FXML
    private Button okpin;
    private TextField receipt1;
    private TextField insufficient2;
    @FXML
    private Label label;
    @FXML
    private Button searchuser;
    @FXML
    private Button maketransaction;
    @FXML
    private HBox homebox;
    @FXML
    private FontAwesomeIconView iconHome;
    @FXML
    private Label labelHome;
    @FXML
    private HBox transactionBox;
    @FXML
    private FontAwesomeIconView iconTransaction;
    @FXML
    private Label labelTransaction;
    @FXML
    private HBox currencyBox;
    @FXML
    private FontAwesomeIconView iconCurrency;
    @FXML
    private Label labelCurrency;
    @FXML
    private HBox historyBox;
    @FXML
    private FontAwesomeIconView iconHistory;
    @FXML
    private Label labelHistory;
    @FXML
    private HBox insightBox;
    @FXML
    private FontAwesomeIconView iconInsight;
    @FXML
    private Label labelInsight;
    @FXML
    private HBox profileBox;
    @FXML
    private FontAwesomeIconView iconProfile;
    @FXML
    private Label labelProfile;
    @FXML
    private HBox logoutBox;
    @FXML
    private FontAwesomeIconView iconLogout;
    @FXML
    private Label labelLogout;
    
   
    //Data
    private String loginname;
    private double senderBalance;
    private int userId;
    private Transaction lastTransaction;
    PreparedStatement st = null;
    ResultSet rs = null;
    private ObservableList<String> options;
    private ObservableList<Transaction> transactions= FXCollections.observableArrayList();
    
 
   public void setUsernameId(String user,int Id){
       this.loginname = user;
       this.userId = Id;
        try {
            this.senderBalance = this.getBalancefromDB(this.userId);
        } catch (SQLException ex) {
            System.out.println("Error with Database");
        }
   }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
      
    // Load the video file
    String URL = "file:/C://Users//jiaye//OneDrive//Documents//NetBeansProjects//E-Gringiotts//src//e//gringotts//searchbackground.mp4";
    if (URL != null) {
        Media media = new Media(URL);
        MediaPlayer mediaPlayerSearch = new MediaPlayer(media);
        backgroundMediaView.setMediaPlayer(mediaPlayerSearch);

        // Play the video in a loop
        mediaPlayerSearch.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayerSearch.play();
    } else {
        System.out.println("Cannot Load Background of This Page");
    }


        
    options = FXCollections.observableArrayList(
                "Food", "Grocery", "Fees", "Gift", "Entertainment", "Transport", "Others"
            );
            Choicebox.setItems(options);
            Choicebox.setOnAction(this::getCategoryFromUser);
        }
    
    @FXML 
public void searchUser() { 
 
 //load userdata from database  
 userData = getUser(); 
 
    // Assuming userData is already populated and sorted by username 
    Collections.sort(userData, Comparator.comparing(Account::getUsername)); 
 
    FilteredList<Account> filteredData = new FilteredList<>(userData, b -> true); 
 
    name.textProperty().addListener((observable, oldValue, newValue) -> { 
        if (newValue == null || newValue.isEmpty()) { 
            filteredData.setPredicate(b -> true); 
        } else { 
            String lowerCaseFilter = newValue.toLowerCase(); 
            filteredData.setPredicate(user -> { 
                String lowercaseUsername = user.getUsername().toLowerCase();
                String lowercaseFilter = lowerCaseFilter.toLowerCase(); 
                int index = binarySearch(userData, lowercaseFilter); 
                if (index < 0) { 
                    index = -(index + 1); // Insertion point if not found 
                } 
                return lowercaseUsername.startsWith(lowercaseFilter); 
            });
        } 
    }); 
 
    SortedList<Account> sortedData = new SortedList<>(filteredData); 
    sortedData.comparatorProperty().bind(Table.comparatorProperty()); 
    Table.setItems(sortedData); 
     
    // Set cell value factories 
    col1.setCellValueFactory(new PropertyValueFactory<Account,String>("username")); 
    col2.setCellValueFactory(new PropertyValueFactory<Account,String>("contact")); 
    col3.setCellValueFactory(new PropertyValueFactory<Account,String>("userType")); 
}

private int binarySearch(List<Account> list, String key) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            String midUsername = list.get(mid).getUsername().toLowerCase();

            if (midUsername.equals(key)) {
                return mid; // Key found
            } else if (midUsername.compareTo(key) < 0) {
                low = mid + 1; // Search in the right half
            } else {
                high = mid - 1; // Search in the left half
            }
        }
        return -1; // Key not found
    }

    
    
    // Method to get the sender's balance from the database
    private double getSenderBalanceFromDB(String username) throws SQLException {
        String query = "SELECT Balance FROM account.account WHERE username = ?";
        try (PreparedStatement stmt = this.con.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("Balance");
                } else {
                    // Handle case when no balance is found for the username
                    return 0.0; // Or any default value you want to use
                }
            }
        }
    }
    
//method to add a transaction between users
    @FXML
    public void addTransaction(ActionEvent event) throws IOException, SQLException {
        
        double amount = Double.parseDouble(this.amount.getText());
        // Method to get the sender's balance from the database
        // Get the latest sender balance from the database
        double senderBalance = getSenderBalanceFromDB(this.loginname);
        
        //If user don't have enough amount to transfer
        if (senderBalance < amount) {
           
                // Show option dialog to ask the user if they want to make another transaction
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Insufficient Balance");
                alert.setHeaderText("Insufficient balance. Do you want to make another transaction?");
                alert.setContentText("Choose your option.");

                ButtonType buttonTypeYes = new ButtonType("Yes");
                ButtonType buttonTypeNo = new ButtonType("No");

                alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

                Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == buttonTypeYes) {
                    // User clicked Yes, clear the amount field to allow re-entry
                    insufficient2.setText("Please enter a new transaction amount.");
                    this.amount.clear();
                } else {
                    // User clicked No, navigate back to the main page
                    // Assume you have a method to navigate to the main page, such as goToMainPage()
                    JumpToDashboard();
                }

          }
    
        //If user do not choose category
      else if(Choicebox.getValue()==null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Information Incomplete");
                alert.setHeaderText("Transaction Category Cannot Be Empty");
                alert.setContentText("Please Choose a category");
                alert.showAndWait();
          
      }
      
      
       //If user have enough amount to be transferred $ category is not empty
      else{ 
         
        
        // Deduct amount from sender
        String updateSenderBalance = "UPDATE account.account SET Balance = (Balance - ?) WHERE username = ?";
            PreparedStatement updateSender = this.con.prepareStatement(updateSenderBalance);
            updateSender.setDouble(1, amount);
            updateSender.setString(2, this.loginname);
            updateSender.executeUpdate();
        

        // Add amount to receiver
        String updateReceiverBalance = "UPDATE account.account SET Balance = (Balance + ? )WHERE username = ?";
            PreparedStatement updateReceiver = this.con.prepareStatement(updateReceiverBalance);
            updateReceiver.setDouble(1, amount);
            updateReceiver.setString(2, name.getText());
            updateReceiver.executeUpdate();
        

        // Record the transaction
        String transactionId = UUID.randomUUID().toString();
        Date now = new Date();
        LocalDateTime time= LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedtime = time.format(formatter);

        
//        // Get sender's userId
        int senderUserId = this.userId;
//            

        // Assuming you have a method to get the updated balance after the transaction
        senderBalance = this.senderBalance-amount;
    
        //get category from user
        String usercategory = Choicebox.getValue();
    
        lastTransaction = new Transaction(
                senderUserId,
                transactionId,
                this.loginname,
                name.getText(),
                amount,
                senderBalance,
                now,
                usercategory,
                formattedtime
            );
    
   String insertTransaction = "INSERT INTO account.transaction (userId, transactionId, senderName, receiverName, amount,  dateOfTransaction, category, timeOfTransaction) VALUES (?, ?, ?, ?, ?, ?,  ?,?)";
        try (PreparedStatement insertStmt = this.con.prepareStatement(insertTransaction)) {
            insertStmt.setInt(1, senderUserId);
            insertStmt.setString(2, lastTransaction.getTransactionId());
            insertStmt.setString(3, lastTransaction.getSenderName());
            insertStmt.setString(4, lastTransaction.getReceiverName());
            insertStmt.setDouble(5, lastTransaction.getAmount());
            insertStmt.setDate(6, new java.sql.Date(lastTransaction.getDateOfTransaction().getTime()));
            insertStmt.setString(7, lastTransaction.getCategory());
            insertStmt.setString(8,lastTransaction.getTime() ); // Setting the timeOfTransaction field
            
            insertStmt.execute();
        }
        this.con.setAutoCommit(true);

        
    //show success alert
    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
    successAlert.setTitle("Transaction Successful");
    successAlert.setContentText("Your transaction was successfully procesed.");
    ButtonType generateReceiptButton = new ButtonType("Generate Receipt");
    successAlert.getButtonTypes().setAll(generateReceiptButton);

    Optional<ButtonType> result = successAlert.showAndWait();
    if (result.isPresent() && result.get() == generateReceiptButton) {
        // Call JumpToReceipt method to switch to the receipt scene
        JumpToReceipt(event);
    }

}
        }
    


    @FXML
    public void showAmount(ActionEvent event) {
        pin.setVisible(true);
        pinhidden.setVisible(true);
        showPin.setVisible(true);
        okpin.setVisible(true);
       

    }


   
    public void JumpToReceipt(ActionEvent event) throws IOException{
        
        if (lastTransaction != null) {
        // Generate and save the receipt
        String receipt = lastTransaction.generateReceipt();


        // Load the FXML and get the controller
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Receipt.fxml"));
            Parent root = loader.load();
            // Get the controller and set the receipt text
              ReceiptController receiptcontroller = loader.getController();
              getTransactions();
         //set the receipt text
              receiptcontroller.setReceiptText(receipt);
              receiptcontroller.setUsernameId(this.loginname, this.userId);
              receiptcontroller.setTransactionList(this.transactions);

            // Switch to the Receipt scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        
        }
    }
    
     public void JumpToDashboard() {
        //Switch to Scene Register
       try{
           FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
           Parent root = loader.load();
           Stage stage = (Stage) insufficient2.getScene().getWindow();
           stage.setScene(new Scene(root));
           stage.show();
           
       }catch(IOException e){
           e.printStackTrace();
       }
    
    }
    
    
   private void getCategoryFromUser(ActionEvent event) {
        String usercategory = Choicebox.getValue();
    }
    
    public void SelectCategory(ActionEvent event) {
    String s = Choicebox.getSelectionModel().getSelectedItem();
    }
    
    
    
    public void showTable() throws SQLException{
        ObservableList<Account> userData = getUser();
        Table.setItems(userData);
//        colus.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("userId"));
        col1.setCellValueFactory(new PropertyValueFactory<Account,String>("username"));
        col2.setCellValueFactory(new PropertyValueFactory<Account,String>("contact"));
        col3.setCellValueFactory(new PropertyValueFactory<Account,String>("userType"));
        
     
    }
    
    public ObservableList<Account> getUser(){
        ObservableList<Account> userData = FXCollections.observableArrayList();
        String query = "select * from account.account WHERE BINARY username = '"+name.getText()+"' OR BINARY contact = '"+name.getText()+"'";
     try{

            st = this.con.prepareStatement(query);
            rs = st.executeQuery();
            while(rs.next()){
                if(!rs.getString("username").equals(this.loginname)){
                     if(!rs.getString("userType").equals("Goblin")){
                    //Remove  goblin from search result
                     Account acc = new Account();
                    acc.setUsername(rs.getString("username"));
                    acc.setContact(rs.getString("contact"));
                    acc.setUserType(rs.getString("userType"));
                    userData.add(acc);
                }
                }
               
             
            }
       
        } catch (SQLException ex) {
           System.out.println("Error with Database");
        }

        return userData;
    }
    
    public boolean CheckPin(String pinEntered) throws SQLException{
        String pinRetrieved = "";
         String query = "select pin from account.account WHERE BINARY username = '"+this.loginname+"'";
         st = this.con.prepareStatement(query);
         rs = st.executeQuery();
          while(rs.next()){
             pinRetrieved = rs.getString("pin");
          }
          return pinEntered.equals(pinRetrieved);
    }
    
      @FXML
 public void showPin(ActionEvent event) {
  
            if (showPin.isSelected()) {
                pin.setText(pinhidden.getText());
                pin.setVisible(true);
                pinhidden.setVisible(false);

                // Hide the password again after 1 second
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.seconds(1),
                        e -> {
                           
                            pin.setVisible(false);
                            pinhidden.setVisible(true);
                            showPin.setSelected(false);
                        }));
                timeline.setCycleCount(1);
                timeline.play();
            } else {
                pin.setVisible(false);
                pinhidden.setVisible(true);
                 
            }
            
     
}
 
    @FXML
 public void showTransactionPin() throws SQLException{
     if(CheckPin(this.pinhidden.getText())){
         amount.setVisible(true);
        comfirm.setVisible(true);
        Choicebox.setVisible(true);
 }else{
         Alert alert = new Alert(AlertType.ERROR); 
                    alert.setTitle("Incorrect PIN"); 
                    alert.setHeaderText(null); 
                    alert.setContentText("Incorrect PIN. Please try again."); 
                    alert.showAndWait(); 
                    pin.clear();
                    pinhidden.clear();
     }
 }
 
 public ObservableList<Transaction> getTransactions(){
        
        String query = "SELECT * FROM account.transaction \n" +
        "WHERE senderName = '"+this.loginname+"' OR receiverName = '"+this.loginname+"'\n" +
        "ORDER BY timeOfTransaction DESC \n" +
        "LIMIT 5;";
        try {
            PreparedStatement PS = con.prepareStatement(query);
            ResultSet RS = PS.executeQuery();
            while(RS.next()){
                Transaction tr = new Transaction();                
                tr.setSenderName(RS.getString("senderName"));
                tr.setReceiverName(RS.getString("receiverName"));
                tr.setAmount(RS.getDouble("amount"));
                tr.setDateTime(RS.getDate("dateOfTransaction"));
                tr.setCategory(RS.getString("category"));
                this.transactions.add(tr);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this.transactions;
        //Return transaction list
    }

   
    
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
    void moveToTransaction(MouseEvent event) {
    transactionBox.setStyle("-fx-background-color: #FFFFFF;");
    labelTransaction.setTextFill(Color.web("#000000"));
    iconTransaction.setFill(Color.web("#000000"));
    
}
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
    private void leaveHistory(MouseEvent event) {
    historyBox.setStyle("-fx-background-color: #000000;");
    labelHistory.setTextFill(Color.web("#FFFFFF"));
    iconHistory.setFill(Color.web("#FFFFFF"));
    }

    @FXML
    private void moveToHistory(MouseEvent event) {
    historyBox.setStyle("-fx-background-color: #FFFFFF;");
    labelHistory.setTextFill(Color.web("#000000"));
    iconHistory.setFill(Color.web("#000000"));
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
    
        //Switch to Scene Register
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void JumpToTransactionHistory(MouseEvent event) throws IOException {
    
        //Switch to Scene transaction history
       FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionHistory.fxml"));
         Parent root = loader.load();
         TransactionHistoryController THC = loader.getController();
         THC.setUsernameId(this.loginname,this.userId);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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
        CCC.setUsernameId(this.loginname,this.userId);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
    
    @FXML
    public void JumpToHome(MouseEvent event) throws IOException, SQLException {
    
        //Switch to Scene conversion currency
       FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        Parent root = loader.load();
        DashboardController db = loader.getController();
        db.setUsernameId(this.loginname,this.userId);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }

}