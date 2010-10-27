package name.pehl.piriti.rebind.json.propertyhandler;

import name.pehl.piriti.rebind.CodeGeneration;
import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.json.JsonPathUtils;
import name.pehl.piriti.rebind.propertyhandler.AbstractArrayPropertyHandler;
import name.pehl.piriti.rebind.propertyhandler.PropertyContext;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandler;
import name.pehl.piriti.rebind.propertyhandler.VariableNames;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;

/**
 * {@link PropertyHandler} for arrays.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 139 $
 */
public class ArrayPropertyHandler extends AbstractArrayPropertyHandler
{
    /**
     * Returns <code>false</code> if this property context is used with a writer
     * and a JSONPath expression is used,
     * 
     * @param writer
     * @param propertyContext
     * @return
     * @throws UnableToCompleteException
     * @see name.pehl.piriti.rebind.propertyhandler.AbstractArrayPropertyHandler#isValid(name.pehl.piriti.rebind.IndentedWriter,
     *      name.pehl.piriti.rebind.propertyhandler.PropertyContext)
     */
    @Override
    public boolean isValid(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        boolean valid = super.isValid(writer, propertyContext);
        if (valid && propertyContext.isWriter() && JsonPathUtils.isJsonPath(propertyContext.getPath()))
        {
            CodeGeneration.skipProperty(writer, propertyContext,
                    "JSONPath expressions are not supported by this JsonWriter");
            return false;
        }
        return valid;
    }


    /**
     * TODO Javadoc
     * 
     * @param writer
     * @param propertyContext
     * @throws UnableToCompleteException
     * @see name.pehl.piriti.rebind.xml.propertyhandler.ConverterPropertyHandler#readInput(name.pehl.piriti.rebind.IndentedWriter,
     *      name.pehl.piriti.rebind.propertyhandler.PropertyContext)
     */
    @Override
    public void readInput(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        JArrayType arrayType = propertyContext.getArrayType();
        JType componentType = arrayType.getComponentType();
        JPrimitiveType primitiveComponentType = componentType.isPrimitive();
        if (primitiveComponentType != null)
        {
            try
            {
                componentType = propertyContext.getTypeOracle().getType(
                        primitiveComponentType.getQualifiedBoxedSourceName());
            }
            catch (NotFoundException e)
            {
                throw new UnableToCompleteException();
            }
        }
        String valueVariableAsList = propertyContext.getVariableNames().newVariableName("AsList");
        String nestedJsonValueVariable = propertyContext.getVariableNames().newVariableName("NestedJsonValue");
        String nestedValueVariable = propertyContext.getVariableNames().newVariableName("NestedValue");
        // The field context is created *without* a path. The nested field
        // handler must take care of this!
        VariableNames variableNames = new VariableNames(nestedJsonValueVariable, nestedValueVariable, propertyContext
                .getVariableNames().getBuilderVariable());
        PropertyContext nestedFieldContext = new PropertyContext(propertyContext.getTypeOracle(),
                propertyContext.getHandlerRegistry(), propertyContext.getReaderOrWriter(), propertyContext.getClazz(),
                componentType, propertyContext.getName(), null, propertyContext.getFormat(), false,
                propertyContext.getConverter(), null, propertyContext.getGetter(), propertyContext.getSetter(),
                variableNames);
        PropertyHandler nestedHandler = propertyContext.getHandlerRegistry().findPropertyHandler(nestedFieldContext);
        if (!nestedHandler.isValid(writer, nestedFieldContext))
        {
            return;
        }

        String jsonValue = CodeGeneration.getOrSelectJson(writer, propertyContext);
        writer.write("if (%s != null) {", jsonValue);
        writer.indent();
        writer.write("JSONArray jsonArray = %s.isArray();", jsonValue);
        writer.write("if (jsonArray != null) {");
        writer.indent();
        writer.write("int size = jsonArray.size();");
        writer.write("List<%1$s> %2$s = new ArrayList<%1$s>();", componentType.getParameterizedQualifiedSourceName(),
                valueVariableAsList);
        writer.write("for (int i = 0; i < size; i++) {");
        writer.indent();
        writer.write("JSONValue %s = jsonArray.get(i);", nestedJsonValueVariable);
        writer.write("if (%1$s != null && %1$s.isNull() == null) {", nestedJsonValueVariable);
        writer.indent();
        nestedHandler.comment(writer, nestedFieldContext);
        nestedHandler.declare(writer, nestedFieldContext);
        nestedHandler.readInput(writer, nestedFieldContext);
        writer.write("if (%s != null) {", nestedFieldContext.getVariableNames().getValueVariable());
        writer.indent();
        writer.write("%s.add(%s);", valueVariableAsList, nestedFieldContext.getVariableNames().getValueVariable());
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.write("if (!%s.isEmpty()) {", valueVariableAsList);
        writer.indent();
        writer.write("int index = 0;");
        if (primitiveComponentType != null)
        {
            writer.write("%s = new %s[%s.size()];", propertyContext.getVariableNames().getValueVariable(),
                    primitiveComponentType.getQualifiedSourceName(), valueVariableAsList);
        }
        else
        {
            writer.write("%s = new %s[%s.size()];", propertyContext.getVariableNames().getValueVariable(),
                    componentType.getQualifiedSourceName(), valueVariableAsList);
        }
        writer.write("for(%s currentValue : %s) {", componentType.getQualifiedSourceName(), valueVariableAsList);
        writer.indent();
        writer.write("%s[index] = currentValue;", propertyContext.getVariableNames().getValueVariable());
        writer.write("index++;");
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
    public void markupStart(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        CodeGeneration.appendJsonKey(writer, propertyContext);
    }


    @Override
    public void writeValue(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
        JArrayType arrayType = propertyContext.getArrayType();
        JType componentType = arrayType.getComponentType();
        JPrimitiveType primitiveComponentType = componentType.isPrimitive();
        if (primitiveComponentType != null)
        {
            try
            {
                componentType = propertyContext.getTypeOracle().getType(
                        primitiveComponentType.getQualifiedBoxedSourceName());
            }
            catch (NotFoundException e)
            {
                throw new UnableToCompleteException();
            }
        }

        String nestedJsonValueVariable = propertyContext.getVariableNames().newVariableName("NestedJsonValue");
        String nestedValueVariable = propertyContext.getVariableNames().newVariableName("NestedValue");
        // The field context is created *without* a path. The nested field
        // handler must take care of this!
        VariableNames variableNames = new VariableNames(nestedJsonValueVariable, nestedValueVariable, propertyContext
                .getVariableNames().getBuilderVariable());
        PropertyContext nestedFieldContext = new PropertyContext(propertyContext.getTypeOracle(),
                propertyContext.getHandlerRegistry(), propertyContext.getReaderOrWriter(), propertyContext.getClazz(),
                componentType, propertyContext.getName(), null, propertyContext.getFormat(), false,
                propertyContext.getConverter(), null, propertyContext.getGetter(), propertyContext.getSetter(),
                variableNames);
        PropertyHandler nestedHandler = propertyContext.getHandlerRegistry().findPropertyHandler(nestedFieldContext);
        if (!nestedHandler.isValid(writer, nestedFieldContext))
        {
            return;
        }

        writer.write("if (%s == null) {", propertyContext.getVariableNames().getValueVariable());
        writer.indent();
        writer.write("%s.append(\"null\");", propertyContext.getVariableNames().getBuilderVariable());
        writer.outdent();
        writer.write("}");
        writer.write("else {");
        writer.indent();
        // Iterate over values
        writer.write("%s.append(\"[\");", propertyContext.getVariableNames().getBuilderVariable());
        writer.write("for (int i = 0; i < %s.length; i++ ) {", propertyContext.getVariableNames().getValueVariable());
        writer.indent();

        nestedHandler.comment(writer, nestedFieldContext);
        nestedHandler.declare(writer, nestedFieldContext);
        // Replace nestedHandler.readField(writer, nestedFieldContext) with
        writer.write("%s = %s[i];", nestedFieldContext.getVariableNames().getValueVariable(), propertyContext
                .getVariableNames().getValueVariable());
        // No nestedHandler.markupStart(writer, nestedFieldContext); since we're
        // in an JSON array
        nestedHandler.writeValue(writer, nestedFieldContext);
        // No nestedHandler.markupEnd(writer, nestedFieldContext); since we're
        // in an JSON array

        writer.write("if (i < %s.length - 1) {", propertyContext.getVariableNames().getValueVariable());
        writer.indent();
        writer.write("%s.append(\",\");", propertyContext.getVariableNames().getBuilderVariable());
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.write("%s.append(\"]\");", propertyContext.getVariableNames().getBuilderVariable());
        writer.outdent();
        writer.write("}");
    }


    /**
     * Empty!
     * 
     * @param writer
     * @param propertyContext
     * @throws UnableToCompleteException
     * @see name.pehl.piriti.rebind.propertyhandler.PropertyHandler#markupEnd(name.pehl.piriti.rebind.IndentedWriter,
     *      name.pehl.piriti.rebind.propertyhandler.PropertyContext)
     */
    @Override
    public void markupEnd(IndentedWriter writer, PropertyContext propertyContext) throws UnableToCompleteException
    {
    }
}
