package name.pehl.piriti.client.restlet;

import java.io.IOException;

/**
 * Common interface for resources which rely on Piriti readers.
 * 
 * @param <T>
 *            The model type
 * @author $Author:$
 * @version $Date:$ $Revision:$
 */
public interface PiritiResource<T>
{
    /**
     * Return an instance of T converted by a Piriti reader.
     * 
     * @return
     * @throws IOException
     */
    T getModel() throws IOException;
}
