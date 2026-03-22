package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal attempts to change a directory.
 *
 * @invar	The referenced directory must be effective
 * 			| isValidFile(getFile())
 * @author  Adelina Vozianu
 * @author  Boglárka Csorba-Vitus
 * @author  Lander Werbrouck
 * @version 2.0
 */
public class DirectoryNotWritableException extends RuntimeException {

    /**
     * Required because this class inherits from Exception
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing the directory to which change was denied.
     */
    private final Directory directory;

    /**
     * Check whether the given directory is a valid directory for this Exception.
     * @param 	directory
     * 			The directory to check
     * @return	result == (directory != null)
     */
    public static boolean isValidDirectory(Directory directory) {
        return directory != null;
    }

    /**
     * Initialize this new directory not writable exception involving the
     * given directory.
     *
     * @param	directory
     * 			The directory for the new directory not writable exception.
     * @pre		The given directory must be a valid directory
     * 			| isValidDirectory(directory)
     * @post	The directory involved in the new directory not writable exception
     * 			is set to the given directory.
     * 			| new.getDirectory() == directory
     */
    public DirectoryNotWritableException(Directory directory) {
        this.directory = directory;
    }

    /**
     * Return the directory involved in this directory not writable exception.
     */
    @Basic @Immutable
    public Directory getDirectory() {
        return directory;
    }

}
