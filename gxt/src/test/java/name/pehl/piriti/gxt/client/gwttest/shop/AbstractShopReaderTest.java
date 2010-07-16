package name.pehl.piriti.gxt.client.gwttest.shop;

import java.util.Date;
import java.util.Set;
import java.util.SortedSet;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * @author $Author: harald.pehl $
 * @version $Date: 2010-07-16 16:19:21 +0200 (Fr, 16. Jul 2010) $ $Revision: 295
 *          $
 */
public abstract class AbstractShopReaderTest extends GWTTestCase
{
    public final static Date _22_11_2010 = new Date(1290380400000l);


    @Override
    public String getModuleName()
    {
        return "name.pehl.piriti.gxt.PiritiGxtTest";
    }


    @Override
    protected void gwtSetUp() throws Exception
    {
        System.out.println(getClass().getName() + "." + getName() + "()");
    }


    protected void assertShop(Shop shop)
    {
        assertNotNull(shop);

        // Customer
        Set<Customer> customers = shop.get("customers");
        assertNotNull(customers);
        assertEquals(1, customers.size());
        Customer customer = customers.iterator().next();
        assertGarryGourmet(customer);

        // Products
        SortedSet<Product> products = shop.get("products");
        assertNotNull(products);
        assertEquals(7, products.size());
        Product product = products.iterator().next();
        assertTyhmian(product);

        // Order
        Order order = shop.get("order");
        assertNotNull(order);
        assertEquals(_22_11_2010, order.get("date"));
        customer = order.get("customer");
        assertGarryGourmet(customer);
        SortedSet<OrderItem> items = order.get("items");
        assertNotNull(items);
        assertEquals(7, items.size());

        // OrderItems
        OrderItem item = items.iterator().next();
        assertNotNull(item);
        assertEquals(1, item.get("amount"));
        product = item.get("product");
        assertTyhmian(product);
    }


    private void assertGarryGourmet(Customer customer)
    {
        assertNotNull(customer);
        assertEquals("gg", customer.get("id"));
        assertEquals("Garry", customer.get("firstname"));
        assertEquals("Gourmet", customer.get("surname"));
    }


    private void assertTyhmian(Product product)
    {
        assertNotNull(product);
        assertEquals("tym", product.get("id"));
        assertEquals("Tyhmian", product.get("name"));
        assertEquals(.5, (Double) product.get("price"), .01);
    }
}
