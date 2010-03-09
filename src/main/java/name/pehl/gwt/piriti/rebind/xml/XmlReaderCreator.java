package name.pehl.gwt.piriti.rebind.xml;

import name.pehl.gwt.piriti.client.xml.XmlField;
import name.pehl.gwt.piriti.rebind.AbstractReaderCreator;
import name.pehl.gwt.piriti.rebind.FieldContext;
import name.pehl.gwt.piriti.rebind.FieldHandler;
import name.pehl.gwt.piriti.rebind.IndentedWriter;
import name.pehl.gwt.piriti.rebind.TypeUtils;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * Class which generates the code necessary to map the annotated fields.
 * 
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class XmlReaderCreator extends AbstractReaderCreator
{
    public XmlReaderCreator(GeneratorContext context, JClassType interfaceType, String implName,
            String readerClassname, TreeLogger logger) throws UnableToCompleteException
    {
        super(context, interfaceType, implName, readerClassname, logger);
    }


    @Override
    protected void createImports(IndentedWriter writer) throws UnableToCompleteException
    {
        super.createImports(writer);
        writer.write("import com.google.gwt.xml.client.Document;");
        writer.write("import com.google.gwt.xml.client.Element;");
        writer.write("import name.pehl.gwt.piriti.client.xml.*;");
    }


    @Override
    protected void createMemberVariables(IndentedWriter writer) throws UnableToCompleteException
    {
        super.createMemberVariables(writer);
        writer.write("private XmlRegistry xmlRegistry;");
    }


    @Override
    protected void createConstructorBody(IndentedWriter writer)
    {
        super.createConstructorBody(writer);
        writer.write("this.xmlRegistry = XmlGinjector.INJECTOR.getXmlRegistry();");
        writer.write("this.xmlRegistry.register(%s.class, this);", modelType.getQualifiedSourceName());
    }


    @Override
    protected void createMethods(IndentedWriter writer) throws UnableToCompleteException
    {
        readSingleFromDocument(writer);
        writer.newline();

        readSingleFromElement(writer);
        writer.newline();

        readListFromDocument(writer);
        writer.newline();

        readListFromElement(writer);
        writer.newline();
    }


    private void readSingleFromDocument(IndentedWriter writer) throws UnableToCompleteException
    {
        readSingle(writer, "Document", "document");
    }


    private void readSingleFromElement(IndentedWriter writer) throws UnableToCompleteException
    {
        readSingle(writer, "Element", "element");
    }


    private void readSingle(IndentedWriter writer, String xmlType, String xmlVariable) throws UnableToCompleteException
    {
        writer.write("public %s readSingle(%s %s) {", modelType.getQualifiedSourceName(), xmlType, xmlVariable);
        writer.indent();
        writer.write("%s model = null;", modelType.getParameterizedQualifiedSourceName());
        writer.write("if (%s != null) {", xmlVariable);
        writer.indent();
        writer.write("model = new %s();", modelType.getParameterizedQualifiedSourceName());
        processFields(writer, modelType.getFields(), xmlVariable);
        writer.outdent();
        writer.write("}");
        writer.write("return model;");
        writer.outdent();
        writer.write("}");
    }


    private void readListFromDocument(IndentedWriter writer) throws UnableToCompleteException
    {
        readList(writer, "Document", "document");
    }


    private void readListFromElement(IndentedWriter writer) throws UnableToCompleteException
    {
        readList(writer, "Element", "element");
    }


    private void readList(IndentedWriter writer, String xmlType, String xmlVariable) throws UnableToCompleteException
    {
        writer.write("public List<%s> readList(%s %s, String xpath) {", modelType.getQualifiedSourceName(), xmlType,
                xmlVariable);
        writer.indent();
        writer.write("List<%1$s> models = new ArrayList<%1$s>();", modelType.getParameterizedQualifiedSourceName());
        writer.write("if (%s != null && xpath != null && xpath.length() != 0) {", xmlVariable);
        writer.indent();
        writer.write("List<Element> elements = XPathUtils.getElements(%s, xpath);", xmlVariable);
        writer.write("if (elements != null && !elements.isEmpty()) {");
        writer.indent();
        writer.write("for (Element currentElement : elements) {");
        writer.indent();
        writer.write("%s model = readSingle(currentElement);", modelType.getParameterizedQualifiedSourceName());
        writer.write("if (model != null) {");
        writer.indent();
        writer.write("models.add(model);");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.outdent();
        writer.write("}");
        writer.write("return models;");
        writer.outdent();
        writer.write("}");
    }


    // ---------------------------------------------------------- field methods

    private void processFields(IndentedWriter writer, JField[] fields, String xmlVariable)
            throws UnableToCompleteException
    {
        if (fields != null && fields.length != 0)
        {
            int counter = 0;
            for (JField field : fields)
            {
                XmlField xmlField = field.getAnnotation(XmlField.class);
                if (xmlField != null)
                {
                    writer.newline();
                    String xpath = calculateXpath(field, xmlField);
                    FieldContext fieldContext = new FieldContext(context.getTypeOracle(), handlerRegistry, modelType,
                            field.getType(), field.getName(), xpath, xmlField.format(), xmlVariable, "value" + counter);
                    FieldHandler handler = handlerRegistry.findFieldHandler(fieldContext);
                    if (handler.isValid(writer, fieldContext))
                    {
                        handler.writeComment(writer, fieldContext);
                        handler.writeDeclaration(writer, fieldContext);
                        handler.writeConverterCode(writer, fieldContext);
                        handler.writeAssignment(writer, fieldContext);
                        counter++;
                    }
                }
            }
        }
    }


    private String calculateXpath(JField field, XmlField xmlField)
    {
        String fieldName = field.getName();
        JType fieldType = field.getType();
        String xpath = xmlField.value();
        if (xpath == null || xpath.length() == 0)
        {
            xpath = fieldName;
            if (fieldType.isPrimitive() != null || TypeUtils.isBasicType(fieldType) || fieldType.isEnum() != null)
            {
                xpath += "/text()";
            }
        }
        return xpath;
    }
}
