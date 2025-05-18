import java.util.Scanner;

/**
 * This parser reads user input and tries to interpret it as an action. 
 * Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word action. It returns the action
 * as an object of class Action.
 *
 * The parser has a set of known action words. It checks user input against
 * the known actions, and if the input is not one of the known actions, it
 * returns a action object that is marked as an unknown action.
 * 
 * @author  Aditya Ranjan
 * @version 9.12.2024
 */
public class Parser 
{
    private ActionWords actions;  
    private Scanner reader;

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        actions = new ActionWords();
        reader = new Scanner(System.in);
    }

    /**
     * @return The next action from the user.
     */
    public Action getAction() 
    {
        String inputLine;   
        String word1 = null;
        String word2 = null;

        System.out.print("> ");     

        inputLine = reader.nextLine();

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
            word1 = tokenizer.next();      
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next();      
                // note: we just ignore the rest of the input line.
            }
        }

        if(actions.isAction(word1)) {
            return new Action(word1, word2);
        }
        else {
            return new Action(null, word2); 
        }
    }

    /**
     * Print out a list of valid action words.
     */
    public void showActions()
    {
        actions.showAll();
    }
}
