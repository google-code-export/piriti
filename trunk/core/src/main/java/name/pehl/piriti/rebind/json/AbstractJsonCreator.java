package name.pehl.piriti.rebind.json;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import name.pehl.piriti.client.json.Json;
import name.pehl.piriti.client.json.JsonMappings;
import name.pehl.piriti.client.json.JsonReader;
import name.pehl.piriti.client.json.JsonWriter;
import name.pehl.piriti.rebind.AbstractCreator;
import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.propertyhandler.MappingType;
import name.pehl.piriti.rebind.propertyhandler.PropertyAnnotation;
import name.pehl.piriti.rebind.propertyhandler.PropertyContext;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandler;
import name.pehl.piriti.rebind.propertyhandler.PropertyStyle;
import name.pehl.piriti.rebind.propertyhandler.VariableNames;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;

/**
 * Common creator for {@linkplain JsonReader}s and {@linkplain JsonWriter}s.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 137 $
 */
public abstract class AbstractJsonCreator extends AbstractCreator
{
    // ---------------------------------------------------------- constructors

    public AbstractJsonCreator(GeneratorContext context, JClassType interfaceType, String implName,
            String readerClassname, TreeLogger logger) throws UnableToCompleteException
    {
        super(context, interfaceType, implName, readerClassname, logger);
    }


    // --------------------------------------------------------- create methods

    @Override
    protected void createImports(IndentedWriter writer) throws UnableToCompleteException
    {
        super.createImports(writer);
        writer.write("import com.google.gwt.json.client.*;");
        writer.write("import name.pehl.piriti.client.json.*;");
    }


    @Override
    protected void createMemberVariables(IndentedWriter writer) throws UnableToCompleteException
    {
        super.createMemberVariables(writer);
        writer.write("private JsonRegistry jsonRegistry;");
        writer.write("private JsonParser jsonParser;");
    }


    @Override
    protected void createConstructorBody(IndentedWriter writer)
    {
        super.createConstructorBody(writer);
        writer.write("this.jsonRegistry = PiritiGinjector.INJECTOR.getJsonRegistry();");
        writer.write("this.jsonRegistry.register(%s.class, this);", modelType.getQualifiedSourceName());
        writer.write("this.jsonParser = PiritiGinjector.INJECTOR.getJsonParser();");
    }


    // --------------------------------------------------------- helper methods

    protected void handleProperties(IndentedWriter writer) throws UnableToCompleteException
    {
        int counter = 0;
        Map<String, PropertyAnnotation<Json>> properties = findPropertyAnnotations();
        for (Iterator<PropertyAnnotation<Json>> iter = properties.values().iterator(); iter.hasNext();)
        {
            PropertyAnnotation<Json> propertyAnnotation = iter.next();
            String jsonPath = calculateJsonPath(propertyAnnotation.getField(), propertyAnnotation.getAnnotation());
            // TODO Implement usage of setters
            VariableNames variableNames = new VariableNames("jsonObject", "value" + counter, "jsonBuilder");
            PropertyContext propertyContext = new PropertyContext(context.getTypeOracle(), handlerRegistry, modelType,
                    propertyAnnotation.getField().getType(), propertyAnnotation.getField().getName(), jsonPath,
                    propertyAnnotation.getAnnotation().format(), false, propertyAnnotation.getAnnotation().converter(),
                    MappingType.MAPPING, PropertyStyle.FIELD, variableNames);
            PropertyHandler propertyHandler = handlerRegistry.findPropertyHandler(propertyContext);
            if (propertyHandler != null && propertyHandler.isValid(writer, propertyContext))
            {
                writer.newline();
                handleProperty(writer, propertyHandler, propertyContext, iter.hasNext());
                counter++;
            }
        }
    }


    /**
     * Returns a map with the fields name as key and the
     * {@link PropertyAnnotation} for {@link Json} as value.
     * 
     * @return
     */
    private Map<String, PropertyAnnotation<Json>> findPropertyAnnotations()
    {
        Map<String, PropertyAnnotation<Json>> fields = new HashMap<String, PropertyAnnotation<Json>>();

        // Step 1: Add all JsonField annotations in the JsonFields annotation
        // from the interfaceType
        JsonMappings interfaceTypeFields = interfaceType.getAnnotation(JsonMappings.class);
        if (interfaceTypeFields != null)
        {
            Json[] annotations = interfaceTypeFields.value();
            for (Json annotation : annotations)
            {
                JField field = modelType.getField(annotation.property());
                if (field != null)
                {
                    fields.put(field.getName(), new PropertyAnnotation<Json>(field, annotation));
                }
                // TODO Is it an error if field == null?
            }
        }

        // Step 2: Add all JsonField annotations of the modelType fields. If
        // there's already an entry for the field from step 1, it will be
        // overwritten!
        JField[] modelTypeFields = findAnnotatedFields(modelType, Json.class);
        for (JField field : modelTypeFields)
        {
            Json annotation = field.getAnnotation(Json.class);
            fields.put(field.getName(), new PropertyAnnotation<Json>(field, annotation));
        }
        return fields;
    }


    protected String calculateJsonPath(JField field, Json jsonField)
    {
        String jsonPath = jsonField.value();
        if (jsonPath == null || jsonPath.length() == 0)
        {
            jsonPath = field.getName();
        }
        return jsonPath;
    }
}
