package name.pehl.piriti.rebind.xml.propertyhandler;

import java.util.logging.Level;

import name.pehl.piriti.json.client.JsonReader;
import name.pehl.piriti.rebind.CodeGeneration;
import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.PropertyContext;
import name.pehl.piriti.rebind.propertyhandler.AbstractPropertyHandler;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandler;
import name.pehl.piriti.xml.client.XmlReader;
import name.pehl.piriti.xml.client.XmlWriter;
import name.pehl.totoe.xml.client.Node;

import org.apache.commons.lang.StringUtils;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JType;

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
    protected static final char[] XML_PATH_SYMBOLS = new char[] {'.', '[', ']', '/', '@',};

    /**
     * Regular expression for a nested path
     */
    protected static final String NESTED_PATH_PATH_REGEX = "\\w+(/\\w+)?";


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
    public void declare(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        super.declare(writer, propertyContext);
        writer.write("String %s = null;", propertyContext.getVariableNames().getValueAsStringVariable());
    }


    @Override
    protected void readInputNatively(IndentedWriter writer, PropertyContext propertyContext)
    {
        String nodeValueVariable = propertyContext.getVariableNames().newVariableName("Node");
        writer.write("Node %s = %s.selectNode(\"%s\");", nodeValueVariable, propertyContext.getVariableNames()
                .getInputVariable(), propertyContext.getPathOrName());
        writer.write("if (%s != null) {", nodeValueVariable);
        writer.indent();
        JType type = propertyContext.getType();
        if (String.class.getName().equals(type.getQualifiedSourceName()))
        {
            writer.write("%s = %s.serialize();", propertyContext.getVariableNames().getValueVariable(),
                    nodeValueVariable);
        }
        else if (type.isClassOrInterface() != null)
        {
            JClassType nodeType = propertyContext.getTypeContext().getTypeOracle().findType(Node.class.getName());
            if (nodeType != null)
            {
                if (nodeType.equals(type.isClassOrInterface()))
                {
                    // Property is a Node
                    CodeGeneration.log(writer, Level.FINE, "Mapping native property %s as Node", propertyContext);
                    writer.write("%s = %s;", propertyContext.getVariableNames().getValueVariable(), nodeValueVariable);
                }
                else if (nodeType.isAssignableFrom(type.isClassOrInterface()))
                {
                    // Property is a subtype of Node
                    CodeGeneration.log(writer, Level.FINE, "Mapping native property %s as subtype of Node",
                            propertyContext);
                    writer.write("if (%s instanceof %s) {", nodeValueVariable, propertyContext.getType()
                            .getQualifiedSourceName());
                    writer.indent();
                    writer.write("%s = (%s)%s;", propertyContext.getVariableNames().getValueVariable(), propertyContext
                            .getType().getQualifiedSourceName(), nodeValueVariable);
                    writer.outdent();
                    writer.write("}");
                }
            }
        }
        writer.outdent();
        writer.write("}");
    }


    @Override
    protected void readInputAsString(IndentedWriter writer, PropertyContext propertyContext)
    {
        writer.write("%s = %s.selectValue(\"%s\", WhitespaceHandling.%s);", propertyContext.getVariableNames()
                .getValueAsStringVariable(), propertyContext.getVariableNames().getInputVariable(), propertyContext
                .getPathOrName(), propertyContext.getWhitespaceHandling());
    }


    @Override
    public void markupStart(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        writer.write("%s.append(\"<\");", propertyContext.getVariableNames().getBuilderVariable());
        writer.write("%s.append(\"%s\");", propertyContext.getVariableNames().getBuilderVariable(),
                propertyContext.getPathOrName());
        writer.write("%s.append(\">\");", propertyContext.getVariableNames().getBuilderVariable());
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
        writer.write("%s.append(%s);", propertyContext.getVariableNames().getBuilderVariable(), propertyContext
                .getVariableNames().getValueVariable());
    }


    @Override
    public void markupEnd(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        writer.write("%s.append(\"</\");", propertyContext.getVariableNames().getBuilderVariable());
        writer.write("%s.append(\"%s\");", propertyContext.getVariableNames().getBuilderVariable(),
                propertyContext.getPathOrName());
        writer.write("%s.append(\">\");", propertyContext.getVariableNames().getBuilderVariable());
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
