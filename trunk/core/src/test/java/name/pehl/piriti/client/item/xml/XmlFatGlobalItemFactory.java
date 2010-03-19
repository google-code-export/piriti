package name.pehl.piriti.client.item.xml;

import java.util.Date;

import name.pehl.piriti.client.converter.DateConverter;
import name.pehl.piriti.client.item.Amount;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

/**
 * @author $LastChangedBy: harald.pehl $
 * @version $LastChangedRevision: 135 $
 */
public final class XmlFatGlobalItemFactory
{
    private static final Date MY_BIRTHDAY = new Date(115813353000l);


    private XmlFatGlobalItemFactory()
    {
    }


    public static Document createDocument()
    {
        int size = 3;
        Document document = XMLParser.createDocument();
        Element model = document.createElement("model");

        // Primitives and simple objects
        createElementAndAppend(document, model, "booleanPrimitive", "true");
        createElementAndAppend(document, model, "booleanObject", "true");
        createElementAndAppend(document, model, "bytePrimitive", "1");
        createElementAndAppend(document, model, "byteObject", "2");
        createElementAndAppend(document, model, "characterPrimitive", "a");
        createElementAndAppend(document, model, "characterObject", "b");
        createElementAndAppend(document, model, "date", DateTimeFormat.getFormat(DateConverter.DEFAULT_FORMAT).format(
                MY_BIRTHDAY));
        createElementAndAppend(document, model, "amount", Amount.THREE.name());
        createElementAndAppend(document, model, "doublePrimitive", "4.5");
        createElementAndAppend(document, model, "doubleObject", "6.7");
        createElementAndAppend(document, model, "floatPrimitive", "8.9");
        createElementAndAppend(document, model, "floatObject", "10.11");
        createElementAndAppend(document, model, "integerPrimitive", "12");
        createElementAndAppend(document, model, "integerObject", "13");
        createElementAndAppend(document, model, "longPrimitive", "14");
        createElementAndAppend(document, model, "longObject", "15");
        createElementAndAppend(document, model, "shortPrimitive", "16");
        createElementAndAppend(document, model, "shortObject", "17");
        Element stringElement = createElementAndAppend(document, model, "string", "achtzehn");
        stringElement.setAttribute("attribute", "neunzehn");

        // SkinnyNestedItem
        model.appendChild(createNestedModel(document, "skinnyNestedItem"));

        // Arrays
        createElementsAndAppend(document, model, "arrayOfIntegerPrimitives", "0", "1", "2");
        createElementsAndAppend(document, model, "arrayOfIntegerObjects", "0", "1", "2");
        createElementsAndAppend(document, model, "arrayOfStrings", "0", "1", "2");
        Element arrayOfDemoNestedModels = document.createElement("arrayOfSkinnyNestedItems");
        for (int i = 0; i < size; i++)
        {
            arrayOfDemoNestedModels.appendChild(createNestedModel(document, "skinnyNestedItem"));
        }
        model.appendChild(arrayOfDemoNestedModels);

        // Collections
        createElementsAndAppend(document, model, "collectionOfIntegerObjects", "0", "1", "2");
        createElementsAndAppend(document, model, "collectionOfStrings", "0", "1", "2");
        Element collectionOfDemoNestedModels = document.createElement("collectionOfSkinnyNestedItems");
        for (int i = 0; i < size; i++)
        {
            collectionOfDemoNestedModels.appendChild(createNestedModel(document, "skinnyNestedItem"));
        }
        model.appendChild(collectionOfDemoNestedModels);

        // Lists
        createElementsAndAppend(document, model, "listOfIntegerObjects", "0", "1", "2");
        createElementsAndAppend(document, model, "listOfStrings", "0", "1", "2");
        Element listOfDemoNestedModels = document.createElement("listOfSkinnyNestedItems");
        for (int i = 0; i < size; i++)
        {
            listOfDemoNestedModels.appendChild(createNestedModel(document, "skinnyNestedItem"));
        }
        model.appendChild(listOfDemoNestedModels);

        // Sets
        createElementsAndAppend(document, model, "setOfIntegerObjects", "0", "1", "2");
        createElementsAndAppend(document, model, "setOfStrings", "0", "1", "2");
        Element setOfDemoNestedModels = document.createElement("setOfSkinnyNestedItems");
        for (int i = 0; i < size; i++)
        {
            setOfDemoNestedModels.appendChild(createNestedModel(document, "skinnyNestedItem"));
        }
        model.appendChild(setOfDemoNestedModels);

        document.appendChild(model);
        return document;
    }


    private static Element createNestedModel(Document document, String elementName)
    {
        Element nestedModel = document.createElement(elementName);

        createElementAndAppend(document, nestedModel, "booleanPrimitive", "true");
        createElementAndAppend(document, nestedModel, "booleanObject", "true");
        createElementAndAppend(document, nestedModel, "bytePrimitive", "1");
        createElementAndAppend(document, nestedModel, "byteObject", "2");
        createElementAndAppend(document, nestedModel, "characterPrimitive", "a");
        createElementAndAppend(document, nestedModel, "characterObject", "b");
        createElementAndAppend(document, nestedModel, "date", DateTimeFormat.getFormat(DateConverter.DEFAULT_FORMAT)
                .format(MY_BIRTHDAY));
        createElementAndAppend(document, nestedModel, "amount", Amount.THREE.name());
        createElementAndAppend(document, nestedModel, "doublePrimitive", "4.5");
        createElementAndAppend(document, nestedModel, "doubleObject", "6.7");
        createElementAndAppend(document, nestedModel, "floatPrimitive", "8.9");
        createElementAndAppend(document, nestedModel, "floatObject", "10.11");
        createElementAndAppend(document, nestedModel, "integerPrimitive", "12");
        createElementAndAppend(document, nestedModel, "integerObject", "13");
        createElementAndAppend(document, nestedModel, "longPrimitive", "14");
        createElementAndAppend(document, nestedModel, "longObject", "15");
        createElementAndAppend(document, nestedModel, "shortPrimitive", "16");
        createElementAndAppend(document, nestedModel, "shortObject", "17");
        Element stringElement = createElementAndAppend(document, nestedModel, "string", "achtzehn");
        stringElement.setAttribute("attribute", "neunzehn");

        return nestedModel;
    }


    private static void createElementsAndAppend(Document document, Element parent, String elementName, String... values)
    {
        if (values != null && values.length != 0)
        {
            for (String value : values)
            {
                createElementAndAppend(document, parent, elementName, value);
            }
        }
    }


    private static Element createElementAndAppend(Document document, Element parent, String elementName, String value)
    {
        Element element = document.createElement(elementName);
        Text text = document.createTextNode(value);
        element.appendChild(text);
        parent.appendChild(element);
        return element;
    }
}
