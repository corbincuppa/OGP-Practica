package filesystem;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import filesystem.DiskItemNotWritableException;
import filesystem.FileType;
import filesystem.File;
import org.junit.*;

/**
 * A JUnit 4 test class for testing the public methods of the filesystem package.
 * @author  Adelina Vozianu
 * @author  Boglárka Csorba-Vitus
 * @author  Lander Werbrouck
 *
 */
public class FileSystemTest {
    // Initiation of Files.
    File fileStringIntBoolean;
    File fileString;
    Date timeBeforeConstruction, timeAfterConstruction;

    File fileNotWritable;
    Date timeBeforeConstructionNotWritable, timeAfterConstructionNotWritable;

    // Initiation of Directories.
    Directory root;
    Directory dirDirStringBoolean;
    Directory dirDirString;
    Directory dirStringBoolean;
    Directory dirString;
    Directory dirModifiedName;
    Directory getTotalSize;
    File      filler1;
    File      filler2;
    File      filler3;
    File      filler4;
    File      filler5;
    File      filler6;
    File      filler7;
    File      filler8;
    Directory alsoFiller;

    // Initiation of Links.

    @Before
    public void setUpFixture(){
        // Files
        timeBeforeConstruction = new Date();
        fileStringIntBoolean = new File(null, "bestand",100, true);
        fileString = new File("bestand", FileType.TXT);
        timeAfterConstruction = new Date();

        timeBeforeConstructionNotWritable = new Date();
        fileNotWritable = new File(null, "bestand",100,false, FileType.TXT);
        timeAfterConstructionNotWritable = new Date();

        // Directories
        root = new Directory("root");
        //root.makeRoot();
        dirDirStringBoolean = new Directory(root, "directory", true);
        dirDirString = new Directory(root, "x-ray");
        dirStringBoolean = new Directory("apple-apple", true);
        dirString = new Directory("Romeo");
        dirModifiedName = new Directory("Name_Before");
        getTotalSize = new Directory("the_one");
        filler1 = new File(getTotalSize, "a", 100, true);
        filler2 = new File(getTotalSize, "b", 100, true);
        filler3 = new File(getTotalSize, "c", 100, true);
        filler4 = new File(getTotalSize, "d", 100, true);
        filler5 = new File(getTotalSize, "e", 100, true);
        filler6 = new File(getTotalSize, "f", 100, true);
        filler7 = new File(getTotalSize, "g", 100, true);
        filler8 = new File(getTotalSize, "h", 100, true);
        alsoFiller = new Directory(getTotalSize, "i");


        // Links
    }

    /**********************************************************
     * File tests
     **********************************************************/
    @Test
    public void testFileStringIntBoolean_LegalCase() {
        assertEquals("bestand",fileStringIntBoolean.getName());
        assertEquals(fileStringIntBoolean.getSize(),100);
        assertTrue(fileStringIntBoolean.isWritable());
        assertNull(fileStringIntBoolean.getModificationTime());
        assertFalse(timeBeforeConstruction.after(fileStringIntBoolean.getCreationTime()));
        assertFalse(fileStringIntBoolean.getCreationTime().after(timeAfterConstruction));
    }

    @Test
    public void testFileStringIntBoolean_IllegalCase() {
        timeBeforeConstruction = new Date();
        fileStringIntBoolean = new File(null, "$IllegalName$",File.getMaximumSize(),false);
        timeAfterConstruction = new Date();
        assertTrue(File.isValidName(fileStringIntBoolean.getName()));
        assertEquals(File.getMaximumSize(),fileStringIntBoolean.getSize());
        assertFalse(fileStringIntBoolean.isWritable());
        assertNull(fileStringIntBoolean.getModificationTime());
        assertFalse(timeBeforeConstruction.after(fileStringIntBoolean.getCreationTime()));
        assertFalse(fileStringIntBoolean.getCreationTime().after(timeAfterConstruction));
    }

    @Test
    public void testFileString_LegalCase() {
        assertEquals("bestand",fileString.getName());
        assertEquals(0,fileString.getSize());
        assertTrue(fileString.isWritable());
        assertNull(fileString.getModificationTime());
        assertFalse(timeBeforeConstruction.after(fileString.getCreationTime()));
        assertFalse(fileString.getCreationTime().after(timeAfterConstruction));
    }

    @Test
    public void testFileString_IllegalCase() {
        timeBeforeConstruction = new Date();
        fileString = new File("$IllegalName$", FileType.TXT);
        timeAfterConstruction = new Date();
        assertTrue(File.isValidName(fileString.getName()));
        assertEquals(0,fileString.getSize());
        assertTrue(fileString.isWritable());
        assertNull(fileString.getModificationTime());
        assertFalse(timeBeforeConstruction.after(fileString.getCreationTime()));
        assertFalse(fileString.getCreationTime().after(timeAfterConstruction));
    }

    @Test
    public void testIsValidName_LegalCase() {
        assertTrue(File.isValidName("abcDEF123-_."));
    }

    @Test
    public void testIsValidName_IllegalCase() {
        assertFalse(File.isValidName(null));
        assertFalse(File.isValidName(""));
        assertFalse(File.isValidName("%illegalSymbol"));

    }

    @Test
    public void testChangeName_LegalCase() {
        Date timeBeforeSetName = new Date();
        fileString.changeName("NewLegalName");
        Date timeAfterSetName = new Date();

        assertEquals("NewLegalName",fileString.getName());
        assertNotNull(fileString.getModificationTime());
        assertFalse(fileString.getModificationTime().before(timeBeforeSetName));
        assertFalse(timeAfterSetName.before(fileString.getModificationTime()));
    }

    @Test (expected = DiskItemNotWritableException.class)
    public void testChangeName_DiskItemNotWritable() {
        fileNotWritable.changeName("NewLegalName");
    }

    @Test
    public void testChangeName_IllegalName() {
        fileString.changeName("$IllegalName$");
        assertEquals("bestand",fileString.getName());
        assertNull(fileString.getModificationTime());
    }

    @Test
    public void testIsValidSize_LegalCase() {
        assertTrue(File.isValidSize(0));
        assertTrue(File.isValidSize(File.getMaximumSize()/2));
        assertTrue(File.isValidSize(File.getMaximumSize()));
    }

    @Test
    public void testIsValidSize_IllegalCase() {
        assertFalse(File.isValidSize(-1));
        if (File.getMaximumSize() < Integer.MAX_VALUE) {
            assertFalse(File.isValidSize(File.getMaximumSize()+1));
        }
    }

    @Test
    public void testEnlarge_LegalCase() {
        File file = new File(null, "bestand.txt",File.getMaximumSize()-1,true);
        Date timeBeforeEnlarge = new Date();
        file.enlarge(1);
        Date timeAfterEnlarge = new Date();
        assertEquals(file.getSize(),File.getMaximumSize());
        assertNotNull(file.getModificationTime());
        assertFalse(file.getModificationTime().before(timeBeforeEnlarge));
        assertFalse(timeAfterEnlarge.before(file.getModificationTime()));
    }

    @Test (expected = DiskItemNotWritableException.class)
    public void testEnlarge_DiskItemNotWritable() {
        fileNotWritable.enlarge(1);
    }

    @Test
    public void testShorten_LegalCase() {
        fileStringIntBoolean.shorten(1);
        Date timeAfterShorten = new Date();
        assertEquals(fileStringIntBoolean.getSize(),99);
        assertNotNull(fileStringIntBoolean.getModificationTime());
        assertFalse(fileStringIntBoolean.getModificationTime().before(timeAfterConstruction));
        assertFalse(timeAfterShorten.before(fileStringIntBoolean.getModificationTime()));
    }

    @Test (expected = DiskItemNotWritableException.class)
    public void testShorten_DiskItemNotWritable() {
        fileNotWritable.shorten(1);
    }

    @Test
    public void testIsValidCreationTime_LegalCase() {
        Date now = new Date();
        assertTrue(File.isValidCreationTime(now));
    }

    @Test
    public void testIsValidCreationTime_IllegalCase() {
        assertFalse(File.isValidCreationTime(null));
        Date inFuture = new Date(System.currentTimeMillis() + 1000*60*60);
        assertFalse(File.isValidCreationTime(inFuture));
    }

    @Test
    public void testcanHaveAsModificationTime_LegalCase() {
        assertTrue(fileString.canHaveAsModificationTime(null));
        assertTrue(fileString.canHaveAsModificationTime(new Date()));
    }

    @Test
    public void testcanHaveAsModificationTime_IllegalCase() {
        assertFalse(fileString.canHaveAsModificationTime(new Date(timeAfterConstruction.getTime() - 1000*60*60)));
        assertFalse(fileString.canHaveAsModificationTime(new Date(System.currentTimeMillis() + 1000*60*60)));
    }

    @Test
    public void testHasOverlappingUsePeriod_UnmodifiedFiles() {
        // one = implicit argument ; other = explicit argument
        File one = new File("one");
        sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
        File other = new File("other");

        //1 Test unmodified case
        assertFalse(one.hasOverlappingUsePeriod(other));

        //2 Test one unmodified case
        other.enlarge(File.getMaximumSize());
        assertFalse(one.hasOverlappingUsePeriod(other));

        //3 Test other unmodified case
        //so re-initialise the other file
        other = new File("other");
        one.enlarge(File.getMaximumSize());
        assertFalse(one.hasOverlappingUsePeriod(other));

    }

    @Test
    public void testHasOverlappingUsePeriod_ModifiedNoOverlap() {
        // one = implicit argument ; other = explicit argument
        File one, other;
        one = new File("one");
        sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
        other = new File("other");

        //1 Test one created and modified before other created and modified case
        one.enlarge(File.getMaximumSize());
        sleep();
        //re-initialise the other
        other = new File("other");
        other.enlarge(File.getMaximumSize());
        assertFalse(one.hasOverlappingUsePeriod(other));

        //2 Test other created and modified before one created and modified
        other.enlarge(File.getMaximumSize());
        sleep();
        one = new File("one");
        one.enlarge(File.getMaximumSize());
        assertFalse(one.hasOverlappingUsePeriod(other));

    }

    @Test
    public void testHasOverlappingUsePeriod_ModifiedOverlap_A() {
        // one = implicit argument ; other = explicit argument
        //A Test one created before other created before one modified before other modified
        File one, other;
        one = new File("one");
        sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
        other = new File("other");

        one.enlarge(File.getMaximumSize());
        sleep();
        other.enlarge(File.getMaximumSize());
        assertTrue(one.hasOverlappingUsePeriod(other));
    }

    @Test
    public void testHasOverlappingUsePeriod_ModifiedOverlap_B() {
        // one = implicit argument ; other = explicit argument
        //B Test one created before other created before other modified before one modified
        File one, other;
        one = new File("one");
        sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
        other = new File("other");

        other.enlarge(File.getMaximumSize());
        sleep();
        one.enlarge(File.getMaximumSize());
        assertTrue(one.hasOverlappingUsePeriod(other));
    }

    @Test
    public void testHasOverlappingUsePeriod_ModifiedOverlap_C() {
        // one = implicit argument ; other = explicit argument
        //C Test other created before one created before other modified before one modified
        File one, other;
        other = new File("other");
        sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
        one = new File("one");

        other.enlarge(File.getMaximumSize());
        sleep();
        one.enlarge(File.getMaximumSize());
        assertTrue(one.hasOverlappingUsePeriod(other));
    }

    @Test
    public void testHasOverlappingUsePeriod_ModifiedOverlap_D() {
        // one = implicit argument ; other = explicit argument
        //D Test other created before one created before one modified before other modified
        File one, other;
        other = new File("one");
        sleep(); // sleep() to be sure that one.getCreationTime() != other.getCreationTime()
        one = new File("other");

        one.enlarge(File.getMaximumSize());
        sleep();
        other.enlarge(File.getMaximumSize());
        assertTrue(one.hasOverlappingUsePeriod(other));
    }

    @Test
    public void testSetWritable() {
        fileString.setWritable(false);
        fileNotWritable.setWritable(true);
        assertFalse(fileString.isWritable());
        assertTrue(fileNotWritable.isWritable());
    }

    @Test
    public void testSetExtension() {
        File fileWithExtension = new File(null, "he",242, true, FileType.JAVA);
        assertSame(fileWithExtension.getFileType().getExtension(), ".java");
    }



    /**********************************************************
     * Directory tests
     **********************************************************/
    // constructors
    @Test
    public void testDirStringBoolean_LegalCase() {
        assertEquals(root, dirDirStringBoolean.getParent());
        assertTrue(root.hasAsItem(dirDirStringBoolean));
        assertEquals("directory", dirDirStringBoolean.getName());
        assertTrue(dirDirStringBoolean.isWritable());
        assertEquals(new ArrayList<PrimitiveDiskItem>(), dirDirStringBoolean.getDiskItems());
    }

    @Test
    public void testDirStringBoolean_IllegalCase() {

    }

    @Test
    public void testDirString_LegalCase() {
        assertEquals(root, dirDirString.getParent());
        assertTrue(root.hasAsItem(dirDirString));
        assertEquals("x-ray", dirDirString.getName());
        assertTrue(dirDirString.isWritable());
        assertEquals(new ArrayList<PrimitiveDiskItem>(), dirDirString.getDiskItems());
    }

    @Test
    public void testDirString_IllegalCase() {

    }

    @Test
    public void testStringBoolean_LegalCase() {
        assertNull(dirStringBoolean.getParent());
        assertTrue(dirStringBoolean.isRoot());
        assertEquals("apple-apple", dirStringBoolean.getName());
        assertTrue(dirStringBoolean.isWritable());
        assertEquals(new ArrayList<PrimitiveDiskItem>(), dirStringBoolean.getDiskItems());
    }

    @Test
    public void testStringBoolean_IllegalCase() {

    }

    @Test
    public void testString_LegalCase() {
        assertNull(dirString.getParent());
        assertTrue(dirString.isRoot());
        assertEquals("Romeo", dirString.getName());
        assertTrue(dirString.isWritable());
        assertEquals(new ArrayList<PrimitiveDiskItem>(), dirString.getDiskItems());
    }

    @Test
    public void testString_IllegalCase() {

    }

    @Test
    public void testChangeNameResort_LegalCase() {
        ArrayList<PrimitiveDiskItem> listBefore = root.getDiskItems();
        root.getDiskItems().add(dirDirString);
        root.getDiskItems().add(dirDirStringBoolean);
        dirDirString.changeName("abomination");
        assertNotEquals(Arrays.asList(dirDirStringBoolean, dirDirString), root.getDiskItems());
    }

    @Test
    public void testGetSizeDir() {
        assertEquals(Arrays.asList(filler1, filler2, filler3, filler4, filler5, filler6, filler7, filler8, alsoFiller), getTotalSize.getDiskItems());
        assertEquals(800, getTotalSize.getSize());
    }

    @Test
    public void testGetNbItems() {
        assertEquals(9, getTotalSize.getNbItems());
    }

    @Test
    public void testGetItemAt() {
        assertEquals(filler1, getTotalSize.getItemAt(1));
    }

    @Test
    public void testGetItem() {
        assertEquals(filler1, getTotalSize.getItem("a"));
    }

    @Test
    public void testContainsDiskItemWithName() {
        assertTrue(getTotalSize.containsDiskItemWithName("g"));
    }

    @Test
    public void testGetIndex() {
        assertEquals(9, getTotalSize.getIndex(alsoFiller));
    }

    @Test
    public void testHasAsItem_LegalCase() {
        assertTrue(getTotalSize.hasAsItem(filler7));
    }

    @Test
    public void testHasAsItem_IllegalCase() {
        assertFalse(getTotalSize.hasAsItem(root));
    }



    /**********************************************************
     * Link tests
     **********************************************************/






    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
