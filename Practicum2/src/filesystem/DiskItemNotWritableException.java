package filesystem;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class for signaling illegal attempts to change a disk item.
 *
 * @invar	The referenced disk item must be effective.
 * 			| isValidDiskItem(getDiskItem())
 *
 * @author  Adelina Vozianu
 * @author  Boglárka Csorba-Vitus
 * @author  Lander Werbrouck
 * @version 2.0
 */
public class DiskItemNotWritableException extends RuntimeException {

    /**
     * Required because this class inherits from Exception
     */
    private static final long serialVersionUID = 1L;

    /**
     * Variable referencing the disk item to which change was denied.
     */
    private final PrimitiveDiskItem diskItem;

    /**
     * Check whether the given disk item is a valid disk item for this Exception.
     * @param 	diskItem
     * 			The disk item to check
     * @return	result == (diskItem != null)
     */
    public static boolean isValidDiskItem(DiskItem diskItem) {
        return diskItem != null;
    }

    /**
     * Initialize this new disk item not writable exception involving the
     * given disk item.
     *
     * @param	diskItem
     * 			The disk item for the new disk item not writable exception.
     * @pre		The given disk item must be a valid disk item
     * 			| isValidDiskItem(diskItem)
     * @post	The disk item involved in the new disk item not writable exception
     * 			is set to the given disk item.
     * 			| new.getDiskItem() == diskItem
     */
    public DiskItemNotWritableException(PrimitiveDiskItem diskItem) {
        this.diskItem = diskItem;
    }

    /**
     * Return the disk item involved in this disk item not writable exception.
     */
    @Basic @Immutable
    public PrimitiveDiskItem getDiskItem() {
        return diskItem;
    }

}

