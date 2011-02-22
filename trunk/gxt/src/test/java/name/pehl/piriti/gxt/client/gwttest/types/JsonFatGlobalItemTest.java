package name.pehl.piriti.gxt.client.gwttest.types;

import name.pehl.piriti.client.gwttest.types.FatGlobalItemResources;

/**
 * @author $Author: harald.pehl $
 * @version $Revision: 237 $
 */
public class JsonFatGlobalItemTest extends AbstractFatGlobalItemTest
{
    // ------------------------------------------------------------- read tests

    public void testRead()
    {
        String json = FatGlobalItemResources.INSTANCE.fatGlobalItemJson().getText();
        FatGlobalItem fgi = FatGlobalItem.JSON_READER.read(json);
        assertFatGlobalItem(fgi, true);
    }

    // ------------------------------------------------------------ write tests

    // NYI
}