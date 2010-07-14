package name.pehl.piriti.rebind;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import name.pehl.piriti.rebind.fieldhandler.FieldHandlerRegistry;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.ConfigurationProperty;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;

/**
 * Base class for creating reader implementations. This class contains some
 * common code and behaviour.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 136 $
 */
public abstract class AbstractReaderCreator
{
    // -------------------------------------------------------------- constants

    // Constants for the GinJectors NYI
    /**
     * The configuration property for the Converter GinJector
     */
    public static final String CONVERTER_GINJECTOR = "converter.ginjector";

    /**
     * The configuration property for the JSON GinJector
     */
    public static final String JSON_GINJECTOR = "json.ginjector";

    /**
     * The configuration property for the XML GinJector
     */
    public static final String XML_GINJECTOR = "xml.ginjector";

    // -------------------------------------------------------- private members

    protected final GeneratorContext context;
    protected final JClassType interfaceType;
    protected final String implName;
    protected final TreeLogger logger;
    protected final JClassType modelType;
    protected final FieldHandlerRegistry handlerRegistry;


    // ----------------------------------------------------------- constructors

    public AbstractReaderCreator(GeneratorContext context, JClassType interfaceType, String implName,
            String readerClassname, TreeLogger logger) throws UnableToCompleteException
    {
        this.context = context;
        this.interfaceType = interfaceType;
        this.implName = implName;
        this.logger = logger;

        // Check for possible misuse 'GWT.create(XmlReader.class)'
        JClassType xmlReaderItself = context.getTypeOracle().findType(readerClassname);
        if (xmlReaderItself.equals(interfaceType))
        {
            die("You must use a subtype of {0} in GWT.create(). E.g.,\n"
                    + "  interface ModelReader extends {0}<Model> {}\n  GWT.create(ModelReader.class);");
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
        this.handlerRegistry = setupFieldHandlerRegistry();
    }


    /**
     * Method to setup the field handler registry used in this creator.
     */
    protected abstract FieldHandlerRegistry setupFieldHandlerRegistry();


    // --------------------------------------------------------- create methods

    /**
     * Creates the code for the reader implementation. Therefore the following
     * methods are called:
     * <ol>
     * <li>{@link #createPackage(IndentedWriter)}
     * <li>{@link #createImports(IndentedWriter)}
     * <li>{@link #createClass(IndentedWriter)}
     * </ol>
     * 
     * @throws UnableToCompleteException
     */
    public void create() throws UnableToCompleteException
    {
        PrintWriter printWriter = context.tryCreate(logger, interfaceType.getPackage().getName(), implName);
        if (printWriter != null)
        {
            IndentedWriter writer = new IndentedWriter(printWriter);

            createPackage(writer);
            writer.newline();

            createImports(writer);
            writer.newline();

            createClass(writer);
            writer.newline();

            context.commit(logger, printWriter);
        }
    }


    /**
     * Creates the package declaration.
     * 
     * @param writer
     * @throws UnableToCompleteException
     */
    protected void createPackage(IndentedWriter writer) throws UnableToCompleteException
    {
        String packageName = interfaceType.getPackage().getName();
        if (packageName.length() > 0)
        {
            writer.write("package %s;", packageName);
        }
    }


    /**
     * Adds some common import statements. The following code is generated by
     * this method:
     * 
     * <pre>
     * java.util.ArrayList;
     * java.util.List;
     * name.pehl.gwt.piriti.client.converter.*;
     * </pre>
     * 
     * @param writer
     * @throws UnableToCompleteException
     */
    protected void createImports(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("import java.util.ArrayList;");
        writer.write("import java.util.List;");
        writer.write("import java.util.Map;");
        writer.write("import java.util.HashMap;");
        writer.write("import name.pehl.piriti.client.converter.*;");
    }


    /**
     * Creates the reader class. Therefore the following methods are called:
     * <ol>
     * <li>{@link #createMemberVariables(IndentedWriter)}
     * <li>{@link #createConstructor(IndentedWriter)}
     * <li>{@link #createMethods(IndentedWriter)}
     * </ol>
     * 
     * @param writer
     * @throws UnableToCompleteException
     */
    protected void createClass(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("public class %s implements %s {", implName, interfaceType.getQualifiedSourceName());
        writer.newline();
        writer.indent();

        createMemberVariables(writer);
        writer.newline();

        createConstructor(writer);
        writer.newline();

        createMethods(writer);
        writer.newline();

        writer.outdent();
        writer.write("}");
    }


    /**
     * Declares the member variables. The following code is generated by this
     * method:
     * 
     * <pre>
     * private ConverterRegistry converterRegistry;
     * </pre>
     * 
     * @param writer
     */
    protected void createMemberVariables(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("private ConverterRegistry converterRegistry;");
        writer.write("private Map<String,%s> idMap;", modelType.getQualifiedSourceName());
    }


    /**
     * Creates a public no-arg constructor. The body of the constructor is
     * created by {@link #createConstructorBody(IndentedWriter)}.
     * 
     * @param writer
     * @throws UnableToCompleteException
     */
    protected void createConstructor(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("public %s() {", implName);
        writer.indent();
        createConstructorBody(writer);
        writer.outdent();
        writer.write("}");
    }


    /**
     * Creates the constructor body. The following code is generated by this
     * method:
     * 
     * <pre>
     * this.converterRegistry = ConverterGinjector.INJECTOR.getConverterRegistry();
     * </pre>
     * 
     * @param writer
     */
    protected void createConstructorBody(IndentedWriter writer)
    {
        writer.write("this.converterRegistry = ConverterGinjector.INJECTOR.getConverterRegistry();");
        writer.write("this.idMap = new HashMap<String,%s>();", modelType.getQualifiedSourceName());
    }


    /**
     * Generates the idRef method.
     * 
     * @param writer
     */
    protected void createMethods(IndentedWriter writer) throws UnableToCompleteException
    {
        idRef(writer);
        writer.newline();
    }


    protected void idRef(IndentedWriter writer)
    {
        writer.write("public %s idRef(String id) {", modelType.getQualifiedSourceName());
        writer.indent();
        writer.write("return this.idMap.get(id);");
        writer.outdent();
        writer.write("}");
    }


    // --------------------------------------------------------- helper methods

    /**
     * Returns all fields from the specified type <b>and</b> all of its
     * supertypes that are marked with the specified annotation. Returns an
     * empty array if no fields were found
     * 
     * @param <T>
     * @param type
     * @param annotationClass
     * @return
     */
    protected <T extends Annotation> JField[] getAllFields(JClassType type, Class<T> annotationClass)
    {
        List<JField> fields = new ArrayList<JField>();
        collectFields(type, fields, annotationClass);
        return fields.toArray(new JField[] {});
    }


    private <T extends Annotation> void collectFields(JClassType type, List<JField> fields, Class<T> annotationClass)
    {
        // Superclass first please!
        if (type == null)
        {
            return;
        }
        collectFields(type.getSuperclass(), fields, annotationClass);

        JField[] allFields = type.getFields();
        if (allFields != null)
        {
            for (JField field : allFields)
            {
                if (field.isAnnotationPresent(annotationClass))
                {
                    fields.add(field);
                }
            }
        }
    }


    /**
     * Reads a configuration property from the module definition. Throws a
     * {@link UnableToCompleteException} if the property is not definied or
     * empty.
     * 
     * @param propertyName
     * @return
     */
    protected String readConfigurationProperty(String propertyName) throws UnableToCompleteException
    {
        String value = null;
        ConfigurationProperty property = null;
        String missingProperty = "No configuration property found for '" + property
                + "'.\n    Did you specifiy <set-configuration-property name=\"" + property
                + "\" value=\"...\" /> in the module definition?";
        try
        {
            property = context.getPropertyOracle().getConfigurationProperty(propertyName);
            if (property == null)
            {
                die(missingProperty);
            }
            List<String> values = property.getValues();
            if (values == null || values.isEmpty())
            {
                die(missingProperty);
            }
            value = values.get(0);
            if (value == null || value.length() == 0)
            {
                die(missingProperty);
            }
        }
        catch (BadPropertyValueException e)
        {
            die(missingProperty);
        }
        return value;
    }


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
