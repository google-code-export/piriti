package name.pehl.piriti.rebind;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import name.pehl.piriti.rebind.propertyhandler.PropertyContext;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandler;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandlerRegistry;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

/**
 * Base class for creating deferred binding implementations. This class contains
 * some common code and behaviour.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 136 $
 */
public abstract class AbstractCreator
{
    // -------------------------------------------------------- private members

    protected final GeneratorContext context;
    protected final JClassType interfaceType;
    protected final String implName;
    protected final TreeLogger logger;
    protected final JClassType modelType;
    protected final PropertyHandlerRegistry handlerRegistry;


    // ----------------------------------------------------------- constructors

    public AbstractCreator(GeneratorContext context, JClassType interfaceType, String implName, String readerClassname,
            TreeLogger logger) throws UnableToCompleteException
    {
        this.context = context;
        this.interfaceType = interfaceType;
        this.implName = implName;
        this.logger = logger;
        this.handlerRegistry = setupFieldHandlerRegistry();

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


    /**
     * Method to setup the field handler registry used in this creator.
     */
    protected abstract PropertyHandlerRegistry setupFieldHandlerRegistry();


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
     * Adds some common import statements.
     * 
     * @param writer
     * @throws UnableToCompleteException
     */
    protected void createImports(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("import java.util.ArrayList;");
        writer.write("import java.util.Iterator;");
        writer.write("import java.util.HashMap;");
        writer.write("import java.util.List;");
        writer.write("import java.util.Map;");
        writer.write("import java.util.Set;");
        writer.write("import name.pehl.piriti.client.PiritiGinjector;");
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
        writer.write("this.converterRegistry = PiritiGinjector.INJECTOR.getConverterRegistry();");
    }


    /**
     * Subclasses must implement this method to create the actual code.
     * 
     * @param writer
     * @throws UnableToCompleteException
     */
    protected abstract void createMethods(IndentedWriter writer) throws UnableToCompleteException;


    protected abstract void handleProperty(IndentedWriter writer, PropertyHandler fieldHandler, PropertyContext fieldContext,
            boolean hasNext) throws UnableToCompleteException;


    // --------------------------------------------------------- helper methods

    /**
     * Returns all fields from the specified type <b>and</b> all of its
     * supertypes that are marked with the specified annotation. Returns an
     * empty array if no annotated fields were found.
     * 
     * @param <T>
     * @param type
     * @param annotationClass
     * @return
     */
    protected <T extends Annotation> JField[] findAnnotatedFields(JClassType type, Class<T> annotationClass)
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
     * Returns all public setters from the specified type <b>and</b> all of its
     * supertypes that are marked with the specified annotation. Returns an
     * empty array if no annotated setters were found.
     * 
     * @param <T>
     * @param type
     * @param annotationClass
     * @return
     */
    protected <T extends Annotation> JMethod[] findAnnotatedSetters(JClassType type, Class<T> annotationClass)
    {
        List<JMethod> setters = new ArrayList<JMethod>();
        collectSetters(type, setters, annotationClass);
        return setters.toArray(new JMethod[] {});
    }


    private <T extends Annotation> void collectSetters(JClassType type, List<JMethod> setters, Class<T> annotationClass)
    {
        // Superclass first please!
        if (type == null)
        {
            return;
        }
        collectSetters(type.getSuperclass(), setters, annotationClass);

        JMethod[] allMethods = type.getMethods();
        if (allMethods != null)
        {
            for (JMethod method : allMethods)
            {
                if (method.isPublic() && JPrimitiveType.VOID.equals(method.getReturnType())
                        && method.getName().startsWith("set"))
                {
                    JParameter[] parameters = method.getParameters();
                    if (parameters != null && parameters.length == 1)
                    {
                        if (method.isAnnotationPresent(annotationClass))
                        {
                            setters.add(method);
                        }
                    }
                }
            }
        }
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
