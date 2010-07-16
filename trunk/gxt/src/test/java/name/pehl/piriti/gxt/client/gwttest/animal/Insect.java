package name.pehl.piriti.gxt.client.gwttest.animal;

import name.pehl.piriti.gxt.client.json.JsonField;
import name.pehl.piriti.gxt.client.json.JsonFields;
import name.pehl.piriti.gxt.client.json.JsonModelReader;
import name.pehl.piriti.gxt.client.xml.XmlField;
import name.pehl.piriti.gxt.client.xml.XmlFields;
import name.pehl.piriti.gxt.client.xml.XmlModelReader;

import com.google.gwt.core.client.GWT;

/**
 * @author $Author$
 * @version $Date$ $Revision$
 */
@JsonFields(@JsonField(name = "flies", type = Boolean.class))
@XmlFields(@XmlField(name = "flies", type = Boolean.class))
public class Insect extends Animal
{
    public interface InsectJsonReader extends JsonModelReader<Insect>
    {
    }

    public static final InsectJsonReader JSON = GWT.create(InsectJsonReader.class);

    public interface InsectXmlReader extends XmlModelReader<Insect>
    {
    }

    public static final InsectXmlReader XML = GWT.create(InsectXmlReader.class);
}
