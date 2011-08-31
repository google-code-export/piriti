package name.pehl.piriti.xml.client;

import java.util.List;

import name.pehl.piriti.commons.client.HasWriteModelHandler;

/**
 * Interface for serializing an instance of T or a list of Ts to XML. All
 * properties of T and superclasses of T are handled by the generated XmlWriter
 * implementation. Null values in T result in empty elements in the resulting
 * XML.
 * <p>
 * The setup of the XmlWriter is inspired by the UiBinder and is typically
 * specified as an inner class:
 * 
 * <pre>
 * pubilc RunnableMessageContext
 * {
 *     interface Writer extends XmlWriter&lt;RunnableMessageContext&gt; {}
 *     public static final Writer XML_WRITER = GWT.create(Writer.class);
 *     
 *     // The properties of this POJO
 * }
 * </pre>
 * 
 * XML can then be generated by calling
 * 
 * <pre>
 * RunnableMessageContext rmc = new RunnableMessageContext();
 * ...
 * String xml = RunnableMessageContext.XML_WRITER.toXml(rmc);
 * </pre>
 * 
 * @param <T>
 *            The type
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 46 $
 */
public interface XmlWriter<T> extends HasWriteModelHandler<T>
{
    /**
     * Serializes the specified models to XML. For the root element and the
     * nested elements the following default values are used:
     * <ol>
     * <li>Root element: The classname of T with the first letter converted to
     * lowercase and a trailing 's'
     * <li>Nested elements: The classname of T with the first letter converted
     * to lowercase
     * </ol>
     * <p>
     * Given you want to serialize a list of books:
     * 
     * <pre>
     * List&lt;Book&gt; books = ...;
     * XmlWriter&lt;Book&gt; xmlWriter = ...;
     * String xml = xmlWriter.toXml(books);
     * </pre>
     * 
     * Then the xml will have the following structure:
     * 
     * <pre>
     * &lt;books&gt;
     *     &lt;book&gt;
     *         ...
     *     &lt;/book&gt;
     *     ...
     * &lt;/books&gt;
     * </pre>
     * 
     * @param models
     *            the models to serialize
     * @return the relevant XML or <code>null</code> if {@code models} is
     *         <code>null</code>
     */
    String toXml(List<T> models);


    /**
     * Serializes the specified models to XML using the specified name for the
     * root element. For the nested elements the classname of T with the first
     * letter converted to lowercase is used.
     * 
     * @param models
     *            the models to serialize
     * @param rootElement
     *            the name for the root element
     * @return the relevant XML or <code>null</code> if {@code models} and/or
     *         {@code rootElement} is <code>null</code>
     */
    String toXml(List<T> models, String rootElement);


    /**
     * Serializes the specified models to XML using the specified names for the
     * root and nested elements.
     * 
     * @param models
     *            the models to serialize
     * @param rootElement
     *            the name for the root element
     * @param nestedRootElement
     *            the name of the nested elements
     * @return the relevant XML or <code>null</code> if {@code models},
     *         {@code rootElement} and/or {@code nestedRootElement} is
     *         <code>null</code>
     */
    String toXml(List<T> models, String rootElement, String nestedRootElement);


    /**
     * Serializes the specified model to XML. For the root element the classname
     * of T with the first letter converted to lowercase is used.
     * <p>
     * Given you want to serialize a book:
     * 
     * <pre>
     * Book book = ...;
     * XmlWriter&lt;Book&gt; xmlWriter = ...;
     * String xml = xmlWriter.toXml(book);
     * </pre>
     * 
     * Then the xml will have the following structure:
     * 
     * <pre>
     * &lt;book&gt;
     *     ...
     * &lt;/book&gt;
     * </pre>
     * 
     * @param model
     *            the model to serialize
     * @return the relevant XML or <code>null</code> if {@code model} is
     *         <code>null</code>
     */
    String toXml(T model);


    /**
     * Serializes the specified model to XML using the specified name for the
     * root element.
     * 
     * @param model
     *            the model to serialize
     * @param rootElement
     *            the name for the root element
     * @return the relevant XML or <code>null</code> if {@code model} and/or
     *         {@code rootElement} is <code>null</code>
     */
    String toXml(T model, String rootElement);


    /**
     * Serializes the specified model to XML using <em>no</em> root element.
     * Please note that the resulting XML is <em>not valid</em>. Use this method
     * if you want to put the XML in your own root element or if you want
     * process the XML in some custom way.
     * 
     * @param model
     * @return the relevant XML <em>without</em> a root element or
     *         <code>null</code> if {@code model} and/or {@code rootElement} is
     *         <code>null</code>
     */
    String toPlainXml(T model);
}