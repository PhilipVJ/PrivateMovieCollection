/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.gui.model.PMCModel;

/**
 *
 * @author Christian Occhionero
 */
public class TesterClass
{
   public static void main (String[] args) throws SQLException, IOException {

        MovieDbDAO m = new MovieDbDAO(); 
        List<Movie> allMovies = m.getAllMovies();
        for (Movie x:allMovies){
            System.out.println(""+ x.getTitle()+"  "+x.getWebrating()+"  "+x.getPersonalrating());
            
        }


   
   }
}
