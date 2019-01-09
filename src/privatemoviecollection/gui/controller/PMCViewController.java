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
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import privatemoviecollection.be.Category;
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
    private TableView<Category> categories;
    @FXML
    private TableView<Movie> catmovies;
    @FXML
    private TableView<Movie> allMovies;
    
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
    @FXML
    private Label lastSeenLabel;
    @FXML
    private TableColumn<Category, String> allCategories;
    @FXML
    private TableColumn<Movie, Double> catIMDBrating;
    @FXML
    private TableColumn<Movie, String> catTitle;
    @FXML
    private TableColumn<Movie, String> catPersonalrating;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            pmcmodel = new PMCModel();

           
            
        allMovTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        allMovIMDBRating.setCellValueFactory(new PropertyValueFactory<>("webrating"));
        allMovRating.setCellValueFactory(new PropertyValueFactory<>("personalrating"));
        
        allMovies.setItems(pmcmodel.getAllMovies());
        ratingSlider.setVisible(false);
        rateButton.setVisible(false);
        checkForBadMovies();
        
        allCategories.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        categories.setItems(pmcmodel.getAllCategories());
        
        catTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        catIMDBrating.setCellValueFactory(new PropertyValueFactory<>("webrating"));
        catPersonalrating.setCellValueFactory(new PropertyValueFactory<>("personalrating"));
        
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
    private void openMovie(ActionEvent event) throws IOException, SQLException
    {
        Movie movieToPlay = allMovies.getSelectionModel().getSelectedItem();
        String path = movieToPlay.getFileLink();
        
//        String pathFormatted = path.substring(6);
        java.util.Date date=new java.util.Date(); 
        pmcmodel.setDate(movieToPlay, date);
        
        Desktop.getDesktop().open(new File(path));
        
        
        
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
            Movie chosenMov = allMovies.getSelectionModel().getSelectedItem();
            if(chosenMov.getDate()!=null){
            lastSeenLabel.setText(chosenMov.getTitle()+" was last seen: "+chosenMov.getDate());
            }
            else{
            lastSeenLabel.setText("You haven't seen this movie yet");
            }
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

    private void checkForBadMovies()
    {
      ArrayList<Movie> badMovies = pmcmodel.checkForBadMovies();
      if (badMovies.size()>0)
      {
          for(Movie x:badMovies)
          {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Important information");
                alert.setHeaderText("Bad movie detected");
                alert.setContentText("You haven't seen "+x.getTitle()+
                " in more than 2 years and you rated it 6 or less. You should consider deleting it");
                alert.showAndWait();
          }
      }
    }

    @FXML
    private void addCategory(ActionEvent event) throws IOException 
    {
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/AddCategory.fxml"));
            Parent root = (Parent)loader.load();
            AddCategoryController addCategoryCon = loader.getController();
        
            addCategoryCon.setModel(pmcmodel);
        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    @FXML
    private void removeCategory(ActionEvent event) throws SQLException, IOException
    {
       if (chosenTableView==1 && categories.getSelectionModel().getSelectedItem()!=null){
           pmcmodel.removeCategory(categories.getSelectionModel().getSelectedItem());
           
           
       }
    

    
}

    @FXML
    private void categoriesViewChosen(MouseEvent event) throws IOException, SQLException
    {
        chosenTableView=1;
        Category chosenCategory= categories.getSelectionModel().getSelectedItem();
        if (chosenCategory!=null)
        {
            pmcmodel.setCatMovies(chosenCategory);
            catmovies.setItems(pmcmodel.getCatMovies());
        }
    }

    @FXML
    private void categoryMovieViewChosen(MouseEvent event)
    {
        chosenTableView=2;
    }

    @FXML
    private void addMovieToCategory(ActionEvent event) throws IOException, SQLException
    {
    Category chosenCategory= categories.getSelectionModel().getSelectedItem();
    Movie chosenMovie=allMovies.getSelectionModel().getSelectedItem();
        if (chosenCategory!=null && chosenMovie!=null){
            pmcmodel.addMovieToCat(chosenCategory, chosenMovie);
        }
    }
    }
