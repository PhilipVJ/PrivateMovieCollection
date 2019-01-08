/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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
        /**
     * Checks for movies with a personal score under 6.0 the user haven't seen in more than 2 years
     * @return 
     */
    public ArrayList<Movie> checkForBadMovies()
    {
      
        java.util.Date date=new java.util.Date();
       
        // Saves the number of miliseconds since January 1. 1970
        double dateTime = date.getTime();
        
        // Converts it to days
        double days = Math.round(dateTime/(1000*60*60*24));
      
        ArrayList<Movie> moviesToDelete = new ArrayList<Movie>();
        
        
        for(Movie x: allMovies)
        {
         double movieRating=10;   
          // Checks weather the movie has been rated
          if(!x.getPersonalrating().equals("Not rated yet")){
          String rating = x.getPersonalrating();
          movieRating = Double.parseDouble(rating);
          }
          // Checks weather the movie has been seen and if it has a rating equal to or below 6
          if(x.getDate()!=null && movieRating<=6){
            double movieTime = x.getDate().getTime();
            double movieDays = Math.round(movieTime/(1000*60*60*24));
           
               // Calculates if it has been 2 years since it was last seen.
               if(days-movieDays>730)
               {
               moviesToDelete.add(x);
               }
           }
                    
        }

        return moviesToDelete;
    }
}