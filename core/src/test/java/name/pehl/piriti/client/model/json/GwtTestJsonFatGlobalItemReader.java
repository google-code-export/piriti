package name.pehl.piriti.client.model.json;

import name.pehl.piriti.client.model.FatGlobalItem;
import name.pehl.piriti.client.model.FatGlobalItemTestCase;

/**
 * @author $Author$
 * @version $Revision$
 */
public class GwtTestJsonFatGlobalItemReader extends FatGlobalItemTestCase
{
    public void testRead()
    {
        String json = JsonFatGlobalItemFactory.createJson();
        FatGlobalItem fgi = FatGlobalItem.JSON.read(json);
        assertFatGlobalItem(fgi);
    }
}
