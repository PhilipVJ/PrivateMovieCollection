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
public class CategoryDbDAO
{
    
public Category addCategory(String name) throws IOException, SQLServerException, SQLException
{
  
    DbConnection ds = new DbConnection();
    Category addedCategory = null;
    
    try(Connection con = ds.getConnection()){
   
            String SQL = "INSERT INTO Category VALUES (?)"; 
            PreparedStatement pstmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,name);
            pstmt.execute();
            
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            
            if (generatedKeys.next())
            {
                addedCategory= new Category(name, generatedKeys.getInt(1));
                System.out.println("Following Category has been added to the database: "+addedCategory.getName());
            }
    }

    return addedCategory;
    
}
    

         
    


public void removeCategory (Category catToRemove) throws SQLException, IOException
{
  DbConnection dc = new DbConnection();
    try(Connection con = dc.getConnection();)
        {
            int CategoryId = catToRemove.getId();
            PreparedStatement pstmt2 = con.prepareStatement("DELETE FROM Category WHERE id=(?)");
            pstmt2.setInt(1,CategoryId );
            pstmt2.execute();
            System.out.println("Following Category has been deleted: "+CategoryId);
        } 
       
}

public void addMovieToCat (Movie movToAdd)
{
    
}

public List<Category> getAllCategories()
{
  return null;  
}
    


}
