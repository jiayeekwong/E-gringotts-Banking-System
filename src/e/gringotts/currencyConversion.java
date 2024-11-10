
package e.gringotts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class currencyConversion <T> extends Account{

    
    private static double processingFee;
    
    //WHAT IS THE PROCESSING FEE PARAMETER USE FOR???
    public double convert (String c1, String c2, double value, double processingFee) throws ClassNotFoundException, SQLException {

        double exchangeRate = conversionGraph(c1, c2);
        double convertedValue = exchangeRate * value;                   
        
        return convertedValue;
    }
    
    //add new currency and its realm in SQL database
    public boolean addCurrency (String realm, String currency) throws ClassNotFoundException, SQLException {
     

        //check if the currency already exist or not
        PreparedStatement stmt = this.con.prepareStatement("SELECT DISTINCT Currency FROM account.exchangerates");
        ResultSet RS = stmt.executeQuery();
        while(RS.next()){ //continue if got next line   
            if (RS.getString("Currency").equalsIgnoreCase(currency))
                return false;
        }
        
        //SQL database
        //Add row
        PreparedStatement PS = con.prepareStatement("INSERT into account.exchangerates (Realm,Currency) VALUES (?,?);");
        PS.setString(1,realm); 
        PS.setString(2,currency); 
        PS.execute();      
        
        //Add column
        PreparedStatement PS2 = con.prepareStatement("ALTER TABLE account.exchangerates ADD COLUMN 1_" + currency + " DECIMAL (10,6);");
        PS2.execute();
        
        return true;
    }
    
    //Update currency value in SQL database
    public boolean updateCurrency (String c1, String c2, double value, double processingFee) throws ClassNotFoundException, SQLException {
      
        PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT Currency FROM account.exchangerates");
        ResultSet RS = stmt.executeQuery();
        PreparedStatement stmt2 = con.prepareStatement("DESCRIBE account.exchangerates");
        ResultSet RS2 = stmt2.executeQuery();
        while(RS2.next()){ //continue if got next line   
            if (RS2.getString("Field").equalsIgnoreCase("1_" + c1)){              
                while(RS.next()){ //continue if got next line   
                    if (RS.getString("Currency").equalsIgnoreCase(c2)){
                        //Note: c1 is one from first row, c2 is under currency column
                        PreparedStatement PS = con.prepareStatement("UPDATE account.exchangerates SET 1_" + c1 + "=? WHERE currency=?");
                        PS.setDouble(1,value); //set 1 c1 = how much c2?
                        PS.setString(2,c2); //c2? (under currency column)
                        PS.execute();

                        //Add new row
                        PreparedStatement ps = con.prepareStatement("INSERT into account.processingfee (Convert_From,Convert_To) VALUES (?,?);");
                        ps.setString(1, c1);
                        ps.setString(2, c2);       
                        ps.execute();
                        
                        //Insert value into the new row
                        PreparedStatement PS2 = con.prepareStatement("UPDATE account.processingfee SET Fee=? WHERE Convert_From=? AND Convert_To=?");
                        PS2.setDouble(1,processingFee); //set 1 c1 = how much c2?
                        PS2.setString(2, c1);
                        PS2.setString(3, c2);       
                        PS2.execute();
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    public boolean removeCurrency (String currency) throws ClassNotFoundException, SQLException{
      
        //check if the currency already exist or not
        PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT Currency FROM account.exchangerates");
        ResultSet RS = stmt.executeQuery();
        while(RS.next()){ //continue if got next line   
            if (RS.getString("Currency").equalsIgnoreCase(currency)){
                //Delete row in exchangerates
                PreparedStatement PS = con.prepareStatement("DELETE FROM account.exchangerates WHERE Currency = '" + currency + "'");
                PS.execute();
                
                //Delete column in exchangerates
                PS = con.prepareStatement("ALTER TABLE account.exchangerates DROP COLUMN 1_" + currency);
                PS.execute();
                
                //Delete in processingfee
                PS = con.prepareStatement("DELETE FROM account.processingfee WHERE Convert_From = '" + currency + "' OR Convert_To = '" + currency + "'");
                PS.execute();
                return true;
            }
        }
        return false;
    }
    
    //Return shortest path value for conversion using Dijkstra Algorithm
    public double conversionGraph(String fromCurrency, String toCurrency) throws ClassNotFoundException, SQLException {
      
        //To store all the currency in exchangerates currency column
        List<String> CurrencyList = new ArrayList<>();
        
        //To check whether the currency conversion rate is null or not
        boolean check = false;
        
        PreparedStatement stmt1 = con.prepareStatement("SELECT COUNT(*) FROM account.exchangerates"); //find how many rows (aka how many currencies)       
        ResultSet RS_1 = stmt1.executeQuery();
        RS_1.next();
        int numOfCurrency = RS_1.getInt("COUNT(*)");      
        DijkstrasShortestPathAdjacencyListWithDHeap d = new DijkstrasShortestPathAdjacencyListWithDHeap (numOfCurrency);       
        
        PreparedStatement stmt2 = con.prepareStatement("SELECT TABLE_NAME, COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
        "WHERE TABLE_NAME = 'exchangerates'");
        ResultSet RS = stmt2.executeQuery();
        RS.next();
        int numOfCurrencyField = RS.getInt("COUNT(*)") - 2; //-2 = minus realm & currency column
        
        WeightedCurrencyGraph w = addVertex(); //this is a graph with all vertex(currency) but no edge
        PreparedStatement stmt3 = con.prepareStatement("SELECT DISTINCT Currency FROM  account.exchangerates");
        RS = stmt3.executeQuery();
        while(RS.next() && numOfCurrencyField != 0){ //continue if got next line           
            String currencyField = RS.getString("Currency"); //select currency from database
            PreparedStatement stmt4 = con.prepareStatement("SELECT DISTINCT Currency FROM account.exchangerates");
            ResultSet RS1 = stmt4.executeQuery();
            
            while(RS1.next()){ //continue if got next line   
                String currencyColumn = RS1.getString("Currency"); //select currency from database
                CurrencyList.add(currencyColumn);
                PreparedStatement stmt5 = con.prepareStatement("SELECT 1_" + currencyField + " FROM account.exchangerates WHERE Currency = '" + currencyColumn + "'");
                ResultSet RS2 = stmt5.executeQuery();
                RS2.next();
                Double Edge = RS2.getDouble("1_" + currencyField);
//                double Edge_d = Edge;
//                int edge = (int) (Edge_d * 1000000); //NOTE: addEdge method below only accept int type weight, so I converted double to int
//                //to get the decimal value together during conversion, I multiply by 10^6, ltr dont forget to divide back when you want to get the actual weight
                if (Edge!= 0.0){ //double (primitive type) cannot be null, so I use Double class instead
                    int indexOfCurrencyField = w.getIndex(currencyField); 
                    int indexOfCurrencyColumn = w.getIndex(currencyColumn); 
                    d.addEdge(indexOfCurrencyField, indexOfCurrencyColumn, Edge);
                }
            }            
            numOfCurrencyField--;
        }
        
        //Check if direct conversion is possible or not
        PreparedStatement PS6 = con.prepareStatement("SELECT 1_" + fromCurrency + " FROM account.exchangerates WHERE Currency = '" + toCurrency + "'");
        ResultSet RS6  = PS6.executeQuery();
        RS6.next();
        int value = RS6.getInt("1_" + fromCurrency);
        if (RS6.wasNull()) { //Direct conversion is not possible
//        if (RS6.getInt("1_" + fromCurrency) == null) { // == null is not working, so i use < 0, at least its working -_-       
            Integer[] fromToNode = (d.reconstructPath(w.getIndex(fromCurrency), w.getIndex(toCurrency))).toArray(new Integer[0]);
//            System.out.println(CurrencyList.get(fromToNode[0]) + " + " + CurrencyList.get(fromToNode.length-1));  
//            System.out.println("f " + CurrencyList.get(fromToNode[1]));
            for (int i: fromToNode){
                System.out.println(i + " " + i);
            }

            Integer[] path = d.reconstructPath(w.getIndex(fromCurrency), w.getIndex(toCurrency)).toArray(new Integer[0]);
            double rate = 1;
            int cnt = 0;
            for (int i: path){
                if (cnt == path.length-1)
                    return rate;
                String from = CurrencyList.get(i);
                String to = CurrencyList.get(path[cnt+1]);
                rate *= d.dijkstra(w.getIndex(from),w.getIndex(to));             
                cnt++;
            }                        
        }
        double result = d.dijkstra(w.getIndex(fromCurrency),w.getIndex(toCurrency));
        
        //Cannot find path to convert
        if (result == Double.POSITIVE_INFINITY)
            return 0;
        
        return result;
    }    
    
    //Return the total processing fee
    public double feeGraph(String fromCurrency, String toCurrency) throws ClassNotFoundException, SQLException {
     
        PreparedStatement stmt1 = con.prepareStatement("SELECT COUNT(*) FROM account.exchangerates"); //find how many rows (aka how many currencies)       
        ResultSet RS_1 = stmt1.executeQuery();
        RS_1.next();
        int numOfRows = RS_1.getInt("COUNT(*)");      
        DijkstrasShortestPathAdjacencyListWithDHeap d = new DijkstrasShortestPathAdjacencyListWithDHeap (numOfRows);               
        
        WeightedCurrencyGraph v = addVertex(); //same graph with currency one (have all same nodes) without edges
        PreparedStatement stmt3 = con.prepareStatement("SELECT Convert_From FROM account.processingfee");
        ResultSet RS = stmt3.executeQuery();
        
        PreparedStatement stmt4 = con.prepareStatement("SELECT Convert_To FROM account.processingfee");            
        ResultSet RS1 = stmt4.executeQuery(); 
        
        while(RS.next() && RS1.next()){ //continue if got next line           
            String convertFrom = RS.getString("Convert_From");                          
            String convertTo = RS1.getString("Convert_To"); 
            
            PreparedStatement stmt5 = con.prepareStatement("SELECT Fee FROM account.processingfee WHERE Convert_From = '" 
            + convertFrom + "' AND Convert_To = '" + convertTo + "'");
            ResultSet RS2 = stmt5.executeQuery();
            RS2.next();
            Double fee = RS2.getDouble("Fee");
//                double Edge_d = Edge;
//                int edge = (int) (Edge_d * 1000000); //NOTE: addEdge method below only accept int type weight, so I converted double to int
//                //to get the decimal value together during conversion, I multiply by 10^6, ltr dont forget to divide back when you want to get the actual weight
            if (fee!= 0.0){ //double (primitive type) cannot be null, so I use Double class instead
                int indexOfConvertFrom = v.getIndex(convertFrom); 
                int indexOfConvertTo = v.getIndex(convertTo); 
                d.addEdge(indexOfConvertFrom, indexOfConvertTo, fee);
            }                
        }               
        
        //To store all the currency in exchangerates currency column
        List<String> CurrencyList = new ArrayList<>();
        PreparedStatement stmt5 = con.prepareStatement("SELECT DISTINCT Currency FROM account.exchangerates");
        ResultSet RS5 = stmt5.executeQuery();
        while(RS5.next()){ //continue if got next line   
            String currencyColumn = RS5.getString("Currency"); //select currency from database
            CurrencyList.add(currencyColumn);
        }
        
        //Check if direct conversion is possible or not
        PreparedStatement PS6 = con.prepareStatement("SELECT 1_" + fromCurrency + " FROM account.exchangerates WHERE Currency = '" + toCurrency + "'");
        ResultSet RS6  = PS6.executeQuery();
        RS6.next();
        int value = RS6.getInt("1_" + fromCurrency);
               
        if (RS6.wasNull()) { //Direct conversion is not possible            
            double fee = 0;
            int cnt = 0;
            String[] currencyPath = path(fromCurrency,toCurrency);
            for (String i: currencyPath){
                if (cnt == currencyPath.length-1)
                    return fee;           
                String from = i;
                String to = currencyPath[cnt+1];        
                double currentFee = d.dijkstra(v.getIndex(from),v.getIndex(to));
                if (currentFee != Double.POSITIVE_INFINITY){                    
                    fee += currentFee;
                }
                else{
                    currentFee = d.dijkstra(v.getIndex(to),v.getIndex(from));
                    fee += d.dijkstra(v.getIndex(to),v.getIndex(from));
                }         
                cnt++;
            }                        
        }
        
        double getDijkstraValue = d.dijkstra(v.getIndex(fromCurrency),v.getIndex(toCurrency));
        
        //use if-else if the opposite conversion processing fee is present in SQL: if -> knut-sickle, else -> sickle-knut
        if (getDijkstraValue != Double.POSITIVE_INFINITY)
            return getDijkstraValue;
        
        getDijkstraValue = d.dijkstra(v.getIndex(toCurrency),v.getIndex(fromCurrency));
        //Cannot find path to convert
        if (getDijkstraValue == Double.POSITIVE_INFINITY)
            return 0;
        return getDijkstraValue;
    }
    
    public String[] path (String fromCurrency, String toCurrency) throws ClassNotFoundException, SQLException {
        //To store all the currency in exchangerates currency column
        List<String> CurrencyList = new ArrayList<>();
        
        PreparedStatement stmt1 = con.prepareStatement("SELECT COUNT(*) FROM account.exchangerates"); //find how many rows (aka how many currencies)       
        ResultSet RS_1 = stmt1.executeQuery();
        RS_1.next();
        int numOfCurrency = RS_1.getInt("COUNT(*)");      
        DijkstrasShortestPathAdjacencyListWithDHeap d = new DijkstrasShortestPathAdjacencyListWithDHeap (numOfCurrency);       
        
        PreparedStatement stmt2 = con.prepareStatement("SELECT TABLE_NAME, COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS " +
        "WHERE TABLE_NAME = 'exchangerates'");
        ResultSet RS = stmt2.executeQuery();
        RS.next();
        int numOfCurrencyField = RS.getInt("COUNT(*)") - 2; //-2 = minus realm & currency column
        
        WeightedCurrencyGraph w = addVertex(); //this is a graph with all vertex(currency) but no edge
        PreparedStatement stmt3 = con.prepareStatement("SELECT DISTINCT Currency FROM account.exchangerates");
        RS = stmt3.executeQuery();
        while(RS.next() && numOfCurrencyField != 0){ //continue if got next line           
            String currencyField = RS.getString("Currency"); //select currency from database
            PreparedStatement stmt4 = con.prepareStatement("SELECT DISTINCT Currency FROM account.exchangerates");
            ResultSet RS1 = stmt4.executeQuery();
            
            while(RS1.next()){ //continue if got next line   
                String currencyColumn = RS1.getString("Currency"); //select currency from database
                CurrencyList.add(currencyColumn);
                PreparedStatement stmt5 = con.prepareStatement("SELECT 1_" + currencyField + " FROM account.exchangerates WHERE Currency = '" + currencyColumn + "'");
                ResultSet RS2 = stmt5.executeQuery();
                RS2.next();
                Double Edge = RS2.getDouble("1_" + currencyField);
//                double Edge_d = Edge;
//                int edge = (int) (Edge_d * 1000000); //NOTE: addEdge method below only accept int type weight, so I converted double to int
//                //to get the decimal value together during conversion, I multiply by 10^6, ltr dont forget to divide back when you want to get the actual weight
                if (Edge!= 0.0){ //double (primitive type) cannot be null, so I use Double class instead
                    int indexOfCurrencyField = w.getIndex(currencyField); 
                    int indexOfCurrencyColumn = w.getIndex(currencyColumn); 
                    d.addEdge(indexOfCurrencyField, indexOfCurrencyColumn, Edge);
                }
            }            
            numOfCurrencyField--;
        }        

        Integer[] indexPath = d.reconstructPath(w.getIndex(fromCurrency), w.getIndex(toCurrency)).toArray(new Integer[0]);
        String[] currencyPath = new String[indexPath.length];

        int cnt = 0;
        for (int i: indexPath){
            currencyPath[cnt] = CurrencyList.get(i);
            cnt++;
        }

        return currencyPath;
    } 
    
    public WeightedCurrencyGraph addVertex () throws ClassNotFoundException, SQLException {
        //Graph

        PreparedStatement stmt = con.prepareStatement("SELECT DISTINCT Currency FROM account.exchangerates");
        ResultSet RS = stmt.executeQuery();
        
        WeightedCurrencyGraph w = new WeightedCurrencyGraph();
        while(RS.next()){ //continue if got next line           
            String currency = RS.getString("Currency"); //select currency from database          
            w.addVertex(currency);
        }        
        return w;
    }    
}
