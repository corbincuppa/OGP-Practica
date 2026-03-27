package filesystem;

import be.kuleuven.cs.som.annotate.*;
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
public class Directory extends DiskItem {

    /**********************************************************
     * Constructors
     **********************************************************/

    public  Directory(dir, String name, boolean writable){
        //dir?
        super(name);
        setWritable(writable);
    }

    public Directory(dir, String name){
        //dir?
        super(name);
        setWritable(true);
    }
    //--> this(...)

    public Directory(String name, boolean writable){
        //dir?
        super(name);
        setWritable(writable);
    }
    //--> this(...)

    public Directory(String name){
        //dir?
        super(name);
        setWritable(true);
    }
    //--> this(...)

    /**********************************************************
     * name - total programming
     **********************************************************/


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
    // --> is niet nodig

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
