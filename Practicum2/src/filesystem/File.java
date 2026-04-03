package filesystem;

import be.kuleuven.cs.som.annotate.*;


/**
 * A class of files.
 *
 * @invar	Each file must have a properly spelled name.
 * 			| isValidName(getName())
 * @invar	Each file must have a valid size.
 * 			| isValidSize(getSize())
 * @invar   Each file must have a valid creation time.
 *          | isValidCreationTime(getCreationTime())
 * @invar   Each file must have a valid modification time.
 *          | canHaveAsModificationTime(getModificationTime())
 * @invar   Each file must have a valid parent directory.
 *          | hasValidParentDir()
 * @invar   Each file must have a valid extension.
 *          | isValidExtension()
 *
 * @author  Adelina Vozianu
 * @author  Boglárka Csorba-Vitus
 * @author  Lander Werbrouck
 * @version 2.0
 */
public class File extends DiskItem {

    /**********************************************************
     * Constructors
     **********************************************************/

    /**
     * Initialize a new file with given parent, name, size, writability and filetype.
     *
     * @param   dir
     *          The directory containing the new file
     * @param  	name
     *        	The name of new file
     * @param  	writable
     *         	The writability of the new file.
     * @param  	size
     *         	The size of the new file.
     * @param  	type
     *         	The FileType of the new file.
     * @effect	The size is set to the given size (must be valid)
     * 			| setSize(size)
     * @post    The filetype is the given filetype
     *          | new.getFileType() == type
     */
    @Raw
    public File(Directory dir, String name, int size, boolean writable, FileType type) {
        super(dir, name, writable);
        setSize(size);
        this.fileType = type;
    }

    /**
     * Initialize a new file with given given parent, name, size, writability and filetype.
     * @param   dir
     *          The directory containing the new file
     * @param  	name
     *        	The name of new file
     * @param  	type
     *         	The FileType of the new file.
     * @effect  This new file is initialized with the given dir, name, a zero size
     * 	        true writability and the given filetype
     *         | this(dir, name, 0, true, type)
     */
    @Raw
    public File(Directory dir, String name, FileType type)  {
        this(dir, name, 0, true, type);
    }


    /**********************************************************
     * file type
     **********************************************************/

    /**
     * Variable registering the extension of a file.
     */
    public final FileType fileType;

    protected boolean isValidFileType() {
        return fileType != null;
    }

    public FileType getFileType() {
        return this.fileType;
    }

    /**********************************************************
     * name
     **********************************************************/

    /**
     * Check whether the given name is a legal name for a file.
     *
     * @param  	name
     *			The name to be checked
     * @return	True if the given string is effective, not
     * 			empty and consisting only of letters, digits,
     * 			hyphens and underscores; false otherwise.
     * 			| result ==
     * 			|	(name != null) && name.matches("[a-zA-Z_0-9-].+")
     */
    public static boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z_0-9-].+"));
    }

    /**********************************************************
     * size - nominal programming
     **********************************************************/

    /**
     * Variable registering the size of this file (in bytes).
     */
    private int size = 0;

    /**
     * Variable registering the maximum size of any file (in bytes).
     */
    private static final int maximumSize = Integer.MAX_VALUE;


    /**
     * Return the size of this file (in bytes).
     */
    @Raw @Basic
    public int getSize() {
        return size;
    }

    /**
     * Set the size of this file to the given size.
     *
     * @param  size
     *         The new size for this file.
     * @pre    The given size must be legal.
     *         | isValidSize(size)
     * @post   The given size is registered as the size of this file.
     *         | new.getSize() == size
     */
    @Raw @Model
    protected void setSize(int size) {
        this.size = size;
    }

    /**
     * Return the maximum fil size.
     */
    @Basic @Immutable
    public static int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Check whether the given size is a valid size for a file.
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
     * Increases the size of this file with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this file
     *          must be increased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this file is increased with the given delta.
     *          | changeSize(delta)
     */
    public void enlarge(int delta) throws DiskItemNotWritableException {
        changeSize(delta);
    }

    /**
     * Decreases the size of this file with the given delta.
     *
     * @param   delta
     *          The amount of bytes by which the size of this file
     *          must be decreased.
     * @pre     The given delta must be strictly positive.
     *          | delta > 0
     * @effect  The size of this file is decreased with the given delta.
     *          | changeSize(-delta)
     */
    public void shorten(int delta) throws DiskItemNotWritableException {
        changeSize(-delta);
    }

    /**
     * Change the size of this file with the given delta.
     *
     * @param  delta
     *         The amount of bytes by which the size of this file
     *         must be increased or decreased.
     * @pre    The given delta must not be 0
     *         | delta != 0
     * @effect The size of this file is adapted with the given delta.
     *         | setSize(getSize()+delta)
     * @effect The modification time is updated.
     *         | setModificationTime()
     * @throws DiskItemNotWritableException(this)
     *         This file is not writable.
     *         | ! isWritable()
     */
    @Model
    private void changeSize(int delta) throws DiskItemNotWritableException{
        if (isWritable()) {
            setSize(getSize()+delta);
            setModificationTime();
        }else{
            throw new DiskItemNotWritableException(this);
        }
    }

    /**********************************************************
     * destructors
     **********************************************************/

    /**
     * Remove a given directory.
     *
     * @param   file
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
    public void destructorFile(File file) {
        if (file.isWritable()) {
            Directory parent = file.getParent();
            parent.removeItem(file);
            parent.setModificationTime();
            }
        else {
            throw new DiskItemNotWritableException(file);
        }
    }



}
