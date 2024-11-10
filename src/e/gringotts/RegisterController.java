
package e.gringotts;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;


public class RegisterController extends Account implements Initializable {

    @FXML
    private TextField passwordText1;
    @FXML
    private TextField passwordText2;
    @FXML
    private TextField newUser;
    @FXML
    private PasswordField passwordHidden1;
    @FXML
    private PasswordField passwordHidden2;
    @FXML
    private CheckBox showPassword2;
    @FXML
    private DatePicker Dob;
    @FXML
    private TextField Contact;
    @FXML
    private TextField buildingNo;
    @FXML
    private TextField Street;
    @FXML
    private TextField PostCode;
    @FXML
    private TextField Country;
    @FXML
    private TextField district;
    @FXML
    private TextField State;
    @FXML
    private ChoiceBox<String> chooseTypeBox;
    @FXML
    private Button Exit;
    @FXML
    private TextField PinText2;
    @FXML
    private TextField PinText1;
    @FXML
    private PasswordField HiddenPin1;
    @FXML
    private PasswordField HiddenPin2;
    @FXML
    private CheckBox showPin;
    
    
    //Data
    private String username,password,contact,address;
    private Date date;
    private Label databaseError;
    private int userId; 
    private String userType;
    Alert alert = new Alert(Alert.AlertType.ERROR);
    MagicalUser mu = new MagicalUser();
    

@Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Enable user to choose their userType from choicebox.
            chooseTypeBox.getItems().addAll(mu.DisplayUserTypeChoiceBox());
        } catch (SQLException ex) {
           databaseError.setText("Database Error");
        }   
    }    
    

    @FXML
 public void showPin(ActionEvent event) {
  
            if (showPin.isSelected()) {
                 PinText1.setText(HiddenPin1.getText());
                 PinText2.setText(HiddenPin2.getText());
                PinText1.setVisible(true);
                PinText2.setVisible(true);
                HiddenPin1.setVisible(false);
                HiddenPin2.setVisible(false);

                // Hide the pin again after 1 seconds
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.seconds(1),
                        e -> {
                           
                            PinText1.setVisible(false);
                            PinText2.setVisible(false);
                            HiddenPin1.setVisible(true);
                            HiddenPin2.setVisible(true);
                            showPin.setSelected(false);
                        }));
                timeline.setCycleCount(1);
                timeline.play();
            } else {
                PinText1.setVisible(false);
                PinText2.setVisible(false);
                HiddenPin1.setVisible(true);
                HiddenPin2.setVisible(true);
                 
            }
     
}
 
    @FXML
 public void showPss2(ActionEvent event) {
  
            if (showPassword2.isSelected()) {
                 passwordText1.setText(passwordHidden1.getText());
                 passwordText2.setText(passwordHidden2.getText());
                passwordText1.setVisible(true);
                passwordText1.setManaged(true);
                passwordText2.setVisible(true);
                passwordText2.setManaged(true);
                passwordHidden1.setVisible(false);
                passwordHidden1.setManaged(false);
                passwordHidden2.setVisible(false);
                passwordHidden2.setManaged(false);

                // Hide the password again after 1 seconds
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.seconds(1),
                        e -> {
                           
                            passwordText1.setVisible(false);
                            passwordText1.setManaged(false);
                            passwordText2.setVisible(false);
                            passwordText2.setManaged(false);
                            passwordHidden1.setVisible(true);
                            passwordHidden1.setManaged(true);
                             passwordHidden2.setVisible(true);
                            passwordHidden2.setManaged(true);
                            showPassword2.setSelected(false);
                        }));
                timeline.setCycleCount(1);
                timeline.play();
            } else {
                passwordText1.setVisible(false);
                passwordText1.setManaged(false);
                passwordText2.setVisible(false);
                passwordText2.setManaged(false);
                passwordHidden1.setVisible(true);
                passwordHidden1.setManaged(true);
                passwordHidden2.setVisible(true);
                passwordHidden2.setManaged(true);
                 
            }
     
}
 
    @FXML
    public void Register(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException{
        this.username = newUser.getText();
        String password1 = passwordHidden1.getText();
        this.password = passwordHidden2.getText();
        LocalDate dob = Dob.getValue();
        this.date = (java.sql.Date.valueOf(dob));
        this.contact = Contact.getText();
        this.address = buildingNo.getText()+", "+Street.getText()+", "+PostCode.getText()+", "+district.getText()+", "+State.getText()+", "+Country.getText()+".";
     
        //Check the validity of the username
        if(this.checkUsername(this.username)){
            //Username already exist
             alert.setTitle("Error Dialog");
             alert.setHeaderText(null);
             alert.setContentText("Username "+this.username+" has already been used");
             alert.showAndWait();
             newUser.clear();
        }
        //check format of contact
        else if(!checkContact(contact)){
            alert.setTitle("Error Dialog");
             alert.setHeaderText(null);
             alert.setContentText("Contact should contains Numbers Only");
             alert.showAndWait();
             Contact.clear();
          }
        else if(getAllContactfromDB().contains(contact)){
            //Contact already exist
             alert.setTitle("Error Dialog");
             alert.setHeaderText(null);
             alert.setContentText("Contact Has Already Been Used");
             alert.showAndWait();
             Contact.clear();
        }
        else if(!checkValidPin(this.HiddenPin1.getText())){
            //Pin not valid
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Invalid Pin");
            alert.setContentText("Pin should contains only 6 digit number");
            alert.showAndWait();
            ClearPin();
        }
        
        else if(!this.HiddenPin1.getText().equals(this.HiddenPin2.getText())){
            //Pin type in different
            alert.setTitle("Error Dialog");
             alert.setHeaderText(null);
             alert.setContentText("Please Use The Same Pin!");
             alert.showAndWait();
            ClearPin();
        }
      
        //if the user type in different password
        else if(!password1.equals(this.password)){
             //Confirm Password type in correctly
             alert.setTitle("Error Dialog");
             alert.setHeaderText(null);
             alert.setContentText("Please Use The Same Password!");
             alert.showAndWait();
             ClearPassword();
       
        }
        
        
            //if password do not meet requirement
        else if(!checkValid(this.password)){
             alert.setTitle("Error Dialog");
             alert.setHeaderText(null);
             alert.setContentText("Password length must be 8 contains Uppercase,Lowercase and Numbers");
             alert.showAndWait();
              ClearPassword();
            }
                //If all the requirements meet by user
                  else{
                        if(chooseTypeBox.getValue().equals("Goblin")){
                            //User choose Goblin as userType, need extra password from bank management
                                    TextInputDialog dialog = new TextInputDialog();
                                    dialog.setTitle("Magical User Type : Goblin");
                                    dialog.setHeaderText("To Verify Your Identity :");
                                    dialog.setContentText("Enter Password:");
                                   // Process the result
                                   Optional<String> result = dialog.showAndWait();
                                   if(result.isPresent() && !result.get().isEmpty()){
                                       //user click ok
                                       String password = result.get();
                                       PreparedStatement PS = con.prepareStatement("SELECT access FROM account.useraccess where UserType = 'Goblin' ");
                                       ResultSet RS = PS.executeQuery();
                                       if(RS.next()){
                                           String realPss = RS.getString("access");
                                           if(realPss.equals(password)){
                                                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                                                informationAlert.setTitle("Information ");
                                                informationAlert.setHeaderText("Identity Verified");
                                                informationAlert.setContentText("Password Correct");
                                                informationAlert.showAndWait();
                                           }
                                       }else{
                                           System.out.println("Error with Database");
                                       }
                                    }
                                   else{
                                       Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                                                informationAlert.setTitle("Information ");
                                                informationAlert.setHeaderText("Verification Cancelled");
                                                informationAlert.setContentText("Please Choose Your Usertype again");
                                                informationAlert.showAndWait();
                                       chooseTypeBox.setValue(null);
                                       return;
                                   }
                         }
                        
                          if(chooseTypeBox.getValue().equals("Others")){
                                   //User choose Others as userType
                                    TextInputDialog dialog = new TextInputDialog();
                                    dialog.setTitle("Magical User Type : Others");
                                   dialog.setHeaderText("Enter The New Magical User :");
                                   dialog.setContentText("Magical User:");
                                   // Process the result
                                   Optional<String> result = dialog.showAndWait();
                                   if(result.isPresent() && !result.get().isEmpty()){
                                       //user click ok
                                       String newTypeUser = result.get();

                                        if(!mu.getAllUserTypeFromDB().contains(newTypeUser)){
                                            //User Type do not exist
                                            chooseTypeBox.setValue(null); //Clear the user choice and allow them to choose again with the new type exist in choice box
                                            mu.addType(newTypeUser);
                                            chooseTypeBox.getItems().clear();
                                            chooseTypeBox.getItems().addAll(mu.getAllUserTypeFromDB());

                                        }
                                        else{
                                         Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                                        informationAlert.setTitle("Information ");
                                       informationAlert.setHeaderText("Update Cancelled");
                                       informationAlert.setContentText("User Type ALready Exist");
                                       informationAlert.showAndWait();
                                       return;
                                        }

                                   }
                                   else{
                                       //User Click Cancel
                                       Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                                      informationAlert.setTitle("Information ");
                                       informationAlert.setHeaderText("Update Cancelled");
                                       informationAlert.setContentText("No New User Type Added");
                                       informationAlert.showAndWait();
                                       return;

                                   }
                       }
                    int Id=0,latestId=0,newId=0;
                    ArrayList NewId = new ArrayList<Integer>();
                    //To get latest new Id assign to new user
                    PreparedStatement PS = con.prepareStatement("SELECT userId FROM account.account ");
                    ResultSet rs=PS.executeQuery();
                    while(rs.next()){
                        Id = rs.getInt("userId");
                        NewId.add(Id);
                    }
                    
                    for(int i=0; i<NewId.size();i++){
                        latestId = (int)NewId.get(i);
                    }
                    
                    //new Id for new User
                    newId = latestId+1;
                    this.userId = newId;
                   
                    //Extra Features: Password Salting
                    // Generate a random salt       
                    byte[] salt = this.generateSalt();
                    
                    // Hash the password with the salt
                    String hashedPassword = this.hashPassword(this.password, salt);
                    //Hash pin
                    String hashedPin = this.hashPin(this.HiddenPin1.getText());
                    this.userType = chooseTypeBox.getValue();
                    //Insert New User to database
                    PreparedStatement PS2 = con.prepareStatement("INSERT INTO account.account (userId,username,password,DOB,address,contact,userType,Balance,salt,pin) VALUES (?,?,?,?,?,?,?,?,?,?) ");
                    PS2.setInt(1, this.userId); //userId generated by system
                    PS2.setString(2, this.username);
                    PS2.setString(3, hashedPassword);
                    PS2.setDate(4, (java.sql.Date) this.date);
                    PS2.setString(5, this.address);
                    PS2.setString(6, this.contact);
                    PS2.setString(7, this.userType);
                    PS2.setDouble(8,100000);
                    PS2.setBytes(9, salt);
                    PS2.setString(10, this.HiddenPin1.getText());
                    PS2.execute();
           
                  Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                  alert.setTitle("Confirmation Dialog");
                  alert.setHeaderText(null);
                  alert.setContentText("Registration Success");
                  Optional<ButtonType> result = alert.showAndWait();
                  if (result.isPresent() && result.get() == ButtonType.OK) {
                        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                  } else {
                        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                  }
                
               
          }
        
            
       
    }
    
    //Check Validity of Password Entered By user
      private boolean checkValid(String password){
         //password must contain at least 8 character with Uppercase and Lowercase letter and number
         boolean length = true;
         char[] specialCharacters = "!@#$%^&*()_+{}[]|\"<>,.?/~".toCharArray();
         char[]ch = password.toCharArray();
         if(ch.length<7){
             length = false;
             
         }
         boolean containSpecialLetter = false;
         boolean containDigit = false;
         boolean containUCase = false;
         boolean containLCase = false;

         for(int i=0; i<ch.length;i++){
              
             if(Character.isDigit(ch[i])){
                 containDigit = true;
             }
             if(Character.isUpperCase(ch[i])){
                 containUCase = true;
             }
             if(Character.isLowerCase(ch[i])){
                 containLCase = true;
             }
             for(int j=0;j<specialCharacters.length;j++){
                 if(ch[i]==specialCharacters[j]){
                     containSpecialLetter = true;
                 }
             }
              
             }
         
        return length && containSpecialLetter && containDigit && containUCase && containLCase;
                 
     }   

    //Check Whether contact entred is number
     public boolean checkContact(String contact){
         char [] check = contact.toCharArray();
         for(int i =0;i<contact.length();i++){
            if(Character.isAlphabetic(check[i])){
                return false;
            }
         }
         return true;
     }
    
     
      
      @FXML
    public void JumpToLoginPage(ActionEvent event) throws IOException {
        //Switch to Scene Loginpage
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        
    }
    
    public boolean checkValidPin(String pin) {
    if (pin.length() != 6) {
        return false;
    }

    for (char c : pin.toCharArray()) {
        if (!Character.isDigit(c)) {
            return false;
        }
    }

    return true;
}

    // Method to generate a random salt
    public static byte[] generateSalt() {       
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];        
        random.nextBytes(salt);
        return salt;
    }

   
    
    // Method to hash pin to salt
    //Return hashpassword
    public String hashPin(String pin) {        
        try {
            // Create a MessageDigest instance for SHA-256            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            md.update(pin.getBytes());
            // Get the hash's bytes 
            byte[] bytes = md.digest(); 
            // Convert bytes to hexadecimal format
            StringBuilder sb = new StringBuilder();            
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));            
            }
            
            return sb.toString();        
        } catch (NoSuchAlgorithmException e) {
            alert.setTitle("Error");
            alert.setHeaderText("Password Error");
            alert.setContentText("Hash Password Cannot be Produced");        }
        
        return null;    
    }
    
    //MEthod to clear password if password's error exist
    public void ClearPassword(){
        passwordHidden1.clear();
             passwordHidden2.clear();
             passwordText1.clear();
             passwordText2.clear();
    }
    
    //Method to clear pin if pin's error exist
    public void ClearPin(){
        HiddenPin1.clear();
            HiddenPin2.clear();
            PinText1.clear();
            PinText2.clear();
    }
}