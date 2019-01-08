/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.be;

import java.util.Date;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Philip
 */
public class Movie
{
private int id;
private SimpleStringProperty title;
private SimpleDoubleProperty webrating;
private SimpleStringProperty personalrating;
private String filelink;
private Date date;

public Movie(int id, String title, String filelink)
{
    this.id=id;
    this.title=new SimpleStringProperty(title);
    this.filelink=filelink;
}

public Movie(int id, String title, String filelink, double IMDBrating)
{
    this.id=id;
    this.title=new SimpleStringProperty(title);
    this.filelink=filelink;
    this.webrating=new SimpleDoubleProperty(IMDBrating);
    personalrating = new SimpleStringProperty("Not rated yet");
}

public String getTitle()
{
   return title.get();
}

public void setDate(Date date)
{
    this.date=date;
}
public String getFileLink()
{
    return filelink;
}

public void setPersonalRating(double rating)
{
    personalrating.set(Double.toString(rating));
}

public Double getWebrating()
 {
     System.out.println(""+webrating.get());
     return webrating.get(); 
 }

 
public String getPersonalrating()
 {
     
     return personalrating.get();
 }

public int getId()
{
    return id;
}

}
