package name.pehl.gwt.piriti.rebind;

import java.io.PrintWriter;

import name.pehl.gwt.piriti.client.xml.XmlField;
import name.pehl.gwt.piriti.client.xml.XmlReader;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;

/**
 * @author $Author:$
 * @version $Revision:$
 */
public class XmlReaderCreator
{
    // -------------------------------------------------------- private members

    private final GeneratorContext context;
    private final JClassType interfaceType;
    private final String implName;
    private final TreeLogger logger;
    private final JClassType modelType;


    // ----------------------------------------------------------- constructors

    public XmlReaderCreator(GeneratorContext context, JClassType interfaceType, String implName, TreeLogger logger)
            throws UnableToCompleteException
    {
        this.context = context;
        this.interfaceType = interfaceType;
        this.implName = implName;
        this.logger = logger;

        // Check for possible misuse 'GWT.create(XmlReader.class)'
        JClassType xmlReaderItself = context.getTypeOracle().findType(XmlReader.class.getCanonicalName());
        if (xmlReaderItself.equals(interfaceType))
        {
            die("You must use a subtype of XmlReader in GWT.create(). E.g.,\n"
                    + "  interface ModelXmlReader extends XmlReader<Model> {}\n  GWT.create(ModelXmlReader.class);");
        }

        JClassType[] xmlReaderTypes = interfaceType.getImplementedInterfaces();
        if (xmlReaderTypes.length == 0)
        {
            die("No implemented interfaces for %s", interfaceType.getName());
        }
        JClassType xmlReaderType = xmlReaderTypes[0];

        // Check type parameter
        JClassType[] typeArgs = xmlReaderType.isParameterized().getTypeArgs();
        if (typeArgs.length != 1)
        {
            die("One model type parameters is required for %s", xmlReaderType.getName());
        }
        this.modelType = typeArgs[0];
    }


    // --------------------------------------------------------- create methods

    public void create() throws UnableToCompleteException
    {
        PrintWriter printWriter = context.tryCreate(logger, interfaceType.getPackage().getName(), implName);
        if (printWriter != null)
        {
            IndentedWriter writer = new IndentedWriter(printWriter);
            createClass(writer);
            context.commit(logger, printWriter);
        }
    }


    private void createClass(IndentedWriter writer) throws UnableToCompleteException
    {
        String packageName = interfaceType.getPackage().getName();
        if (packageName.length() > 0)
        {
            writer.write("package %s;", packageName);
            writer.newline();
        }

        // Imports
        writer.write("import java.util.ArrayList;");
        writer.write("import java.util.List;");
        writer.write("import com.google.gwt.xml.client.Document;");
        writer.write("import com.google.gwt.xml.client.Element;");
        writer.write("import name.pehl.gwt.piriti.client.converter.Converter;");
        writer.write("import name.pehl.gwt.piriti.client.converter.ConverterFactory;");
        writer.newline();

        // Class
        writer.write("public class %s implements %s {", implName, interfaceType.getQualifiedSourceName());
        writer.newline();
        writer.indent();

        // Private members and constructor
        writer.write("private ConverterFactory converterFactory;");
        writer.write("private XmlRegistry xmlRegistry;");
        writer.newline();
        writer.write("public %s() {", implName);
        writer.indent();
        writer.write("this.converterFactory = XmlReaderGinjector.INJECTOR.getConverterFactory();");
        writer.write("this.xmlRegistry = XmlReaderGinjector.INJECTOR.getXmlRegistry();");
        writer.write("this.xmlRegistry.register(%s.class, this);", modelType.getQualifiedSourceName());
        writer.outdent();
        writer.write("}");
        writer.newline();

        // Interface methods
        readSingleFromDocument(writer);
        readSingleFromElement(writer);
        readListFromDocument(writer);
        readListFromElement(writer);

        // That's all
        writer.outdent();
        writer.write("}");
    }


    // ---------------------------------------------- interface method creation

    private void readSingleFromDocument(IndentedWriter writer) throws UnableToCompleteException
    {
        readSingle(writer, "Document", "document");
    }


    private void readSingleFromElement(IndentedWriter writer) throws UnableToCompleteException
    {
        readSingle(writer, "Element", "element");
    }


    private void readSingle(IndentedWriter writer, String sourceType, String sourceVariable)
            throws UnableToCompleteException
    {
        writer.write("public %s readSingle(%s %s) {", modelType.getQualifiedSourceName(), sourceType, sourceVariable);
        writer.indent();
        writer.write("%s model = null;", modelType.getParameterizedQualifiedSourceName());
        writer.write("if (%s != null) {", sourceVariable);
        writer.indent();
        writer.write("model = new %s();", modelType.getParameterizedQualifiedSourceName());
        processFields(writer, modelType.getFields(), sourceType, sourceVariable);
        writer.outdent();
        writer.write("}");
        writer.write("return model;");
        writer.outdent();
        writer.write("}");
        writer.newline();
    }


    private void readListFromDocument(IndentedWriter writer) throws UnableToCompleteException
    {
        readList(writer, "Document", "document");
    }


    private void readListFromElement(IndentedWriter writer) throws UnableToCompleteException
    {
        readList(writer, "Element", "element");
    }


    private void readList(IndentedWriter writer, String sourceType, String sourceVariable)
            throws UnableToCompleteException
    {
        writer.write("public List<%s> readList(%s %s, String xpath) {", modelType.getQualifiedSourceName(), sourceType,
                sourceVariable);
        writer.indent();
        writer.write("List<%1$s> models = new ArrayList<%1$s>();", modelType.getParameterizedQualifiedSourceName());
        writer.write("if (%s != null && xpath != null && xpath.length() != 0) {", sourceVariable);
        writer.indent();
        writer.write("List<Element> elements = XPathUtils.getElements(%s, xpath);", sourceVariable);
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
        writer.newline();
    }


    // ---------------------------------------------------------- field methods

    private void processFields(IndentedWriter writer, JField[] fields, String sourceType, String sourceVariable)
            throws UnableToCompleteException
    {
        if (fields != null && fields.length != 0)
        {
            int counter = 0;
            FieldHandlerLookup handlerLookup = new FieldHandlerLookup();
            for (JField field : fields)
            {
                XmlField xmlField = field.getAnnotation(XmlField.class);
                if (xmlField != null)
                {
                    writer.newline();
                    if (modelType.equals(field.getType()))
                    {
                        // prevent recursion
                        writer
                                .write(
                                        "// Skipping field %s to prevent endless recursion (it has the same type as the model).",
                                        field.getName());
                        continue;
                    }

                    FieldContext fieldContext = new FieldContext(context, sourceType, sourceVariable, xmlField, field,
                            counter);
                    processField(writer, handlerLookup, fieldContext);
                    counter++;
                }
            }
        }
    }


    private void processField(IndentedWriter writer, FieldHandlerLookup handlerLookup, FieldContext fieldContext)
    {
        FieldHandler handler = null;
        if (fieldContext.isPrimitive())
        {
            handler = new DefaultFieldHandler();
        }
        else if (fieldContext.isEnum())
        {
            handler = new EnumFieldHandler();
        }
        else if (fieldContext.isArray())
        {
            handler = new ArrayFieldHandler();
        }
        else
        {
            // Ask the FieldHandlerLookup for all other stuff (basic types,
            // collections, maps, ...)
            handler = handlerLookup.get(fieldContext.getType().getQualifiedSourceName());
            if (handler == null)
            {
                // Delegate to the XmlRegistry to resolve other mapped models
                handler = new XmlRegistryFieldHandler();
            }
        }
        handler.write(writer, fieldContext);
    }


    // --------------------------------------------------------- helper methods

    /**
     * Post an error message and halt processing. This method always throws an
     * {@link UnableToCompleteException}
     */
    public void die(String message) throws UnableToCompleteException
    {
        logger.log(TreeLogger.ERROR, message);
        throw new UnableToCompleteException();
    }


    /**
     * Post an error message and halt processing. This method always throws an
     * {@link UnableToCompleteException}
     */
    public void die(String message, Object... params) throws UnableToCompleteException
    {
        logger.log(TreeLogger.ERROR, String.format(message, params));
        throw new UnableToCompleteException();
    }
}
