package filesystem;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.*;

/**
 * A JUnit 4 test class for testing the public methods of the Directory Class
 * @author Tommy Messelis
 *
 */
public class DirectoryTest {

    File file1;
    File file2;
    File file3;
    File file4;
    File file5;
    Directory dir;
    Directory dir2;
    ArrayList<DiskItem> listOfItems;

    @Before
    public void setUpFixture() {
        file1 = new File("text", Extension.TXT);
        file2 = new File("people", Extension.PDF);
        file3 = new File("racehorse", Extension.JAVA);
        file4 = new File("apple", Extension.TXT);
        file5 = new File("happyappy", Extension.TXT);
        dir2 = new Directory("wowzers", null);
        listOfItems = new ArrayList<>();
        listOfItems.add(file1);
        listOfItems.add(file2);
        listOfItems.add(file3);
        listOfItems.add(file4);
        listOfItems.add(file5);
        listOfItems.add(dir2);
        dir = new Directory("yoopie", listOfItems);
    }

    @Test
    public void testDirDiskItemsOrganize() {
        dir.organiseDiskItems();
        assertNotSame(listOfItems, dir.getDiskItems());
    }
}
