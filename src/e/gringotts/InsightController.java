package e.gringotts;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class InsightController extends Account {

    @FXML
    private PieChart transactionPieChart;

    @FXML
    private RadioButton allRadioButton;

    private ToggleGroup allGroup;

    @FXML
    private RadioButton lastMonthRadioButton;
    private ToggleGroup monthGroup;

    @FXML
    private RadioButton lastWeekRadioButton;
    private ToggleGroup weekGroup;
    
  
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
    
      //Data
    private String loginname;
    private int userId;

 public void setUsernameId(String name,int Id) {
       this.userId = Id;
       this.loginname = name;
   }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Add data
        List<String[]> transactionList;
        try {
            transactionList = findAllTrxnById(this.userId);//Get all transaction from db according to userid
        
        allGroup = new ToggleGroup();
        allRadioButton.setToggleGroup(allGroup); //All
        monthGroup = new ToggleGroup();
        lastMonthRadioButton.setToggleGroup(monthGroup); //Month
        weekGroup = new ToggleGroup();
        lastWeekRadioButton.setToggleGroup(weekGroup); //Week
        // Populate the PieChart
        updatePieChart(transactionList);
        allRadioButton.setSelected(true);
        allRadioButton.setOnAction(e -> { //User choose all
            try {
                updatePieChart(findAllTrxnById(this.userId)); //Update pie chart
                lastMonthRadioButton.setSelected(false);
                lastWeekRadioButton.setSelected(false);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        lastMonthRadioButton.setOnAction(e -> { //User choose last month
            try {
                updatePieChart(filterTransactionsByDateRange(this.userId,30));
                allRadioButton.setSelected(false);
                lastWeekRadioButton.setSelected(false);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        lastWeekRadioButton.setOnAction(e -> { //User choose last week   
                try {
                    updatePieChart(filterTransactionsByDateRange(this.userId,7));
                } catch (SQLException ex) {
                    System.out.println("Database Error");
                }
                
                allRadioButton.setSelected(false);
                lastMonthRadioButton.setSelected(false);
           
        });
        } catch (SQLException ex) {
            System.out.println("Database Error");
        }
    }

    private void updatePieChart(List<String[]> trxnList) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        double sum = 0;

        for (String[] transaction : trxnList) {
            sum += Double.valueOf(transaction[1]);
            pieChartData.add(new PieChart.Data(transaction[0], Double.valueOf(transaction[1])));
        }

        double finalSum = sum;
        pieChartData.forEach(data -> data.nameProperty().bind(
                Bindings.concat(data.getName()," (", (int)((data.getPieValue()/ finalSum)*100),"%)")
        ));
         transactionPieChart.setData(pieChartData);
    }
    
    private List<String[]> findAllTrxnById(int userId) throws SQLException {
        List<String[]> transactionList = new ArrayList<>();
        PreparedStatement statement = this.con.prepareStatement(String.format("SELECT sum(amount) as amount, category FROM account.transaction WHERE userId = %s group by category;",userId));
        ResultSet set = statement.executeQuery();
        while(set.next()){
            transactionList.add(new String[]{set.getString("category"), set.getString("amount")} );
        }
        return transactionList;
    }

    private List<String[]> filterTransactionsByDateRange(int userId, int days) throws SQLException {
        List<String[]> transactionList = new ArrayList<>();
        PreparedStatement statement = con.prepareStatement(String.format("SELECT sum(amount) as amount, category FROM account.transaction WHERE userID = %s and datediff(now(), dateOfTransaction)<=%s  group by category;",userId, String.valueOf(days)));
        ResultSet set = statement.executeQuery();
        while(set.next()){
            transactionList.add(new String[]{set.getString("category"), set.getString("amount")} );
        }
        return transactionList;


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
    public void LogOut(MouseEvent event) throws IOException {
    
        //Switch to Scene Register
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
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
