package name.pehl.piriti.converter.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Time;

import org.junit.Before;
import org.junit.Test;

/**
 * @author $LastChangedBy:$
 * @version $LastChangedRevision:$
 */

public class TimeConverterTest
{
    private TestableTimeConverter underTest;


    @Before
    public void setUp() throws Exception
    {
        underTest = new TestableTimeConverter();
    }


    @Test
    public void testConvertNull()
    {
        Time result = underTest.convert(null);
        assertNull(result);
    }


    @Test
    public void testConvertEmpty()
    {
        Time result = underTest.convert("");
        assertNull(result);
    }


    @Test
    public void testConvertBlank()
    {
        Time result = underTest.convert("    ");
        assertNull(result);
    }


    @Test
    public void testConvertFoo()
    {
        Time result = underTest.convert("foo");
        assertNull(result);
    }


    @Test
    public void testConvertTime()
    {
        String value = "11:22:33";
        Time result = underTest.convert(value);
        assertEquals(Time.valueOf(value), result);
    }
}
