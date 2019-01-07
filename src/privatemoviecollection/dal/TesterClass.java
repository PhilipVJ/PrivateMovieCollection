/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Christian Occhionero
 */
public class TesterClass
{
   public static void main (String[] args) throws SQLException, IOException {
        MovieDbDAO m = new MovieDbDAO(); 
        m.addMovie("link", "Big Hero 96", 9.4);
   }
}
