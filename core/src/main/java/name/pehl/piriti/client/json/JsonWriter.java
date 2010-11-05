package name.pehl.piriti.client.json;

import java.util.List;

/**
 * Interface for serializing an instance of T or a list of Ts to JSON data. The
 * implementation for this interface is generated using deferred binding. All
 * properties of T which are annotated with {@link Json} are handled by the
 * generated JsonWriter implementation.
 * <p>
 * Null values in T are generated into the resulting JSON data.
 * <p>
 * The setup of the JsonWriter is inspired by the UiBinder and is typically
 * specified as an inner class:
 * 
 * <pre>
 * pubilc RunnableMessageContext
 * {
 *     interface Writer extends JsonWriter&lt;RunnableMessageContext&gt; {}
 *     public static final Writer JSON_WRITER = GWT.create(Writer.class);
 *     
 *     // The properties of this POJO annotated with {@code @}Json.
 * }
 * </pre>
 * 
 * JSON data can then be generated by calling
 * 
 * <pre>
 * RunnableMessageContext rmc = new RunnableMessageContext();
 * ...
 * String json = RunnableMessageContext.JSON_WRITER.toJson(rmc);
 * </pre>
 * 
 * @param <T>
 *            The type
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 46 $
 */
public interface JsonWriter<T>
{
    /**
     * Generates JSON data for the list of Ts according to the annotated
     * properties in T. The resulting JSON data contains the instances ot T
     * inside an array with the specified name:
     * 
     * <pre>
     * List&lt;RunnableMessageContext&gt; rmcs = ...;
     * String json = RunnableMessageContext.JSON_WRITER.toJson(rmcs, "data");
     * // json will be somethig like {"data":[...]}
     * </pre>
     * 
     * @param models
     *            the list of Ts to serialize to JSON. May be <code>null</code>.
     * @param arrayKey
     *            the array key
     * @return JSON data for the list of Ts or <code>null</code> if
     *         {@code models} and/or {@code arrayKey} is <code>null</code>.
     */
    String toJson(List<T> models, String arrayKey);


    /**
     * Generates JSON data for T according to the annotated properties in T.
     * 
     * @param model
     *            the instance to serialize to JSON. May be <code>null</code>.
     * @return JSON data for T or <code>null</code> if {@code model} is
     *         <code>null</code>.
     */
    String toJson(T model);
}
