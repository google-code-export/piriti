package name.pehl.piriti.rebind;

import name.pehl.piriti.rebind.propertyhandler.PropertyHandler;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;

public abstract class AbstractReaderCreator extends AbstractCreator
{
    public AbstractReaderCreator(GeneratorContext generatorContext, JClassType rwType, String implName,
            String rwClassname, TreeLogger logger) throws UnableToCompleteException
    {
        super(generatorContext, rwType, implName, rwClassname, logger);
    }


    @Override
    protected void createImports(IndentedWriter writer) throws UnableToCompleteException
    {
        super.createImports(writer);
        writer.write("import name.pehl.piriti.commons.client.InstanceContextHolder;");
    }


    @Override
    protected void createMethods(IndentedWriter writer) throws UnableToCompleteException
    {
        createReaderMethods(writer);
        writer.newline();

        readIds(writer);
        writer.newline();

        readProperties(writer);
        writer.newline();

        readIdRefs(writer);
        writer.newline();
    }


    protected abstract void createReaderMethods(IndentedWriter writer) throws UnableToCompleteException;


    protected abstract void readList(IndentedWriter writer) throws UnableToCompleteException;


    protected abstract void readSingle(IndentedWriter writer) throws UnableToCompleteException;


    protected void readIds(IndentedWriter writer) throws UnableToCompleteException
    {
        boolean validIdField = false;
        PropertyHandler handler = null;
        PropertyContext idContext = typeContext.getId();
        if (idContext != null)
        {
            handler = propertyHandlerLookup.lookup(idContext);
            validIdField = handler != null && handler.isValid(writer, idContext);
        }

        writer.write("private %s readIds(%s %s) {", typeContext.getType().getParameterizedQualifiedSourceName(),
                typeContext.getVariableNames().getInputType(), typeContext.getVariableNames().getInputVariable());
        writer.indent();
        writer.write("if (%s != null) {", typeContext.getVariableNames().getInputVariable());
        writer.indent();
        if (validIdField)
        {
            handler.log(writer, idContext);
            handler.declare(writer, idContext);
            handler.readInput(writer, idContext, propertyHandlerLookup);
            writer.write("%s %s = this.idRef(%s);", typeContext.getType().getParameterizedQualifiedSourceName(),
                    typeContext.getVariableNames().getInstanceVariable(), idContext.getVariableNames()
                            .getValueVariable());
            writer.write("if (%s == null) {", typeContext.getVariableNames().getInstanceVariable());
            writer.indent();
            newInstance(writer, false);
            handler.assign(writer, idContext);
            writer.outdent();
            writer.write("}");
        }
        else
        {
            newInstance(writer, true);
        }
        // TODO Is this necessary? Currently this causes StackOverflowError!
        handleIdsInNestedTypes(writer);

        writer.write("return %s;", typeContext.getVariableNames().getInstanceVariable());
        writer.outdent();
        writer.write("}");
        writer.write("return null;");
        writer.outdent();
        writer.write("}");
    }


    protected abstract void handleIdsInNestedTypes(IndentedWriter writer) throws UnableToCompleteException;


    protected void readProperties(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("private %1$s readProperties(%2$s %3$s, %1$s %4$s) {", typeContext.getType()
                .getParameterizedQualifiedSourceName(), typeContext.getVariableNames().getInputType(), typeContext
                .getVariableNames().getInputVariable(), typeContext.getVariableNames().getInstanceVariable());
        writer.indent();
        writer.write("if (%s != null) {", typeContext.getVariableNames().getInputVariable());
        writer.indent();

        handleProperties(writer);

        writer.outdent();
        writer.write("}");
        writer.write("return %s;", typeContext.getVariableNames().getInstanceVariable());
        writer.outdent();
        writer.write("}");
    }


    protected void readIdRefs(IndentedWriter writer) throws UnableToCompleteException
    {
        writer.write("private %1$s readIdRefs(%2$s %3$s, %1$s %4$s) {", typeContext.getType()
                .getParameterizedQualifiedSourceName(), typeContext.getVariableNames().getInputType(), typeContext
                .getVariableNames().getInputVariable(), typeContext.getVariableNames().getInstanceVariable());
        writer.indent();
        writer.write("if (%s != null) {", typeContext.getVariableNames().getInputVariable());
        writer.indent();

        handleIdRefs(writer);

        writer.outdent();
        writer.write("}");
        writer.write("return %s;", typeContext.getVariableNames().getInstanceVariable());
        writer.outdent();
        writer.write("}");
    }


    /**
     * Call distinct methods of {@link PropertyHandler} in this order
     * <ol>
     * <li> {@link PropertyHandler#log(IndentedWriter, PropertyContext)}
     * <li> {@link PropertyHandler#declare(IndentedWriter, PropertyContext)}
     * <li>
     * {@link PropertyHandler#readInput(IndentedWriter, PropertyContext, name.pehl.piriti.rebind.propertyhandler.PropertyHandlerLookup)}
     * <li> {@link PropertyHandler#assign(IndentedWriter, PropertyContext)}
     * </ol>
     * 
     * @see name.pehl.piriti.rebind.AbstractCreator#handleProperty(name.pehl.piriti.rebind.IndentedWriter,
     *      name.pehl.piriti.rebind.propertyhandler.PropertyHandler,
     *      name.pehl.piriti.rebind.PropertyContext, boolean)
     */
    @Override
    protected void handleProperty(IndentedWriter writer, PropertyHandler propertyHandler,
            PropertyContext propertyContext, boolean hasNext) throws UnableToCompleteException
    {
        propertyHandler.log(writer, propertyContext);
        propertyHandler.declare(writer, propertyContext);
        propertyHandler.readInput(writer, propertyContext, propertyHandlerLookup);
        propertyHandler.assign(writer, propertyContext);
    }
}
