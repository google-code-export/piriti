package name.pehl.piriti.client.gwttest.book;

import java.util.List;

import name.pehl.piriti.client.json.JsonField;
import name.pehl.piriti.client.json.JsonReader;
import name.pehl.piriti.client.xml.XmlField;
import name.pehl.piriti.client.xml.XmlReader;

import com.google.gwt.core.client.GWT;

/**
 * @author $Author: harald.pehl $
 * @version $Date: 2010-02-15 16:03:08 +0100 (Mo, 15 Feb 2010) $ $Revision: 131
 *          $
 */
public class Book
{
    public interface BookJsonReader extends JsonReader<Book>
    {
    }
    
    public static final BookJsonReader JSON = GWT.create(BookJsonReader.class);
    
    public interface BookXmlReader extends XmlReader<Book>
    {
    }

    public static final BookXmlReader XML = GWT.create(BookXmlReader.class);

    @JsonField
    @XmlField
    String isbn;

    @JsonField
    @XmlField
    int pages;

    @JsonField
    @XmlField
    String title;

    @JsonField
    @XmlField
    Author author;

    @JsonField
    @XmlField("reviews/review")
    List<String> reviews;
    
    @JsonField
    @XmlField("related/book")
    List<Book> related;
}