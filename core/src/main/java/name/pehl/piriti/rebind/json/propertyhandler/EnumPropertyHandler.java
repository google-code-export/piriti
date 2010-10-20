package name.pehl.piriti.rebind.json.propertyhandler;

import name.pehl.piriti.rebind.CodeGeneration;
import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.propertyhandler.AbstractEnumPropertyHandler;
import name.pehl.piriti.rebind.propertyhandler.PropertyContext;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandler;

import com.google.gwt.core.ext.UnableToCompleteException;

/**
 * {@link PropertyHandler} for enum types. This implementation reads the JSON data
 * as string and tries to convert it using <code>enumType.valueOf(String)</code>
 * .
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 139 $
 */
public class EnumPropertyHandler extends AbstractEnumPropertyHandler
{
    /**
     * TODO Javadoc
     * 
     * @param writer
     * @param fieldContext
     * @throws UnableToCompleteException
     * @see name.pehl.piriti.rebind.xml.propertyhandler.ConverterFieldHandler#readInput(name.pehl.piriti.rebind.IndentedWriter,
     *      name.pehl.piriti.rebind.propertyhandler.PropertyContext)
     */
    @Override
    public void readInput(IndentedWriter writer, PropertyContext fieldContext) throws UnableToCompleteException
    {
        // If there's a path then get the JSON value using this path,
        // otherwise it is expected that the JSON value is the inputVariable
        // itself (e.g. an array of strings has no path information for the
        // array elements)
        String jsonValue = fieldContext.newVariableName("AsJsonValue");
        if (fieldContext.getPath() != null)
        {
            writer.write("JSONValue %s = %s.get(\"%s\");", jsonValue, fieldContext.getInputVariable(),
                    fieldContext.getPath());
        }
        else
        {
            writer.write("JSONValue %s = %s;", jsonValue, fieldContext.getInputVariable());
        }
        writer.write("if (%s != null) {", jsonValue);
        writer.indent();
        writer.write("if (%s.isNull() == null) {", jsonValue);
        writer.indent();
        String jsonString = fieldContext.newVariableName("AsJsonString");
        writer.write("JSONString %s = %s.isString();", jsonString, jsonValue);
        writer.write("if (%s != null) {", jsonString);
        writer.indent();
        writer.write("try {");
        writer.indent();
        writer.write("%s = %s.valueOf(%s.stringValue());", fieldContext.getValueVariable(), fieldContext.getEnumType()
                .getQualifiedSourceName(), jsonString);
        writer.outdent();
        writer.write("}");
        writer.write("catch (IllegalArgumentException e1) {");
        writer.indent();
        writer.write("try {");
        writer.indent();
        writer.write("%s = %s.valueOf(%s.stringValue().toUpperCase());", fieldContext.getValueVariable(), fieldContext
                .getEnumType().getQualifiedSourceName(), jsonString);
        writer.outdent();
        writer.write("}");
        writer.write("catch (IllegalArgumentException e2) {}");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
    }


    @Override
    public void markupStart(IndentedWriter writer, PropertyContext fieldContext) throws UnableToCompleteException
    {
        CodeGeneration.appendJsonKey(writer, fieldContext);
    }


    @Override
    public void writeValue(IndentedWriter writer, PropertyContext fieldContext) throws UnableToCompleteException
    {
        writer.write("if (%s == null) {", fieldContext.getValueVariable());
        writer.indent();
        writer.write("%s.append(\"null\");", fieldContext.getBuilderVariable());
        writer.outdent();
        writer.write("}");
        writer.write("else {");
        writer.indent();
        CodeGeneration.appendJsonValue(writer, fieldContext, true);
        writer.outdent();
        writer.write("}");
    }


    /**
     * Empty!
     * 
     * @param writer
     * @param fieldContext
     * @throws UnableToCompleteException
     * @see name.pehl.piriti.rebind.propertyhandler.PropertyHandler#markupEnd(name.pehl.piriti.rebind.IndentedWriter,
     *      name.pehl.piriti.rebind.propertyhandler.PropertyContext)
     */
    @Override
    public void markupEnd(IndentedWriter writer, PropertyContext fieldContext) throws UnableToCompleteException
    {
    }
}
