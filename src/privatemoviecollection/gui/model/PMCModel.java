/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import privatemoviecollection.be.Category;
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
private ObservableList<Category> allCategories;
private ObservableList<Movie> catMovies;


public PMCModel() throws IOException, SQLException
{
    pmcmanager = new PMCManager();
    allMovies=FXCollections.observableList(pmcmanager.getAllMovies());
    allCategories = FXCollections.observableList(pmcmanager.getAllCategories());
   
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
      
        Calendar calender = Calendar.getInstance();
        
            
        // Saves the number of miliseconds since January 1. 1970
        double dateTime = calender.getTimeInMillis();
        
        // Converts it to days
        double days = dateTime/(1000*60*60*24);
        
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
            double movieDays = movieTime/(1000*60*60*24);
            double timeLapsed = days-movieDays;
              System.out.println(""+timeLapsed);
           
               // Calculates if it has been 2 years since it was last seen.
               if(timeLapsed>730)
               {
               moviesToDelete.add(x);
               }
           }
                    
        }

        return moviesToDelete;
    }
    
    public void addCategory(String name) throws IOException, SQLException
    {
        Category categoryToAdd = pmcmanager.addCategory(name);
        allCategories.add(categoryToAdd);
    }
    
    public ObservableList<Category> getAllCategories() throws IOException, SQLException
    {
        return allCategories;
    }

    public void removeCategory(Category selectedItem) throws SQLException, IOException
    {
       pmcmanager.removeCategory(selectedItem);
        for(Category x:allCategories){
        if(x.getId()==selectedItem.getId()){
            allCategories.remove(x);
            return;
        }
        }
    }

    public void setCatMovies(Category chosenCategory)
    {
//      ArrayList<Integer> movieIds = chosenCategory.getMovies();
//      
//      for (Integer x: movieIds){
//          
//      }
    }

    public void addMovieToCat(Category chosenCategory, Movie chosenMovie) throws IOException, SQLException
    {
       pmcmanager.addMovieToCat(chosenCategory, chosenMovie);
    }
}