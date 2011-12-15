package name.pehl.piriti.gxt.xml.client;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import name.pehl.piriti.converter.client.Converter;
import name.pehl.piriti.converter.client.NoopConverter;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Annotation used in {@link XmlMappings} to map one property of a
 * {@linkplain com.extjs.gxt.ui.client.data.ModelData GXT model}. When the
 * mapping was successful the data is set using
 * {@link com.extjs.gxt.ui.client.data.ModelData#set(String, Object)}.
 * <p>
 * For a description of the format and path semantics please refer to
 * {@link name.pehl.piriti.xml.client.Xml}.
 * 
 * @author $LastChangedBy:$
 * @version $LastChangedRevision:$
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Xml
{
    /**
     * The name of the property in the {@link ModelData GXT model}.
     * 
     * @return
     */
    String property();


    /**
     * An XPath expression to select the XML data. If not specified the property
     * is taken as a base for the XPath expression.
     * 
     * @return
     */
    String path() default "";


    /**
     * The type for the property.
     * 
     * @return
     */
    Class<?> type();


    /**
     * True if the type is an array.
     * 
     * @return
     */
    boolean array() default false;


    /**
     * The type variable in case of a collection.
     * 
     * @return
     */
    Class<?> typeVariable() default Void.class;


    /**
     * The format to use when converting the XML data to the type. Defaults to
     * "".
     * 
     * @return
     */
    String format() default "";


    /**
     * If <code>true</code> white spaces and new lines are stripped from the
     * selected XPath value. Defaults to <code>true</code>.
     * 
     * @return
     */
    boolean stripWsnl() default true;


    /**
     * A custom converter which is used for the parsing and serialization of the
     * json value. Defaults to {@link NoopConverter}, which means no custom
     * converter should be used.
     * 
     * @return
     */
    Class<? extends Converter<?>> converter() default NoopConverter.class;
}
