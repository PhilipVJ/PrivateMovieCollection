/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.dal;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import java.io.File;
import java.util.List;

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
TsvParserSettings settings = new TsvParserSettings(); //you will find MANY options here
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
    
}
