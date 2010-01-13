package name.pehl.gwt.piriti.rebind;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Registry which maps classnames to {@linkplain FieldHandler}s. This registry
 * is used by the {@link XmlReaderCreator} when generating code.
 * 
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class FieldHandlerRegistry
{
    private Map<String, FieldHandler> registry;


    /**
     * Construct a new instance of this class and registeres the initial field
     * handlers.
     */
    public FieldHandlerRegistry()
    {
        registry = new HashMap<String, FieldHandler>();
        registerInitialFieldHandlers();
    }


    /**
     * Registers the initial field handler used in piriti. The following
     * handlers are registered:
     * <ul>
     * <li>{@linkplain DefaultFieldHandler}
     * <ul>
     * <li>Boolean.class.getName()
     * <li>Byte.class.getName()
     * <li>Character.class.getName()
     * <li>Date.class.getName()
     * <li>Double.class.getName()
     * <li>Float.class.getName()
     * <li>Integer.class.getName()
     * <li>Long.class.getName()
     * <li>Short.class.getName()
     * </ul>
     * <li>{@linkplain StringFieldHandler}
     * <ul>
     * <li>String.class.getName()
     * </ul>
     * <li>{@linkplain CollectionFieldHandler}
     * <ul>
     * <li>Collection.class.getName()
     * <li>List.class.getName()
     * <li>ArrayList.class.getName()
     * <li>LinkedList.class.getName()
     * <li>Set.class.getName()
     * <li>HashSet.class.getName()
     * <li>SortedSet.class.getName()
     * <li>TreeSet.class.getName()
     * </ul>
     * <li>{@linkplain MapFieldHandler}
     * <ul>
     * <li>Map.class.getName()
     * <li>HashMap.class.getName()
     * <li>SortedMap.class.getName()
     * <li>TreeMap.class.getName()
     * </ul>
     * <ul>
     */
    protected void registerInitialFieldHandlers()
    {
        FieldHandler handler = null;

        // Basics
        handler = new DefaultFieldHandler();
        registry.put(Boolean.class.getName(), handler);
        registry.put(Byte.class.getName(), handler);
        registry.put(Character.class.getName(), handler);
        registry.put(Date.class.getName(), handler);
        registry.put(Double.class.getName(), handler);
        registry.put(Float.class.getName(), handler);
        registry.put(Integer.class.getName(), handler);
        registry.put(Long.class.getName(), handler);
        registry.put(Short.class.getName(), handler);

        // String
        handler = new StringFieldHandler();
        registry.put(String.class.getName(), handler);

        // Collections
        handler = new CollectionFieldHandler();
        registry.put(Collection.class.getName(), handler);
        registry.put(List.class.getName(), handler);
        registry.put(ArrayList.class.getName(), handler);
        registry.put(LinkedList.class.getName(), handler);
        registry.put(Set.class.getName(), handler);
        registry.put(HashSet.class.getName(), handler);
        registry.put(SortedSet.class.getName(), handler);
        registry.put(TreeSet.class.getName(), handler);

        // Maps
        handler = new MapFieldHandler();
        registry.put(Map.class.getName(), handler);
        registry.put(HashMap.class.getName(), handler);
        registry.put(SortedMap.class.getName(), handler);
        registry.put(TreeMap.class.getName(), handler);
    }


    /**
     * Looks up a field handler based on the information provided in the field
     * context. The lookup logic is implemented like this:
     * <ol>
     * <li>If the fields type is a primitive return the
     * {@link DefaultFieldHandler}
     * <li>if the fields type is an enum return {@link EnumFieldHandler}
     * <li>If the fields type is an array return {@link ArrayFieldHandler}
     * <li>Try to lookup the field handler by the fields type classname (this
     * will resolve all types registered in
     * {@link #registerInitialFieldHandlers()}
     * <li>If no field handler return {@link XmlRegistryFieldHandler}.
     * </ol>
     * 
     * @param fieldContext
     * @return
     */
    public FieldHandler findFieldHandler(FieldContext fieldContext)
    {
        FieldHandler handler = null;
        if (fieldContext.isPrimitive())
        {
            handler = new DefaultFieldHandler();
        }
        else if (fieldContext.isEnum())
        {
            handler = new EnumFieldHandler();
        }
        else if (fieldContext.isArray())
        {
            handler = new ArrayFieldHandler();
        }
        else
        {
            // Ask the registry for all other stuff (basic types,
            // collections, maps, ...)
            handler = registry.get(fieldContext.getFieldType().getQualifiedSourceName());
            if (handler == null)
            {
                // Delegate to the XmlRegistry to resolve other mapped models
                handler = new XmlRegistryFieldHandler();
            }
        }
        return handler;
    }
}
