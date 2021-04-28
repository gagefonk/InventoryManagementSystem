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
 * CLASS RESPONSIBLE FOR ADDING A PART
 */
public class AddPartSceneController implements Initializable {

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



   //DECLARE EVENT HANDLERS

    /**
     * Event Handler to change the form based off which radio button was selected.
     */
    EventHandler<ActionEvent> changeForm = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            partSceneRadioUpdate();
        }
    };
    /**
     * Event Handler to save the part.
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
     * Event Handler to close the window.
     */
    EventHandler<ActionEvent> closeWindow = new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
            try {
                closePartAddScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * Update form based on which radio button is selected.
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
     * Save the part to inventory
     * @throws IOException
     */
    public void addPartSaveButtonAction() throws IOException {
        if (errorCheck()){
            //get Id count and increment by 1 for new ID
            int id = 1;
            try {
                id = inventory.getAllParts().size() + 1;
            }catch (Exception e){
                System.out.println(e);
            }
            String name = nameTextField.getText();
            Double price = Double.parseDouble(priceTextField.getText());
            int stock = Integer.parseInt(inventoryTextField.getText());
            int min = Integer.parseInt(minTextField.getText());
            int max = Integer.parseInt(maxTextField.getText());

            if(inHouseRadio.isSelected()){
                int machineID = Integer.parseInt(inHouseTextField.getText());
                InHouse part = new InHouse(id, name, price, stock, min, max, machineID);
                inventory.addPart(part);
            }else if(outsourcedRadio.isSelected()){
                String companyName = outsourcedTextField.getText();
                Outsourced part = new Outsourced(id, name, price, stock, min, max, companyName);
                inventory.addPart(part);
            }
            clearPartFields();
            closePartAddScene();
        }

    }

    /**
     * Error Check all fields
     * @return true/false
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
        if(!inHouseTextField.getText().matches("[0-9]+") && !outsourcedRadio.isSelected()){
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
     * Close the Window
     * @throws IOException
     */
    public void closePartAddScene() throws IOException {
        Stage stage = (Stage) partCancelButton.getScene().getWindow();
        stage.close();
        mainController.updateTables();

    }

    /**
     * Main init method
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inHouseRadio.setOnAction(changeForm);
        outsourcedRadio.setOnAction(changeForm);
        partCancelButton.setOnAction(closeWindow);
        partSaveButton.setOnAction(saveForm);

    }

    /**
     * Init by passing reference from main controller
     * @param mainController
     * @param inventory
     */
    public void customInit(MainController mainController, Inventory inventory){
        this.mainController = mainController;
        this.inventory = inventory;
    }
}

