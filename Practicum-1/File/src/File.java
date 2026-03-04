import be.kuleuven.cs.som.annotate.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
     * The time the files last modification expressed as XYZ
     *
     */
    private Date modificationTime;
    /**
     *
     */
    private int usagePeriod;
    /**
     * A variable to determine the value of the dates in Ms os we can compare it with other values
     */
    private long creationTimeMs;
    private long modificationTimeMs;
    private boolean overlappingUsePeriod;


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
            this.modificationTime = new Date();
            this.modificationTimeMs = this.modificationTime.getTime();
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
                this.modificationTime = new Date();
                this.modificationTimeMs = this.modificationTime.getTime();
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
     * This function calculates the usageperiod of the file
     * @pre the modification time can not equal null (no check for negative values since
     *      it is impossible to get a negative modification time or creation time)
     *      | modificationTime != 0
     * @return the current usagePeriod
     *          | result == usagePeriod
     */
    private long CalculateUsageperiod(long usagePeriod) {
        if (this.modificationTime != null) {
            usagePeriod = modificationTimeMs - creationTimeMs;
            long days = TimeUnit.MILLISECONDS.toDays(usagePeriod);
            long hours = TimeUnit.MILLISECONDS.toHours(usagePeriod);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(usagePeriod);


        }
        return usagePeriod;
    }
    /**
     * This function checks if two files have an overlapping usage period
     * @param other
     *      the other file we need to compare with
     *
     * @pre the modification time has to exist
     *      | this.modificationTime != null
     * @pre the other file also has to have a modification time
     *      | other.modificationTime != null
     * @return this function returns a boolean called overlappingUsePeriod
     *      | result == overlappingUsePeriod
     */
    public boolean hasOverlappingUsePeriod(File other){
        if(this.modificationTime != null && this.modificationTimeMs > 0 && other.modificationTime != null && other.modificationTimeMs > 0){
            if(this.creationTime.before(other.creationTime) && this.modificationTime.before(other.modificationTime)){
                this.overlappingUsePeriod = true;
            }
            else if(other.creationTime.before(this.creationTime) && other.modificationTime.before(this.modificationTime)) {
                this.overlappingUsePeriod = true;
            }
            else this.overlappingUsePeriod = false;
        }

        return overlappingUsePeriod;
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
     * Gets the writable permission of this file.
     *
     * @return The writable permission of this file.
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * Sets the current writable to given writable.
     *
     * @param writable
     *        The value of writable determines whether the file can be modified.
     *
     * @post
     * The writability of this file is the given writable.
     */
    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    /**
     * Gets creation time of the file.
     *
     * @return Creation time of the file.
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Gets modification time of the file.
     *
     * @return Modification time of the file.
     */
    public Date getModificationTime() {
        return modificationTime;
    }

    /**
     * Gets creation time in milliseconds.
     * @return Creation time in milliseconds.
     */
    public long getCreationTimeMs() {
        return creationTimeMs;
    }

    /**
     * Gets modification time in milliseconds.
     * @return Modification time in milliseconds.
     */
    public long getModificationTimeMs() {
        return modificationTimeMs;
    }

    /**
     * Checks if there in overlapping use period.
     * @return True if there is overlapping use period.
     */
    public boolean isOverlappingUsePeriod() {
        return overlappingUsePeriod;
    }

    public int getUsagePeriod() {
        return usagePeriod;
    }

    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }



    /**
     * Initializes a new file with given name, size, writable and current time.
     *
     * @param name
     *        The name of the file
     * @param size
     *        The size of the file
     * @param writable
     *        The value of writable determines whether the file can be modified.
     * @effect
     * A new file is initialized with given name, size, writable and current time.
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
     *        The name of the file.
     *
     * @effect
     * A new file is initialized with given name, size = 0, writable = true and current time.
     */
    public File(String name) {
        this.name = this.correctionName(name);
        this.size = 0;
        this.writable = true;
        this.creationTime = new Date();
    }
}