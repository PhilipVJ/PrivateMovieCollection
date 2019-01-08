/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.awt.Desktop;
import java.io.File;
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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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
    
    private PMCModel pmcmodel;
    @FXML
    private TableColumn<Movie, String> allMovTitle;
    @FXML
    private TableColumn<Movie, Double> allMovIMDBRating;
    @FXML
    private TableColumn<Movie, String> allMovRating;
    @FXML
    private Label scoreLabel;
    @FXML
    private Slider ratingSlider;
    
    private int chosenTableView;
    @FXML
    private Button rateButton;

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
        ratingSlider.setVisible(false);
        rateButton.setVisible(false);
        
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

    @FXML
    private void openMovie(ActionEvent event) throws IOException
    {
        Movie movieToPlay = allMovies.getSelectionModel().getSelectedItem();
        String path = movieToPlay.getFileLink();
        String pathFormatted = path.substring(6);
        Desktop.getDesktop().open(new File(pathFormatted));
        
    }

    @FXML
    private void deleteMovie(ActionEvent event) throws IOException, SQLException
    {
        Movie movieToRemove = allMovies.getSelectionModel().getSelectedItem();
        pmcmodel.removeMovie(movieToRemove);
        
    }

    @FXML
    private void setScore(MouseEvent event)
    {
      double rating = ratingSlider.getValue();
        double oneDigitRating = Math.round(rating * 10) / 10.0;
        scoreLabel.setText(Double.toString(oneDigitRating)); 

        
    }

    @FXML
    private void ratingDrag(MouseEvent event) 
    {
        
        double rating = ratingSlider.getValue();
        double oneDigitRating = Math.round(rating * 10) / 10.0;
        scoreLabel.setText(Double.toString(oneDigitRating));
        
        
    }

    @FXML
    private void allMoviesChosen(MouseEvent event)
    {
        chosenTableView=3;
        if(allMovies.getSelectionModel().getSelectedItem()!=null){
            ratingSlider.setVisible(true);
            rateButton.setVisible(true);
            
        }
    }

  

    @FXML
    private void rateMovie(ActionEvent event)  throws IOException, SQLException
    {
        double rating = ratingSlider.getValue();
        double oneDigitRating = Math.round(rating * 10) / 10.0;
        pmcmodel.rateMovie(allMovies.getSelectionModel().getSelectedItem(), oneDigitRating);
        allMovies.refresh();
    }

   
    
}
