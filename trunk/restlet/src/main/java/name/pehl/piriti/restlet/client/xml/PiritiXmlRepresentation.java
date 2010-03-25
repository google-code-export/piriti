package name.pehl.piriti.restlet.client.xml;

import java.io.IOException;
import java.util.List;

import name.pehl.piriti.client.xml.XmlReader;
import name.pehl.piriti.restlet.client.PiritiRepresentation;

import org.restlet.client.data.MediaType;
import org.restlet.client.ext.xml.DomRepresentation;
import org.restlet.client.representation.Representation;

import com.google.gwt.xml.client.Document;

/**
 * Representation which uses an {@link XmlReader} for converting XML to an
 * instance of T.
 * 
 * @param <T>
 *            The model type
 * @author $Author$
 * @version $Date$ $Revision: 264
 *          $
 */
public class PiritiXmlRepresentation<T> extends DomRepresentation implements PiritiRepresentation<T>
{
    /** The XmlReader for converting the XML to an instance of T. */
    private final XmlReader<T> xmlReader;


    // ----------------------------------------------------------- constructors

    /**
     * Constructor for an empty document.
     * 
     * @param xmlReader
     *            The XmlReader for converting the XML to an instance of T.
     * @param mediaType
     *            The representation's media type.
     */
    public PiritiXmlRepresentation(XmlReader<T> xmlReader, MediaType mediaType)
    {
        super(mediaType);
        this.xmlReader = xmlReader;
    }


    /**
     * Constructor from an existing DOM document.
     * 
     * @param xmlReader
     *            The XmlReader for converting the XML to an instance of T.
     * @param mediaType
     *            The representation's media type.
     * @param xmlDocument
     *            The source DOM document.
     */
    public PiritiXmlRepresentation(XmlReader<T> xmlReader, MediaType mediaType, Document xmlDocument)
    {
        super(mediaType, xmlDocument);
        this.xmlReader = xmlReader;
    }


    /**
     * Constructor.
     * 
     * @param xmlReader
     *            The XmlReader for converting the XML to an instance of T.
     * @param xmlRepresentation
     *            A source XML representation to parse.
     */
    public PiritiXmlRepresentation(XmlReader<T> xmlReader, Representation xmlRepresentation)
    {
        super(xmlRepresentation);
        this.xmlReader = xmlReader;
    }


    // --------------------------------------------------------- public methods

    /**
     * @return the XmlReader for converting the XML to an instance of T.
     */
    public XmlReader<T> getXmlReader()
    {
        return xmlReader;
    }


    /**
     * Converts the XML to an instance of T using the {@link XmlReader} given as
     * constructor argument. Returns null if {@link #getDocument()} or
     * {@link XmlReader} is null.
     * 
     * @return the converted instance of T or null if {@link #getDocument()} or
     *         {@link XmlReader} is null.
     * @throws IOException
     */
    @Override
    public T getModel() throws IOException
    {
        T model = null;
        Document document = getDocument();
        if (document != null)
        {
            model = xmlReader.read(document);
        }
        return model;
    }


    /**
     * Converts the XML to a list of Ts using the {@link XmlReader} given as
     * constructor argument. Returns null if {@link #getDocument()} or
     * {@link XmlReader} is null. More precisely the child elements of the
     * documents root element are converted to instances of T.
     * 
     * @return the list of converted Ts or null if {@link #getDocument()} or
     *         {@link XmlReader} is null.
     * @throws IOException
     */
    @Override
    public List<T> getModels() throws IOException
    {
        List<T> models = null;
        Document document = getDocument();
        if (document != null)
        {
            models = xmlReader.readList(document);
        }
        return models;
    }
}
