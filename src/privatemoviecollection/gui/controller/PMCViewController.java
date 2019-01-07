/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import privatemoviecollection.gui.model.PMCModel;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class PMCViewController implements Initializable
{

    @FXML
    private TableView<?> categories;
    @FXML
    private TableView<?> catmovies;
    @FXML
    private TableColumn<?, ?> title;
    @FXML
    private TableView<?> allMovies;
    @FXML
    private Button addCategory;
    @FXML
    private Button removeCategory;
    @FXML
    private Button addMovie;
    @FXML
    private Button deleteMovie;
    
    private PMCModel pmcmodel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        pmcmodel = new PMCModel();
    }    

    @FXML
    private void addMovie(ActionEvent event) throws IOException
    {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/AddMovie.fxml"));
            Parent root = (Parent)loader.load();
            AddMovieController addMovieCon = loader.getController();
        
            addMovieCon.setModel(pmcmodel);
        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
    }
    
}
