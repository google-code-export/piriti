package name.pehl.piriti.rebind.xml.propertyhandler;

import java.util.logging.Level;

import name.pehl.piriti.commons.client.WhitespaceHandling;
import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.rebind.CodeGeneration;
import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.PropertyContext;
import name.pehl.piriti.rebind.propertyhandler.AbstractPropertyHandler;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandler;
import name.pehl.piriti.xml.client.XmlReader;

import org.apache.commons.lang.StringUtils;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * Abstract base class for {@linkplain PropertyHandler}s used for XML
 * (de)serialization.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 140 $
 */
public abstract class AbstractXmlPropertyHandler extends AbstractPropertyHandler
{
    // -------------------------------------------------------------- constants

    /**
     * XPath special characters.
     */
    private static final char[] XML_PATH_SYMBOLS = new char[] {'.', '[', ']', '/', '@',};


    // ----------------------------------------------------------- constructors

    public AbstractXmlPropertyHandler(TreeLogger logger)
    {
        super(logger);
    }


    // ---------------------------------------------------- overwritten methods

    /**
     * Returns <code>true</code> for {@linkplain XmlReader}s. For
     * {@linkplain XmlWriter}s this method returns <code>true</code> if no XPath
     * is used and <code>false</code> otherwise.
     * 
     * @param writer
     * @param propertyContext
     * @return <code>true</code> for {@linkplain JsonReader}s. For
     *         {@linkplain XmlWriter}s this method returns <code>true</code> if
     *         no XPath is used and <code>false</code> otherwise.
     * @throws UnableToCompleteException
     * @see name.pehl.piriti.rebind.propertyhandler.PropertyHandler#isValid(name.pehl.piriti.rebind.IndentedWriter,
     *      name.pehl.piriti.rebind.PropertyContext)
     */
    @Override
    public boolean isValid(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        if (propertyContext.getTypeContext().isWriter())
        {
            if (isXmlPath(propertyContext.getPath()))
            {
                skipProperty(writer, propertyContext, "The path \"" + propertyContext.getPath()
                        + "\" is a XPath expressions which is not supported by this XmlWriter");
                return false;
            }
        }
        return true;
    }


    @Override
    protected void readInputAsString(IndentedWriter writer, PropertyContext propertyContext)
    {
        if (propertyContext.getPath() != null)
        {
            CodeGeneration.log(writer, Level.FINE, "Using XPath \"%s\" to select xml value", propertyContext.getPath());
            writer.write("String %s = %s.selectValue(\"%s\", %s);", propertyContext.getVariableNames()
                    .getValueAsStringVariable(), propertyContext.getVariableNames().getInputVariable(), propertyContext
                    .getPath(), propertyContext.getWhitespaceHandling() == WhitespaceHandling.REMOVE);
        }
        else
        {
            if (propertyContext.getWhitespaceHandling() == WhitespaceHandling.REMOVE)
            {
                writer.write("String %s = %s.getText();",
                        propertyContext.getVariableNames().getValueAsStringVariable(), propertyContext
                                .getVariableNames().getInputVariable());
            }
            else
            {
                writer.write("String %s = %s.getTextStripped();", propertyContext.getVariableNames()
                        .getValueAsStringVariable(), propertyContext.getVariableNames().getInputVariable());
            }
        }
    }


    @Override
    public void markupStart(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        writer.write("%s.append(\"<\");");
        String elementName = propertyContext.getPath() != null ? propertyContext.getPath() : propertyContext.getName();
        writer.write("%s.append(\"%s\");", elementName);
        writer.write("%s.append(\">\");");
    }


    @Override
    protected void writeValueAsString(IndentedWriter writer, PropertyContext propertyContext)
    {
        writer.write("%s.append(%s);", propertyContext.getVariableNames().getBuilderVariable(), propertyContext
                .getVariableNames().getValueAsStringVariable());
    }


    @Override
    protected void writeValueDirectly(IndentedWriter writer, PropertyContext propertyContext)
    {
        CodeGeneration.log(writer, Level.WARNING, "writeValueDirectly() NYI");
    }


    @Override
    public void markupEnd(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        writer.write("%s.append(\"</\");");
        String elementName = propertyContext.getPath() != null ? propertyContext.getPath() : propertyContext.getName();
        writer.write("%s.append(\"%s\");", elementName);
        writer.write("%s.append(\">\");");
    }


    // --------------------------------------------------------- helper methods

    /**
     * Return <code>true</code> if the path contains {@link #XML_PATH_SYMBOLS},
     * <code>false</code> otherwise.
     * 
     * @param path
     * @return <code>true</code> if the path contains {@link #XML_PATH_SYMBOLS}
     *         , <code>false</code> otherwise.
     */
    protected boolean isXmlPath(String path)
    {
        return StringUtils.containsAny(path, XML_PATH_SYMBOLS);
    }
}
