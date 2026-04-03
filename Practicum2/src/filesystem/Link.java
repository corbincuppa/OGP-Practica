package filesystem;

public class Link extends PrimitiveDiskItem {
    /**********************************************************
     * constructors
     **********************************************************/

    /*
     * @param parent
     * @param name
     * */
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
     * destructors
     **********************************************************/

}
