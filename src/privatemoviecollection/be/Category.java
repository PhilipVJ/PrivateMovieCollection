/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.be;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Philip
 */
public class Category
{
    private final String name; 
    private final int id; 
    private final ArrayList<Integer> movieIdList;
    private CheckBox select;
    private StringProperty test;



    public Category (String name, int id) {
        this.name = name;
        this.id = id; 
        movieIdList = new ArrayList<>();
        select= new CheckBox();
        test= new SimpleStringProperty();
    }
    
    public String getName()
    {
        return name;
    }

    public int getId()
    {
        return id;
    }

    public void addMovieWithID(int movieId)
    {
      movieIdList.add(movieId);
    }

    public ArrayList<Integer> getMovies()
    {
        return movieIdList;
    }
    
    public void removeMovieWithID(int movieId)
    {
        for (Integer x:movieIdList)
        {
            if(x==movieId){
                movieIdList.remove(x);
                return;
            }
        }
    }

    public CheckBox getSelect()
    {
        
        return select;
        
    }

    public void setSelect(CheckBox select)
    {
        
        this.select = select;
        
        test.set(Boolean.toString(select.isSelected()));
        
    }
    
        public StringProperty getTest()
    {
        return test;
    }
    
    
    
    
}
