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
     * linkedItem
     *********************************************************/

     protected DiskItem linkedItem = null ;

     /**********************************************************
     * destructors
     **********************************************************/

}
