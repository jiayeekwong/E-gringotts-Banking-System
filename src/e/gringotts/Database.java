
package e.gringotts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {
    private Connection con;
    
    public Database(){
        createConnection();
    }
    
     private void createConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/account","root","JY!040910");
           
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
     
     protected Connection getConnection(){
         return this.con;
     }
}
