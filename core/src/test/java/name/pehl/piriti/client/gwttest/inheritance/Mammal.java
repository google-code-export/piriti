package name.pehl.piriti.client.gwttest.inheritance;

import name.pehl.piriti.client.json.Json;
import name.pehl.piriti.client.xml.Xml;

/**
 * @author $Author$
 * @version $Date$ $Revision$
 */
// @formatter:off
public abstract class Mammal extends Animal
{
    @Json @Xml Gender gender;
}
