/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import privatemoviecollection.be.IMDBMovie;
import privatemoviecollection.gui.model.PMCModel;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class IMDBsuggestionsController implements Initializable
{

    @FXML
    private ListView<IMDBMovie> suggestions;
    
    private PMCModel pmcmodel;
    @FXML
    private AnchorPane rootPane2;
    private AddMovieController prevController;
    
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }   
    
    public void setModel(PMCModel currentModel)
    {
        pmcmodel=currentModel;
    }

    void setSearch(String text)
    {
        
        ObservableList<IMDBMovie> allSuggestions = FXCollections.observableList(pmcmodel.getMovieSuggestions(text));
        suggestions.setItems(allSuggestions);
    }

    @FXML
    private void save(ActionEvent event)
    {
        IMDBMovie chosenMovie = suggestions.getSelectionModel().getSelectedItem();
        
        prevController.setTitleAndRating(chosenMovie.getMovieTitle(), pmcmodel.getRating(chosenMovie.getMovieId()));
        Stage stage = (Stage) rootPane2.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel(ActionEvent event)
    {
    Stage stage = (Stage) rootPane2.getScene().getWindow();
    stage.close();
    }
    
    public void setPrevController(AddMovieController controller)
    {
        this.prevController=controller;
    }
    
}