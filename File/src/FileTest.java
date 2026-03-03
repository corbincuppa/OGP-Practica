import org.junit.jupiter.api.*;

import static java.lang.IO.print;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A JUnit 5 test class for testing the public methods of the File Class.
 *
 * @author	Adelina Vozianu
 * @author	Boglarka Csorba-Vitus
 * @author	Lander Werbrouck
 *
 * @version 1.0
 *
 */
public class FileTest {

    File myLittleFile;
    File fileWithSupportedCharacters;
    File fileWithUnsupportedCharacters;
    File fileWithVERYLargeSize;
    File fileWithNormalSize;
    File fileWithSizeZero;
    File fileNotWritable;


    @BeforeEach
    public void setupFixture() {
        fileWithSupportedCharacters = new File("AaBc._DeF-Gh", 10, true);
        fileWithUnsupportedCharacters = new File(".;]#.#'unsupported", 10, true);
        fileWithVERYLargeSize = new File("LARGE", 2147483647, true);
        fileWithNormalSize = new File("Normal_size", 30, true);
        fileWithSizeZero = new File("size.zero", 0, true);
        fileNotWritable = new File("not_writable", 39, false);
    }

    @Test
    public void testConstructorStringInt_LegalCase() {
        myLittleFile = new File(".;]#.#'unsupported", 10, true);
        assertTrue(myLittleFile.isValidName(myLittleFile.getName()));
        assertFalse(myLittleFile.getName() ==  ".___.__unsupported");
        assertFalse(myLittleFile.getName() == ".___.__Unsupported");
    }

    @Test
    public void testCanHaveAsName() {
        // True because unsupported characters get changed for underscores at creation with constructor.
        assertTrue(fileWithUnsupportedCharacters.isValidName(fileWithUnsupportedCharacters.getName()));
    }

    @Test
    public void testIsValidName() {
        assertTrue(fileWithSupportedCharacters.isValidName(fileWithSupportedCharacters.getName()));
    }


    @Test
    public void testRenameNonWritable() {
        fileNotWritable.rename("Should.Not.Change");
        assertNotSame("Should.Not.Change", fileNotWritable.getName());
    }

    @Test
    public void testRenameException() {
        try {
            fileNotWritable.rename("Should.Not.Change");
        }catch(NoWritingPermission e) {
            System.out.println("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testeEnlargeNegativeAmount() {
        fileWithNormalSize.enlarge(-10);
        assertNotEquals(20, fileWithNormalSize.getSize());
        print(fileWithNormalSize.getSize());
    }

    @Test
    public void canHaveAsSize() {
        myLittleFile = new File("Largest_file", 2147483647, true);
        assertEquals(2147483647, myLittleFile.getSize());
    }

    @Test
    public void testEnlargeHUGESize() {
        fileWithVERYLargeSize.enlarge(1);
        //assertNotEquals(2147483648, fileWithVERYLargeSize.getSize());
        //assertThrows("java: integer number too large", fileWithVERYLargeSize.getSize());
    }

    @Test
    public void testeShortenNegativeAmount() {
        fileWithNormalSize.shorten(-10);
        assertNotEquals(40, fileWithNormalSize.getSize());
        print(fileWithNormalSize.getSize());
    }

    @Test
    public void testEnlargeNonWritable() {
        fileNotWritable.enlarge(10);
        assertNotEquals(49, fileNotWritable.getSize());
        print(fileNotWritable.getSize());
    }

    @Test
    public void testShortenNonWritable() {
        fileNotWritable.shorten(10);
        assertNotEquals(29, fileNotWritable.getSize());
        print(fileNotWritable.getSize());
    }
}

