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
     * @param  	name
     *         	The name of the new disk item.
     * @effect  The name of the disk item is set to the given name.
     * 			If the given name is not valid, a default name is set.
     *          | setName(name)
     * @post    The new creation time of this disk item is initialized to some time during
     *          constructor execution.
     *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
     *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
     * @post    The new disk item has no time of last modification.
     *          | new.getModificationTime() == null
     * @note	The constructor is annotated raw because at the start of the execution, not all fields are
     * 			defaulted to a value that is accepted by the invariants.
     * 			E.g. the name is defaulted to null, which is not allowed,
     * 			thus the object is in a raw state upon entry of the constructor.
     */
    @Raw @Model
    protected DiskItem(Directory parent, String name) {
        super(parent, name);
    }

    @Raw @Model
    protected DiskItem(String name) {
        super(null, name);
    }

    @Raw @Model
    protected DiskItem(Directory parent, String name, int size, boolean writable) {
        super(parent, name);
        setSize(size);
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



    /**********************************************************
     * size - nominal programming
     **********************************************************/

    /**
     * Variable registering the size of this disk item (in bytes).
     */
    private int size = 0;

    /**
     * Variable registering the maximum size of any disk item (in bytes).
     */
    private static final int maximumSize = Integer.MAX_VALUE;


    /**
     * Return the size of this disk item (in bytes).
     */
    @Raw @Basic
    public int getSize() {
        return size;
    }

    /**
     * Set the size of this disk item to the given size.
     *
     * @param  size
     *         The new size for this disk item.
     * @pre    The given size must be legal.
     *         | isValidSize(size)
     * @post   The given size is registered as the size of this disk item.
     *         | new.getSize() == size
     */
    @Raw @Model
    protected void setSize(int size) {
        this.size = size;
    }

    /**
     * Return the maximum disk item size.
     */
    @Basic @Immutable
    public static int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Check whether the given size is a valid size for a disk item.
     *
     * @param  size
     *         The size to check.
     * @return True if and only if the given size is positive and does not
     *         exceed the maximum size.
     *         | result == ((size >= 0) && (size <= getMaximumSize()))
     */
    public static boolean isValidSize(int size) {
        return ((size >= 0) && (size <= getMaximumSize()));
    }

    /**
     * Increases the size of this disk item with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this disk item
     *          must be increased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this disk item is increased with the given delta.
     *          | changeSize(delta)
     */
    public void enlarge(int delta) throws DiskItemNotWritableException {
        changeSize(delta);
    }

    /**
     * Decreases the size of this disk item with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this disk item
     *          must be decreased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this disk item is decreased with the given delta.
     *          | changeSize(-delta)
     */
    public void shorten(int delta) throws DiskItemNotWritableException {
        changeSize(-delta);
    }

    /**
     * Change the size of this disk item with the given delta.
     *
     * @param  delta
     *         The amount of bytes by which the size of this disk item
     *         must be increased or decreased.
     * @pre    The given delta must not be 0
     *         | delta != 0
     * @effect The size of this disk item is adapted with the given delta.
     *         | setSize(getSize()+delta)
     * @effect The modification time is updated.
     *         | setModificationTime()
     * @throws DiskItemNotWritableException(this)
     *         This disk item is not writable.
     *         | ! isWritable()
     */
    @Model
    private void changeSize(int delta) throws DiskItemNotWritableException{
        if (isWritable()) {
            setSize(getSize()+delta);
            setModificationTime();
        }else{
            throw new DiskItemNotWritableException(this);
        }
    }



}
