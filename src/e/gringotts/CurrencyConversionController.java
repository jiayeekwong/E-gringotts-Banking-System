
package e.gringotts;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


public class CurrencyConversionController extends Account implements Initializable{

 //Data
    private String FCurrency;
    private String TCurrency;
    private double balance;
    private double processingFee;
    private double updatedBalance;
    
    @FXML
    TextField fromCurrencyTF;
    private Stage stage;
    private Scene scene;
    private Parent root;	
    @FXML
    private Label toCurrencyLabel;
    private Label initialBalanceLabel;
    @FXML
    private Button btnConvert;
    @FXML
    private Label processingFeeAmountLabel;
    private Label receiptLabel;
    @FXML
    private ChoiceBox<String> fromCurrencyCB;
    @FXML
    private ChoiceBox<String> toCurrencyCB;
    private int userId;
    private String loginname;
    private Label Receipt;
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
    Transaction lastTransaction;
    private ObservableList<Transaction> transactions= FXCollections.observableArrayList();
     public void setUsernameId(String name,int Id) throws SQLException{
       this.userId = Id;
       this.loginname = name;
       this.balance = this.getBalancefromDB(this.userId); 
   }
     
    @Override
    public void initialize (URL arg0, ResourceBundle arg1){
     try {
            fromCurrencyCB.getItems().addAll(currencyList()); //Show in choice box
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CurrencyConversionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CurrencyConversionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        fromCurrencyCB.setOnAction(this::getFCurrency);
        
        try {
            toCurrencyCB.getItems().addAll(currencyList());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CurrencyConversionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CurrencyConversionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        toCurrencyCB.setOnAction(this::getTCurrency);
    }
    
    public String[] currencyList()throws ClassNotFoundException, SQLException {
        //Retrieve currency from db
        ArrayList<String> list = new ArrayList<>();
        PreparedStatement stmt = this.con.prepareStatement("SELECT DISTINCT Currency FROM exchangerates");
        ResultSet RS = stmt.executeQuery();
        while (RS.next()){
            list.add(RS.getString("Currency"));
        }
        
        String[] arr = new String[list.size()];
        for (int i=0; i<list.size(); i++){
            arr[i] = list.get(i);
        }
        return arr;
    }
    
    public void getFCurrency (ActionEvent event){
        //From currency:
        this.FCurrency = fromCurrencyCB.getValue();        
    }
    
    public void getTCurrency (ActionEvent event){
        //To Currency:
        this.TCurrency = toCurrencyCB.getValue();        
    }
    
    @FXML
    public void btnConvertClicked (ActionEvent event) throws IOException, ClassNotFoundException, SQLException{
        
        currencyConversion c = new currencyConversion();
        String fromCurrency = fromCurrencyTF.getText();
        toCurrencyLabel.setText(Double.toString(BigDecimal.valueOf(c.convert(FCurrency, TCurrency,Double.parseDouble(fromCurrency),0.01)).setScale(5, RoundingMode.HALF_UP).doubleValue()));
        double pfValue = BigDecimal.valueOf(c.feeGraph(FCurrency, TCurrency)).setScale(3, RoundingMode.HALF_UP).doubleValue();
                
        processingFeeAmountLabel.setText(Double.toString(pfValue)); 
      
    
        double fC = Double.parseDouble(fromCurrency);
        BigDecimal fCDecimal = BigDecimal.valueOf(fC);
        BigDecimal balanceDecimal = BigDecimal.valueOf(balance);
        BigDecimal pfValueDecimal = BigDecimal.valueOf(pfValue);
        BigDecimal pfTotalDecimal = fCDecimal.multiply(pfValueDecimal);
        BigDecimal CBDecimal = balanceDecimal.subtract(pfTotalDecimal);
        this.processingFee = pfTotalDecimal.doubleValue();
        this.updatedBalance = CBDecimal.doubleValue();
          
    }
    
    @FXML
    public void btnConfirmClicked(ActionEvent event) throws IOException, SQLException{
         // Record the transaction
        String transactionId = UUID.randomUUID().toString();
        Date now = new Date();
        LocalDateTime time= LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedtime = time.format(formatter);
        
        lastTransaction = new Transaction(
                this.userId,
                transactionId,
                this.loginname,
                "E-Gringotts",
                this.processingFee,
                this.updatedBalance, //this one is updated balance
                now,
                "Currency Conversion",
                formattedtime
            );
        
          //Store into transaction database
         String insertTransaction = "INSERT INTO account.transaction (userId, transactionId, senderName, receiverName, amount,  dateOfTransaction, category, timeOfTransaction) VALUES (?, ?, ?, ?, ?, ?,  ?,?)";
        try (PreparedStatement insertStmt = this.con.prepareStatement(insertTransaction)) {
            insertStmt.setInt(1, this.userId);
            insertStmt.setString(2, lastTransaction.getTransactionId());
            insertStmt.setString(3, lastTransaction.getSenderName());
            insertStmt.setString(4, lastTransaction.getReceiverName());
            insertStmt.setDouble(5, lastTransaction.getAmount());
            insertStmt.setDate(6, new java.sql.Date(lastTransaction.getDateOfTransaction().getTime()));
            insertStmt.setString(7, lastTransaction.getCategory());
            insertStmt.setString(8,lastTransaction.getTime() ); // Setting the timeOfTransaction field
            
            insertStmt.execute();
        }
        
        // Deduct processing fees from user
        String updateSenderBalance = "UPDATE account.account SET Balance = (Balance - ?) WHERE userId = ?";
            PreparedStatement updateSender = this.con.prepareStatement(updateSenderBalance);
            updateSender.setDouble(1, this.processingFee);
            updateSender.setInt(2, this.userId);
            updateSender.executeUpdate();
            
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

     public void JumpToReceipt(ActionEvent event) throws IOException{
        
        if (lastTransaction != null) {
        // Generate and save the receipt
        String receipt = lastTransaction.generateConversionReceipt(this.balance,this.processingFee);

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
    void moveToCurrency(MouseEvent event) {
    currencyBox.setStyle("-fx-background-color: #FFFFFF;");
    labelCurrency.setTextFill(Color.web("#000000"));
    iconCurrency.setFill(Color.web("#000000"));
    
    
}
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
    public void LogOut(MouseEvent event) throws IOException {
    
        //Switch to Scene Register
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void JumpToTransaction(MouseEvent event) throws IOException {
    
        //Switch to Scene Register
         FXMLLoader loader = new FXMLLoader(getClass().getResource("Search.fxml"));
         Parent root = loader.load();
         SearchController SC = loader.getController();
         SC.setUsernameId(this.loginname,this.userId);
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
