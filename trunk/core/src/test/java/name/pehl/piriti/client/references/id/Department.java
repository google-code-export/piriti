package name.pehl.piriti.client.references.id;

import name.pehl.piriti.commons.client.Id;
import name.pehl.piriti.commons.client.IdRef;
import name.pehl.piriti.commons.client.Path;
import name.pehl.piriti.xml.client.XmlReader;

import com.google.gwt.core.client.GWT;

/**
 * @author $Author$
 * @version $Date$ $Revision: 421
 *          $
 */
// @formatter:off
public class Department
{
    // ---------------------------------------------------- xml reader / writer

    public interface DepartmentXmlReader extends XmlReader<Department> {}
    public static final DepartmentXmlReader XML = GWT.create(DepartmentXmlReader.class);

    // ------------------------------------------------------------------- data

    @Id @Path("@id") String id;
    String name;
    @IdRef @Path("employees/@members") Employee[] employees;


    // --------------------------------------------------------- public methods

    @Override
    public String toString()
    {
        return "Department [" + id + "]";
    }
}
