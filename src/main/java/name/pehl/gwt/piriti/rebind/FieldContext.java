package name.pehl.gwt.piriti.rebind;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JEnumType;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

/**
 * Class which contains information needed to generate code for the XPath
 * evaluation, conversion and assignment of a field. An instance of this class
 * is generated in {@link XmlReaderCreator} and passed to the
 * {@link FieldHandler}.
 * 
 * @author $LastChangedBy$
 * @version $LastChangedRevision$
 */
public class FieldContext
{
    private static final String AS_STRING_SUFFIX = "AsString";
    private static final String READER_SUFFIX = "Reader";

    private final TypeOracle typeOracle;
    private final FieldHandlerRegistry handlerRegistry;
    private final JClassType modelType;
    private final JType fieldType;
    private final String fieldName;
    private final String xpath;
    private final String format;
    private final String xmlVariable;
    private final String valueVariable;
    private final String valueAsStringVariable;
    private final String valueReaderVariable;


    public FieldContext(TypeOracle typeOracle, FieldHandlerRegistry handlerRegistry, JClassType modelType,
            JType fieldType, String fieldName, String xpath, String format, String xmlVariable, String valueVariable)
            throws UnableToCompleteException
    {
        this.typeOracle = typeOracle;
        this.handlerRegistry = handlerRegistry;
        this.modelType = modelType;

        JPrimitiveType primitiveType = fieldType.isPrimitive();
        if (primitiveType != null) // isPrimitive() is not available here!
        {
            try
            {
                // Use the boxed type for primitives
                this.fieldType = typeOracle.getType(primitiveType.getQualifiedBoxedSourceName());
            }
            catch (NotFoundException e)
            {
                throw new UnableToCompleteException();
            }
        }
        else
        {
            this.fieldType = fieldType;
        }

        this.fieldName = fieldName;
        this.xpath = adjustXpath(fieldName, xpath);
        if (format == null || format.length() == 0)
        {
            this.format = null;
        }
        else
        {
            this.format = format;
        }
        this.xmlVariable = xmlVariable;
        this.valueVariable = valueVariable;
        this.valueAsStringVariable = valueVariable + AS_STRING_SUFFIX;
        this.valueReaderVariable = valueVariable + READER_SUFFIX;
    }


    private String adjustXpath(String defaultValue, String xpath)
    {
        String effectiveXpath = xpath;
        if (effectiveXpath == null || effectiveXpath.length() == 0)
        {
            effectiveXpath = defaultValue;
            if (isPrimitive() || isBasicType() || isEnum())
            {
                effectiveXpath += "/text()";
            }
        }
        return effectiveXpath;
    }


    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder().append(fieldType.getParameterizedQualifiedSourceName()).append(" ")
                .append(fieldName).append(", xpath=\"").append(xpath).append("\"");
        if (format != null)
        {
            builder.append(", format=\"").append(format).append("\"");
        }
        return builder.toString();
    }


    public boolean isPrimitive()
    {
        return getPrimitiveType() != null;
    }


    public JPrimitiveType getPrimitiveType()
    {
        return fieldType.isPrimitive();
    }


    public boolean isBasicType()
    {
        return TypeUtils.isBasicType(fieldType);
    }


    public boolean isEnum()
    {
        return getEnumType() != null;
    }


    public JEnumType getEnumType()
    {
        return fieldType.isEnum();
    }


    public boolean isClassOrInterface()
    {
        return getClassOrInterfaceType() != null;
    }


    public JClassType getClassOrInterfaceType()
    {
        return fieldType.isClass();
    }


    public boolean isArray()
    {
        return getArrayType() != null;
    }


    public JArrayType getArrayType()
    {
        return fieldType.isArray();
    }


    public boolean isCollection()
    {
        return TypeUtils.isCollection(fieldType);
    }


    public boolean isMap()
    {
        return TypeUtils.isMap(fieldType);
    }


    public TypeOracle getTypeOracle()
    {
        return typeOracle;
    }


    public FieldHandlerRegistry getHandlerRegistry()
    {
        return handlerRegistry;
    }


    public JClassType getModelType()
    {
        return modelType;
    }


    public JType getFieldType()
    {
        return fieldType;
    }


    public String getFieldName()
    {
        return fieldName;
    }


    public String getXpath()
    {
        return xpath;
    }


    public String getFormat()
    {
        return format;
    }


    public String getXmlVariable()
    {
        return xmlVariable;
    }


    public String getValueVariable()
    {
        return valueVariable;
    }


    public String getValueAsStringVariable()
    {
        return valueAsStringVariable;
    }


    public String getValueReaderVariable()
    {
        return valueReaderVariable;
    }
}
