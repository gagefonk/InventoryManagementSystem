import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the controller class for changing/updating all the FXML scenes and elements.
 * FUTURE ENHANCEMENT
 * FOR MOST MODIFY/DELETE METHODS - A LOGIC CLASS SHOULD BE IMPLEMENTED
 * THAT PASSES THE REFERENCE IN TO REDUCE REDUNDANT CODE
 */
public class MainController implements Initializable {

    //DECLARE VARIABLES
    Inventory inventory = new Inventory();
    @FXML private Button partsAddButton;
    @FXML private Button partsModifyButton;
    @FXML private Button partsDeleteButton;
    @FXML private Button productDeleteButton;
    @FXML private Button productAddButton;
    @FXML private Button productModifyButton;
    @FXML private TableColumn partTableId;
    @FXML private TableColumn partTableName;
    @FXML private TableColumn partTableInventory;
    @FXML private TableColumn partTablePrice;
    @FXML private TableColumn productTableId;
    @FXML private TableColumn productTableName;
    @FXML private TableColumn productTableInventory;
    @FXML private TableColumn productTablePrice;
    @FXML private TableView partTableView;
    @FXML private TableView productTableView;
    @FXML private TextField mainPartTextField;
    @FXML private TextField mainProductTextField;
    Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this?", ButtonType.YES, ButtonType.NO);


    //EVENT HANDLERS
    /**
     * Event Handler to open add part scene
     */
    EventHandler<ActionEvent> partAdd = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                showPartsAddScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * Event Handler to open modify part scene
     */
    EventHandler<ActionEvent> partModify = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                modifyPartTable();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * Event Handler to delete part
     */
    EventHandler<ActionEvent> partDelete = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            deletePart();
        }
    };
    /**
     * Event Handler to open product add scene
     */
    EventHandler<ActionEvent> productAdd = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                showProductAddScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * Event Handler to open product modify scene
     */
    EventHandler<ActionEvent> productModify = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                showProductModifyScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * Event Handler to delete product
     */
    EventHandler<ActionEvent> productDelete = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            deleteProduct();
        }
    };

    //UNIVERSAL METHODS

    /**
     * Close Application
     */
    public void closeApplication(){
        System.exit(0);
    }

    /**
     * Update table information
     */
    public void updateTables(){
        partTableId.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));
        partTableName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partTableInventory.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));
        partTablePrice.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        partTableView.setItems(inventory.getAllParts());

        productTableId.setCellValueFactory(new PropertyValueFactory<Part, String>("id"));
        productTableName.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        productTableInventory.setCellValueFactory(new PropertyValueFactory<Part, String>("stock"));
        productTablePrice.setCellValueFactory(new PropertyValueFactory<Part, String>("price"));
        productTableView.setItems(inventory.getAllProducts());
    }

    //MAIN METHODS

    /**
     * Show the part add scene
     */
    public void showPartsAddScene() throws IOException {
        AddPartSceneController controller = new AddPartSceneController();
        controller.customInit(this, inventory);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/PartAddScene.fxml"));
        loader.setController(controller);
        Stage stage = new Stage();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setAlwaysOnTop(true);
        stage.show();
    }
    /**
     * Show product add scene
     */
    public void showProductAddScene() throws IOException {
        AddProductSceneController controller = new AddProductSceneController();
        controller.customInit(this, inventory);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ProductAddScene.fxml"));
        loader.setController(controller);
        Stage stage = new Stage();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setAlwaysOnTop(true);
        stage.show();
    }

    /**
     * Show part modify scene
     */
    public void modifyPartTable() throws IOException {
        if(partTableView.getSelectionModel().getSelectedItem() != null){
            ModifyPartSceneController controller = new ModifyPartSceneController();
            if(partTableView.getSelectionModel().getSelectedItem().getClass().equals(InHouse.class)){
                InHouse part = (InHouse) partTableView.getSelectionModel().getSelectedItem();
                controller.customInit(this, inventory, part);
            }else if(partTableView.getSelectionModel().getSelectedItem().getClass().equals(Outsourced.class)){
                Outsourced part = (Outsourced) partTableView.getSelectionModel().getSelectedItem();
                controller.customInit(this, inventory, part);
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ModifyPartScene.fxml"));
            loader.setController(controller);
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.show();
        }
    }

    /**
     * Show product modify scene
     */
    public void showProductModifyScene() throws IOException {
        if(productTableView.getSelectionModel().getSelectedItem() != null){
            Product selectedProduct = (Product) productTableView.getSelectionModel().getSelectedItem();
            ModifyProductSceneController controller = new ModifyProductSceneController();
            controller.customInit(this, inventory, selectedProduct);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ModifyProductScene.fxml"));
            loader.setController(controller);
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.show();
        }
    }

    /**
     * Delete part from inventory
     */
    public void deletePart(){
        Part part = (Part) partTableView.getSelectionModel().getSelectedItem();
        confirmDelete.showAndWait();
        if(confirmDelete.getResult() == ButtonType.YES){
            inventory.deletePart(part);
            updateTables();
        }
    }
    /**
     * Delete product from inventory
     */
    public void deleteProduct(){
        Product product = (Product) productTableView.getSelectionModel().getSelectedItem();
        if(product.getAllAssociatedParts().isEmpty()){
            confirmDelete.showAndWait();
            if(confirmDelete.getResult() == ButtonType.YES){
                inventory.deleteProduct(product);
                updateTables();
            }
        }else{
            Alert errorCheckAlert = new Alert(Alert.AlertType.WARNING, "Cannot delete product with associated parts", ButtonType.OK);
            errorCheckAlert.initOwner(productDeleteButton.getScene().getWindow());
            errorCheckAlert.showAndWait();
        }

    }

    /**
     * Search through available parts
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
     * Search through available products
     */
    public void searchProduct(String searchTerm){
        Product product = inventory.lookupProduct(searchTerm);
        if(searchTerm == "" || product == null){
            productTableView.getSelectionModel().clearSelection();
            productTableView.scrollTo(0);
        }else{
            productTableView.getSelectionModel().select(product);
            productTableView.scrollTo(product);
        }
    }
    /**
     * Main init method
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsAddButton.setOnAction(partAdd);
        partsModifyButton.setOnAction(partModify);
        partsDeleteButton.setOnAction(partDelete);
        productAddButton.setOnAction(productAdd);
        productModifyButton.setOnAction(productModify);
        productDeleteButton.setOnAction(productDelete);
        mainPartTextField.textProperty().addListener(observable -> searchPart(mainPartTextField.getText()));
        mainProductTextField.textProperty().addListener(observable -> searchProduct(mainProductTextField.getText()));
        updateTables();
    }
}
