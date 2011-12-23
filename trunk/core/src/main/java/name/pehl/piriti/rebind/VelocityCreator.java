package name.pehl.piriti.rebind;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import name.pehl.piriti.rebind.type.PojoTypeProcessor;
import name.pehl.piriti.rebind.type.RwTypeProcessor;
import name.pehl.piriti.rebind.type.TypeContext;
import name.pehl.piriti.rebind.type.TypeProcessor;
import name.pehl.piriti.rebind.type.TypeUtils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

public abstract class VelocityCreator
{
    // -------------------------------------------------------------- constants

    private static final String VELOCITY_PROPERTIES = "name/pehl/piriti/rebind/velocity.properties";

    // -------------------------------------------------------- private members

    private final JClassType rwType;
    private final String implName;
    private JClassType type;


    // ----------------------------------------------------------- constructors

    public VelocityCreator(JClassType rwType, String implName, String rwClassname) throws UnableToCompleteException
    {
        // Check for possible misuse:
        // GWT.create(XmlReader.class)
        JClassType rwInterface = GeneratorContextHolder.get().getContext().getTypeOracle().findType(rwClassname);
        if (rwInterface.equals(rwType))
        {
            Logger.get().die(
                    "You must use a subtype of {0} in GWT.create(). E.g.,\n"
                            + "  interface ModelReader extends {0}<Model> {}\n  GWT.create(ModelReader.class);");
        }

        // Check for right interface:
        // interface FooReader extends JsonReader<Foo> {}
        JClassType[] rwTypes = rwType.getImplementedInterfaces();
        if (rwTypes.length == 0)
        {
            Logger.get().die("No implemented interfaces for %s", rwType.getName());
        }

        // Look for the type parameter
        for (JClassType t : rwTypes)
        {
            if (t.getQualifiedSourceName().equals(rwInterface.getQualifiedSourceName()))
            {
                JClassType[] typeArgs = t.isParameterized().getTypeArgs();
                if (typeArgs.length != 1)
                {
                    Logger.get().die("One type parameter is required for %s", t.getName());
                }
                type = typeArgs[0];
                break;
            }
        }
        if (type == null)
        {
            Logger.get().die("No type parameter found in %s", (Object[]) rwTypes);
        }
        if (type.isParameterized() != null)
        {
            Logger.get().die("Type parameters for the model are not supported!");
        }
        this.rwType = rwType;
        this.implName = implName;
    }


    // ------------------------------------------------------- template methods

    public void createCode() throws UnableToCompleteException
    {
        PrintWriter printWriter = GeneratorContextHolder.get().getContext()
                .tryCreate(Logger.get().getTreeLogger(), rwType.getPackage().getName(), implName);
        if (printWriter != null)
        {
            // collect properties, id and references
            TypeProcessor typeProcessor = new PojoTypeProcessor();
            typeProcessor.setNext(new RwTypeProcessor());
            TypeContext typeContext = new TypeContext(type, rwType);
            typeProcessor.process(typeContext);

            // setup velocity context
            VelocityContext context = new VelocityContext();
            context.put("TypeUtils", TypeUtils.class);
            context.put("typeContext", typeContext);
            context.put("implName", implName);

            // merge template
            VelocityEngine velocityEngine = createVelocityEngine();
            velocityEngine.mergeTemplate(getTemplate(), "UTF-8", context, printWriter);
            GeneratorContextHolder.get().getContext().commit(Logger.get().getTreeLogger(), printWriter);
        }
    }


    // --------------------------------------------------------- helper methods

    /**
     * Returns the velocity template which should be merged.
     * 
     * @return the velocity template which should be merged.
     */
    protected abstract String getTemplate();


    /**
     * Creates the Velocity engine with the properties from
     * {@value #VELOCITY_PROPERTIES}
     * 
     * @return the intialized Velocity engine.
     * @throws UnableToCompleteException
     */
    private VelocityEngine createVelocityEngine() throws UnableToCompleteException
    {
        VelocityEngine engine = null;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(VELOCITY_PROPERTIES);
        Properties properties = new Properties();
        try
        {
            properties.load(inputStream);
            engine = new VelocityEngine(properties);
        }
        catch (IOException e)
        {
            Logger.get().die("Cannot load velocity properties from " + VELOCITY_PROPERTIES);
        }
        return engine;
    }
}
