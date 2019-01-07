/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.be;

import java.util.Date;

/**
 *
 * @author Philip
 */
public class Movie
{
private int id;
private String title;
private double IMDBrating;
private double personalRating;
private String filelink;
private Date date;

public Movie(int id, String title, String filelink)
{
    this.id=id;
    this.title=title;
    this.filelink=filelink;
}

public Movie(int id, String title, String filelink, double IMDBrating)
{
    this.id=id;
    this.title=title;
    this.filelink=filelink;
    this.IMDBrating=IMDBrating;
}

public String getTitle()
{
    return title;
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
    this.personalRating=rating;
}
}
