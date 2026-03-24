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
