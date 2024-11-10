
package e.gringotts;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



public class AdminControlUserController extends Account implements Initializable  {

    @FXML
    private TextField userid;
    @FXML
    private Button searchuserid;
    @FXML
    private TextField username;
    @FXML
    private Button searchusername;
    @FXML
    private TextField password;
    @FXML
    private Button searchpassword;
    @FXML
    private TextField dob;
    @FXML
    private Button searchdob;
    @FXML
    private TextField address;
    @FXML
    private Button searchaddress;
    @FXML
    private TextField contact;
    @FXML
    private Button searchcontact;
    @FXML
    private TextField usertype;
    @FXML
    private Button searchusertype;
    @FXML
    private TextField balance;
    @FXML
    private Button searchbalance;
    @FXML
    private TextField pin;
    @FXML
    private Button searchpin;
    private ObservableList<Account> account = FXCollections.observableArrayList();
    @FXML
    private TableView<Account> table;
    @FXML
    private TableColumn<Account, Integer> col1;
    @FXML
    private TableColumn<Account, String> col2;
    @FXML
    private TableColumn<Account, String> col3;
    @FXML
    private TableColumn<Account, Date> col4;
    @FXML
    private TableColumn<Account, String> col5;
    @FXML
    private TableColumn<Account, String> col6;
    @FXML
    private TableColumn<Account, String> col7;
    @FXML
    private TableColumn<Account, Double> col8;
    @FXML
    private TableColumn<Account, String> col9;
    @FXML
    private Button exitadmincontroluser;
    @FXML
    private Label NoOfUserLabel;

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showatbeginning(); //show the table contains all user info
            NoOfUserLabel.setText(String.valueOf(TotalUsers())); //Show the total number of user
        } catch (SQLException ex) {
            System.out.println("Error with Database Table Account");
        }
    }    
    
    @FXML
    public  void searchUserId() throws SQLException{
        //Allow Admin To search By userId
        String query = "SELECT * FROM account.account WHERE userId = ?";
    try {
        
        PreparedStatement PS = con.prepareStatement(query);
        PS.setInt(1, Integer.parseInt(userid.getText()));
        ResultSet RS = PS.executeQuery();
        account.clear();
        while (RS.next()) {
            Account tr = new Account();
            tr.setUserId(RS.getInt("userId"));
            tr.setUsername(RS.getString("username"));
            tr.setPassword(RS.getString("password"));
            tr.setDOB((Date)RS.getDate("DOB"));
            tr.setAddress(RS.getString("address"));
            tr.setContact(RS.getString("contact"));
            tr.setUserType(RS.getString("userType"));
            tr.setBalance(RS.getDouble("Balance"));
            tr.setPin(RS.getString("pin"));
            this.account.add(tr);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    showTable();
       
    }
    
    @FXML
    public  void searchUsername()throws SQLException{
        //Allow Admin to search by username
       String query = "SELECT * FROM account.account WHERE username = ?";
    try {
        PreparedStatement PS = con.prepareStatement(query);
        PS.setString(1, username.getText());
        ResultSet RS = PS.executeQuery();
        account.clear();
        while (RS.next()) {
            Account tr = new Account();
            tr.setUserId(RS.getInt("userId"));
            tr.setUsername(RS.getString("username"));
            tr.setPassword(RS.getString("password"));
            tr.setDOB(RS.getDate("DOB"));
            tr.setAddress(RS.getString("address"));
            tr.setContact(RS.getString("contact"));
            tr.setUserType(RS.getString("userType"));
            tr.setBalance(RS.getDouble("Balance"));
            tr.setPin(RS.getString("pin"));
            this.account.add(tr);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    showTable();
        
    }
    
    @FXML
    public  void searchPassword()throws SQLException{
        //Allow admin to search by password
         String query = "SELECT * FROM account.account WHERE password = ?";
    try {
        PreparedStatement PS = con.prepareStatement(query);
        PS.setString(1, password.getText());
        ResultSet RS = PS.executeQuery();
        account.clear();
        while (RS.next()) {
            Account tr = new Account();
            tr.setUserId(RS.getInt("userId"));
            tr.setUsername(RS.getString("username"));
            tr.setPassword(RS.getString("password"));
            tr.setDOB(RS.getDate("DOB"));
            tr.setAddress(RS.getString("address"));
            tr.setContact(RS.getString("contact"));
            tr.setUserType(RS.getString("userType"));
            tr.setBalance(RS.getDouble("Balance"));
            tr.setPin(RS.getString("pin"));
            this.account.add(tr);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    showTable();
        
        
    }
    
    @FXML
    public  void searchDOB()throws SQLException{
        //Allow admin to search by date of birth
         String query = "SELECT * FROM account.account WHERE DOB = ?";
    try {
        PreparedStatement PS = con.prepareStatement(query);
        PS.setString(1, dob.getText());
        ResultSet RS = PS.executeQuery();
        account.clear();
        while (RS.next()) {
            Account tr = new Account();
            tr.setUserId(RS.getInt("userId"));
            tr.setUsername(RS.getString("username"));
            tr.setPassword(RS.getString("password"));
            tr.setDOB(RS.getDate("DOB"));
            tr.setAddress(RS.getString("address"));
            tr.setContact(RS.getString("contact"));
            tr.setUserType(RS.getString("userType"));
            tr.setBalance(RS.getDouble("Balance"));
            tr.setPin(RS.getString("pin"));
            this.account.add(tr);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    showTable();
        
    }
    @FXML
    public  void searchAddress()throws SQLException{
        //Allow admin to search by address
         String query = "SELECT * FROM account.account WHERE address = ?";
    try {
        PreparedStatement PS = con.prepareStatement(query);
        PS.setString(1, address.getText());
        ResultSet RS = PS.executeQuery();
        account.clear();
        while (RS.next()) {
            Account tr = new Account();
            tr.setUserId(RS.getInt("userId"));
            tr.setUsername(RS.getString("username"));
            tr.setPassword(RS.getString("password"));
            tr.setDOB(RS.getDate("DOB"));
            tr.setAddress(RS.getString("address"));
            tr.setContact(RS.getString("contact"));
            tr.setUserType(RS.getString("userType"));
            tr.setBalance(RS.getDouble("Balance"));
            tr.setPin(RS.getString("pin"));
            this.account.add(tr);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    showTable();
        
        
    }
    
    @FXML
    public  void searchContact()throws SQLException{
        //Allow admin to search by contact
          String query = "SELECT * FROM account.account WHERE contact = ?";
    try {
        PreparedStatement PS = con.prepareStatement(query);
        PS.setString(1, contact.getText());
        ResultSet RS = PS.executeQuery();
        account.clear();
        while (RS.next()) {
            Account tr = new Account();
            tr.setUserId(RS.getInt("userId"));
            tr.setUsername(RS.getString("username"));
            tr.setPassword(RS.getString("password"));
            tr.setDOB(RS.getDate("DOB"));
            tr.setAddress(RS.getString("address"));
            tr.setContact(RS.getString("contact"));
            tr.setUserType(RS.getString("userType"));
            tr.setBalance(RS.getDouble("Balance"));
            tr.setPin(RS.getString("pin"));
            this.account.add(tr);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    showTable();
        
    }
    
    @FXML
    public  void searchuserType()throws SQLException{
        //Allow admin to search by usertype
          String query = "SELECT * FROM account.account WHERE userType = ?";
    try {
        PreparedStatement PS = con.prepareStatement(query);
        PS.setString(1, usertype.getText());
        ResultSet RS = PS.executeQuery();
        account.clear();
        while (RS.next()) {
            Account tr = new Account();
            tr.setUserId(RS.getInt("userId"));
            tr.setUsername(RS.getString("username"));
            tr.setPassword(RS.getString("password"));
            tr.setDOB(RS.getDate("DOB"));
            tr.setAddress(RS.getString("address"));
            tr.setContact(RS.getString("contact"));
            tr.setUserType(RS.getString("userType"));
            tr.setBalance(RS.getDouble("Balance"));
            tr.setPin(RS.getString("pin"));
            this.account.add(tr);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    showTable();
        
        
    }
    @FXML
    public  void searchBalance()throws SQLException{
        //Allow admin to search by account balance
          String query = "SELECT * FROM account.account WHERE Balance = ?";
    try {
        PreparedStatement PS = con.prepareStatement(query);
        PS.setDouble(1, Double.parseDouble(balance.getText()));
        ResultSet RS = PS.executeQuery();
        account.clear();
        while (RS.next()) {
            Account tr = new Account();
            tr.setUserId(RS.getInt("userId"));
            tr.setUsername(RS.getString("username"));
            tr.setPassword(RS.getString("password"));
            tr.setDOB(RS.getDate("DOB"));
            tr.setAddress(RS.getString("address"));
            tr.setContact(RS.getString("contact"));
            tr.setUserType(RS.getString("userType"));
            tr.setBalance(RS.getDouble("Balance"));
            tr.setPin(RS.getString("pin"));
            this.account.add(tr);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    showTable();
    }
    
    
    @FXML
    public  void searchPin(){
        //Allow admin to search by transaction pin
          String query = "SELECT * FROM account.account WHERE pin = ?";
    try {
        PreparedStatement PS = con.prepareStatement(query);
        PS.setString(1,pin.getText());
        ResultSet RS = PS.executeQuery();
        account.clear();
        while (RS.next()) {
            Account tr = new Account();
            tr.setUserId(RS.getInt("userId"));
            tr.setUsername(RS.getString("username"));
            tr.setPassword(RS.getString("password"));
            tr.setDOB(RS.getDate("DOB"));
            System.out.println(RS.getDate("DOB"));
            tr.setAddress(RS.getString("address"));
            tr.setContact(RS.getString("contact"));
            tr.setUserType(RS.getString("userType"));
            tr.setBalance(RS.getDouble("Balance"));
            tr.setPin(RS.getString("pin"));
            this.account.add(tr);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    showTable();
        
    }
    
    //Show information taht admin request 
        public void showTable(){
            table.setItems(this.account);
        col1.setCellValueFactory(new PropertyValueFactory<Account,Integer>("userId"));
        col2.setCellValueFactory(new PropertyValueFactory<Account,String>("username"));
        col3.setCellValueFactory(new PropertyValueFactory<Account,String>("password"));
        col4.setCellValueFactory(new PropertyValueFactory<Account,Date>("DOB"));
        col5.setCellValueFactory(new PropertyValueFactory<Account,String>("address"));
        col6.setCellValueFactory(new PropertyValueFactory<Account,String>("contact"));
        col7.setCellValueFactory(new PropertyValueFactory<Account,String>("userType"));
        col8.setCellValueFactory(new PropertyValueFactory<Account,Double>("Balance"));
        col9.setCellValueFactory(new PropertyValueFactory<Account,String>("pin"));
        
        
        }
        
        public  void showatbeginning() throws SQLException{
          //Method to get all user information from db  
        String query = "SELECT * FROM account.account ";
        
        try {
            PreparedStatement PS = this.con.prepareStatement(query);
            ResultSet RS = PS.executeQuery();
            while(RS.next()){
                Account tr = new Account();                
                tr.setUserId(RS.getInt("userId"));
                tr.setUsername(RS.getString("username"));
                tr.setPassword(RS.getString("password"));
                tr.setDOB(RS.getDate("DOB"));
                tr.setAddress(RS.getString("address"));
                tr.setContact(RS.getString("contact"));
                tr.setUserType(RS.getString("userType"));
                tr.setBalance(RS.getDouble("Balance"));
                tr.setPin(RS.getString("pin"));
                this.account.add(tr);
              
            }
             showTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }
        
        @FXML
       public void JumpToMainPage(ActionEvent event) throws IOException{
             //Switch to Home
       Parent root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
      
        }
        
        public int TotalUsers() throws SQLException{
            //Get total numbers of users in db
            int total=0;
            String query = "SELECT MAX(userId) from account.account";
            PreparedStatement PS = this.con.prepareStatement(query);
            ResultSet RS = PS.executeQuery();
            if(RS.next()){
                total = RS.getInt("MAX(userId)");
            }
            else{
                System.out.println("Error with database");
            }
           
            return total;
        }
        
       
       
    }
        
       

