package name.pehl.piriti.xml.client;

import java.util.List;

import name.pehl.piriti.commons.client.AbstractWriter;

/**
 * Base class for generated XmlWriters. Contains common code and methods.
 * 
 * @param <T>
 *            The type
 * @author $LastChangedBy:$
 * @version $LastChangedRevision:$
 */
public abstract class AbstractXmlWriter<T> extends AbstractWriter<T> implements XmlWriter<T>
{
    // -------------------------------------------------------------- constants

    static final String PROLOG = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    // ----------------------------------------------------------------- fields

    protected final XmlRegistry xmlRegistry;


    // ----------------------------------------------------------- constructors

    protected AbstractXmlWriter()
    {
        super();
        this.xmlRegistry = XmlGinjector.INJECTOR.getXmlRegistry();
    }


    // ----------------------------------------------------- write list methods

    @Override
    public String toXml(List<T> models)
    {
        return toXml(models, modelsName(), modelName());
    }


    @Override
    public String toXml(List<T> models, String rootElement)
    {
        return toXml(models, rootElement, modelName());
    }


    @Override
    public String toXml(List<T> models, String rootElement, String nestedRootElement)
    {
        String xml = null;
        if (models != null && rootElement != null && nestedRootElement != null)
        {
            StringBuilder out = new StringBuilder(PROLOG).append("<").append(rootElement).append(">");
            for (T model : models)
            {
                String modelXml = toXml(model, nestedRootElement);
                if (modelXml != null)
                {
                    out.append(modelXml);
                }
            }
            out.append("</").append(rootElement).append(">");
            xml = out.toString();
        }
        return xml;
    }


    // --------------------------------------------------- write single methods

    @Override
    public String toXml(T model)
    {
        return toXml(model, modelName());
    }


    @Override
    public String toXml(T model, String rootElement)
    {
        String xml = null;
        if (model != null && rootElement != null)
        {
            XmlBuilder xmlBuilder = new XmlBuilder(rootElement);
            appendModel(xmlBuilder, model);
            xml = xmlBuilder.toString();
        }
        return PROLOG + xml;
    }


    // ------------------------------------------------------- abstract methods

    protected abstract String appendModel(XmlBuilder xmlBuilder, T model);


    // --------------------------------------------------------- helper methods

    protected abstract String modelName();


    protected String modelsName()
    {
        return modelName() + "s";
    }
}
