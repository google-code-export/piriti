package name.pehl.piriti.rebind.json;

import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.fieldhandler.FieldContext;
import name.pehl.piriti.rebind.fieldhandler.FieldHandler;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * Class which generates the code necessary to serialize a POJO to JSON.
 * 
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class JsonWriterCreator extends AbstractJsonCreator
{
    // ----------------------------------------------------------- constructors

    public JsonWriterCreator(GeneratorContext context, JClassType interfaceType, String implName,
            String readerClassname, TreeLogger logger) throws UnableToCompleteException
    {
        super(context, interfaceType, implName, readerClassname, logger);
    }


    // --------------------------------------------------------- create methods

    @Override
    protected void createMethods(IndentedWriter writer) throws UnableToCompleteException
    {
        writeList(writer);
        writer.newline();

        writeSingle(writer);
        writer.newline();
    }


    // ---------------------------------------------------------- write methods

    protected void writeList(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("public String toJson(List<%s> values, String arrayKey) {",
                modelType.getParameterizedQualifiedSourceName());
        writer.indent();
        writer.write("String json = null;");
        writer.write("if (values != null && arrayKey != null) {");
        writer.indent();
        writer.write("StringBuilder jsonBuilder = new StringBuilder();");
        writer.write("jsonBuilder.append(\"{\");");
        writer.write("jsonBuilder.append(arrayKey);");
        writer.write("jsonBuilder.append(\":[\"");
        writer.write("for (Iterator<%s> iter = values.iterator(); iter.hasNext(); ) {",
                modelType.getParameterizedQualifiedSourceName());
        writer.indent();
        writer.write("%s value = iter.next();", modelType.getParameterizedQualifiedSourceName());
        writer.write("String jsonValue = toJson(value);");
        writer.write("if (jsonValue != null) {");
        writer.indent();
        writer.write("jsonBuilder.append(jsonValue);");
        writer.outdent();
        writer.write("}");
        writer.write("if (iter.hasNext()) {");
        writer.indent();
        writer.write("jsonBuilder.append(\",\");");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.write("jsonBuilder.append(\"]}\");");
        writer.write("json = jsonBuilder.toString();");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("return json;");
        writer.write("}");
    }


    protected void writeSingle(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("public String toJson(%s value) {", modelType.getParameterizedQualifiedSourceName());
        writer.indent();
        writer.write("String json = null;");
        writer.write("if (value != null) {");
        writer.indent();
        writer.write("StringBuilder jsonBuilder = new StringBuilder();");

        handleFields(writer);

        writer.write("json = jsonBuilder.toString();");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("return json;");
        writer.write("}");
    }


    // ---------------------------------------------------- overwritten methods

    @Override
    protected void handleField(IndentedWriter writer, FieldHandler fieldHandler, FieldContext fieldContext)
            throws UnableToCompleteException
    {
    }
}
