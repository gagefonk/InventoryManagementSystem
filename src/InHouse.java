/**
 * This is the class that pertains to the inhouse parts that extends the Part.java class
 */
public class InHouse extends Part{

    //private variable for inhouse machineID
    private int machineId;

    //constructor with inherited class and new private variable
    /**
     * InHouse Class Constructor
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param machineID
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineId = machineID;
    }

    //set method
    /**
     * @param machineId the id to be set
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    //get method
    /**
     * @return machineId
     */
    public int getMachineId(){
        return machineId;
    }

}
