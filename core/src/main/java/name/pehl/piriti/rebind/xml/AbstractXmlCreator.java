package name.pehl.piriti.rebind.xml;

import static name.pehl.piriti.rebind.propertyhandler.Assignment.AssignmentPolicy.*;
import static name.pehl.piriti.rebind.propertyhandler.Assignment.AssignmentType.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import name.pehl.piriti.client.xml.XmlField;
import name.pehl.piriti.client.xml.XmlFields;
import name.pehl.piriti.client.xml.XmlReader;
import name.pehl.piriti.client.xml.XmlWriter;
import name.pehl.piriti.rebind.AbstractCreator;
import name.pehl.piriti.rebind.IndentedWriter;
import name.pehl.piriti.rebind.TypeUtils;
import name.pehl.piriti.rebind.propertyhandler.Assignment;
import name.pehl.piriti.rebind.propertyhandler.PropertyAnnotation;
import name.pehl.piriti.rebind.propertyhandler.PropertyContext;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandler;
import name.pehl.piriti.rebind.propertyhandler.PropertyHandlerRegistry;
import name.pehl.piriti.rebind.propertyhandler.VariableNames;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * Common creator for {@linkplain XmlReader}s and {@linkplain XmlWriter}s.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 139 $
 */
public abstract class AbstractXmlCreator extends AbstractCreator
{
    // ---------------------------------------------------------- constructors

    public AbstractXmlCreator(GeneratorContext context, JClassType interfaceType, String implName,
            String readerClassname, TreeLogger logger) throws UnableToCompleteException
    {
        super(context, interfaceType, implName, readerClassname, logger);
    }


    // ---------------------------------------------------- overwritten methods

    @Override
    protected PropertyHandlerRegistry setupFieldHandlerRegistry()
    {
        return new XmlPropertyHandlerRegistry();
    }


    // --------------------------------------------------------- create methods

    @Override
    protected void createImports(IndentedWriter writer) throws UnableToCompleteException
    {
        super.createImports(writer);
        writer.write("import name.pehl.piriti.client.xml.*;");
        writer.write("import name.pehl.totoe.client.*;");
        writer.write("import static name.pehl.piriti.client.xml.XmlReader.*;");
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
        writer.write("this.xmlRegistry = PiritiGinjector.INJECTOR.getXmlRegistry();");
        writer.write("this.xmlRegistry.register(%s.class, this);", modelType.getQualifiedSourceName());
    }


    protected void handleFields(IndentedWriter writer) throws UnableToCompleteException
    {
        int counter = 0;
        Map<String, PropertyAnnotation<XmlField>> properties = findFieldAnnotations();
        for (Iterator<PropertyAnnotation<XmlField>> iter = properties.values().iterator(); iter.hasNext();)
        {
            PropertyAnnotation<XmlField> propertyAnnotation = iter.next();
            String xpath = calculateXpath(propertyAnnotation.getField(), propertyAnnotation.getAnnotation().value());
            // TODO Implement usage of setters
            Assignment assignment = new Assignment(MAPPING, FIELD_FIRST);
            VariableNames variableNames = new VariableNames("element", "value" + counter, "xmlBuilder");
            PropertyContext propertyContext = new PropertyContext(context.getTypeOracle(), handlerRegistry, modelType,
                    propertyAnnotation.getField().getType(), propertyAnnotation.getField().getName(), xpath,
                    propertyAnnotation.getAnnotation().format(), propertyAnnotation.getAnnotation().stripWsnl(),
                    propertyAnnotation.getAnnotation().converter(), assignment, variableNames);
            PropertyHandler fieldHandler = handlerRegistry.findPropertyHandler(propertyContext);
            if (fieldHandler != null && fieldHandler.isValid(writer, propertyContext))
            {
                writer.newline();
                handleProperty(writer, fieldHandler, propertyContext, iter.hasNext());
                counter++;
            }
        }
    }


    /**
     * Returns a map with the fields name as key and the
     * {@link PropertyAnnotation} for {@link XmlField} as value.
     * 
     * @return
     */
    protected Map<String, PropertyAnnotation<XmlField>> findFieldAnnotations()
    {
        Map<String, PropertyAnnotation<XmlField>> fields = new HashMap<String, PropertyAnnotation<XmlField>>();

        // Step 1: Add all XmlField annotations in the XmlFields annotation
        // from the interfaceType
        XmlFields xmlFields = interfaceType.getAnnotation(XmlFields.class);
        if (xmlFields != null)
        {
            XmlField[] annotations = xmlFields.value();
            for (XmlField annotation : annotations)
            {
                JField field = modelType.getField(annotation.name());
                if (field != null)
                {
                    fields.put(field.getName(), new PropertyAnnotation<XmlField>(field, annotation));
                }
                // TODO Is it an error if field == null?
            }
        }

        // Step 2: Add all XmlField annotations of the modelType fields. If
        // there's already an entry for the field from step 1, it will be
        // overwritten!
        JField[] modelTypeFields = findAnnotatedFields(modelType, XmlField.class);
        for (JField field : modelTypeFields)
        {
            XmlField annotation = field.getAnnotation(XmlField.class);
            fields.put(field.getName(), new PropertyAnnotation<XmlField>(field, annotation));
        }
        return fields;
    }


    protected String calculateXpath(JField field, String defaultValue)
    {
        String xpath = defaultValue;
        if (xpath == null || xpath.length() == 0)
        {
            xpath = field.getName();
            JType fieldType = field.getType();
            if (fieldType.isPrimitive() != null || TypeUtils.isBasicType(fieldType) || fieldType.isEnum() != null)
            {
                xpath += "/text()";
            }
        }
        return xpath;
    }
}
