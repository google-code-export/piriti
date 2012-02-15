package name.pehl.piriti.rebind.type;

import java.util.Set;

import name.pehl.piriti.commons.client.InstanceCreator;
import name.pehl.piriti.commons.client.Mapping;
import name.pehl.piriti.commons.client.Mappings;
import name.pehl.piriti.converter.client.Converter;
import name.pehl.piriti.property.client.PropertyGetter;
import name.pehl.piriti.property.client.PropertySetter;
import name.pehl.piriti.rebind.Logger;
import name.pehl.piriti.rebind.property.PropertySource;
import name.pehl.totoe.commons.client.WhitespaceHandling;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;

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
                addProperty(typeContext, new AnnotationPropertySource(typeContext, mapping));
            }
            Logger.get().debug("Normal mappings done");

            Logger.get().debug("Looking for id...");
            Mapping idMapping = mappingsAnno.id();
            if (!idMapping.value().equals(Mappings.NO_ID))
            {
                setId(typeContext, new AnnotationPropertySource(typeContext, idMapping));
            }
            Logger.get().debug("Id done");

            Logger.get().debug("Collect reference mappings...");
            Mapping[] refMappings = mappingsAnno.references();
            for (Mapping refMapping : refMappings)
            {
                addReference(typeContext, new AnnotationPropertySource(typeContext, refMapping));
            }
            Logger.get().debug("Reference mappings done");
        }
    }

    static class AnnotationPropertySource implements PropertySource
    {
        final Mapping mapping;
        final JField field;


        AnnotationPropertySource(TypeContext typeContext, Mapping mapping)
        {
            this.mapping = mapping;
            this.field = TypeUtils.findField(typeContext.getType(), mapping.value());
        }


        @Override
        public int getOrder()
        {
            return TypeContext.nextOrder();
        }


        @Override
        public JType getType()
        {
            return field.getType();
        }


        @Override
        public String getName()
        {
            return field.getName();
        }


        @Override
        public String getPath()
        {
            return mapping.path();
        }


        @Override
        public Class<? extends Converter<?>> getConverter()
        {
            return mapping.convert();
        }


        @Override
        public String getFormat()
        {
            return mapping.format();
        }


        @Override
        public WhitespaceHandling getWhitespaceHandling()
        {
            return mapping.whitespace();
        }


        @Override
        public boolean isNative()
        {
            return mapping.native_();
        }


        @Override
        public Class<? extends InstanceCreator<?, ?>> getInstanceCreator()
        {
            return mapping.createWith();
        }


        @Override
        public Class<? extends PropertyGetter<?, ?>> getGetter()
        {
            return mapping.getter();
        }


        @Override
        public Class<? extends PropertySetter<?, ?>> getSetter()
        {
            return mapping.setter();
        }
    }
}
