package name.pehl.piriti.client.polymorph;

import name.pehl.piriti.json.client.JsonInstanceCreator;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class MediumCreator extends JsonInstanceCreator<Medium>
{
    @Override
    public Medium newInstance(JSONObject context)
    {
        Medium medium = null;
        JSONValue idValue = context.get("id");
        if (idValue != null)
        {
            JSONString idString = idValue.isString();
            if (idString != null)
            {
                String id = idString.stringValue();
                if (id.startsWith("isbn-"))
                {
                    medium = new Book();
                }
                else if (id.startsWith("cd-"))
                {
                    medium = new Cd();
                }
                if (id.startsWith("dvd-"))
                {
                    medium = new Dvd();
                }
            }
        }
        return medium;
    }
}
