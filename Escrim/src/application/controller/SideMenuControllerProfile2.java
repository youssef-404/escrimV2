package application.controller;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class SideMenuControllerProfile2 {

	@FXML
    private Pane contentPane;

	@FXML
    private Button inventoryButton;

	@FXML
    private Button medicamentButton;
	@FXML
    private Button medicalSupplyButton;
	@FXML
    private Button patientButton;



    public void initialize() {
        setupButtonActions();
        inventoryButton.fire();
    }

    private void setupButtonActions() {
        inventoryButton.setOnAction(event -> switchPane("/application/view/inventoriesProfile2.fxml"));
        medicamentButton.setOnAction(event -> switchPane("/application/view/medicaments.fxml"));
        medicalSupplyButton.setOnAction(event -> switchPane("/application/view/medicalSupply.fxml"));
        patientButton.setOnAction(event -> switchPane("/application/view/patientProfile2.fxml"));
    }

    private void switchPane(String fxmlFile) {
        try {
            Node pane = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    


}

    
    
