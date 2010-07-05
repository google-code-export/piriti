package name.pehl.piriti.rebind.fieldhandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import name.pehl.piriti.rebind.CodeGeneration;
import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.TypeUtils;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * Abstract {@link FieldHandler} for collections.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 140 $
 */
public abstract class AbstractCollectionFieldHandler extends AbstractFieldHandler
{
    public static Map<String, String> interfaceToImplementation = new HashMap<String, String>();
    static
    {
        interfaceToImplementation.put(Collection.class.getName(), ArrayList.class.getName());
        interfaceToImplementation.put(List.class.getName(), ArrayList.class.getName());
        interfaceToImplementation.put(Set.class.getName(), HashSet.class.getName());
        interfaceToImplementation.put(SortedSet.class.getName(), TreeSet.class.getName());
    }


    /**
     * Returns <code>false</code> if the field type is no collection, if the
     * collection has no type arguments or if the type argument of the
     * collection is an array, collection or map, <code>true</code> otherwise.
     * 
     * @param writer
     * @param fieldContext
     * @return
     * @see name.pehl.piriti.rebind.fieldhandler.AbstractFieldHandler#isValid(name.pehl.piriti.rebind.fieldhandler.FieldContext)
     */
    @Override
    public boolean isValid(IndentedWriter writer, FieldContext fieldContext) throws UnableToCompleteException
    {
        if (!fieldContext.isCollection())
        {
            skipField(writer, fieldContext, "Type is no collection");
            return false;
        }
        JClassType parameterType = getTypeVariable(fieldContext);
        if (parameterType != null)
        {
            if (parameterType.isArray() != null || TypeUtils.isCollection(parameterType)
                    || TypeUtils.isMap(parameterType))
            {
                skipField(writer, fieldContext, "Nested arrays / collections / maps are not supported");
                return false;
            }
        }
        else
        {
            // collections and maps without type arguments are not
            // supported!
            skipField(writer, fieldContext, "Collection has no type argument");
            return false;
        }
        // Initialize the parameter type to make sure the relevant reader
        // is in the registry (ugly - but it works)
        CodeGeneration.writeReaderInitialization(writer, parameterType);
        return true;
    }


    protected JClassType getTypeVariable(FieldContext fieldContext)
    {
        return TypeUtils.getTypeVariable(fieldContext.getFieldType());
    }
}
