
package e.gringotts;

import java.io.IOException;
import java.sql.Connection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static javafx.scene.media.MediaPlayer.Status.PLAYING;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Account <T extends String> implements Initializable {
    //Controller for login page
    protected Connection con;
    @FXML
    private TextField passwordHidden;
    @FXML
    private CheckBox showPss;
    @FXML
    private TextField passwordText;
    @FXML
    private Label label;
    @FXML
    private Button Loginbutton;
    @FXML
    private MediaView VideoPlayer;
    @FXML
    private Label description;
    protected MediaPlayer mediaplayer;
    @FXML
    private TextField Username;
    @FXML
    private Button regBtn;
    
    //data
    private int userId;
    private String username,contact,userType;
    private String password;
    private Date DOB;
    private String address;
    private Double balance;
    private String pin;
    
    Alert alert = new Alert(Alert.AlertType.ERROR);
    
    public Account(){
        //get connection from database
        Database db = new Database();
        this.con = db.getConnection(); 
       
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //To play the video at first of the login page
      String URL = "file:/C://Users//jiaye//OneDrive//Documents//NetBeansProjects//E-Gringiotts//src//e//gringotts//Description.mp4";
      Media media = new Media(URL);
      mediaplayer = new MediaPlayer(media);
      VideoPlayer.setMediaPlayer(mediaplayer);
      mediaplayer.setCycleCount(MediaPlayer.INDEFINITE);
      mediaplayer.play();
      
     //This is the description text that display at the loginpage
      description.setText("\t E-Gringotts: Your Wizarding World Bank App\n" +
"\t Welcome to E-Gringotts, the premier banking app designed for wizards, witches, and magical beings alike! "+
"\tInspired by the legendary Gringotts Wizarding Bank in Diagon Alley, E-Gringotts brings the security, trust, and magic of the wizarding world’s most famous bank to your fingertips. "
+ "\tWhether you are a Hogwarts student, a Ministry official, or a magical creature managing your galleons, sickles, and knuts has never been easier.\n" +
"\n" +
"\tKey Features:\n" +
"\tSecure Vault Access: Just like the vaults deep beneath Gringotts, our app ensures your magical currency and treasures are protected by the highest security spells and enchantments.\n" +
"\tSeamless Transactions: Effortlessly transfer funds between your accounts, pay for your Daily Prophet subscription, or send money to friends at Hogwarts.\n" +
"\tCurrency Conversion: Instantly convert between Galleons, Sickles, and Knuts, or even into Muggle money for those rare trips to the non-magical world.\n" +
"\tInvestment Opportunities: Grow your wealth by investing in the wizarding world's most promising ventures, from broomstick companies to potion enterprises.\n" +
"\tExpense Tracking: Keep track of your spending on wands, robes, and magical creatures with our intuitive expense tracker.");
      description.setWrapText(true);
      
    }    
    
    
    @FXML
    public void login(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException {
        //get data from user input
         this.username = Username.getText();
         String pss = passwordHidden.getText();
 
         //check textfield not blank
         if(!this.username.isBlank() && !pss.isBlank()){
                //check account exist or not
               if(checkUsername(this.username)){
                     //Get theuserid according to username
                      this.userId = getUserId(this.username);
                    
                   if(checkPassword(pss)){
                        //If correct password with database
                        if(!getUserTypefromDB().equals("Goblin")){
                              //If user are not admin @ Goblin
                              this.mediaplayer.stop();
                                 //Jump to user Main Dashboard
                                  FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                                  Parent root = loader.load();
                                  DashboardController DC = loader.getController();
                                  DC.setUsernameId(this.username,this.userId);
                                  Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                                  Scene scene = new Scene(root);
                                  stage.setScene(scene);
                                  stage.show();

                             }
                        else{ 
                                //User sign in as Admin
                            this.mediaplayer.stop();
                                 //Jump to Admin Control Main Page
                                  FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminHome.fxml"));
                                  Parent root = loader.load();
                                  Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                                  Scene scene = new Scene(root);
                                  stage.setScene(scene);
                                  stage.show();     
                        }


                   }
                   else{
                       //Wrong password
                       alert.setTitle("Error Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Your password Wrong!");
                        alert.showAndWait();
                        passwordHidden.clear();
                        passwordText.clear();
                   }
               }
                    //Account not exist   
               else if(!checkUsername(this.username)){
                    alert.setTitle("Error Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Wrong Username!");
                        alert.showAndWait();
                        Username.clear();
                 }
            } 
             //if information incomplete
         else{
              alert.setTitle("Error Dialog");
                        alert.setHeaderText(null);
                        alert.setContentText("Information Incomplete");
                        alert.showAndWait();
         }
         
        
         
    }

    @FXML
    public void JumpToSceneRegister(ActionEvent event) throws IOException {
        //Switch to Scene Register
        this.mediaplayer.stop();
        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
    }
    
    
    public boolean checkUsername(String user) throws SQLException{
        //to check the username already exist or not
        PreparedStatement PS = this.con.prepareStatement("SELECT username FROM account.account WHERE BINARY username= '"+user+"'");
        ResultSet rs=PS.executeQuery();
      return rs.next();
    }
    
     
     public boolean checkPassword(String pss) throws SQLException, NoSuchAlgorithmException{
         String UserInputPss = "";  //password key in when login
          String originalPss = "";  //password retrieve from database
        PreparedStatement PS = con.prepareStatement("SELECT * FROM account.account WHERE BINARY username =?");
        PS.setString(1, this.username);
        ResultSet rs=PS.executeQuery();
        if(rs.next()){
            byte[] salt = rs.getBytes("salt"); //get the salt 
            UserInputPss = hashPassword(pss,salt); //hash with the password user enter
            originalPss = rs.getString("password"); //Get orgiginal password from database
            
        }
        return UserInputPss.equals(originalPss); //compare passsword from database and the password key in

    }
     
    
    @FXML
     public void showPss(){
        //Function enable user to see their password when typing
         if(!showPss.isSelected()){
             passwordHidden.setText(passwordText.getText());
             passwordText.setVisible(false);
             passwordHidden.setVisible(true);
            
         }
         else{
             
             passwordHidden.setVisible(false);
             passwordText.setText(passwordHidden.getText());
             passwordText.setVisible(true);
             // Hide the password again after 1 seconds
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.seconds(1),
                        e -> {
                            passwordText.setVisible(false);                            
                            passwordHidden.setVisible(true);             
                            showPss.setSelected(false);
                        }));
                timeline.setCycleCount(1);
                timeline.play();
         }
     }

  
    
    
     
     public int getUserId(String user) throws SQLException {
         //Get user Id from database
       int userId ;
       PreparedStatement PS = this.con.prepareStatement("SELECT * FROM account.account WHERE BINARY username = ?");
       PS.setString(1,user);
       ResultSet RS = PS.executeQuery();
      RS.next();
        userId = RS.getInt("userId");
         return userId;
     }
     
     //Getter and Setter Method
     public String getUsername(){
         return this.username;
        }
     public int getUserId(){
         return this.userId;
     }
     public String getPassword(){
         return this.password;
     }

    public String getContact() {
        return this.contact;
    }

    public String getUserType() {
        return this.userType;
    }

    public Date getDOB() {
        return this.DOB;
    }

    public String getAddress() {
        return this.address;
    }

    public Double getBalance() {
        return this.balance;
    }

    public String getPin() {
        return this.pin;
    }
     
     public  void setUsername(String username) {
        this.username = username;
    }
   public void setContact(String contact){
       this.contact = contact;
   }
    public void setUserType(String usertype){
       this.userType = usertype;
   }
    public void setUserId(int userId){
        this.userId = userId;
    }
    
    public void setPassword(String password){
        this.password = password;
    }

    public void setDOB(Date dob) {
        this.DOB = dob;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
     
    
    
    public String getContactfromDB() throws SQLException {
        //Get user's contact from db
         PreparedStatement PS = this.con.prepareStatement("SELECT * FROM account.account WHERE BINARY username = ?");
       PS.setString(1,this.username);
       ResultSet RS = PS.executeQuery();
        if(RS.next()){
              this.contact = RS.getString("contact");
        }
        else{
            System.out.println("No Contact Found");
        }
     
         return this.contact;
    }
    
     
    public ArrayList<String> getAllContactfromDB() throws SQLException {
        //Get all contact number from db
        ArrayList<String> all = new ArrayList<String>();
         PreparedStatement PS = this.con.prepareStatement("SELECT * FROM account.account");
       ResultSet RS = PS.executeQuery();
       while(RS.next()){
           all.add(RS.getString("contact"));
       }
      return all;
    }

    public String getUserTypefromDB() throws SQLException  {
        //Get user's userType from db
        PreparedStatement PS = this.con.prepareStatement("SELECT userType FROM account.account WHERE BINARY username = ?");
       PS.setString(1,this.username);
       ResultSet RS = PS.executeQuery();
       if(RS.next()){
            this.userType = RS.getString("userType");
       } 
         return this.userType;
    }
    
   public Double getBalancefromDB(int id) throws SQLException{
       //Get user's balance from db
      double balance=0.0;
      PreparedStatement PS = this.con.prepareStatement("SELECT Balance FROM account.account WHERE BINARY userId = ? ");
       PS.setInt(1,id);
       ResultSet RS = PS.executeQuery();
       if(RS.next()){
           balance = RS.getDouble("Balance");
       }
       else{
           System.out.println("Cannot connect with database");
       }
         return balance;
    }
   
   public String getAddressfromDB() throws SQLException  {
       //Get user's address from db
        PreparedStatement PS = this.con.prepareStatement("SELECT address FROM account.account WHERE BINARY username = ?");
       PS.setString(1,this.username);
       ResultSet RS = PS.executeQuery();
       RS.next();
       this.address = RS.getString("address");
         return this.address;
    }
     

  
   // Method to hash the password with salt
   //Return hashpassword
    public String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {        
            // Create a MessageDigest instance for SHA-256 algorithm           
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Add salt bytes to the digest
            md.update(salt);
            // Get the hash's bytes            
            byte[] bytes = md.digest(password.getBytes());
            // Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();            
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));  // 0 = zero padded ; 2 = 2 characters ; x = lowercase hexadecimal
            }
            
            return sb.toString();        
        } 
        
           
    

     
   
}
