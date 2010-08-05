package name.pehl.piriti.client.json;

import java.util.List;

import com.google.inject.internal.Nullable;

/**
 * Interface for serializing an instance of T or a list of Ts to JSON data. The
 * implementation for this interface is generated using deferred binding. All
 * fields of T which are annotated with {@link JsonField} are handled by the
 * generated JsonWriter implementation.
 * <p>
 * Please note:
 * <ul>
 * <li>Fields in T must not be private
 * <li>Null values in T are generated into the resulting JSON data
 * </ul>
 * <p>
 * The setup of the XmlWriter is inspired by the UiBinder and is typically
 * specified as an inner class:
 * 
 * <pre>
 * pubilc RunnableMessageContext
 * {
 *     interface Writer extends JsonWriter&lt;RunnableMessageContext&gt; {}
 *     public static final Writer JSON_WRITER = GWT.create(Writer.class);
 *     
 *     // The fields of this POJO annotated with JsonField.
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
     * Generates JSON data for the list of Ts according to the annotated fields
     * in T. The resulting JSON data contains the instances ot T inside an array
     * with the specified name:
     * 
     * <pre>
     * List&lt;RunnableMessageContext&gt; rmcs = ...;
     * String json = RunnableMessageContext.JSON_WRITER.toJson(rmcs, "data");
     * // json will be somethig like {"data":[...]}
     * </pre>
     * 
     * @param models
     *            the list of Ts to serialize to JSON.
     * @param arrayKey
     *            the array key
     * @return JSON data for the list of Ts or <code>null</code> if
     *         {@code models} and/or {@code arrayKey} is <code>null</code>.
     */
    String toJson(@Nullable List<T> models, String arrayKey);


    /**
     * Generates JSON data for T according to the annotated fields in T.
     * 
     * @param model
     *            the instance to serialize to JSON.
     * @return JSON data for T or <code>null</code> if {@code model} is
     *         <code>null</code>.
     */
    String toJson(@Nullable T model);
}
