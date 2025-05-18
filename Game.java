import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
/**
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the actions that the parser returns.
 * 
 * @author  Aditya Ranjan
 * @version 9.12.2024
 */

public class Game {
    private Parser parser;
    private Room currentRoom;
    private Square theSquare;
    private ArrayList <Items> items = new ArrayList<>(); // Stores the keys and screwdrivers.
    Items item_held;
    private Bishop bishop;
    private Rook rook;
    
    /**
     * Create the game, initialises its internal map, items and the robots.
     */
    public Game() 
    {
        parser = new Parser();   
        theSquare = new Square();
        currentRoom = theSquare.getRoom("11");
        
        Items item_held = null;
        Random rand = new Random();
        int no_key = rand.nextInt(4)+1; // amount between 1-4
        int no_screwdriver = rand.nextInt(4)+1;
        
        for (int i = 0;i<no_key;i++){
            this.items.add(new Key());
        }
        
        for (int j = 0;j<no_screwdriver;j++){
            this.items.add(new Screwdriver());
        } 
        
        bishop = new Bishop(theSquare);
        rook = new Rook(theSquare);
    }
    
    /**
     *  This is the main game loop.
     *  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        
        boolean finished = false;
        while (! finished) {
            Action action = parser.getAction();
            finished = processAction(action);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to The Square.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
    }
    

    /**
     * Given a action, process (that is: execute) the action.
     * @param action The action to be processed.
     * @return true If the action ends the game, false otherwise.
     */
    private boolean processAction(Action action) 
    {
        boolean wantToQuit = false;

        if(action.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String actionWord = action.getActionWord();
        
        if (actionWord.equals("help")) {
            printHelp();
            wantToQuit = agents_act();
        }
        else if (actionWord.equals("go")) {
            goRoom(action);
            wantToQuit = agents_act();
        }
        else if (actionWord.equals("quit")) {
            wantToQuit = quit(action);
        }
        else if (actionWord.equals("search")){
            search();
            wantToQuit = agents_act();
        }
        else if (actionWord.equals("pickup")){
            pickup(action);
            wantToQuit = agents_act();
        }
        else if (actionWord.equals("drop")){
            drop(action);
            wantToQuit = agents_act();
        }
        else if (actionWord.equals("lock")){
            lock(action);
            wantToQuit = agents_act();
        }
        else if (actionWord.equals("unlock")){
            unlock(action);
            wantToQuit = agents_act();
        }
        
        else if (actionWord.equals("map")){
            displayMap(action);
            wantToQuit = false;
        }
        
        return wantToQuit;
    }

    /**
     * Print out some help information.
     * Print some cryptic message and a list of the 
     * action words.
     */
    private void printHelp() 
    {
        System.out.println("You are trapped in The Square");
        System.out.println("and must find your way out.");
        System.out.println("Beware of Bishop & Rook.");        
        System.out.println();
        System.out.println("Your action words are:");
        parser.showActions();
        System.out.println("You are in room id " + currentRoom.getId());
    }
    
    /**
     * Used to enable the rook and bishop to act after the player's turn. 
     * Note: The player's winning condition is checked before the agents act.
     * The checking of whether bishop/rook is in the player room both before and after the agents
     * move to make sure that the player is able to disable the bishop/rook instead of the bishop/rook moving away.
     * @return true If the game comes to an end, false otherwise.
     */
    private boolean agents_act(){
        if (currentRoom.getId().equals("55")){
                System.out.println("You won");
                return true;
        }
        
        if (bishop.get_location().equals(currentRoom.getId())){
            if (item_held instanceof Screwdriver){
                bishop.disable();
                System.out.println("Bishop disabled");
            }
            else{
                System.out.println("The bishop found you! Game over" );
                return true;
            }
        }
        
        if (rook.get_location().equals(currentRoom.getId()) && item_held instanceof Screwdriver){
            rook.disable();
            System.out.println("Rook disabled");
        }
        
        
        HashMap bishop_hashmap = bishop.sensing(items, currentRoom, item_held);
        HashMap rook_hashmap = rook.sensing(items,currentRoom,item_held);
        bishop.act(bishop_hashmap);
        rook.act(rook_hashmap);
        
        
        if (bishop.get_location().equals(currentRoom.getId())){
            if (item_held instanceof Screwdriver){
                bishop.disable();
                System.out.println("Bishop disabled");
            }
            else{
                System.out.println("The bishop found you! Game over" );
                return true;
            }
        }
        
        if (rook.get_location().equals(currentRoom.getId()) && item_held instanceof Screwdriver){
            rook.disable();
            System.out.println("Rook disabled");
        }
        
        return false;
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Action action) 
    {
        if(!action.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = action.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);
        

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else if (!(currentRoom.get_status(direction))) {
            currentRoom = nextRoom;
            System.out.println("You are in room " + currentRoom.getId());
        }
        else{
            System.out.println("Door locked!");
        }
    
    }
    
    
    /**
     * Used to display the map to the player. 
     * @param action Used to check if the correct input has been provided.
     */
    private void displayMap(Action action){
        if(action.hasSecondWord()) {
            System.out.println("Incorrect action");
            return;
        }

        for (int y = theSquare.getHeight();y>0;y--){
            for (int x = 1; x <= theSquare.getWidth();x++){
                if (theSquare.isValid(x,y)){
                    System.out.print("---");
                    String room_id = String.valueOf(x)+String.valueOf(y);
                
                    if (theSquare.getRoom(room_id).getExit("north")!=null){
                        if (theSquare.getRoom(room_id).get_status("north")){
                            System.out.print("X--");
                        }
                        else{
                            System.out.print(" --");
                        }
                    }
                    else{
                        System.out.print("---");
                    }  
                }

            }
            System.out.println("-");
        
            for (int x = 1; x <= theSquare.getWidth();x++){
                System.out.print("|");
                String room_id = String.valueOf(x)+String.valueOf(y);
                
                if (room_id.equals(bishop.get_location())){
                    System.out.print("B");
                }
                else{
                    System.out.print(" ");
                }
                System.out.print("    ");
            }
            System.out.println("|");
        
            for (int x = 1; x <= theSquare.getWidth();x++){
                String room_id = String.valueOf(x)+String.valueOf(y);
                
                if (theSquare.isValid(x,y)){
                    if (theSquare.getRoom(room_id).getExit("west")!=null){
                        if (theSquare.getRoom(room_id).get_status("west")){
                            System.out.print("X  ");
                        }
                        else{
                            System.out.print("   ");
                        }
                    }
                    else{
                        System.out.print("|  ");
                    }
                    
                    if (room_id.equals(currentRoom.getId())){
                        System.out.print("P");
                    }
                    else{
                        System.out.print(" ");
                    }
                    
                    System.out.print("  ");
                }
            }
            System.out.println("|");
            
            for (int x = 1; x <= theSquare.getWidth();x++){
                    System.out.print("|    ");
                    String room_id = String.valueOf(x)+String.valueOf(y);
                
                    if (room_id.equals(rook.get_location())){
                        System.out.print("R");
                    }
                    else{
                        System.out.print(" ");
                    }
            }
            System.out.println("|");
            
        
        }
        
        for (int x = 1; x<=theSquare.getWidth();x++){
            System.out.print("------");
        }
        
        System.out.println("-");
        
        return;
    }
    
    /**
     * Used to search through the room, giving info on what items/robots are in the room. 
     */
    private void search(){
        String room_id = currentRoom.getId();
        boolean flag = true;
        
        for (Items item:items){
            if (item.get_location().equals(room_id)){
                if (item instanceof Key){
                    System.out.println("You found a key" );
                }
                else{
                    System.out.println("You found a screwdriver" );
                }
                flag = false;
            }
        }
        
        if (bishop.get_location().equals(room_id)){
            System.out.println("Bishop in room!" );
            flag = false;
        }
        
        if (rook.get_location().equals(room_id)){
            System.out.println("Rook in room!" );
            flag = false;
        }
        
        if (flag){
            System.out.println("Nothing found");
        }
    }
    
    /**
     * Used to pickup an item present in the room. 
     * @param action The action to be performed (item to be picked up).
     */
    private void pickup(Action action){
        if(!action.hasSecondWord()) {
            System.out.println("Pick up what?");
            return;
        }
        
        String type = action.getSecondWord();
        
        boolean flag = true;
        
        for (Items item: items){
            if (item.get_location().equals(currentRoom.getId()) && type.equals("key")){
                if (item instanceof Key){
                    if (item_held == null){
                        System.out.println("You picked up a key" );
                        item_held = item;
                        item.set_status(false);
                        System.out.println(item_held); 
                    }
                    else{
                        System.out.println("You already have an item, drop it first!");
                    }
                    flag = false;
                }
            }
            else if (item.get_location().equals(currentRoom.getId()) && type.equals("screwdriver")){
                if (item instanceof Screwdriver){
                    if (item_held == null){
                        System.out.println("You picked up a screwdriver");
                        item.set_status(false);
                        item_held = item;
                        System.out.println(item_held);
                    }
                    else{
                        System.out.println("You already have an item, drop it first!");
                    }
                    flag = false;
                }
            }
            
        }
        
        if (flag){
            System.out.println("The item you chose is not in this room");
        }
        
    }
    
    /**
     * Used to drop an item in the room the player is in. 
     * @param action The action to be performed (item to be dropped).
     */
    private void drop(Action action){
        if(!action.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        }
        
        String type = action.getSecondWord();
        
        boolean flag = true;
        
        if (type.equals("key") && item_held instanceof Key){
            item_held.set_status(true);
            item_held.setLocation(currentRoom.getId());
            System.out.println("Key dropped");
            item_held = null;
        }
        else if (type.equals("screwdriver") && item_held instanceof Screwdriver){
            item_held.set_status(true);
            item_held.setLocation(currentRoom.getId());
            System.out.println("Screwdriver dropped");
            item_held = null;
        }
        else{
            System.out.println("Nothing to drop or dropping wrong item!");
        }
    }
    
    /**
     * Used to facilitate locking/unlocking doors. (given a direction, it reverses it)
     * @param direction Takes a given direction
     * @return opposite: the opposite direction
     */
    private String opposite_direction(String direction){
        String opposite=null;
        if (direction.equals("north")){
            opposite = "south";
        }
        else if (direction.equals("south")){
            opposite = "north";
        }
        else if (direction.equals("east")){
            opposite = "west";
        }
        else if (direction.equals("west")){
            opposite = "east";
        }
        return opposite;
    }
    
    /**
     * Locks a given door only if a player has a key.
     * @param action The action to be processed. (direction in this scenario)
     */
    private void lock(Action action){
        if(!action.hasSecondWord()) {
            System.out.println("Lock what?");
            return;
        }
        
        String direction = action.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);
        
        if (item_held instanceof Key){
            if (nextRoom == null) {
                System.out.println("There is no door!");
            }
            else {
                nextRoom.set_status(opposite_direction(direction),true);
                currentRoom.set_status(direction,true);
                System.out.println("Door locked");
            }
        }
        else{
            System.out.println("You don't have a key!");
        }
        
    }
    
    /**
     * Unlocks a given door only if the player has a key.
     * @param action The action to be processed. (direction in this scenario)
     */
    private void unlock (Action action){
        
        if(!action.hasSecondWord()) {
            System.out.println("Unlock what?");
            return;
        }
        String direction = action.getSecondWord();
        Room nextRoom = currentRoom.getExit(direction);
        
        if (item_held instanceof Key){
            if (nextRoom == null) {
                System.out.println("There is no door!");
            }
            else {
                nextRoom.set_status(opposite_direction(direction),false);
                currentRoom.set_status(direction,false);
                System.out.println("Door unlocked");
            }
        }
        else{
            System.out.println("You don't have a key!");
        }
        
    }

    /** 
     * "Quit" was entered. Check the rest of the action to see
     * whether we really quit the game.
     * @return true, if this action quits the game, false otherwise.
     */
    private boolean quit(Action action) 
    {
        if(action.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true; 
        }
    }
}
