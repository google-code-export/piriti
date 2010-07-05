package name.pehl.piriti.gxt.rebind.json.fieldhandler;

import name.pehl.piriti.gxt.rebind.CodeGeneration;
import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.fieldhandler.FieldContext;
import name.pehl.piriti.rebind.fieldhandler.FieldHandler;

import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * Simple {@link FieldHandler} implementation for strings.
 * 
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class StringFieldHandler extends name.pehl.piriti.rebind.json.fieldhandler.StringFieldHandler
{
    @Override
    public void writeAssignment(IndentedWriter writer, FieldContext fieldContext) throws UnableToCompleteException
    {
        CodeGeneration.writeAssignement(writer, fieldContext);
    }
}
