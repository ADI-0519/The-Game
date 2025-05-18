/**  
 * This class holds an enumeration of all action words known to the game.
 * It is used to recognise actions as they are typed in.
 *
 * @author  Aditya Ranjan
 * @version 9.12.2024
 */

public class ActionWords
{
    // a constant array that holds all valid action words
    private static final String[] validActions = {
        "go", "quit", "help", "search", "pickup", "drop", "lock", "unlock", "map"
    };

    /**
     * Constructor - initialise the action words.
     */
    public ActionWords()
    {
        // nothing to do at the moment...
    }

    /**
     * Check whether a given String is a valid action word. 
     * @return true if it is, false if it isn't.
     */
    public boolean isAction(String aString)
    {
        for(int i = 0; i < validActions.length; i++) {
            if(validActions[i].equals(aString))
                return true;
        }
        
        return false;
    }

    /**
     * Print all valid action to System.out.
     */
    public void showAll() 
    {
        for(String action: validActions) {
            System.out.print(action + "  ");
        }
        System.out.println();
    }
}
