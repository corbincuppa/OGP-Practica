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
     * @effect  If the name is valid and the disk item is writable, the order of the items
     *          in this directory is resorted.
     *          | if (isValidName(name) && isWritable())
     *          | then sortDiskItems()
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
                sortDiskItems();
            }
        } else {
            throw new DiskItemNotWritableException(this);
        }
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
     * @effect  If the directory is writable, and the directory doesn't contain itself,
     *          then the order of the contents of this directory is resorted lexicographically.
     *          | if (isWritable() && ! isDirectOrIndirectChildOf(this))
     *          | then sortDiskItems()
     * @effect  If this directory is writable, and not a child of itself,
     *          then the item's parent is set to this directory.
     *          | if (isWritable() && ! isDirectOrIndirectChildOf(this))
     *          | then setParent(this)
     * @effect  If the directory is writable and the directory doesn't contain itself,
     *          the modification time of the disk item is updated.
     *          | if (isWritable() && ! isDirectOrIndirectChildOf(this))
     *          | then setModificationTime()
     * @throws  DirectoryNotWritableException(this)
     *          This directory is not writable
     *          | ! isWritable()
     * @throws  DirectoryContainsSelfException(this)
     *          This directory contains itself
     *          | isDirectOrIndirectChildOf()
     */
    public void addItem(DiskItem item) throws DirectoryContainsSelfException, DirectoryNotWritableException {
        if (isWritable()) {
            if (item.isDirectOrIndirectChildOf(this)) {
                diskItems.add(item);
                this.sortDiskItems();
                item.setParent(this);
                setModificationTime();
            } else {
                throw new DirectoryContainsSelfException(this);
            }
        }
        else{
                throw new DirectoryNotWritableException(this);
        }

    }

    /**
     * Add a list of disk items to this directory.
     *
     * @param list
     *        The list of disk items to be added to this directory.
     */
    public void addList(ArrayList<DiskItem> list){
        for (DiskItem item: list){
            this.addItem(item);
        }
    }

    /**
     * Swap two items in the contents of this directory.
     *
     * @param index
     *        The index of the first disk item you want to swap.
     * @param otherIndex
     *        The index of the other disk item you want to swap with the first item.
     */
    private void swapItems(int index, int otherIndex) {
        DiskItem tmp1 = this.getItemAt(index);
        DiskItem tmp2 = this.getItemAt(otherIndex);
        diskItems.set(index, tmp2);
        diskItems.set(otherIndex, tmp1);
    }

    /**
     * Sort the disk items in lexicographical order.
     *
     * @effect  If the name of the first item lexicographically comes before the second,
     *          then the two items are swapped.
     *          | if (name1.compareToIgnoreCase(name2) > 0)
     *          | then swapItems(indexItem, indexItem+1)
     * @throws  DiskItemsHaveSameNameException
     *          Two items in this directory have the same name.
     *          | name1.compareToIgnoreCase(name2) == 0
     */
    public void sortDiskItems() throws DiskItemsHaveSameNameException {
        for (int pass = 0 ; pass < diskItems.size(); pass++){
            for (int indexItem = 0 ; indexItem < diskItems.size()-1 ; indexItem++) {
                DiskItem item1 = diskItems.get(indexItem);
                DiskItem item2 = diskItems.get(indexItem + 1);
                if (item1 != null && item2 != null) {
                    String name1 = item1.getName();
                    String name2 = item2.getName();
                    if (name1.compareToIgnoreCase(name2) == 0) {
                        throw new DiskItemsHaveSameNameException(item1, item2);
                    } else if (name1.compareToIgnoreCase(name2) > 0) {
                        swapItems(indexItem, indexItem+1);

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



    /**********************************************************
     * destructors
     **********************************************************/

    /**
     * Remove a given item from this directory.
     *
     * @param   item
     *          The given item to be removed from this directory.
     * @effect  If this directory is writable, and it contains the given item,
     *          then the item is removed from the contents of this directory.
     *          | if (isWritable() && diskItems.contains(item))
     *          | then diskItems.remove(item)
     * @effect  If this directory is writable, and it contains the given item,
     *          then the item's parent is set to null.
     *          | if (isWritable() && diskItems.contains(item))
     *          | then setParent(null)
     * @effect  If this directory is writable, and it contains the given item,
     *          then the modification time of this disk item is updated.
     *          | if (isWritable() && diskItems.contains(item))
     *          | then setModificationTime()
     * @throws  DirectoryNotWritableException
     *          This directory is not writable.
     *          | isWritable()
     */
    public void removeFile(File file) throws DirectoryNotWritableException {
        if (isWritable()) {
            if (diskItems.contains(file)) {
                diskItems.remove(file);
                file.setParent(null);
                setModificationTime();
            }
        }
        else{
            throw new DirectoryNotWritableException(this);
        }
    }

    /**
     * Remove a given directory.
     *
     * @param   dir
     *          The given dir to be removed.
     * @effect  If the given directory is writable and its contents are empty,
     *          the given directory is removed and the modification time of the
     *          parent directory is set to the current time.
     *          | if (isWritable() && getDiskItems() == null)
     *          | then parent.setModificationTime()
     * @throws  DiskItemNotWritableException
     *          The given directory is not writable.
     *          | isWritable()
     * @throws  DirectoryNotEmptyException
     *          The given directory is not empty.
     *          | getDiskItems() != null
     */
    public void removeDir(Directory dir) {
        if (dir.isWritable()) {
            if (dir.getDiskItems() == null) {
                diskItems.remove(dir);
                Directory parent = dir.getParent();
                parent.setModificationTime();
            }
            else{
                throw new DirectoryNotEmptyException(dir);
            }
        }
        else {
            throw new DiskItemNotWritableException(dir);
        }
    }



    public void sortList()
    {
        // Sorting the list using lambda function
        this.diskItems.sort((a, b) -> a.getName().compareTo(b.getName()));
    }

}
