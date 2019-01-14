/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.be;

import java.util.Date;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Philip
 */
public class Movie
{

    private final int id;
    private final SimpleStringProperty title;
    private final SimpleStringProperty webrating;
    private final SimpleStringProperty personalrating;
    private final String filelink;
    private Date date;

    public Movie(int id, String title, String filelink, double IMDBrating)
    {
        this.id = id;
        this.title = new SimpleStringProperty(title);
        this.filelink = filelink;
        String imdbrating = String.valueOf(IMDBrating);
        this.webrating = new SimpleStringProperty(imdbrating);
        this.personalrating = new SimpleStringProperty("Not rated yet");
    }

    public String getTitle()
    {
        return title.get();
    }

    public void setDate(Date date)
    {

        this.date = date;
    }

    public String getFileLink()
    {
        return filelink;
    }

    public void setPersonalRating(double rating)
    {
        personalrating.set(Double.toString(rating));

    }

    public String getWebrating()
    {
        if (webrating.get().equals("1000.0"))
        {
            return "No IMDB rating";
        } else
        {
            return webrating.get();
        }
    }

    public String getPersonalrating()
    {

        return personalrating.get();
    }

    public int getId()
    {
        return id;
    }

    public Date getDate()
    {
        return date;
    }

}
