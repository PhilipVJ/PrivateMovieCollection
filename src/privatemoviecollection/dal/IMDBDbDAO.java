/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javafx.scene.control.Alert;
import org.apache.commons.io.FileUtils;
import privatemoviecollection.be.IMDBMovie;

/**
 *
 * @author Philip
 */
public class IMDBDbDAO
{

    /**
     * Returns the rating of the given IMDB id.
     *
     * @param url
     * @return
     */
    public String getRating(String id)
    {
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);
        String source = "data/rating.tsv";
        File title = new File(source);

// parses all rows in one go.
        List<String[]> allRows = parser.parseAll(title);
        for (String[] x : allRows)
        {
            if (id.equals(x[0]))
            {
                return x[1];
            }
        }

        return "No rating found";

    }

    /**
     * Returns all titles from the IMDB database file which contains the search
     * word.
     *
     * @param movieSearch
     * @return
     */
    public ArrayList<IMDBMovie> getTitles(String movieSearch)
    {
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);
        String source = "data/title.tsv";
        File title = new File(source);

        parser.beginParsing(title);
        ArrayList<IMDBMovie> allSearches = new ArrayList<>();

        String[] row;
        while ((row = parser.parseNext()) != null)
        {
            String lowerCaseSearch = row[3].toLowerCase();
            String lowerCaseInput = movieSearch.toLowerCase();
            if (lowerCaseSearch.contains(lowerCaseInput))
            {
                IMDBMovie movToAdd = new IMDBMovie(row[0], row[3]);
                allSearches.add(movToAdd);
            }
        }

        return allSearches;
    }

    /**
     * Downloads and updates the IMDB database files on the computer
     *
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    public boolean updateIMDBDatabase() throws IOException
    {
        // Downloads and renames files

        String fromFile = "https://datasets.imdbws.com/title.ratings.tsv.gz";
        String toFile = "data/update/title.ratings.tsv.gz";

        FileUtils.copyURLToFile(new URL(fromFile), new File(toFile), 10000, 10000);

        String fromFile2 = "https://datasets.imdbws.com/title.basics.tsv.gz";
        String toFile2 = "data/update/title.basics.tsv.gz";

        FileUtils.copyURLToFile(new URL(fromFile2), new File(toFile2), 10000, 10000);

        gunzipRating();

        File f1 = new File("data/update/data.tsv");
        File f2 = new File("data/update/rating.tsv");
        f1.renameTo(f2);

        gunzipTitles();

        File f3 = new File("data/update/data.tsv");
        File f4 = new File("data/update/title.tsv");
        f3.renameTo(f4);

        // Delete old files
        File f5 = new File("data/rating.tsv");
        File f6 = new File("data/title.tsv");
        f5.delete();
        f6.delete();

        //Move new updated files
        File f7 = new File("data/update/title.tsv");
        File f8 = new File("data/title.tsv");

        FileUtils.moveFile(f7, f8);

        File f9 = new File("data/update/rating.tsv");
        File f10 = new File("data/rating.tsv");

        FileUtils.moveFile(f9, f10);

        // Delete zip containers
        File f11 = new File("data/update/title.basics.tsv.gz");
        File f12 = new File("data/update/title.ratings.tsv.gz");
        f11.delete();
        f12.delete();

        return true;
    }

    /**
     * Unzips the .gz files from the IMDB interface.
     */
    private void gunzipRating()
    {

        byte[] buffer = new byte[1024];

        try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream("data/update/title.ratings.tsv.gz")); FileOutputStream out = new FileOutputStream("data/update/data.tsv");)
        {

            int len;
            while ((len = gzis.read(buffer)) > 0)
            {
                out.write(buffer, 0, len);
            }
            out.close();

        } catch (IOException ex)
        {
            Logger.getLogger(IMDBDbDAO.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Important information");
            alert.setHeaderText("An error has occured");
            alert.setContentText("The files could not be located");
            alert.showAndWait();
        }

    }

    private void gunzipTitles()
    {
        byte[] buffer = new byte[1024];

        try (GZIPInputStream gzis = new GZIPInputStream(new FileInputStream("data/update/title.basics.tsv.gz")); FileOutputStream out = new FileOutputStream("data/update/data.tsv");)
        {

            int len;
            while ((len = gzis.read(buffer)) > 0)
            {
                out.write(buffer, 0, len);
            }
            out.close();
        } catch (IOException ex)
        {
            Logger.getLogger(IMDBDbDAO.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Important information");
            alert.setHeaderText("An error has occured");
            alert.setContentText("The files could not be located");
            alert.showAndWait();
        }

    }

    /**
     * Returns the last time the database files were updated.
     *
     * @return
     */
    public String getLastUpdatedInfo()
    {
        File data = new File("data/title.tsv");
        long lastModified = data.lastModified();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String lastMod = "IMDB data files was last updated: " + sdf.format(lastModified);

        return lastMod;
    }

    /**
     * Gets 10 random movies, tv-shows etc. with a rating above 8.5 and more
     * than 100.000 votes
     *
     * @return
     */
    public List<IMDBMovie> getHighRatedMovies()
    {
        List<IMDBMovie> highRatedMovies = new ArrayList<>();
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);
        String source = "data/rating.tsv";
        File title = new File(source);

        // parses all rows in one go.
        List<String[]> allHighRatedMovies = parser.parseAll(title);
        // removes the first String array since it only contains column titles
        allHighRatedMovies.remove(0);
        for (String[] x : allHighRatedMovies)
        {

            double rating = Double.parseDouble(x[1]);
            double numberOfVotes = Double.parseDouble(x[2]);

            if ((rating > 8.5) && numberOfVotes > 100000)
            {
                IMDBMovie toAdd = new IMDBMovie(x[0], "");
                toAdd.setRating(x[1]);
                highRatedMovies.add(toAdd);

            }
        }
        // Removes all objects except 10 random ones
        while (highRatedMovies.size() > 10)
        {
            int randomNumber = (int) (Math.random() * highRatedMovies.size());
            highRatedMovies.remove(randomNumber);
        }
        // Sets the title for all the IMDBMovie objects
        for (IMDBMovie x : highRatedMovies)
        {
            x.setMovieTitle(getTitleById(x.getMovieId()));
        }
        return highRatedMovies;

    }

    /**
     * Returns the title of the given IMDB id
     *
     * @param id
     * @return
     */
    public String getTitleById(String id)
    {
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);
        String source = "data/title.tsv";
        File title = new File(source);
        parser.beginParsing(title);
        String[] row;
        while ((row = parser.parseNext()) != null)
        {
            if (row[0].equals(id))
            {
                parser.stopParsing();
                return row[3];
            }
        }

        return "No title found";
    }

    public List<IMDBMovie> getIMDBTop250()
    {
        List<IMDBMovie> top250Movies = new ArrayList<>();
        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");

        TsvParser parser = new TsvParser(settings);
        String source = "data/rating.tsv";
        File title = new File(source);

        // parses all rows in one go.
        List<String[]> allRatedMovies = parser.parseAll(title);
        // removes the first String array since it only contains column titles
        allRatedMovies.remove(0);
        // Gets all movies with more than 25.000 votes
        double averageRatingScore = 0;
        for (String[] x : allRatedMovies)
        {
            double numberOfVotes = Double.parseDouble(x[2]);
            String rating = x[1];
            double rat = Double.parseDouble(rating);
            averageRatingScore += rat;
            if (numberOfVotes >= 25000)
            {
                IMDBMovie toAdd = new IMDBMovie(x[0], "");
                toAdd.setRating(rating);
                toAdd.setNumberOfVotes(numberOfVotes);
                top250Movies.add(toAdd);

            }
        }
        double averageRat = averageRatingScore / (allRatedMovies.size() + 1);

        parser.stopParsing();
        // Removes all movies which isn't categorized as a movie in the IMDB database
        String source2 = "data/title.tsv";
        File cat = new File(source2);
        String[] row;
        parser.beginParsing(cat);

        HashMap<String, String> hashMap = new HashMap<>();
        while ((row = parser.parseNext()) != null)
        {
            String category = row[1];
            if (category.equals("movie"))
            {
                String movTitle = row[3];
                String movId = row[0];
                hashMap.put(movId, movTitle);
            }
        }

        List<IMDBMovie> sortedMovies = new ArrayList<>();
        for (IMDBMovie x : top250Movies)
        {
            String titleOfMovie = hashMap.get(x.getMovieId());
            if (titleOfMovie != null)
            {
                x.setMovieTitle(titleOfMovie);
                x.calculateWeightedRating(averageRat);
                sortedMovies.add(x);
            }
        }

        sortedMovies.sort(Comparator.comparing(IMDBMovie::getWeightedRating));
        Collections.reverse(sortedMovies);
        int size = sortedMovies.size();
        int counter = 1;
        while (sortedMovies.size() != 250)
        {
            sortedMovies.remove(size - counter);
            counter++;
        }
        return sortedMovies;

    }
}
