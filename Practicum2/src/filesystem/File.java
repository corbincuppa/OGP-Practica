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
    public File(String name, int size, boolean writable, Extension extension) {
        super(name, size, writable);
        this.extension = extension;
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
    public File(String name, Extension extension) {
        super(name,0,true);
        this.extension = extension;
    }



    /**********************************************************
     * extension
     **********************************************************/

    /**
     * Variable registering the extension of a file.
     */
    public final Extension extension;


    /**
     * Return the extension of the file.
     */
    public String getExtension(){
        return this.extension.getExtension();
    }

}
