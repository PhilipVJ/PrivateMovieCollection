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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    if (filelink.getText().length()!=0 && title.getText().length()!=0 && IMDBrating.getText().length()!=0)
    {
    String rat = IMDBrating.getText();
    double ratDouble = Double.parseDouble(rat);
    System.out.println("Rating:"+ratDouble);
    String movieTitle = title.getText();
        System.out.println("Title: "+movieTitle);
    String movieFilelink = filelink.getText();
        System.out.println("Filelink: "+movieFilelink);
    
    
    pmcmodel.addMovie(movieFilelink, movieTitle, ratDouble);
    Stage stage = (Stage) rootPane2.getScene().getWindow();
    stage.close();
    }
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
    fileChooser.setTitle("Open Music File");
    Stage stage = (Stage) rootPane2.getScene().getWindow();
    File mediafile = fileChooser.showOpenDialog(stage);
    String path = mediafile.toURI().toString();
    filelink.setText(path);
    
    }


}
