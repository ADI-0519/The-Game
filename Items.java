import java.util.Random;
/**
 * Class items - an item in an adventure game.
 *
 * An item is either a key or screwdriver that is located randomly in one of the rooms.
 * 
 * @author  Aditya Ranjan
 * @version 9.12.2024
 */
public class Items
{
    private String location;
    private boolean dropped;

    /**
     * Constructor for objects of class Items.
     */
    public Items()
    {
        this.location = placeItem();
        this.dropped = true;
    }

    /**
     * Randomly assigns a location for the item.
     */
    public String placeItem()
    {
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
     * Gets the location of the item.
     * @ return String location of item
     */
    public String get_location(){
        return location;
    }
    
    /**
     * Gets the status of the item.
     * @ return boolean whether item is dropped or not;
     */
    public boolean get_status(){
        return dropped;
    }
    
    /**
     * Sets the location of the item.
     * @param new_location which is the new location of item.
     */
    
    public void setLocation(String new_location){
        this.location = new_location;
    }
    
    /**
     * Sets the status of the item.
     * @ param true if dropped, false otherwise.
     */
    public void set_status(boolean b){
        dropped = b;
    }
}
