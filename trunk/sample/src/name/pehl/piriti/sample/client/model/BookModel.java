package name.pehl.piriti.sample.client.model;

import java.util.List;

import name.pehl.piriti.gxt.client.json.JsonField;
import name.pehl.piriti.gxt.client.json.JsonModel;
import name.pehl.piriti.gxt.client.json.JsonModelReader;
import name.pehl.piriti.gxt.client.xml.XmlField;
import name.pehl.piriti.gxt.client.xml.XmlModel;
import name.pehl.piriti.gxt.client.xml.XmlModelReader;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.google.gwt.core.client.GWT;

/**
 * @author $Author: harald.pehl $
 * @version $Date: 2010-05-18 23:30:44 +0200 (Di, 18 Mai 2010) $ $Revision: 327
 *          $
 */
@XmlModel( {@XmlField(property = "isbn", type = String.class), @XmlField(property = "pages", type = Integer.class),
        @XmlField(property = "title", type = String.class), @XmlField(property = "author", type = Author.class),
        @XmlField(path = "reviews/review", property = "reviews", type = List.class, typeVariable = String.class)})
@JsonModel( {@JsonField(property = "isbn", type = String.class), @JsonField(property = "pages", type = Integer.class),
        @JsonField(property = "title", type = String.class), @JsonField(property = "author", type = Author.class),
        @JsonField(property = "reviews", type = List.class, typeVariable = String.class)})
public class BookModel extends BaseModel
{
    public interface BookXmlReader extends XmlModelReader<BookModel>
    {
    }

    public static final BookXmlReader XML = GWT.create(BookXmlReader.class);

    public interface BookJsonReader extends JsonModelReader<BookModel>
    {
    }

    public static final BookJsonReader JSON = GWT.create(BookJsonReader.class);
}