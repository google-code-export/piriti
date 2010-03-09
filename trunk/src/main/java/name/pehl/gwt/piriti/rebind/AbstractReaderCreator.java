package name.pehl.gwt.piriti.rebind;

import java.io.PrintWriter;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * Base class for creating reader implementations. This class contains some
 * common code and behaviour.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 136 $
 */
public abstract class AbstractReaderCreator
{
    // -------------------------------------------------------- private members

    protected final GeneratorContext context;
    protected final JClassType interfaceType;
    protected final String implName;
    protected final TreeLogger logger;
    protected final JClassType modelType;
    protected FieldHandlerRegistry handlerRegistry;


    // ----------------------------------------------------------- constructors

    public AbstractReaderCreator(GeneratorContext context, JClassType interfaceType, String implName,
            String readerClassname, TreeLogger logger) throws UnableToCompleteException
    {
        this.context = context;
        this.interfaceType = interfaceType;
        this.implName = implName;
        this.logger = logger;
        this.handlerRegistry = new FieldHandlerRegistry();

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
    }


    // --------------------------------------------------------- create methods

    /**
     * Creates the code for the reader implementation. Therefore the following
     * methods are called:
     * <ol>
     * <li>{@link #createPackage(IndentedWriter)}
     * <li>{@link #createImports(IndentedWriter)}
     * <li>{@link #createClass(IndentedWriter)}
     * <ol>
     * <li>{@link #createMemberVariables(IndentedWriter)}
     * <li>{@link #createConstructor(IndentedWriter)}
     * <ol>
     * <li>{@link #createConstructorBody(IndentedWriter)}
     * </ol>
     * <li>{@link #createMethods(IndentedWriter)}
     * </ol>
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
        writer.write("import name.pehl.gwt.piriti.client.converter.*;");
    }


    /**
     * Creates the reader class. Therefore the following methods are called:
     * <ol>
     * <li>{@link #createMemberVariables(IndentedWriter)}
     * <li>{@link #createConstructor(IndentedWriter)}
     * <ol>
     * <li>{@link #createConstructorBody(IndentedWriter)}
     * </ol>
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
    }


    /**
     * Empty implementation.
     * 
     * @param writer
     */
    protected void createMethods(IndentedWriter writer) throws UnableToCompleteException
    {
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
