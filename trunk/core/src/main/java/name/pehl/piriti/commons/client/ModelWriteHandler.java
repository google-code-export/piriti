package name.pehl.piriti.commons.client;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for {@link ModelWriteEvent} events.
 * 
 * @author $LastChangedBy:$
 * @version $LastChangedRevision:$
 * @param <T>
 *            the model type
 */
public interface ModelWriteHandler<T> extends EventHandler
{
    /**
     * Called <em>before</em> the model instance is written as JSON / XML.
     * 
     * @param event
     *            the {@link ModelReadEvent} that was fired
     */
    void onModelWrite(ModelWriteEvent<T> event);
}
