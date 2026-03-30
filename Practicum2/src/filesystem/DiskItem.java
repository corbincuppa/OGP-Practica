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
public abstract class DiskItem {
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
     * name - total programming
     **********************************************************/

    /**
     * Variable referencing the name of this disk item.
     * @note		See Coding Rule 32, for information on the initialization of fields.
     */
    protected String name = null;

    /**
     * Return the name of this disk item.
     * @note		See Coding Rule 19 for the Basic annotation.
     */
    @Raw @Basic
    public String getName() {
        return name;
    }

    /**
     * Check whether the given name is a legal name for a disk item.
     *
     * @param  	name
     *			The name to be checked
     * @return	True if the given string is effective, not
     * 			empty and consisting only of letters, digits, dots,
     * 			hyphens and underscores; false otherwise.
     * 			| result ==
     * 			|	(name != null) && name.matches("[a-zA-Z_0-9-]+")
     */
    public static boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z_0-9-]+"));
    }

    /**
     * Set the name of this disk item to the given name.
     *
     * @param   name
     * 			The new name for this disk item.
     * @post    If the given name is valid, the name of
     *          this disk item is set to the given name,
     *          otherwise the name of the disk item is set to a valid name (the default).
     *          | if (isValidName(name))
     *          |      then new.getName().equals(name)
     *          |      else new.getName().equals(getDefaultName())
     */
    @Raw @Model
    private void setName(String name) {
        if (isValidName(name)) {
            this.name = name;
        } else {
            this.name = getDefaultName();
        }
    }

    /**
     * Return the name for a new disk item which is to be used when the
     * given name is not valid.
     *
     * @return   A valid disk item name.
     *         | isValidName(result)
     */
    @Model
    private static String getDefaultName() {
        return "new_diskItem";
    }

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
     * creationTime
     **********************************************************/

    /**
     * Variable referencing the time of creation.
     */
    protected final Date creationTime = new Date();

    /**
     * Return the time at which this disk item was created.
     */
    @Raw @Basic @Immutable
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Check whether the given date is a valid creation time.
     *
     * @param  	date
     *         	The date to check.
     * @return 	True if and only if the given date is effective and not
     * 			in the future.
     *         	| result ==
     *         	| 	(date != null) &&
     *         	| 	(date.getTime() <= System.currentTimeMillis())
     */
    public static boolean isValidCreationTime(Date date) {
        return 	(date!=null) &&
                (date.getTime()<=System.currentTimeMillis());
    }



    /**********************************************************
     * modificationTime
     **********************************************************/

    /**
     * Variable referencing the time of the last modification,
     * possibly null.
     */
    protected Date modificationTime = null;

    /**
     * Return the time at which this disk item was last modified, that is
     * at which the name or size was last changed. If this disk item has
     * not yet been modified after construction, null is returned.
     */
    @Raw @Basic
    public Date getModificationTime() {
        return modificationTime;
    }

    /**
     * Check whether this disk item can have the given date as modification time.
     *
     * @param	date
     * 			The date to check.
     * @return 	True if and only if the given date is either not effective
     * 			or if the given date lies between the creation time and the
     * 			current time.
     *         | result == (date == null) ||
     *         | ( (date.getTime() >= getCreationTime().getTime()) &&
     *         |   (date.getTime() <= System.currentTimeMillis())     )
     */
    @Raw
    public boolean canHaveAsModificationTime(Date date) {
        return (date == null) ||
                ( (date.getTime() >= getCreationTime().getTime()) &&
                        (date.getTime() <= System.currentTimeMillis()) );
    }

    /**
     * Set the modification time of this disk item to the current time.
     *
     * @post   The new modification time is effective.
     *         | new.getModificationTime() != null
     * @post   The new modification time lies between the system
     *         time at the beginning of this method execution and
     *         the system time at the end of method execution.
     *         | (new.getModificationTime().getTime() >=
     *         |                    System.currentTimeMillis()) &&
     *         | (new.getModificationTime().getTime() <=
     *         |                    (new System).currentTimeMillis())
     */
    @Model
    protected void setModificationTime() {
        modificationTime = new Date();
    }

    /**
     * Return whether this disk item and the given other disk item have an
     * overlapping use period.
     *
     * @param 	other
     *        	The other disk item to compare with.
     * @return 	False if the other disk item is not effective
     * 			False if the prime object does not have a modification time
     * 			False if the other disk item is effective, but does not have a modification time
     * 			otherwise, true if and only if the open time intervals of this disk item and
     * 			the other disk item overlap
     *        	| if (other == null) then result == false else
     *        	| if ((getModificationTime() == null)||
     *        	|       other.getModificationTime() == null)
     *        	|    then result == false
     *        	|    else
     *        	| result ==
     *        	| ! (getCreationTime().before(other.getCreationTime()) &&
     *        	|	 getModificationTime().before(other.getCreationTime()) ) &&
     *        	| ! (other.getCreationTime().before(getCreationTime()) &&
     *        	|	 other.getModificationTime().before(getCreationTime()) )
     */
    public boolean hasOverlappingUsePeriod(DiskItem other) {
        if (other == null) return false;
        if(getModificationTime() == null || other.getModificationTime() == null) return false;
        return ! (getCreationTime().before(other.getCreationTime()) &&
                getModificationTime().before(other.getCreationTime()) ) &&
                ! (other.getCreationTime().before(getCreationTime()) &&
                        other.getModificationTime().before(getCreationTime()) );
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
