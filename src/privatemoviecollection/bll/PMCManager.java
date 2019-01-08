/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.bll;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import privatemoviecollection.be.Movie;
import privatemoviecollection.dal.MovieDbDAO;

/**
 *
 * @author Philip
 */
public class PMCManager
{
private MovieDbDAO movieDbDAO;

public PMCManager()
{
    movieDbDAO=new MovieDbDAO();
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
}
