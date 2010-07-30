package name.pehl.piriti.client.gwttest.lotteryticket;

import name.pehl.totoe.client.Document;
import name.pehl.totoe.client.XmlParser;

/**
 * @author $Author$
 * @version $Date$ $Revision: 295
 *          $
 */
public class LotteryTicketTest extends AbstractLotteryTicketTest
{
    // ------------------------------------------------------------- read tests

    /**
     * Test namespace
     */
    public void testNs()
    {
        String xml = LotteryTicketResources.INSTANCE.lotteryTicketNs().getText();
        Document document = new XmlParser().parse(xml, LotteryTicketResources.NS);
        LotteryTicket lt = LotteryTicket.XML_READER.read(document);
        assertLotteryTicket(lt);
    }


    /**
     * Test nested namespace
     */
    public void testNns()
    {
        String xml = LotteryTicketResources.INSTANCE.lotteryTicketNns().getText();
        Document document = new XmlParser().parse(xml, LotteryTicketResources.NS);
        LotteryTicket lt = LotteryTicket.XML_READER.read(document);
        assertLotteryTicket(lt);
    }

    // ------------------------------------------------------------ write tests

    // NYI
}
