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
import privatemoviecollection.be.IMDBMovie;
import privatemoviecollection.be.Movie;

/**
 *
 * @author Christian Occhionero
 */
public class TesterClass
{
   public static void main (String[] args) throws SQLException, IOException {

 MovieDbDAO tester = new MovieDbDAO();
 List<Movie> test2 = tester.IMDBintervalSearch(1, 4);
       System.out.println(""+test2.size());

   }
 
   
}
