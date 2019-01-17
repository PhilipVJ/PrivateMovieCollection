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

    private final String movieId;
    private String movieTitle;
    private double rating;
    private double weightedRating;
    private double numberOfVotes;

    public IMDBMovie(String id, String title)
    {
        this.movieId = id;
        this.movieTitle = title;
    }
    
    public IMDBMovie(String id)
    {
        this.movieId = id;
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

    public double getRating()
    {
        return rating;
    }

    public void setRating(String rating)
    {
        this.rating = Double.parseDouble(rating);
    }

    public void setMovieTitle(String movieTitle)
    {
        this.movieTitle = movieTitle;
    }

    public double getWeightedRating()
    {
        return weightedRating;
    }

    public void setNumberOfVotes(double numberOfVotes)
    {
        this.numberOfVotes = numberOfVotes;
    }

    /**
     * Uses an IMDB calculation to calculate the weighted rating used to rank
     * the top movies.
     *
     * @param averageRat
     */
    public void calculateWeightedRating(double averageRat)
    {
        weightedRating = (numberOfVotes / (numberOfVotes + 25000) * rating + (25000 / (numberOfVotes + 25000)) * averageRat);

    }

}
