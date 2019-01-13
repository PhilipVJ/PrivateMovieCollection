/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import privatemoviecollection.be.IMDBMovie;
import privatemoviecollection.gui.model.PMCModel;

/**
 * FXML Controller class
 *
 * @author Philip
 */
public class RecommendationsController implements Initializable
{

    @FXML
    private TableColumn<String, IMDBMovie> title;
    @FXML
    private TableColumn<String, IMDBMovie> rating;
    private PMCModel pmcmodel;
    @FXML
    private TableView<IMDBMovie> recommendations;
    @FXML
    private Label nowShowing;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
    title.setCellValueFactory(new PropertyValueFactory<>("movieTitle"));
    rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
    
    }    

    @FXML
    private void cancel(ActionEvent event)
    {
        Stage stageToClose = (Stage) recommendations.getScene().getWindow();
        stageToClose.close();
        
    }

    public void setModel(PMCModel pmcmodel)
    {
        this.pmcmodel = pmcmodel;
    }
    
    public void setTableView()
    {
        ObservableList<IMDBMovie> highR = pmcmodel.getHighRatedMovies();
        recommendations.setItems(highR);
        
    }

    @FXML
    private void showTop250(ActionEvent event)
    {
        ObservableList<IMDBMovie> top250 = pmcmodel.getTop250Movies();
        recommendations.setItems(top250);
        nowShowing.setText("Here is the IMDB Top 250 list");
        nowShowing.setAlignment(Pos.CENTER);
    }
    
}
