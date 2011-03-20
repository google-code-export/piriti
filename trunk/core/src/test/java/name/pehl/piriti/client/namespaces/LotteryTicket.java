package name.pehl.piriti.client.namespaces;

import java.util.Date;
import java.util.List;

import name.pehl.piriti.xml.client.Xml;
import name.pehl.piriti.xml.client.XmlReader;
import name.pehl.piriti.xml.client.XmlWriter;

import com.google.gwt.core.client.GWT;

/**
 * @author $LastChangedBy:$
 * @version $LastChangedRevision:$
 */
public class LotteryTicket
{
    // ---------------------------------------------------- xml reader / writer

    public interface LotteryTicketReader extends XmlReader<LotteryTicket>
    {
    }

    public static final LotteryTicketReader XML_READER = GWT.create(LotteryTicketReader.class);

    public interface LotteryTicketWriter extends XmlWriter<LotteryTicket>
    {
    }

    public static final LotteryTicketWriter XML_WRITER = GWT.create(LotteryTicketWriter.class);

    // ------------------------------------------------------------------- data

    @Xml(value = "@date", format = "dd.MM.yyyy")
    Date date;

    @Xml("foo:player")
    Player player;

    @Xml("numbers/@game")
    String game;

    @Xml("numbers/number")
    List<Integer> numbers;
}
