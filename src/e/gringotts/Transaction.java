
package e.gringotts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Transaction {
    // +userID
     private int userId;
    private String transactionId;
    private String senderName;
    private String receiverName;
    private double amount;
    private double balance;
    private Date dateTime;
    private String category;
    private String time;

    public Transaction() {
    }

    public Transaction(int userId, String transactionId, String senderName, String receiverName, double amount,double balance, Date dateTime,String category, String time) {
        this.userId=userId;
        this.transactionId = transactionId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.amount = amount;
        this.balance = balance;
        this.dateTime = dateTime;
        this.category = category;
        this.time = time;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public Date getDateOfTransaction() {
        return dateTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setTime(String time){
        this.time=time;
        
    }

    public String getTime() {
        return time;
    }
    
    
    public int getUserId() {
        return userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public void setCategory(String category){
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    
    public String generateReceipt() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy 'at' HH:mm:ss");
        String formattedDate = dateFormat.format(dateTime);

        return String.format(
            "🌟 E-GRINGOTTS RECEIPT 🌟\n\n" +
            "Transaction ID: %s\n" +
            "Date: %s\n\n" +
            "From : %s\n" +
            "To: %s\n" +
            "Amount: **%.2f Knut**\n\n" +
            "Thank you for using E-Gringotts! Your magical transfer has been successfully completed. ✨✨\n\n" +
            "For any inquiries or further assistance, owl us at support@gringotts.com.\n\n" +
            "May your galleons multiply like Fizzing Whizbees! 🌠🌠",
            transactionId, formattedDate, senderName, receiverName, amount
        );
    }
    
    public String generateConversionReceipt(double initialB,double processingFee) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy 'at' HH:mm:ss");
        String formattedDate = dateFormat.format(dateTime);

        return String.format(
            "🌟 E-GRINGOTTS RECEIPT 🌟\n\n" +
            "Transaction ID: %s\n" +
            "Date: %s\n\n" +
            "From : %s\n" +
            "To: %s\n" +
            "Amount: **%.2f Knut**\n" +
            "Initial Balance : **%.2f Knut**\n"+
            "Processing Fee : **%.2f Knut**\n"+
            "Account Balance : **%.2f Knut** \n\n"+
            "Thank you for using E-Gringotts! Your magical transfer has been successfully completed. ✨✨\n\n" +
            "For any inquiries or further assistance, owl us at support@gringotts.com.\n\n" +
            "May your galleons multiply like Fizzing Whizbees! 🌠🌠",
            transactionId, formattedDate, senderName, receiverName, amount,initialB,processingFee,balance
        );
    }
  
    
    
}

