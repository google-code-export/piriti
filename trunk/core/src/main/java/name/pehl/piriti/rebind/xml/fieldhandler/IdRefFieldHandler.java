package name.pehl.piriti.rebind.xml.fieldhandler;

import name.pehl.piriti.client.xml.XmlReader;
import name.pehl.piriti.client.xml.XmlWriter;
import name.pehl.piriti.rebind.CodeGeneration;
import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.TypeUtils;
import name.pehl.piriti.rebind.fieldhandler.AbstractCollectionFieldHandler;
import name.pehl.piriti.rebind.fieldhandler.AbstractRegistryFieldHandler;
import name.pehl.piriti.rebind.fieldhandler.FieldContext;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * @author $Author$
 * @version $Date$ $Revision: 421
 *          $
 */
public class IdRefFieldHandler extends AbstractRegistryFieldHandler
{
    private static final String NESTED_TYPE = "nestedType";


    /**
     * Returns <code>true</code> if the field type is a class or interface and
     * if there's a public static field of type {@link #getReaderClassname()} in
     * the field type or if the field type is an array or collection of such a
     * type, <code>false</code> otherwise.
     * 
     * @param writer
     * @param fieldContext
     * @return
     * @see name.pehl.piriti.rebind.fieldhandler.AbstractFieldHandler#isValid(name.pehl.piriti.rebind.fieldhandler.FieldContext)
     */
    @Override
    public boolean isValid(IndentedWriter writer, FieldContext fieldContext) throws UnableToCompleteException
    {
        JType type = null;
        if (fieldContext.isArray())
        {
            type = fieldContext.getArrayType().getComponentType();
        }
        else if (TypeUtils.isCollection(fieldContext.getFieldType()))
        {
            type = TypeUtils.getTypeVariable(fieldContext.getFieldType());
        }
        else
        {
            type = fieldContext.getFieldType();
        }

        JClassType classOrInterface = type.isClassOrInterface();
        if (classOrInterface == null)
        {
            CodeGeneration.skipField(writer, fieldContext, "Type is no class or interface");
            return false;
        }
        fieldContext.addMetadata(NESTED_TYPE, classOrInterface);
        return true;
    }


    @Override
    public void readInput(IndentedWriter writer, FieldContext fieldContext) throws UnableToCompleteException
    {
        JClassType nestedType = fieldContext.getMetadata(NESTED_TYPE);
        String readerVariable = startReader(writer, fieldContext, "xmlRegistry", nestedType);

        String references = fieldContext.newVariableName("References");
        writer.write("String[] %s = %s.selectValues(\"%s\", %s);", references, fieldContext.getInputVariable(),
                fieldContext.getPath(), fieldContext.isStripWsnl());
        writer.write("if (%s.length == 1) {", references);
        writer.indent();
        // If there's only one value it is expected that this value contains
        // the references seperated with space
        writer.write("%1$s = %1$s[0].split(\" \");", references);
        writer.outdent();
        writer.write("}");
        writer.write("if (%s.length != 0) {", references);
        writer.indent();
        if (fieldContext.isArray() || TypeUtils.isCollection(fieldContext.getFieldType()))
        {
            String collectionVariable = null;
            if (fieldContext.isArray())
            {
                collectionVariable = fieldContext.newVariableName("AsList");
                writer.write("List<%1$s> %2$s = new ArrayList<%1$s>();",
                        nestedType.getParameterizedQualifiedSourceName(), collectionVariable);
            }
            else
            {
                collectionVariable = fieldContext.getValueVariable();
                String collectionImplementation = AbstractCollectionFieldHandler.interfaceToImplementation
                        .get(fieldContext.getFieldType().getErasedType().getQualifiedSourceName());
                if (collectionImplementation == null)
                {
                    // the field type is already an implementation
                    collectionImplementation = fieldContext.getFieldType().getParameterizedQualifiedSourceName();
                }
                writer.write("%s = new %s<%s>();", fieldContext.getValueVariable(), collectionImplementation,
                        nestedType.getQualifiedSourceName());
            }
            writer.write("for (String reference : %s) {", references);
            writer.indent();
            writer.write("%s referenceInstance = %s.idRef(reference);",
                    nestedType.getParameterizedQualifiedSourceName(), readerVariable);
            writer.write("if (referenceInstance != null) {");
            writer.indent();
            writer.write("%s.add(referenceInstance);", collectionVariable);
            writer.outdent();
            writer.write("}");
            writer.outdent();
            writer.write("}");
            if (fieldContext.isArray())
            {
                writer.write("%s = new %s[%s.size()];", fieldContext.getValueVariable(),
                        nestedType.getQualifiedSourceName(), collectionVariable);
                writer.write("int index = 0;");
                writer.write("for(%s currentValue : %s) {", nestedType.getQualifiedSourceName(), collectionVariable);
                writer.indent();
                writer.write("%s[index] = currentValue;", fieldContext.getValueVariable());
                writer.write("index++;");
                writer.outdent();
                writer.write("}");
            }
        }
        else
        {
            writer.write("%s = %s.idRef(%s[0]);", fieldContext.getValueVariable(), readerVariable, references);
        }
        writer.outdent();
        writer.write("}");

        endReader(writer);
    }


    @Override
    public void markupStart(IndentedWriter writer, FieldContext fieldContext) throws UnableToCompleteException
    {
        writer.write("// markupStart() NYI");
    }


    @Override
    public void writeValue(IndentedWriter writer, FieldContext fieldContext) throws UnableToCompleteException
    {
        writer.write("// writeValue() NYI");
    }


    @Override
    public void markupEnd(IndentedWriter writer, FieldContext fieldContext) throws UnableToCompleteException
    {
        writer.write("// markupEnd() NYI");
    }


    @Override
    protected String getReaderClassname()
    {
        return XmlReader.class.getName();
    }


    @Override
    protected String getWriterClassname()
    {
        return XmlWriter.class.getName();
    }
}
