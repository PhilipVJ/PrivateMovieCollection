/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import privatemoviecollection.be.IMDBMovie;

/**
 *
 * @author Philip
 */
public class IMDBDbDAO
{
/**
 * Returns the rating of the given IMDB url. Returns 1000 if no rating was found.
 * @param url
 * @return 
 */
public String getRating (String url)
{
TsvParserSettings settings = new TsvParserSettings(); 
settings.getFormat().setLineSeparator("\n");

TsvParser parser = new TsvParser(settings);
String source = "data/rating.tsv";
File title = new File(source);

// parses all rows in one go.
List<String[]> allRows = parser.parseAll(title);
for (String[] x: allRows)
{
    if(url.equals(x[0]))
    {
        return x[1];
    }
}

return "No rating found";

}

public ArrayList<IMDBMovie> getTitles(String movieSearch)
{
TsvParserSettings settings = new TsvParserSettings(); 
settings.getFormat().setLineSeparator("\n");

TsvParser parser = new TsvParser(settings);
String source = "data/title.tsv";
File title = new File(source);


parser.beginParsing(title);
ArrayList<IMDBMovie> allSearches = new ArrayList<>();

String[] row;
while ((row = parser.parseNext()) != null) {
  String lowerCaseSearch = row[3].toLowerCase();
  String lowerCaseInput = movieSearch.toLowerCase();
   if (lowerCaseSearch.contains(lowerCaseInput)){
       IMDBMovie movToAdd = new IMDBMovie(row[0], row[3]);
       allSearches.add(movToAdd);
   }     
}

return allSearches;
}
}
