import java.util.Set;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 *
 * A "Room" represents one location in the Square.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Aditya Ranjan
 * @version 9.12.2024
 */

public class Room 
{
    private String id;
    private HashMap <String, Room> exits;        // stores exits of this room.
    public HashMap <String, Boolean> doors;      // Stores the exits and whether or not they're locked.
    
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String id) 
    {
        this.id = id;
        exits = new HashMap<>();
        doors = new HashMap<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    /**
     * Sets a certain door to be locked or unlocked
     * @param direction The direction of the door
     * @param locked Whether it is locked or not.
     */
    
    public void set_status(String direction, boolean locked){
        doors.put(direction,locked);
    }
    
    /**
     * Returns status of a given door.
     * @param direction The direction of the door
     * @return Boolean of whether the door is locked or not.
     */
    public boolean get_status(String direction){
        return doors.get(direction);    
    }
    
    public HashMap get_doors(){
        return doors;
    }
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getId()
    {
        return id;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
}

