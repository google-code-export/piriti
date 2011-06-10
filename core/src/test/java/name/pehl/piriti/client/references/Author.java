package name.pehl.piriti.client.references;

import name.pehl.piriti.commons.client.Mapping;
import name.pehl.piriti.commons.client.Mappings;
import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.json.client.JsonWriter;
import name.pehl.piriti.xml.client.XmlReader;
import name.pehl.piriti.xml.client.XmlWriter;

import com.google.gwt.core.client.GWT;

/**
 * @author $Author: harald.pehl $
 * @version $Date: 2010-02-15 16:03:08 +0100 (Mo, 15 Feb 2010) $ $Revision: 131
 *          $
 */
// @formatter:off
public class Author
{
    // --------------------------------------------------- json reader / writer

    public interface AuthorJsonReader extends JsonReader<Author> {}
    public static final AuthorJsonReader JSON_READER = GWT.create(AuthorJsonReader.class);

    public interface AuthorJsonWriter extends JsonWriter<Author> {}
    public static final AuthorJsonWriter JSON_WRITER = GWT.create(AuthorJsonWriter.class);

    // ---------------------------------------------------- xml reader / writer

    @Mappings(@Mapping(value = "bestseller", path = "bestseller/book"))
    public interface AuthorXmlReader extends XmlReader<Author> {}
    public static final AuthorXmlReader XML_READER = GWT.create(AuthorXmlReader.class);

    @Mappings(@Mapping(value = "bestseller", path = "bestseller/book"))
    public interface AuthorXmlWriter extends XmlWriter<Author> {}
    public static final AuthorXmlWriter XML_WRITER = GWT.create(AuthorXmlWriter.class);

    // ------------------------------------------------------------------- data

    String firstname;
    String surname;
    Book bestseller;
}
