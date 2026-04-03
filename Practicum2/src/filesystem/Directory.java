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
 * @invar   Each directory must have a valid parent directory, unless the
 *          directory is a root directory.
 *          | if !isRoot()
 *          | then hasValidParentDir()
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

    /**
     * Initialize a directory with given parent, name and writability.
     *
     * @param   parent
     *          The directory containing the directory
     * @param  	name
     *        	The name of the directory
     * @param  	writable
     *         	The writability of the directory.
     */
    public Directory(Directory parent, String name, boolean writable){
        super(parent, name, writable);
    }

    /**
     * Initialize a directory with given parent, name and true writability.
     *
     * @param   parent
     *          The directory containing the directory
     * @param  	name
     *        	The name of the directory
     * @effect  This new file is initialized with the given parent, name and
     * 	        true writability
     *         | this(dir, name, true)
     */
    public Directory(Directory parent, String name){
        this(parent, name,true);
    }

    /**
     * Initialize a directory with parent null, given name and writability.
     *
     * @param  	name
     *        	The name of the directory
     * @param  	writable
     *         	The writability of the directory.
     * @effect  This new file is initialized with parent null, the given name and writability
     *         | this(null, name, writable)
     */
    public Directory(String name, boolean writable){
        this(null, name,writable);
    }

    /**
     * Initialize a directory with parent null, given name and true writability.
     *
     * @param  	name
     *        	The name of the directory
     * @effect  This new file is initialized with the given name, parent null and
     * 	        true writability
     *         | this(null, name, true)
     */
    public Directory(String name){
        this(null, name,true);
    }



    /**********************************************************
     * name - total programming
     **********************************************************/

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
     * @effect  If the name is valid and the directory is writable, the order of the items
     *          in this directory is resorted.
     *          | if (isValidName(name) && isWritable())
     *          | then sortDiskItems()
     * @throws  DiskItemNotWritableException(this)
     *          This directory is not writable
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
    @Raw @Basic
    public int getSize() {
        int sum = 0;
        for(PrimitiveDiskItem item: diskItems){
            if (item instanceof File) {
                int size = ((File) item).getSize();
                sum += size;
            }
        }
        return sum;
    }



    /**********************************************************
     * disk items - defensive programming
     **********************************************************/

    /**
     * Variable referring to the list of disk items inside a directory.
     */
    private final ArrayList<PrimitiveDiskItem> diskItems = new ArrayList<>();

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
     *        The given position starting from 1.
     */
    public PrimitiveDiskItem getItemAt(int position) {
        return this.diskItems.get(position-1);
    }

    /**
     * Return the disk item with the given name, if the disk item
     * is inside this directory.
     *
     * @param   nameItem
     *          The name of the disk item to be returned.
     * @throws  DiskItemNotInDirectoryException
     *          The disk item with the given name is not inside this directory.
     *          | ! containsDiskItemWithName(nameItem)
     * @return  item
     *          The disk item with the given name which is
     *          inside this directory.
     */
    public PrimitiveDiskItem getItem(String nameItem) {
        for (PrimitiveDiskItem item : diskItems) {
            if (item.getName().equals(nameItem)) {
                return item;
            }
        }
        return null;
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
        return (diskItems.contains(this.getItem(name)));
    }

    /**
     * Return the index in the directory of the given disk item.
     * Index starting from 1.
     * 
     * @param item
     *        The given disk item.
     */
    public int getIndex(DiskItem item) {
        return diskItems.indexOf(item) + 1;
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
        return (diskItems.contains(item));
    }

    /**
     * Adds a given disk item to this directory.
     *
     * @param	item
     * 			The item to be added to this directory.
     *
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
     * @throws  DiskItemNotWritableException(this)
     *          This directory is not writable
     *          | ! isWritable()
     * @throws  DirectoryContainsSelfException(this)
     *          This directory contains itself
     *          | isDirectOrIndirectChildOf()
     */
    public void addItem(PrimitiveDiskItem item) throws DirectoryContainsSelfException, DiskItemNotWritableException {
        if (this.isWritable()) {
            if (!this.isDirectOrIndirectChildOf(this)) {
                item.setParent(this);
                this.sortDiskItems();
                setModificationTime();
            } else {
                throw new DirectoryContainsSelfException(this);
            }
        }
        else{
                throw new DiskItemNotWritableException(this);
        }

    }

    /**
     * Remove a given item.
     *
     * @param   item
     *          The given file to be removed from its parent directory.
     * @effect  If the parent directory is writable, then the item is removed
     *          from the contents of the directory.
     *          | if (file.getParent().isWritable() )
     *          | then diskItems.remove(item)
     * @effect  If this directory is writable, and it contains the given item,
     *          then the modification time of this disk item is updated.
     *          | if (isWritable() && diskItems.contains(item))
     *          | then setModificationTime()
     * @throws  DiskItemNotWritableException
     *          This parent directory is not writable.
     *          | file.getParent().isWritable()
     */
    public void removeItem(PrimitiveDiskItem item) throws DiskItemNotWritableException {
        Directory parent = item.getParent();
        if (parent.isWritable()) {
            parent.getDiskItems().remove(item);
            setModificationTime();
        }
        else{
            throw new DiskItemNotWritableException(parent);
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
    @Model
    private void swapItems(int index, int otherIndex) {
        PrimitiveDiskItem tmp1 = this.getItemAt(index+1);
        PrimitiveDiskItem tmp2 = this.getItemAt(otherIndex+1);
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
    protected void sortDiskItems() throws DiskItemsHaveSameNameException {
        for (int pass = 0 ; pass < diskItems.size(); pass++){
            for (int indexItem = 0 ; indexItem < diskItems.size()-1 ; indexItem++) {
                PrimitiveDiskItem item1 = diskItems.get(indexItem);
                PrimitiveDiskItem item2 = diskItems.get(indexItem + 1);
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
    public ArrayList<PrimitiveDiskItem> getDiskItems() {
        return diskItems;
    }



    /**********************************************************
     * root
     **********************************************************/

    /**
     * Check whether this directory is a root directory.
     *
     * @return True if this directory has no parents, false otherwise.
     *         | result == (this.getParent() == null)
     */
    public boolean isRoot() {
        return this.getParent() == null;
    }

    public void makeRoot(){
        Directory parent = this.getParent();
        parent.removeItem(this);
        this.setParent(null);
    }



    /**********************************************************
     * parent
     **********************************************************/

    /**
     * Check if the given directory has a valid parent directory,
     * i.e. not a null parent, unless the given directory is a root directory.
     *
     * @param dir
     *        The given directory of which its parent is to be checked.
     * @return True is the given directory is not a root directory and its parent
     *         is not equal to a null pointer, false if the parent is a null pointer.
     *         | if !dir.isRoot()
     *         | then result == dir.getParent() != null.
     */
    @Override
    protected boolean hasValidParentDir(Directory dir) {
        if (!dir.isRoot()) {
            return dir.getParent() != null;
        }
        return true;
    }



    /**********************************************************
     * destructors
     **********************************************************/

    /**
     * Remove a given directory.
     *
     * @param   dir
     *          The given directory to be removed.
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
    public void destructorDir(Directory dir) {
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

}
