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
 * @note 	Set up a test environment with a number of variables to be used throughout the tests.
 * 			Make a separate testcase for each test instance. Sometimes, several testcases can be combined
 * 			in one method, but this makes it harder to test them independently from the other cases, and
 * 			can make it harder to find the exact problem.
 * 			As a general rule of thumb, you test only 1 exceptional condition for 1 aspect per case.
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
    File fileWithNoParam;


    @BeforeEach
    public void setupFixture() {
        // this code gets run before each testcase is run.
        // We can thus be sure of the capacity and contents of the following tanks during testing.
        //
        // We make different oil tanks for testing: empty ones, full ones, and a couple of half-full ones:
        fileWithSupportedCharacters = new File("AaBc._DeF-Gh", 10, true);
        fileWithUnsupportedCharacters = new File(".;]#.#'unsupported", 10, true);
        fileWithVERYLargeSize = new File("LARGE", 2147483647, true);
        fileWithNormalSize = new File("Normal_size", 30, true);
        fileWithSizeZero = new File("size.zero", 0, true);
        fileNotWritable = new File("not_writable", 39, false);
        //fileWithNoParam = new File();

    }

    @Test
    public void testConstructorStringInt_LegalCase() {
        myLittleFile = new File(".;]#.#'unsupported", 10, true);
        assertTrue(myLittleFile.isValidName(myLittleFile.getName()));
        assertEquals(myLittleFile.getName(), ".___.__unsupported");
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
        assertFalse("Should.Not.Change" == fileNotWritable.getName());
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

