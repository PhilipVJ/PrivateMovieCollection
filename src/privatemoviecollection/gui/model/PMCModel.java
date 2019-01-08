/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import privatemoviecollection.be.Movie;
import privatemoviecollection.bll.PMCManager;

/**
 *
 * @author Philip
 */
public class PMCModel
{
private final PMCManager pmcmanager;
private ObservableList<Movie> allMovies;


public PMCModel() throws IOException, SQLException
{
    pmcmanager = new PMCManager();
    allMovies=FXCollections.observableList(pmcmanager.getAllMovies());
   
}

    public void addMovie(String filelink, String title, double IMDBrating) throws IOException, SQLException
    {
      Movie movToAdd = pmcmanager.addMovie(filelink, title, IMDBrating);
      allMovies.add(movToAdd);
    }
    
    public ObservableList<Movie> getAllMovies() throws IOException, SQLException
    {
        return allMovies;
    }

    public void removeMovie(Movie movieToRemove) throws IOException, SQLException
    {
    pmcmanager.removeMovie(movieToRemove);
    for(Movie x:allMovies){
        if(x.getId()==movieToRemove.getId()){
            allMovies.remove(x);
            return;
        }
    }
    }

    public void rateMovie(Movie movToRate, double oneDigitRating) throws IOException, SQLException
    {
      pmcmanager.rateMovie(movToRate, oneDigitRating);
       for(Movie x:allMovies){
        if(x.getId()==movToRate.getId()){
            x.setPersonalRating(oneDigitRating);
            return;
        }
    }
      
    }

    public void setDate(Movie movieToPlay, Date date) throws IOException, SQLException
    {
      pmcmanager.setDate(movieToPlay, date);
       for(Movie x:allMovies){
        if(x.getId()==movieToPlay.getId()){
            x.setDate(date);
            return;
        }
    }
    
    }
}