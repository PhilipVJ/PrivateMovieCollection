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
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinResults;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.IMDBMovie;
import privatemoviecollection.be.Movie;
import privatemoviecollection.bll.PMCManager;

/**
 *
 * @author Philip
 */
public class PMCModel
{

    private final PMCManager pmcmanager;
    private final ObservableList<Movie> allMovies;
    private final ObservableList<Category> allCategories;
    private final ObservableList<Movie> catMovies;

    public PMCModel() throws IOException, SQLException
    {
        pmcmanager = new PMCManager();
        allMovies = FXCollections.observableList(pmcmanager.getAllMovies());
        allCategories = FXCollections.observableList(pmcmanager.getAllCategories());
        catMovies = FXCollections.observableList(new ArrayList<Movie>());
    }

    /**
     *  Movie methods
    */
    public boolean addMovie(String filelink, String title, double IMDBrating) throws IOException, SQLException
    {
        for (Movie x : allMovies)
        {
            if (x.getTitle().equals(title))
            {
                return false;
            }

            if (checkForSimularities(title, x.getTitle()) == false)
            {
                return false;
            }
        }
        Movie movToAdd = pmcmanager.addMovie(filelink, title, IMDBrating);
        allMovies.add(movToAdd);
        return true;
    }

    public ObservableList<Movie> getAllMovies() throws IOException, SQLException
    {
        return allMovies;
    }

    public void removeMovie(Movie movieToRemove) throws IOException, SQLException
    {
        pmcmanager.removeMovie(movieToRemove);
        for (Movie x : allMovies)
        {
            if (x.getId() == movieToRemove.getId())
            {
                allMovies.remove(x);
                break;
            }
        }

        for (Movie x : catMovies)
        {
            if (x.getId() == movieToRemove.getId())
            {
                catMovies.remove(x);
                break;
            }
        }
    }

    public void rateMovie(Movie movToRate, double oneDigitRating) throws IOException, SQLException
    {
        pmcmanager.rateMovie(movToRate, oneDigitRating);
        for (Movie x : allMovies)
        {
            if (x.getId() == movToRate.getId())
            {
                x.setPersonalRating(oneDigitRating);
                break;
            }
        }

        if (catMovies != null)
        {
            for (Movie y : catMovies)
            {
                if (y.getId() == movToRate.getId())
                {
                    y.setPersonalRating(oneDigitRating);
                    return;
                }
            }
        }
    }

    public void setDate(Movie movieToPlay, Date date) throws IOException, SQLException
    {
        pmcmanager.setDate(movieToPlay, date);
        for (Movie x : allMovies)
        {
            if (x.getId() == movieToPlay.getId())
            {
                java.sql.Date sDate = new java.sql.Date(date.getTime());
                x.setDate(sDate);
                break;
            }
        }
        for (Movie y : catMovies)
        {
            if (y.getId() == movieToPlay.getId())
            {
                java.sql.Date sDate = new java.sql.Date(date.getTime());
                y.setDate(sDate);
            }
        }
    }

    /**
     * Checks for movies with a personal score under 6.0, which the user haven't
     * seen in more than 2 years
     *
     * @return
     */
    public void checkForBadMovies()
    {
        Calendar calender = Calendar.getInstance();

        // Saves the number of miliseconds since January 1. 1970
        double dateTime = calender.getTimeInMillis();

        // Converts it to days
        double days = dateTime / (1000 * 60 * 60 * 24);

        ArrayList<Movie> badMovies = new ArrayList<>();

        for (Movie x : allMovies)
        {
            double movieRating = 10;
            // Checks weather the movie has been rated
            if (!x.getPersonalrating().equals("Not rated yet"))
            {
                String rating = x.getPersonalrating();
                movieRating = Double.parseDouble(rating);
            }
            // Checks weather the movie has been seen and if it has a rating equal to or below 6
            if (x.getDate() != null && movieRating <= 6)
            {
                double movieTime = x.getDate().getTime();
                double movieDays = movieTime / (1000 * 60 * 60 * 24);
                double timeLapsed = days - movieDays;

                // Calculates if it has been 2 years since it was last seen.
                if (timeLapsed > 730)
                {
                    badMovies.add(x);
                }
            }

        }

        if (badMovies.size() > 0)
        {
            for (Movie x : badMovies)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Important information");
                alert.setHeaderText("Bad movie detected");
                alert.setContentText("You haven't seen " + x.getTitle()
                        + " in more than 2 years and you rated it 6 or less. You should consider deleting it");
                alert.showAndWait();
            }
        }
    }

    public void getMoviesWithSearchWord(String searchWord) throws IOException, SQLException
    {
        List<Movie> searchResults = pmcmanager.getMoviesWithSearchWord(searchWord);
        allMovies.clear();
        for (Movie x : searchResults)
        {
            allMovies.add(x);
        }
    }
    
    public Image getMoviePoster(String movieId) throws IOException
    {
        return pmcmanager.getMoviePoster(movieId);
    }

    public String getTrailerURL(String title) throws IOException
    {
        return pmcmanager.getTrailerURL(title);
    }
    
    /**
     * Category methods
    */
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
        allCategories.remove(selectedItem);
        setCategoryMovies();
    }

    public void addMovieToCat(Category chosenCategory, Movie chosenMovie) throws IOException, SQLException
    {
        pmcmanager.addMovieToCat(chosenCategory, chosenMovie);
        catMovies.add(chosenMovie);
        for (Category x : allCategories)
        {
            if (x.getId() == chosenCategory.getId())
            {
                x.addMovieWithID(chosenMovie.getId());
                return;
            }
        }
    }
    
    public ObservableList<Movie> getCatMovies()
    {
        return catMovies;
    }
    
    public void deleteMovieFromCategory(Category selectedCategory, Movie movToDelete) throws SQLException, IOException
    {
        pmcmanager.deleteMovieFromCategory(selectedCategory, movToDelete);

        for (Movie x : catMovies)
        {
            if (x.getId() == movToDelete.getId())
            {
                catMovies.remove(x);
                break;
            }
        }

        for (Category y : allCategories)
        {
            if (y.getId() == selectedCategory.getId())
            {
                y.removeMovieWithID(movToDelete.getId());
                return;
            }
        }
    }
    
    public void setCategoryMovies() throws IOException, SQLException
    {
        catMovies.clear();
        List<Movie> allCatMovies = new ArrayList<>();
        for (Category x : allCategories)
        {
            if (x.getSelect().isSelected() == true)
            {
                for (Integer y : x.getMovies())
                {
                    allCatMovies.add(pmcmanager.getMovie(y));
                }
            }
        }
        //Removes duplicates
        List<Movie> allCatMovies2 = new ArrayList<>();
        for (Movie y : allCatMovies)
        {
            boolean found = false;
            for (Movie k : allCatMovies2)
            {
                if (y.getId() == k.getId())
                {
                    found = true;
                }
            }
            if (found == false)
            {
                allCatMovies2.add(y);
                catMovies.add(y);
            }
        }
    }
    
    /**
     * IMDB methods
    */
    public String getRating(String formattedMovieCode)
    {
        return pmcmanager.getRating(formattedMovieCode);
    }

    public ArrayList<IMDBMovie> getMovieSuggestions(String text)
    {
        return pmcmanager.getMovieSuggestions(text);
    }

    public void IMDBintervalSearch(double low, double high) throws IOException, SQLException
    {
        List<Movie> IMDBinterSearch = pmcmanager.IMDBintervalSearch(low, high);
        allMovies.clear();
        for (Movie x : IMDBinterSearch)
        {
            allMovies.add(x);
        }
    }

    public boolean updateIMDBdatabase() throws IOException
    {
        return pmcmanager.updateIMDBdatabase();
    }

    public String getLastUpdatedData()
    {
        return pmcmanager.getLastUpdatedData();
    }
    
    public ObservableList<IMDBMovie> getHighRatedMovies()
    {
        return FXCollections.observableList(pmcmanager.getHighRatedMovies());
    }

    public ObservableList<IMDBMovie> getTop250Movies()
    {
        return FXCollections.observableList(pmcmanager.getTop250Movies());
    }
    
    /**
     *  Clear search fields
    */
    public void clearSearches() throws IOException, SQLException
    {
        allMovies.clear();
        List<Movie> clearedSearch = pmcmanager.getAllMovies();
        for (Movie x : clearedSearch)
        {
            allMovies.add(x);
        }
    }

    /**
     * Generates an alert with the specified String
     *
     * @param message
     */
    public void generateErrorAlarm(String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Important information");
        alert.setHeaderText("An error has occured");
        alert.setContentText("" + message);
        alert.showAndWait();
    }

    public void duplicateAlarm()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Important information");
        alert.setHeaderText("Duplicate movie");
        alert.setContentText("You already have this movie in your database");
        alert.showAndWait();
    }

    /**
     * Uses the levenshtein distance to check how simular the new title and a
     * title from the database are. Returns true if a save is approved.
     *
     * @param title
     * @param title2
     * @return
     */
    private Boolean checkForSimularities(String title, String title2)
    {
        LevenshteinDetailedDistance i = LevenshteinDetailedDistance.getDefaultInstance();
        LevenshteinResults k = i.apply(title, title2);
        int levenshteinDistance = k.getDistance();

        if (levenshteinDistance <= 2)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Did you mean " + title2 + "?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES)
            {
                return false;
            }

            if (alert.getResult() == ButtonType.NO)
            {
                return true;
            }
        }
        return true;
    }
}
