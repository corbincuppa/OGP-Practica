package filesystem;

public class Link extends PrimitiveDiskItem {
    /**********************************************************
     * constructor
     **********************************************************/

    /**
     * Initialize a directory with given parent, name and writability.
     *
     * @param   parent
     *          The directory containing the directory
     * @param  	name
     *        	The name of the directory
     * @param   linkedItem
     *          The disk item(s) the link is linked to
     */
    public Link(Directory parent, String name, DiskItem linkedItem) {
        super(parent, name);
        this.linkedItem = linkedItem;
        //setLinkedItem(linkedItem)
    }

    /**********************************************************
     * name
     **********************************************************/

    /**
     * Check whether the given name is a legal name for a link.
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
     * linkedItem
     *********************************************************/

     protected DiskItem linkedItem = null ;

     /**********************************************************
     * destructor
     **********************************************************/

    /**
     * Remove a given link.
     *
     * @effect  The given link is removed and the modification time of the
     *          parent directory is set to the current time.
     *          then parent.setModificationTime()
     */
    public void destructorLink() {
        Directory parent = this.getParent();
        parent.removeItem(this);
        parent.setModificationTime();
    }
}