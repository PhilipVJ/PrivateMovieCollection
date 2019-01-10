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
import javafx.scene.control.TextField;
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
    private TableColumn<Movie, String> allMovIMDBRating;
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
    private TableColumn<Movie, String> catIMDBrating;
    @FXML
    private TableColumn<Movie, String> catTitle;
    @FXML
    private TableColumn<Movie, String> catPersonalrating;
    @FXML
    private TextField lowRating;
    @FXML
    private Button searchRatings;
    @FXML
    private TextField highRating;
    @FXML
    private Label ratingWarning;
    @FXML
    private TextField searchField;

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
            generateErrorAlarm("Database.info could not be located");
            
            
        } catch (SQLException ex)
        {
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
            generateErrorAlarm("A problem occurred with the SQL database");
        }
     
     
        
      
    }    

    @FXML
    private void addMovie(ActionEvent event)
    {
       
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/AddMovie.fxml"));
            Parent root = (Parent)loader.load();
            AddMovieController addMovieCon = loader.getController();
        
            addMovieCon.setModel(pmcmodel);
        
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex)
        {
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
            generateErrorAlarm("The AddMovie.fxml file could not be located");
        }
 
    }

    @FXML
    private void deleteFromCategory(ActionEvent event)
    {
        if ( catmovies.getSelectionModel().getSelectedItem()!=null)
            {
            
            try {
                Category selectedCategory = categories.getSelectionModel().getSelectedItem();
                Movie movToDelete = catmovies.getSelectionModel().getSelectedItem();
                
                pmcmodel.deleteMovieFromCategory(selectedCategory, movToDelete);
                categories.setItems(pmcmodel.getAllCategories());
            } catch (IOException ex) {
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("Database.info could not be located");
            } catch (SQLException ex) {
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("A problem occurred with the SQL database");
            }
           
            }
                
                
            
        
    }
    
    @FXML
    private void openMovie(ActionEvent event) 
    {
        System.out.println("Opens movie");
        try
        {
            Movie movieToPlay = allMovies.getSelectionModel().getSelectedItem();
            String path = movieToPlay.getFileLink();
            
            java.util.Date date=new java.util.Date();
            pmcmodel.setDate(movieToPlay, date);
            setLastSeenInfo(movieToPlay);
            
            Desktop.getDesktop().open(new File(path));
        } catch (IOException ex)
        {
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
            generateErrorAlarm("Database.info could not be located");
        } catch (SQLException ex)
        {
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
            generateErrorAlarm("A problem occurred with the SQL database");
        } catch (IllegalArgumentException ex){
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
            generateErrorAlarm("Could not locate mediafile");
        }
        
        
    
 
      
        
        
    }

    @FXML
    private void deleteMovie(ActionEvent event)
    {
        try
        {
            Movie movieToRemove = allMovies.getSelectionModel().getSelectedItem();
            if(movieToRemove!=null){
            pmcmodel.removeMovie(movieToRemove);
            }
        } catch (IOException ex)
        {
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
            generateErrorAlarm("Database.info could not be located");
        } catch (SQLException ex)
        {
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
            generateErrorAlarm("A problem occurred with the SQL database");
        }
        
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
            makeRatingVisible();
            Movie chosenMov = allMovies.getSelectionModel().getSelectedItem();
            setLastSeenInfo(chosenMov);

        }
    }

    public void makeRatingVisible()
    {
        ratingSlider.setVisible(true);
        rateButton.setVisible(true);
        scoreLabel.setVisible(true);
        
    }
    public void makeRatingInvisible()
    {
        ratingSlider.setVisible(false);
        rateButton.setVisible(false);
        scoreLabel.setVisible(false);
    }
  

    @FXML
    private void rateMovie(ActionEvent event)
    {
        double rating = ratingSlider.getValue();
        double oneDigitRating = Math.round(rating * 10) / 10.0;
        if(chosenTableView==3 && allMovies.getSelectionModel().getSelectedItem()!=null){
            try {
                pmcmodel.rateMovie(allMovies.getSelectionModel().getSelectedItem(), oneDigitRating);
                allMovies.refresh();
                catmovies.refresh();
                return;
             } catch (IOException ex){
              Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
             generateErrorAlarm("Database.info could not be located");
             } catch (SQLException ex){
            Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
            generateErrorAlarm("Could not get access to the SQL database");
             }
        
        if(chosenTableView==2 && catmovies.getSelectionModel().getSelectedItem()!=null){
                try {
                    pmcmodel.rateMovie(catmovies.getSelectionModel().getSelectedItem(), oneDigitRating);
                    allMovies.refresh();
                    catmovies.refresh();
                } catch (IOException ex){
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("Database.info could not be located");
                } catch (SQLException ex){
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("Could not get access to the SQL database");
                }
              }
        }
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
    private void addCategory(ActionEvent event) 
    {
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/AddCategory.fxml"));
                Parent root = (Parent)loader.load();
                AddCategoryController addCategoryCon = loader.getController();
                
                addCategoryCon.setModel(pmcmodel);
                
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex)
            {
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("The AddCategory.fxml file could not be located");
            }
        }
    }

    @FXML
    private void removeCategory(ActionEvent event)
    {
       if (chosenTableView==1 && categories.getSelectionModel().getSelectedItem()!=null){
           try
           {
               pmcmodel.removeCategory(categories.getSelectionModel().getSelectedItem());
                } catch (IOException ex){
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("Database.info could not be located");
                } catch (SQLException ex){
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("Could not get access to the SQL database");
           
                }
       }
    

    
}

    @FXML
    private void categoriesViewChosen(MouseEvent event)
    {
        chosenTableView=1;
        makeRatingInvisible();
        makeLastSeenInfoInvisible();
        Category chosenCategory= categories.getSelectionModel().getSelectedItem();
        if (chosenCategory!=null)
        {
            try {
                System.out.println("Setting up category movies");
                pmcmodel.setCatMovies(chosenCategory);
                catmovies.setItems(pmcmodel.getCatMovies());
                } catch (IOException ex){
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("Database.info could not be located");
                } catch (SQLException ex){
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("Could not get access to the SQL database");
                }
            
        }
    }

    @FXML
    private void categoryMovieViewChosen(MouseEvent event)
    {
        chosenTableView=2;
        Movie chosenMovie= catmovies.getSelectionModel().getSelectedItem();
        if (chosenMovie!=null)
        {
            setLastSeenInfo(chosenMovie);
            makeRatingVisible();
        }
    }

    @FXML
    private void addMovieToCategory(ActionEvent event)
    {
    Category chosenCategory= categories.getSelectionModel().getSelectedItem();
    Movie chosenMovie=allMovies.getSelectionModel().getSelectedItem();
        if (chosenCategory!=null && chosenMovie!=null){
        try
        {
            pmcmodel.addMovieToCat(chosenCategory, chosenMovie);
                } catch (IOException ex){
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("Database.info could not be located");
                } catch (SQLException ex){
                Logger.getLogger(PMCViewController.class.getName()).log(Level.SEVERE, null, ex);
                generateErrorAlarm("Could not get access to the SQL database");
                }
        }
    }

    @FXML
    private void searchIMDBRating(ActionEvent event) throws IOException, SQLException
    {
        
        
        try 
        {
            double lowS = Double.parseDouble(lowRating.getText());
            double highS = Double.parseDouble(highRating.getText());
            
            
            if(!lowRating.getSelectedText().isEmpty() && !highRating.getSelectedText().isEmpty())
            {
                pmcmodel.IMDBintervalSearch(lowS, highS);
                
            }
        } 
        catch (NumberFormatException nfe) 
        {
            ratingWarning.setText("Try 3.0 - 6.0");
        }
    }
    
    private void setLastSeenInfo(Movie chosenMov)
    {
            if(chosenMov.getDate()!=null){
            lastSeenLabel.setText(chosenMov.getTitle()+" was last seen: "+chosenMov.getDate());
            }
            else{
            lastSeenLabel.setText("You haven't seen this movie yet");
            }
    }
    
    private void makeLastSeenInfoInvisible()
    {
        lastSeenLabel.setText("");
    }

    @FXML
    private void searchButton(ActionEvent event) throws IOException, SQLException
    {
        String searchWord = searchField.getText();
        if (searchWord.length()!=0)
        {
            pmcmodel.getMoviesWithSearchWord(searchWord);
            
        }
        
        
    }

    @FXML
    private void clearSearches(ActionEvent event) throws IOException, SQLException
    {
        pmcmodel.clearSearches();
        searchField.clear();
        lowRating.clear();
        highRating.clear();
    }
    
    private void generateErrorAlarm(String message)
    {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Important information");
      alert.setHeaderText("An error has occured");
      alert.setContentText(""+message);
      alert.showAndWait(); 
    }
}
