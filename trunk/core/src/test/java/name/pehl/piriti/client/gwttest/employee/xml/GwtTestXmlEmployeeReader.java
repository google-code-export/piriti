package name.pehl.piriti.client.gwttest.employee.xml;

import java.util.List;

import name.pehl.piriti.client.gwttest.employee.Employee;
import name.pehl.piriti.client.gwttest.employee.EmployeeTestCase;

import com.google.gwt.xml.client.Document;

/**
 * @author $Author$
 * @version $Revision$
 */
public class GwtTestXmlEmployeeReader extends EmployeeTestCase
{
    public void testRead()
    {
        Document document = XmlEmployeeFactory.createEmployees();
        List<Employee> employees = Employee.XML.readList(document);
        assertEmployees(employees);
    }
}
