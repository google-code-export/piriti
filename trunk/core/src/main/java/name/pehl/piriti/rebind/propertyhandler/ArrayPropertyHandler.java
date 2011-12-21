package name.pehl.piriti.rebind.propertyhandler;

import name.pehl.piriti.rebind.PropertyContext;
import name.pehl.piriti.rebind.TypeUtils;

import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * {@link PropertyHandler} for arrays.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 139 $
 */
public class ArrayPropertyHandler extends AbstractPropertyHandler
{
    @Override
    public boolean isValid(PropertyContext propertyContext)
    {
        if (!super.isValid(propertyContext))
        {
            return false;
        }
        JType elementType = propertyContext.getArrayType().getComponentType();
        if (elementType.isArray() != null)
        {
            skipProperty(propertyContext, "Multi-dimensional arrays are not supported");
            return false;
        }
        if (TypeUtils.isCollection(elementType) || TypeUtils.isMap(elementType))
        {
            skipProperty(propertyContext, "Arrays of collections / maps are not supported");
            return false;
        }
        JPrimitiveType primitiveComponentType = elementType.isPrimitive();
        if (primitiveComponentType != null)
        {
            elementType = propertyContext.getTypeContext().getTypeOracle()
                    .findType(primitiveComponentType.getQualifiedBoxedSourceName());

        }
        PropertyContext nestedContext = new PropertyContext.Builder(propertyContext, elementType).build();
        PropertyHandler nestedHandler = new PropertyHandlerLookup().lookup(nestedContext);
        if (nestedHandler == null)
        {
            skipProperty(propertyContext, "No property handler found for element type" + nestedContext);
            return false;
        }
        if (!nestedHandler.isValid(nestedContext))
        {
            skipProperty(propertyContext, "Element type " + nestedContext + " is not valid");
            return false;
        }
        return true;
    }


    @Override
    public void calculateTemplate(PropertyContext propertyContext)
    {
        super.calculateTemplate(propertyContext);

        JType elementType = propertyContext.getArrayType().getComponentType();
        JPrimitiveType primitiveComponentType = elementType.isPrimitive();
        if (primitiveComponentType != null)
        {
            elementType = propertyContext.getTypeContext().getTypeOracle()
                    .findType(primitiveComponentType.getQualifiedBoxedSourceName());

        }
        StringBuilder nestedTemplate = basePath(propertyContext);
        PropertyContext nestedContext = new PropertyContext.Builder(propertyContext, elementType).build();
        PropertyHandler nestedHandler = new PropertyHandlerLookup().lookup(nestedContext);
        if (nestedHandler.isValid(nestedContext))
        {
            nestedTemplate.append("Nested").append(nestedHandler.getClass().getSimpleName()).append(".vm");
        }
        nestedContext.setTemplate(nestedTemplate.toString());
    }
}
