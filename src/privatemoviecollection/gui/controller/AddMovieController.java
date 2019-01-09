/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import static java.util.Locale.filter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.filechooser.FileNameExtensionFilter;
import privatemoviecollection.gui.model.PMCModel;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class AddMovieController implements Initializable
{
private PMCModel pmcmodel;
    @FXML
    private TextField title;
    @FXML
    private TextField filelink;
    @FXML
    private TextField IMDBrating;
    @FXML
    private AnchorPane rootPane2;
    @FXML
    private Label info;
    @FXML
    private TextField imdbUrl;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    
    
    public void setModel(PMCModel model)
    {
    pmcmodel=model;
    }

    @FXML
    private void saveMovie(ActionEvent event) throws IOException, SQLException
    {
    if (filelink.getText().length()==0)
    {
        info.setText("Please select a movie");
        return;
    }
    
    if (title.getText().length()==0){
        info.setText("Please type in a title");
        return;
    }
    
    if (IMDBrating.getText().equals("No rating found") || IMDBrating.getText().length()==0){
        String movieTitle = title.getText();
        String movieFilelink = filelink.getText();
        pmcmodel.addMovie(movieFilelink, movieTitle, 1000);
        Stage stage = (Stage) rootPane2.getScene().getWindow();
        stage.close();
        return;
    }
        

    String rat = IMDBrating.getText();
    double ratDouble = Double.parseDouble(rat);
    String movieTitle = title.getText();
    String movieFilelink = filelink.getText();
     
     pmcmodel.addMovie(movieFilelink, movieTitle, ratDouble);
    Stage stage = (Stage) rootPane2.getScene().getWindow();
    stage.close();
    
    }

    @FXML
    private void cancel(ActionEvent event)
    {
    Stage stage = (Stage) rootPane2.getScene().getWindow();
    stage.close();
    }

    @FXML
    private void findMovie(ActionEvent event)
    {
    FileChooser fileChooser = new FileChooser();
    // Adds filters to the FileChooser
    FileChooser.ExtensionFilter mp4filter = new FileChooser.ExtensionFilter("MP4 files (*.mp4)", "*.mp4");
    FileChooser.ExtensionFilter mpeg4filter = new FileChooser.ExtensionFilter("MPEG4 files (*.mpeg4)", "*.mpeg4");
    fileChooser.getExtensionFilters().add(mp4filter);
    fileChooser.getExtensionFilters().add(mpeg4filter);
    
    fileChooser.setTitle("Open Movie File");
    Stage stage = (Stage) rootPane2.getScene().getWindow();
    File mediafile = fileChooser.showOpenDialog(stage);
    if (mediafile!=null){
       
    String path = mediafile.getAbsolutePath();
        
    filelink.setText(path);
    }
    
    
    }

    @FXML
    private void getRating(ActionEvent event)
    {
    if(imdbUrl.getText().length()!=0)
    {
        int size = imdbUrl.getText().length();
        
        char lastLetter = imdbUrl.getText().charAt(size-1);
        String lastLetter2=String.valueOf(lastLetter);  
        if(lastLetter2.equals("/")){
            String deleteSlash = imdbUrl.getText().substring(0, size-1);
            int lastIndexOfSlash = deleteSlash.lastIndexOf("/");
            String formattedMovieCode = deleteSlash.substring(lastIndexOfSlash+1);
            IMDBrating.setText(pmcmodel.getRating(formattedMovieCode));
            
        }
        else{
            int lastIndexOfSlash = imdbUrl.getText().lastIndexOf("/");
            String formattedMovieCode = imdbUrl.getText().substring(lastIndexOfSlash+1);
            IMDBrating.setText(pmcmodel.getRating(formattedMovieCode));
        }
        
    }
    }

    @FXML
    private void findIMDBsuggestion(ActionEvent event) throws IOException
    {    
        String textIMDB = title.getText();
        if(textIMDB.length() == 0)
        {
            title.setText("Please write something");
            return;
        }    
        if(!textIMDB.isEmpty())
        {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/IMDBsuggestions.fxml"));
        Parent root = (Parent)loader.load();
        IMDBsuggestionsController imdbController = loader.getController();
        
        imdbController.setModel(pmcmodel);
        imdbController.setPrevController(this);
            
        imdbController.setSearch(title.getText());
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        }
    }
    
    public void setTitleAndRating(String foundTitle, String rating)
    {
        title.setText(foundTitle);
        IMDBrating.setText(rating);
    }


}
