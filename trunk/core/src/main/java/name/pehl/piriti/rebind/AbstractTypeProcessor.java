package name.pehl.piriti.rebind;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * @author $LastChangedBy:$
 * @version $LastChangedRevision:$
 */
public abstract class AbstractTypeProcessor implements TypeProcessor
{
    private TypeProcessor next;
    private Set<? extends JClassType> skipTypes;


    public AbstractTypeProcessor()
    {
        super();
        skipTypes = new HashSet<JClassType>();
    }


    @Override
    public TypeProcessor getNext()
    {
        return next;
    }


    @Override
    public void setNext(TypeProcessor processor)
    {
        next = processor;
    }


    @Override
    public boolean hasNext()
    {
        return next != null;
    }


    /**
     * @param typeContext
     * @throws UnableToCompleteException
     * @see name.pehl.piriti.rebind.TypeProcessor#process(name.pehl.piriti.rebind.TypeContext)
     */
    @Override
    public final void process(TypeContext typeContext) throws UnableToCompleteException
    {
        // prepare
        VariableNames variableNames = typeContext.getVariableNames();
        skipTypes = typeContext.getStopAt().getFlattenedSupertypeHierarchy();

        // process
        doProcess(typeContext, skipTypes, variableNames);

        // next one please
        if (hasNext())
        {
            getNext().process(typeContext);
        }
    }


    protected abstract void doProcess(TypeContext typeContext, Set<? extends JClassType> skipTypes2,
            VariableNames variableNames) throws UnableToCompleteException;


    protected boolean skipType(JClassType type)
    {
        if (type == null)
        {
            return true;
        }
        for (JClassType t : skipTypes)
        {
            if (t.isInterface() != null)
            {
                continue;
            }
            if (t.equals(type))
            {
                return true;
            }
        }
        return false;
    }
}
