/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.be;

/**
 *
 * @author Philip
 */
public class IMDBMovie
{
private String movieId;
private String movieTitle;

public IMDBMovie(String id, String title)
{
    this.movieId=id;
    this.movieTitle=title;
}

public String getMovieId()
{
    return movieId;
}

public String getMovieTitle()
{
    return movieTitle;
}

public String toString()
{
    return movieTitle;
}
}
