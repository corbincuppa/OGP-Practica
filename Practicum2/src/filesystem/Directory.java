package filesystem;

import be.kuleuven.cs.som.annotate.*;

import java.util.Date;
import java.util.List;

/**
 * A class of directories.
 *
 * @invar	Each directory must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar	Each directory must have a valid size.
 * 			| isValidSize(getSize())
 * @invar   Each directory must have a valid creation time.
 *          | isValidCreationTime(getCreationTime())
 * @invar   Each directory must have a valid modification time.
 *          | canHaveAsModificationTime(getModificationTime())
 *
 * @author  Adelina Vozianu
 * @author  Boglárka Csorba-Vitus
 * @author  Lander Werbrouck
 * @version 2.0
 */
public class Directory {

    /**********************************************************
     * Constructors
     **********************************************************/

    public Directory(String name, int size, boolean writable) {
        super(name, size, writable);
    }

    public Directory(String name) {
        super(name);
    }



    /**********************************************************
     * name - total programming
     **********************************************************/

    /**
     * Variable referencing the name of this directory.
     * @note		See Coding Rule 32, for information on the initialization of fields.
     */
    private String name = null;

    /**
     * Return the name of this directory.
     * @note		See Coding Rule 19 for the Basic annotation.
     */
    @Raw @Basic
    public String getName() {
        return name;
    }

    /**
     * Check whether the given name is a legal name for a directory.
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
     * Set the name of this directory to the given name.
     *
     * @param   name
     * 			The new name for this directory.
     * @post    If the given name is valid, the name of
     *          this directory is set to the given name,
     *          otherwise the name of the directory is set to a valid name (the default).
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
     * Return the name for a new directory which is to be used when the
     * given name is not valid.
     *
     * @return   A valid directory name.
     *         | isValidName(result)
     */
    @Model
    private static String getDefaultName() {
        return "new_directory";
    }

    /**
     * Change the name of this directory to the given name.
     *
     * @param	name
     * 			The new name for this directory.
     * @effect  The name of this directory is set to the given name,
     * 			if this is a valid name and the directory is writable,
     * 			otherwise there is no change.
     * 			| if (isValidName(name) && isWritable())
     *          | then setName(name)
     * @effect  If the name is valid and the directory is writable, the modification time
     * 			of this directory is updated.
     *          | if (isValidName(name) && isWritable())
     *          | then setModificationTime()
     * @throws  DirectoryNotWritableException(this)
     *          This directory is not writable
     *          | ! isWritable()
     */
    public void changeName(String name) throws DirectoryNotWritableException {
        if (isWritable()) {
            if (isValidName(name)){
                setName(name);
                setModificationTime();
            }
        } else {
            throw new DirectoryNotWritableException(this);
        }
    }

    /**********************************************************
     * size - nominal programming
     **********************************************************/

    /**
     * Variable registering the size of this directory (in bytes).
     */
    private int size = 0;

    /**
     * Variable registering the maximum size of any directory (in bytes).
     */
    private static final int maximumSize = Integer.MAX_VALUE;


    /**
     * Return the size of this directory (in bytes).
     */
    @Raw @Basic
    public int getSize() {
        return size;
    }

    /**
     * Set the size of this directory to the given size.
     *
     * @param  size
     *         The new size for this directory.
     * @pre    The given size must be legal.
     *         | isValidSize(size)
     * @post   The given size is registered as the size of this directory.
     *         | new.getSize() == size
     */
    @Raw @Model
    private void setSize(int size) {
        this.size = size;
    }

    /**
     * Return the maximum directory size.
     */
    @Basic @Immutable
    public static int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Check whether the given size is a valid size for a directory.
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
     * Increases the size of this directory with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this directory
     *          must be increased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this directory is increased with the given delta.
     *          | changeSize(delta)
     */
    public void enlarge(int delta) throws DirectoryNotWritableException {
        changeSize(delta);
    }

    /**
     * Decreases the size of this directory with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this directory
     *          must be decreased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this directory is decreased with the given delta.
     *          | changeSize(-delta)
     */
    public void shorten(int delta) throws DirectoryNotWritableException {
        changeSize(-delta);
    }

    /**
     * Change the size of this directory with the given delta.
     *
     * @param  delta
     *         The amount of bytes by which the size of this directory
     *         must be increased or decreased.
     * @pre    The given delta must not be 0
     *         | delta != 0
     * @effect The size of this directory is adapted with the given delta.
     *         | setSize(getSize()+delta)
     * @effect The modification time is updated.
     *         | setModificationTime()
     * @throws DirectoryNotWritableException(this)
     *         This directory is not writable.
     *         | ! isWritable()
     */
    @Model
    private void changeSize(int delta) throws DirectoryNotWritableException{
        if (isWritable()) {
            setSize(getSize()+delta);
            setModificationTime();
        }else{
            throw new DirectoryNotWritableException(this);
        }
    }



    /**********************************************************
     * creationTime
     **********************************************************/

    /**
     * Variable referencing the time of creation.
     */
    private final Date creationTime = new Date();

    /**
     * Return the time at which this directory was created.
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
    private Date modificationTime = null;

    /**
     * Return the time at which this directory was last modified, that is
     * at which the name or size was last changed. If this directory has
     * not yet been modified after construction, null is returned.
     */
    @Raw @Basic
    public Date getModificationTime() {
        return modificationTime;
    }

    /**
     * Check whether this directory can have the given date as modification time.
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
     * Set the modification time of this directory to the current time.
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
    private void setModificationTime() {
        modificationTime = new Date();
    }

    /**
     * Return whether this directory and the given other directory have an
     * overlapping use period.
     *
     * @param 	other
     *        	The other directory to compare with.
     * @return 	False if the other directory is not effective
     * 			False if the prime object does not have a modification time
     * 			False if the other directory is effective, but does not have a modification time
     * 			otherwise, true if and only if the open time intervals of this directory and
     * 			the other directory overlap
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
    public boolean hasOverlappingUsePeriod(Directory other) {
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
     * Variable registering whether or not this directory is writable.
     */
    private boolean isWritable = true;

    /**
     * Check whether this directory is writable.
     */
    @Basic
    public boolean isWritable() {
        return isWritable;
    }

    /**
     * Set the writability of this directory to the given writability.
     *
     * @param isWritable
     *        The new writability
     * @post  The given writability is registered as the new writability
     *        for this directory.
     *        | new.isWritable() == isWritable
     */
    @Raw
    public void setWritable(boolean isWritable) {
        this.isWritable = isWritable;
    }


    /**********************************************************
     * disk items
     **********************************************************/

    /**
     * Variable referring to the list of disk items inside a directory.
     */
    private List diskItems;

    /**
     * Return the number of disk items inside a directory.
     */
    public int getNbItems() {
        return diskItems.size();
    }

    /**
     * @param position
     *        The given position
     * Return disk item situated at the given position in the directory.
     */
    //public String getItemAt(int position) {}

    /**
     * @param item
     *        The given item
     * Checks whether or not a given item is inside the directory.
     * @return Returns false if the given disk item is not inside the directory
     *         and true if the given disk item is inside the directory.
     */
    public boolean hasAsItem(File item) {
        if (diskItems.contains(item)) {
            return true;
        }
        else{
            return false;
        }
    }
}
