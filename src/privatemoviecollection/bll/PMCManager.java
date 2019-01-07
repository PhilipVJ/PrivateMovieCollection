/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.bll;

import java.io.IOException;
import java.sql.SQLException;
import privatemoviecollection.dal.MovieDbDAO;

/**
 *
 * @author Philip
 */
public class PMCManager
{
MovieDbDAO movieDbDAO;

public PMCManager()
{
    movieDbDAO=new MovieDbDAO();
}

    public void addMovie(String filelink, String title, double IMDBrating) throws IOException, SQLException
    {
        movieDbDAO.addMovie(filelink, title, IMDBrating);
    }
    
}
