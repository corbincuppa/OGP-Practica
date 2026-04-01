package filesystem;

import be.kuleuven.cs.som.annotate.*;

import java.util.Date;

/**
 * A class of disk items.
 *
 * @invar	Each disk item must have a properly spelled name.
 * 			| isValidName(getName())
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
public abstract class PrimitiveDiskItem {
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
    @Raw
    @Basic
    public String getName() {
        return name;
    }

    /**
     * Check whether the given name is a legal name for a disk item.
     *
     * @param  	name
     *			The name to be checked
     * @return	True if the given string is effective, not
     * 			empty and consisting only of letters, digits,
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
    void setName(String name) {
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
     * 			if this is a valid name, otherwise there is no change.
     * 			| if (isValidName(name))
     *          | then setName(name)
     * @effect  If the name is valid, the modification time
     * 			of this disk item is updated.
     *          | if (isValidName(name))
     *          | then setModificationTime()
     */
    public void changeName(String name) {
        if (isValidName(name)) {
            setName(name);
            setModificationTime();
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
     * parent directory
     **********************************************************/

    /**
     * The parents directory of this disk item, thus the directory which contains
     * this disk item. Cannot be null.
     */
    protected Directory dir;

    /**
     * Make the parent directory of this disk item the given directory.
     *
     * @effect  If the given directory is a valid parents directory and the parent of
     *          this directory is a valid parent, then the current parent of this
     *          directory is removed.
     *          | if (isValidParentDir(this.getParent()) && isValidParentDir(directory))
     *          | then dir.remove(this)
     * @effect  If the given directory is a valid parents directory and the parent of
     *          this directory is a valid parent, then the directory is added to the
     *          contents of the given directory.
     *          | if (isValidParentDir(this.getParent()) && isValidParentDir(directory))
     *          | then directory.addItem(this)
     * @effect  If the given directory is a valid parents directory and the parent of
     *          this directory is a valid parent, then the new parent of this directory
     *          is set to the given directory
     *          | if (isValidParentDir(this.getParent()) && isValidParentDir(directory))
     *          | then setParent(directory)
     * @param directory
     *        The given directory to which this disk item will be "moved".
     */
    @Raw @Model
    protected void setParent(Directory directory){
        if (isValidParentDir(this.getParent()) && isValidParentDir(directory)) {
            dir.remove(this);
            directory.addItem(this);
            setParent(directory);
        }
        this.dir = directory;
    }

    /**
     * Return the parent directory of this directory.
     */
    public Directory getParent() {
        return this.dir;
    }

    /**
     * Check if the given directory is a valid parent directory,
     * i.e. not null.
     *
     * @param   dir
     *          The given directory to check.
     * @return  Return false if this disk item does not have a parent directory,
     *          return true otherwise.
     *          | result == (dir != null)
     */
    protected boolean isValidParentDir(Directory dir) {
        if (dir != null) {
            return true;
        }
        return false;
    }

}
