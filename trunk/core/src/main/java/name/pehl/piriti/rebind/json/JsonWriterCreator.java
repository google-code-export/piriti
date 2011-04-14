package name.pehl.piriti.rebind.json;

import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.PropertyContext;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandler;

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
    // --------------------------------------------------------- initialization

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
        writer.write("public String toJson(List<%s> models, String arrayKey) {", typeContext.getType()
                .getParameterizedQualifiedSourceName());
        writer.indent();
        writer.write("String json = null;");
        writer.write("if (models != null && arrayKey != null) {");
        writer.indent();
        writer.write("StringBuilder %s = new StringBuilder();", typeContext.getVariableNames().getBuilderVariable());
        writer.write("%s.append(\"{\\\"\");", typeContext.getVariableNames().getBuilderVariable());
        writer.write("%s.append(arrayKey);", typeContext.getVariableNames().getBuilderVariable());
        writer.write("%s.append(\"\\\":[\");", typeContext.getVariableNames().getBuilderVariable());
        writer.write("for (Iterator<%s> iter = models.iterator(); iter.hasNext(); ) {", typeContext.getType()
                .getParameterizedQualifiedSourceName());
        writer.indent();
        writer.write("%s %s = iter.next();", typeContext.getType().getParameterizedQualifiedSourceName(), typeContext
                .getVariableNames().getInstanceVariable());
        writer.write("String jsonValue = toJson(%s);", typeContext.getVariableNames().getInstanceVariable());
        writer.write("if (jsonValue != null) {");
        writer.indent();
        writer.write("%s.append(jsonValue);", typeContext.getVariableNames().getBuilderVariable());
        writer.outdent();
        writer.write("}");
        writer.write("if (iter.hasNext()) {");
        writer.indent();
        writer.write("%s.append(\",\");", typeContext.getVariableNames().getBuilderVariable());
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.write("%s.append(\"]}\");", typeContext.getVariableNames().getBuilderVariable());
        writer.write("json = %s.toString();", typeContext.getVariableNames().getBuilderVariable());
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("return json;");
        writer.write("}");
    }


    protected void writeSingle(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("public String toJson(%s %s) {", typeContext.getType().getParameterizedQualifiedSourceName(),
                typeContext.getVariableNames().getInstanceVariable());
        writer.indent();
        writer.write("String json = null;");
        writer.write("if (%s != null) {", typeContext.getVariableNames().getInstanceVariable());
        writer.indent();
        writer.write("StringBuilder %s = new StringBuilder();", typeContext.getVariableNames().getBuilderVariable());
        writer.write("%s.append(\"{\");", typeContext.getVariableNames().getBuilderVariable());

        // This creates all FieldHandler / FieldContexts and calls
        // handleProperty()
        // in a loop
        handleProperties(writer);

        writer.write("%s.append(\"}\");", typeContext.getVariableNames().getBuilderVariable());
        writer.write("json = %s.toString();", typeContext.getVariableNames().getBuilderVariable());
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("return json;");
        writer.write("}");
    }


    // ---------------------------------------------------- overwritten methods

    @Override
    protected void handleProperty(IndentedWriter writer, PropertyHandler propertyHandler,
            PropertyContext propertyContext, boolean hasNext) throws UnableToCompleteException
    {
        propertyHandler.log(writer, propertyContext);
        propertyHandler.declare(writer, propertyContext);
        propertyHandler.readProperty(writer, propertyContext);
        propertyHandler.markupStart(writer, propertyContext);
        propertyHandler.writeValue(writer, propertyContext, propertyHandlerLookup);
        propertyHandler.markupEnd(writer, propertyContext);
        if (hasNext)
        {
            writer.write("%s.append(\",\");", propertyContext.getVariableNames().getBuilderVariable());
        }
    }
}
