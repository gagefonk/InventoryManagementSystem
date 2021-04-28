import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This is the class that pertains to the Inventory system and logic
 */
public class Inventory {

    //declare variables
    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //add part
    /**
     * @param newPart to be added
     */
    public void addPart(Part newPart){
        allParts.add(newPart);
    }

    //add product
    /**
     * @param newProduct to be added
     */
    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    //lookup part id
    /**
     * @param search
     * @return part from search
     */
    public Part lookupPart(String search){
        for(Part part : allParts){
            if(search == ""){
                return null;
            }else if (String.valueOf(part.getId()).contains(search) ) {
                return part;
            }else if(part.getName().toLowerCase().contains(search.toLowerCase())){
                return part;
            }
        }
        return null;
    }

    //lookup product
    /**
     * @param search
     * @return product from search
     */
    public Product lookupProduct(String search){
        for(Product product : allProducts){
            if(search == ""){
                return null;
            }else if (String.valueOf(product.getId()).contains(search) ) {
                return product;
            }else if(product.getName().toLowerCase().contains(search.toLowerCase())){
                return product;
            }
        }
        return null;
    }

    //update part
    /**
     * @param index of new part to be replaced
     * @param selectedPart new part
     */
    public void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    //update product
    /**
     * @param index of new product to be replaced
     * @param newProduct new product
     */
    public void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    //delete part
    /**
     * @param selectedPart to be deleted
     * @return true/false
     */
    public boolean deletePart(Part selectedPart){
        try{
            int index = allParts.indexOf(selectedPart);
            allParts.remove(index);
            for(Product p : allProducts){
                p.deleteAssociatedPart(selectedPart);
            }
            for (Part part : allParts){
                part.setId(allParts.indexOf(part)+1);
            }
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    //delete product
    /**
     * @param selectedProduct to be deleted
     * @return true/false
     */
    public boolean deleteProduct(Product selectedProduct){
        try{
            int index = allProducts.indexOf(selectedProduct);
            allProducts.remove(index);
            for (Product product : allProducts){
                product.setId(allParts.indexOf(product)+1);
            }
            return true;
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

    //get all parts
    /**
     * @return all parts
     */
    public ObservableList<Part> getAllParts(){
        return allParts;
    }

    //get all products
    /**
     * @return all products
     */
    public ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
