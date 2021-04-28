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
 * CLASS RESPONSIBLE FOR MODIFYING A PRODUCT
 */
public class ModifyProductSceneController implements Initializable {

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
    Product selectedProduct;


    //EVENT HANDLERS
    /**
     * Add part to associated parts
     */
    EventHandler<ActionEvent> addPart = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            addAssociatedPart();
        }
    };
    /**
     * Remove associated part
     */
    EventHandler<ActionEvent> removePart = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            deletePart();
        }
    };
    /**
     * Update modified product
     */
    EventHandler<ActionEvent> saveProduct = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                updateProduct();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * Cancel and close window
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
     * Update table with part/associated part information
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
        associatedPartTableView.setItems(selectedProduct.getAllAssociatedParts());
    }

     // PRODUCT ADD METHODS

    /**
     * Save product and return to main scene
     * @throws IOException
     */
    public void updateProduct() throws IOException {
        if (errorCheck()){
            //get Id count and increment by 1 for new ID
            selectedProduct.setId(Integer.parseInt(idTextField.getText()));
            selectedProduct.setName(nameTextField.getText());
            selectedProduct.setPrice(Double.parseDouble(priceTextField.getText()));
            selectedProduct.setStock(Integer.parseInt(inventoryTextField.getText()));
            selectedProduct.setMax(Integer.parseInt(maxTextField.getText()));
            selectedProduct.setMin(Integer.parseInt(minTextField.getText()));
            inventory.updateProduct((selectedProduct.getId()-1), selectedProduct);
            clearPartFields();
            closeProductAddScene();
        }

    }

    /**
     * Add associated part
     */
    public void addAssociatedPart(){
        Part part = (Part) partTableView.getSelectionModel().getSelectedItem();
        selectedProduct.addAssociatedPart(part);
        updateTables();
    }

    /**
     * Search Parts
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
     * Delete part from associated list
     */
    public void deletePart(){
        Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this?", ButtonType.YES, ButtonType.NO);
        confirmDelete.initOwner(removeAssociatedPartButton.getScene().getWindow());
        Part part = (Part) associatedPartTableView.getSelectionModel().getSelectedItem();
        confirmDelete.showAndWait();
        if(confirmDelete.getResult() == ButtonType.YES){
            selectedProduct.deleteAssociatedPart(part);
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
     * Update input fields on init
     */
    public void updateFields(){
        idTextField.setText(String.valueOf(selectedProduct.getId()));
        nameTextField.setText(selectedProduct.getName());
        inventoryTextField.setText(String.valueOf(selectedProduct.getStock()));
        priceTextField.setText(String.valueOf(selectedProduct.getPrice()));
        maxTextField.setText(String.valueOf(selectedProduct.getMax()));
        minTextField.setText(String.valueOf(selectedProduct.getMin()));
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
        updateFields();

    }

    /**
     * Custom init to pass reference from main controller
     * @param mainController
     * @param inventory
     * @param selectedProduct
     */
    public void customInit(MainController mainController, Inventory inventory, Product selectedProduct){
        this.mainController = mainController;
        this.inventory = inventory;
        this.selectedProduct = selectedProduct;
    }
}
