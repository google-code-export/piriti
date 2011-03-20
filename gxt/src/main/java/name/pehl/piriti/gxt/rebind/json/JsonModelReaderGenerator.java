package name.pehl.piriti.gxt.rebind.json;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.rebind.AbstractCreator;
import name.pehl.piriti.rebind.AbstractGenerator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * GWT Generator which creates a {@link JsonModelReaderCreator}.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 137 $
 */
public class JsonModelReaderGenerator extends AbstractGenerator
{
    /**
     * Creates a {@link JsonModelReaderCreator}.
     * 
     * @param context
     * @param interfaceType
     * @param implName
     * @param logger
     * @return {@link JsonModelReaderCreator}
     * @throws UnableToCompleteException
     * @see name.pehl.piriti.rebind.AbstractGenerator#createCreator(com.google.gwt.core.ext.GeneratorContext,
     *      com.google.gwt.core.ext.typeinfo.JClassType, java.lang.String,
     *      com.google.gwt.core.ext.TreeLogger)
     */
    @Override
    protected AbstractCreator createCreator(GeneratorContext context, JClassType interfaceType, String implName,
            TreeLogger logger) throws UnableToCompleteException
    {
        return new JsonModelReaderCreator(context, interfaceType, implName, JsonReader.class.getCanonicalName(), logger);
    }
}
