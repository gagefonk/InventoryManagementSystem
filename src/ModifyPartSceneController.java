import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * This is the controller class for modifying parts.
 */
public class ModifyPartSceneController implements Initializable {

    //DECLARE VARIABLES
    @FXML private RadioButton inHouseRadio;
    @FXML private RadioButton outsourcedRadio;
    @FXML private Label machineIdLabel;
    @FXML private Label companyNameLabel;
    @FXML private TextField idTextField;
    @FXML private TextField nameTextField;
    @FXML private TextField inventoryTextField;
    @FXML private TextField priceTextField;
    @FXML private TextField maxTextField;
    @FXML private TextField minTextField;
    @FXML private TextField inHouseTextField;
    @FXML private TextField outsourcedTextField;
    @FXML private Button partCancelButton;
    @FXML private Button partSaveButton;
    Inventory inventory;
    MainController mainController;
    InHouse inhousePart;
    Outsourced outsourcedPart;

    //EVENT HANDLERS
    /**
     * Event Handler for changing form based off radio button selected
     */
    EventHandler<ActionEvent> changeForm = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            partSceneRadioUpdate();
        }
    };
    /**
     * Event Handler to save modified part
     */
    EventHandler<ActionEvent> saveForm = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                addPartSaveButtonAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     *Event Handler to close window
     */
    EventHandler<ActionEvent> closeWindow = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                closePartModifyScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };



    //PART ADD METHODS
    /**
     *Show form based on radio button selected
     */
    public void partSceneRadioUpdate(){
        if (inHouseRadio.isSelected()){
            companyNameLabel.setVisible(false);
            outsourcedTextField.setVisible(false);
            machineIdLabel.setVisible(true);
            inHouseTextField.setVisible(true);
            outsourcedTextField.setText("");
        }else if(outsourcedRadio.isSelected()){
            companyNameLabel.setVisible(true);
            outsourcedTextField.setVisible(true);
            machineIdLabel.setVisible(false);
            inHouseTextField.setVisible(false);
            inHouseTextField.setText("");
        }
    }

    /**
     * Update part and go back to main scene
     */
    public void addPartSaveButtonAction() throws IOException {
        if(errorCheck()){
            int id = Integer.parseInt(idTextField.getText());
            String name = nameTextField.getText();
            Double price = Double.parseDouble(priceTextField.getText());
            int stock = Integer.parseInt(inventoryTextField.getText());
            int min = Integer.parseInt(minTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());
            if(inHouseRadio.isSelected()){
                int machineID = Integer.parseInt(inHouseTextField.getText());
                InHouse part = new InHouse(id, name, price, stock, min, max, machineID);
                inventory.updatePart((id-1), part);
            }else if(outsourcedRadio.isSelected()){
                String companyName = outsourcedTextField.getText();
                Outsourced part = new Outsourced(id, name, price, stock, min, max, companyName);
                inventory.updatePart((id-1), part);
            }
            clearPartFields();
            closePartModifyScene();
        }

    }

    /**
     * Error checking for input fields
     */
    public boolean errorCheck(){
        Alert errorCheckAlert = new Alert(Alert.AlertType.WARNING, "", ButtonType.OK);
        errorCheckAlert.initOwner(partSaveButton.getScene().getWindow());
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
        if(!inHouseTextField.getText().matches("[0-9]+")){
            errorCheckAlert.setContentText("Machine ID should only contain numbers");
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
        inHouseTextField.setText("");
        outsourcedTextField.setText("");
    }

    /**
     * Update input fields for InHouse objects
     */
    public void updateInhouse(){
        inHouseRadio.setSelected(true);
        partSceneRadioUpdate();
        idTextField.setText(String.valueOf(inhousePart.getId()));
        nameTextField.setText(inhousePart.getName());
        inventoryTextField.setText(String.valueOf(inhousePart.getStock()));
        priceTextField.setText(String.valueOf(inhousePart.getPrice()));
        maxTextField.setText(String.valueOf(inhousePart.getMax()));
        minTextField.setText(String.valueOf(inhousePart.getMin()));
        inHouseTextField.setText(String.valueOf(inhousePart.getMachineId()));
    }

    /**
     * Update input fields for Outsourced objects
     */
    public void updateOutsourced(){
        outsourcedRadio.setSelected(true);
        partSceneRadioUpdate();
        idTextField.setText(String.valueOf(outsourcedPart.getId()));
        nameTextField.setText(outsourcedPart.getName());
        inventoryTextField.setText(String.valueOf(outsourcedPart.getStock()));
        priceTextField.setText(String.valueOf(outsourcedPart.getPrice()));
        maxTextField.setText(String.valueOf(outsourcedPart.getMax()));
        minTextField.setText(String.valueOf(outsourcedPart.getMin()));
        outsourcedTextField.setText(outsourcedPart.getCompanyName());
    }

    /**
     * Close window
     * @throws IOException
     */
    public void closePartModifyScene() throws IOException {
        Stage stage = (Stage) partCancelButton.getScene().getWindow();
        stage.close();
        mainController.updateTables();
    }

    /**
     * RUNTIME ERROR
     * Can't update both methods if one is null or else it throws an error, implemented
     * check for part null or not
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inHouseRadio.setOnAction(changeForm);
        outsourcedRadio.setOnAction(changeForm);
        partCancelButton.setOnAction(closeWindow);
        partSaveButton.setOnAction(saveForm);
        if(inhousePart != null){
            updateInhouse();
        }else if(outsourcedPart != null){
            updateOutsourced();
        }
    }

    /**
     * Custom init to pass references from main controller
     * @param mainController
     * @param inventory
     * @param part
     */
    public void customInit(MainController mainController, Inventory inventory, InHouse part){
        this.mainController = mainController;
        this.inventory = inventory;
        inhousePart = part;

    }
    /**
     * Custom init to pass references from main controller
     * @param mainController
     * @param inventory
     * @param part
     */
    public void customInit(MainController mainController, Inventory inventory, Outsourced part){
        this.mainController = mainController;
        this.inventory = inventory;
        outsourcedPart = part;

    }
}

