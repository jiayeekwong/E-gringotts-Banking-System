
package e.gringotts;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class AdminHomeController implements Initializable {

    @FXML
    private Label TIMEDATE;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss   yyyy-MM-dd ");
            String formattedTimestamp = now.format(formatter);
            TIMEDATE.setText(formattedTimestamp);
    }    

    @FXML
    private void JumpToAllUser(ActionEvent event) throws IOException  {
        
        //Switch to Scene controlUser
       Parent root = FXMLLoader.load(getClass().getResource("AdminControlUser.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void JumpToAllTransaction(ActionEvent event) throws IOException {
        
         //Switch to Scene controlUser
       Parent root = FXMLLoader.load(getClass().getResource("TransactionHistoryAdmin.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    
    @FXML
    public void LogOut(ActionEvent event) throws IOException {
    
        //Switch to Scene Register
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void JumpToAddCurrency(ActionEvent event) throws IOException {
         //Switch to add currency
        Parent root = FXMLLoader.load(getClass().getResource("AddCurrency.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void JumpToDeleteCurrency(ActionEvent event) throws IOException {
        //Switch to delete Currency
        Parent root = FXMLLoader.load(getClass().getResource("deleteCurrency.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void JumpToUpdateCurrency(ActionEvent event) throws IOException {
        //Switch to updatecurrency
        Parent root = FXMLLoader.load(getClass().getResource("UpdateCurrency.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void JumpToShowCurrency(ActionEvent event) throws IOException {
         //Switch to show All currency info
        Parent root = FXMLLoader.load(getClass().getResource("ShowCurrencyAdmin.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

   
}
