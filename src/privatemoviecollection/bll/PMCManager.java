/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.bll;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.IMDBMovie;
import privatemoviecollection.be.Movie;
import privatemoviecollection.dal.CategoryDbDAO;
import privatemoviecollection.dal.IMDBDbDAO;
import privatemoviecollection.dal.MovieDbDAO;

/**
 *
 * @author Philip
 */
public class PMCManager
{
private MovieDbDAO movieDbDAO;
private CategoryDbDAO categoryDbDAO;
private IMDBDbDAO imdbDbDAO;

public PMCManager()
{
    movieDbDAO=new MovieDbDAO();
    categoryDbDAO=new CategoryDbDAO(); 
    imdbDbDAO = new IMDBDbDAO();
}

    public Movie addMovie(String filelink, String title, double IMDBrating) throws IOException, SQLException
    {
        return movieDbDAO.addMovie(filelink, title, IMDBrating);
    }
    
    public List<Movie> getAllMovies() throws IOException, SQLException
    {
        return movieDbDAO.getAllMovies();
    }

    public void removeMovie(Movie movieToRemove) throws IOException, SQLException
    {
        movieDbDAO.removeMovie(movieToRemove);
    }

    public void rateMovie(Movie movToRate, double oneDigitRating) throws IOException, SQLException
    {
        movieDbDAO.addRating(movToRate, oneDigitRating);
    }

    public Movie getMovie(int movID) throws IOException, SQLException
    {
       return movieDbDAO.getMovie(movID);
    }
    
    public Category addCategory(String name) throws IOException, SQLException
    {
        return categoryDbDAO.addCategory(name); 
    }
    
    public List<Category> getAllCategories() throws SQLException, IOException
    {
        return categoryDbDAO.getAllCategories(); 
    }
    
    public void removeCategory (Category categoryToRemove) throws SQLException, IOException
    {
        categoryDbDAO.removeCategory(categoryToRemove);
    }
        
    


    public void setDate(Movie movieToPlay, Date date) throws IOException, SQLException
    {
   movieDbDAO.setDate(movieToPlay, date);
    }

    public void addMovieToCat(Category chosenCategory, Movie chosenMovie) throws IOException, SQLException
    {
  categoryDbDAO.addMovieToCat(chosenMovie, chosenCategory);
    }

    public String getRating(String formattedMovieCode)
    {
     return imdbDbDAO.getRating(formattedMovieCode);
    }

    public ArrayList<IMDBMovie> getMovieSuggestions(String text)
    {
      return imdbDbDAO.getTitles(text);
    }


}
