/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import privatemoviecollection.be.Movie;
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
    private TableView<Movie> allMovies;
    @FXML
    private Button addCategory;
    @FXML
    private Button removeCategory;
    @FXML
    private Button addMovie;
    @FXML
    private Button deleteMovie;
    
    private PMCModel pmcmodel;
    @FXML
    private TableColumn<Movie, String> allMovTitle;
    @FXML
    private TableColumn<Movie, Double> allMovIMDBRating;
    @FXML
    private TableColumn<Movie, String> allMovRating;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            pmcmodel = new PMCModel();
            pmcmodel.getAllMovies();
            
        allMovTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        allMovIMDBRating.setCellValueFactory(new PropertyValueFactory<>("webrating"));
        allMovRating.setCellValueFactory(new PropertyValueFactory<>("personalrating"));
        
        allMovies.setItems(pmcmodel.getAllMovies());
        
        } catch (IOException ex)
        {
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    @FXML
    private void deleteFromCategory(ActionEvent event)
    {
    }
    
}
