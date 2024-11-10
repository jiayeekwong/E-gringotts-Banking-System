
package e.gringotts;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Random;
import java.util.Date;

public class Card extends Account {
    // To store information of debit/credit cards of a user
    private int cVV,userId;
    private LocalDate expiryDate;
    private String cardNumber;
    private ArrayList<String> CardRecord = new ArrayList<>();
  
   
    public String getCardNo(){
        return this.cardNumber;
    }
    
    public int getCVV(){
        return this.cVV;
    }
    
    public LocalDate getExpiredDate(){
        return this.expiryDate;
    }
    
    public int getUserId(){
        return this.userId;
    }
    
     public void SetUserId(int id){
        this.userId = id;
    }
    
      public void SetCardNo(String card){
        this.cardNumber = card;
    }
    
      public void setCvv(int cvv){
          this.cVV = cvv;
      }
      
      public void setExpiry(LocalDate date){
          this.expiryDate = date;
      }
  
     protected void GenerateCVV(){
         Random r = new Random();
         int CVV = r.nextInt(1000); //to generate random value between 0-999
          this.cVV = CVV;
    }
       
     protected void GenerateExpiredDate(){
        LocalDate today = LocalDate.now();
        int year = today.getYear()+5;
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();
        LocalDate date = LocalDate.of(year, month, day);
        this.expiryDate = date;
    }
     
     protected void GenerateCardNo(){
         Random r = new Random();
         int num = 2; //The while loop will atsrt generate from 2nd number
         int length =19; //The length of card number is set to be 20 include desh "-"
         int first = r.nextInt(10)+1;
         StringBuilder card = new StringBuilder();
         card.append(first);
         while(num<=length){
             if(num%5==0){ //Specific card number pattern
                 card.append("-");
             }
             else {
                 int nextNo = r.nextInt(10);
                 card.append(nextNo);
                 
             }
             num++; //update number
         }
         
         this.cardNumber = card.toString();
     }
    
    public String toString(){
        return "Card Number : "+this.cardNumber+"\n"
                + "Card cVV : "+this.cVV+"\n"
                + "Card Expiry Date : "+this.expiryDate;
    }
    
  
     public ArrayList<String> ShowCardNo(int id) throws SQLException{
      //Retreive Card Info From Database and show
            ArrayList<String> cardNo = new ArrayList<String>();
            PreparedStatement PS = con.prepareStatement("SELECT * FROM account.card WHERE userId = ? ");
            PS.setInt(1,id);
            ResultSet RS = PS.executeQuery();
            while(RS.next()){
                String Card = RS.getString("cardNo");
                char[] encrypt = Card.toCharArray();
                String last4No = encrypt[15]+""+encrypt[16]+""+encrypt[17]+""+encrypt[18];
               cardNo.add("**** **** **** "+last4No);
                
            }   
            return cardNo;
   }
    
     public ArrayList<java.sql.Date> ShowCardExpiry(int id) throws SQLException{
      //Retreive Card Info From Database and show
            ArrayList<java.sql.Date> Expiry = new ArrayList<>();     
            PreparedStatement PS = con.prepareStatement("SELECT * FROM account.card WHERE userId = ? ");
            PS.setInt(1,id);
            ResultSet RS = PS.executeQuery();
            while(RS.next()){ 
                java.sql.Date Expired = RS.getDate("ExpiryDate");             
                     Expiry.add(Expired);
              
            }   
            return Expiry;
   }
     
     public void removeExpiredCard() throws SQLException{
        //Remova card that has expired
        LocalDate today = LocalDate.now();
        java.sql.Date date = java.sql.Date.valueOf(today);
        PreparedStatement PS = this.con.prepareStatement("DELETE FROM account.Card WHERE ExpiryDate = '"+date+"'");
        PS.execute();
        
    }
    
     
    
     
   
 
}
