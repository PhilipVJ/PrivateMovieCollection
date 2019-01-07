/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import java.io.IOException;
import java.sql.SQLException;
import privatemoviecollection.be.Category;

/**
 *
 * @author Christian Occhionero
 */
public class testerClassDAL
{
   public static void main (String[] args) throws SQLException, IOException {
        CategoryDbDAO m = new CategoryDbDAO(); 
       
      Category Action = new Category("Action", 2);
      m.removeCategory(Action);
      
   }
}
