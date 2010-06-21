package name.pehl.piriti.gxt.client.gwttest.book.xml;

import java.util.List;

import name.pehl.piriti.client.gwttest.book.xml.XmlBookFactory;
import name.pehl.piriti.gxt.client.gwttest.book.Book;
import name.pehl.piriti.gxt.client.gwttest.book.AbstractBookReaderTest;

import com.google.gwt.xml.client.Document;

/**
 * @author $Author$
 * @version $Revision$
 */
public class XmlBookReaderTest extends AbstractBookReaderTest
{
    public void testRead()
    {
        Document document = XmlBookFactory.createBook();
        Book book = Book.XML.read(document);
        assertBook(book, true, true);
    }


    public void testReadList()
    {
        Document document = XmlBookFactory.createBooks();
        List<Book> books = Book.XML.readList(document);
        assertBooks(books, true, true);
    }


    public void testReadListWithXpath()
    {
        Document document = XmlBookFactory.createBooks();
        List<Book> books = Book.XML.readList(document, "/books/book");
        assertBooks(books, true, true);
    }


    public void testReadListWithWrongXpath()
    {
        Document document = XmlBookFactory.createBooks();
        List<Book> books = Book.XML.readList(document, "//moo");
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }
}
