/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.model;

import java.io.IOException;
import java.sql.SQLException;
import privatemoviecollection.bll.PMCManager;

/**
 *
 * @author Philip
 */
public class PMCModel
{
PMCManager pmcmanager;

public PMCModel()
{
    pmcmanager = new PMCManager();
}

    public void addMovie(String filelink, String title, double IMDBrating) throws IOException, SQLException
    {
     pmcmanager.addMovie(filelink, title, IMDBrating);
    }
    
}
