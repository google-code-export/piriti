package name.pehl.piriti.client.gwttest.simplejsonpath;

import name.pehl.piriti.client.json.Json;
import name.pehl.piriti.client.json.JsonReader;

import com.google.gwt.core.client.GWT;

/**
 * @author $LastChangedBy:$
 * @version $LastChangedRevision:$
 */
// @formatter:off
public class Person
{
    interface PersonReader extends JsonReader<Person> {}
    public static final PersonReader READER = GWT.create(PersonReader.class);
    
    @Json String name;
    @Json("@.address.street") String street;
    @Json("@.address.city") String city;
}
