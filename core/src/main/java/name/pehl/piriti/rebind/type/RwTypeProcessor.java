package name.pehl.piriti.rebind.type;

import java.util.Set;

import name.pehl.piriti.commons.client.InstanceCreator;
import name.pehl.piriti.commons.client.Mapping;
import name.pehl.piriti.commons.client.Mappings;
import name.pehl.piriti.converter.client.Converter;
import name.pehl.piriti.property.client.PropertyGetter;
import name.pehl.piriti.property.client.PropertySetter;
import name.pehl.piriti.rebind.Logger;
import name.pehl.piriti.rebind.ReferenceType;
import name.pehl.piriti.rebind.property.PropertyContext;
import name.pehl.piriti.rebind.property.PropertySource;
import name.pehl.totoe.commons.client.WhitespaceHandling;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;

public class RwTypeProcessor extends AbstractTypeProcessor
{
    @Override
    protected void doProcess(TypeContext typeContext, Set<? extends JClassType> skipTypes)
            throws UnableToCompleteException
    {
        JClassType rwType = typeContext.getRwType();
        if (rwType.isAnnotationPresent(Mappings.class))
        {
            Logger.get().debug("Collect normal mappings...");
            Mappings mappingsAnno = rwType.getAnnotation(Mappings.class);
            Mapping[] mappings = mappingsAnno.value();
            for (Mapping mapping : mappings)
            {
                addProperty(typeContext, fromAnnotation(mapping));
            }
            Logger.get().debug("Normal mappings done");

            Logger.get().debug("Looking for id...");
            Mapping idMapping = mappingsAnno.id();
            if (!idMapping.value().equals(Mappings.NO_ID))
            {
                setId(typeContext, fromAnnotation(idMapping));
            }
            Logger.get().debug("Id done");

            Logger.get().debug("Collect reference mappings...");
            Mapping[] refMappings = mappingsAnno.references();
            for (Mapping refMapping : refMappings)
            {
                addProperty(typeContext, fromAnnotation(refMapping));
            }
            Logger.get().debug("Reference mappings done");
        }
    }


    private PropertySource fromAnnotation(Mapping mapping)
    {
        return null;
    }


    protected PropertyContext createPropertyContext(int order, TypeContext typeContext, Mapping mapping,
            ReferenceType referenceType) throws UnableToCompleteException
    {
        PropertyContext propertyContext = null;
        JField field = TypeUtils.findField(typeContext.getType(), mapping.value());
        if (field != null)
        {
            String path = mapping.path();
            Class<? extends Converter<?>> converter = mapping.convert();
            String format = mapping.format();
            WhitespaceHandling whitespaceHandling = mapping.whitespace();
            Class<? extends InstanceCreator<?, ?>> instanceCreator = mapping.createWith();
            Class<? extends PropertyGetter<?, ?>> getter = mapping.getter();
            Class<? extends PropertySetter<?, ?>> setter = mapping.setter();
            boolean native_ = mapping.native_();

            propertyContext = new PropertyContext.Builder(order, typeContext, field.getType(), field.getName())
                    .path(path).converter(converter).format(format).whitespaceHandling(whitespaceHandling)
                    .instanceCreator(instanceCreator).getter(getter).setter(setter).referenceType(referenceType)
                    .native_(native_).build();
            return propertyContext;
        }
        else
        {
            Logger.get().die("Field %s cannot be found in type hirarchy of %s!", mapping.value(),
                    typeContext.getType().getParameterizedQualifiedSourceName());
        }
        return propertyContext;
    }
}
