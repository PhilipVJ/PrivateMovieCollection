/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.be;

import java.util.ArrayList;

/**
 *
 * @author Philip
 */
public class Category
{
    private String name; 
    private int id; 
    private ArrayList<Integer> movieIdList;

    public Category (String name, int id) {
        this.name = name;
        this.id = id; 
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
    
    
}
