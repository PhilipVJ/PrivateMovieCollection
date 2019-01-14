/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    @FXML
    private Label updatedLabel;
    @FXML
    private Button updateIMDBdatabaseId;
    @FXML
    private Label lastUpdatedLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {

    }

    public void setModel(PMCModel model)
    {
        pmcmodel = model;
        lastUpdatedLabel.setText(pmcmodel.getLastUpdatedData());
    }

    @FXML
    private void saveMovie(ActionEvent event)
    {
        try
        {
            if (verifyTextFields() == false)
            {
                return;
            }
            // Saves movie without IMDB rating
            if (IMDBrating.getText().equals("No rating found") || IMDBrating.getText().length() == 0)
            {
                String movieTitle = title.getText();
                String movieFilelink = filelink.getText();
                // If no IMDB has been typed it will be given a score of 1000
                boolean checker = pmcmodel.addMovie(movieFilelink, movieTitle, 1000);
                if (checker == false)
                {
                    pmcmodel.duplicateAlarm();
                    return;

                }
                Stage stage = (Stage) rootPane2.getScene().getWindow();
                stage.close();
                return;
            }

            // Saves movie with a real IMDB score
            String rat = IMDBrating.getText();
            double ratDouble = Double.parseDouble(rat);
            String movieTitle = title.getText();
            String movieFilelink = filelink.getText();

            boolean checker = pmcmodel.addMovie(movieFilelink, movieTitle, ratDouble);
            if (checker == false)
            {
                pmcmodel.duplicateAlarm();
                return;
            }
            Stage stage = (Stage) rootPane2.getScene().getWindow();
            stage.close();
        } catch (IOException ex)
        {
            Logger.getLogger(AddMovieController.class.getName()).log(Level.SEVERE, null, ex);
            pmcmodel.generateErrorAlarm("Database.info could not be located");
        } catch (SQLException ex)
        {
            Logger.getLogger(AddMovieController.class.getName()).log(Level.SEVERE, null, ex);
            pmcmodel.generateErrorAlarm("A problem occurred with the SQL database");
        }

    }

    /**
     * This method verfies that textfields contains text.
     *
     * @return
     */
    public boolean verifyTextFields()
    {
        if (filelink.getText().length() == 0)
        {
            info.setText("Please select a movie");
            return false;
        }
        if (title.getText().length() == 0)
        {
            info.setText("Please type in a title");
            return false;
        }
        return true;
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
        if (mediafile != null)
        {

            String path = mediafile.getAbsolutePath();

            filelink.setText(path);
        }
    }

    @FXML
    private void getRating(ActionEvent event)
    {
        if (imdbUrl.getText().length() != 0)
        {
            // Formats the inserted URL
            int size = imdbUrl.getText().length();
            char lastLetter = imdbUrl.getText().charAt(size - 1);
            String lastLetter2 = String.valueOf(lastLetter);
            if (lastLetter2.equals("/"))
            {
                String deleteSlash = imdbUrl.getText().substring(0, size - 1);
                int lastIndexOfSlash = deleteSlash.lastIndexOf("/");
                String formattedMovieCode = deleteSlash.substring(lastIndexOfSlash + 1);
                IMDBrating.setText(pmcmodel.getRating(formattedMovieCode));

            } else
            {
                int lastIndexOfSlash = imdbUrl.getText().lastIndexOf("/");
                String formattedMovieCode = imdbUrl.getText().substring(lastIndexOfSlash + 1);
                IMDBrating.setText(pmcmodel.getRating(formattedMovieCode));
            }
        }
    }

    @FXML
    private void findIMDBsuggestion(ActionEvent event)
    {
        String textIMDB = title.getText();
        if (textIMDB.length() == 0)
        {
            title.setText("Please write something");
            return;
        }
        if (!textIMDB.isEmpty())
        {
            try
            {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/IMDBsuggestions.fxml"));
                Parent root = (Parent) loader.load();
                IMDBsuggestionsController imdbController = loader.getController();

                imdbController.setModel(pmcmodel);
                imdbController.SetTextFields(title, IMDBrating);

                imdbController.setSearch(title.getText());

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException ex)
            {
                Logger.getLogger(AddMovieController.class.getName()).log(Level.SEVERE, null, ex);
                pmcmodel.generateErrorAlarm("The IMDBsuggestions.fxml file could not be started");

            }
        }
    }

    public void setTitleAndRating(String foundTitle, String rating)
    {
        title.setText(foundTitle);
        IMDBrating.setText(rating);
    }

    @FXML
    private void updateIMDBdatabase(ActionEvent event)
    {
        try
        {
            boolean done = pmcmodel.updateIMDBdatabase();
            if (done == true)
            {
                updatedLabel.setText("IMDB data has been updated");
                updateIMDBdatabaseId.setVisible(false);
                lastUpdatedLabel.setText(pmcmodel.getLastUpdatedData());
            }
        } catch (IOException ex)
        {
            Logger.getLogger(AddMovieController.class.getName()).log(Level.SEVERE, null, ex);
            pmcmodel.generateErrorAlarm("Could not update IMDB database");
        }

    }

}
