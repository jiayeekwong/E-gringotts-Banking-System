
package e.gringotts;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ReceiptController extends Account implements Initializable{

    @FXML
    private TextArea receipt1;
    @FXML
    private Button exitreceipt;
    @FXML
    private ImageView backgroundImageView;
    
    private String loginname;
    private int userId;
    private ObservableList<Transaction> transactions= FXCollections.observableArrayList();
   
     public void setUsernameId(String user,int Id){
       this.loginname = user;
       this.userId = Id;
     }
     
     public void setTransactionList(ObservableList<Transaction> t){
         this.transactions = t;
     }
     
     
     @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
        
    // Method to set the receipt text
    public void setReceiptText(String receipt) {
        if (receipt1 != null) {
            receipt1.setText(receipt);
        } else {
            System.out.println("receipt1 is null");
        }
    }
    
    @FXML
           public void JumpToDashboard() throws SQLException {
        //Switch to Scene dashboard
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
                        Parent root = loader.load();
                        DashboardController DC = loader.getController();
                        getTransactions();
                        DC.setUsernameId(this.loginname,this.userId); 
                        Stage stage = (Stage)exitreceipt.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
           
       }catch(IOException e){
           e.printStackTrace();
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
}
