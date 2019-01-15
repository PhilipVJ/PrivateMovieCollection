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
import javafx.scene.image.Image;

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
    private final MovieDbDAO movieDbDAO;
    private final CategoryDbDAO categoryDbDAO;
    private final IMDBDbDAO imdbDbDAO;

    public PMCManager()
    {
        movieDbDAO = new MovieDbDAO();
        categoryDbDAO = new CategoryDbDAO();
        imdbDbDAO = new IMDBDbDAO();
    }

    /**
     *  Movie methods
     * @param filelink
     * @param title
     * @param IMDBrating
     * @return
     * @throws IOException
     * @throws SQLException 
     */
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
    
    public void setDate(Movie movieToPlay, Date date) throws IOException, SQLException
    {
        movieDbDAO.setDate(movieToPlay, date);
    }
    
    public List<Movie> IMDBintervalSearch(double low, double high) throws IOException, SQLException
    {
        return movieDbDAO.IMDBintervalSearch(low, high);
    }
    
    public List<Movie> getMoviesWithSearchWord(String searchWord) throws IOException, SQLException
    {
        return movieDbDAO.getMoviesWithSearchWord(searchWord);
    }
    
    public Image getMoviePoster(String movieId) throws IOException
    {
        return movieDbDAO.getMoviePoster(movieId);
    }

    public String getTrailerURL(String title) throws IOException
    {
        return movieDbDAO.getTrailerURL(title);
    }
    
    /**
     * Category methods
     * @param name
     * @return
     * @throws IOException
     * @throws SQLException 
     */
    public Category addCategory(String name) throws IOException, SQLException
    {
        return categoryDbDAO.addCategory(name);
    }

    public List<Category> getAllCategories() throws SQLException, IOException
    {
        return categoryDbDAO.getAllCategories();
    }

    public void removeCategory(Category categoryToRemove) throws SQLException, IOException
    {
        categoryDbDAO.removeCategory(categoryToRemove);
    }

    public void deleteMovieFromCategory(Category selectedCategory, Movie movToDelete) throws SQLException, IOException
    {
        categoryDbDAO.deleteMovieFromCategory(selectedCategory, movToDelete);
    }

    public void addMovieToCat(Category chosenCategory, Movie chosenMovie) throws IOException, SQLException
    {
        categoryDbDAO.addMovieToCat(chosenMovie, chosenCategory);
    }
    
    /**
     *  IMDB methods
     * @param formattedMovieCode
     * @return 
     */
    public String getRating(String formattedMovieCode)
    {
        return imdbDbDAO.getRating(formattedMovieCode);
    }

    public ArrayList<IMDBMovie> getMovieSuggestions(String text)
    {
        return imdbDbDAO.getTitles(text);
    }

    public boolean updateIMDBdatabase() throws IOException
    {
        return imdbDbDAO.updateIMDBDatabase();
    }

    public String getLastUpdatedData()
    {
        return imdbDbDAO.getLastUpdatedInfo();
    }

    public List<IMDBMovie> getHighRatedMovies()
    {
        return imdbDbDAO.getHighRatedMovies();
    }

    public List<IMDBMovie> getTop250Movies()
    {
        return imdbDbDAO.getIMDBTop250();
    }
}
