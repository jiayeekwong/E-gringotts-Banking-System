package e.gringotts;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javafx.scene.image.Image;

public class UserAvatar{
    protected Image image;
    private File file;
    private String filePath;

   //Get different userpath accordingly to userid
  
    public Image getImage(int id){
        if(id%2==0){
            filePath = "C:/Users/jiaye/OneDrive/Documents/NetBeansProjects/E-Gringiotts/src/e/gringotts/Platinum Patronus.jpeg";
             file = new File(filePath);
             image = new Image(file.toURI().toString());
        }
        else if(id%3==0){
            filePath = "C:/Users/jiaye/OneDrive/Documents/NetBeansProjects/E-Gringiotts/src/e/gringotts/Silver Snitch.jpg";
             file = new File(filePath);
             image = new Image(file.toURI().toString());
        }
        else if(id%5==0){
            filePath = "C:/Users/jiaye/OneDrive/Documents/NetBeansProjects/E-Gringiotts/src/e/gringotts/Golden Galleon.png";
              file = new File(filePath);
             image = new Image(file.toURI().toString());
        }
        else if(id%7==0){
            filePath = "C:/Users/jiaye/OneDrive/Documents/NetBeansProjects/E-Gringiotts/src/e/gringotts/nifflers.jpg";
            file = new File(filePath);
            image = new Image(file.toURI().toString());
        }
        else{
             String filePath = "C:/Users/jiaye/OneDrive/Documents/NetBeansProjects/E-Gringiotts/src/e/gringotts/dobby.jpg";
             file = new File(filePath);
             image = new Image(file.toURI().toString());
        }
      
        return this.image;
    }
}
