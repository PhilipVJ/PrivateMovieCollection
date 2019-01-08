/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.bll;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.dal.CategoryDbDAO;
import privatemoviecollection.dal.MovieDbDAO;

/**
 *
 * @author Philip
 */
public class PMCManager
{
private MovieDbDAO movieDbDAO;
private CategoryDbDAO categoryDbDAO;

public PMCManager()
{
    movieDbDAO=new MovieDbDAO();
    categoryDbDAO=new CategoryDbDAO(); 
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

    
    public Category addCategory(int id, String name) throws IOException, SQLException
    {
        return categoryDbDAO.addCategory(name); 
    }
    
    public List<Category> getAllCategories() throws SQLException, IOException
    {
        return categoryDbDAO.getAllCategories(); 
    }
    
    public void removeCategory (Category catecoryToRemove) throws SQLException, IOException
    {
        categoryDbDAO.removeCategory(catecoryToRemove);
    }
        
    


    public void setDate(Movie movieToPlay, Date date) throws IOException, SQLException
    {
   movieDbDAO.setDate(movieToPlay, date);
    }

}
