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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import privatemoviecollection.gui.model.PMCModel;

/**
 * FXML Controller class
 *
 * @author Henrik Ferrari
 */
public class AddCategoryController implements Initializable {

    @FXML
    private AnchorPane rootPane3;
    
    private PMCModel pmcmodel;
    
    @FXML
    private Label info;
    @FXML
    private TextField addCategoryName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void setModel(PMCModel model)
    {
    pmcmodel=model;
    }
    
    @FXML
    private void saveBtn(ActionEvent event) throws IOException, SQLException 
    {
        if (addCategoryName.getText().length()==0)
        {
            info.setText("Please write a Category name");
            return;
        }
        
        String categoryName = addCategoryName.getText();
        
        pmcmodel.addCategory(categoryName);
        Stage stage = (Stage) rootPane3.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancelBtn(ActionEvent event)
    {
        Stage stage = (Stage) rootPane3.getScene().getWindow();
        stage.close();
    }


    
}