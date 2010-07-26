package name.pehl.piriti.client.converter;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.inject.internal.Nullable;

/**
 * Converter for integer objects. Uses {@code Integer.valueOf(value)} if no
 * format is specified and {@link NumberFormat#parse(String)} otherwise.
 * 
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 7 $
 */
public class IntegerConverter extends AbstractConverter<Integer>
{
    /**
     * Converts the specified value to integer.
     * 
     * @param value
     *            The string to be converted. May be <code>null</code>.
     * @param format
     *            Must be a valid number format or {@code null}
     * @return {@code null} if the value is {@code null}, empty or in the wrong
     *         format, otherwise the converted integer
     * @see name.pehl.piriti.client.converter.Converter#convert(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public Integer convert(@Nullable String value, @Nullable String format)
    {
        if (isValid(value))
        {
            try
            {
                if (format == null)
                {
                    return Integer.valueOf(value);
                }
                else
                {
                    return parseInteger(value, format);
                }
            }
            catch (NumberFormatException e)
            {
                return null;
            }
        }
        return null;
    }


    /**
     * Parsing happens in an extra method so it can be overwritten in unit
     * tests.
     * 
     * @param value
     * @param format
     * @return
     */
    protected Integer parseInteger(String value, String format)
    {
        NumberFormat numberFormat = NumberFormat.getFormat(format);
        return new Integer((int) numberFormat.parse(value));
    }


    @Override
    public String serialize(Integer value, String format)
    {
        if (value != null && format != null)
        {
            return serializeInteger(value, format);
        }
        return super.serialize(value, format);
    }


    /**
     * Serialization happens in an extra method so it can be overwritten in unit
     * tests.
     * 
     * @param value
     * @param format
     */
    protected String serializeInteger(Integer value, String format)
    {
        NumberFormat numberFormat = NumberFormat.getFormat(format);
        return numberFormat.format(value);
    }
}
