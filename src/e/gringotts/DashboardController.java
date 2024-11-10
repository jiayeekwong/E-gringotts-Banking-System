
package e.gringotts;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import javafx.scene.layout.HBox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.util.Callback;
import jdk.dynalink.Operation;



public class DashboardController extends Account implements Initializable {
//FXML component
    @FXML
    private Label TIMEDATE;
    @FXML
    private HBox CARD1;
    @FXML
    private HBox CARD2;
    @FXML
    private HBox CARD3;
    @FXML
    private HBox CARD4;
    @FXML
    private Label ACCOUNTNO1;
    @FXML
    private Label LABEL1;
    @FXML
    private Label ACCOUNTNO2;
    @FXML
    private Label LABEL2;
    @FXML
    private Label ACCOUNTNO3;
    @FXML
    private Label LABEL3;
    @FXML
    private Label ACCOUNTNO4;
    @FXML
    private Label LABEL4;
    @FXML
    private TableView<Transaction> tableTransaction;
    @FXML
    private TableColumn<Transaction, String> colSender;
    @FXML
    private TableColumn<Transaction, String> colReceiver;
    @FXML
    private TableColumn<Transaction, Double> colAmount;
    @FXML
    private TableColumn<Transaction, Date> colDate;
    @FXML
    private Button Addcard;
    @FXML
    private TableColumn<Transaction, String> colCategory;
    Alert alertError = new Alert(Alert.AlertType.ERROR);
    Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
    Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
    @FXML
    private GridPane AddExistance;
    private ArrayList<String> cardNo = new ArrayList<>();
    private ArrayList<java.sql.Date> expiry = new ArrayList<>();
    private ObservableList<Transaction> transactions= FXCollections.observableArrayList();
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
    private FontAwesomeIconView iconLogout;
    @FXML
    private Label labelLogout;
    @FXML
    private HBox logoutBox;
    @FXML
    private Label showBalance;
    
     //Data
    private String loginname;
    private int userId;
    Card cardUser = new Card();
    
    
   public void setUsernameId(String user,int Id) throws SQLException{
       this.loginname = user;
       this.userId = Id;
       this.cardNo = cardUser.ShowCardNo(Id);
       this.expiry = cardUser.ShowCardExpiry(Id);
       showBalance.setText(String.valueOf(getBalancefromDB(this.userId))); //Show balance in dashboard
       int count = cardNo.size();
       //To display card according to db
            for(int i =0;i<count;i++){
           switch (i) {
               case 0:{
                   ACCOUNTNO1.setText(cardNo.get(i));
                   LABEL1.setText(expiry.get(i).toString());
                   CARD1.setVisible(true);
                   break;}
               case 1:{
                   ACCOUNTNO2.setText(cardNo.get(i));
                   LABEL2.setText(expiry.get(i).toString());
                   CARD2.setVisible(true);
                   break;}
               case 2:{
                   ACCOUNTNO3.setText(cardNo.get(i));
                   LABEL3.setText(expiry.get(i).toString());
                   CARD3.setVisible(true);
                   break;}
               case 3:{
                   ACCOUNTNO4.setText(cardNo.get(i));
                   LABEL4.setText(expiry.get(i).toString());
                   CARD4.setVisible(true);
                   break;}
               default:
                   break;
           }
            }
       getTransactions(); //Method to display last 5 transaction and update
   }
  
   
    @Override
    public void initialize(URL url, ResourceBundle rb){
            CARD4.setVisible(false);
            CARD3.setVisible(false);
            CARD2.setVisible(false);
            CARD1.setVisible(false);
            
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss   yyyy-MM-dd "); //Show time on main dashboard
            String formattedTimestamp = now.format(formatter);
            TIMEDATE.setText(formattedTimestamp);
             try {        
              cardUser.removeExpiredCard(); //Update Card Database to remove any card that already expired
          } catch (SQLException ex) {
                 System.out.println("Database Error");
          }
       
               
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
         UserProfileController upc = new UserProfileController();
         upc.setUsernameId(loginname, userId);
         FXMLLoader loader = new FXMLLoader(getClass().getResource("UserProfile.fxml"));
         loader.setController(upc);
         Parent root = loader.load();
       
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void LogOut(MouseEvent event) throws IOException {
        //Switch to Scene login page
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void StoreCardInfo(Card E) throws SQLException{
        //Store card info into database
        PreparedStatement PS = this.con.prepareStatement("INSERT INTO account.card (userId,cVV,ExpiryDate,cardNo) VALUES (?,?,?,?) ");
        PS.setInt(1, this.userId);
        PS.setInt(2, E.getCVV());
        PS.setDate(3, java.sql.Date.valueOf(E.getExpiredDate()));
        PS.setString(4, E.getCardNo());
        PS.execute();
    }
    
    public Card generateNewCard(){
      //  generate and store into card class
        cardUser.GenerateCVV();
        cardUser.GenerateCardNo();
        cardUser.GenerateExpiredDate();
        
        return cardUser;
    }
    
    
    @FXML
   public void Addcard() throws SQLException{
      if(this.cardNo.size()<=3){
           //if card not more than 4
        alertConfirm.setTitle("Add Card Section");
        alertConfirm.setHeaderText("Please Select The Operation Below :");
        alertConfirm.setContentText("All The Information is Confidential :");
        ButtonType buttonNew = new ButtonType("Create New");
        ButtonType buttonExist = new ButtonType("Add Exist Card");
        
        alertConfirm.getButtonTypes().setAll(buttonNew, buttonExist);
        Optional<ButtonType> result = alertConfirm.showAndWait();
        if (result.isPresent() && result.get() == buttonNew) {
            // User want to create a new card
                StoreCardInfo(generateNewCard());
                informationAlert.setTitle("Card Information");
                informationAlert.setHeaderText("Success");
                informationAlert.setContentText(cardUser.toString());
                informationAlert.showAndWait();
                this.cardNo = cardUser.ShowCardNo(this.userId);
                this.expiry = cardUser.ShowCardExpiry(this.userId);
                RefreshCard(this.cardNo);
                
        } else {
            // User already have a card and want to add in his app
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Card Dialog");
            dialog.setHeaderText("Enter Your Information");
            // Create input controls for user information
            TextField cardNo = new TextField();
            cardNo.setPromptText("Card Number");
            TextField cVV = new TextField();
            cVV.setPromptText("cVV");
            TextField expiredDate = new TextField();
            expiredDate.setPromptText("Expiry Date");
            AddExistance.add(new Label("Card Number: (include -)"), 0, 0);
            AddExistance.add(cardNo, 1, 0);
            AddExistance.add(new Label("cVV:"), 0, 1);
            AddExistance.add(cVV, 1, 1);
            AddExistance.add(new Label("Expiry Date:"), 0, 2);
            AddExistance.add(expiredDate, 1, 2);
            dialog.getDialogPane().setContent(AddExistance);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            //Set Function according to user input
             dialog.setResultConverter(new Callback<ButtonType, ButtonType>() {
           
            public ButtonType call(ButtonType dialogButton) {
                if (dialogButton == ButtonType.OK) {
                    // User clicked OK
                    cardUser.SetCardNo(cardNo.getText());
                    cardUser.setCvv(Integer.parseInt(cVV.getText()));
                    cardUser.setExpiry(LocalDate.parse(expiredDate.getText()));
                     try {
                        StoreCardInfo(cardUser);
                    } catch (SQLException ex) {
                        alertError.setTitle("Error!");
                        alertError.setContentText("Database Connection Error.Adding Existence Card Failed.");
                    }
                    informationAlert.setTitle("Card Information");
                    informationAlert.setHeaderText("Success");
                    informationAlert.setContentText(cardUser.toString());
                    informationAlert.showAndWait();
                    
                    

                    } else {
                        // User cancelled adding the card, button 'cancel' clicked
                        informationAlert.setTitle("Card Information");
                        informationAlert.setHeaderText("Cancelled");
                    }  
                return null;
            }
        });
         
         dialog.showAndWait();
         this.cardNo = cardUser.ShowCardNo(this.userId);
         this.expiry = cardUser.ShowCardExpiry(this.userId);
         RefreshCard(this.cardNo);
        }
        
   }else{
         //POPs out error dialog: Card number exceed 
          alertError.setTitle("Error");
          alertError.setHeaderText("System Error : Maximum Card Reach");
          alertError.setContentText("User already have 4 Cards which is the Limit of Card Per User");
          alertError.showAndWait();
            
             }  
      
   }
   
   public void RefreshCard(ArrayList<String> cardNo){
       //Refresh the dahshboard once user add a new card
       int count = cardNo.size();
       for(int i =0;i<count;i++){
                if(i==0){
                ACCOUNTNO1.setText(cardNo.get(i));
                LABEL1.setText(expiry.get(i).toString());
                CARD1.setVisible(true);
                }
                else if(i==1){
                    ACCOUNTNO2.setText(cardNo.get(i));
                    LABEL2.setText(expiry.get(i).toString());
                    CARD2.setVisible(true);
                }
                else if(i==2){
                     ACCOUNTNO3.setText(cardNo.get(i));
                     LABEL3.setText(expiry.get(i).toString());
                     CARD3.setVisible(true);
                }
                else if(i==3){
                     ACCOUNTNO4.setText(cardNo.get(i));
                     LABEL4.setText(expiry.get(i).toString());
                     CARD4.setVisible(true);
                }
       }
     }
   
  //For Latest 5 transaction history Money in and out to user's account
  //For transaction history
   public void getTransactions()throws SQLException{
        ObservableList<Transaction> transactions= FXCollections.observableArrayList();
        //Either sender name or receiver name will be user
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
                transactions.add(tr);
            }
            //All info is stored 
            tableTransaction.setItems(transactions);
        
        colSender.setCellValueFactory(new PropertyValueFactory<Transaction,String>("senderName"));
        colReceiver.setCellValueFactory(new PropertyValueFactory<Transaction,String>("receiverName"));
        colAmount.setCellValueFactory(new PropertyValueFactory<Transaction,Double>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<Transaction,Date>("dateOfTransaction"));
        colCategory.setCellValueFactory(new PropertyValueFactory<Transaction,String>("category"));
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
    }
   
    void moveToHome(MouseEvent event) {
    homebox.setStyle("-fx-background-color: #FFFFFF;");
    labelHome.setTextFill(Color.web("#000000"));
    iconHome.setFill(Color.web("#000000"));
    
}
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
    void leaveHistory(MouseEvent event) {
    historyBox.setStyle("-fx-background-color: #000000;");
    labelHistory.setTextFill(Color.web("#FFFFFF"));
    iconHistory.setFill(Color.web("#FFFFFF"));
    }

    @FXML
    void moveToHistory(MouseEvent event) {
    historyBox.setStyle("-fx-background-color: #FFFFFF;");
    labelHistory.setTextFill(Color.web("#000000"));
    iconHistory.setFill(Color.web("#000000"));
    }
   
   
  
}