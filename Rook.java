import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
/**
 * Inherits from the Bishop class, and is a rook in the game. It is able to lock/unlock doors and move rooms.
 *
 * @author  Aditya Ranjan
 * @version 9.12.2024
 */
public class Rook extends Bishop
{
    private Room currentRoom;
    /**
     * Constructor for objects of class Rook
     */
    public Rook(Square theSquare)
    {
        super(theSquare);
        this.currentRoom = theSquare.getRoom(super.get_location());
    }
    
    /**
     * Used to facilitate locking/unlocking doors. (given a direction, it reverses it)
     * @param direction Takes a given direction
     * @return opposite: the opposite direction
     */
    public String opposite_direction(String direction){
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
     * Used to lock a given door
     * @param direction
     */
    public void lock(String direction){
        Room nextRoom = currentRoom.getExit(direction);
        nextRoom.set_status(opposite_direction(direction),true);
        currentRoom.set_status(direction,true);
    }

    /**
     * Used to unlock a given door
     * @param direction
     */
    public void unlock(String direction){
        Room nextRoom = currentRoom.getExit(direction);
        nextRoom.set_status(opposite_direction(direction),false);
        currentRoom.set_status(direction,false);
    }
    
    /**
     * The rook acts on the info given during the sweep. It uses the same movement patterns as the bishop, but is not affected by locked doors.
     * It locks the two closest doors to the exit by calculating their relevant manhattan distances.
     * It unlocks any other doors in an attempt to override player's locked doors and help let bishop through locked doors.
     * @param direction
     */
    public void act(HashMap info){
        if (super.get_status()){
            return;
        }
        
        Set <String> keys = info.keySet();
        Double highest_heuristic = -Double.MAX_VALUE;
        String best_action = null;
        int m1 = 9; // Set manh_dist to 9 since the largest possible manh_dist is from 11 to 55, which is 8.
        int m2 = 9;
        String d1 = null;
        String d2 = null;
        Integer manhattan_distance = 0;
        
        for (String direction: keys){
            ArrayList <Object> details = (ArrayList)info.get(direction);
            Integer length = details.size();
            double heuristic = 0;
            
            for (int i = 1; i<length;i++){
                if (i == 1){
                    if ((boolean)details.get(i)){
                        if ((Items)(details.get(i+1)) instanceof Screwdriver){
                            heuristic -= 2;
                        }
                        else{
                            heuristic += 5;
                        }  
                    }  
                }
            
                else if (i>2){
                    if (((String)(details.get(i))).equals("Key")){
                        heuristic += 0.5;   
                    }
                    else {
                        heuristic += 0.7;
                    }
                }
                
            }
            
            if (heuristic>highest_heuristic){
                highest_heuristic = heuristic;
                best_action = direction;
            }
    
            // reverse any locks the player has done!
            if (currentRoom.get_status(direction)){
                unlock(direction);
            }
            
            Room afterRoom = currentRoom.getExit(direction);
            Integer distance = Integer.parseInt("55") - Integer.parseInt(afterRoom.getId());
            manhattan_distance = (distance % 10) + ((Math.floorDiv(distance,10)) % 10);
            
            // gets the closest two possible rooms and locks their respective doors
            
            if (manhattan_distance<m1){
                m2 = m1;
                m1 = manhattan_distance;
                d2 = d1;
                d1 = direction;
            }
            else if (manhattan_distance<m2){
                m2 = manhattan_distance;
                d2 = direction;
            }
            
        }
        
        lock(d1);
        lock(d2);
        
        Room nextRoom = currentRoom.getExit(best_action);
        
        if (highest_heuristic >= 0 && nextRoom!=null){
            currentRoom = nextRoom;
            super.set_location(currentRoom.getId());
        }   
        
    }
    
}
