/**
 * This class holds information about an action that was issued by the user.
 * An action currently consists of two strings: an action word and a second
 * word (for example, if the action was "pick-up screwdriver", then the two 
 * strings are "pick-up" and "screwdriver").
 * 
 * The way this is used is: Actions are already checked for being valid
 * action words. If the user entered an invalid action (a word that is not
 * known) then the action word is <null>.
 *
 * If the action had only one word, then the second word is <null>.
 * 
 * @author  Aditya Ranjan
 * @version 9.12.2024
 */

public class Action
{
    private String actionWord;
    private String secondWord;

    /**
     * Create a action object. First and second word must be supplied, but
     * either one (or both) can be null.
     * @param firstWord The first word of the action. Null if the action
     *                  was not recognised.
     * @param secondWord The second word of the action.
     */
    public Action(String firstWord, String secondWord)
    {
        actionWord = firstWord;
        this.secondWord = secondWord;
    }

    /**
     * Return the action word (the first word) of this action. If the
     * action was not understood, the result is null.
     * @return The action word.
     */
    public String getActionWord()
    {
        return actionWord;
    }

    /**
     * @return The second word of this action. Returns null if there was no
     * second word.
     */
    public String getSecondWord()
    {
        return secondWord;
    }

    /**
     * @return true if this action was not understood.
     */
    public boolean isUnknown()
    {
        return (actionWord == null);
    }

    /**
     * @return true if the action has a second word.
     */
    public boolean hasSecondWord()
    {
        return (secondWord != null);
    }
}

