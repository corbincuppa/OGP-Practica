package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal attempts to have disk items with the same name
 * in the same directory.
 *
 * @invar	The referenced disk item must be effective.
 * 			| isValidDiskItem(getDiskItem())
 *
 * @author  Adelina Vozianu
 * @author  Boglárka Csorba-Vitus
 * @author  Lander Werbrouck
 * @version 2.0
 */
public class DiskItemsHaveSameNameException extends RuntimeException {


    /**
     * Required because this class inherits from Exception
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing the disk item to which has the same name.
     */
    private final PrimitiveDiskItem diskItem;

    /**
     * Variable referencing the other disk item to which has the same name.
     */
    private final PrimitiveDiskItem otherDiskItem;

    /**
     * Check whether the given disk item is a valid disk item for this Exception.
     * @param 	diskItem
     * 			The disk item to check
     * @return	result == (diskItem != null)
     */
    public static boolean isValidDiskItem(PrimitiveDiskItem diskItem) {
        return diskItem != null;
    }

    /**
     * Initialize this new disk items have the same name exception involving the two given
     * disk items.
     *
     * @param	diskItem
     * 			The disk item which has the same name as the other disk item.
     * @param   otherDiskItem
     *          The other disk item which has the same name as the first item.
     * @pre		The given disk item must be a valid disk item
     * 			| isValidDiskItem(diskItem)
     * @post	The disk item involved in the new disk item not writable exception
     * 			is set to the given disk item.
     * 			| new.getDiskItem() == diskItem
     */
    public DiskItemsHaveSameNameException(PrimitiveDiskItem diskItem, PrimitiveDiskItem otherDiskItem) {
        this.diskItem = diskItem;
        this.otherDiskItem = otherDiskItem;
    }

    /**
     * Return the name of the disk items involved in this disk items have the same name exception.
     */
    @Basic @Immutable
    public String getNameItem() {
        return diskItem.getName();
    }
}
