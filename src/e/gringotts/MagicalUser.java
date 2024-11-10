
package e.gringotts;

import com.sun.jdi.connect.spi.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MagicalUser<T> extends Account{
    private int userId;
    private String username;
    private T user;
    private ArrayList<T> types = new ArrayList<>();
    
    public MagicalUser(){
        try {
            getAllUserTypeFromDB();
            //To store all user types into arraylist first
        } catch (SQLException ex) {
            System.out.println("Database Error");
        }
    }
  
    public void setType(T User){
        this.user = User;
    }
    
    public boolean checkExist(T user) throws SQLException{
        //to get total types of usertypes
        this.types = getAllUserTypeFromDB();
        if(types.contains(user))return true;
        else return false;
    }

    //if user choose others
   public void addType(T newtype) throws SQLException{
        PreparedStatement PS = this.con.prepareStatement("INSERT INTO ACCOUNT.USERACCESS (userType,access) VALUES (?,?) ");
        PS.setString(1,String.valueOf(newtype));
        PS.setString(2,null);
       PS.executeUpdate();
   }
    
   public ArrayList<T> getAllUserTypeFromDB() throws SQLException{
       this.types.clear();
       PreparedStatement PS = this.con.prepareStatement("SELECT DISTINCT userType FROM account.useraccess ");
       ResultSet RS = PS.executeQuery();
       while(RS.next()){
            this.types.add((T) RS.getString("userType"));
       }
      
         return this.types;
   }
   
    public String[] DisplayUserTypeChoiceBox() throws SQLException{  
        //To update choice box 
         String[] UserType = new String[types.size()+1];
         for(int i =0;i<types.size();i++){
             UserType[i] = (String)types.get(i);
         }
         UserType[types.size()] = "Others";
         
        return UserType;
     }

   
    
    
    
  

                 
  
}
