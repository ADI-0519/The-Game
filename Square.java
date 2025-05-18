import java.util.HashMap;


/**
 * Used to initialise the internal map of the game.
 *
 * @author  Aditya Ranjan
 * @version 9.12.2024
 */
public class Square
{
    private HashMap<String, Room> theSquare;
    private int WIDTH = 5;
    private int HEIGHT = 5;

    public Square()
    {
        theSquare = new HashMap<>();
        String roomId;
        Room room;
        
        for (int x=1; x<=WIDTH; x++) {
            
            for (int y=1; y<=HEIGHT; y++) {
                roomId = x + "" + y;        
                
                if (!theSquare.containsKey(roomId))
                    theSquare.put(roomId, new Room(roomId));
                
                room = theSquare.get(roomId);    
                addExit(room, "east", x+1, y);
                addExit(room, "west", x-1, y);
                addExit(room, "north", x, y+1);
                addExit(room, "south", x, y-1);                
            }
        }
    }

    /**
     * Checks an x & y coordinate.  Returns true if the location is valid,
     * i.e., it's within the grid (default is 5x5).
     */
    public boolean isValid(int x, int y) {
        return x >= 1 && x <= WIDTH && y >= 1 && y <= HEIGHT;
    }
    
    /**
     * Will set the exit for a room if the exit should exists (that is, the room 
     * is in a valid location.
     */
    private void addExit(Room room, String direction, int x, int y) {
        
        if (isValid(x, y)) {
            String exitId = x + "" + y;   
            
            if (!theSquare.containsKey(exitId)) {
                theSquare.put(exitId, new Room(exitId));    
            }
            
            Room exit = theSquare.get(exitId);
            room.setExit(direction, exit); 
            room.set_status(direction,false);
            //System.out.println();
        }
    }
    
    /**
     * Given a room id, returns the room object associate with that id.
     * This method does not check if the roomId is valid.
     */
    public Room getRoom(String roomId) {
        return theSquare.get(roomId);
    }
    
    public int getWidth(){
        return WIDTH; 
    }
    
    public int getHeight(){
        return HEIGHT; 
    }
}
