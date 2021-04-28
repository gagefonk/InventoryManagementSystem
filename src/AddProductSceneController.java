import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * CLASS RESPONSIBLE FOR ADDING A PRODUCT
 */
public class AddProductSceneController implements Initializable {

    //DECLARE VARIABLES
    @FXML private TextField idTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField inventoryTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;
    @FXML private TextField mainPartTextField;
    @FXML private TableColumn partTableId;
    @FXML private TableColumn partTableName;
    @FXML private TableColumn partTableInventory;
    @FXML private TableColumn partTablePrice;
    @FXML private TableColumn associatedPartTableId;
    @FXML private TableColumn associatedPartTableName;
    @FXML private TableColumn associatedPartTableInventory;
    @FXML private TableColumn associatedPartTablePrice;
    @FXML private TableView partTableView;
    @FXML private TableView associatedPartTableView;
    @FXML private Button addButton;
    @FXML private Button removeAssociatedPartButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    MainController mainController;
    Inventory inventory;
    Product tempProduct = new Product(1, "", 0,0,0,0);


    //BUTTON ACTIONS
    /**
     * Event Handler for adding part to associated parts list
     */
    EventHandler<ActionEvent> addPart = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            addAssociatedPart();
        }
    };
    /**
     * Event Handler for removing associated part
     */
    EventHandler<ActionEvent> removePart = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            deletePart();
        }
    };
    /**
     * Event Handler for saving the product
     */
    EventHandler<ActionEvent> saveProduct = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                addProduct();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * Event Handler for closing the window
     */
    EventHandler<ActionEvent> closeWindow = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                closeProductAddScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    //UNIVERSAL METHODS

    /**
     * Close Window
     * @throws IOException
     */
    public void closeProductAddScene() throws IOException {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        mainController.updateTables();
    }

    /**
     * Update Tables with Part/Associated Parts
     */
    public void updateTables(){
        partTableId.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));
        partTableName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partTableInventory.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));
        partTablePrice.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        partTableView.setItems(inventory.getAllParts());

        associatedPartTableId.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));
        associatedPartTableName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        associatedPartTableInventory.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));
        associatedPartTablePrice.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        associatedPartTableView.setItems(tempProduct.getAllAssociatedParts());
    }

    //PART ADD METHODS

    /**
     * Add the product to inventory and return to main screen
     * @throws IOException
     */
    public void addProduct() throws IOException {
        if (errorCheck()){
            //get Id count and increment by 1 for new ID
            tempProduct.setId(1);
            try {
                tempProduct.setId(inventory.getAllProducts().size()+1);
            }catch (Exception e){
                System.out.println(e);
            }
            tempProduct.setName(nameTextField.getText());
            tempProduct.setPrice(Double.parseDouble(priceTextField.getText()));
            tempProduct.setStock(Integer.parseInt(inventoryTextField.getText()));
            tempProduct.setMax(Integer.parseInt(maxTextField.getText()));
            tempProduct.setMin(Integer.parseInt(minTextField.getText()));
            inventory.addProduct(tempProduct);
            clearPartFields();
            closeProductAddScene();
        }

    }

    /**
     * Add Part to associated parts
     */
    public void addAssociatedPart(){
        Part part = (Part) partTableView.getSelectionModel().getSelectedItem();
        tempProduct.addAssociatedPart(part);
        updateTables();
    }

    /**
     * Search for parts
     * @param searchTerm
     */
    public void searchPart(String searchTerm){
        Part part = inventory.lookupPart(searchTerm);
        if(searchTerm == "" || part == null){
            partTableView.getSelectionModel().clearSelection();
            partTableView.scrollTo(0);
        }else{
            partTableView.getSelectionModel().select(part);
            partTableView.scrollTo(part);
        }
    }

    /**
     * Delete associated part
     */
    public void deletePart(){
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this?", ButtonType.YES, ButtonType.NO);
        confirmDelete.initOwner(removeAssociatedPartButton.getScene().getWindow());
        Part part = (Part) associatedPartTableView.getSelectionModel().getSelectedItem();
        confirmDelete.showAndWait();
        if(confirmDelete.getResult() == ButtonType.YES){
            tempProduct.deleteAssociatedPart(part);
            updateTables();
        }
    }

    /**
     * Error checking for input fields
     * @return
     */
    public boolean errorCheck(){
        Alert errorCheckAlert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        errorCheckAlert.initOwner(addButton.getScene().getWindow());
        if (!inventoryTextField.getText().matches("[0-9]+")){
            errorCheckAlert.setContentText("Inventory should only contain numbers");
            errorCheckAlert.showAndWait();
            return false;
        }
        if (!priceTextField.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")){
            errorCheckAlert.setContentText("Price should only contain numbers");
            errorCheckAlert.showAndWait();
            return false;
        }
        if(!maxTextField.getText().matches("[0-9]+") || !minTextField.getText().matches("[0-9]+")){
            errorCheckAlert.setContentText("Max and Min fields should only contain numbers");
            errorCheckAlert.showAndWait();
            return false;
        }
        if(Integer.parseInt(maxTextField.getText()) < Integer.parseInt(minTextField.getText())){
            errorCheckAlert.setContentText("Max cannot be less than min");
            errorCheckAlert.showAndWait();
            return false;
        }
        if(Integer.parseInt(inventoryTextField.getText()) > Integer.parseInt(maxTextField.getText()) || Integer.parseInt(inventoryTextField.getText()) < Integer.parseInt(minTextField.getText())){
            errorCheckAlert.setContentText("Stock must be between the min and max");
            errorCheckAlert.showAndWait();
            return false;
        }

        return true;
    }

    /**
     * Clear input fields
     */
    public void clearPartFields(){
        idTextField.setText("");
        nameTextField.setText("");
        priceTextField.setText("");
        inventoryTextField.setText("");
        minTextField.setText("");
        maxTextField.setText("");
    }

    /**
     * Main init method
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setOnAction(addPart);
        removeAssociatedPartButton.setOnAction(removePart);
        saveButton.setOnAction(saveProduct);
        cancelButton.setOnAction(closeWindow);
        mainPartTextField.textProperty().addListener(observable -> searchPart(mainPartTextField.getText()));
        updateTables();

    }

    /**
     * Customer init to pass references from main controller
     * @param mainController
     * @param inventory
     */
    public void customInit(MainController mainController, Inventory inventory){
        this.mainController = mainController;
        this.inventory = inventory;
    }
}
