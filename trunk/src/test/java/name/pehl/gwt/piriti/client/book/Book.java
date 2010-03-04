package name.pehl.gwt.piriti.client.book;

import java.util.List;

import name.pehl.gwt.piriti.client.json.JsonField;
import name.pehl.gwt.piriti.client.xml.XmlField;
import name.pehl.gwt.piriti.client.xml.XmlReader;

import com.google.gwt.core.client.GWT;

/**
 * @author $Author: harald.pehl $
 * @version $Date: 2010-02-15 16:03:08 +0100 (Mo, 15 Feb 2010) $ $Revision: 131
 *          $
 */
public class Book
{
    public interface BookXmlReader extends XmlReader<Book>
    {
    }

    public static final BookXmlReader XML = GWT.create(BookXmlReader.class);

    // public interface BookJsonReader extends JsonReader<Book>
    // {
    // }
    //
    // public static final BookJsonReader JSON =
    // GWT.create(BookJsonReader.class);

    @XmlField
    @JsonField
    public String isbn;

    @XmlField
    @JsonField
    public int pages;

    @XmlField
    @JsonField
    public String title;

    @XmlField
    @JsonField
    public Author author;

    @XmlField("reviews/review")
    @JsonField
    public List<String> reviews;
}
