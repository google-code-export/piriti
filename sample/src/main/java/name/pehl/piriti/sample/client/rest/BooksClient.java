package name.pehl.piriti.sample.client.rest;

import java.io.IOException;
import java.util.List;

import name.pehl.piriti.client.json.JsonReader;
import name.pehl.piriti.client.xml.XmlReader;
import name.pehl.piriti.restlet.client.json.PiritiJsonRepresentation;
import name.pehl.piriti.restlet.client.xml.PiritiXmlRepresentation;
import name.pehl.piriti.sample.client.event.BooksEvent;
import name.pehl.piriti.sample.client.event.EventBus;
import name.pehl.piriti.sample.client.util.StopWatch;
import name.pehl.piriti.sample.client.util.TimeInterval;

import org.restlet.client.Request;
import org.restlet.client.Response;
import org.restlet.client.Uniform;
import org.restlet.client.data.MediaType;
import org.restlet.client.resource.ClientResource;

/**
 * @author $Author$
 * @version $Date$ $Revision: 299
 *          $
 */
public class BooksClient
{
    public static final String URL = "/rest/v1/books";


    public <T> void readFromJson(final JsonReader<T> jsonReader, final String sourceCode)
    {
        ClientResource clientResource = new ClientResource(URL);
        clientResource.setOnResponse(new Uniform()
        {
            @Override
            public void handle(Request request, Response response)
            {
                BooksEvent booksEvent = null;
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                PiritiJsonRepresentation<T> representation = new PiritiJsonRepresentation<T>(jsonReader, response
                        .getEntity());
                try
                {
                    List<T> books = representation.getModels();
                    TimeInterval timeInterval = stopWatch.stop();
                    booksEvent = new BooksEvent(books, timeInterval, sourceCode);
                }
                catch (IOException e)
                {
                    booksEvent = new BooksEvent(null, null, null);
                }
                EventBus.get().fireEvent(booksEvent);
            }
        });
        clientResource.get(MediaType.APPLICATION_JSON);
    }


    public <T> void readFromXml(final XmlReader<T> xmlReader, final String sourceCode)
    {
        ClientResource clientResource = new ClientResource(URL);
        clientResource.setOnResponse(new Uniform()
        {
            @Override
            public void handle(Request request, Response response)
            {
                BooksEvent booksEvent = null;
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                PiritiXmlRepresentation<T> representation = new PiritiXmlRepresentation<T>(xmlReader, response
                        .getEntity());
                try
                {
                    List<T> books = representation.getModels();
                    TimeInterval timeInterval = stopWatch.stop();
                    booksEvent = new BooksEvent(books, timeInterval, sourceCode);
                }
                catch (IOException e)
                {
                    booksEvent = new BooksEvent(null, null, null);
                }
                EventBus.get().fireEvent(booksEvent);
            }
        });
        clientResource.get(MediaType.TEXT_XML);
    }
}
