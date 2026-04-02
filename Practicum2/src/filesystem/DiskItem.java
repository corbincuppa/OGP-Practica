package filesystem;

import be.kuleuven.cs.som.annotate.*;


/**
 * A class of disk items.
 *
 * @invar	Each disk item must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar	Each disk item must have a valid size.
 * 			| isValidSize(getSize())
 * @invar   Each disk item must have a valid creation time.
 *          | isValidCreationTime(getCreationTime())
 * @invar   Each disk item must have a valid modification time.
 *          | canHaveAsModificationTime(getModificationTime())
 * @invar   Each disk item must have a valid parent directory.
 *          | isValidParentDir()
 *
 * @author  Adelina Vozianu
 * @author  Boglárka Csorba-Vitus
 * @author  Lander Werbrouck
 * @version 2.0
 */
public class DiskItem extends PrimitiveDiskItem{

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new disk item with given name, size and writability.
     *
     * @param  	writable
     *         	The writability of the new diskItem.
     * @effect The writability is set to the given flag
     *         | setWritable(writable)
     */
    @Raw @Model
    protected DiskItem(Directory parent, String name, boolean writable) {
        super(parent, name);
        setWritable(writable);
    }


    /**********************************************************
     * name - total programming
     **********************************************************/

    /**
     * Change the name of this disk item to the given name.
     *
     * @param	name
     * 			The new name for this disk item.
     * @effect  The name of this disk item is set to the given name,
     * 			if this is a valid name and the disk item is writable,
     * 			otherwise there is no change.
     * 			| if (isValidName(name) && isWritable())
     *          | then setName(name)
     * @effect  If the name is valid and the disk item is writable, the modification time
     * 			of this disk item is updated.
     *          | if (isValidName(name) && isWritable())
     *          | then setModificationTime()
     * @throws  DiskItemNotWritableException(this)
     *          This disk item is not writable
     *          | ! isWritable()
     */
    @Override
    public void changeName(String name) throws DiskItemNotWritableException {
        if (isWritable()) {
            if (isValidName(name)){
                setName(name);
                setModificationTime();
            }
        } else {
            throw new DiskItemNotWritableException(this);
        }
    }



    /**********************************************************
     * writable
     **********************************************************/

    /**
     * Variable registering whether this disk item is writable.
     */
    private boolean writable = true;

    /**
     * Check whether this disk item is writable.
     */
    @Basic
    public boolean isWritable() {
        return writable;
    }

    /**
     * Set the writability of this disk item to the given writability.
     *
     * @param writable
     *        The new writability
     * @post  The given writability is registered as the new writability
     *        for this disk item.
     *        | new.isWritable() == isWritable
     */
    @Raw
    public void setWritable(boolean writable) {
        this.writable = writable;
    }

}
