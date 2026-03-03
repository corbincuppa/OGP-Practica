import org.junit.jupiter.api.*;
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
    File fileWithSizeZero;
    File fileNonWritable;
    File fileWithUnsupportedCharacters;
    File fileWithVERYLargeSize;
    File fileWithNoParam;


    @BeforeEach
    public void setupFixture() {
        // this code gets run before each testcase is run.
        // We can thus be sure of the capacity and contents of the following tanks during testing.
        //
        // We make different oil tanks for testing: empty ones, full ones, and a couple of half-full ones:
        fileWithSizeZero = new File("size.zero", 0, true);
        fileNonWritable = new File("non_writable", 30, false);
        //fileWithUnsupportedCharacters = new File(".;]#.#'unsupported", 10, true);
        fileWithVERYLargeSize = new File("LARGE", (1 << 30) + 1, true);
        //fileWithNoParam = new File();

    }

    @Test
    public void testConstructorStringInt_LegalCase() {
        myLittleFile = new File(".;]#.#'unsupported", 10, true);
        assertFalse(myLittleFile.isValidName(myLittleFile.getName()));
        assertEquals(myLittleFile.getName(), ".___.__unsupported");
        assertEquals(myLittleFile.getName(), ".___.__Unsupported");
    }
}

