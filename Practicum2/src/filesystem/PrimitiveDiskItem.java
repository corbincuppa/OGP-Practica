package filesystem;

import be.kuleuven.cs.som.annotate.*;

import java.util.Date;

/**
 * A class of disk items.
 *
 * @invar	Each disk item must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar   Each disk item must have a valid creation time.
 *          | isValidCreationTime(getCreationTime())
 * @invar   Each disk item must have a valid modification time.
 *          | canHaveAsModificationTime(getModificationTime())
 * @invar   Each disk item must have a valid parent directory.
 *          | hasValidParentDir()
 *
 * @author  Adelina Vozianu
 * @author  Boglárka Csorba-Vitus
 * @author  Lander Werbrouck
 * @version 2.0
 */
public abstract class PrimitiveDiskItem {
    /**********************************************************
     * constructors
     **********************************************************/

    /**
     * Initialize a new primitive disk item with given parent and name.
     *
     * @param   dir
     *        	The directory containing the new primitive disk item
     * @param  	name
     *         	The name of the primitive disk item
     * @effect  The name of the primitive disk item is set to the given name.
     * 			If the given name is not valid, a default name is set.
     *          | setName(name)
     * @effect	The directory is set to the given directory
     * 			| setParent(dir)
     * @post    The new creation time of this primitive disk item is initialized to some time during
     *          constructor execution.
     *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
     *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
     * @post    The primitive disk item has no time of last modification.
     *          | new.getModificationTime() == null
     * @note	The constructor is annotated raw because at the start of the execution, not all fields are
     * 			defaulted to a value that is accepted by the invariants.
     * 			E.g. the name is defaulted to null, which is not allowed,
     * 			thus the object is in a raw state upon entry of the constructor.
     */
    @Raw
    public PrimitiveDiskItem(Directory dir, String name) {
        setParent(dir);
        setName(name);
    }

    /**********************************************************
     * name - total programming
     **********************************************************/

    /**
     * Variable referencing the name of this primitive disk item.
     */
    protected String name = null;

    /**
     * Return the name of this primitive disk item.
     */
    @Raw @Basic
    public String getName() {
        return name;
    }

    /**
     * Check whether the given name is a legal name for a primitive disk item.
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
     * Set the name of this primitive disk item to the given name.
     *
     * @param   name
     * 			The new name for this primitive disk item.
     * @post    If the given name is valid, the name of
     *          this primitive disk item is set to the given name,
     *          otherwise the name of the primitive disk item is set to a valid name (the default).
     *          | if (isValidName(name))
     *          |      then new.getName().equals(name)
     *          |      else new.getName().equals(getDefaultName())
     */
    @Raw @Model
    protected void setName(String name) {
        if (isValidName(name)) {
            this.name = name;
        } else {
            this.name = getDefaultName();
        }
    }

    /**
     * Return the name for a new primitive disk item which is to be used when the
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
     * Change the name of this primitive disk item to the given name.
     *
     * @param	name
     * 			The new name for this primitive disk item.
     * @effect  The name of this primitive disk item is set to the given name,
     * 			if this is a valid name, otherwise there is no change.
     * 			| if (isValidName(name))
     *          | then setName(name)
     * @effect  If the name is valid, the modification time
     * 			of this primitive disk item is updated.
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
     * Return the time at which this primitive disk item was last modified, that is
     * at which the name or size was last changed. If this primitive disk item has
     * not yet been modified after construction, null is returned.
     */
    @Raw @Basic
    public Date getModificationTime() {
        return modificationTime;
    }

    /**
     * Check whether this primitive disk item can have the given date as modification time.
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
     * Set the modification time of this primitive disk item to the current time.
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
     * Return whether this primitive disk item and the given other primitive disk item have an
     * overlapping use period.
     *
     * @param 	other
     *        	The other disk item to compare with.
     * @return 	False if the other primitive disk item is not effective
     * 			False if the prime object does not have a modification time
     * 			False if the other primitive disk item is effective, but does not have a modification time
     * 			otherwise, true if and only if the open time intervals of this primitive disk item and
     * 			the other primitive disk item overlap
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
     * this disk item cannot be null.
     */
    protected Directory parent;

    /**
     * Make the parent directory of this disk item the given directory.
     *
     * @effect  If the given directory is a valid parent directory and the parent of
     *          this directory is a valid parent, then this disk item is removed from
     *          its parent's contents.
     *          | if (hasValidParentDir(this)) && directory != null)
     *          | then dir.getDiskItems().remove(this)
     * @effect  If the given directory is a valid parent directory and the parent of
     *          this disk item is a valid parent, then this disk item is added to the
     *          contents of the given directory.
     *          | if (hasValidParentDir(this)) && directory != null)
     *          | then directory.getDiskItems().add(this)
     * @effect  If the given directory is a valid parent directory and the parent of
     *          this disk item is a valid parent, then the new parent of this disk item
     *          is set to the given directory.
     *          | if (hasValidParentDir(this)) && directory != null)
     *          | then this.parent = directory
     * @param directory
     *        The given directory which is to be set as the parent of this disk item.
     */
    @Raw @Model
    protected void setParent(Directory directory){
        if (hasValidParentDir((Directory) this) && directory != null) {
            parent.getDiskItems().remove(this);
            directory.getDiskItems().add(this);
            this.parent = directory;
        } else if (directory != null && !hasValidParentDir((Directory) this)) {
            directory.getDiskItems().add(this);
            this.parent = directory;
        }
    }

    /**
     * Return the parent directory of this directory.
     */
    public Directory getParent() {
        return this.parent;
    }

    /**
     * Check if the given directory has a valid parent directory,
     * i.e. not a null parent.
     *
     * @param   dir
     *          The given directory of which its parent is to be checked.
     * @return  True if the parent of the given directory is not null, false otherwise.
     *          | result == dir.getParent() != null
     */
    protected boolean hasValidParentDir(Directory dir) {
        return dir.getParent() != null;
    }

    /**
     * Check whether this primitive disk item is a direct or indirect child of the given directory
     *
     * @param  	directory
     *          The directory to check against
     * @return 	True if this primitive disk item is contained somewhere in the directory tree of the given directory
     *         	|result == (exists Directory parent;
     *          |(parent == this.getParent() && parent.equals(directory)) ||
     *          |(parent == this.getParent().getParent() && parent.equals(directory)) ||
     *          | ...
     */
    public boolean isDirectOrIndirectChildOf(Directory directory){
        Directory parent = this.getParent();
        while (parent != null) {
            if (parent.equals(directory)) {
                return true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    /**
     * Get the root directory of the directory tree of the given directory
     *
     * @return 	The root directory of this primitive disk item
     *          | result == (result.getParent == null)
     */
    public Directory getRoot(){
        PrimitiveDiskItem child = this;
        Directory parent = this.getParent();
        while (parent != null) {
            child = parent;
            parent = parent.getParent();
        }
        return (Directory) child;
    }

    /**********************************************************
     * destructors
     **********************************************************/


}
