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
 *          | isValidParentDir()
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
     * Initialize a new file with given name, size and writability.
     *
     * @param  	name
     *         	The name of the new file.
     * @param  	size
     *         	The size of the new file.
     * @param  	writable
     *         	The writability of the new file.
     * @effect  The name of the file is set to the given name.
     * 			If the given name is not valid, a default name is set.
     *          | setName(name)
     * @effect	The size is set to the given size (must be valid)
     * 			| setSize(size)
     * @effect	The writability is set to the given flag
     * 			| setWritable(writable)
     * @post    The new creation time of this file is initialized to some time during
     *          constructor execution.
     *          | (new.getCreationTime().getTime() >= System.currentTimeMillis()) &&
     *          | (new.getCreationTime().getTime() <= (new System).currentTimeMillis())
     * @post    The new file has no time of last modification.
     *          | new.getModificationTime() == null
     *
     * @note	The constructor is annotated raw because at the start of the execution, not all fields are
     * 			defaulted to a value that is accepted by the invariants.
     * 			E.g. the name is defaulted to null, which is not allowed,
     * 			thus the object is in a raw state upon entry of the constructor.
     */
    @Raw
    public File(Directory dir, String name, int size, boolean writable, FileType type) {
        super(dir, name);
        setSize(size);
        setWritable(writable);
        this.fileType = type;
    }

    @Raw
    public File(Directory dir, String name, int size, boolean writable) {
        this(dir, name, size, writable, FileType.TXT)
    }

    @Raw
    public File(String name) {
        super(name);
        fileType = FileType.TXT;
    }


    /**
     * Initialize a new file with given name.
     *
     * @param   name
     *          The name of the new file.
     * @effect  This new file is initialized with the given name, a zero size
     *          and true writability.
     *         | this(name,0,true)
     */
    @Raw
    public File(String name, FileType fileType) {
        //dir?
        super(name);
        setSize(0);
        setWritable(true);
        this.fileType = fileType;
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
     * name - total programming
     **********************************************************/

    /**
     * Check whether the given name is a legal name for a disk item.
     *
     * @param  	name
     *			The name to be checked
     * @return	True if the given string is effective, not
     * 			empty and consisting only of letters, digits, dots,
     * 			hyphens and underscores; false otherwise.
     * 			| result ==
     * 			|	(name != null) && name.matches("[a-zA-Z_0-9.-]+")
     */
    public static boolean isValidName(String name) {
        return (name != null && name.matches("[a-zA-Z_0-9.-]+"));
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
