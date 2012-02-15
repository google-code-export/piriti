package name.pehl.piriti.client.events;

import name.pehl.piriti.commons.client.ModelReadEvent;
import name.pehl.piriti.commons.client.ModelReadHandler;
import name.pehl.piriti.commons.client.ModelWriteEvent;
import name.pehl.piriti.commons.client.ModelWriteHandler;

public class CountHandler<C> implements ModelReadHandler<Person, C>, ModelWriteHandler<Person>
{
    int readCount = 0;
    int writeCount = 0;


    @Override
    public void onModelRead(ModelReadEvent<Person, C> event)
    {
        readCount++;
    }


    @Override
    public void onModelWrite(ModelWriteEvent<Person> event)
    {
        writeCount++;
    }
}
