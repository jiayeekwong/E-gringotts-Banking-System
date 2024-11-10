
package e.gringotts;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateCurrencyController implements Initializable {

    @FXML
    private Label addNewCurrencyLabel;
    @FXML
    private Label realmLabel;
    @FXML
    private Label realmLabel1;
    @FXML
    private Label realmLabel11;
    @FXML
    private Label realmLabel111;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
    @FXML
    private Label updatedLabel;
    @FXML
    TextField fromCurrencyTF;
    @FXML
    TextField toCurrencyTF;
    @FXML
    TextField processingFeeTF;
    @FXML
    TextField rateTF;
    
    @FXML
    public void btnUpdateClicked (ActionEvent event) throws ClassNotFoundException, SQLException{
        currencyConversion c = new currencyConversion();
        boolean update = false;
        update = c.updateCurrency(fromCurrencyTF.getText(), toCurrencyTF.getText(), Double.parseDouble(rateTF.getText()), Double.parseDouble(processingFeeTF.getText()));
        
        if (update){
             Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Update Successfully");
                alert.showAndWait();
        }
        else {
           Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Failed");
                alert.setHeaderText("Update Failed.Try Again");
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
