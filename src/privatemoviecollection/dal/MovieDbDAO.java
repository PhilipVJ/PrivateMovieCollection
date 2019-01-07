/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    try(Connection con = ds.getConnection()){
   
            String SQL = "INSERT INTO Movie VALUES (?, ?, ?)"; 
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,titel);
            pstmt.setFloat(2, (float) IMDBrating);
            pstmt.setString(3, filelink);
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

public List<Movie> getAllMovies()
{
    return null;
}


public void addRating(double rating)
{

}

}