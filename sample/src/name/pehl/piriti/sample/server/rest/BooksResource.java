package name.pehl.piriti.sample.server.rest;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * @author $Author$
 * @version $Date$ $Revision: 272
 *          $
 */
public class BooksResource extends ServerResource
{
    @Get("json")
    public Representation listAsJson() throws JSONException
    {
        return loadResource("name/pehl/piriti/sample/server/rest/books.json", MediaType.APPLICATION_JSON);
    }


    @Get("xml")
    public Representation listAsXml() throws IOException
    {
        return loadResource("name/pehl/piriti/sample/server/rest/books.xml", MediaType.TEXT_XML);
    }


    private Representation loadResource(String resource, MediaType mediaType)
    {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = contextClassLoader.getResourceAsStream(resource);
        InputRepresentation representation = new InputRepresentation(stream, mediaType);
        return representation;
    }
}
