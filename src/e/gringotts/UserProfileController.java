package e.gringotts;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.scene.control.Alert;



public class UserProfileController  extends Account implements Initializable {
    
    //DATA
    private String loginname;
    private int userId;
    private String userType;
    PreparedStatement PS ;
    ResultSet RS;
    
    //FXML Componenet
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
    @FXML
    private ImageView ProfilePic;
    @FXML
    private Button updateName;
    @FXML
    private Button updateContact;
    @FXML
    private Button updateAddress;
    @FXML
    private Button updateUserType;
    @FXML
    private Label labelContact;
    @FXML
    private Label labelUsername;
    @FXML
    private Label labelAddress;
    @FXML
    private Label labelUserType;
    @FXML
    private GridPane UpdateBox;
    
   
    Alert alertError = new Alert(Alert.AlertType.ERROR);
    
        
    public void setUsernameId(String name, int id){
        this.loginname = name;
        this.userId = id;
       this.setUsername(this.loginname);
        try {
            this.userType = this.getUserTypefromDB();
        } catch (SQLException e) {
            System.out.println("Error database");
        }
        
    }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserAvatar UA = new UserAvatar();
        ProfilePic.setImage(UA.getImage(this.userId));  //get user profile
        labelUsername.setText(this.loginname);
        try {
            labelContact.setText(this.getContactfromDB());
            labelAddress.setText(this.getAddressfromDB());
            labelUserType.setText(this.getUserTypefromDB());
        } catch (SQLException ex) {
            System.out.println("Database Error");
        }
       
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
    void moveToInsight(MouseEvent event) {
    insightBox.setStyle("-fx-background-color: #FFFFFF;");
    labelInsight.setTextFill(Color.web("#000000"));
    iconInsight.setFill(Color.web("#000000"));
    
}
    void leaveInsight(MouseEvent event) {
    insightBox.setStyle("-fx-background-color: #000000;");
    labelInsight.setTextFill(Color.web("#FFFFFF"));
    iconInsight.setFill(Color.web("#FFFFFF"));
    
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

    @FXML
    private void JumpToTransaction(MouseEvent event) throws IOException {
        
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
    private void updateName(ActionEvent event) throws SQLException {
         TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Update Username");
            dialog.setHeaderText("Enter your new username:");
            dialog.setContentText("Username:");
            // Process the result
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent() && !result.get().isEmpty()){
                    //User click ok
                    String newUsername = result.get();
                    if(!this.checkUsername(newUsername)){
                        //Username doesn't repeat with others
                        Update(newUsername,"username");
                        labelUsername.setText(newUsername); // Refresh label
                    }
                    else{
                         //Username already used
                          alertError.setTitle("Error");
                            alertError.setHeaderText("Update Failed");
                            alertError.setContentText("Username already been Used.Please use another.");
                            alertError.showAndWait();
            
                    }
                
            }
            else{
                //User Click Cancel
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
               informationAlert.setTitle("Information ");
                informationAlert.setHeaderText("Update Cancelled");
                informationAlert.setContentText("Username Remain Unchanged");
                informationAlert.showAndWait();
            }
    }

    @FXML
    private void updateContact(ActionEvent event) throws SQLException {
         TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Contact Number");
            dialog.setHeaderText("Enter your new contact number:");
            dialog.setContentText("Contact Number:");
            // Process the result
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent() && !result.get().isEmpty()){
                //Usr click ok   
                String newContact = result.get();
                 if(!getAllContactfromDB().contains(newContact)){
                     //Contact has not been used
                      Update(newContact,"contact");
                       labelContact.setText(newContact); // Refresh label
                 }
                 else{
                  Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                 informationAlert.setTitle("Information ");
                informationAlert.setHeaderText("Update Cancelled");
                informationAlert.setContentText("Contact ALready Used By Others");
                informationAlert.showAndWait();
                 }
               
            }
            else{
                //User Click Cancel
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
               informationAlert.setTitle("Information ");
                informationAlert.setHeaderText("Update Cancelled");
                informationAlert.setContentText("Contact Remain Unchanged");
                informationAlert.showAndWait();
            }
    }

    @FXML
    private void updateAddress(ActionEvent event) throws SQLException {
         TextInputDialog dialog = new TextInputDialog();
          dialog.setTitle("Update Address");
            dialog.setHeaderText("Enter your new Address:");
            dialog.setContentText("Address:");
            // Process the result
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent() && !result.get().isEmpty()){
                    //User click ok
                    String newAddress = result.get();
                    Update(newAddress,"address");
                   labelAddress.setText(newAddress); // Refresh label
                 
            }
            else{
                //User Click Cancel
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
               informationAlert.setTitle("Information ");
                informationAlert.setHeaderText("Update Cancelled");
                informationAlert.setContentText("Address Remain Unchanged");
                informationAlert.showAndWait();
            }
    }
    

    @FXML
    private void updateUserType(ActionEvent event) throws SQLException {
         TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update userType");
            dialog.setHeaderText("Enter your new userType:");
            dialog.setContentText("userType:");
            // Process the result
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent() && !result.get().isEmpty()){
                    //User click ok
                    String newUserType = result.get();
                    Update(newUserType,"userType");
                     labelUserType.setText(newUserType); // Refresh label
                 
            }
            else{
                //User Click Cancel
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
               informationAlert.setTitle("Information ");
                informationAlert.setHeaderText("Update Cancelled");
                informationAlert.setContentText("User Type Remain Unchanged");
                informationAlert.showAndWait();
            }
    }

    public void Update(String info,String column) throws SQLException{
        //Update into database
        PreparedStatement PS = this.con.prepareStatement("Update account.account set "+column+" = ? where userId = ? ");
        PS.setString(1, info);
        PS.setInt(2, userId);
        PS.executeUpdate();
        if(column.equalsIgnoreCase("username")){
        PreparedStatement PS2 = this.con.prepareStatement("UPDATE account.transaction\n" +
                                            "SET senderName = CASE\n" +
                                            "WHEN senderName = ? THEN ?\n" +
                                            "ELSE senderName\n" +
                                            "END,\n" +
                                            "receiverName = CASE\n" +
                                            "WHEN receiverName = ? THEN ?\n" +
                                            "ELSE receiverName\n" +
                                            "END\n" +
                                            "WHERE senderName = ? OR receiverName = ?;");
        PS2.setString(1, this.loginname);
        PS2.setString(2, info);
        PS2.setString(3, this.loginname);
        PS2.setString(4, info);
        PS2.setString(5, this.loginname);
        PS2.setString(6, this.loginname);
        PS2.executeUpdate();
        }
       
        Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
        informationAlert.setTitle("Information ");
                informationAlert.setHeaderText("Update Success");
                informationAlert.setContentText("New "+column+" :"+info);
                informationAlert.showAndWait();
    }
  
}
