package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal attempts to call upon a disk item
 * which is not contained in the given directory.
 *
 * @author  Adelina Vozianu
 * @author  Boglárka Csorba-Vitus
 * @author  Lander Werbrouck
 * @version 2.0
 */
public class DiskItemNotInDirectoryException extends RuntimeException {
    /**
     * Required because this class inherits from Exception
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing the name of the disk item which was not found.
     */
    private final String nameDiskItem;

    /**
     * Check whether the given name of the disk item is a valid name for this Exception.
     * @param 	nameDiskItem
     * 			The name of the disk item to check
     * @return	result == (nameDiskItem != null)
     */
    public static boolean isValidNameItem(String nameDiskItem) {
        return nameDiskItem != null;
    }

    /**
     * Initialize this new disk item not in directory exception involving the
     * given disk item.
     *
     * @param	nameDiskItem
     * 			The given name of the disk item for the new disk item not in directory exception.
     * @pre		The given name must be a valid name
     * 			| isValidNameItem(diskItem)
     * @post	The name of the disk item involved in the new disk item not in directory exception
     * 			is set to the given name.
     * 			| new.getNameItem() == nameDiskItem
     */
    public DiskItemNotInDirectoryException(String nameDiskItem) {
        this.nameDiskItem = nameDiskItem;
    }

    /**
     * Return the name of the disk item involved in this disk item not in directory exception.
     */
    @Basic
    @Immutable
    public String getNameItem() {
        return nameDiskItem;
    }
}
