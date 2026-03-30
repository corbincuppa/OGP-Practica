package filesystem;

import be.kuleuven.cs.som.annotate.*;
import java.util.ArrayList;
import java.util.Collections;

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
 * @invar   Each directory must have a valid parent directory, unless the
 *          directory is a root directory.
 *          | if !isRoot()
 *          | then isValidParentDir()
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

    public Directory(Directory parent, String name, boolean writable){
        super(parent, name);
        setWritable(writable);
    }

    public Directory(Directory parent, String name){
        super(parent, name);
        setWritable(true);
    }

    public Directory(String name, boolean writable){
        super(name);
        setWritable(writable);
    }

    public Directory(String name){
        super(name);
        setWritable(true);
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
     * disk items - defensive programming
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
     *        The given position starting from 0.
     */
    public DiskItem getItemAt(int position) {
        return this.diskItems.get(position);
    }

    /**
     * Return the disk item with the given name, if the disk item
     * is inside this directory. // throws exception if not in directory??
     *
     * @param nameItem
     * @return item
     * The disk item with the given name which is
     * inside this directory.
     */
    public Object getItem(String nameItem) {
        for(DiskItem item: diskItems) {
            if (item.getName() == nameItem) {
                return item;
            }
        }
        return "Found no such item.";
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
     *
     * @param	item
     * 			The item to be added to this directory.
     * @effect  The given disk item is added to the contents of this directory
     * 		    if this directory is writable and is not an indirect child of itself,
     * 		    otherwise there is no change.
     * 			| if (isWritable() && ! isDirectOrIndirectChildOf(this))
     *          | then diskItems.add(item)
     * @effect  If the directory is writable and the directory doesn't contain itself,
     *          the modification time of the disk item is updated.
     *          | if (isWritable() && ! isDirectOrIndirectChildOf(this))
     *          | then item.setModificationTime()
     * @throws  DirectoryNotWritableException(this)
     *          This directory is not writable
     *          | ! isWritable()
     * @throws  DirectoryContainsSelfException(this)
     *          This directory contains itself
     *          | isDirectOrIndirectChildOf()
     */
    public void addItem(DiskItem item) throws DirectoryContainsSelfException, DirectoryNotWritableException {
        if (isWritable()) {
            // if (item.isDirectOrIndirectChildOf(this))
            if (item == null) {
                diskItems.add(item);
                this.sortDiskItems();
                item.setParent(this);
            } else {
                throw new DirectoryContainsSelfException(this);
            }
        }
        else{
                throw new DirectoryNotWritableException(this);
        }

    }

    public void addList(ArrayList<DiskItem> list){
        for (DiskItem item: list){
            this.addItem(item);
        }
    }

    /**
     *
     * @param indexItem1
     */
    private void swapItems(int indexItem1) {
        DiskItem tmp1 = this.getItemAt(indexItem1);
        DiskItem tmp2 = this.getItemAt(indexItem1+1);
        diskItems.set(indexItem1, tmp2);
        diskItems.set(indexItem1+1, tmp1);
    }

    /**
     * Sort the disk items in lexicographical order.
     *
     * @effect FEBHVYILFGWEBYILEQFVY
     */
    public void sortDiskItems() {
        for (int pass = 0 ; pass < diskItems.size(); pass++){
            for (int indexItem = 0 ; indexItem < diskItems.size()-1 ; indexItem++) {
                DiskItem item1 = diskItems.get(indexItem);
                DiskItem item2 = diskItems.get(indexItem + 1);
                if (item1 != null && item2 != null) {
                    String name1 = item1.getName();
                    String name2 = item2.getName();
                    // mogen niet dezelfde string zijn
                    if (name1.compareToIgnoreCase(name2) == 0) {
                        // throw new DiskItemsHaveSameNameException
                    } else if (name1.compareToIgnoreCase(name2) > 0) {
                        swapItems(indexItem);

                    }
                }
            }
        }
    }


    /**
     * Return the contents of this directory.
     */
    public ArrayList<DiskItem> getDiskItems() {
        return diskItems;
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
            return this.getParent() == null;
        }







    public void sortList()
    {
        // Sorting the list using lambda function
        this.diskItems.sort((a, b) -> a.getName().compareTo(b.getName()));
    }

}
