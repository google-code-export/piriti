package name.pehl.piriti.client.events;

/**
 * @author $Author: harald.pehl $
 * @version $Revision: 133 $
 */
public class XmlPersonsTest extends AbstractPersonsTest
{
    // ------------------------------------------------------------- read tests

    public void testRead()
    {
        CountHandler handler = new CountHandler();
        Person.XML_READER.addModelReadHandler(handler);

        String xml = PersonResources.INSTANCE.personsXml().getText();
        Person.XML_READER.readList(xml);
        assertEquals(PersonResources.COUNT, handler.readCount);
    }


    // ------------------------------------------------------------ write tests

    public void _testWrite()
    {
        CountHandler handler = new CountHandler();
        Person.XML_WRITER.addModelWriteHandler(handler);

        Person.XML_WRITER.toXml(persons());
        assertEquals(PersonResources.COUNT, handler.writeCount);
    }
}
