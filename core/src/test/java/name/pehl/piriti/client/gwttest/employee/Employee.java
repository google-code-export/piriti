package name.pehl.piriti.client.gwttest.employee;

import java.util.List;

import name.pehl.piriti.client.json.JsonId;
import name.pehl.piriti.client.json.JsonIdRef;
import name.pehl.piriti.client.json.JsonReader;
import name.pehl.piriti.client.xml.XmlField;
import name.pehl.piriti.client.xml.XmlId;
import name.pehl.piriti.client.xml.XmlIdRef;
import name.pehl.piriti.client.xml.XmlReader;

import com.google.gwt.core.client.GWT;

/**
 * @author $Author$
 * @version $Date$ $Revision: 408
 *          $
 */
public class Employee
{
    public interface EmployeeJsonReader extends JsonReader<Employee>
    {
    }

    public static final EmployeeJsonReader JSON = GWT.create(EmployeeJsonReader.class);

    public interface EmployeeXmlReader extends XmlReader<Employee>
    {
    }

    public static final EmployeeXmlReader XML = GWT.create(EmployeeXmlReader.class);

    @JsonId
    @XmlId
    String id;

    @XmlField
    String name;

    @JsonIdRef
    @XmlIdRef("boss/@ref")
    Employee boss;

    @JsonIdRef
    @XmlIdRef("team/member/@ref")
    List<Employee> team;
}
