package name.pehl.piriti.client.fat.xml;

import name.pehl.piriti.client.fat.FatGlobalItem;
import name.pehl.piriti.client.fat.FatGlobalItemTestCase;

import com.google.gwt.xml.client.Document;

/**
 * @author $Author: harald.pehl $
 * @version $Revision: 133 $
 */
public class GwtTestXmlFatGlobalItemReader extends FatGlobalItemTestCase
{
    public void testRead()
    {
        Document document = XmlFatGlobalItemFactory.createDocument();
        FatGlobalItem model = FatGlobalItem.XML.read(document);
        assertFatGlobalItem(model);
    }
}
