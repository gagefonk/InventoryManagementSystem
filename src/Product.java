import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This is the Products Class
 */
public class Product {

    //DECLARE VARIABLES
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     */
    public Product(int id, String name, double price, int stock, int min, int max){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    //set methods
    /**
     * @param id to be set
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * @param name to be set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * @param price to be set
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * @param stock to be set
     */
    public void setStock(int stock){
        this.stock = stock;
    }

    /**
     * @param min to be set
     */
    public void setMin(int min){
        this.min = min;
    }

    /**
     * @param max to be set
     */
    public void setMax(int max){
        this.max = max;
    }

    //get methods
    /**
     * @return id
     */
    public int getId(){
        return id;
    }

    /**
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * @return price
     */
    public double getPrice(){
        return price;
    }

    /**
     * @return stock
     */
    public int getStock(){
        return stock;
    }

    /**
     * @return min
     */
    public int getMin(){
        return min;
    }

    /**
     * @return max
     */
    public int getMax(){
        return max;
    }

    //add part to arraylist
    /**
     * @param part to be added
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    //delete selected part return true, else return false
    /**
     * @param selectedAssociatedPart to be deleted
     * @return true/false
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        try{
            associatedParts.remove(selectedAssociatedPart);
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    //return array list of all parts
    /**
     * @return associated parts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

}
