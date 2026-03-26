package filesystem;

import be.kuleuven.cs.som.annotate.*;
import java.util.ArrayList;

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


    /**********************************************************
     * size - nominal programming
     **********************************************************/
    /**
     * Return the size of this disk item (in bytes).
     */
    @Raw @Basic @Override
    public int getSize() {
        int sum = 0;
        for(DiskItem item: diskItems){
            int size = item.getSize();
            sum += size;
        }
        return sum;
    }



    /**********************************************************
     * disk items
     **********************************************************/

    /**
     * Variable referring to the list of disk items inside a directory.
     */
    private ArrayList<DiskItem> diskItems;

    /**
     * Return the number of disk items inside a directory.
     */
    public int getNbItems() {
        return diskItems.size();
    }

    /**
     * Return disk item situated at the given position in the directory.
     *
     * @param position
     *        The given position
     */
    public DiskItem getItemAt(int position) {
        return this.diskItems.get(position+1);
    }

    /**
     * Return the disk item with the given name, if the disk item
     * is inside this directory. // throws exception if not in directory??
     *
     * @param nameItem
     * @return item
     *         The disk item with the given name which is
     *         inside this directory.
     */
    public DiskItem getItem(String nameItem) {
        for(DiskItem item: diskItems) {
            if (item.getName() == nameItem) {
                return item;
            }
        }
    }

    /**
     * Check whether or not this directory contains a disk item with
     * the given name.
     *
     * @param name
     *        The given name of the disk item.
     * @return Return true if this directory contains a disk item with the given name
     *         and returns false otherwise.
     */
    public boolean containsDiskItemWithName(String name) {
        if (diskItems.contains(this.getItem(name))) {
            return true;
        }
        return false;
    }

    /**
     * Return the index in the directory of the given disk item.
     * 
     * @param item
     *        The given disk item.
     */
    public int getIndex(DiskItem item) {
        return diskItems.indexOf(item);
    }

    /**
     * Check whether a given disk item is inside this directory.
     *
     * @param item
     *        The given item.
     * @return True if this directory contains the given disk item
     *          and false otherwise.
     *          | result == diskItems.contains(item)
     */
    public boolean hasAsItem(DiskItem item) {
        if (diskItems.contains(item)) {
            return true;
        }
        return false;
    }

    /**
     * Adds a given disk item to this directory.
     * // throws exception!!!!!!!!!!! DirectoryCannotHaveParentAsSelf ofzo, needs to check writabilityytytyyty
     *  and has to like rearrange the disk items in the doirectory in alphabetical roder??
     *
     * @param item
     */
    public void addItem(DiskItem item) {
        if (this.isWritable() == true && item.isIndirectChildOf(this) == false) {
            diskItems.add(item);
        }
    }

    private void organizeDiskItems() {
        for (DiskItem item: diskItems) {
            String itemName = item.getName();
        }
    }



    /**********************************************************
     * root
     **********************************************************/

    /**
     * Variable registering whether this directory is a root directory.
     */
    private boolean root = true;

    /**
     * Check whether this directory is a root directory.
     *
     * @return True if this directory has no parents, false otherwise.
     *         | result == (this.getParent() == null)
     */
    public boolean isRoot() {
        if (this.getParent() == null) {
            return true;
        }

        return false;
    }



    /**********************************************************
     * parent
     **********************************************************/

    /**
     * Variable registering the parent of this directory. If this
     * directory is a root directory, this directory has no parents.
     */
    private Directory parent;

    /**
     * Return the parent directory of this directory.
     */
    public Directory getParent() {
        return this.parent;
    }

}
