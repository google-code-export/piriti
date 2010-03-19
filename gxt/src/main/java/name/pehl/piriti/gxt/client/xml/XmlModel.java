package name.pehl.piriti.gxt.client.xml;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class annotations for {@linkplain com.extjs.gxt.ui.client.data.ModelData GXT
 * models}. For each property that should be mapped a {@link XmlField}
 * annotation must be specified.
 * 
 * @author $LastChangedBy:$
 * @version $LastChangedRevision:$
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface XmlModel
{
    XmlField[] value();
}
