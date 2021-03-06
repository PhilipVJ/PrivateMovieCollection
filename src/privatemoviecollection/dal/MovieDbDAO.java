/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.scene.image.Image;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import privatemoviecollection.be.Movie;

/**
 *
 * @author Philip
 */
public class MovieDbDAO
{

    public Movie addMovie(String filelink, String titel, double IMDBrating) throws IOException, SQLServerException, SQLException
    {
        DbConnection ds = new DbConnection();
        Movie addedMovie = null;
        BigDecimal imdbr;
        if (IMDBrating != 1000)
        {
            imdbr = new BigDecimal(IMDBrating);
            imdbr = imdbr.setScale(1, BigDecimal.ROUND_HALF_UP);
        } else
        {
            imdbr = null;
        }

        String SQL = "INSERT INTO Movie VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);)
        {
            pstmt.setString(1, titel);
            pstmt.setBigDecimal(2, imdbr);
            pstmt.setBigDecimal(3, null);
            pstmt.setString(4, filelink);
            pstmt.setDate(5, null);
            pstmt.execute();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();

            if (generatedKeys.next())
            {
                addedMovie = new Movie(generatedKeys.getInt(1), titel, filelink, IMDBrating);
            }
        }

        return addedMovie;
    }

    public void removeMovie(Movie movToRemove) throws IOException, SQLServerException, SQLException
    {
        int movId = movToRemove.getId();
        DbConnection dc = new DbConnection();

        try (Connection con = dc.getConnection();
                PreparedStatement pstmt1 = con.prepareStatement("DELETE FROM CatMovie WHERE MovieId= (?)");
                PreparedStatement pstmt2 = con.prepareStatement("DELETE FROM Movie WHERE id=(?)");)
        {
            pstmt1.setInt(1, movId);
            pstmt1.execute();
            pstmt2.setInt(1, movId);
            pstmt2.execute();
        }
    }

    public List<Movie> getAllMovies() throws SQLServerException, SQLException, IOException
    {
        ArrayList<Movie> allMovies = new ArrayList<>();
        DbConnection dc = new DbConnection();

        try (Connection con = dc.getConnection(); Statement statement = con.createStatement();)
        {

            ResultSet rs = statement.executeQuery("Select * FROM Movie;");
            while (rs.next())
            {
                String title = rs.getString("name");
                String path = rs.getString("filelink");
                int id = rs.getInt("id");
                BigDecimal r = rs.getBigDecimal("IMDBrating");
                BigDecimal pr = rs.getBigDecimal("personalrating");
                Date lastseen = rs.getDate("lastview");
                Movie movToAdd = new Movie(id, title, path, 1000);

                if (r != null)
                {
                    movToAdd = new Movie(id, title, path, r.doubleValue());
                }

                if (pr != null)
                {
                    movToAdd.setPersonalRating(pr.doubleValue());
                }

                if (lastseen != null)
                {
                    movToAdd.setDate(lastseen);

                }
                allMovies.add(movToAdd);
            }
            return allMovies;
        }

    }

    public void addRating(Movie movieToRate, double rating) throws IOException, SQLServerException, SQLException
    {
        DbConnection dc = new DbConnection();
        BigDecimal bigRating = new BigDecimal(rating);
        bigRating = bigRating.setScale(1, BigDecimal.ROUND_HALF_UP);

        try (Connection con = dc.getConnection(); PreparedStatement pstmt = con.prepareStatement("UPDATE Movie SET personalrating = (?) WHERE id = (?)");)
        {
            pstmt.setBigDecimal(1, bigRating);
            pstmt.setInt(2, movieToRate.getId());
            pstmt.execute();

        }
    }

    public void setDate(Movie movieToDate, Date thisDate) throws SQLServerException, IOException, SQLException
    {
        DbConnection dc = new DbConnection();
        java.sql.Date sDate = new java.sql.Date(thisDate.getTime());

        try (Connection con = dc.getConnection(); PreparedStatement pstmt = con.prepareStatement("UPDATE Movie SET lastview = (?) WHERE id = (?)");)
        {
            pstmt.setDate(1, sDate);
            pstmt.setInt(2, movieToDate.getId());
            pstmt.execute();
        }
    }

    public Movie getMovie(int movieId) throws SQLServerException, IOException, SQLException
    {
        DbConnection dc = new DbConnection();
        try (Connection con = dc.getConnection(); PreparedStatement pstmt = con.prepareStatement("Select * FROM Movie WHERE id= (?)");)
        {
            Movie movToGet = null;
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                String title = rs.getString("name");
                String path = rs.getString("filelink");
                int id = rs.getInt("id");
                BigDecimal IMDBratings = rs.getBigDecimal("IMDBrating");
                BigDecimal pRatings = rs.getBigDecimal("personalrating");
                Date date = rs.getDate("lastview");
                movToGet = new Movie(id, title, path, 1000);

                if (IMDBratings != null)
                {
                    movToGet = new Movie(id, title, path, IMDBratings.doubleValue());
                }

                if (pRatings != null)
                {
                    movToGet.setPersonalRating(pRatings.doubleValue());
                }

                if (date != null)
                {
                    movToGet.setDate(date);

                }

            }
            return movToGet;
        }

    }

    /**
     * Returns all movies within the movie list which has a rating within the
     * given interval
     *
     * @param low
     * @param high
     * @return
     * @throws IOException
     * @throws SQLServerException
     * @throws SQLException
     */
    public List<Movie> IMDBintervalSearch(double low, double high) throws IOException, SQLServerException, SQLException
    {
        DbConnection dc = new DbConnection();
        ArrayList<Movie> intervalMovies = new ArrayList<>();
        BigDecimal lowBig = new BigDecimal(low);
        BigDecimal highBig = new BigDecimal(high);
        try (Connection con = dc.getConnection(); PreparedStatement pstmt = con.prepareStatement("Select * FROM Movie WHERE IMDBrating BETWEEN (?) AND (?)"))
        {
            pstmt.setBigDecimal(1, lowBig);
            pstmt.setBigDecimal(2, highBig);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                String title = rs.getString("name");
                String path = rs.getString("filelink");
                int id = rs.getInt("id");
                BigDecimal r = rs.getBigDecimal("IMDBrating");
                BigDecimal pr = rs.getBigDecimal("personalrating");
                Date lastseen = rs.getDate("lastview");
                Movie movToAdd = new Movie(id, title, path, 1000);

                if (r != null)
                {
                    movToAdd = new Movie(id, title, path, r.doubleValue());
                }

                if (pr != null)
                {
                    movToAdd.setPersonalRating(pr.doubleValue());
                }

                if (lastseen != null)
                {
                    movToAdd.setDate(lastseen);
                }

                intervalMovies.add(movToAdd);
            }
            return intervalMovies;
        }
    }

    public List<Movie> getMoviesWithSearchWord(String searchWord) throws IOException, SQLServerException, SQLException
    {
        DbConnection dc = new DbConnection();
        List<Movie> searchedMovies = new ArrayList<>();
        try (Connection con = dc.getConnection(); PreparedStatement pstmt = con.prepareStatement("Select * FROM Movie WHERE name LIKE (?)");)
        {
            Movie movToGet = null;
            pstmt.setString(1, "%" + searchWord + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next())
            {
                String title = rs.getString("name");
                String path = rs.getString("filelink");
                int id = rs.getInt("id");
                BigDecimal IMDBratings = rs.getBigDecimal("IMDBrating");
                BigDecimal pRatings = rs.getBigDecimal("personalrating");
                Date date = rs.getDate("lastview");
                movToGet = new Movie(id, title, path, 1000);

                if (IMDBratings != null)
                {
                    movToGet = new Movie(id, title, path, IMDBratings.doubleValue());
                }

                if (pRatings != null)
                {
                    movToGet.setPersonalRating(pRatings.doubleValue());
                }

                if (date != null)
                {
                    movToGet.setDate(date);

                }

                searchedMovies.add(movToGet);

            }
            return searchedMovies;
        }
    }

    /**
     * Returns the first image from Bings Image Search engine
     *
     * @param movieTitle
     * @return
     * @throws IOException
     */
    public Image getMoviePoster(String movieTitle) throws IOException
    {
        String url = "https://www.bing.com/images/search?q=" + movieTitle + "%20poster";
        System.setProperty("http.agent", "Chrome");
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        String url2 = "";
        for (Element x : links)
        {
            if (x.toString().contains(movieTitle) && x.toString().contains("view=detailV2"))
            {
                url2 = x.absUrl("href");
                break;
            }
        }
        doc = Jsoup.connect(url2).get();
        links = doc.select("a[href]");
        String finalUrl = "";
        for (Element y : links)
        {
            if (y.toString().contains("mediaurl="))
            {
                finalUrl = y.toString();
                break;
            }
        }
        int lastIndex = finalUrl.lastIndexOf("mediaurl=");
        int lastIndex2 = finalUrl.lastIndexOf("&amp;exph");
        String subStringURL = finalUrl.substring(lastIndex + 9, lastIndex2);
        String urlDecoded = "" + URLDecoder.decode(subStringURL, "UTF-8");
        Image poster = new Image(urlDecoded);
        return poster;
    }
/**
 * Gets the URL for the IMDB trailer of the specific movie
 * @param title
 * @return
 * @throws IOException 
 */
    public String getTrailerURL(String title) throws IOException, StringIndexOutOfBoundsException
    {
        String url = "https://www.imdb.com/title/" + title;
        System.setProperty("http.agent", "Chrome");
        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        String preparedSearch = title + "/videoplayer";
        String fullURL = "https://www.imdb.com/";
        String preFormat = "";
        for (Element x : links)
        {
            if (x.toString().contains(preparedSearch))
            {
                preFormat = x.toString();
                break;
            }

        }
        int firstIndex = preFormat.indexOf("title");
        String formatted = preFormat.substring(firstIndex);
        int firstIndex2 = formatted.indexOf(" ");
        String moreFormatted = formatted.substring(0, firstIndex2 - 1);

        return fullURL+moreFormatted;
    }
}
