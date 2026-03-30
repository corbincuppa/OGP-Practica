package filesystem;

import be.kuleuven.cs.som.annotate.*;
import java.util.Date;


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
public abstract class DiskItem extends PrimitiveDiskItem{
    /**
     *
     */
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
    protected DiskItem(Dir dir, String name) {
        setDir(dir);
        setName(name);
    }

    /**********************************************************
     * dir
     **********************************************************/
    protected Dir dir;

    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        // als writable?
        this.dir = dir;
    }



    /**********************************************************
     * writable
     **********************************************************/

    /**
     * Variable registering whether or not this disk item is writable.
     */
    private boolean isWritable = true;

    /**
     * Check whether this disk item is writable.
     */
    @Basic
    public boolean isWritable() {
        return isWritable;
    }

    /**
     * Set the writability of this disk item to the given writability.
     *
     * @param isWritable
     *        The new writability
     * @post  The given writability is registered as the new writability
     *        for this disk item.
     *        | new.isWritable() == isWritable
     */
    @Raw
    public void setWritable(boolean isWritable) {
        this.isWritable = isWritable;
    }

    /**********************************************************
     * parent directory
     **********************************************************/

    /**
     * The parents directory of this disk item, thus the directory which contains
     * this disk item. Cannot be null.
     */
    protected Dir parent;

    /**
     * Make the parent directory of this disk item the given directory.
     *
     * @param directory
     *        The given directory to which this disk item will be "moved".
     */
    protected void setParent(Dir directory){
        if (isValidParentDir(this.getParent()) && isValidParentDir(directory)) {
            parent.remove(this);
            directory.add(this);
            setParent(directory);
        }
        this.parent = directory;
    }

    /**
     * Return the parent directory of this directory.
     */
    public Dir getParent() {
        return this.parent;
    }

    /**
     * Check if the given directory is a valid parent directory,
     * i.e. not null.
     *
     * @param   dir
     *          The given directory to check.
     * @return  Return false if this disk item does not have a parent directory,
     *          return true otherwise.
     */
    protected boolean isValidParentDir(Dir dir) {
        if (dir != null) {
            return true;
        }
        return false;
    }
}
