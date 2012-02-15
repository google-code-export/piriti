package name.pehl.piriti;

import junit.framework.Test;
import junit.framework.TestSuite;
import name.pehl.piriti.client.constraints.JsonAmoebaReaderTest;
import name.pehl.piriti.client.constraints.JsonAmoebaWriterTest;
import name.pehl.piriti.client.constraints.XmlAmoebaReaderTest;
import name.pehl.piriti.client.constraints.XmlAmoebaWriterTest;
import name.pehl.piriti.client.converter.JsonBackAndForthTest;
import name.pehl.piriti.client.converter.XmlBackAndForthTest;
import name.pehl.piriti.client.escape.JsonRefugeeTest;
import name.pehl.piriti.client.escape.XmlRefugeeTest;
import name.pehl.piriti.client.events.JsonPersonsTest;
import name.pehl.piriti.client.events.XmlPersonsTest;
import name.pehl.piriti.client.external.JsonShopTest;
import name.pehl.piriti.client.external.XmlShopTest;
import name.pehl.piriti.client.inheritance.JsonAnimalTest;
import name.pehl.piriti.client.inheritance.XmlAnimalTest;
import name.pehl.piriti.client.namespaces.XmlLotteryTicketTest;
import name.pehl.piriti.client.native_.JsonResponseTest;
import name.pehl.piriti.client.native_.XmlResponseTest;
import name.pehl.piriti.client.polymorph.JsonLibraryTest;
import name.pehl.piriti.client.polymorph.XmlLibraryTest;
import name.pehl.piriti.client.references.JsonBookTest;
import name.pehl.piriti.client.references.XmlBookTest;
import name.pehl.piriti.client.references.id.XmlCompanyTest;
import name.pehl.piriti.client.types.JsonAnythingTest;
import name.pehl.piriti.client.types.JsonArraysTest;
import name.pehl.piriti.client.types.JsonBooleansTest;
import name.pehl.piriti.client.types.JsonDatesTest;
import name.pehl.piriti.client.types.JsonFatGlobalItemTest;
import name.pehl.piriti.client.types.JsonNullsTest;
import name.pehl.piriti.client.types.XmlAnythingTest;
import name.pehl.piriti.client.types.XmlArraysTest;
import name.pehl.piriti.client.types.XmlBooleansTest;
import name.pehl.piriti.client.types.XmlDatesTest;
import name.pehl.piriti.client.types.XmlFatGlobalItemTest;
import name.pehl.piriti.client.types.XmlNullsTest;

import com.google.gwt.junit.tools.GWTTestSuite;

/**
 * @author $Author$
 * @version $Date$ $Revision: 597
 *          $
 */
public class PiritiDevGwtTestSuite extends GWTTestSuite
{
    public static Test suite()
    {
        TestSuite suite = new TestSuite("GWT tests for piriti-core");

        suite.addTestSuite(JsonAmoebaReaderTest.class);
        suite.addTestSuite(JsonAmoebaWriterTest.class);
        suite.addTestSuite(JsonAnimalTest.class);
        suite.addTestSuite(JsonAnythingTest.class);
        suite.addTestSuite(JsonArraysTest.class);
        suite.addTestSuite(JsonBackAndForthTest.class);
        suite.addTestSuite(JsonBookTest.class);
        suite.addTestSuite(JsonBooleansTest.class);
        suite.addTestSuite(JsonDatesTest.class);
        suite.addTestSuite(JsonFatGlobalItemTest.class);
        suite.addTestSuite(JsonLibraryTest.class);
        suite.addTestSuite(JsonNullsTest.class);
        suite.addTestSuite(JsonPersonsTest.class);
        suite.addTestSuite(JsonRefugeeTest.class);
        suite.addTestSuite(JsonResponseTest.class);
        suite.addTestSuite(JsonShopTest.class);
        suite.addTestSuite(XmlAmoebaReaderTest.class);
        suite.addTestSuite(XmlAmoebaWriterTest.class);
        suite.addTestSuite(XmlAnimalTest.class);
        suite.addTestSuite(XmlAnythingTest.class);
        suite.addTestSuite(XmlArraysTest.class);
        suite.addTestSuite(XmlBackAndForthTest.class);
        suite.addTestSuite(XmlBookTest.class);
        suite.addTestSuite(XmlBooleansTest.class);
        suite.addTestSuite(XmlCompanyTest.class);
        suite.addTestSuite(XmlDatesTest.class);
        suite.addTestSuite(XmlFatGlobalItemTest.class);
        suite.addTestSuite(XmlLibraryTest.class);
        suite.addTestSuite(XmlNullsTest.class);
        suite.addTestSuite(XmlLotteryTicketTest.class);
        suite.addTestSuite(XmlPersonsTest.class);
        suite.addTestSuite(XmlRefugeeTest.class);
        suite.addTestSuite(XmlResponseTest.class);
        suite.addTestSuite(XmlShopTest.class);

        return suite;
    }
}
