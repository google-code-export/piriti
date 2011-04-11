package name.pehl.piriti.rebind;

import static name.pehl.piriti.rebind.propertyhandler.ReferenceType.ID;
import static name.pehl.piriti.rebind.propertyhandler.ReferenceType.IDREF;

import java.util.Set;

import name.pehl.piriti.commons.client.Mapping;
import name.pehl.piriti.commons.client.Mappings;
import name.pehl.piriti.commons.client.WhitespaceHandling;
import name.pehl.piriti.converter.client.Converter;
import name.pehl.piriti.property.client.PropertyGetter;
import name.pehl.piriti.property.client.PropertySetter;
import name.pehl.piriti.rebind.propertyhandler.ReferenceType;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;

public class RwTypeProcessor extends AbstractTypeProcessor
{
    public RwTypeProcessor(TreeLogger logger)
    {
        super(logger);
    }


    @Override
    protected void doProcess(TypeContext typeContext, Set<? extends JClassType> skipTypes)
            throws UnableToCompleteException
    {
        JClassType rwType = typeContext.getRwType();
        if (rwType.isAnnotationPresent(Mappings.class))
        {
            int index = 0;
            Mappings mappingsAnno = rwType.getAnnotation(Mappings.class);
            Mapping[] mappings = mappingsAnno.value();
            for (Mapping mapping : mappings)
            {
                PropertyContext propertyContext = createPropertyContext(index++, typeContext, mapping, null);
                if (propertyContext != null)
                {
                    typeContext.addProperty(propertyContext);
                }
            }

            Mapping idMapping = mappingsAnno.id();
            if (!idMapping.value().equals(Mappings.NO_ID))
            {
                PropertyContext propertyContext = createPropertyContext(TypeContext.nextOrder(), typeContext,
                        idMapping, ID);
                if (propertyContext != null)
                {
                    typeContext.setId(propertyContext);
                }
            }

            Mapping[] idRefMappings = mappingsAnno.references();
            for (Mapping idRefMapping : idRefMappings)
            {
                PropertyContext propertyContext = createPropertyContext(TypeContext.nextOrder(), typeContext,
                        idRefMapping, IDREF);
                if (propertyContext != null)
                {
                    typeContext.addReference(propertyContext);
                }
            }
        }
    }


    protected PropertyContext createPropertyContext(int order, TypeContext typeContext, Mapping mapping,
            ReferenceType referenceType) throws UnableToCompleteException
    {
        PropertyContext propertyContext = null;
        JField field = typeContext.getType().getField(mapping.value());
        if (field != null)
        {
            String path = mapping.path() != null ? mapping.path() : mapping.value();
            String format = mapping.format();
            WhitespaceHandling whitespaceHandling = mapping.whitespace();
            Class<? extends Converter<?>> converter = mapping.convert();
            Class<? extends PropertyGetter<?, ?>> getter = mapping.getter();
            Class<? extends PropertySetter<?, ?>> setter = mapping.setter();
            propertyContext = new PropertyContext(order, typeContext, field.getType(), field.getName(), path, format,
                    whitespaceHandling, converter, getter, setter, null, logger);
        }
        else
        {
            // TODO Warning / error?
        }
        return propertyContext;
    }
}
