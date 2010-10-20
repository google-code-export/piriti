package name.pehl.piriti.gxt.rebind.xml.propertyhandler;

import name.pehl.piriti.client.xml.XmlReader;
import name.pehl.piriti.gxt.client.xml.XmlModelReader;

/**
 * {@link PropertyHandler} implementation for types with an own {@link XmlReader}.
 * 
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class XmlRegistryPropertyHandler extends name.pehl.piriti.rebind.xml.propertyhandler.XmlRegistryPropertyHandler
{
    @Override
    protected String getReaderClassname()
    {
        return XmlModelReader.class.getName();
    }

    // TODO
    // @Override
    // protected String getWriterClassname()
    // {
    // return XmlModelWriter.class.getName();
    // }
}
