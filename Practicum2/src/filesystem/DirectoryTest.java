package filesystem;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
    Dir dir;
    Dir dir2;
    ArrayList<DiskItem> listOfItems;

    @Before
    public void setUpFixture() {
        file1 = new File("text", FileType.TXT);
        file2 = new File("people", FileType.PDF);
        file3 = new File("racehorse", FileType.JAVA);
        file4 = new File("apple", FileType.TXT);
        file5 = new File("happyappy", FileType.TXT);
        dir2 = new Dir(null, "wowzers");
        listOfItems = new ArrayList<>();
        listOfItems.add(file1);
        listOfItems.add(file2);
        listOfItems.add(file3);
        listOfItems.add(file4);
        listOfItems.add(file5);
        listOfItems.add(dir2);
        dir = new Dir("yoopie");
        dir.addItem(file1);
        dir.addItem(file2);
        dir.addItem(file3);
        dir.addItem(file4);
        dir.addItem(file5);
        dir.addItem(dir2);
    }

    @Test
    public void testDirDiskItemsOrganize() {
        dir.organiseDiskItems();
        assertNotSame(listOfItems, dir.getDiskItems());
    }
}
