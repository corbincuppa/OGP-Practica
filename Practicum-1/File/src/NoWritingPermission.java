/**
 * A class of exceptions signaling no writing permission to rename or transform files.
 *
 * @version 1.0
 *
 * @author  Adelina Vozianu
 * @author	Boglarka Csorba-Vitus
 * @author	Lander Werbrouck
 */
public class NoWritingPermission extends RuntimeException{
    /**
     *
     * @param message
     */
    public NoWritingPermission(String message){
        super(message);
    }
}
