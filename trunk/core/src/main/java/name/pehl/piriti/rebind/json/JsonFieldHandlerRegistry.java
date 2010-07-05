package name.pehl.piriti.rebind.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import name.pehl.piriti.rebind.TypeUtils;
import name.pehl.piriti.rebind.fieldhandler.FieldContext;
import name.pehl.piriti.rebind.fieldhandler.FieldHandler;
import name.pehl.piriti.rebind.fieldhandler.FieldHandlerRegistry;
import name.pehl.piriti.rebind.json.fieldhandler.ArrayFieldHandler;
import name.pehl.piriti.rebind.json.fieldhandler.BooleanFieldHandler;
import name.pehl.piriti.rebind.json.fieldhandler.CollectionFieldHandler;
import name.pehl.piriti.rebind.json.fieldhandler.ConverterFieldHandler;
import name.pehl.piriti.rebind.json.fieldhandler.EnumFieldHandler;
import name.pehl.piriti.rebind.json.fieldhandler.JsonRegistryFieldHandler;
import name.pehl.piriti.rebind.json.fieldhandler.NumberFieldHandler;
import name.pehl.piriti.rebind.json.fieldhandler.StringFieldHandler;

import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * {@link FieldHandlerRegistry} used by the {@link JsonReaderCreator}.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 136 $
 */
public class JsonFieldHandlerRegistry implements FieldHandlerRegistry
{
    private Map<String, FieldHandler> registry;


    /**
     * Construct a new instance of this class and registers the initial field
     * handlers.
     */
    public JsonFieldHandlerRegistry()
    {
        registry = new HashMap<String, FieldHandler>();
        registerInitialFieldHandlers();
    }


    /**
     * Registers the initial field handler for the json reader. The following
     * handlers are registered:
     * <ul>
     * <li>{@linkplain BooleanFieldHandler}
     * <ul>
     * <li>boolean, Boolean
     * </ul>
     * <li>{@link NumberFieldHandler}
     * <ul>
     * <li>byte, Byte
     * <li>short, Short
     * <li>int, Integer
     * <li>long, Long
     * <li>float, Float
     * <li>double, Double
     * </ul>
     * <li>{@link ConverterFieldHandler}
     * <ul>
     * <li>char, Character
     * <li>Date
     * </ul>
     * <li>{@linkplain StringFieldHandler}
     * <ul>
     * <li>String
     * </ul>
     * <li>{@linkplain CollectionFieldHandler}
     * <ul>
     * <li>Collection
     * <li>List
     * <li>ArrayList
     * <li>LinkedList
     * <li>Set
     * <li>HashSet
     * <li>SortedSet
     * <li>TreeSet
     * </ul>
     * <ul>
     */
    private void registerInitialFieldHandlers()
    {
        FieldHandler handler = null;

        // Boolean
        handler = newBooleanFieldHandler();
        registry.put(JPrimitiveType.BOOLEAN.getQualifiedSourceName(), handler);
        registry.put(JPrimitiveType.BOOLEAN.getQualifiedBoxedSourceName(), handler);

        // Numbers
        handler = newNumberFieldHandler();
        registry.put(JPrimitiveType.BYTE.getQualifiedSourceName(), handler);
        registry.put(JPrimitiveType.BYTE.getQualifiedBoxedSourceName(), handler);
        registry.put(JPrimitiveType.SHORT.getQualifiedSourceName(), handler);
        registry.put(JPrimitiveType.SHORT.getQualifiedBoxedSourceName(), handler);
        registry.put(JPrimitiveType.INT.getQualifiedSourceName(), handler);
        registry.put(JPrimitiveType.INT.getQualifiedBoxedSourceName(), handler);
        registry.put(JPrimitiveType.LONG.getQualifiedSourceName(), handler);
        registry.put(JPrimitiveType.LONG.getQualifiedBoxedSourceName(), handler);
        registry.put(JPrimitiveType.FLOAT.getQualifiedSourceName(), handler);
        registry.put(JPrimitiveType.FLOAT.getQualifiedBoxedSourceName(), handler);
        registry.put(JPrimitiveType.DOUBLE.getQualifiedSourceName(), handler);
        registry.put(JPrimitiveType.DOUBLE.getQualifiedBoxedSourceName(), handler);

        // Characters Dates are handle by the ConverterFieldHandler
        handler = newConverterFieldHandler();
        registry.put(JPrimitiveType.CHAR.getQualifiedSourceName(), handler);
        registry.put(JPrimitiveType.CHAR.getQualifiedBoxedSourceName(), handler);
        registry.put(Date.class.getName(), handler);

        // String
        handler = newStringFieldHandler();
        registry.put(String.class.getName(), handler);

        // Collections
        handler = newCollectionFieldHandler();
        registry.put(Collection.class.getName(), handler);
        registry.put(List.class.getName(), handler);
        registry.put(ArrayList.class.getName(), handler);
        registry.put(LinkedList.class.getName(), handler);
        registry.put(Set.class.getName(), handler);
        registry.put(HashSet.class.getName(), handler);
        registry.put(SortedSet.class.getName(), handler);
        registry.put(TreeSet.class.getName(), handler);
    }


    /**
     * Looks up a field handler based on the information provided in the field
     * context. The lookup logic is implemented like this:
     * <ol>
     * <li>If the fields type is a primitive return the
     * {@link ConverterFieldHandler}
     * <li>if the fields type is an enum return {@link EnumFieldHandler}
     * <li>If the fields type is an array return {@link ArrayFieldHandler}
     * <li>Try to lookup the field handler by the fields type classname (this
     * will resolve all types registered in
     * {@link #registerInitialFieldHandlers()}
     * <li>If no field handler return {@link JsonRegistryFieldHandler}.
     * </ol>
     * 
     * @param fieldContext
     * @return
     * @see name.pehl.piriti.rebind.fieldhandler.FieldHandlerRegistry#findFieldHandler(name.pehl.piriti.rebind.fieldhandler.FieldContext)
     */
    public FieldHandler findFieldHandler(FieldContext fieldContext)
    {
        FieldHandler handler = null;
        if (fieldContext.isEnum())
        {
            handler = newEnumFieldHandler();
        }
        else if (fieldContext.isArray())
        {
            handler = newArrayFieldHandler();
        }
        else
        {
            // If there's a format specified in the annotation and
            // the type is int, long, float or double use the converter
            // to convert the value, which is expected to be a string!
            JType fieldType = fieldContext.getFieldType();
            if (fieldContext.getFormat() != null
                    && (TypeUtils.isInteger(fieldType) || TypeUtils.isLong(fieldType) || TypeUtils.isFloat(fieldType) || TypeUtils
                            .isDouble(fieldType)))
            {
                handler = newConverterFieldHandler();
            }
            else
            {
                // Ask the registry for all other stuff (basic types,
                // collections, maps, ...)
                handler = registry.get(fieldContext.getFieldType().getQualifiedSourceName());
                if (handler == null)
                {
                    // Delegate to the XmlRegistry to resolve other mapped
                    // models
                    handler = newRegistryFieldHandler();
                }
            }
        }
        return handler;
    }


    protected FieldHandler newBooleanFieldHandler()
    {
        return new BooleanFieldHandler();
    }


    protected FieldHandler newNumberFieldHandler()
    {
        return new NumberFieldHandler();
    }


    protected FieldHandler newConverterFieldHandler()
    {
        return new ConverterFieldHandler();
    }


    protected FieldHandler newStringFieldHandler()
    {
        return new StringFieldHandler();
    }


    protected FieldHandler newEnumFieldHandler()
    {
        return new EnumFieldHandler();
    }


    protected FieldHandler newArrayFieldHandler()
    {
        return new ArrayFieldHandler();
    }


    protected FieldHandler newCollectionFieldHandler()
    {
        return new CollectionFieldHandler();
    }


    protected FieldHandler newRegistryFieldHandler()
    {
        return new JsonRegistryFieldHandler();
    }
}
