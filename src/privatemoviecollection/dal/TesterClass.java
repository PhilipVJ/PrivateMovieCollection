/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import com.sun.javafx.geom.AreaOp;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.bll.PMCManager;
import privatemoviecollection.gui.model.PMCModel;

/**
 *
 * @author Christian Occhionero
 */
public class TesterClass
{
   public static void main (String[] args) throws SQLException, IOException {
PMCModel t = new PMCModel();
t.checkForBadMovies();

 
   }
}
