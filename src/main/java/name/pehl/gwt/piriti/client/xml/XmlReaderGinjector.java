package name.pehl.gwt.piriti.client.xml;

import name.pehl.gwt.piriti.client.converter.ConverterRegistry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

/**
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
@GinModules(XmlReaderModule.class)
public interface XmlReaderGinjector extends Ginjector
{
    XmlReaderGinjector INJECTOR = GWT.create(XmlReaderGinjector.class);


    ConverterRegistry getConverterRegistry();


    XmlRegistry getXmlRegistry();
}
