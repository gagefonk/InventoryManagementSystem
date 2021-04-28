/**
 * This is the class that pertains to the outsourced parts that extends the Part.java class
 */
public class Outsourced extends Part{
    //private variable for outsourced company name
    private String companyName;

    //constructor with inherited class and new private variable

    /**
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    //set method
    /**
     * @param companyName to be set
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    //get method
    /**
     * @return the company name
     */
    public String getCompanyName(){
        return companyName;
    }
}
