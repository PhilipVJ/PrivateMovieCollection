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
import java.util.ArrayList;
import java.util.List;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;

/**
 *
 * @author Philip
 */
public class CategoryDbDAO
{

    public Category addCategory(String name) throws IOException, SQLServerException, SQLException
    {
        DbConnection ds = new DbConnection();
        Category addedCategory = null;
        String SQL = "INSERT INTO Category VALUES (?)";

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);)
        {

            pstmt.setString(1, name);
            pstmt.execute();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();

            if (generatedKeys.next())
            {
                addedCategory = new Category(name, generatedKeys.getInt(1));
            }
        }
        return addedCategory;
    }

    public void removeCategory(Category catToRemove) throws SQLException, IOException, SQLServerException
    {
        DbConnection dc = new DbConnection();
        int CategoryId = catToRemove.getId();
        try (Connection con = dc.getConnection();
                PreparedStatement pstmt1 = con.prepareStatement("DELETE FROM CatMovie WHERE CategoryId= (?)");
                PreparedStatement pstmt2 = con.prepareStatement("DELETE FROM Category WHERE id= (?)");)
        {

            pstmt1.setInt(1, CategoryId);
            pstmt1.execute();
            pstmt2.setInt(1, CategoryId);
            pstmt2.execute();
        }
    }

    public void addMovieToCat(Movie movToAdd, Category chosenCategory) throws IOException, SQLServerException, SQLException
    {
        DbConnection dc = new DbConnection();
        String SQL = "INSERT INTO CatMovie VALUES (?, ?)";
        try (Connection con = dc.getConnection(); PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);)
        {

            pstmt.setInt(1, chosenCategory.getId());
            pstmt.setInt(2, movToAdd.getId());
            pstmt.execute();
        }

    }

    public List<Category> getAllCategories() throws SQLException, IOException
    {
        ArrayList<Category> allCategories = new ArrayList<>();
        DbConnection dc = new DbConnection();

        try (Connection con = dc.getConnection(); Statement statement = con.createStatement();)
        {

            ResultSet rs = statement.executeQuery("Select * FROM Category;");
            while (rs.next())
            {
                String name = rs.getString("name");
                int id = rs.getInt("id");
                Category cat = new Category(name, id);

                try (PreparedStatement pstmt = con.prepareStatement("Select * FROM CatMovie WHERE CategoryId = (?)"))
                {
                    pstmt.setInt(1, id);
                    ResultSet rs2 = pstmt.executeQuery();

                    while (rs2.next())
                    {

                        int movieId = rs2.getInt("MovieId");
                        cat.addMovieWithID(movieId);

                    }
                    allCategories.add(cat);
                }
            }
            return allCategories;
        }

    }

    public void deleteMovieFromCategory(Category selectedCategory, Movie movToDelete) throws SQLException, IOException
    {
        DbConnection dc = new DbConnection();
        try (Connection con = dc.getConnection();)
        {
            int MovieId = movToDelete.getId();
            int CategoryId = selectedCategory.getId();
            int categorySize = getAllCategories().size();

            try (PreparedStatement pstmt = con.prepareStatement("DELETE FROM CatMovie WHERE MovieId=(?) AND CategoryId=(?)"))
            {
                pstmt.setInt(1, MovieId);
                pstmt.setInt(2, CategoryId);
                pstmt.execute();
            }

        }

    }

}
