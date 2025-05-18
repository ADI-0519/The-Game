import java.util.Random;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
/**
 * The bishop is a robot in an adventure game whose main objective is to try reach the player.
 *
 * @author  Aditya Ranjan
 * @version 9.12.2024
 */
public class Bishop
{
    private boolean disabled;
    private String location;
    private HashMap <String,ArrayList> details;
    private Square theSquare;

    /**
     * Constructor for objects of class Bishop
     */
    public Bishop(Square theSquare)
    {
        this.disabled = false;
        this.location = locator(); 
        this.details = new HashMap<>();
        this.theSquare = theSquare;
    }
    
    /**
     * Randomly assigns a room.
     * @return room_id which is the id of the room.
     */
    public String locator(){
        Random random = new Random();
        int row_no = random.nextInt(5) + 1; 
        int col_no;
        if (row_no != 1){
            col_no = random.nextInt(5) + 1;
        }
        else{
            col_no = random.nextInt(4) + 2;
        }
        String room_id = String.valueOf(row_no) + String.valueOf(col_no);
        return room_id;
    }

    /**
     * Disables the robot.
     */
    public void disable()
    {   
        this.disabled = true;
    }
    
    /**
     * Senses the adjacent rooms, returning a hashmap which has directions to each room, and an arraylist
     * The arrayList contains: locked_status, player_presence, player_item, any other items in room
     * Example arrayList: [False, False, Key(obj), "Key", "Screwdriver"]
     * @return details (hashmap <String, ArrayList>)
     */
    public HashMap sensing(ArrayList <Items> items,Room playerRoom, Items player_item){
        
        Room currentRoom = theSquare.getRoom(location);
        HashMap doors = currentRoom.get_doors();
        this.details = new HashMap<>();
        
        Set <String> exits = new HashSet<>(doors.keySet());
        
        for (String exit: exits){
            ArrayList <Object> things = new ArrayList<>(); 
            Room nextRoom = currentRoom.getExit(exit);
            things.add(doors.get(exit));
            
            if (playerRoom.getId().equals(nextRoom.getId())){
                things.add(true);
            }
            else{
                things.add(false);
            }
            
            things.add(player_item);
            
            for (Items item:items){
                if (item.get_location().equals(nextRoom.getId())){
                    if (item instanceof Key){
                        things.add("Key");
                    }
                    else{
                        things.add("Screwdriver");
                    }
                }
            }

            details.put(exit,things);
            
        }
        return details;
        
    }
    
    /**
     * Acts based on the data (if not disabled) it is given using heuristics to help bias the bishop to choosing moves which are more favourable.
     * Sets heuristics to 0, and ensures bishop only acts if heuristic above 0. (better to do nothing than a worse action)
     * If all heuristics are 0, then it's biased to pick east since that is closer to room 55.
     * @param Hashmap info which is a hashmap from the sensing it has done.
     */
    
    public void act(HashMap info){
        
        if (disabled){
            return;
        }
        
        Set <String> keys = info.keySet();
        Double highest_heuristic = -Double.MAX_VALUE;
        String best_action = null;
        
        for (String direction: keys){
            ArrayList <Object> details = (ArrayList)info.get(direction);
            Integer length = details.size();
            double heuristic = 0;
            
            for (int i = 0; i<length;i++){
                if (i == 0){
                    if ((boolean)details.get(i)){
                        heuristic -= 1;
                    }
                }
                
                else if (i == 1){
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
        
        }
        
        Room bishopRoom = theSquare.getRoom(location);
        Room nextRoom = bishopRoom.getExit(best_action);
            
        if (highest_heuristic>=0 && nextRoom!=null && !(bishopRoom.get_status(best_action))){
            bishopRoom = nextRoom;
            location = bishopRoom.getId();
        }
        
    }
    
    /**
     * Returns whether or not the bishop is disabled.
     * @return disabled(boolean)
     */
    public boolean get_status(){
        return this.disabled;
    }
    
    
    /**
     * Returns the location of the robot.
     * @return location.
     */
    protected String get_location(){
        return this.location;
    }
    
    /**
     * Sets location of robot.
     * @param newlocation
     */
    protected void set_location(String newlocation){
        this.location = newlocation;
    }
    
    
}
