/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;

/**
 *
 * @author Philip
 */
public class MovieDbDAO
{
public Movie addMovie (String filelink, String titel, double IMDBrating) throws IOException, SQLServerException, SQLException
{
    DbConnection ds = new DbConnection();
    Movie addedMovie = null;
    BigDecimal imdbr = new BigDecimal(IMDBrating);
    imdbr = imdbr.setScale(1, BigDecimal.ROUND_HALF_UP);
    System.out.println(""+imdbr.toString());    
    try(Connection con = ds.getConnection()){
   
            String SQL = "INSERT INTO Movie VALUES (?, ?, ?, ?, ?)"; 
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,titel);
            pstmt.setBigDecimal(2, imdbr);
            pstmt.setBigDecimal(3, null);
            pstmt.setString(4, filelink);
            pstmt.setDate(5, null);
            pstmt.execute();
            
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            
            if (generatedKeys.next())
            {
                addedMovie= new Movie(generatedKeys.getInt(1), titel, filelink, IMDBrating);
                System.out.println("Following movie has been added to the database: "+addedMovie.getTitle());
            }
    }

    return addedMovie;
}

public void removeMovie(Movie movToRemove)
{
    
}

public List<Movie> getAllMovies() throws IOException, SQLServerException, SQLException
{
       
            ArrayList<Movie> allMovies = new ArrayList<>();
            DbConnection dc = new DbConnection();
            
            try(Connection con = dc.getConnection())
            {
            
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("Select * FROM Movie;");
            while (rs.next())
            {
                String title = rs.getString("name");
                String path = rs.getString("filelink");
                int id = rs.getInt("id");
                BigDecimal r = rs.getBigDecimal("IMDBrating");
             
                
                allMovies.add(new Movie(id, title, path, r.doubleValue()));
            }
            return allMovies;
            }
        
}


public void addRating(double rating)
{

}

}