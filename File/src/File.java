import be.kuleuven.cs.som.annotate.*;

import java.util.Date;

/**
 * A class of files involving a file name, filesize, date of creation, date of when the file was last edited,
 * and write and read permissions.
 *
 * @author	Adelina Vozianu
 * @author	Boglarka Csorba-Vitus
 * @author	Lander Werbrouck
 *
 * @version 1.0
 *
 *
 * @invar	The name of a file must only be composed of capital letters "A-Z", lower case letters "a-z",
 *          periods ".", dashes "-" and underscores "_" and it must be at least one character long.
 * 			| isValidName(name)
 */
public class File {
    /**
     * Name of the file expressed as a String.
     */
    public String name;
    /**
     * Size of the file expressed as an integer number.
     */
    private int size;
    /**
     * Permission to make changes to the file expressed as a boolean.
     */
    private boolean writable;
    /**
     * The time of creation of the file expressed as XYZ.
     */
    private final Date creationTime;


    /**
     * Checks whether the given name is composed of only capital letters "A-Z" , lowercase letters "a-z", periods ".", dashes
     * "-" or underscores "_".
     *
     * @param name
     *        The name to be checked.
     * @return True if the name is only composed of the aforementioned characters, and it contains at least
     *         one character.
     *         False if it contains not supported characters or the length of the given name is less than
     *         one character.
     *         | result ==
     *         |            name.matches("[A-Za-z0-9._-]+") && name.length() > 1
     */
    public boolean isValidName(String name) {

        return name.matches("[A-Za-z0-9._-]+") && name.length() > 1;
    }

    /**
     * Replaces all characters which are not letters "A-Z" , "a-z", periods ".", dashes
     * "-" or underscores "_" with underscores.
     *
     * @param name
     *        The name to be made valid.
     * @return The given name, but all forbidden characters are replaced with underscores.
     *         | result ==
     *         |           name.replaceAll("[^A-Za-z0-9._-]", "_")
     */
    public String correctionName(String name) {

        return name.replaceAll("[^A-Za-z0-9._-]", "_");
    }

    /**
     * Replaces the old filename with the new given name.
     *
     * @param name
     *        The new given name to replace the old file name.
     * @pre  The write permission of the file must be set to true.
     *       | if (writable == true) {
     * @pre  Name must contain supported characters, otherwise those characters will be
     *       replaced with "_". The given name must also contain at least one character.
     *       | if ((!isValidName(name)) && name.length() > 1) {
     *             name = correctionName(name);
     * @exception NoWritingPermission
     *            The file is not writable and therefore cannot be renamed.
     *            | (writable == false)
     * @post The new name of this file is equal to the given name, but unsupported characters are replaced to supported
     *       characters.
     *       | this.name = name
     */
    public void rename(String name) {
        if (!writable ) {
            throw new NoWritingPermission("File must be writable in order to rename it.");
        }
        else{ if ((!isValidName(name)) && name.length() > 1) {
            name = correctionName(name);
        }}
            this.name = name;{
        }
    }

    /**
     * Gives the current name of the file.
     *
     * @return Current name of the file.
     *         | result == name
     */
    public String getName() {
        return name;
    }

    /**
     * The max file size is the largest possible number in Java.
     */
    private static final int max_filesize = 1 << 30; // 2^30

    /**
     * Enlarges the filesize by a given amount.
     *
     * @param amount
     *        Amount of bytes with which the filesize shall be enlarged.
     *
     * @pre Amount is not 0.
     *      | amount > 0
     * @pre Sum between current size and amount is lesser than or equal to the maximum filesize (2^30).
     *      | this.size + amount <= max_filesize
     * @pre File must have permission to be writable.
     *      | writable == true
     * @exception NoWritingPermission
     *            The file is not writable and therefore cannot be enlarged.
     *            | (writable == false)
     * @post The new filesize is equal to the old filesize incremented with the given amount of bytes.
     */
    public void enlarge(int amount) {
        if (!writable) {
            throw new NoWritingPermission("File must be writable in order to enlarge it.");
        }
        else{if (amount > 0 && (this.size + amount) <= max_filesize) {
            this.size += amount;
        }}
    }

    /**
     * Shortens the filesize by a given amount.
     *
     * @param amount
     *        Amount of bytes with which the current filesize will be shortened.
     *
     * @pre Amount is not 0.
     *      | amount > 0
     * @pre Difference between current size and amount is greater than or equal to 0.
     *      | this.size - amount >= 0
     * @pre File must have permission to be writable.
     *      | writable == true
     * @exception NoWritingPermission
     *            The file is not writable and therefore cannot be shortened.
     *            | (writable == false)
     * @post The new filesize is equal to the old filesize decremented with the given amount of bytes.
     *      | this.size -= amount
     */
    public void shorten(int amount) {
        if (!writable){
            throw new NoWritingPermission("File must be writable in order to shorten it.");
        }
        else {
            if (amount > 0 && this.size - amount >= 0) {
                this.size -= amount;
            }
        }
    }

    /**
     * Gets the current filesize (in bytes).
     *
     * @return The current filesize.
     *         | result == size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets the current filesize to given filesize.
     *
     * @param size
     *        New filesize to replace old filesize.
     * @post New filesize is equal to given filesize.
     *       | this.size == size
     */
    private void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets the writable of this file
     *
     * @return The writable of this file
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * Sets the current writable to given writable
     *
     * @param writable
     *        The value of writable determines whether or not the file can be modified
     *
     * @post
     * The writable of this file is the given writable
     */
    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    /**
     *Initialize a new file with given name, size, writable and current time
     *
     * @param name
     *        The name of the file
     * @param size
     *        The size of the file
     * @param writable
     *        The value of writable determines whether or not the file can be modified
     * @effect
     * A new file is initialized with given name, size, writable and current time
     *
     */
    public File(String name, int size, boolean writable) {
        this.name = this.correctionName(name);
        this.size = size;
        this.writable = writable;
        this.creationTime = new Date();
    }

     /**
     * Initialize a new file with given name, size = 0, writable = true and current time
     *
     * @param name
     *        The name of the file
     *
     * @effect
     * A new file is initialized with given name, size = 0, writable = true and current time
     */
    public File(String name) {
        this.name = this.correctionName(name);
        this.size = 0;
        this.writable = true;
        this.creationTime = new Date();
    }
}