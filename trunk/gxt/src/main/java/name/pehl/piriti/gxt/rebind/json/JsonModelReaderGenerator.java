package name.pehl.piriti.gxt.rebind.json;

import name.pehl.piriti.client.json.JsonReader;
import name.pehl.piriti.rebind.AbstractReaderCreator;
import name.pehl.piriti.rebind.AbstractReaderGenerator;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * GWT Generator for JsonReaders. The generator delegates to
 * {@link JsonModelReaderCreator} which is responsible for generating the
 * code.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 137 $
 */
public class JsonModelReaderGenerator extends AbstractReaderGenerator
{
    @Override
    protected AbstractReaderCreator createCreator(GeneratorContext context, JClassType interfaceType, String implName,
            TreeLogger logger) throws UnableToCompleteException
    {
        return new JsonModelReaderCreator(context, interfaceType, implName, JsonReader.class.getCanonicalName(), logger);
    }
}
