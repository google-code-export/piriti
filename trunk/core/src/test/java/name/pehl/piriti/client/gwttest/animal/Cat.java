package name.pehl.piriti.client.gwttest.animal;

import name.pehl.piriti.client.json.JsonField;
import name.pehl.piriti.client.json.JsonReader;
import name.pehl.piriti.client.json.JsonWriter;
import name.pehl.piriti.client.xml.XmlField;
import name.pehl.piriti.client.xml.XmlReader;
import name.pehl.piriti.client.xml.XmlWriter;

import com.google.gwt.core.client.GWT;

/**
 * @author $Author$
 * @version $Date$ $Revision: 686
 *          $
 */
public class Cat extends Mammal
{
    // --------------------------------------------------- json reader / writer

    // @formatter:off
    public interface CatJsonReader extends JsonReader<Cat> {}
    public static final CatJsonReader JSON_READER = GWT.create(CatJsonReader.class);

    public interface CatJsonWriter extends JsonWriter<Cat> {}
    public static final CatJsonWriter JSON_WRITER = GWT.create(CatJsonWriter.class);
    // @formatter:on

    // ---------------------------------------------------- xml reader / writer

    // @formatter:off
    public interface CatXmlReader extends XmlReader<Cat> {}
    public static final CatXmlReader XML_READER = GWT.create(CatXmlReader.class);

    public interface CatXmlWriter extends XmlWriter<Cat> {}
    public static final CatXmlWriter XML_WRITER = GWT.create(CatXmlWriter.class);
    // @formatter:on

    // ------------------------------------------------------------------- data

    @JsonField
    @XmlField
    String color;
}
