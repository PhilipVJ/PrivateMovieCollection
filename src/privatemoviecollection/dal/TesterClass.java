/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import java.util.List;
import privatemoviecollection.be.IMDBMovie;

/**
 *
 * @author Philip
 */
public class TesterClass
{
public static void main(String[] args) {
    IMDBDbDAO tester = new IMDBDbDAO();
    List<IMDBMovie> test = tester.getIMDBTop250();
    System.out.println(""+test.size());
    for(IMDBMovie x:test){
        System.out.println(""+x.getMovieId()+"    "+x.getRating()+"    "+x.getMovieTitle());
    }
   
}
}