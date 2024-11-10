
package e.gringotts;

import java.io.IOException;
import javafx.fxml.FXML;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;



public class AddCurrencyController implements Initializable {
    private Label resultLabel;
    @FXML
    TextField newCurrencyTF;
    @FXML
    TextField realmTF;
    @FXML
    private Label addNewCurrencyLabel;
    @FXML
    private Label NewCurrencyLabel;
    @FXML
    private Button btnAdd;
    @FXML
    private Label realmLabel;
    
    public void initialize(URL url, ResourceBundle rb) {
    
    
    }    
            
    @FXML
    public void btnAddClicked (ActionEvent event) throws ClassNotFoundException, SQLException{
        currencyConversion c = new currencyConversion();
        boolean add = c.addCurrency(realmTF.getText(), newCurrencyTF.getText());
        
        if (add){
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Add Successfully");
                alert.showAndWait();
        }
           
        else{
             Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Curency Failed");
                alert.setHeaderText("Please Try Again");
                alert.showAndWait();
        }
            
    }

    @FXML
    private void JumpToMenu(ActionEvent event) throws IOException {
          //Switch to Home
       Parent root = FXMLLoader.load(getClass().getResource("AdminHome.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
            
            
}
